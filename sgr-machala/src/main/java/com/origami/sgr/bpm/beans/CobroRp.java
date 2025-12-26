/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.GeTipoTramite;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpEstadoPago;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenTipoEntidadBancaria;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.models.PagoModel;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
//import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.origami.sgr.services.interfaces.AsynchronousService;
import java.math.BigInteger;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class CobroRp extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private UserSession us;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private AsynchronousService as;
    @Inject
    private DocumentsManagedLocal docm;
    @Inject
    private RegistroPropiedadServices reg;

    protected LazyModel<RegpLiquidacion> liquidaciones;
    protected RegpLiquidacion liquidacion;
    protected ContenidoReportes proforma;
    protected ContenidoReportes comprobante;
    protected RegRegistrador registrador;
    protected PagoModel modelPago;
    protected HashMap<String, Object> pars;
    protected Integer tipoIngreso = 0;
    protected GeTipoTramite tipoTramite;
    protected RenCajero cajero;
    protected Long referencia;
    protected List<RenEntidadBancaria> bancos;
    protected List<RenEntidadBancaria> bancosDep;
    protected List<RenEntidadBancaria> tarjetas;
    protected Valores valor;
    protected CtlgCatalogo catalogo;
    protected CtlgItem estudioJuridico;
    protected CtlgItem estudioJuridicoNuevo;
    protected CatEnte solicitanteInterviniente;
    protected String infoAdicional;
    protected AclUser asignado;
    protected StreamedContent stream;
    protected HistoricoTramites historico = new HistoricoTramites();
    protected HistoricoTramites ht;
    protected Boolean online = false;
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HistoricTaskInstance tareaActual;
    protected List<RegpLiquidacionDetalles> actosPorPagar;
    protected int priority = 0;
    //private Long usuarioIdFiltro;
    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private ArchivoDocs archivo;
    private String email;

    @PostConstruct
    protected void iniView() {
        try {
            liquidaciones = new LazyModel(RegpLiquidacion.class, "fechaCreacion", "DESC");
            //liquidaciones.addFilter("totalPagar:gt", BigDecimal.ZERO);
            //liquidaciones.addFilter("tramiteOnline", Boolean.FALSE);
            liquidacion = new RegpLiquidacion();
            modelPago = new PagoModel();
            map = new HashMap();
            map.put("code", Constantes.piePaginaProforma);
            proforma = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("code", Constantes.piePaginaComprobante);
            comprobante = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            bancos = manager.findObjectByParameterOrderList(RenEntidadBancaria.class, map,
                    new String[]{"descripcion"}, Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(2L));
            tarjetas = manager.findObjectByParameterOrderList(RenEntidadBancaria.class, map,
                    new String[]{"descripcion"}, Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            map.put("deposito", Boolean.TRUE); 
            bancosDep = manager.findObjectByParameterOrderList(RenEntidadBancaria.class, map,
                    new String[]{"descripcion"}, Boolean.TRUE);
            map = new HashMap();
            map.put("code", Constantes.diasValidezProforma);
            valor = (Valores) manager.findObjectByParameter(Valores.class, map);

            solicitanteInterviniente = new CatEnte();
            map = new HashMap();
            map.put("nombre", Constantes.estudiosJuridicos);
            catalogo = (CtlgCatalogo) manager.findObjectByParameter(CtlgCatalogo.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showProforma(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            ss.agregarParametro("IMG_CUENTAS", JsfUti.getRealPath("/resources/image/bancos_registro.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showTituloCredito(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            //ss.setNombreReporte("titulo_credito");
            ss.setNombreReporte("comprobante_ingreso");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            //ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
            //ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showFormularioUafe(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("formularioUafe");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showComprobanteIngreso(RegpLiquidacion re) {
        try {
            if (re.getEstadoLiquidacion().getId() == 2L) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                if (re.getGeneraFactura()) {
                    ss.setNombreReporte("comprobante_factura");
                } else {
                    ss.setNombreReporte("comprobante_factura_exonerada");
                }
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", re.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "La proforma no ha sido ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void showEnvComprobante(RegpLiquidacion re) {
        try {
            online = false;
            liquidacion = re;
            email = liquidacion.getCorreoTramite();
            this.generarReenvComprobante();
            JsfUti.update("formEnvioCompMail");
            JsfUti.executeJS("PF('dlgReenvioComp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgTipoIngreso(RegpLiquidacion re) {
        try {
            tipoIngreso = 0;
            if (cajero != null) {
                if (this.comprararFechas(re.getFechaCreacion())) {
                    // if (sec.cobroDisponible(re.getId(), us.getUserId())) {
                    liquidacion = re;
                    // LIQUIDACION CON ESTADO ACEPTADO Y VALOR DE PAGO 0.00
                    if (re.getEstadoLiquidacion().getId() == 1L && (re.getTotalPagar().compareTo(BigDecimal.ZERO) == 0)) {
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                    } else if (re.getEstadoLiquidacion().getId() == 1L && re.getEstadoPago().getId() == 1L) { // PENDIENTE DE PAGO
                        tipoIngreso = 1;
                        procesarLiquidacion();
                        /*
                                SE COMENTA ESTO XK ACA NO EXISTEN LAS COMPENSACIONES NI LAS CUENTAS POR COBRAR
                                LOS TRAMITES DE VALOR 0 PASAN DIRECTAMENTE
                         */
                        //JsfUti.update("formIngreso");
                        //JsfUti.executeJS("PF('dlgIngresoTramite').show();");

                    } else if (re.getEstadoPago().getId() == 3L) { // CTA POR COBRAR
                        JsfUti.messageWarning(null, "Proforma ya esta Ingresada como Cta. por Cobrar.", "");
                        /*liquidacion.setPagoFinal(liquidacion.getTotalPagar());
                         modelPago = new PagoModel(liquidacion.getTotalPagar());
                         JsfUti.update("formProcesar");
                         JsfUti.executeJS("PF('dlgProcesar').show();");*/
                    } else if (re.getEstadoPago().getId() == 2L) { // CANCELADO
                        JsfUti.messageWarning(null, "Proforma ya esta Cancelada e Ingresada.", "");
                    } else {
                        JsfUti.messageWarning(null, "Estado de Pago ANULADO o INCOMPLETO.", "");
                    }
//                    } else {
//                        JsfUti.messageError(null, "La liquidación fue ingresada por otro cajero.", "");
//                    }
                } else {
                    JsfUti.messageWarning(null, "La proforma paso fecha limite de validez.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Usuario no tiene Cajero asignado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean comprararFechas(Date in) {
        try {
            Date fecha = Utils.sumarDiasFechaSinWeekEnd(in, valor.getValorNumeric().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date hoy = sdf.parse(sdf.format(new Date()));
            fecha = sdf.parse(sdf.format(fecha));
            return !fecha.before(hoy);
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void showDlgEditProforma(RegpLiquidacion re) {
        try {
            if (re.getEstadoLiquidacion().getId() == 1L || re.getEstadoLiquidacion().getId() == 4L) { // ESTADO LIQUIDACION INGRESADA
                if (Objects.equals(re.getUserCreacion(), us.getUserId())) {
                    ss.instanciarParametros();
                    ss.agregarParametro("proforma", re.getId());
                    JsfUti.redirectFaces("/procesos/registro/editarProforma.xhtml");
                } else {
                    JsfUti.messageWarning(null, "Proforma solo se puede editar por el usuario que la creo.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Proforma no se puede Editar. Ya esta ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditInfoAdiCorreoProf(RegpLiquidacion re) {
        try {
            online = false;
            ht = re.getTramite();
            liquidacion = re;
            JsfUti.update("formInformAdicionalCorreo");
            JsfUti.executeJS("PF('dlgVerEditInfAdCorreo').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void procesarLiquidacion() {
        try {
            if (null != tipoIngreso) {
                switch (tipoIngreso) {
                    case 0:
                        JsfUti.messageWarning(null, "Seleccionar el tipo de Ingreso.", "");
                        break;
                    case 1:
                        if (liquidacion.getEsJuridico()) {
                            System.out.println("liquidacion.getFechaCreacion() : " + liquidacion.getFechaCreacion());
                            if (liquidacion.getRepertorio() != null) {
                                if (validarRepertorio(liquidacion.getFechaCreacion())) {
                                    return;
                                }
                            }
                        }
                        liquidacion.setPagoFinal(liquidacion.getTotalPagar());
                        modelPago = new PagoModel();
                        modelPago.setValorLimite(liquidacion.getTotalPagar());
                        modelPago.setValorRecibido(liquidacion.getTotalPagar());
                        modelPago.setValorCobrar(liquidacion.getTotalPagar());
                        //modelPago.setValorTotalEfectivo(liquidacion.getTotalPagar());
                        modelPago.calcularTotalPago();
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formProcesar");
                        JsfUti.executeJS("PF('dlgProcesar').show();");
                        break;
                    case 2:
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                        break;
                    case 3:
                        JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                        JsfUti.update("formIngresoExo");
                        JsfUti.executeJS("PF('dlgIngresoExo').show();");
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarLiquidacion() {
        try {
            if (modelPago.getValorTotal().compareTo(liquidacion.getTotalPagar()) == 0) {
                if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                    liquidacion.setFechaIngreso(new Date());
                    liquidacion.setUserIngreso(us.getUserId());
                    if (solicitanteInterviniente.getId() != null) {
                        liquidacion.setTramitador(solicitanteInterviniente);
                    }
                    liquidacion.setEstudioJuridico(estudioJuridico);
                    //liquidacion.setInfAdicionalProf(infoAdicional);
                    liquidacion.setEstado(us.getUserId().intValue());
                    liquidacion = itl.cancelarLiquidacion(liquidacion, modelPago.realizarPago(liquidacion), cajero);
                    if (liquidacion != null) {
                        liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                        /*if (!liquidacion.getReingreso()) {
                            if (liquidacion.getGeneraFactura()) {
                                facturacion.emitirFacturaElectronica(liquidacion, cajero);
                            }
                        }*/
                        if (liquidacion.getTramiteOnline()) {
                            this.initTramiteOnline();
                        } else {
                            this.initTramite();
                        }
                    }
                } else {
                    JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                }
            } else {
                JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor ni menor al de la proforma.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void initTramite() {
        this.iniciarTramiteActiviti();
        this.generarComprobante();
    }

    private void initTramiteOnline() {
        //vp.iniciarTramiteActivitiOnline(liquidacion);
        this.generarComprobante();
    }

    public void initTramiteFactura(RegpLiquidacion liq) {
        liquidacion = liq;
        this.iniciarTramiteActiviti();
        JsfUti.messageInfo(null, "Tramite", "Iniciado correctamente");

    }

    public void updateCorreoBeneceificiario() {
        System.out.println("Email: " + liquidacion.getBeneficiario().getCorreo1());
        manager.update(liquidacion.getBeneficiario());
        JsfUti.messageInfo(null, "Correo", "Actualizado correctamente.");
    }

    public void updateInformacionAdicionalLiq() {
        System.out.println("Informacion: " + liquidacion.getInfAdicionalProf());
        manager.update(liquidacion);
        JsfUti.messageInfo(null, "Observaciones", "Actualizado correctamente.");
    }

    public void updateCorreoTramiteLiq() {
        System.out.println("Informacion: " + liquidacion.getCorreoTramite());
        manager.update(liquidacion);
        JsfUti.messageInfo(null, "Correo Tramite", "Actualizado correctamente.");
    }

    public void selectInterv(SelectEvent event) {
        CatEnte in = (CatEnte) event.getObject();
        liquidacion.setBeneficiario(in);
    }

    /*public void generarComprobante() {
        try {
            if (liquidacion.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                if (liquidacion.getGeneraFactura()) {
                    ss.setNombreReporte("comprobante_factura");
                } else {
                    ss.setNombreReporte("comprobante_factura_exonerada");
                }
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));

                JsfUti.executeJS("PF('dlgProcesar').hide();");
                JsfUti.executeJS("PF('dlgIngresoExo').hide();");
                JsfUti.update("formUserAsignado");
                JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }*/
    public void generarComprobante() {
        try {
            if (liquidacion.getId() != null) {
                ss.instanciarParametros();

                ss.setGeneraFile(true);
                ss.setRutaDocumento(SisVars.rutaTemporales + "comprobante-" + liquidacion.getNumTramiteRp() + ".pdf");

                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                ss.setNombreReporte("comprobante_ingreso");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));

                JsfUti.executeJS("PF('dlgIngresoTramite').hide();");
                JsfUti.update("formUserAsignado");
                JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void generarReenvComprobante() {
        try {
            if (liquidacion.getId() != null) {
                ss.instanciarParametros();

                ss.setGeneraFile(true);
                ss.setRutaDocumento(SisVars.rutaTemporales + "comprobante-" + liquidacion.getNumTramiteRp() + ".pdf");

                ss.setTieneDatasource(true);
                ss.setImprimir(Boolean.TRUE);
                ss.setNombreReporte("comprobante_ingreso");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobanteV1() {
        try {
            if (liquidacion.getId() != null) {
                ss.instanciarParametros();

                ss.setGeneraFile(true);
                ss.setRutaDocumento(SisVars.rutaTemporales + "titulo-" + liquidacion.getNumTramiteRp() + ".pdf");

                ss.setTieneDatasource(true);
                ss.setNombreReporte("titulo_credito");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                //ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/formato_titulo.png"));
                //ss.agregarParametro("IMG_FIRMA", JsfUti.getRealPath("/resources/image/firma_titulo_credito.png"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");

                JsfUti.executeJS("PF('dlgProcesar').hide();");
                JsfUti.executeJS("PF('dlgIngresoExo').hide();");
                JsfUti.update("formUserAsignado");
                JsfUti.executeJS("PF('dlgUsuarioAsignado').show();");

                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarTramiteSinValor() {
        try {
            if (liquidacion.getId() != null) {
                liquidacion.setFechaIngreso(new Date());
                liquidacion.setUserIngreso(us.getUserId());
                if (liquidacion.getTotalPagar().compareTo(BigDecimal.ZERO) == 0) {
                    liquidacion.setEstadoPago(new RegpEstadoPago(2L)); //ESTADO PAGO: CANCELADA
                } else if (tipoIngreso == 2) {
                    liquidacion.setEstadoPago(new RegpEstadoPago(3L)); //ESTADO PAGO: CTA. POR COBRAR
                } else if (tipoIngreso == 3) {
                    liquidacion.setEstadoPago(new RegpEstadoPago(4L)); //ESTADO PAGO: COMPENSACION
                }
                if (solicitanteInterviniente.getId() != null) {
                    liquidacion.setTramitador(solicitanteInterviniente);
                }
                if (asignado != null) {
                    liquidacion.setInscriptor(asignado);
                }
                liquidacion.setEstudioJuridico(estudioJuridico);
                //liquidacion.setInfAdicionalProf(infoAdicional);
                liquidacion.setEstado(us.getUserId().intValue());
                //manager.update(liquidacion);
                liquidacion = itl.asignarNroComprobante(liquidacion, cajero);
                if (liquidacion != null) {
                    liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), cajero);
                    if (liquidacion != null) {
                        //facturacion.emitirFacturaElectronicaEnCero(liquidacion, cajero);
                        this.iniciarTramiteActiviti();
                        this.generarComprobante();
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean tramiteReferencia() {
        try {
            map = new HashMap();
            map.put("numTramiteRp", referencia);
            map.put("estadoLiquidacion", new RegpEstadoLiquidacion(2L));
            RegpLiquidacion temp = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            if (temp != null) {
                /*if (!liquidacion.getReingreso()) { //SI LA LIQUIDACION ACTUAL ES REINGRESO NO COPIA LA MISMA FECHA DE ENTREGA DE LA REFERENCIA
                    HistoricoTramites ht = liquidacion.getTramite();
                    ht.setFechaEntrega(temp.getTramite().getFechaEntrega());
                    manager.update(ht);
                }*/
                if (temp.getTramite().getFechaEntrega().after(new Date())) { //SI LA FECHA DE ENTREGA YA PASO, NO SE ASIGNA PARA QUE SE CALCULE UNA NUEVA
                    HistoricoTramites ht = liquidacion.getTramite();
                    ht.setFechaEntrega(temp.getTramite().getFechaEntrega());
                    manager.update(ht);
                }
                liquidacion.setTramiteReferencia(referencia);
                liquidacion.setInscriptor(temp.getInscriptor());
                manager.update(liquidacion);
                return true;
            } else {
                JsfUti.messageError(null, "No se encontró tramite ingresado con el número: " + referencia + ", revisar el número o el estado INGRESADO del trámite.", "");
                return false;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void iniciarTramiteActiviti() {
        if (liquidacion.getObservacionIngreso() != null && !liquidacion.getObservacionIngreso().isEmpty()) {
            reg.guardarObservaciones(liquidacion.getTramite(), session.getName_user(),
                    liquidacion.getObservacionIngreso(), "INGRESO DE TRAMITE");
        } else {
            reg.guardarObservaciones(liquidacion.getTramite(), session.getName_user(),
                    "Trámite ingresado desde ventanilla", "INGRESO DE TRAMITE");
        }
        Boolean result = reg.iniciarTramiteActiviti(liquidacion, false);
        if (!result) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public String getNameUserByIdAclUser(Long id) {
        try {
            if (id != null && id > 0L) {
                return itl.getNameUserByAclUserId(id);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return "";
        }
        return "";
    }

    public void generarXML(RegpLiquidacion re) {
        try {
            if (itl.generarXml(re.getId())) {
                JsfUti.messageInfo(null, "XML generado con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "No se puedo generar XMl.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarXMLWsTest(RegpLiquidacion re) {
        try {
            if (itl.pruebasWsFacturacion(re.getId())) {
                JsfUti.messageInfo(null, "XML generado con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "No se puedo generar XMl.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFactura(RegpLiquidacion re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() == null) {
                JsfUti.messageWarning(null, "La factura electronica aun no esta autorizada.", "");
            } else if (re.getEstadoWs() == null) {
                ruta = Constantes.rutaFeOld + "/" + re.getClaveAcceso() + ".pdf";
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else if (re.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                ruta = Constantes.rutaFeOld + "/factura_" + re.getCodigoComprobante() + ".pdf";
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo. Revisar documento autorizado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadFacturaXml(RegpLiquidacion re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() == null) {
                JsfUti.messageWarning(null, "La factura electronica aun no esta autorizada.", "");
            } else if (re.getEstadoWs() == null) {
                ruta = Constantes.rutaFeOld + "/" + re.getClaveAcceso() + ".xml";
                ss.instanciarParametros();
                ss.setContentType("application/xml");
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else if (re.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                ruta = Constantes.rutaFeOld + "/factura_" + re.getCodigoComprobante() + ".xml";
                ss.instanciarParametros();
                ss.setContentType("application/xml");
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo. Revisar documento autorizado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRIDE(RegpLiquidacion re) {
        try {
            if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                this.showRIDE(re);
            } else {
                re = itl.cargarAutorizacionFactura(re, cajero);
                if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                    this.showRIDE(re);
                } else {
                    JsfUti.messageWarning(null, "No se encontro el archivo autorizado.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showRIDE(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("ride");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("LIQUIDACION", re.getId());
            ss.agregarParametro("FORMA_PAGO", itl.getEstadoPagoLiquidacion(re));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/ingreso/");
            ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/icons/logorp.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgCorrDoc(RegpLiquidacion re) {
        if (re.getEstadoLiquidacion().getId() == 2L) { //TRAMITE INGRESADO
            historico = re.getTramite();
            JsfUti.update("formDocs");
            JsfUti.executeJS("PF('dlgDocs').show();");
        } else {
            JsfUti.messageWarning(null, "El tramite debe estar ingresado para corregir documento.", "");
        }
    }

    public List<CtlgItem> getEstudiosJuridicos() {
        return manager.findAllEntCopy(Querys.getCtlgItemListEstudiosJuridicoa);
    }

    public void showDlgEstudioJuridico() {
        estudioJuridicoNuevo = new CtlgItem();
        JsfUti.update("formEstudioJuridico");
        JsfUti.executeJS("PF('estudioJuridico').show();");
    }

    public void showDlgEditEstudioJuridico() {
        if (estudioJuridico != null) {
            estudioJuridicoNuevo = estudioJuridico;
            //JsfUti.update("formProcesar:tabDetalle:tabInfoPro");.
            JsfUti.update("formEstudioJuridico");
            JsfUti.executeJS("PF('estudioJuridico').show();");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar el elemento para editar.", "");
        }
    }

    public void guardarEstudioJuridico() {
        try {
            if (estudioJuridicoNuevo.getValor() != null) {
                estudioJuridicoNuevo.setEstado("A");
                estudioJuridicoNuevo.setCatalogo(catalogo);
                estudioJuridicoNuevo.setCodename(estudioJuridicoNuevo.getValor().trim().toLowerCase());
                estudioJuridico = (CtlgItem) manager.persist(estudioJuridicoNuevo);
                estudioJuridicoNuevo = new CtlgItem();
                if (tipoIngreso == 1) {
                    JsfUti.update("formProcesar:tabDetalle:panelEstudio");
                } else {
                    JsfUti.update("formIngresoExo:tabDetalleIng:pnlEstudio");
                }
                JsfUti.executeJS("PF('estudioJuridico').hide();");
            } else {
                JsfUti.messageWarning(null, "El campo nombre esta vacio.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectObject(SelectEvent event) {
        solicitanteInterviniente = (CatEnte) event.getObject();
    }

    public void handleUpload(FileUploadEvent event) throws IOException {
        try {
            if (historico.getId() != null) {
                if (docm.saveDocumentoHabilitante(event.getFile(), historico, session.getName_user())) {
                    JsfUti.update("mainForm");
                    JsfUti.executeJS("PF('dlgDocs').hide();");
                    JsfUti.messageInfo(null, "Reemplazo de Documento con exito!!!", "");
                } else {
                    JsfUti.messageError(null, "ERROR al subir el archivo!!!", "");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void generarNuevaFactura(RegpLiquidacion re) {
        try {

            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            RenCajero temp = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            //facturacion.emitirFacturaElectronica(re, temp);
            JsfUti.messageInfo(null, "Se confirma la emision del comprobante elecronico.", "");

            /*if (re.getNumeroComprobante().compareTo(BigInteger.ZERO) == 0
                    && re.getTotalPagar().compareTo(BigDecimal.ZERO) > 0
                    && re.getEstadoLiquidacion().getId() == 2L) {
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                //map.put("usuario", new AclUser(re.getUserIngreso()));
                map.put("usuario", new AclUser(us.getUserId()));
                RenCajero temp = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                facturacion.emitirFacturaElectronica(re, temp);
                JsfUti.messageInfo(null, "Se confirma la emision del comprobante elecronico.", "");
            } else {
                JsfUti.messageWarning(null, "Revisar los datos de la proforma.", "");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pruebasFacturacion(RegpLiquidacion re) {
        try {
            itl.pruebasWsFacturacion(re.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void desblockLiquidacion(RegpLiquidacion re) {
        try {
            re.setEstado(0);
            manager.update(re);
            JsfUti.messageInfo(null, "OK.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void viewProcess(RegpLiquidacion rl) {
        try {
            if (rl.getTramite() != null) {
                if (rl.getTramite().getIdProceso() != null) {
                    stream = DefaultStreamedContent.builder().contentType("image/png")
                            .stream(() -> engine.getProcessInstanceDiagram(rl.getTramite().getIdProceso())).build();
                    JsfUti.executeJS("PF('dlgDiagrama').show()");
                    JsfUti.update("frmDiagrama");
                    return;
                }
            }
            JsfUti.messageError(null, "NO se pudo generar diagrama del proceso", "");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showInfo(RegpLiquidacion re) {
        try {
            online = false;
            ht = re.getTramite();
            liquidacion = re;
            // Traer documentos
            lazyArchivos = new LazyModelWS<>(SisVars.urlOrigamiDocs + "misDocumentos?numTramite="
                    + ht.getNumTramite(), session.getToken());
            lazyArchivos.setEntitiArray(ArchivoDocs[].class);
            if (liquidacion.getTramite().getIdProceso() == null) {
                tareas = new ArrayList<>();
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(liquidacion.getTramite().getIdProceso());
                if (!tareas.isEmpty()) {
                    tareaActual = tareas.get(0);
                    priority = tareaActual.getPriority();
                }
                if (liquidacion.getEstadoPago().getId() == 7L) {
                    online = true;
                }
            }
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgIngresoEgob(RegpLiquidacion liq) {
        try {
            liquidacion = liq;
            actosPorPagar = itl.getActosPorPagar(liquidacion);
            JsfUti.update("formTramiteGenerado");
            JsfUti.executeJS("PF('dlgIngresoTramite').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void ingresarTramiteEgob() {
        try {
            if (liquidacion.getId() != null) {
                for (RegpLiquidacionDetalles det : actosPorPagar) {
                    if (det.getComprobante() == null || det.getComprobante().isEmpty()) {
                        JsfUti.messageWarning(null, "Es obligatorio ingresar el campo Comprobante.", "");
                        return;
                    }
                    if (det.getReferencia() == null || det.getReferencia().compareTo(BigInteger.ONE) < 0) {
                        JsfUti.messageWarning(null, "Es obligatorio ingresar el campo Referencia.", "");
                        return;
                    }
                }
                liquidacion.setFechaIngreso(new Date());
                liquidacion.setUserIngreso(session.getUserId());
                liquidacion.setEstado(session.getUserId().intValue());
                liquidacion = itl.asignarNroComprobante(liquidacion, null);
                if (liquidacion != null) {
                    itl.updateActosPorPagar(actosPorPagar);
                    liquidacion = itl.asignarUsuarioSecuencias(liquidacion.getId(), null);
                    if (liquidacion != null) {
                        this.iniciarTramiteActiviti();
                        this.generarComprobante();
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizarDocumento(ArchivoDocs docs) {
        JsfUti.redirectNewTab(SisVars.urlbase + "documental/digitalizacion/viewerDocs.xhtml?archivoId=" + docs.getId());
    }

    public void descargarDocumento(ArchivoDocs doc) {
        try {
            //inline
            //JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/pdf/" + doc.getNombre());
            //attachment
            JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/document/" + doc.getNombre());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public void continuarCobro() {
        try {
            if (liquidacion.getId() != null) {
                String rutaDocumento = SisVars.rutaTemporales + "comprobante-" + liquidacion.getNumTramiteRp() + ".pdf";
                as.enviarCorreoTituloCredito(liquidacion, rutaDocumento, session.getName_user());
                JsfUti.redirectFaces("/procesos/registro/cobroRp.xhtml");
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }

    }

    public List<AclUser> getDisponibles() {
        return manager.findAllEntCopy(Querys.getUsuarios);
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public LazyModel<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(LazyModel<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public PagoModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoModel modelPago) {
        this.modelPago = modelPago;
    }

    public Integer getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(Integer tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public List<RenEntidadBancaria> getBancos() {
        return bancos;
    }

    public void setBancos(List<RenEntidadBancaria> bancos) {
        this.bancos = bancos;
    }

    public List<RenEntidadBancaria> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<RenEntidadBancaria> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public HistoricoTramites getHistorico() {
        return historico;
    }

    public void setHistorico(HistoricoTramites historico) {
        this.historico = historico;
    }

    public CtlgItem getEstudioJuridico() {
        return estudioJuridico;
    }

    public void setEstudioJuridico(CtlgItem estudioJuridico) {
        this.estudioJuridico = estudioJuridico;
    }

    public CtlgItem getEstudioJuridicoNuevo() {
        return estudioJuridicoNuevo;
    }

    public void setEstudioJuridicoNuevo(CtlgItem estudioJuridicoNuevo) {
        this.estudioJuridicoNuevo = estudioJuridicoNuevo;
    }

    public CatEnte getSolicitanteInterviniente() {
        return solicitanteInterviniente;
    }

    public void setSolicitanteInterviniente(CatEnte solicitanteInterviniente) {
        this.solicitanteInterviniente = solicitanteInterviniente;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public AclUser getAsignado() {
        return asignado;
    }

    public void setAsignado(AclUser asignado) {
        this.asignado = asignado;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public List<RegpLiquidacionDetalles> getActosPorPagar() {
        return actosPorPagar;
    }

    public void setActosPorPagar(List<RegpLiquidacionDetalles> actosPorPagar) {
        this.actosPorPagar = actosPorPagar;
    }

    public LazyModelWS<ArchivoDocs> getLazyArchivos() {
        return lazyArchivos;
    }

    public void setLazyArchivos(LazyModelWS<ArchivoDocs> lazyArchivos) {
        this.lazyArchivos = lazyArchivos;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public List<RenEntidadBancaria> getBancosDep() {
        return bancosDep;
    }

    public void setBancosDep(List<RenEntidadBancaria> bancosDep) {
        this.bancosDep = bancosDep;
    }

}
