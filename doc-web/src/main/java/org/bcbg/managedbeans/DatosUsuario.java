/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.UsuarioDocs;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.Persona;
import org.bcbg.entities.PersonaRH;
import org.bcbg.entities.User;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.Data;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DatosUsuario implements Serializable {

    private static final Logger LOG = Logger.getLogger(DatosUsuario.class.getName());

    @Inject
    protected UserSession us;
    @Inject
    protected ServletSession ss;
    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;
    protected User user;
    private String username = "";
    private String pass;
    private String passOne;
    private String passTwo;
    private String claveFirma;
    protected Boolean permitido = false;
    protected Integer tipo = 0;
    protected Date desde;
    protected Date hasta;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private PersonaRH persona;
    private FirmaElectronica firmaElectronica;
    private Boolean firmaValidada;
    private String imagen;
    private UploadedFile file;

    @PostConstruct
    protected void iniView() {
        try {
            user = (User) service.methodGET(SisVars.ws + "usuario/find?id=" + us.getUserId(), User.class);
            if (user == null) {
                JsfUti.redirectFaces("/");
            } else {
                claveFirma = "";
                firmaValidada = Boolean.FALSE;
                firmaElectronica = us.getFirmaElectronica();
                if (firmaElectronica == null) {
                    firmaElectronica = new FirmaElectronica();
                }
                username = user.getNombreUsuario();
                this.validaRoles();
                desde = sdf.parse(sdf.format(new Date()));
                hasta = sdf.parse(sdf.format(new Date()));
                if (user.getRecursoHumano() != null) {
                    persona = user.getRecursoHumano().getPersona();
                } else {
                    persona = new PersonaRH();
                }
            }
            //System.out.println("firma digital: " + firmaElectronica.getFirmaDigital());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        for (Long l : us.getRoles()) {
            if (l == 1) {
                permitido = true;
            }
        }
    }

    public void cerrarDialogo() {
        passOne = "";
        passTwo = "";
    }

    public void cambioClave() {
        try {
            if (passOne != null && passTwo != null && pass != null) {
                pass = Utils.encriptaEnMD5(pass);
                User u = new User(user.getId());
                u.setClave(pass);
                if (validaUser(u)) {
                    if (passOne.length() > 7) {
                        if (passOne.equals(passTwo)) {
                            user.setClave(passOne);
                            user = (User) service.methodPUT(user, SisVars.ws + "update/password/user", User.class);
                            if (user != null && user.getId() != null) {
                                AclLogin aclLogin = new AclLogin();
                                aclLogin.setEvento("Cambio de clave");
                                aclLogin.setIpUserSession(us.getAclLogin().getIpUserSession());
                                aclLogin.setUserSessionId(BigInteger.valueOf(user.getId()));
                                aclLogin.setUserSessionName(us.getName_user());
                                aclLogin.setMacClient(us.getAclLogin().getMacClient());
                                service.methodPOST(aclLogin, SisVars.ws + "create/aclLogin", AclLogin.class);
                                JsfUti.messageInfo(null, "Datos actualizados con éxito. Debe salir e iniciar sesion nuevamente para comprobar los cambios.", "");
                                JsfUti.executeJS("PF('dlgChangePass').hide();");
                                JsfUti.update("mainForm");
                            } else {
                                JsfUti.messageError(null, Messages.problemaConexion, "");
                            }
                        } else {
                            JsfUti.messageError(null, Messages.noCoincidenClaves, "");
                        }
                    } else {
                        JsfUti.messageError(null, "La clave debe tener minimo 8 caracteres.", "");
                    }
                } else {
                    JsfUti.messageError(null, "La clave actual ingresada no coincide.", "");
                }
            } else {
                JsfUti.messageError(null, Messages.faltanCampos, "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validaUser(User u) {
        Data data = (Data) service.methodPOST(u, SisVars.ws + "create/validate", Data.class);
        return data != null;
    }

    public void upload(FileUploadEvent file) {
        try {
            this.file = file.getFile();
            String name = Utils.getValorUuid();
            FilesUtil.copyFileServerHidden(file.getFile(), SisVars.rutaFirmasElectronicas, name);
            firmaElectronica.setArchivo(name);
        } catch (IOException e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo de la Firma Electrónica", "");
        }
    }

    public void validarFirmaElectronica() {
        if (firmaElectronica.getArchivo() != null && claveFirma != null && !claveFirma.isEmpty()) {
            firmaElectronica.setClave(claveFirma);
            try {
                FirmaElectronica firmaElectronicaValidar = new FirmaElectronica();
                firmaElectronicaValidar.setArchivo(firmaElectronica.getArchivo());
                firmaElectronicaValidar.setClave(claveFirma);
                firmaElectronicaValidar = (FirmaElectronica) service.methodPOST(firmaElectronicaValidar, SisVars.wsFirmaEC + "firmaElectronica/validar", FirmaElectronica.class);
                if (firmaElectronicaValidar.getUid() != null) {
                    firmaValidada = Boolean.TRUE;
                    firmaElectronica.setUbicacion(SisVars.ubicacion);
                    firmaElectronica.setUid(firmaElectronicaValidar.getUid());
                    firmaElectronica.setEstadoFirma(firmaElectronicaValidar.getEstadoFirma());
                    firmaElectronica.setCn(firmaElectronicaValidar.getCn());
                    firmaElectronica.setEmision(firmaElectronicaValidar.getEmision());
                    firmaElectronica.setFechaEmision(firmaElectronicaValidar.getFechaEmision());
                    firmaElectronica.setFechaExpiracion(firmaElectronicaValidar.getFechaExpiracion());
                    firmaElectronica.setIsuser(firmaElectronicaValidar.getIsuser());
                    firmaElectronica.setTipoFirma("QR");
                    firmaElectronica.setFirmaCaducada(firmaElectronicaValidar.getFirmaCaducada());
                    firmaElectronica.setEstadofirmaCaducada(firmaElectronicaValidar.getEstadofirmaCaducada());
                    JsfUti.messageInfo(null, "Firma electrónica validada", "");
                } else {
                    firmaValidada = Boolean.FALSE;
                    JsfUti.messageError(null, "No se pudo validar su firma electrónica", "Intente nuevamente");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsfUti.messageError(null, "Debe subir el archivo P12 y escribir su clave", "");
        }

    }

    public void guardarActualizarFirmaElectronica() {
        if (firmaValidada) {
            firmaElectronica.setEstado(Boolean.TRUE);
            if (firmaElectronica.getId() == null) {
                firmaElectronica.setClave(null);
                firmaElectronica.setFechaCreacion(new Date());
                firmaElectronica.setUsuario(us.getUsuarioDocs());
                firmaElectronica = (FirmaElectronica) service.methodPOST(firmaElectronica, SisVars.wsDocs + "firmaElectronicas/guardar", FirmaElectronica.class);
                us.setFirmaElectronica(firmaElectronica);
            } else {
                firmaElectronica = (FirmaElectronica) service.methodPUT(firmaElectronica, SisVars.wsDocs + "firmaElectronicas/actualizar", FirmaElectronica.class);
            }
            JsfUti.messageInfo(null, "Datos actualizados correctamente", "");
            firmaValidada = Boolean.FALSE;
        } else {
            JsfUti.messageError(null, "Debe validar la firma para poder editarla", "");
        }
    }

    public void descargarModelo(SelectEvent event) {
        FirmaElectronica fe = (FirmaElectronica) event.getObject();
        if (fe != null) {
            System.out.println("Filter " + fe.getArchivoFirmado());
//            JsfUti.redirectNewTab(SisVars.wsMedia + "resource/pdf/" + Utils.getFilterRuta(fe.getArchivoFirmado()) + "/descarga/" + SisVars.DOWNLOAD_DOC);
            JsfUti.redirectNewTab(SisVars.urlbase + Utils.getFilterRuta(fe.getArchivoFirmado()) + "/descarga/" + SisVars.DOWNLOAD_DOC);
        } else {
            JsfUti.messageError(null, "Error al firmar electrónicamente", "Intente nuevamente");
        }
    }

//    public void archivoFirmado(SelectEvent event) {
//        try {
//            FirmaElectronica fe = (FirmaElectronica) event.getObject();
//            if (fe != null) {
//                File f = new File(fe.getArchivoFirmar());
//                if (f.exists()) {
//                    f.delete();
//                }
//                //  agregarDocumento(Boolean.TRUE, fe.getArchivoFirmado());
//            } else {
//                JsfUti.messageError(null, "Error al firmar electrónicamente", "Intente nuevamente");
//            }
//            // streamedContent = createStream(fe.getArchivoFirmado());
//            // /servers_files/archivos/1609864384530_
//            ss.setNombreDocumento(fe.getArchivoFirmado().substring(38));
//            ss.setUrlWebService(fe.getUrlArchivoFirmado());
//            PrimeFaces.current().ajax().update("formMain");
//            JsfUti.redirectNewTab(fe.getUrlArchivoFirmado());
//        } catch (Exception e) {
//            System.out.println("exception archivoFirmado " + e.getMessage());
//        }
//    }
    public void firmaDigital() {
        try {
            String imgBase64 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imgBase64");
            if (imgBase64 != null) {
                firmaElectronica.setFirmaDigital(imgBase64);
            }
        } catch (Exception ex) {
            Logger.getLogger(DatosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
//            user.setImagenPerfil(SisVars.wsMedia + "resource/image/" + f.getName());
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateUser() {
        if (persona != null && persona.getId() != null) {
            service.methodPUT(persona, SisVars.ws + "personas/actualizar", Persona.class);
        }
        user = (User) service.methodPUT(user, SisVars.ws + "update/user", User.class);
        if (confirmUser()) {
            us.setUrlImagen("");
            username = "";
            username = user.getNombreUsuario();
            persona = user.getRecursoHumano().getPersona();
            us.setName_user(user.getNombreUsuario());
//            us.setUrlImagen(user.getImagenPerfil() != null ? user.getImagenPerfil() : "");
            JsfUti.messageInfo(null, "Usuario actualizado correctamente", "");
            JsfUti.executeJS("PF('dlgChangeData').hide()");
            JsfUti.update("mainForm");
        } else {
            JsfUti.messageInfo(null, "Error al actualizar, intente nuevamente", "");
        }
    }

    public void cancelar() {
        user = (User) service.methodGET(SisVars.ws + "usuario/find?id=" + us.getUserId(), User.class);
        if (confirmUser()) {
            username = user.getNombreUsuario();
            if (user.getRecursoHumano() != null) {
                persona = user.getRecursoHumano().getPersona();
            }
        }
    }

    private Boolean confirmUser() {
        return user != null && user.getId() != null;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassOne() {
        return passOne;
    }

    public void setPassOne(String passOne) {
        this.passOne = passOne;
    }

    public String getPassTwo() {
        return passTwo;
    }

    public void setPassTwo(String passTwo) {
        this.passTwo = passTwo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public PersonaRH getPersona() {
        return persona;
    }

    public void setPersona(PersonaRH persona) {
        this.persona = persona;
    }

    public Boolean getFirmaValidada() {
        return firmaValidada;
    }

    public void setFirmaValidada(Boolean firmaValidada) {
        this.firmaValidada = firmaValidada;
    }

    public String getClaveFirma() {
        return claveFirma;
    }

    public void setClaveFirma(String claveFirma) {
        this.claveFirma = claveFirma;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
//</editor-fold>

}
