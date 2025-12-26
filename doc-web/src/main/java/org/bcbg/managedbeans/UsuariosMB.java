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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.RandomStringUtils;
import org.bcbg.config.SisVars;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.AclRegistroUser;
import org.bcbg.entities.CtlgCatalogo;
import org.bcbg.entities.CtlgItem;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.Dominio;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Rol;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class UsuariosMB implements Serializable {

    @Inject
    protected UserSession us;
    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;

    private LazyModelWS<User> usuarios;
    private User acluser;
    private Rol rol;
    private Persona ente;
    private List<Rol> rolsUser;
    private List<RolUsuario> rolUsuarioDelet;
    private List<RolUsuario> rolesUsuario;
    private List<AclRegistroUser> aclRegistroUsers;
    private String passOne;
    private String passTwo;
    private String cedula;
    private String usuario;
    private String motivo;

    private String palabra;
    private String cambio;

    private LazyModelWS<Rol> roles;
    private LazyModelWS<Departamento> depts;
    private Departamento dept;
    private String imagen;
    private List<CtlgItem> tipoUnidades;
    protected TreeNode root;
    private List<CtlgItem> listaCatalogoItemTipos;
    private CtlgItem tipoCatalogo;
    private CtlgCatalogo catalogo;
    private List<Dominio> dominios;
    private String dominio;
    private Boolean renderedDominio;
    private Boolean isFormEdit;
    private int size;

    @PostConstruct
    protected void iniView() {

        loadLazyUsuario();
        loadLazyRoles();
        loadLazyDepartamentos();
        inicializarVariables();
        listaCatalogoItemTipos = new ArrayList<>();
        catalogo = new CtlgCatalogo();
        System.out.println("us " + us.getToken());
        this.loadDominios();
    }

    private void loadLazyUsuario() {
        usuarios = new LazyModelWS<>(SisVars.ws + "usuarios", User[].class, us.getToken());
    }

    private void loadLazyRoles() {
        roles = new LazyModelWS<>(SisVars.ws + "roles?sort=nombre", us.getToken());
        roles.setEntitiArray(Rol[].class);
    }

    private void loadLazyDepartamentos() {
        depts = new LazyModelWS<>(SisVars.ws + "departamentos?sort=nombre", us.getToken());
        depts.setEntitiArray(Departamento[].class);
        tipoUnidades = new ArrayList<>();

    }

    public void selectUsuario(User u) {
        PrimeFaces.current().dialog().closeDynamic(u);
    }

    public void inicializarVariables() {
        acluser = new User();
        rolUsuarioDelet = new ArrayList<>();
        ente = new Persona();
        rolsUser = new ArrayList<>();
        passOne = "";
        passTwo = "";
        cedula = "";
        usuario = "";
        rol = new Rol();
        dept = new Departamento();
    }

    public void showDlgConsulta(User us, String urlFacelet) {
        acluser = appServices.getUsuario(new User(us.getId()));
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("closable", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public Void selectData(SelectEvent event) {
        Persona en = (Persona) event.getObject();
        if (en != null) {
            System.out.println("/////event " + en.getId());
            if (acluser.getId() != null) {
//                acluser.setEnte(en);
                System.out.println("////ente " + acluser.toString());
                User u = (User) service.methodPUT(acluser, SisVars.ws + "update/user", User.class);
                if (u.getId() != null) {
                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                }
                loadLazyUsuario();
            }
        }
        return null;
    }

    public void showDlgNewUser() {
        this.inicializarVariables();
        this.existeCorreo();
        JsfUti.update("frmNewUsr");
        JsfUti.executeJS("PF('dlgNUsr').show();");
    }

    public void enviarCorreosActivarUsuarios() {
        service.methodPOST(SisVars.ws + "activarUsuarios/enviarCorreo", User[].class);
        JsfUti.messageInfo(null, "", "Se realizó el envío de activación de usuario.");
    }

    public void showDlgEditUser(User u) {
        acluser = appServices.getUsuario(new User(u.getId()));
        passOne = "";
        passTwo = "";
        JsfUti.update("formCambioClave");
        JsfUti.executeJS("PF('dlgCambioClave').show();");
    }

    public void showDlgRegistroUser(User u) {
        acluser = appServices.getUsuario(new User(u.getId()));
        JsfUti.update("formEstadoUser");
        JsfUti.executeJS("PF('dlgEstadoUser').show();");
    }


    public void nuevaClave() {
        passOne = RandomStringUtils.randomAlphanumeric(10);
        passTwo = passOne;
        System.out.println("passTwo: " + passTwo);
    }

    public void cambioClave() {
        try {
            if (acluser.getNombreUsuario() != null) {
                if (Utils.isEmpty(passOne).length() != 0 && Utils.isEmpty(passTwo).length() != 0) {
                    acluser.setClave(passOne);
//                    acluser.setCaducadaPass(Boolean.TRUE);
                    service.methodPUT(acluser, SisVars.ws + "update/password/user", User.class);
                    loadLazyUsuario();
                    AclLogin aclLogin = new AclLogin();
                    aclLogin.setEvento("Cambio de clave");
                    aclLogin.setIpUserSession(us.getAclLogin().getIpUserSession());
                    aclLogin.setUserSessionId(BigInteger.valueOf(acluser.getId()));
                    aclLogin.setUserSessionName(us.getName_user());
                    aclLogin.setMacClient(us.getAclLogin().getMacClient());
                    service.methodPOST(aclLogin, SisVars.ws + "create/aclLogin", AclLogin.class);
//                    Correo correo = new Correo(acluser.getEnte().getCorreo(), "Restablecimiento de Contraseña",
//                            Utils.mailHtmlNotificacion("Restablecimiento de Contraseña", "<strong>Estimado(a)</strong><br><strong> " + acluser.getEnte().getNombreCompleto() + "</strong>"
//                                    + " usuario: " + acluser.getEnte().getIdentificacion() + " con clave temporal es: " + passOne + "</strong><br>Favor de cambiar su contraseña.", "<strong>Gracias por la Atención Brindada</strong>",
//                                    "Este correo fue enviado de forma automática y no requiere respuesta."), null);
//                    appServices.enviarCorreo(correo);
                    this.inicializarVariables();
                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                    JsfUti.executeJS("PF('dlgCambioClave').hide();");
                    JsfUti.update("formUsuarios");

                } else {
                    JsfUti.messageError(null, "Debe genarar la nueva clave", "");
                }
            } else {
                JsfUti.messageError(null, Messages.faltanCampos, "");
            }
        } catch (Exception e) {
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditarRoles(User u,Boolean isEdit) {
        this.isFormEdit = isEdit;
        this.inicializarVariables();
        acluser = u;
        llenarRoles();

        JsfUti.update("dlgCambioRoles");
        JsfUti.executeJS("PF('dlgRoles').show();");
    }

    private void llenarRoles() {
        rolsUser = new ArrayList<>();
        RolUsuario r = new RolUsuario();
        r.setUsuario(acluser.getId());
        List<RolUsuario> rolU = appServices.getRolesUsuarios(r);
        if (!rolU.isEmpty()) {
            rolU.forEach(ru -> {
                rolsUser.add(ru.getRol());
            });
        }
    }

    public void agregarRol() {
        if (rol == null) {
            JsfUti.messageError(null, Messages.campoVacio, "");
        } else {
            if (rolsUser.contains(rol)) {
                JsfUti.messageInfo(null, Messages.elementoRepetido, "");
            } else {
                rolsUser.add(rol);
                rol = new Rol();
            }
        }
    }

    public void eliminarRol(Rol r) {
        rolsUser.remove(r);
        RolUsuario rolDelete = new RolUsuario(acluser.getId(), r);
        rolUsuarioDelet.add(rolDelete);
    }

    public void editarRoles() {
        try {
            if (rolsUser.isEmpty()) {
                JsfUti.messageInfo(null, Messages.noListaVacia, "");
            } else {
                eliminarRolesUsuario();
                service.methodListPOST(initRolesUsuario(), SisVars.ws + "roles/usuario/save", RolUsuario[].class);
                loadLazyUsuario();
                this.inicializarVariables();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
                JsfUti.executeJS("PF('dlgRoles').hide();");
                JsfUti.update("formUsuarios");
            }
        } catch (Exception e) {
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private List<RolUsuario> initRolesUsuario() {
        List<RolUsuario> rolU = new ArrayList<>();
        rolsUser.stream().map(r -> new RolUsuario(acluser.getId(), r)).forEachOrdered(ru -> {
            rolU.add(ru);
        });
        return rolU;
    }

    private void eliminarRolesUsuario() {
        if (!rolUsuarioDelet.isEmpty()) {
            service.methodListPUT(rolUsuarioDelet, SisVars.ws + "roles/usuario/delete", RolUsuario[].class);
        }
    }

    public void showDlgDetalleUser(User u) {
//        if (u.getEnte() == null) {
//            JsfUti.messageError(null, Messages.noAsignadoPersona, "");
//        } else {
//            this.inicializarVariables();
////            ente = u.getEnte();
//            JsfUti.update("formDetalleCliente");
//            JsfUti.executeJS("PF('dlgDetalleClient').show();");
//        }
    }

    public void buscarEnte() {
        if (cedula != null && !cedula.isEmpty()) {
            ente = appServices.buscarCatEnte(new Persona(cedula));
            if (ente == null) {
                ente = new Persona();
                usuario = "";
                JsfUti.messageError(null, Messages.sinCoincidencias, "");
            } else {
                if (ente.getNombres() != null) {
                    usuario = ente.getIdentificacion();
                } else {
                    usuario = "";
                }
            }
            this.existeCorreo();
        } else {
            usuario = "";
            JsfUti.messageError(null, Messages.campoVacio, "");
        }
    }

    public Boolean userDisponible() {
        return appServices.getUsuario(new User(usuario, null)) == null;
    }

    public void comprobarUsuario() {
        if (usuario != null) {
            if (this.userDisponible()) {
                JsfUti.messageInfo(null, Messages.userDisponible, "");
            } else {
                JsfUti.messageError(null, Messages.enteExiste, "");
            }
        } else {
            JsfUti.messageError(null, Messages.campoVacio, "");
        }
    }

    public void guardarNuevoUser() {
        try {
            if (ente.getId() != null) {
                if (usuario != null) {
                    if (this.userDisponible()) {
                        if (!rolsUser.isEmpty()) {
                            if (isNoEmptyCorreo()) {
//                                acluser.setUser(usuario);
//                                acluser.setEnte(ente);
//                                //SE ENVIA FALSE PARA LA ACTIVACION DEL USUARIO VIA CORREO
//                                acluser.setSisEnabled(Boolean.FALSE);
//                                acluser.setCaducadaPass(Boolean.TRUE);
//                                acluser.setEsSuperUser(Boolean.FALSE);
//                                acluser.setUserIsDirector(Boolean.FALSE);
                                acluser = (User) service.methodPOST(acluser, SisVars.ws + "create/user", User.class);
                                if (acluser != null && acluser.getId() != null) {
                                    //guardando los roles del usuario 
                                    service.methodListPOST(initRolesUsuario(), SisVars.ws + "roles/usuario/save", RolUsuario[].class);
                                    loadLazyUsuario();
                                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                                    JsfUti.update("formUsuarios");
                                    JsfUti.executeJS("PF('dlgNUsr').hide();");
//                                    JsfUti.update("fmrInfoUserPass");
//                                    JsfUti.executeJS("PF('dlgInfoUserPass').show();");
                                } else {
                                    JsfUti.messageError(null, Messages.problemaConexion, "");
                                }
                            }
                        } else {
                            JsfUti.messageInfo(null, "El usuario debe tener ingresado mínimo un Rol.", "");
                        }
                    } else {
                        JsfUti.messageError(null, Messages.enteExiste, "");
                    }
                } else {
                    JsfUti.messageError(null, "Debe ingresar el nombre de Usuario.", "");
                }
            } else {
                JsfUti.messageError(null, Messages.noAsignadoPersona, "");
            }
        } catch (Exception e) {
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Boolean isNoEmptyCorreo() {
        if (renderedDominio) {
            if (ente.getCorreo() != null) {
                ente.setCorreo(ente.getCorreo().concat(dominio));
            }
        }
        if (ente.getCorreo() != null && !ente.getCorreo().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo())) {
            JsfUti.messageWarning(null, "Dato del campo Correo no es valido.", "");
            return false;
        }
        return true;
    }

    public void showDlgRol() {
        rol = new Rol();
        JsfUti.update("formRolsUser");
        JsfUti.executeJS("PF('dlgRolUser').show();");
    }

    public void showDlgEditRol(Rol ro) {
        rol = ro;
        JsfUti.update("formRolsUser");
        JsfUti.executeJS("PF('dlgRolUser').show();");
    }

    public List<Departamento> getDepartamentos() {
        return appServices.getListDepartamentos();
    }

    public void saveRoles() {
        try {
            if (rol == null) {
                JsfUti.messageWarning(null, "Ingrese el rol.", "");
                return;
            }
            if (rol.getNombre() == null) {
                JsfUti.messageWarning(null, "Ingrese el nombre del rol.", "");
                return;
            }
            if (rol.getDepartamento() == null) {
                JsfUti.messageWarning(null, "Ingrese el departamento", "");
                return;
            }
            Rol r = (Rol) service.methodPOST(rol, SisVars.ws + "rol/save", Rol.class);
            if (r.getId() != null) {
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            } else {
                JsfUti.messageError(null, Messages.transacError, "");
                return;
            }
            loadLazyRoles();
            JsfUti.update("formUsuarios:accMantenimiento:dtRoles");
            JsfUti.executeJS("PF('dlgRolUser').hide();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
//            dept.setUrlImagen(SisVars.wsMedia + "resource/image/" + f.getName());
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirdlogoCatalogo() {
        tipoCatalogo = new CtlgItem();
        catalogo = new CtlgCatalogo();
        catalogo = appServices.finByCatalogo("tipo_unidad");
        listaCatalogoItemTipos = appServices.finByCatalogoItem("tipo_unidad");
        JsfUti.update("formTiposUnidades");
        JsfUti.executeJS("PF('dlgTipoUnidades').show();");
    }

    public void cargarEdicion(CtlgItem c) {
        tipoCatalogo = new CtlgItem();
        tipoCatalogo = c;
        JsfUti.update("formTiposUnidades");
    }

    public void registrarTipoDepartamento() {

        if (tipoCatalogo.getCodename().isEmpty() || tipoCatalogo.getEstado().isEmpty() || tipoCatalogo.getValor().isEmpty()) {
            JsfUti.messageWarning("", "Los Campos no puede estar vacios", "");
            return;
        }
        tipoCatalogo.setCatalogo(catalogo);
        tipoCatalogo = appServices.guardarItme(tipoCatalogo);
        JsfUti.messageInfo(null, Messages.transaccionOK, "");
        tipoCatalogo = new CtlgItem();
        listaCatalogoItemTipos = new ArrayList<>();
        listaCatalogoItemTipos = appServices.finByCatalogoItem("tipo_unidad");
    }

    public void loadDominios() {
        dominios = service.methodListGET(SisVars.ws + "dominio/findAll", Dominio[].class);
    }

    public void existeCorreo() {
        if (ente.getCorreo() == null || ente.getCorreo().isEmpty()) {
            size = 20;
            renderedDominio = Boolean.TRUE;
            return;
        }
        renderedDominio = Boolean.FALSE;
        size = 30;
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<AclRegistroUser> getAclRegistroUsers() {
        return aclRegistroUsers;
    }

    public void setAclRegistroUsers(List<AclRegistroUser> aclRegistroUsers) {
        this.aclRegistroUsers = aclRegistroUsers;
    }

    public List<RolUsuario> getRolesUsuario() {
        return rolesUsuario;
    }

    public void setRolesUsuario(List<RolUsuario> rolesUsuario) {
        this.rolesUsuario = rolesUsuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public LazyModelWS<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(LazyModelWS<User> usuarios) {
        this.usuarios = usuarios;
    }

    public User getAcluser() {
        return acluser;
    }

    public void setAcluser(User acluser) {
        this.acluser = acluser;
    }

    public Persona getEnte() {
        return ente;
    }

    public void setEnte(Persona ente) {
        this.ente = ente;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getListRols() {
        return appServices.getListRolsActivos();
    }

    public List<Rol> getRolsUser() {
        return rolsUser;
    }

    public void setRolsUser(List<Rol> rolsUser) {
        this.rolsUser = rolsUser;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

//    public AclUsersLazy getAllUsers() {
//        return allUsers;
//    }
//
//    public void setAllUsers(AclUsersLazy allUsers) {
//        this.allUsers = allUsers;
//    }
    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public LazyModelWS<Rol> getRoles() {
        return roles;
    }

    public void setRoles(LazyModelWS<Rol> roles) {
        this.roles = roles;
    }

    public LazyModelWS<Departamento> getDepts() {
        return depts;
    }

    public void setDepts(LazyModelWS<Departamento> depts) {
        this.depts = depts;
    }

    public Departamento getDept() {
        return dept;
    }

    public void setDept(Departamento dept) {
        this.dept = dept;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<CtlgItem> getTipoUnidades() {
        return tipoUnidades;
    }

    public void setTipoUnidades(List<CtlgItem> tipoUnidades) {
        this.tipoUnidades = tipoUnidades;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

//</editor-fold>
    public List<CtlgItem> getListaCatalogoItemTipos() {
        return listaCatalogoItemTipos;
    }

    public void setListaCatalogoItemTipos(List<CtlgItem> listaCatalogoItemTipos) {
        this.listaCatalogoItemTipos = listaCatalogoItemTipos;
    }

    public List<Dominio> getDominios() {
        return dominios;
    }

    public void setDominios(List<Dominio> dominios) {
        this.dominios = dominios;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public Boolean getRenderedDominio() {
        return renderedDominio;
    }

    public void setRenderedDominio(Boolean renderedDominio) {
        this.renderedDominio = renderedDominio;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    public CtlgItem getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(CtlgItem tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public Boolean getIsFormEdit() {
        return isFormEdit;
    }

    public void setIsFormEdit(Boolean isFormEdit) {
        this.isFormEdit = isFormEdit;
    }
 
}
