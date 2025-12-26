/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Dominio;
import org.bcbg.entities.Persona;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.VerCedulaUtils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class AdministradorEnte implements Serializable {

    private static final Logger LOG = Logger.getLogger(AdministradorEnte.class.getName());

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;

    @Inject
    private UserSession us;

    protected VerCedulaUtils vcu;
    private LazyModelWS<Persona> cel;
    protected Persona ente, enteDinardap;
    protected Boolean persona = true;
    protected String cedula = "";
    protected String dominio = "";
    protected String nombre = "";
    protected String razonsocial = "";
    private String tipoDocumento = "C";
    private Boolean consultaDinardap = Boolean.FALSE;
    private List<Dominio> dominios;
    private int size;
    private String correo;

    @PostConstruct
    protected void iniView() {
        try {
            loadLazy();
            ente = new Persona();
            enteDinardap = new Persona();
            correo = "";
            dominio = "@outlook.com";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void loadLazy() {
        cel = new LazyModelWS<>(SisVars.ws + "personas/find", us.getToken());
        cel.setEntitiArray(Persona[].class);
    }

    public void showDlgNewEnte() {
        ente = new Persona();
        enteDinardap = new Persona();
        correo = "";
        this.loadDominios();
        JsfUti.executeJS("PF('dlgNewCliente').show();");
        JsfUti.update("formNewEnte");
    }

    public void showDlgEditEnte(Persona catente) {
        ente = catente;
        tipoDocumento = ente.getTipoIdentificacion();
        this.loadDominios();
        correoConDominio();
        JsfUti.executeJS("PF('dlgEditEnte').show();");
        JsfUti.update("formEditEnte");
    }

    public void loadDominios() {
        dominios = service.methodListGET(SisVars.ws + "dominio/findAll", Dominio[].class);
    }

    public void updateTipoDoc() {
        if (!ente.getEsPersona()) {
            tipoDocumento = "R";
        } else {
            tipoDocumento = "C";
        }
    }

    public String regEx() {
        switch (tipoDocumento) {
            case "C":
            case "R":
                return "/[0-9]+/";
            case "P":
                return "/[0-9-a-zA-Z\\s\\ ]+/";
        }
        return "";
    }

    public void editEnte() {
        try {
            if (this.validateNames()) {
                if (!this.validacionCorreo()) {
                    return;
                }
                this.namesToUpper();
                ente.setTipoIdentificacion(tipoDocumento);
                ente.setUserMod(us.getName_user());
                ente.setFechaMod(new Date());
                //em.update(ente);
                appServices.registrarActualizarCatEnte(ente);
                loadLazy();
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgEditEnte').hide();");
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o razon social.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validacionCedula() {
        try {
            if (ente.getIdentificacion() != null) {
                if (this.existeEnte(ente.getIdentificacion())) {
                    JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                } else {
                    if (ente.getEsPersona()) {
                        //this.buscarDatoSeguro();
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validacionCorreo() {
        if (correo != null && !correo.isEmpty()) {
            ente.setCorreo(correo.concat(dominio));
            System.out.println("correo>> " + ente.getCorreo());
            if (!Utils.validarEmailConExpresion(ente.getCorreo())) {
                JsfUti.messageWarning(null, "Dato del campo Correo no es valido.", "");
                return false;
            }
        } else {
            JsfUti.messageWarning(null, "Ingrese un correo electrónico.", "");
            return false;
        }
        return true;
    }

    public Boolean existeEnte(String ciruc) {
        if (ente != null && ente.getId() != null) {
            return Boolean.FALSE;
        }
        Persona temp = appServices.buscarCatEnte(new Persona(ciruc));
        LOG.log(Level.SEVERE, "temp!=null? {0}", temp != null);
        return temp != null;
    }

    public boolean validateNames() {
        if (ente.getEsPersona()) {
            if (ente.getNombres().isEmpty() || ente.getApellidos().isEmpty()) {
                JsfUti.messageWarning(null, "Nombre - Apellido requerido", "");
                return false;
            }
            if (ente.getSexo() == null) {
                JsfUti.messageWarning(null, "Sexo Requerido", "");
                return false;
            }
        } else {
            if (ente.getRazonSocial().isEmpty()) {
                JsfUti.messageWarning(null, "Razón Social requerida", "");
                return false;
            }
        }
        return true;
    }

    public void namesToUpper() {
        if (ente.getNombres() != null) {
            ente.setNombres(ente.getNombres().toUpperCase());
        }
        if (ente.getApellidos() != null) {
            ente.setApellidos(ente.getApellidos().toUpperCase());
        }
        if (ente.getRazonSocial() != null) {
            ente.setRazonSocial(ente.getRazonSocial().toUpperCase());
        }
        if (ente.getNombreComercial() != null) {
            ente.setRazonSocial(ente.getNombreComercial().toUpperCase());
        }
    }

    /*
     * SOLO FUNCIONA PARA PERSONAS NATURALES y JURIDICAS =( </3
     */
    public void buscarDatoSeguro() {
        try {
            if (ente.getIdentificacion() != null && !ente.getIdentificacion().isEmpty()) {
                //enteDinardap = appServices.buscarGuardarEnteDinardap(ente.getCiRuc());
                enteDinardap = appServices.buscarCatEnte(new Persona(ente.getIdentificacion()));
                if (enteDinardap != null) {
                    ente = enteDinardap;
                    ente.setTipoIdentificacion(tipoDocumento);
                    JsfUti.messageInfo(null, "Datos encontrados", "");
                } else {
                    cedula = ente.getIdentificacion();
                    ente = new Persona();
                    ente.setIdentificacion(cedula);
                    JsfUti.messageError(null, "No se encontraron Resultados para: " + cedula, "");
                }
                correoConDominio();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, "MENSAJE DE ERROR", "ERROR DE APLICACION!!!");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void correoConDominio() {
        if (ente.getCorreo() != null && !ente.getCorreo().isEmpty()) {
            String[] dom = ente.getCorreo().split("@");
            correo = dom[0];
            dominio = "@" + dom[1];
        }
    }

    public void saveEnte() {
        try {
            if (this.validateNames()) {
                if (!this.validacionCorreo()) {
                    return;
                }
                this.namesToUpper();
                ente.setUserCre(us.getName_user());
                ente.setFechaCre(new Date());
                ente.setTipoIdentificacion(tipoDocumento);
                if (ente.getIdentificacion().isEmpty()) {
                    ente.setExcepcional(true);
                    this.confirmacion();
                } else if (this.existeEnte(ente.getIdentificacion())) {
                    JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                } else {
                    vcu = new VerCedulaUtils();
                    switch (tipoDocumento) {
                        case "P":
                            updateEnte();
                            break;
                        case "C":
                            if (consultaDinardap) {
                                updateEnte();
                            } else if (vcu.isCIValida(ente.getIdentificacion())) {
                                appServices.registrarActualizarCatEnte(ente);
                                this.confirmacion();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                        case "R":
                            if (consultaDinardap) {
                                updateEnte();
                            } else if (vcu.isRucValido(ente.getIdentificacion())) {
                                updateEnte();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o sexo o razon social.", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void updateEnte() {
        appServices.registrarActualizarCatEnte(ente);
        this.confirmacion();
    }

    public void confirmacion() {
        tipoDocumento = "C";
        loadLazy();
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgNewCliente').hide();");
        JsfUti.messageInfo(null, Messages.transaccionOK, "");
    }

    public LazyModelWS<Persona> getCel() {
        return cel;
    }

    public void setCel(LazyModelWS<Persona> cel) {
        this.cel = cel;
    }

    public Persona getEnte() {
        return ente;
    }

    public void setEnte(Persona ente) {
        this.ente = ente;
    }

    public Boolean getPersona() {
        return persona;
    }

    public void setPersona(Boolean persona) {
        this.persona = persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<Dominio> getDominios() {
        return dominios;
    }

    public void setDominios(List<Dominio> dominios) {
        this.dominios = dominios;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Persona getEnteDinardap() {
        return enteDinardap;
    }

    public void setEnteDinardap(Persona enteDinardap) {
        this.enteDinardap = enteDinardap;
    }

}
