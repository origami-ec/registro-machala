/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans.component;

import org.bcbg.config.SisVars;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Dominio;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.VerCedulaUtils;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyEnte implements Serializable {

    private static final Logger LOG = Logger.getLogger(DlgLazyEnte.class.getName());

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;

    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    protected LazyModelWS<Persona> catEntes;
    protected Persona ente;
    private String tipoDocumento = "C";
    protected VerCedulaUtils vcu;
    private int tabIndex = 0;
    private List<Dominio> dominios;
    private String dominio;
    private int size;

    @PostConstruct
    protected void initView() {
        try {
            loadLazyEnte();
            ente = new Persona();
            dominio = "@outlook.com";
            if (ss.tieneParametro("ciRuc_")) {
                String ced = (String) ss.getParametros().get("ciRuc_");
                ente.setIdentificacion(ced);
                vcu = new VerCedulaUtils();
                boolean valido = vcu.comprobarDocumento(ente.getIdentificacion());
                if (!valido) {
                    JsfUti.messageError(null, "", Messages.CiRucInvalida);
                }
                if (vcu.getTeu().getNatural()) {
                    ente.setEsPersona(vcu.getTeu().getNatural());
                } else if (vcu.getTeu().getJuridica()) {
                    ente.setEsPersona(vcu.getTeu().getJuridica());
                } else {
                    ente.setEsPersona(vcu.getTeu().getNacExt());
                }
                ente.setTipoIdentificacion(vcu.getTeu().gettDocumento());
                setTipoDocumento(vcu.getTeu().gettDocumento());
                tabIndex = 1;
            } else {
                tabIndex = 0;
            }
            if (!ente.getEsPersona()) {
                tipoDocumento = "R";
            } else {
                tipoDocumento = "C";
            }
            this.loadDominios();
            this.existeCorreo();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, tipoDocumento, e);
        }
    }

    private void loadLazyEnte() {
        catEntes = new LazyModelWS<>(SisVars.ws + "personas/find?sort=nombres", us.getToken());
        catEntes.setEntitiArray(Persona[].class);
    }

    public void close(Persona e) {
        PrimeFaces.current().dialog().closeDynamic(e);
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

    public boolean validateNames() {
        if (ente.getEsPersona()) {
            if (ente.getNombres().trim().length() == 0 || ente.getApellidos().trim().length() == 0) {
                return false;
            }
            if (ente.getNombres().isEmpty() || ente.getApellidos().isEmpty()) {
                return false;
            }
        } else {
            if (ente.getRazonSocial().trim().length() == 0) {
                return false;
            }
            if (ente.getRazonSocial().isEmpty()) {
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
            ente.setNombreComercial(ente.getNombreComercial().toUpperCase());
        }

    }

    public boolean validacionCorreo() {
        if (ente.getCorreo() != null) {
            ente.setCorreo(ente.getCorreo().concat(dominio));
        }
        if (ente.getCorreo() != null && !ente.getCorreo().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo())) {
            JsfUti.messageWarning(null, "Dato del campo Correo no es valido.", "");
            return false;
        }

        return true;
    }

    public Boolean existeEnte(String ciruc) {
        return appServices.buscarCatEnte(new Persona(ciruc)) != null;
    }

    public void saveEnte() {
        try {
            if (this.validateNames()) {
                if (!this.validacionCorreo()) {
                    return;
                }
                this.namesToUpper();
                ente.setEstado("A");
                ente.setUserCre(us.getName_user());
                ente.setFechaCre(new Date());
                ente.setTipoIdentificacion(tipoDocumento);
                if (ente.getCorreo() != null) {
                    ente.setCorreo(ente.getCorreo().toLowerCase());
                }
                if (ente.getIdentificacion().isEmpty()) {
                    ente.setExcepcional(true);
                    //itl.saveCatEnteSinCedRuc(ente);
                    this.confirmacion();
                } else if (this.existeEnte(ente.getIdentificacion())) {
                    JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                } else {
                    vcu = new VerCedulaUtils();
                    switch (tipoDocumento) {
                        case "P":
                            ente = (Persona) service.methodPOST(ente, SisVars.ws + "personas/guardar", Persona.class);
                            this.confirmacion();
                            break;
                        case "C":
                            if (vcu.isCIValida(ente.getIdentificacion())) {
                                ente = (Persona) service.methodPOST(ente, SisVars.ws + "personas/guardar", Persona.class);
                                this.confirmacion();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                        case "R":
                            if (vcu.isRucValido(ente.getIdentificacion())) {
                                ente = (Persona) service.methodPOST(ente, SisVars.ws + "personas/guardar", Persona.class);
                                this.confirmacion();
                            } else {
                                JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                            }
                            break;
                        default:

                            break;
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o razon social.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            e.printStackTrace();
        }
    }

    public void loadDominios() {
        dominios = service.methodListGET(SisVars.ws + "dominio/findAll", Dominio[].class);
    }

    public void existeCorreo() {
        if (ente.getCorreo() == null || ente.getCorreo().isEmpty()) {
            size = 30;
            return;
        }
        size = 40;
    }

    public void confirmacion() {
        tipoDocumento = "C";
        // JsfUti.executeJS("PF('dlgNewCliente').hide();");
        JsfUti.messageInfo(null, Messages.transaccionOK, "");
        PrimeFaces.current().dialog().closeDynamic(ente);
    }

    public LazyModelWS<Persona> getCatEntes() {
        return catEntes;
    }

    public void setCatEntes(LazyModelWS<Persona> catEntes) {
        this.catEntes = catEntes;
    }

    public Persona getEnte() {
        return ente;
    }

    public void setEnte(Persona ente) {
        this.ente = ente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
