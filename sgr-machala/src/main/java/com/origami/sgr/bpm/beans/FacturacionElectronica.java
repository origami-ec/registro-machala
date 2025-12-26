/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.ebilling.models.ComprobanteSRI;
import com.origami.sgr.ebilling.services.OrigamiGTService;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.CtlgCatalogo;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegActo;
import com.origami.sgr.entities.RegPapel;
import com.origami.sgr.entities.RegTipoCobroActo;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpExoneracion;
import com.origami.sgr.entities.RegpIntervinientes;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenFactura;
import com.origami.sgr.entities.RenNotaCredito;
import com.origami.sgr.entities.RenPagoRubro;
import com.origami.sgr.entities.RenTipoEntidadBancaria;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RenFacturaLazy;
import com.origami.sgr.lazymodels.RenNotaCreditoLazy;
import com.origami.sgr.models.FacturaConsultaErp;
import com.origami.sgr.models.FacturaEmitirErp;
import com.origami.sgr.models.FacturaModelo;
import com.origami.sgr.models.FacturaRespuestaERP;
import com.origami.sgr.models.PagoModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.EntityBeanCopy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FacturacionElectronica extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(FacturacionElectronica.class.getName());

    //@Inject
    //private Entitymanager em;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private AsynchronousService as;
    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private OrigamiGTService origami;
    @Inject
    private RegistroPropiedadServices reg;

    protected RenNotaCreditoLazy lazy;
    protected int dias = 0;
    protected Map map;
    protected RenCajero cajero;
    protected Date fecha = new Date();
    protected Date ingreso = new Date();
    protected Date desde = new Date();
    protected Date hasta = new Date();
    protected List<RegpLiquidacion> liquidaciones, facturas;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected List<AclUser> cajeros = new ArrayList<>();
    protected AclUser user, caja;
    protected Boolean flag = Boolean.FALSE;
    protected Boolean block = Boolean.FALSE;
    protected Boolean propiedad = null;
    protected CatEnte solicitante = new CatEnte();
    protected Integer tipoTarea = 0, estado = 1;
    protected Long tramite;
    protected String observacion;
    protected RegpLiquidacion proforma = new RegpLiquidacion();

    protected CatEnte ente;
    protected Long numerotramite;
    protected String formaPago;
    protected String cedula = "";
    protected RenFacturaLazy emisiones;

    //VARIABLLEES PARA AGRREGAR ACTOS A LA FACTURA =O
    protected LazyModel<RegActo> actos;
    protected LazyModel<RegActo> actosConSolvenciaFalse;
    protected List<RegpIntervinientes> listInterv;
    protected RegpIntervinientes interviniente;
    private Integer aniosDiferencia;
    protected RegActo acto;
    protected RegpLiquidacionDetalles rld = new RegpLiquidacionDetalles();
    protected boolean editar = false;
    protected boolean certificado = false;
    protected boolean diferenciaPagos = false;
    protected CtlgCatalogo catalogo;
    protected CtlgItem usoDocumento;
    protected CtlgItem nuevoUsoDoc;
    protected String nombre = "";
    protected String obsAdicional = "";
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
    protected BigDecimal adicional = BigDecimal.ZERO;
    protected BigDecimal valorDiferenciaActo = BigDecimal.ZERO;
    protected Integer indice;
    protected List<RegpLiquidacionDetalles> actosPorPagar;
    protected int indiceActo = 0;
    private Boolean beneficiarioEsSolicitante, agregaBeneficiario, agregaSolicitante, facturaSinTramite = false;
    private Integer indexBeneficiario, indexSolicitante;
    private String email, nombresFact;
    //VARIABLES PARA LA REFORMA DE TABLA DE ARANCELES
    protected Integer tipocalculo = 0;
    protected RegpLiquidacion liquidacion;
    protected Valores valor;
    protected List<RenEntidadBancaria> tarjetas;
    protected PagoModel modelPago;
    protected List<RenEntidadBancaria> bancos;
    protected List<RenEntidadBancaria> bancosDep;
    private String obs;
    private RenFactura factura;

    private List<ComprobanteSRI> comprobantesElectronicos;
    private ComprobanteSRI comprobanteSRI;
    protected Valores limiteActo;
    protected Valores salarioBasico;
    protected Valores limiteFactura;
    protected String jsonRespuesta;

    @PostConstruct
    protected void iniView() {
        try {
            actos = new LazyModel(RegActo.class, "nombre", "ASC");
            actos.addFilter("tipoCobro", new RegTipoCobroActo(2L)); //ACTOS CON ARANCELES

            actosConSolvenciaFalse = new LazyModel<>(RegActo.class, "nombre", "ASC");
            actosConSolvenciaFalse.addFilter("tipoCobro", new RegTipoCobroActo(2L));
            actosConSolvenciaFalse.addFilter("solvencia", false);

            lazy = new RenNotaCreditoLazy();
            liquidaciones = new ArrayList<>();
            facturas = new ArrayList<>();
            cajeros = itl.getUsuariosByRolName("cajero");
            emisiones = new RenFacturaLazy();
            ente = new CatEnte();
            listInterv = new ArrayList<>();
            acto = new RegActo();
            actosPorPagar = new ArrayList<>();
            map = new HashMap();
            map.put("code", Constantes.diasValidezProforma);
            valor = (Valores) manager.findObjectByParameter(Valores.class, map);
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", new AclUser(us.getUserId()));
            cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            map = new HashMap();
            map.put("estado", Boolean.TRUE);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            bancos = manager.findObjectByParameterList(RenEntidadBancaria.class, map);
            map.put("tipo", new RenTipoEntidadBancaria(2L));
            tarjetas = manager.findObjectByParameterList(RenEntidadBancaria.class, map);
            map.put("tipo", new RenTipoEntidadBancaria(1L));
            map.put("deposito", Boolean.TRUE); 
            bancosDep = manager.findObjectByParameterOrderList(RenEntidadBancaria.class, map,
                    new String[]{"descripcion"}, Boolean.TRUE);
            map = new HashMap();
            map.put("code", Constantes.limiteValorContrato);
            limiteActo = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.limiteFactura);
            limiteFactura = (Valores) manager.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("code", Constantes.salarioBasicoUnificado);
            salarioBasico = (Valores) manager.findObjectByParameter(Valores.class, map);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarComprobantes() {
        if (solicitante == null || solicitante.getId() == null) {
            JsfUti.messageWarning(null, "Debe seleccionar un Solicitante.", "");
        } else {
            //comprobantesElectronicos = fac.getAllComprobanteByCedula(solicitante.getCiRuc());
        }
    }

    public void onRowSelect() {
        try {
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
            diferenciaPagos = false;
            this.verTipoCalculo();
            rld = new RegpLiquidacionDetalles();
            rld.setCantidad(1);
            rld.setActo(acto);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setRecargo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setCantidadIntervinientes(1);
            if (acto.getTipoActo() != null) {
                String name = acto.getTipoActo().getNombre().toUpperCase();
                if (name.contains("HISTORIADO")) {
                    certificado = true;
                }
                if (name.contains("DIFERENCIA")) {
                    diferenciaPagos = true;
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

    public void verTipoCalculo() {
        tipocalculo = 0;
        if (acto.getTipoCobro() != null) {
            tipocalculo = acto.getTipoCobro().getId().intValue();
        }
    }

    public void cargarFacturasAutorizadas() {
        try {
            if (user != null && fecha != null) {
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", user);
                cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                if (cajero != null) {
                    liquidaciones = itl.cargarFacturasNoEnviadas(sdf.format(fecha), user.getId());
                    if (liquidaciones.isEmpty()) {
                        JsfUti.messageWarning(null, "No se han cargado facturas para generar RIDE.", "");
                    }
                    JsfUti.update("mainForm");
                } else {
                    JsfUti.messageWarning(null, "Usuario no tiene Caja asignada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el cajero y fecha de ingreso.", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRIDE() {
        try {
            if (liquidaciones.isEmpty()) {
                JsfUti.messageWarning(null, "No se han cargado facturas para generar RIDE.", "");
            } else if (cajero != null) {
                map = new HashMap();
                map.put("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/ingreso/");
                map.put("LOGO_URL", JsfUti.getRealPath("/resources/icons/logorp.png"));
                flag = itl.generarRIDE(liquidaciones, cajero, JsfUti.getRealPath("/reportes/ingreso/ride.jasper"), map);
                if (flag) {
                    JsfUti.messageInfo(null, "Se generaron los RIDEs con exito.", "");
                } else {
                    JsfUti.messageWarning(null, "Problemas al generar RIDE.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarNotificacion(RegpLiquidacion re) {
        try {
            if (re.getFechaAutorizacion() != null && re.getNumeroAutorizacion() != null) {
                if (itl.envioCorreoFacturaElectronica(re, cajero)) {
                    JsfUti.messageInfo(null, "Se envio el correo con EXITO.", "");
                } else {
                    JsfUti.messageError(null, "NO se envio el correo electronico.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se pudo enviar el correo electronico.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarTodosLosCorreos() {
        try {
            if (liquidaciones.isEmpty()) {
                JsfUti.messageWarning(null, "No hay facturas para enviar.", "");
            } else if (flag) {
                as.enviarCorreosRIDE(liquidaciones, cajero);
                block = true;
                JsfUti.update("mainForm:tabFacturacion:pnlComandos");
                JsfUti.messageInfo(null, "Se estan enviando los correos electronicos.", "");
            } else {
                JsfUti.messageWarning(null, "Debe generar RIDEs para enviar los correos electronicos.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findLiquidacion() {
        try {
            if (tramite != null) {
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                proforma = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                if (proforma == null) {
                    proforma = new RegpLiquidacion();
                    JsfUti.messageWarning(null, "No se encuentra el Tramite.", "");
                }

                if (proforma.getEstadoLiquidacion().getId() != 1L) {
                    JsfUti.messageWarning(null, "No se puede inactivar Proforma.", "");
                    proforma = new RegpLiquidacion();
                }
                JsfUti.update("mainForm");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de Tramite.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void tareasFacturas() {
        if (proforma.getId() != null) {
            switch (tipoTarea) {
                case 1:
                    this.showDlgAnulacion();
                    break;
                case 2:
                    this.generarNuevaFactura();
                    break;
                default:
                    JsfUti.messageWarning(null, "Debe seleccionar la tarea para la factura.", "");
                    break;
            }
        } else {
            JsfUti.messageWarning(null, "Debe buscar la proforma.", "");
        }
    }

    public void showDlgAnulacion() {
        if (proforma.getEstadoLiquidacion().getId() == 3L) {
            JsfUti.messageWarning(null, "La proforma ya fue anulada.", "");
        } else {
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        }
    }

    public void anularTramite() {
        try {
            if (observacion != null) {
                proforma.setInfAdicional(observacion);
                proforma.setUserAnula(us.getUserId());
                proforma.setFechaAnulacion(new Date());
                proforma.setEstadoLiquidacion(new RegpEstadoLiquidacion(3L));
                manager.merge(proforma);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgObsvs').hide();");
                JsfUti.messageInfo(null, Messages.liquidacionAnulada, "");
                proforma = new RegpLiquidacion();
                observacion = "";
            } else {
                JsfUti.messageWarning(null, "Debe ingresar la observacion de la anulacion.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarNuevaFactura() {
        try {
            if (proforma.getEstadoLiquidacion().getId() == 3L
                    && proforma.getNumeroComprobante().compareTo(BigInteger.ZERO) > 0) {
                if (itl.nuevaFacturaTramiteExistente(proforma)) {
                    block = Boolean.TRUE;
                    JsfUti.update("mainForm");
                    JsfUti.messageInfo(null, "Ya hay nueva factura para el tramite.", "");
                } else {
                    JsfUti.messageWarning(null, Messages.error, "");
                }
            } else {
                JsfUti.messageWarning(null, "La factura debe estar anulada para generar una nueva.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarFacturasElectronicas() {
        try {
            map = new HashMap();
            map.put("ingreso", sdf.format(ingreso));
            if (caja != null && ingreso != null) {
                map.put("usuario", caja.getId());
                if (estado == 1) {
                    facturas = manager.findNamedQuery(Querys.getFacturasAutorizadas, map);
                } else {
                    facturas = manager.findNamedQuery(Querys.getFacturasNoAutorizadas, map);
                }
            } else {
                if (estado == 1) {
                    facturas = manager.findNamedQuery(Querys.getFacturasAutorizadasAllUser, map);
                } else {
                    facturas = manager.findNamedQuery(Querys.getFacturasNoAutorizadasAllUser, map);
                }
            }
            JsfUti.update("mainForm");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargarCorreoFacturaElectronicaSRI(ComprobanteSRI comprobanteSRI) {
        this.comprobanteSRI = comprobanteSRI;
        JsfUti.update("formCorreo");
        JsfUti.executeJS("PF('dlgReenvioCorreo').show();");
    }

    public void reenviarCorreoFacturaElectronicaSRI() {
        //Systmanager.out.println("comprobanteSRI " + comprobanteSRI.getContribuyente().getEmail());
        //fac.reenviarCorreoFacturaElectronicaSRI(comprobanteSRI);
        JsfUti.messageWarning(null, "Correo enviado Correctamente.", "");
        JsfUti.update("mainForm");
        JsfUti.executeJS("PF('dlgReenvioCorreo').hide();");
    }

    public void reenvioFacturas(RegpLiquidacion re) {
        try {
            /*if (re.getEstadoWs() != null && re.getEstadoWs().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                JsfUti.messageWarning(null, "Factura con estado: AUTORIZADO, no se necesita comprobar el reenvio.", "");
            } else { }*/
            AclUser temp = manager.find(AclUser.class, re.getUserIngreso());
            map = new HashMap();
            map.put("habilitado", Boolean.TRUE);
            map.put("usuario", temp);
            cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
            if (cajero != null) {
                //fac.reenviarFacturaElectronica(re, cajero, Boolean.FALSE);
                if (this.estado == 2) {
                    this.consultarFacturasElectronicas();
                }
                JsfUti.messageWarning(null, "Factura reenviada, debe esperar unos minutos.", "");
            } else {
                JsfUti.messageWarning(null, "No se encuentra caja de ingreso de Tramite.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlg(String urlFacelet) {
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

    public void showDlgPapel(String urlFacelet, int indice) {
        interviniente = listInterv.get(indice);
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

    public void selectObjectExo(SelectEvent event) {
        RegpExoneracion ex = (RegpExoneracion) event.getObject();
        //exoneracion.setExoneracion(ex);
        interviniente.setExoneracion(ex);
    }

    public void eliminarInterviniente(int index) {
        try {
            RegpIntervinientes in = listInterv.get(index);
            listInterv.remove(index);
            if (in.getId() != null) {
                manager.delete(in);
            }
        } catch (Exception e) {
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
                        //rld.setValorUnitario(itl.calculoCuantia(avaluo, salarioBasico.getValorNumeric()));
                        rld.setValorUnitario(itl.calculoCuantia(avaluo));
                    } else {
                        rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo));
                    }
                } else {
                    if (this.propiedad) {
                        //rld.setValorUnitario(itl.calculoCuantia(cuantia, salarioBasico.getValorNumeric()));
                        rld.setValorUnitario(itl.calculoCuantia(cuantia));
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
                    //rld.setValorUnitario(itl.calculoCuantia(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                    rld.setValorUnitario(itl.calculoCuantia(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                } else {
                    rld.setValorUnitario(itl.calculoCuantiaDeterminada(avaluo.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
                }
            } else {
                if (this.propiedad) {
                    //rld.setValorUnitario(itl.calculoCuantia(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP), salarioBasico.getValorNumeric()));
                    rld.setValorUnitario(itl.calculoCuantia(cuantia.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP)));
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
            if (!acto.getSolvencia() && rei.getPapel() == null) {
                JsfUti.messageWarning(null, "Debe ingresar el papel de el/los intervinientes del contrato.", "");
                return false;
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
        //Boolean certificados = false;
        if (rld.getReingreso()) {
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
        } else {
            rld.setRecargo(BigDecimal.ZERO);
            rld.setSubtotal(rld.getValorUnitario().multiply(new BigDecimal(rld.getCantidad())));
            if (!rld.getActo().getSolvencia()) {
                if (!rld.getActo().getArancel().getValorFijo()) {
                    rld.setRecargo(rld.getSubtotal().multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP));
                } else if (rld.getSubtotal().compareTo(new BigDecimal("20.00")) >= 0) {
                    rld.setRecargo(rld.getSubtotal().multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP));
                }
            }
            rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
        }

        if (rld.getSubtotal().compareTo(limiteActo.getValorNumeric()) > 0) {
            rld.setRecargo(BigDecimal.ZERO);
            rld.setValorUnitario(limiteActo.getValorNumeric());
            rld.setSubtotal(limiteActo.getValorNumeric());
            rld.setValorTotal(limiteActo.getValorNumeric());
        }
        //rld.setValorTotal(rld.getSubtotal().add(rld.getRecargo()).setScale(2, RoundingMode.HALF_UP));
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
        rld.setRegpIntervinientesCollection(temp);
        if (editar) {
            actosPorPagar.add(indiceActo, rld);
        } else {
            if (rld.getFechaIngreso() == null) {
                rld.setFechaIngreso(new Date());
            }
            actosPorPagar.add(rld);
        }
        /*
        if (certificados) {
            this.agregarCertificados();
        }*/
        this.calculoTotalPagar();
        JsfUti.update("mainForm:pnlContratos");
        JsfUti.executeJS("PF('dlgCuantia').hide();");
        JsfUti.executeJS("PF('dglEditCuantia').hide();");
    }

    public void eliminarDetalle(int rowIndex) {
        try {
            RegpLiquidacionDetalles de = actosPorPagar.remove(rowIndex);
            if (de.getId() != null) {
                manager.delete(de);
            }
            this.calculoTotalPagar();
            JsfUti.update("mainForm:tabViewAnulacion:pnlContratos");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarCertificados() {
        RegActo reg = manager.find(RegActo.class, 11L);
        map = new HashMap();
        map.put("code", Constantes.cantidadCertificadosPH);
        Valores temp = (Valores) manager.findObjectByParameter(Valores.class, map);
        for (int i = 0; i < temp.getValorNumeric().intValue(); i++) {
            //Systmanager.out.println("// cada ingreso...");
            rld = new RegpLiquidacionDetalles();
            rld.setActo(reg);
            rld.setAvaluo(BigDecimal.ZERO);
            rld.setCuantia(BigDecimal.ZERO);
            rld.setDescuento(BigDecimal.ZERO);
            rld.setValorUnitario(BigDecimal.ZERO.setScale(2));
            rld.setValorTotal(BigDecimal.ZERO.setScale(2));
            rld.setFechaIngreso(new Date());
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

    public void selectObject(SelectEvent event) {
        solicitante = (CatEnte) event.getObject();
        consultarComprobantes();
    }

    public void downLoadFacturas() {
        File file, temp = null;
        FileInputStream fi;
        List<InputStream> fis = new ArrayList<>();
        try {
            if (solicitante.getId() == null || desde == null || hasta == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el solicitante y escoger las fechas correctamente.", "");
            } else if (desde.after(hasta)) {
                JsfUti.messageWarning(null, "Debe seleccionar las fechas correctamente.", "");
            } else {
                List<RegpLiquidacion> list = itl.getComprobantesBySolicitante(solicitante.getId(), desde, Utils.sumarRestarDiasFecha(hasta, 1));
                System.out.println("// " + list);
                /*List<RegpLiquidacion> list = itl.getComprobantesBySolicitante(solicitante.getId(), sdf.format(desde),
                        sdf.format(Utils.sumarRestarDiasFecha(hasta, 1)));*/
                if (list != null && !list.isEmpty()) {
                    for (RegpLiquidacion cod : list) {
                        if (cod.getEstadoWs() == null) {
                            temp = new File(Constantes.rutaFeOld + "/" + cod.getClaveAcceso() + ".pdf");
                        } else if (cod.getEstadoWs().equalsIgnoreCase("AUTORIZADO")) {
                            temp = new File(Constantes.rutaFeOld + "/factura_" + cod.getCodigoComprobante() + ".pdf");
                        }
                        if (temp != null && temp.exists()) {
                            fi = new FileInputStream(temp);
                            fis.add(fi);
                        }
                    }
                    file = itl.mergeFilesPdf(fis);
                    if (file != null) {
                        ss.instanciarParametros();
                        ss.setNombreDocumento(file.getAbsolutePath());
                        JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
                    } else {
                        JsfUti.messageWarning(null, "No se pudo generar el Archivo.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "No se encontraron facturas a nombre del solicitante.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectSolicitante(SelectEvent event) {
        ente = (CatEnte) event.getObject();
    }

    public void limpiarDatosBeneficiario() {
        ente = new CatEnte();
    }

    public void facturaElectronica() {
        try {
            if (ente.getId() != null) {
                if (!facturaSinTramite) {
                    if (numerotramite == null) {
                        JsfUti.messageWarning(null, "Debe Ingresar un Número de Trámite", "");
                        return;
                    }
                }
                map = new HashMap();
                map.put("habilitado", Boolean.TRUE);
                map.put("usuario", new AclUser(us.getUserId()));
                RenCajero temp = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);
                if (temp != null) {
                    if (!facturaSinTramite) {
                        map = new HashMap();
                        map.put("numTramiteRp", numerotramite);
                        liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    } else {
                        liquidacion = new RegpLiquidacion();
                        liquidacion.setLiquidacionSinTramite(Boolean.TRUE);
                    }
                    if (liquidacion.getEstadoLiquidacion().getId() == 2 && liquidacion.getEstadoPago().getId() == 2) {
                        liquidacionTemp();
                    } else {
                        JsfUti.messageWarning(null, "El alcance de pago solo se realiza a tramites ingresados", "");
                    }

                } else {
                    JsfUti.messageWarning(null, "El usuario debe tener caja asignada.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe llenar todos los datos para generar la factura.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarBeneficiario() {
        if (ente.getCiRuc() != null || ente.getCiRuc().isEmpty()) {
            if (!ente.getCiRuc().isEmpty()) {
                cedula = ente.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    ente = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                } else {
                    ente = new CatEnte();
                }
                if (ente == null || ente.getId() == null) {
                    ente = reg.buscarGuardarEnteDinardap(cedula);
                }

            }
            if (ente == null) {
                ente = new CatEnte();
            }
            if (ente.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                JsfUti.update("mainForm:pnlSolicitante");
            }
        }
    }

    public void selectObjectPapel(SelectEvent event) {
        RegPapel pa = (RegPapel) event.getObject();
        interviniente.setPapel(pa);
    }

    public void liquidacionTemp() {
        try {
            liquidacion.setSolicitante(ente);
            liquidacion.setFechaCreacion(new Date());
            liquidacion.setUserCreacion(us.getUserId());
            liquidacion.setSubTotal(subTotal);
            liquidacion.setValorActos(subTotalDesc);
            liquidacion.setAdicional(recargoAplicado);
            liquidacion.setDescLimitCobro(descLimitCobro);
            liquidacion.setDescuentoValor(descPorLey);
            liquidacion.setDescuentoPorc(porcPago);
            liquidacion.setGastosGenerales(gastosGenerales);
            liquidacion.setTotalPagar(totalPagar);

            if (totalPagar.compareTo(BigDecimal.ZERO) == 0) {
                liquidacion.setGeneraFactura(Boolean.FALSE);
            } else {
                liquidacion.setGeneraFactura(Boolean.TRUE);
            }
            if (liquidacion.getRegpLiquidacionDetallesCollection() != null) {
                liquidacion.getRegpLiquidacionDetallesCollection().clear();
            } else {
                liquidacion.setRegpLiquidacionDetallesCollection(new ArrayList());
            }
            liquidacion.setRegpLiquidacionDetallesCollection(actosPorPagar);

            liquidacion.setPagoFinal(liquidacion.getTotalPagar());
            modelPago = new PagoModel();
            modelPago.setValorLimite(liquidacion.getTotalPagar());
            modelPago.setValorRecibido(liquidacion.getTotalPagar());
            modelPago.setValorCobrar(liquidacion.getTotalPagar());
            //modelPago.setValorTotalEfectivo(liquidacion.getTotalPagar());
            modelPago.calcularTotalPago();
            JsfUti.update("formProcesar");
            JsfUti.executeJS("PF('dlgProcesar').show();");

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelarLiquidacion() {
        try {
            if (obs != null && !obs.isEmpty()) {
                if (modelPago.getValorTotal().compareTo(liquidacion.getTotalPagar()) == 0) {
                    if (modelPago.getValorTotal().compareTo(BigDecimal.ZERO) >= 0) {
                        factura = itl.emitirFacturaSinTramiteSinGuardarRenLiquidacionSoloRenPago(liquidacion, modelPago.realizarPago(liquidacion), cajero, obs);
                        if (factura != null) {
                            this.generarComprobante();
                        } else {
                            JsfUti.messageWarning(null, "Ocurrio un error al generar la Factura", "");
                        }
                    } else {
                        JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                    }
                } else {
                    JsfUti.messageWarning(null, "Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor ni menor al de la proforma.");
                }
            } else {
                JsfUti.messageWarning(null, "Ingrese una Observación", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobante() {
        try {
            if (factura.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("comprobante_ingreso_alcance");

                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", factura.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
                ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/image/logo_comprobante.jpg"));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
                //ss.agregarParametro("FOOTER", comprobante.getValor());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(liquidacion.getTotalPagar().toString()));
                List<String> urlList = new ArrayList<>();
                String url = "/procesos/tesoreria/alcancePagos.xhtml";
                urlList.add(SisVars.urlbase + "Documento");

                String rutaDocumento = SisVars.rutaTemporales + "comprobante-" + liquidacion.getNumTramiteRp() + ".pdf";
                as.enviarCorreoTituloCredito(liquidacion, rutaDocumento, session.getName_user());

                JsfUti.redirectMultipleConIP_V2(url, urlList);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobante(Long idfactura, BigDecimal totalPagar) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("ingreso");
            ss.setNombreReporte("comprobante_ingreso_alcance");
            ss.agregarParametro("ID_LIQUIDACION", idfactura);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("LOGO_URL", JsfUti.getRealPath("/resources/image/logo_comprobante.jpg"));
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(totalPagar.toString()));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showEnvComprobante(RenFactura re) {
        try {
            liquidacion = re.getLiquidacion();
            email = liquidacion.getCorreoTramite();
            nombresFact = re.getSolicitante().getNombreCompleto();
            this.generarComprobante(re.getId(), re.getTotalPagar());
            JsfUti.update("formEnvioCompMail");
            JsfUti.executeJS("PF('dlgReenvioComp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void continuarEnvCorreo() {
        try {
            if (liquidacion.getId() != null) {
                String rutaDocumento = SisVars.rutaTemporales + "comprobante-" + liquidacion.getNumTramiteRp() + ".pdf";
                as.enviarCorreoTituloCredito(liquidacion, rutaDocumento, session.getName_user());
                JsfUti.redirectFaces("/procesos/tesoreria/facturacionElectronica.xhtml");
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }

    }

    public void downloadFactura(RenFactura re) {
        String ruta;
        try {
            if (re.getNumeroAutorizacion() != null && re.getCodigoComprobante() != null) {
                ruta = Constantes.rutaFeOld + "factura_" + re.getCodigoComprobante().replace("-", "").trim() + ".pdf";
                System.out.println("ruta " + ruta);
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico aun no esta autorizada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenvioEmision(RenFactura nc) {
        try {
            if (nc.getEstadoWs() != null) {
                if (!nc.getEstadoWs().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                    reenvio(nc);
                } else {
                    JsfUti.messageWarning(null, "El comprobante electronico ya tiene estado AUTORIZADO.", "");
                }
            } else {
                reenvio(nc);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void reenvio(RenFactura renFactura) {
//        map = new HashMap();
//        map.put("habilitado", Boolean.TRUE);
//        map.put("id", renFactura.getCaja());
//        RenCajero cajero = (RenCajero) manager.findObjectByParameter(RenCajero.class, map);

        List<RegpLiquidacionDetalles> actos = new ArrayList();
        RegpLiquidacionDetalles detalle = null;
        RegpLiquidacionDetalles detalleTemp = null;

        map = new HashMap();
        map.put("pago", renFactura.getPago());

        List<RenPagoRubro> pagoRubros = (List<RenPagoRubro>) manager.findObjectByParameterList(RenPagoRubro.class, map);
        System.out.println("pagoRubros " + pagoRubros.size());

        for (RenPagoRubro rdr : pagoRubros) {

//            for (RegpLiquidacionDetalles rld : rdr.getPago().getLiquidacion().getRegpLiquidacionDetallesCollection()) {
//                if (rld.getActo().getId().equals(rdr.getRubro().getId())) {
//                    detalleTemp = rld;
//                    actos.add(detalleTemp);
//                }
//            }
            detalle = new RegpLiquidacionDetalles();
            detalle.setActo(rdr.getRubro());
            detalle.setValorTotal(rdr.getValor());
            detalle.setValorUnitario(rdr.getValor());
            detalle.setRecargo(BigDecimal.ZERO);
            detalle.setDescuento(BigDecimal.ZERO);
            detalle.setCantidad(1);
            actos.add(detalle);
        }

        renFactura.setLiquidacionDetalles(actos);
        /*if (fac.reenviarFacturaElectronicaSinTramite(renFactura, renFactura.getCaja())) {
            emisiones = new RenFacturaLazy();
            JsfUti.messageInfo(null, "Se realizo el reenvio de la Factura.", "");
        } else {
            JsfUti.messageWarning(null, "Problemas en el reenvio de la Factura.", "");
        }*/
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

    public void agregarSolicitanteInterviniente() {
        if (agregaSolicitante) {
            if (ente != null && ente.getId() != null) {
                indexSolicitante = agregarIntereniente(ente);
                if (indexSolicitante == null) {
                    agregaSolicitante = Boolean.FALSE;
                }
            } else {
                JsfUti.messageWarning(null, "Debe Agregar un Solicitante", "");
                agregaSolicitante = Boolean.FALSE;
            }
        } else {
            if (indexSolicitante != null) {
                listInterv.remove(indexSolicitante.intValue());
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

    public void showDlgTipoIngreso() {
        try {
            if (cajero != null) {

            } else {
                JsfUti.messageWarning(null, "Usuario no tiene Cajero asignado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaInterviniente(String cedula) {
        return listInterv.stream().noneMatch((r) -> (r.getEnte().getCiRuc().equalsIgnoreCase(cedula)));
    }

    public void generarReporteFacturacionElectronica(String ruta) {
        try {
            ss.instanciarParametros();
            ss.setContentType("application/pdf");
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void xmlComprobanteSRI(String ruta) {
        ss.instanciarParametros();
        ss.setContentType("application/xml");
        ss.setNombreDocumento(ruta);
        JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
    }

    public void downloadNotaCredito(RenNotaCredito nc) {
        String ruta;
        try {
            if (nc.getNumeroAutorizacionModifica() != null && nc.getNumeroDocumento() != null) {
                ruta = Constantes.rutaFeOld + "notacredito_" + nc.getNumeroDocumento().replace("-", "").trim() + ".pdf";
                System.out.println("ruta " + ruta);
                ss.instanciarParametros();
                ss.setNombreDocumento(ruta);
                JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            } else {
                JsfUti.messageWarning(null, "El comprobante electronico aun no esta autorizada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reenvioNotaCredito(RenNotaCredito nc) {
        try {
            if (nc.getEstado() != null) {
                if (!nc.getEstado().equalsIgnoreCase("RECIBIDA;AUTORIZADO")) {
                    reenviarNC(nc);
                } else {
                    JsfUti.messageWarning(null, "El comprobante electronico ya tiene estado AUTORIZADO.", "");
                }
            } else {
                reenviarNC(nc);
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void reenviarNC(RenNotaCredito nc) {
        /*if (fac.emitirNotaCredito(nc)) {
            lazy = new RenNotaCreditoLazy();
            JsfUti.update("mainForm");
            JsfUti.messageInfo(null, "Se realizo el reenvio de la Nota de Credito.", "");
        } else {
            JsfUti.messageWarning(null, "Problemas en el reenvio de la Nota de Credito.", "");
        }*/
    }

    public void quitarDescuento(int index) {
        RegpIntervinientes in = listInterv.get(index);
        in.setExoneracion(null);
    }

    public void visualizarJson(RegpLiquidacion re) {
        try {
            FacturaModelo temp = itl.retornaModelErp(re.getId());
            jsonRespuesta = origami.toJsonGeneric(temp);
            JsfUti.update("formVerJson");
            JsfUti.executeJS("PF('dlgVerJson').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void enviarFactura(RegpLiquidacion re) {
        try {
            if (re.getTituloCredito() != null) {
                JsfUti.messageWarning(null, "La factura ya ha sido generada.", "");
                return;
            }
            FacturaRespuestaERP respuesta = itl.registrarLiquidacionERP(re.getId());
            if (respuesta.getData() != null) {
                JsfUti.messageInfo(null, respuesta.getData().getMensaje(), "");
                this.consultarFacturasElectronicas();
            } else {
                JsfUti.messageError(null, respuesta.getMessage(), "");
            }
            /*jsonRespuesta = origami.toJsonGeneric(respuesta);
            JsfUti.update("formVerJson");
            JsfUti.executeJS("PF('dlgVerJson').show();");*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void autorizarFactura(RegpLiquidacion re) {
        try {
            if (re.getTituloCredito() == null) {
                JsfUti.messageError(null, "La factura debe ser generada.", "");
                return;
            }
            FacturaEmitirErp respuesta = itl.emitirFacturaErp(re.getId());
            if (respuesta.getSuccess().equalsIgnoreCase("true")) {
                JsfUti.messageInfo(null, respuesta.getData(), "");
                this.consultarFacturasElectronicas();
            } else {
                JsfUti.messageError(null, respuesta.getData(), "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarEstadoFactura(RegpLiquidacion re) {
        try {
            jsonRespuesta = null;
            /*if (re.getEstadoWs() == null) {
                JsfUti.messageError(null, "La factura debe ser generada antes.", "");
                return;
            }
            if (!re.getEstadoWs().contains("AUTORIZADO")) {
                JsfUti.messageError(null, "La factura debe estar en estado AUTORIZADO.", "");
                return;
            }*/
            if (re.getTituloCredito() == null) {
                JsfUti.messageError(null, "No se ha generado factura para poder consultar.", "");
                return;
            }
            FacturaConsultaErp respuesta = itl.consultarEstadoFactura(re.getTituloCredito());
            jsonRespuesta = origami.toJsonGeneric(respuesta);
            JsfUti.update("formVerJson");
            JsfUti.executeJS("PF('dlgVerJson').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void limpiarDatosSolicitante() {
        ente = new CatEnte();
    }

    public void updateCorreoTramiteLiq() {
        RegpLiquidacion liq = manager.find(RegpLiquidacion.class, liquidacion.getId());
        liq.setCorreoTramite(liquidacion.getCorreoTramite());
        manager.merge(liq);
        JsfUti.messageInfo(null, "Correo Tramite", "Actualizado correctamente.");
    }

    public void updateInformacionAdicionalLiq() {
        RegpLiquidacion liq = manager.find(RegpLiquidacion.class, liquidacion.getId());
        liq.setInfAdicionalProf(liquidacion.getInfAdicionalProf());
        manager.merge(liq);
        System.out.println("Informacion: " + liquidacion.getInfAdicionalProf());
        manager.update(liquidacion);
        JsfUti.messageInfo(null, "Observaciones", "Actualizado correctamente.");
    }

    public void showDlgEditActo(int index) {
        try {

            editar = true;
            certificado = false;
            indiceActo = index;

            rld = actosPorPagar.remove(index);

            acto = rld.getActo();
            avaluo = rld.getAvaluo();
            cuantia = rld.getCuantia();

            listInterv = (List<RegpIntervinientes>) rld.getRegpIntervinientesCollection();

            if (listInterv == null) {
                map = new HashMap();
                map.put("liquidacion", rld);
                listInterv = manager.findObjectByParameterList(RegpIntervinientes.class, map);
                if (listInterv == null) {
                    listInterv = new ArrayList<>();
                }
            }

            agregaBeneficiario = agregaSolicitante = Boolean.FALSE;
            for (RegpIntervinientes in : listInterv) {
                if (solicitante != null) {
                    if (solicitante.getId() != null) {
                        if (solicitante.getCiRuc().equalsIgnoreCase(in.getEnte().getCiRuc())) {
                            agregaSolicitante = Boolean.TRUE;
                        }
                    }
                }
            }

            // Refrescamos el formulario dentro del diálogo
            JsfUti.update("formEditCuantia");
            // Mostramos el diálogo
            JsfUti.executeJS("PF('dglEditCuantia').show();");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al abrir edición de acto", e);
            JsfUti.messageError(null, "No se pudo abrir el diálogo de edición.", "");
        }
    }

    public void buscar() {
        if (ente.getCiRuc() != null || solicitante.getCiRuc().isEmpty()) {
            if (!ente.getCiRuc().isEmpty()) {
                cedula = ente.getCiRuc();
                map = new HashMap<>();
                map.put("ciRuc", cedula);
                Long count = ((Long) manager.findObjectByParameter(Querys.CatEnteCount, map));
                if (count == 1) {
                    ente = (CatEnte) manager.findObjectByParameter(CatEnte.class, map);
                } else {
                    ente = new CatEnte();
                }
                /*if (solicitante == null || solicitante.getId() == null) {
                    solicitante = reg.buscarGuardarEnteDinardap(cedula);
                }*/
                //buscarActosIngresados(cedula);
            }
            if (ente == null) {
                ente = new CatEnte();
            }
            if (ente.getId() == null) {
                ss.instanciarParametros();
                if (cedula != null && !cedula.isEmpty()) {
                    ss.agregarParametro("ciRuc_", cedula);
                }
                showDlg("/resources/dialog/dlglazyente");
            } else {
                JsfUti.update("mainForm:pnlSolicitante");
            }
        }
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public List<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<RegpLiquidacion> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<RegpLiquidacion> facturas) {
        this.facturas = facturas;
    }

    public List<AclUser> getCajeros() {
        return cajeros;
    }

    public void setCajeros(List<AclUser> cajeros) {
        this.cajeros = cajeros;
    }

    public AclUser getUser() {
        return user;
    }

    public void setUser(AclUser user) {
        this.user = user;
    }

    public AclUser getCaja() {
        return caja;
    }

    public void setCaja(AclUser caja) {
        this.caja = caja;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Integer getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(Integer tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public RegpLiquidacion getProforma() {
        return proforma;
    }

    public void setProforma(RegpLiquidacion proforma) {
        this.proforma = proforma;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatEnte getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(CatEnte solicitante) {
        this.solicitante = solicitante;
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

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public Long getNumerotramite() {
        return numerotramite;
    }

    public void setNumerotramite(Long numerotramite) {
        this.numerotramite = numerotramite;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public RenFacturaLazy getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(RenFacturaLazy emisiones) {
        this.emisiones = emisiones;
    }

    public RenCajero getCajero() {
        return cajero;
    }

    public void setCajero(RenCajero cajero) {
        this.cajero = cajero;
    }

    public List<RegpIntervinientes> getListInterv() {
        return listInterv;
    }

    public void setListInterv(List<RegpIntervinientes> listInterv) {
        this.listInterv = listInterv;
    }

    public Integer getAniosDiferencia() {
        return aniosDiferencia;
    }

    public void setAniosDiferencia(Integer aniosDiferencia) {
        this.aniosDiferencia = aniosDiferencia;
    }

    public RegActo getActo() {
        return acto;
    }

    public void setActo(RegActo acto) {
        this.acto = acto;
    }

    public RegpLiquidacionDetalles getRld() {
        return rld;
    }

    public void setRld(RegpLiquidacionDetalles rld) {
        this.rld = rld;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public Integer getTipocalculo() {
        return tipocalculo;
    }

    public void setTipocalculo(Integer tipocalculo) {
        this.tipocalculo = tipocalculo;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObsAdicional() {
        return obsAdicional;
    }

    public void setObsAdicional(String obsAdicional) {
        this.obsAdicional = obsAdicional;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotalDesc() {
        return subTotalDesc;
    }

    public void setSubTotalDesc(BigDecimal subTotalDesc) {
        this.subTotalDesc = subTotalDesc;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public BigDecimal getDescPorLey() {
        return descPorLey;
    }

    public void setDescPorLey(BigDecimal descPorLey) {
        this.descPorLey = descPorLey;
    }

    public BigDecimal getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(BigDecimal recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }

    public BigDecimal getDescLimitCobro() {
        return descLimitCobro;
    }

    public void setDescLimitCobro(BigDecimal descLimitCobro) {
        this.descLimitCobro = descLimitCobro;
    }

    public BigDecimal getGastosGenerales() {
        return gastosGenerales;
    }

    public void setGastosGenerales(BigDecimal gastosGenerales) {
        this.gastosGenerales = gastosGenerales;
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

    public BigDecimal getPorcPago() {
        return porcPago;
    }

    public void setPorcPago(BigDecimal porcPago) {
        this.porcPago = porcPago;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
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

    public RegpIntervinientes getInterviniente() {
        return interviniente;
    }

    public void setInterviniente(RegpIntervinientes interviniente) {
        this.interviniente = interviniente;
    }

    public Boolean getBeneficiarioEsSolicitante() {
        return beneficiarioEsSolicitante;
    }

    public void setBeneficiarioEsSolicitante(Boolean beneficiarioEsSolicitante) {
        this.beneficiarioEsSolicitante = beneficiarioEsSolicitante;
    }

    public Integer getIndexBeneficiario() {
        return indexBeneficiario;
    }

    public void setIndexBeneficiario(Integer indexBeneficiario) {
        this.indexBeneficiario = indexBeneficiario;
    }

    public Integer getIndexSolicitante() {
        return indexSolicitante;
    }

    public void setIndexSolicitante(Integer indexSolicitante) {
        this.indexSolicitante = indexSolicitante;
    }

    public List<RegpLiquidacionDetalles> getActosPorPagar() {
        return actosPorPagar;
    }

    public void setActosPorPagar(List<RegpLiquidacionDetalles> actosPorPagar) {
        this.actosPorPagar = actosPorPagar;
    }

    public int getIndiceActo() {
        return indiceActo;
    }

    public void setIndiceActo(int indiceActo) {
        this.indiceActo = indiceActo;
    }

    public BigDecimal getAdicional() {
        return adicional;
    }

    public void setAdicional(BigDecimal adicional) {
        this.adicional = adicional;
    }

    public List<CtlgItem> getUsosDocumentos() {
        return manager.findAllEntCopy(Querys.getCtlgItemListUsosDocs);
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public PagoModel getModelPago() {
        return modelPago;
    }

    public void setModelPago(PagoModel modelPago) {
        this.modelPago = modelPago;
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

    public boolean isDiferenciaPagos() {
        return diferenciaPagos;
    }

    public void setDiferenciaPagos(boolean diferenciaPagos) {
        this.diferenciaPagos = diferenciaPagos;
    }

    public BigDecimal getValorDiferenciaActo() {
        return valorDiferenciaActo;
    }

    public void setValorDiferenciaActo(BigDecimal valorDiferenciaActo) {
        this.valorDiferenciaActo = valorDiferenciaActo;
    }

    public List<ComprobanteSRI> getComprobantesElectronicos() {
        return comprobantesElectronicos;
    }

    public void setComprobantesElectronicos(List<ComprobanteSRI> comprobantesElectronicos) {
        this.comprobantesElectronicos = comprobantesElectronicos;
    }

    public ComprobanteSRI getComprobanteSRI() {
        return comprobanteSRI;
    }

    public void setComprobanteSRI(ComprobanteSRI comprobanteSRI) {
        this.comprobanteSRI = comprobanteSRI;
    }

    public RenNotaCreditoLazy getLazy() {
        return lazy;
    }

    public void setLazy(RenNotaCreditoLazy lazy) {
        this.lazy = lazy;
    }

    public Boolean getFacturaSinTramite() {
        return facturaSinTramite;
    }

    public void setFacturaSinTramite(Boolean facturaSinTramite) {
        this.facturaSinTramite = facturaSinTramite;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public LazyModel<RegActo> getActos() {
        return actos;
    }

    public void setActos(LazyModel<RegActo> actos) {
        this.actos = actos;
    }

    public String getJsonRespuesta() {
        return jsonRespuesta;
    }

    public void setJsonRespuesta(String jsonRespuesta) {
        this.jsonRespuesta = jsonRespuesta;
    }

    public String getNombresFact() {
        return nombresFact;
    }

    public void setNombresFact(String nombresFact) {
        this.nombresFact = nombresFact;
    }

    public LazyModel<RegActo> getActosConSolvenciaFalse() {
        return actosConSolvenciaFalse;
    }

    public void setActosConSolvenciaFalse(LazyModel<RegActo> actosConSolvenciaFalse) {
        this.actosConSolvenciaFalse = actosConSolvenciaFalse;
    }

    public List<RenEntidadBancaria> getBancosDep() {
        return bancosDep;
    }

    public void setBancosDep(List<RenEntidadBancaria> bancosDep) {
        this.bancosDep = bancosDep;
    }
    
    

}
