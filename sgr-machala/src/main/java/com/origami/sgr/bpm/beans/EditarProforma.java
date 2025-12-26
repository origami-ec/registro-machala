/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegEnteJudiciales;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.entities.RegpDetalleExoneracion;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpExoneracion;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpLiquidacionExoneracion;
import com.origami.sgr.entities.RegpObservacionesIngreso;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegActoLazy;
import com.origami.sgr.lazymodels.RegpTareasDinardapLazy;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class EditarProforma extends BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private IngresoTramiteLocal itl;

    @Inject
    protected RegistroPropiedadServices reg;

    protected CatEnte solicitante;
    protected CatEnte beneficiario;
    protected RegpLiquidacionExoneracion exoneracion;
    protected List<RegpLiquidacionExoneracion> exoneraciones = new ArrayList<>();
    protected RegpLiquidacion liquidacion;
    protected HistoricoTramites ht;
    protected List<RegpIntervinientes> listInterv, listIntervEliminar;
    protected List<RegpLiquidacionDetalles> actosPorPagar;
    protected List<RegpLiquidacionDetalles> regpLiquidacionDetallesRemover;
    protected List<RegpDetalleExoneracion> detalleExoneraciones;
    protected RegActoLazy actos;
    protected RegActo acto;
    protected CtlgCatalogo catalogo;
    protected CtlgItem usoDocumento;
    protected CtlgItem nuevoUsoDoc;
    protected RegpTareasDinardapLazy tareasLazy;
    protected RegpTareasDinardap regpTareasDinardap;
    protected String nombre = "";
    protected String obsAdicional = "";
    protected String cedula;
    protected BigDecimal subTotal;
    protected BigDecimal subTotalDesc;
    protected BigDecimal totalPagar;
    protected BigDecimal descPorLey;
    protected BigDecimal recargoAplicado;
    protected BigDecimal descLimitCobro;
    protected BigDecimal gastosGenerales;
    protected BigDecimal avaluo = BigDecimal.ZERO;
    protected BigDecimal cuantia = BigDecimal.ZERO;
    protected BigDecimal porcPago = BigDecimal.ONE;
    protected Integer indice;
    protected Boolean block = false;
    protected Boolean diferencia = false;
    protected ContenidoReportes contenido;
    protected RegRegistrador registrador;
    protected RegpObservacionesIngreso observacion;
    protected RegEnteJudiciales enju = new RegEnteJudiciales();
    protected RegpLiquidacionDetalles rld = new RegpLiquidacionDetalles();
    protected boolean editar = false;
    protected boolean certificado = false;
    protected int indiceActo = 0, indiceInterviniente = 0;
    protected RegpEstadoLiquidacion estadoLiquidacion, incompleta;
    protected RegpEstadoPago estadoPago;
    protected RegpIntervinientes interviniente;

    //VARIABLES PARA LA REFORMA DE TABLA DE ARANCELES
    protected Integer tipocalculo = 0;
    protected BigDecimal adicional = BigDecimal.ZERO;
    private Boolean beneficiarioEsSolicitante, agregaBeneficiario, agregaSolicitante;
    private Integer indexBeneficiario, indexSolicitante;
    private Integer aniosDiferencia;
    protected Valores valor, validez;
    private Long numFicha;
    private LazyModel fichasRegistrales;
    private Long idActo;
    protected Boolean propiedad = null;
    protected Valores limiteActo;
    protected Valores salarioBasico;
    protected Valores limiteFactura;

    @PostConstruct
    protected void iniView() {
        try {
            if (ss.getParametros() == null) {
                JsfUti.redirectFaces("/procesos/registro/proformasRp.xhtml");
            } else if (ss.getParametros().get("proforma") == null) {
                JsfUti.redirectFaces("/procesos/registro/proformasRp.xhtml");
            } else {
                Long proforma = (Long) ss.getParametros().get("proforma");
                liquidacion = manager.find(RegpLiquidacion.class, proforma);
                if (liquidacion != null) {
                    propiedad = liquidacion.getEsRegistroPropiedad();
                    if (liquidacion.getSolicitante() != null) {
                        solicitante = liquidacion.getSolicitante();
                    }
                    if (liquidacion.getBeneficiario() != null) {
                        beneficiario = liquidacion.getBeneficiario();
                    }
                    if (liquidacion.getTramite() != null) {
                        ht = liquidacion.getTramite();
                    }
                    if (liquidacion.getRegpLiquidacionDetallesCollection() != null) {
                        map = new HashMap();
                        map.put("liquidacion", liquidacion);
                        actosPorPagar = manager.findObjectByParameterList(RegpLiquidacionDetalles.class, map);
                        if (actosPorPagar != null) {
                            for (RegpLiquidacionDetalles dd : actosPorPagar) {
                                if (dd.getReingreso()) {
                                    dd.setValorTotal(BigDecimal.ZERO.setScale(2));
                                } else {
                                    dd.setSubtotal(dd.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
                                }
                            }
                        }

                    }
                    if (liquidacion.getValorActos() != null) {
                        subTotalDesc = liquidacion.getValorActos();
                    }
                    if (liquidacion.getSubTotal() != null) {
                        subTotal = liquidacion.getSubTotal();
                    }
                    if (liquidacion.getTotalPagar() != null) {
                        totalPagar = liquidacion.getTotalPagar();
                    }
                    if (liquidacion.getDescuentoValor() != null) {
                        descPorLey = liquidacion.getDescuentoValor();
                    }
                    if (liquidacion.getDescLimitCobro() != null) {
                        descLimitCobro = liquidacion.getDescLimitCobro();
                    }
                    if (liquidacion.getDescuentoPorc() != null) {
                        porcPago = liquidacion.getDescuentoPorc();
                    }
                    if (liquidacion.getGastosGenerales() != null) {
                        gastosGenerales = liquidacion.getGastosGenerales();
                    }
                    if (liquidacion.getEnteJudicial() != null) {
                        enju = liquidacion.getEnteJudicial();
                    }
                    if (liquidacion.getUsoDocumento() != null) {
                        usoDocumento = liquidacion.getUsoDocumento();
                    }
                    recargoAplicado = BigDecimal.ZERO;
                    if (liquidacion.getAdicional() != null) {
                        recargoAplicado = liquidacion.getAdicional();
                    }
                    /*if (liquidacion.getFicha() != null) {
                        numFicha = liquidacion.getFicha().getNumFicha();
                    }*/
                    map = new HashMap();
                    map.put("nombre", Constantes.usosDocumento);
                    catalogo = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.limiteFactura);
                    valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.diasValidezProforma);
                    validez = (Valores) manager.findObjectByParameter(Valores.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.piePaginaProforma);
                    contenido = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
                    map = new HashMap();
                    map.put("actual", Boolean.TRUE);
                    registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.limiteValorContrato);
                    limiteActo = (Valores) manager.findObjectByParameter(Valores.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.limiteFactura);
                    limiteFactura = (Valores) manager.findObjectByParameter(Valores.class, map);
                    map = new HashMap();
                    map.put("code", Constantes.salarioBasicoUnificado);
                    salarioBasico = (Valores) manager.findObjectByParameter(Valores.class, map);
                    estadoLiquidacion = manager.find(RegpEstadoLiquidacion.class, 1L); //ESTADO LIQUIDACION ACEPTADA
                    incompleta = manager.find(RegpEstadoLiquidacion.class, 4L); //ESTADO LIQUIDACION INCOMPLETA
                    estadoPago = manager.find(RegpEstadoPago.class, 1L); // ESTADO DE PAGO PENDIENTE DE PAGO
                    ss.instanciarParametros();
                    ht = liquidacion.getTramite();
                    actos = new RegActoLazy(new RegTipoCobroActo(2L));
                    regpLiquidacionDetallesRemover = new ArrayList<>();
                    listIntervEliminar = new ArrayList<>();
                    listInterv = new ArrayList<>();
                } else {
                    JsfUti.redirectFaces("/procesos/registro/proformasRp.xhtml");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<CtlgItem> getUsosDocumentos() {
        return manager.findAllEntCopy(Querys.getCtlgItemListUsosDocs);
    }

    public void selectObjectJudicial(SelectEvent event) {
        enju = (RegEnteJudiciales) event.getObject();
        liquidacion.setEnteJudicial(enju);
    }

    public void selectSolicitante(SelectEvent event) {
        solicitante = (CatEnte) event.getObject();
    }

    public void selectNotaria(SelectEvent event) {
        enju = (RegEnteJudiciales) event.getObject();
        JsfUti.update("mainForm:pnlUnidadJudicial");
    }

    public void selectBeneficiario(SelectEvent event) {
        beneficiario = (CatEnte) event.getObject();
        if (beneficiario.equals(solicitante)) {
            beneficiarioEsSolicitante = Boolean.TRUE;
        } else {
            beneficiarioEsSolicitante = Boolean.FALSE;
        }
    }

    public void agregarBeneficiarioInterviniente() {
        if (agregaBeneficiario) {
            if (beneficiario != null && beneficiario.getId() != null) {
                indexBeneficiario = agregarIntereniente(beneficiario);
                if (indexBeneficiario == null) {
                    agregaBeneficiario = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Beneficiario", "");
                agregaBeneficiario = Boolean.FALSE;
            }
        } else {
            int postDelete = -1;
            for (int i = 0; i < listInterv.size(); i++) {
                if (listInterv.get(i).getEnte().getCiRuc().equalsIgnoreCase(beneficiario.getCiRuc())) {
                    postDelete = i;
                    break;
                }
            }
            if (postDelete != -1) {
                listInterv.remove(postDelete);
            }
        }
    }

    public void agregarSolicitanteInterviniente() {
        if (agregaSolicitante) {
            if (solicitante != null && solicitante.getId() != null) {
                indexSolicitante = agregarIntereniente(solicitante);
                if (indexSolicitante == null) {
                    agregaSolicitante = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Solicitante", "");
                agregaSolicitante = Boolean.FALSE;
            }
        } else {
            int postDelete = -1;
            for (int i = 0; i < listInterv.size(); i++) {
                if (listInterv.get(i).getEnte().getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                    postDelete = i;
                    break;
                }
            }
            if (postDelete != -1) {
                listInterv.remove(postDelete);
            }
        }
    }

    public void selectInterv(SelectEvent event) {
        CatEnte ente = (CatEnte) event.getObject();
        agregarIntereniente(ente);
    }

    private Integer agregarIntereniente(CatEnte e) {
        if (this.validaInterviniente(e.getCiRuc())) {
            RegpIntervinientes in = new RegpIntervinientes();
            in.setEnte(e);
            listInterv.add(in);
            return listInterv.size() - 1;
        } else {
            JsfUti.messageWarning(null, "Ya esta ingresado el interviniente.", "");
            return null;
        }
    }

    public boolean validaInterviniente(String cedula) {
        return listInterv.stream().noneMatch((in) -> (in.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    public List<RegPapel> complete(String query) {
        List<RegPapel> results = manager.findMax(Querys.getRegCatPapelByPapel, new String[]{"papel"}, new Object[]{query.toLowerCase().trim().replaceAll(" ", "%") + "%"}, 10);
        return results;
    }

    public void selectObjectPapel(SelectEvent event) {
        RegPapel pa = (RegPapel) event.getObject();
        interviniente.setPapel(pa);
    }

    public void eliminarInterviniente(int index) {
        try {
            RegpIntervinientes in = listInterv.get(index);
            listInterv.remove(index);
            if (in.getId() != null) {
//                if (rld.getId() != null) {
//                    if (Utils.isNotEmpty(rld.getRegpIntervinientesCollection())) {
//                        if(rld.getRegpIntervinientesCollection().contains(in)){
//                            rld.getRegpIntervinientesCollection().remove(in);
//                        };
//                    }
//                }
                listIntervEliminar.add(in);
//                manager.delete(in);
            }
            if (in.getEnte().getCiRuc().equalsIgnoreCase(beneficiario.getCiRuc())) {
                agregaBeneficiario = Boolean.FALSE;
            }
            if (in.getEnte().getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                agregaSolicitante = Boolean.FALSE;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void onRowSelect() {
        try {
            certificado = false;
            if (acto.getArancel() == null) {
                JsfUti.messageWarning(null, "El acto seleccionado no tiene arancel asociado.", "");
                return;
            }
            if (propiedad == null) {
                propiedad = acto.getTipoActo().getId() == 1L;
            } else {
                Boolean temp = acto.getTipoActo().getId() == 1L;
                if (!Objects.equals(temp, propiedad)) {
                    JsfUti.messageWarning(null, "No se pueden seleccionar actos de propiedad con actos mercantil.", "");
                    return;
                }
            }
            aniosDiferencia = 0;
            editar = false;
            certificado = false;
            this.verTipoCalculo();
            rld = new RegpLiquidacionDetalles();
            rld.setCantidad(1);
            rld.setActo(acto);
            idActo = acto.getId();
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setRecargo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setBase(BigDecimal.ZERO);
            rld.setCantidadIntervinientes(1);

            if (acto.getTipoActo() != null) {
                String name = acto.getTipoActo().getNombre().toUpperCase();
                if (name.contains("HISTORIADO")) {
                    certificado = true;
                }
            }
            listInterv.stream().map((re) -> {
                re.setId(null);
                return re;
            }).forEachOrdered((re) -> {
                re.setExoneracion(null);
            });
            JsfUti.update("formCuantia");
            JsfUti.executeJS("PF('dlgCuantia').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aniosCalculo() {
        aniosDiferencia = 0;
        if (rld.getAnioUltimaTrasnferencia() != null && rld.getAnioAntecedenteSolicitado() != null) {
            aniosDiferencia = rld.getAnioUltimaTrasnferencia() - rld.getAnioAntecedenteSolicitado();
            if (aniosDiferencia < 0) {
                JsfUti.messageWarning(null, "El año de la ultima transferencia no puede ser menor al del movimiento solicitado.", "");
                aniosDiferencia = 0;
                return;
            }
            if (aniosDiferencia - 15 >= 0) {
                aniosDiferencia = aniosDiferencia - 15;
            } else {
                aniosDiferencia = 0;
            }
        }

    }

    public void verTipoCalculo() {
        tipocalculo = 0;
        if (acto.getTipoCobro() != null) {
            tipocalculo = acto.getTipoCobro().getId().intValue();
        }
    }

    public void showDlgPapel(String urlFacelet, int indice) {
        interviniente = listInterv.get(indice);
        indiceInterviniente = indice;
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "60%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
    }

    public void showDlgEditActo(int indice) {
        try {
            certificado = false;
            editar = true;
            indiceActo = indice;
            rld = actosPorPagar.get(indice);

            /*if (rld.getActo().getTipoActo() != null) {
                String name = rld.getActo().getTipoActo().getNombre().toUpperCase();
                if (name.contains("HISTORIADO")) {
                    certificado = true;
                    aniosCalculo();
                }
            }*/
            acto = rld.getActo();
            idActo = acto.getId();
            avaluo = rld.getAvaluo();
            cuantia = rld.getCuantia();
            //this.verTipoCalculo();
            if (rld.getId() != null) {
                //BUSCA INTERVINIENTES DETALLES
                Boolean contieneActo = Boolean.FALSE;
                if (Utils.isNotEmpty(listIntervEliminar)) {
                    for (RegpIntervinientes i : listIntervEliminar) {
                        if (i.getLiquidacion() != null) {
                            if (i.getLiquidacion().getId().equals(rld.getId())) {
                                contieneActo = Boolean.TRUE;
                            }
                        }
                    }
                }
                if (!contieneActo) {
                    System.out.println("holi desde Utils.isNotEmpty(rld.getIntervinientes()");
                    if (Utils.isNotEmpty(rld.getIntervinientes())) {
                        listInterv = rld.getIntervinientes();
                    } else {
                        map = new HashMap();
                        map.put("liquidacion", rld);
                        listInterv = manager.findObjectByParameterList(RegpIntervinientes.class, map);
                        if (listInterv == null) {
                            listInterv = new ArrayList<>();
                        }
                    }
                } else if (Utils.isNotEmpty(rld.getIntervinientes())) {
                    listInterv = rld.getIntervinientes();
                } else {
                    listInterv = new ArrayList<>();
                }
//
//                List<RegpIntervinientes> ris = new ArrayList<>();
//                if (Utils.isNotEmpty(listIntervEliminar)) {
//                    for (RegpIntervinientes i : listIntervEliminar) {
//                        for (RegpIntervinientes o : listInterv) {
//                            if (o.getId() != null) {
//                                if (i.getId() != null) {
//                                    if (o.getId().equals(i.getId())) {
//                                        ris.add(o);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                if (Utils.isNotEmpty(ris)) {
//                    listInterv.removeAll(ris);
//                }
//                if (Utils.isNotEmpty(rld.getIntervinientes())) {
//                    for (RegpIntervinientes i : rld.getIntervinientes()) {
//                        listInterv.add(i);
//                    }
//                }
            } else {
                listInterv = (List<RegpIntervinientes>) rld.getRegpIntervinientesCollection();
            }

            agregaBeneficiario = agregaSolicitante = Boolean.FALSE;
            if (Utils.isNotEmpty(listInterv)) {
                for (RegpIntervinientes in : listInterv) {
                    if (solicitante != null) {
                        if (solicitante.getId() != null) {
                            if (solicitante.getCiRuc().equalsIgnoreCase(in.getEnte().getCiRuc())) {
                                agregaSolicitante = Boolean.TRUE;
                            }
                        }
                    }
                    if (beneficiario != null) {
                        if (beneficiario.getId() != null) {
                            if (beneficiario.getCiRuc().equalsIgnoreCase(in.getEnte().getCiRuc())) {
                                agregaBeneficiario = Boolean.TRUE;
                            }
                        }
                    }

                }
            }

            if (solicitante != null && beneficiario != null) {
                if (solicitante.getId() != null && beneficiario.getId() != null) {
                    if (beneficiario.getCiRuc().equalsIgnoreCase(solicitante.getCiRuc())) {
                        if (agregaBeneficiario && agregaSolicitante) {
                            agregaBeneficiario = Boolean.FALSE;
                        }
                    }
                }
            }
            JsfUti.update("formEditCuantia");
            JsfUti.executeJS("PF('dglEditCuantia').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageWarning(null, "ERROR DE APLICACION.", "");
        }
    }

    public void agregarActoOld() {
        try {
            if (this.validaPapeles()) {

                if (certificado) {
                    if (rld.getAnioUltimaTrasnferencia() == null) {
                        JsfUti.messageWarning(null, "Debe especificar el anio de la ultima trasferencia.", "");
                        return;
                    }
                    if (rld.getAnioAntecedenteSolicitado() == null) {
                        JsfUti.messageWarning(null, "Debe especificar el anio del antecedente solicitado.", "");
                        return;
                    }
                }
                if (indice > 0 && (rld.getCantidadIntervinientes() != null && rld.getCantidadIntervinientes() < indice)) {
                    JsfUti.messageWarning(null, "La cantidad de intervinientes es menor a la cantidad de intervinientes exonerados.", "");
                    return;
                }
                if (rld.getCantidadIntervinientes() == null && indice > 0) {
                    JsfUti.messageWarning(null, "Debe de especificar la cantidad de intervinientes comparecientes al pago para aplicar el descuento.", "");
                    return;
                }
                if (rld.getCantidad() == null) {
                    JsfUti.messageWarning(null, "Debe de ingresar la cantidad.", "");
                    return;
                }
                if (rld.getCantidad() == 0) {
                    JsfUti.messageWarning(null, "La cantidad no puede ser 0.", "");
                    return;
                }
                if (acto.getArancel() != null) {
                    RegActo a = manager.find(RegActo.class, idActo);
                    if (a.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
                        if (this.validaCuantiaAvaluo()) {
                            if (avaluo != null) {
                                rld.setAvaluo(avaluo);
                            }
                            if (cuantia != null) {
                                rld.setCuantia(cuantia);
                            }
                            if (avaluo.compareTo(cuantia) > 0) {
                                rld.setValorUnitario(itl.calculoCuantia(avaluo));
                            } else {
                                rld.setValorUnitario(itl.calculoCuantia(cuantia));
                            }
                            this.calculoContrato();
                        }
                    } else {
                        if (avaluo != null) {
                            rld.setAvaluo(avaluo);
                        }
                        if (cuantia != null) {
                            rld.setCuantia(cuantia);
                        }
                        rld.setValorUnitario(a.getArancel().getValor());
                        this.calculoContrato();
                    }
                    if (avaluo != null && cuantia != null) {
                        if (avaluo.compareTo(cuantia) >= 0) {
                            rld.setBase(avaluo);
                        } else {
                            rld.setBase(cuantia);
                        }
                    }
                    if (avaluo == null && cuantia != null) {
                        rld.setBase(cuantia);
                    }
                    if (avaluo != null && cuantia == null) {
                        rld.setBase(avaluo);
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarActo() {
        try {
            if (this.validaPapeles()) {
                if (acto.getArancel() != null) {
                    switch (acto.getArancel().getCodigo()) {
                        case 1:
                            this.calculonormal();
                            break;
                        case 2: //50% AVALUO
                            this.calculoMediaCuantia();
                            break;
                        case 3: //LEY MERCADOS Y VALORES
                            this.calculonormal();
                            break;
                        default:
                            this.calculonormal();
                    }
                    this.setearvalores();
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void calculonormal() {
        if (acto.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
            if (this.validaCuantiaAvaluo()) {
                rld.setAvaluo(avaluo);
                rld.setCuantia(cuantia);
                if (avaluo.compareTo(cuantia) > 0) {
                    if (this.propiedad) {
                        rld.setValorUnitario(itl.calculoCuantia(avaluo, salarioBasico.getValorNumeric()));
                    } else {
                        rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo));
                    }
                } else {
                    if (this.propiedad) {
                        rld.setValorUnitario(itl.calculoCuantia(cuantia, salarioBasico.getValorNumeric()));
                    } else {
                        rld.setValorUnitario(itl.calculoCuantiaDeterminada(cuantia));
                    }
                }
                this.calculoContrato();
            }
        } else {
            if (avaluo != null) {
                rld.setAvaluo(avaluo);
            }
            if (cuantia != null) {
                rld.setCuantia(cuantia);
            }
            rld.setValorUnitario(acto.getArancel().getValor());
            this.calculoContrato();
        }
    }

    public void calculoMediaCuantia() {
        if (this.validaCuantiaAvaluo()) {
            rld.setAvaluo(avaluo);
            rld.setCuantia(cuantia);
            if (avaluo.compareTo(cuantia) > 0) {
                if (this.propiedad) {
                    rld.setValorUnitario(itl.calculoCuantia(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                }
            } else {
                if (this.propiedad) {
                    rld.setValorUnitario(itl.calculoCuantia(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                }
            }
            this.calculoContrato();
        }
    }

    public void setearvalores() {
        if (avaluo != null && cuantia != null) {
            if (avaluo.compareTo(cuantia) >= 0) {
                rld.setBase(avaluo);
            } else {
                rld.setBase(cuantia);
            }
        }
        if (avaluo == null && cuantia != null) {
            rld.setBase(cuantia);
        }
        if (avaluo != null && cuantia == null) {
            rld.setBase(avaluo);
        }
    }
    
    public void quitarDescuento(int index) {
        RegpIntervinientes in = listInterv.get(index);
        in.setExoneracion(null);
    }

    public boolean validaPapeles() {
        indice = 0;
        porcPago = BigDecimal.ZERO;
        if (rld.getCantidad() == null || rld.getCantidad() == 0) {
            JsfUti.messageWarning(null, "La cantidad no puede ser 0.", "");
            return false;
        }
        if (listInterv.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el/los propietarios del contrato.", "");
            return false;
        }
        for (RegpIntervinientes rei : listInterv) {
            if (rei.getExoneracion() != null) {
                indice++;
                porcPago = porcPago.add(rei.getExoneracion().getValor());
            }
        }
        if (indice > 0) {
            porcPago = porcPago.divide(new BigDecimal(indice), 2, RoundingMode.HALF_UP);
        }
        return true;
    }

    public Boolean validaCuantiaAvaluo() {
        Boolean flag = false;
        if (avaluo == null) {
            avaluo = BigDecimal.ZERO;
        }
        if (cuantia == null) {
            cuantia = BigDecimal.ZERO;
        }
        if (avaluo.compareTo(BigDecimal.ZERO) > 0 || cuantia.compareTo(BigDecimal.ZERO) > 0) {
            flag = true;
        } else {
            JsfUti.messageWarning(null, "El valor debe del avaluo o la cuantia debe ser mayor a 0.", "");
        }
        return flag;
    }

    public void calculoContrato() {
        Boolean certificados = false;
        if (rld.getReingreso()) {
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
        } else {
            rld.setValorTotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
            rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
            if (aniosDiferencia == null) {
                aniosDiferencia = 0;
            }
            rld.setRecargo(new BigDecimal(aniosDiferencia).multiply(new BigDecimal(rld.getCantidad())));
        }

        rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
        BigDecimal descontar = BigDecimal.ZERO;

        if (indice > 0) {
            BigDecimal valReferencia = rld.getValorTotal().divide(new BigDecimal(rld.getCantidadIntervinientes()), 2, RoundingMode.HALF_UP);
            boolean compartida = true;
            BigDecimal porciento = BigDecimal.ZERO;
            for (RegpIntervinientes i : listInterv) {
                if (i.getExoneracion() != null) {
                    if (i.getExoneracion().getCompartida()) {
                        descontar = descontar.add(valReferencia.multiply(i.getExoneracion().getValor()));
                    } else {
                        porciento = i.getExoneracion().getValor();
                        compartida = Boolean.FALSE;
                        break;
                    }
                }
            }
            if (!compartida) {
                descontar = rld.getValorTotal().multiply(porciento);
            }
        }
        rld.setDescuento(descontar.setScale(2, RoundingMode.HALF_UP));

        rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).subtract(rld.getDescuento()).setScale(2, RoundingMode.HALF_UP));

        List<RegpIntervinientes> temp = (List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv);
        for (RegpIntervinientes r : temp) {
            r.setLiquidacion(rld);
        }
        if (rld.getId() != null) {
            rld.setIntervinientes(temp);
        }
        rld.setRegpIntervinientesCollection(temp);
        if (editar) {
            actosPorPagar.set(indiceActo, rld);
        } else {
            if (rld.getFechaIngreso() == null) {
                rld.setFechaIngreso(new Date());
            }
            actosPorPagar.add(rld);
        }
        if (certificados) {
            this.agregarCertificados();
        }
        this.calculoTotalPagar();
        JsfUti.update("mainForm:accPanelRP:pnlContratos");
        JsfUti.executeJS("PF('dlgCuantia').hide();");
        JsfUti.executeJS("PF('dglEditCuantia').hide();");
    }

    public void agregarCertificados() {
        RegActo reg = manager.find(RegActo.class, 11L);
        map = new HashMap();
        map.put("code", Constantes.cantidadCertificadosPH);
        Valores temp = (Valores) manager.findObjectByParameter(Valores.class, map);
        for (int i = 0; i < temp.getValorNumeric().intValue(); i++) {
            //System.out.println("// cada ingreso...");
            rld = new RegpLiquidacionDetalles();
            rld.setActo(reg);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setDescuento(BigDecimal.ZERO);
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
            if (rld.getFechaIngreso() == null) {
                rld.setFechaIngreso(new Date());
            }
            rld.setRegpIntervinientesCollection((List<RegpIntervinientes>) EntityBeanCopy.clone(listInterv));
            actosPorPagar.add(rld);
        }
    }

    public void calculoTotalPagar() {
        totalPagar = BigDecimal.ZERO;
        descPorLey = BigDecimal.ZERO;
        recargoAplicado = BigDecimal.ZERO;
        subTotal = BigDecimal.ZERO;
        subTotalDesc = BigDecimal.ZERO;
        descLimitCobro = BigDecimal.ZERO;
        actosPorPagar.stream().map((det) -> {
            subTotal = subTotal.add(det.getSubtotal());
            return det;
        }).map((det) -> {
            descPorLey = descPorLey.add(det.getDescuento());
            recargoAplicado = recargoAplicado.add(det.getRecargo());
            return det;
        }).forEachOrdered((det) -> {
            subTotalDesc = subTotalDesc.add(det.getValorTotal());
        });
        gastosGenerales = subTotalDesc.multiply(adicional).setScale(2, RoundingMode.HALF_UP);
        totalPagar = subTotal.subtract(descPorLey).subtract(gastosGenerales).add(recargoAplicado);
    }

    public void eliminarDetalle(int rowIndex) {
        try {
            RegpLiquidacionDetalles de = actosPorPagar.remove(rowIndex);
            if (de.getId() != null) {
                //manager.delete(de);
                regpLiquidacionDetallesRemover.add(de);
            }
            this.calculoTotalPagar();
            JsfUti.update("mainForm:accPanelRP:pnlContratos");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgUsoDoc() {
        nuevoUsoDoc = new CtlgItem();
        JsfUti.update("formUsoDoc");
        JsfUti.executeJS("PF('usoDocumento').show();");
    }

    public void showDlgSolicitudes(Boolean esJuridico) {
        tareasLazy = new RegpTareasDinardapLazy(Boolean.TRUE, esJuridico);
        if (!esJuridico) {
            JsfUti.update("frmSolicitudes");
            JsfUti.executeJS("PF('dglSolicitudes').show();");
        } else {
            JsfUti.update("frmSolicitudesJuridico");
            JsfUti.executeJS("PF('dglSolicitudesJuridico').show();");
        }
    }

    public void seleccionarSolicitud(RegpTareasDinardap rtt, Boolean esJuridico) {
        this.regpTareasDinardap = rtt;
        if (!esJuridico) {
            liquidacion.setOficioMemoReferencia(regpTareasDinardap.getNumeroSolicitud());
            JsfUti.update("mainForm:accPanelRP:solicitud");
            JsfUti.executeJS("PF('dglSolicitudes').hide();");
        } else {
            System.out.println("regpTareasDinardap.getNumeroSolicitud() " + regpTareasDinardap.getNumeroSolicitud());
            liquidacion.setEscritJuicProvResolucion(regpTareasDinardap.getNumeroSolicitud());
            JsfUti.update("mainForm:accPanelRP:numero_juicio");
            JsfUti.executeJS("PF('dglSolicitudesJuridico').hide();");
        }
    }

    public void showDlgEditUsoDoc() {
        if (usoDocumento != null) {
            nuevoUsoDoc = usoDocumento;
            JsfUti.update("formUsoDoc");
            JsfUti.executeJS("PF('usoDocumento').show();");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar el elemento para editar.", "");
        }
    }

    public void guardarUsoDoc() {
        try {
            if (nuevoUsoDoc.getValor() == null || nuevoUsoDoc.getValor().isEmpty()) {
                JsfUti.messageWarning(null, "El campo nombre esta vacio.", "");
            } else {
                nuevoUsoDoc.setValor(nuevoUsoDoc.getValor().toUpperCase());
                nuevoUsoDoc.setEstado("A");
                nuevoUsoDoc.setCatalogo(catalogo);
                nuevoUsoDoc.setCodename(nuevoUsoDoc.getValor().trim().toLowerCase());
                usoDocumento = (CtlgItem) manager.persist(nuevoUsoDoc);
                nuevoUsoDoc = new CtlgItem();
                JsfUti.update("mainForm:accPanelRP:pnlUsoDoc");
                JsfUti.executeJS("PF('usoDocumento').hide();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarLiquidacion() {
        try {
            if (this.validaciones()) {
                //ht.setTipoTramite(tipoTramite);
                //ELIMINA DDETALLES DE LIQUIDACION
                if (!regpLiquidacionDetallesRemover.isEmpty()) {
                    for (RegpLiquidacionDetalles de : regpLiquidacionDetallesRemover) {
                        manager.delete(manager.find(RegpLiquidacionDetalles.class, de.getId()));
                    }
                }
                //ELIMINA INTERVIIENTES
                if (Utils.isNotEmpty(listIntervEliminar)) {
                    for (RegpIntervinientes r : listIntervEliminar) {
                        if (r.getId() != null) {
                            manager.delete(manager.find(RegpIntervinientes.class, r.getId()));
                        }
                    }
                }
                //ht.setFecha(new Date());
                ht.setNombrePropietario(solicitante.getNombreCompleto());
                ht.setSolicitante(solicitante);

                liquidacion.setUsoDocumento(usoDocumento);
                liquidacion.setSubTotal(subTotal);
                liquidacion.setValorActos(subTotalDesc);
                liquidacion.setAdicional(recargoAplicado);
                liquidacion.setDescLimitCobro(descLimitCobro);
                liquidacion.setDescuentoValor(descPorLey);
                liquidacion.setDescuentoPorc(porcPago);
                liquidacion.setGastosGenerales(gastosGenerales);
                liquidacion.setTotalPagar(totalPagar);
                liquidacion.setSolicitante(solicitante);
                liquidacion.setBeneficiario(beneficiario);
                liquidacion.setCantidadRazones(0);
                liquidacion.setTramite(ht);
                liquidacion.setUserEdicion(session.getUserId());
                liquidacion.setEstadoPago(estadoPago);
                liquidacion.setEstadoLiquidacion(estadoLiquidacion);
                if (totalPagar.compareTo(BigDecimal.ZERO) == 0) {
                    liquidacion.setGeneraFactura(Boolean.FALSE);
                } else {
                    liquidacion.setGeneraFactura(Boolean.TRUE);
                }
                if (liquidacion.getReingreso()) {
                    liquidacion.setGeneraFactura(Boolean.FALSE);
                }
                Integer pesoAcum = 0;
                for (RegpLiquidacionDetalles detalles : actosPorPagar) {
                    if (detalles.getActo().getPeso() != null) {
                        pesoAcum = detalles.getActo().getPeso() + pesoAcum;
                    }
                }
                if (pesoAcum == 0) {
                    pesoAcum = 1;
                }
                liquidacion.setPesoTramite(pesoAcum);
                liquidacion.setUserEdicion(session.getUserId());
                liquidacion.setUserCreacion(session.getUserId());
                liquidacion.setFechaEdicion(new Date());
                liquidacion = itl.editLiquidacion(liquidacion, actosPorPagar);
                /*if (this.comprararFechas(liquidacion.getFechaCreacion())) {
                    // DENTRO DE FECHA DE VALIDEZ DE PROFORMA
                    liquidacion.setUserEdicion(session.getUserId());
                    liquidacion.setFechaEdicion(new Date());
                    //liquidacion.setFechaCreacion(new Date());
                    liquidacion = itl.editLiquidacion(liquidacion, actosPorPagar);
                } else {
                    // FUERA DE FECHA DE VALIDEZ DE PROFORMA
                    //liquidacion.setFechaCreacion(new Date());
                    liquidacion.setUserCreacion(session.getUserId());
                    liquidacion = itl.duplicarLiquidacion(liquidacion, actosPorPagar);
                }*/
                if (liquidacion == null) {
                    block = true;
                    JsfUti.update("mainForm:accPanelRP:pnlUsoDoc");
                    JsfUti.messageError(null, Messages.error, "");
                } else {
                    this.cargarDatosReporte();
                    List<String> urlList = new ArrayList<>();
                    String url = "/procesos/registro/proformasRp.xhtml";
                    urlList.add(SisVars.urlbase + "Documento");
                    JsfUti.redirectMultipleConIP_V2(url, urlList);
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean comprararFechas(Date in) {
        try {
            Date fecha = Utils.sumarDiasFechaSinWeekEnd(in, validez.getValorNumeric().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date hoy = sdf.parse(sdf.format(new Date()));
            fecha = sdf.parse(sdf.format(fecha));
            if (fecha.before(hoy)) {
                return false; // FUERA DE FECHA DE VALIDEZ DE PROFORMA
            } else {
                return true; // DENTRO DE FECHA DE VALIDEZ DE PROFORMA
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean validaciones() {
        if (solicitante.getId() == null) {
            JsfUti.messageWarning(null, "Debe ingresar Cliente solicitante.", "");
            return false;
        }
        if (beneficiario.getId() == null) {
            JsfUti.messageWarning(null, "Debe ingresar Cliente beneficiario.", "");
            return false;
        }
        if (actosPorPagar.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el/los contrato(s) del tramite.", "");
            return false;
        }
        for (RegpLiquidacionDetalles d : actosPorPagar) {
            if (d.getActo().getSolvencia()) {
                if (usoDocumento == null) {
                    JsfUti.messageWarning(null, "Debe seleccionar el uso del documento.", "");
                    return false;
                }
            }
        }
        return true;
    }

    public void cargarDatosReporte() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("comprobante_proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarAplicarExo() {
        exoneraciones = new ArrayList<>();
        porcPago = BigDecimal.ONE;
        this.inicializarDesc();
        JsfUti.update("mainForm:accPanelRP:pnlContratos");
        JsfUti.executeJS("PF('dlgExoneracion').hide();");
    }

    public void inicializarDesc() {
        actosPorPagar.stream().map((de) -> {
            de.setDescuento(BigDecimal.ZERO);
            return de;
        }).forEachOrdered((de) -> {
            de.setValorTotal(de.getValorUnitario());
        });
        this.calculoTotalPagar();
    }

    public void showDlgAplicarExo() {
        if (this.cargarIntervsExos()) {
            JsfUti.update("formExo");
            JsfUti.executeJS("PF('dlgExoneracion').show();");
        } else {
            JsfUti.messageWarning(null, "No hay contratos marcados para aplicar descuento.", "");
        }
    }

    public boolean cargarIntervsExos() {
        exoneraciones = new ArrayList<>();
        RegpLiquidacionExoneracion temp;
        List<RegpIntervinientes> intervs;
        for (RegpLiquidacionDetalles de : actosPorPagar) {
            if (de.getAplicaDescuento()) {
                intervs = (List<RegpIntervinientes>) de.getRegpIntervinientesCollection();
                if (intervs == null) {
                    map = new HashMap();
                    map.put("liquidacion", de);
                    intervs = manager.findObjectByParameterList(RegpIntervinientes.class, map);
                    if (intervs == null) {
                        intervs = new ArrayList<>();
                    }
                }
                for (RegpIntervinientes rei : intervs) {
                    if (this.validaProps(rei.getEnte().getCiRuc())) {
                        temp = new RegpLiquidacionExoneracion();
                        temp.setEnte(rei.getEnte());
                        exoneraciones.add(temp);
                    }
                }
            }
        }
        return !exoneraciones.isEmpty();
    }

    public boolean validaProps(String cedula) {
        return exoneraciones.stream().noneMatch((r) -> (r.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    public void calcularValorDescuento() {
        try {
            if (this.calcularPorcPago()) {
                this.aplicarValorDesc();
                JsfUti.update("mainForm:accPanelRP:pnlContratos");
                JsfUti.executeJS("PF('dlgExoneracion').hide();");
            } else {
                JsfUti.messageWarning(null, "No se ha seleccionado el tipo de exoneracion.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void aplicarValorDesc() {
        actosPorPagar.forEach((de) -> {
            if (de.getAplicaDescuento()) {
                de.setValorTotal(de.getValorUnitario().multiply(porcPago).setScale(2, RoundingMode.HALF_UP));
                de.setDescuento(de.getValorUnitario().subtract(de.getValorTotal()));
            } else {
                de.setDescuento(BigDecimal.ZERO);
                de.setValorTotal(de.getValorUnitario());
            }
        });
        this.calculoTotalPagar();
    }

    public boolean calcularPorcPago() {
        indice = 0;
        porcPago = BigDecimal.ONE;
        BigDecimal temp = BigDecimal.ZERO;
        boolean aplicado = false;
        for (RegpLiquidacionExoneracion ex : exoneraciones) {
            if (ex.getBeneficiario()) {
                indice++;
                if (ex.getExoneracion() == null) {
                    temp = temp.add(BigDecimal.ONE);
                } else {
                    aplicado = true;
                    temp = temp.add(BigDecimal.ONE.subtract(ex.getExoneracion().getValor()));
                }
            } else {
                ex.setExoneracion(null);
            }
        }
        if (indice > 0 && aplicado) {
            if (temp.compareTo(BigDecimal.ZERO) > 0) {
                porcPago = temp.divide(new BigDecimal(indice), 4, RoundingMode.HALF_UP);
            } else {
                porcPago = BigDecimal.ZERO;
            }
            return true;
        } else {
            return false;
        }
    }

    public void showDlgExon(String urlFacelet, int indice) {
        exoneracion = exoneraciones.get(indice);
        if (exoneracion.getBeneficiario()) {
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "60%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
        } else {
            JsfUti.messageWarning(null, "Para seleccionar exoneracion debe marcar si es beneficiario.", "");
        }
    }

    public void actualizarBeneficiarioEsSolicitante() {
        if (beneficiarioEsSolicitante) {
            if (solicitante != null) {
                if (solicitante.getId() != null) {
                    beneficiario = solicitante;
                } else {
                    JsfUti.messageWarning(null, "Na ha seleccionado el beneficiario.", "");
                    beneficiarioEsSolicitante = Boolean.FALSE;
                }
            } else {
                beneficiarioEsSolicitante = Boolean.FALSE;
                JsfUti.messageWarning(null, "Na ha seleccionado el beneficiario.", "");
            }
        } else {
            beneficiario = new CatEnte();
            beneficiarioEsSolicitante = Boolean.FALSE;
        }

    }

    public void actualizarDatosSolicitante() {
        try {
            manager.persist(solicitante);
            JsfUti.messageInfo(null, Messages.correcto, "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void actualizarDatosBeneficiario() {
        try {
            manager.merge(beneficiario);
            JsfUti.messageInfo(null, Messages.correcto, "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void selectObjectExo(SelectEvent event) {
        RegpExoneracion ex = (RegpExoneracion) event.getObject();
        interviniente.setExoneracion(ex);

        //listInterv.set(indiceInterviniente, interviniente);
    }

    public void buscar() {
        if (solicitante.getCiRuc() != null || solicitante.getCiRuc().isEmpty()) {
            if (!solicitante.getCiRuc().isEmpty()) {
                cedula = solicitante.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    solicitante = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                } else {
                    solicitante = new CatEnte();
                }
                if (solicitante == null || solicitante.getId() == null) {
                    solicitante = reg.buscarGuardarEnteDinardap(cedula);
                }
            }
            if (solicitante == null) {
                solicitante = new CatEnte();
            }
            if (solicitante.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                JsfUti.update("mainForm:accPanelRP:pnlSolicitante");
            }
        }
    }

    public void buscarBeneficiario() {
        if (beneficiario.getCiRuc() != null || beneficiario.getCiRuc().isEmpty()) {
            if (!beneficiario.getCiRuc().isEmpty()) {
                cedula = beneficiario.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    beneficiario = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                } else {
                    beneficiario = new CatEnte();
                }
                if (beneficiario == null || beneficiario.getId() == null) {
                    beneficiario = reg.buscarGuardarEnteDinardap(cedula);
                }
            }
            if (beneficiario == null) {
                beneficiario = new CatEnte();
            }
            if (beneficiario.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                JsfUti.update("mainForm:accPanelRP:pnlBeneficiario");
            }
        }
    }

    public boolean validarTramiteIngresado(RegpLiquidacion liquid) {
        try {
            if (liquid.getNumInscripcion() == null || liquid.getAnioInscripcion() == null) {
                JsfUti.messageWarning(null, "", "Debe ingresar el numero de inscripcion y el año de inscripcion.");
                return false;
            }
            StringBuffer result = this.itl.validarInscripcion(liquid);
            if (result != null) {
                JsfUti.messageWarning(null, "", result.toString());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return false;
    }

    public void showDlgFichas() {
        Long count = 0l;
        Map<String, Object> fill = new HashMap<>();
        fill.put("numInscripcion", liquidacion.getNumInscripcion());
        if (liquidacion.getAnioInscripcion() != null) {
            fill.put("anioInscripcion", liquidacion.getAnioInscripcion().toString());
            count = ((Long) manager.findObjectByParameter(Querys.RegFichaCount, fill));
        }
        // MOSTRAMOS LAS FICHAS ASOCIADAS
        fill.clear();
        fill.put("regMovimientoFichaCollection.movimiento.numInscripcion", liquidacion.getNumInscripcion());
        fichasRegistrales = new LazyModel(RegFicha.class, "id", "DESC");
        if (count > 0) {
            fichasRegistrales.setFilterss(fill);
        }
        JsfUti.update("frmFichas");
        JsfUti.executeJS("PF('dglFichas').show();");
    }

    public void seleccionarFicha(RegFicha regFicha) {
        //this.liquidacion.setFicha(regFicha);
        numFicha = regFicha.getNumFicha();
        this.validarTramiteIngresado(liquidacion);
        JsfUti.update("mainForm:accPanelRP:ficha_registral");
        JsfUti.executeJS("PF('dglFichas').hide();");

    }

    public void limpiarFicha() {
        this.numFicha = 0L;
        //this.liquidacion.setFicha(null);
        JsfUti.update("mainForm:accPanelRP:ficha_registral");
    }

    public void limpiarDatosSolicitante() {
        solicitante = new CatEnte();
    }

    public void limpiarDatosBeneficiario() {
        beneficiario = new CatEnte();
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public RegEnteJudiciales getEnju() {
        return enju;
    }

    public void setEnju(RegEnteJudiciales enju) {
        this.enju = enju;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
    }

    public List<RegpIntervinientes> getListInterv() {
        return listInterv;
    }

    public void setListInterv(List<RegpIntervinientes> listInterv) {
        this.listInterv = listInterv;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegActoLazy getActos() {
        return actos;
    }

    public void setActos(RegActoLazy actos) {
        this.actos = actos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RegpLiquidacionDetalles> getActosPorPagar() {
        return actosPorPagar;
    }

    public void setActosPorPagar(List<RegpLiquidacionDetalles> actosPorPagar) {
        this.actosPorPagar = actosPorPagar;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getObsAdicional() {
        return obsAdicional;
    }

    public void setObsAdicional(String obsAdicional) {
        this.obsAdicional = obsAdicional;
    }

    public CtlgItem getUsoDocumento() {
        return usoDocumento;
    }

    public void setUsoDocumento(CtlgItem usoDocumento) {
        this.usoDocumento = usoDocumento;
    }

    public CtlgItem getNuevoUsoDoc() {
        return nuevoUsoDoc;
    }

    public void setNuevoUsoDoc(CtlgItem nuevoUsoDoc) {
        this.nuevoUsoDoc = nuevoUsoDoc;
    }

    public BigDecimal getDescPorLey() {
        return descPorLey;
    }

    public void setDescPorLey(BigDecimal descPorLey) {
        this.descPorLey = descPorLey;
    }

    public BigDecimal getDescLimitCobro() {
        return descLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal descLimitCobro) {
        this.descLimitCobro = descLimitCobro;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public RegpLiquidacionDetalles getRld() {
        return rld;
    }

    public void setRld(RegpLiquidacionDetalles rld) {
        this.rld = rld;
    }

    public Boolean getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Boolean diferencia) {
        this.diferencia = diferencia;
    }

    public RegpIntervinientes getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegpIntervinientes interviniente) {
        this.interviniente = interviniente;
    }

    public BigDecimal getSubTotalDesc() {
        return subTotalDesc;
    }

    public void setSubTotalDesc(BigDecimal subTotalDesc) {
        this.subTotalDesc = subTotalDesc;
    }

    public CatEnte getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(CatEnte beneficiario) {
        this.beneficiario = beneficiario;
    }

    public RegpLiquidacionExoneracion getExoneracion() {
        return exoneracion;
    }

    public void setExoneracion(RegpLiquidacionExoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public List<RegpLiquidacionExoneracion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(List<RegpLiquidacionExoneracion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public BigDecimal getPorcPago() {
        return porcPago.multiply(new BigDecimal(100)).setScale(2);
    }

    public void setPorcPago(BigDecimal porcPago) {
        this.porcPago = porcPago;
    }

    public Integer getTipocalculo() {
        return tipocalculo;
    }

    public void setTipocalculo(Integer tipocalculo) {
        this.tipocalculo = tipocalculo;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
    }

    public Boolean getBeneficiarioEsSolicitante() {
        return beneficiarioEsSolicitante;
    }

    public void setBeneficiarioEsSolicitante(Boolean beneficiarioEsSolicitante) {
        this.beneficiarioEsSolicitante = beneficiarioEsSolicitante;
    }

    public List<RegpDetalleExoneracion> getDetalleExoneraciones() {
        return detalleExoneraciones;
    }

    public void setDetalleExoneraciones(List<RegpDetalleExoneracion> detalleExoneraciones) {
        this.detalleExoneraciones = detalleExoneraciones;
    }

    public Boolean getAgregaBeneficiario() {
        return agregaBeneficiario;
    }

    public void setAgregaBeneficiario(Boolean agregaBeneficiario) {
        this.agregaBeneficiario = agregaBeneficiario;
    }

    public Boolean getAgregaSolicitante() {
        return agregaSolicitante;
    }

    public void setAgregaSolicitante(Boolean agregaSolicitante) {
        this.agregaSolicitante = agregaSolicitante;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public Integer getAniosDiferencia() {
        return aniosDiferencia;
    }

    public void setAniosDiferencia(Integer aniosDiferencia) {
        this.aniosDiferencia = aniosDiferencia;
    }

    public BigDecimal getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(BigDecimal recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }

    public String valorArancel(RegActo ac) {
        String result = "SIN ARANCEL";

        if (ac.getArancel() != null) {
            if (ac.getArancel().getValor().compareTo(new BigDecimal(-1)) == 0) {
                result = "CONFORME CUANTIA";
            } else {
                result = ac.getArancel().getValor().toString();
            }
        }

        return result;
    }

    public RegpTareasDinardapLazy getTareasLazy() {
        return tareasLazy;
    }

    public void setTareasLazy(RegpTareasDinardapLazy tareasLazy) {
        this.tareasLazy = tareasLazy;
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public LazyModel getFichasRegistrales() {
        return fichasRegistrales;
    }

    public void setFichasRegistrales(LazyModel fichasRegistrales) {
        this.fichasRegistrales = fichasRegistrales;
    }

}
