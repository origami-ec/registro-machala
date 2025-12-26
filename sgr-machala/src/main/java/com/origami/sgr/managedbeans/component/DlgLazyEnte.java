/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.lazymodels.CatEnteLazy;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
//import com.origami.sgr.util.VerCedulaUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyEnte implements Serializable {

    private static final Logger LOG = Logger.getLogger(DlgLazyEnte.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private RegistroPropiedadServices reg;

    protected CatEnteLazy lazy;
    protected CatEnte ente;
    private String tipoDocumento = "C";
    //protected VerCedulaUtils vcu;
    private int tabIndex = 0;
    protected PubPersona persona;
    protected Map map;

    @PostConstruct
    protected void initView() {
        try {
            lazy = new CatEnteLazy();
            ente = new CatEnte();
            if (ss.tieneParametro("ciRuc_")) {
                String ced = (String) ss.getParametros().get("ciRuc_");
                ente.setCiRuc(ced);
                //vcu = new VerCedulaUtils();
                /*boolean valido = vcu.comprobarDocumento(ente.getCiRuc());
                if (!valido) {
                JsfUti.messageError(null, "", Messages.CiRucInvalida);
                }
                if (vcu.getTeu().getNatural()) {
                ente.setEsPersona(vcu.getTeu().getNatural());
                } else if (vcu.getTeu().getJuridica()) {
                ente.setEsPersona(vcu.getTeu().getJuridica());
                } else {
                ente.setEsPersona(vcu.getTeu().getNacExt());
                }*/
                switch (ente.getCiRuc().length()) {
                    case 10:
                        ente.setEsPersona(Boolean.TRUE);
                        ente.setTipoIdentificacion("C");
                        break;
                    case 13:
                        ente.setEsPersona(Boolean.FALSE);
                        ente.setTipoIdentificacion("R");
                        break;
                    default:
                        ente.setEsPersona(Boolean.TRUE);
                        ente.setTipoIdentificacion("P");
                        JsfUti.messageWarning(null, "Advertencia", "El documento ingresado NO tiene 10 o 13 dígitos.");
                        break;
                }
                //ente.setTipoIdentificacion(vcu.getTeu().gettDocumento());
                //setTipoDocumento(vcu.getTeu().gettDocumento());
                this.setTipoDocumento(ente.getTipoIdentificacion());
                tabIndex = 1;
            } else {
                tabIndex = 0;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, tipoDocumento, e);
        }
    }

    public void selectEnte(CatEnte e) {
        try {
            //System.out.println("select catente: " + e);
            PrimeFaces.current().dialog().closeDynamic(e);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void updateTipoDoc() {
        if (!ente.getEsPersona()) {
            tipoDocumento = "R";
        }
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
        if (ente.getCorreo1() != null && !ente.getCorreo1().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo1())) {
            JsfUti.messageWarning(null, "Dato del campo Correo1 no es valido.", "");
            return false;
        }
        if (ente.getCorreo2() != null && !ente.getCorreo2().isEmpty() && !Utils.validarEmailConExpresion(ente.getCorreo2())) {
            JsfUti.messageWarning(null, "Dato del campo Correo2 no es valido.", "");
            return false;
        }
        return true;
    }

    public Boolean existeEnte(String ciruc) {
        CatEnte temp = (CatEnte) em.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{ciruc});
        return temp != null;
    }

    public void saveEnte() {
        try {
            if (this.validateNames()) {
                if (this.validacionCorreo()) {
                    this.namesToUpper();
                    ente.setUserCre(us.getName_user());
                    ente.setFechaCre(new Date());
                    ente.setTipoIdentificacion(tipoDocumento);
                    if (ente.getCorreo1() != null) {
                        ente.setCorreo1(ente.getCorreo1().toLowerCase());
                    }
                    if (ente.getCiRuc().isEmpty()) {
                        ente.setExcepcional(true);
                        ente = itl.saveCatEnteSinCedRuc(ente);
                        //this.confirmacion();
                        this.selectEnte(ente);
                    } else if (this.existeEnte(ente.getCiRuc())) {
                        JsfUti.messageWarning(null, Messages.ciRucExiste, "");
                    } else {
                        //vcu = new VerCedulaUtils();
                        switch (tipoDocumento) {
                            case "P":
                                ente = (CatEnte) em.persist(ente);
                                //this.confirmacion();
                                this.selectEnte(ente);
                                break;
                            case "C":
                                //if (vcu.isCIValida(ente.getCiRuc())) {
                                if (ente.getCiRuc().length() == 10) {
                                    ente = (CatEnte) em.persist(ente);
                                    //this.confirmacion();
                                    this.selectEnte(ente);
                                } else {
                                    //JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                                    JsfUti.messageError(null, "Tipo documento cédula debe tener 10 digitos.", "");
                                }
                                break;
                            case "R":
                                //if (vcu.isRucValido(ente.getCiRuc())) {
                                if (ente.getCiRuc().length() == 13) {
                                    ente = (CatEnte) em.persist(ente);
                                    //this.confirmacion();
                                    this.selectEnte(ente);
                                } else {
                                    //JsfUti.messageWarning(null, Messages.CiRucInvalida, "");
                                    JsfUti.messageError(null, "Tipo documento RUC debe tener 13 digitos.", "");
                                }
                                break;
                        }
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Faltan de ingresar lo campos nombres, apellidos o razon social.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void confirmacion() {
        tipoDocumento = "C";
        // JsfUti.executeJS("PF('dlgNewCliente').hide();");
        JsfUti.messageInfo(null, Messages.transaccionOK, "");
        PrimeFaces.current().dialog().closeDynamic(ente);
    }

    public void buscarDinardap() {
        try {
            if (!ente.getCiRuc().isEmpty()) {
                //persona = reg.buscarDinardap(ente.getCiRuc());
                persona = reg.buscarSistemaTurnos(ente.getCiRuc());
                if (persona != null && !persona.getCedRuc().isEmpty()) {
                    if (persona.getCedRuc().length() == 13) {
                        ente.setEsPersona(false);
                        tipoDocumento = "R";
                        ente.setRazonSocial(persona.getNombres());
                        ente.setDireccion(persona.getDireccion());
                        ente.setTelefono1(persona.getTelefono1());
                        ente.setCorreo1(persona.getCorreo1());
                        if (persona.getFechaExpedicionLong() != null) {
                            ente.setFechaNacimiento(new Date(persona.getFechaExpedicionLong()));
                        }

                    } else {
                        ente.setNombres(persona.getNombres());
                        ente.setApellidos(persona.getApellidos());
                        ente.setDireccion(persona.getDireccion());
                        ente.setTelefono1(persona.getTelefono1());
                        ente.setCorreo1(persona.getCorreo1());
                        if (persona.getFechaNacimientoLong() != null) {
                            ente.setFechaNacimiento(new Date(persona.getFechaNacimientoLong()));
                        }
                        if (persona.getEstadoCivil() != null) {
                            
                            ente.setEstadoCivil(this.getCatalogo(persona.getEstadoCivil()));
                        }
                        /*String[] name = persona.getApellidos().split(" ");
                        ente.setApellidos(name[0].concat(" ").concat(name[1]));
                        if (name.length > 2) {
                            ente.setNombres("");
                            for (int i = 2; i < name.length; i++) {
                                ente.setNombres(ente.getNombres().concat(" ").concat(name[i]));
                            }
                            ente.setNombres(ente.getNombres().trim());
                        }*/
                    }
                } else {
                    JsfUti.messageError(null, "NO se encontró información con la identificación ingresada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar campo CI/RUC.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<CtlgItem> getEstadosCiviles() {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        return em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
    }

    public CtlgItem getCatalogo(String value) {
        map = new HashMap<>();
        map.put("catalogo", Constantes.estadosCivil);
        map.put("codename", value.toLowerCase().trim());
        return (CtlgItem) em.findObjectByParameter(Querys.getCtlgItemByCatalogoCodeName, map);
    }

    public CatEnteLazy getLazy() {
        return lazy;
    }

    public void setLazy(CatEnteLazy lazy) {
        this.lazy = lazy;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
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

}
