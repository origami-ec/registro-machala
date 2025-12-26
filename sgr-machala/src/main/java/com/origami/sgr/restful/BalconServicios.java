/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.ebilling.models.FacturasVentanilla;
import com.origami.sgr.entities.*;
import com.origami.sgr.models.ActosEnLinea;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.models.AppTramite;
import com.origami.sgr.models.NamedItem;
import com.origami.sgr.models.Pago;
import com.origami.sgr.models.PagoReverso;
import com.origami.sgr.restful.models.Certificado;
import com.origami.sgr.restful.models.CertificadoTramite;
import com.origami.sgr.restful.models.CodeValidation;
import com.origami.sgr.restful.models.Contrato;
import com.origami.sgr.restful.models.ContratoRoot;
import com.origami.sgr.restful.models.DatosProforma;
import com.origami.sgr.services.ejbs.IngresoTramiteEjb;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import com.origami.sgr.ebilling.services.OrigamiGTService;
import com.origami.sgr.restful.models.CatalogoErrorExterno;
import com.origami.sgr.restful.models.DatosSolicitud;
import com.origami.sgr.restful.models.RespuestaWs;
import com.origami.sgr.restful.services.RestServices;

/**
 * @author Anyelo
 */
@Path(value = "ventanilla")
@Produces(value = {MediaType.APPLICATION_JSON})
public class BalconServicios extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(BalconServicios.class.getName());
    private static final long serialVersionUID = 1L;

    @Inject
    protected VentanillaPubLocal vp;
    @Inject
    private Entitymanager em;
    @Inject
    private RegCertificadoService certs;
    @Inject
    protected FirmaDigitalLocal fd;
    @Inject
    protected RegistroPropiedadServices rps;
    @Inject
    private OrigamiGTService fac;
    @Inject
    private AsynchronousService as;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private RestServices ser;
    protected HashMap<String, Object> par;

    @POST
    @Path(value = "/updatePubSolicitud")
    @Consumes(MediaType.APPLICATION_JSON)
    public void actualizarSolicitud(String solicitudJson) {
        try {
            if (solicitudJson != null) {
                Gson gson = new Gson();
                PubSolicitud solicitud = (PubSolicitud) gson.fromJson(solicitudJson, PubSolicitud.class);
                if (solicitud != null) {
                    if (solicitud.getNumeroTramite() != null) {
                        Map mapa = new HashMap();
                        mapa.put("numTramite", solicitud.getNumeroTramite());
                        HistoricoTramites historicoTramites = (HistoricoTramites) em.findObjectByParameter(HistoricoTramites.class, mapa);
                        if (historicoTramites != null) {
                            mapa = new HashMap();
                            mapa.put("tramite", historicoTramites);
                            PubSolicitud ps
                                    = (PubSolicitud) em.findObjectByParameter(PubSolicitud.class, mapa);
                            if (ps != null) {
                                ps.setOidDocument(solicitud.getOidDocument());
                                ps.setOidDocument2(solicitud.getOidDocument2());
                                ps.setOidDocument3(solicitud.getOidDocument3());
                                em.merge(ps);
                            }
                        }
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            System.out.println(e);
        }
    }

    @POST
    @Path(value = "/iniciarTramiteOnline")
    @Consumes(MediaType.APPLICATION_JSON)
    public RespuestaWs iniciarTramiteOnline(String solicitudJson) throws InterruptedException {
        RegpLiquidacion liquidacion = null;
        DatosSolicitud solicitud;
        Map mapa = new HashMap();
        try {
            Gson gson = new Gson();
            solicitud = gson.fromJson(solicitudJson, DatosSolicitud.class);
            mapa.put("numTramiteRp", solicitud.getNumTramite());
            liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, mapa);
            /*if (liquidacion != null) {
                DatosProforma proforma = new DatosProforma(liquidacion);
                return new RespuestaWs(CatalogoErrorExterno.E022.codigo(), CatalogoErrorExterno.E022.descripcion(),
                        CatalogoErrorExterno.E022.sugerencia(), proforma);
            } else {
                liquidacion = vp.iniciarTramiteOnline(solicitud);
                if (liquidacion != null && liquidacion.getId() != null) {
                    DatosProforma proforma = new DatosProforma(liquidacion);
                    return new RespuestaWs(CatalogoErrorExterno.E000.codigo(), CatalogoErrorExterno.E000.descripcion(), proforma);
                }
            }*/
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return new RespuestaWs(CatalogoErrorExterno.E002.codigo(), CatalogoErrorExterno.E002.descripcion());
        } finally {
            if (liquidacion != null && liquidacion.getId() != null) {
                vp.iniciarTramiteActiviti(liquidacion, "INGRESO DE TRAMITE");
            }
        }
        return new RespuestaWs(CatalogoErrorExterno.E002.codigo(), CatalogoErrorExterno.E002.descripcion());
    }

    @POST
    @Path(value = "/solicitudCertificadoAutomatico")
    @Consumes(MediaType.APPLICATION_JSON)
    public RespuestaWs solicitudCertificadoAutomatico(String solicitudJson) {
        DatosSolicitud solicitud = null;
        RegCertificado certificado = null;
        RegpLiquidacion liquidacion;
        Map mapa = new HashMap();
        try {
            Gson gson = new Gson();
            solicitud = gson.fromJson(solicitudJson, DatosSolicitud.class);
            mapa.put("numTramiteRp", solicitud.getNumTramite());
            liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, mapa);
            /*if (liquidacion != null) {
                DatosProforma proforma = new DatosProforma(liquidacion);
                return new RespuestaWs(CatalogoErrorExterno.E022.codigo(), CatalogoErrorExterno.E022.descripcion(),
                        CatalogoErrorExterno.E022.sugerencia(), proforma);
            } else {
                solicitud = vp.generarTramiteOnline(solicitud);
                certificado = ser.generarCertificadoOnline(solicitud);
                if (certificado.getId() != null) {
                    DatosCertificado datosCertificado = new DatosCertificado(certificado);
                    return new RespuestaWs(CatalogoErrorExterno.E000.codigo(), CatalogoErrorExterno.E000.descripcion(), datosCertificado);
                }
            }*/
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return new RespuestaWs(CatalogoErrorExterno.E002.codigo(), CatalogoErrorExterno.E002.descripcion());
        } finally {
            if (certificado != null && solicitud != null) {
                as.generarFirmaEnviarCorreoCertificadoOnline(certificado, solicitud.getNumTramite(),
                        solicitud.getSolicitanteCorreo(), certificado.getUserCreador().getUsuario());
            }
        }
        return new RespuestaWs(CatalogoErrorExterno.E002.codigo(), CatalogoErrorExterno.E002.descripcion());
    }

    @POST
    @Path(value = "/iniciarTramiteInscripcion")
    @Consumes(MediaType.APPLICATION_JSON)
    public DatosProforma iniciarTramiteInscripcion(String solicitudJson) {

        RegpLiquidacion liquidacion = new RegpLiquidacion();
        try {
            Gson gson = new Gson();
            PubSolicitud solicitud = gson.fromJson(solicitudJson, PubSolicitud.class);

            solicitud.setId(null);
            liquidacion = vp.iniciarTramiteInscripcionOnline(solicitud);
            //return new DatosProforma(liquidacion);
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        } finally {
            if (liquidacion.getNumTramiteRp() != null) {
                vp.iniciarTramiteActiviti(liquidacion);
                //fac.emitirFacturaElectronica(liquidacion, vp.getCajaVentanilla());
            }
        }
        //return new DatosProforma(liquidacion);
        return null;
    }

    @POST
    @Path(value = "/iniciarTramiteSolicitudInscripcion")
    @Consumes(MediaType.APPLICATION_JSON)
    public DatosProforma iniciarTramiteSolicitudInscripcion(String solicitudJson) {
        Gson gson = new Gson();
        PubSolicitud solicitud = null;
        PubSolicitud json = gson.fromJson(solicitudJson, PubSolicitud.class);
        List<ActosRequisito> requisitos = json.getRequisitos();
        DatosProforma datosProforma = new DatosProforma();
        try {

            solicitud = vp.generarSolicitudInscripcion(json);
            HistoricoTramites ht = solicitud.getTramite();
            datosProforma.setNumerotramite(ht.getNumTramite());
            datosProforma.setFechaingreso(ht.getFecha().getTime());
            datosProforma.setFechaentrega(ht.getFecha().getTime());

        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        } finally {
            as.enviarCorreoSolicitudIncripcion(solicitud, requisitos);
        }
        return datosProforma;
    }

    @POST
    @Path(value = "/actualizarRequisitos")
    @Consumes(MediaType.APPLICATION_JSON)
    public DatosProforma actualizarRequisitosInscripcion(String solicitudJson) {
        DatosProforma datosProforma = new DatosProforma();
        try {
            if (solicitudJson != null) {
                Gson gson = new Gson();
                PubSolicitud solicitud = (PubSolicitud) gson.fromJson(solicitudJson, PubSolicitud.class);
                HistoricoTramites historicoTramites = vp.actualizarRequisitosInscripcion(solicitud);
                if (historicoTramites != null) {
                    datosProforma.setNumerotramite(historicoTramites.getNumTramite());
                    datosProforma.setFechaingreso(historicoTramites.getFecha().getTime());
                    datosProforma.setFechaentrega(historicoTramites.getFecha().getTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datosProforma;
    }

    @GET
    @Path(value = "/consultar/tramite/{numerotramite}")
    @Consumes(MediaType.APPLICATION_JSON)
    public DatosProforma consultarTramite(@PathParam(value = "numerotramite") Long tramite) {
        RegpLiquidacion liquidacion;
        DatosProforma proforma = new DatosProforma();
        Map map;
        try {
            map = new HashMap();
            map.put("numTramiteRp", tramite);
            liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
            if (liquidacion == null) {
                //proforma.setMensaje(Messages.tramiteNoEncontrado);
            } else if (liquidacion.getEstadoLiquidacion().getId() == 2L || liquidacion.getEstadoLiquidacion().getId() == 5L) {
                //proforma.constructor(liquidacion);
                proforma.setFechaentrega(itl.diasEntregaTramite(liquidacion.getFechaIngreso(), 6).getTime());
                //proforma.setProcedePago(Boolean.FALSE);
                //proforma.setMensaje("El trámite ya ha sido cancelado");
                if (liquidacion.getUsoDocumento() != null) {
                    proforma.setDetalleSolicitud(liquidacion.getUsoDocumento().getValor());
                }
                if (!liquidacion.getCertificadoSinFlujo()) {
                    String nameTask = rps.getNameTaskFromNumTramite(tramite);
                    if (!nameTask.isEmpty()) {
                        proforma.setTareaActual(nameTask.toUpperCase());
                    } else {
                        proforma.setTareaActual("TRÁMITE ENTREGADO");
                    }
                } else {
                    map = new HashMap();
                    map.put("numTramite", liquidacion.getNumTramiteRp());
                    HistoricoTramites historicoTramites = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    List<Observaciones> htObservaciones = new ArrayList(historicoTramites.getObservacionesCollection());
                    if (Utils.isEmpty(htObservaciones)) {
                        proforma.setTareaActual("TRÁMITE ENTREGADO");
                    } else {
                        htObservaciones.sort((Observaciones o1, Observaciones o2) -> o2.getId().compareTo(o1.getId()));
                        if (htObservaciones.get(0).getObservacion().contains("retira los documentos")) {
                            proforma.setTareaActual("TRÁMITE ENTREGADO");
                        } else {
                            switch (htObservaciones.get(0).getObservacion()) {
                                case "EMISIÓN DE CERTIFICADO":
                                    proforma.setTareaActual("EMISIÓN DE CERTIFICADO");
                                    break;
                                case "PENDIENTE DE APROBACIÓN":
                                    proforma.setTareaActual("FIRMA CERTIFICADO");
                                    break;
                                case "APROBADO":
                                    proforma.setTareaActual("ENTREGA CERTIFICADO");
                                    break;
                                default:
                                    proforma.setTareaActual("CERTIFICADO XPRESS");
                                    break;
                            }
                        }
                    }
                }
                String numFichas = "";
//                List<RegFicha> fichas = getFichasHT(tramite);
//                if (Utils.isNotEmpty(fichas)) {
//                    for (RegFicha rf : fichas) {
//                        numFichas = rf.getNumFicha().toString() + "; " + numFichas;
//                    }
//                }
//                if (!numFichas.isEmpty()) {
//                    proforma.setFichas(numFichas.substring(0, numFichas.length() - 2));
//                }
                map = new HashMap<>();
                map.put("idLiquidacion", liquidacion.getId());
                Integer dias = (Integer) manager.findObjectByParameter(Querys.getMaxDiasByTramite, map);
                if (dias > 0) {
                    //proforma.setRevisor(dias + " DIAS LABORABLES");
                }

            } else if (liquidacion.getEstadoLiquidacion().getId() == 1L) { //PARA INSCRIPCIONES EN LINEA VALIDA SI SE PUEDE PROCEDER AL PAGO
                //proforma.constructor(liquidacion);
                Valores valor;
                map = new HashMap();
                map.put("code", Constantes.diasValidezProforma);
                valor = (Valores) manager.findObjectByParameter(Valores.class, map);
                Date fecha = Utils.sumarDiasFechaSinWeekEnd(liquidacion.getFechaCreacion(), valor.getValorNumeric().intValue());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date hoy = sdf.parse(sdf.format(new Date()));
                fecha = sdf.parse(sdf.format(fecha));
                Boolean procedePago = !fecha.before(hoy);
                //proforma.setProcedePago(procedePago);
                if (!procedePago) {
                    //proforma.setMensaje("La proforma paso fecha limite de validez");
                } else {
                    //proforma.setMensaje("Proforma");
                }
            } else {
                //proforma.setMensaje(Messages.tramiteNoIngresado);
            }
            System.out.println("proforma: " + proforma.toString());
            return proforma;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            e.printStackTrace();
            return new DatosProforma();
        }
    }

    @GET
    @Path(value = "/contratos/{nombreacto}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ContratoRoot consultaContratos(@PathParam(value = "nombreacto") String nombre) {
        ContratoRoot contrato = new ContratoRoot();
        nombre = Utils.quitarTildes(nombre);
        try {
            contrato.setContratos(em.getSqlQueryValues(Contrato.class, Querys.getContratosByNombre, new Object[]{nombre.trim().toLowerCase().replaceAll(" ", "%") + "%"}));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
        return contrato;
    }

    @GET
    @Path("/certificado/descarga/codigo/{code}")
    @Produces("application/pdf")
    public Response downloadCertificado(@PathParam("code") String validationCode) throws SQLException {
        RegCertificado ce = certs.findByValidationCode(validationCode);
        if (ce != null) {
            //if (ce.getPrintOnline() == 0) {
            if (ce.getSecuencia() == 1) {
                String pathCert = certs.genCertificadoPdf(ce.getId());
                File pdfFile = new File(pathCert);
                ResponseBuilder response = Response.ok(pdfFile);
                response.header("Content-Disposition", "attachment; filename=certificado.pdf");
                return response.build();
            }
        }
        ResponseBuilder response = Response.noContent();
        return response.build();
    }

    @GET
    @Path("/certificado/validate/codigo/{code}")
    @Produces("application/pdf")
    public Response validateCertificado(@PathParam("code") String validationCode) throws SQLException {
        RegCertificado ce = certs.findByValidationCode(validationCode);
        if (ce != null) {
            if (ce.getSecuencia() == 1) {
                //String pathCert = certs.genCertificadoValidatePdf(ce.getId());
                String pathCert = certs.genCertificadoPdf(ce.getId());
                File pdfFile = new File(pathCert);
                ResponseBuilder response = Response.ok(pdfFile);
                response.header("Content-Disposition", "attachment; filename=certificado.pdf");
                return response.build();
            }
        }
        ResponseBuilder response = Response.noContent();
        return response.build();
    }

    //METODO PARA ACTUALIZAR EL CONTEO DE LA IMPRESION
    @GET
    @Path("/certificado/firma/codigo/{code}")
    public String downloadCertificadoFirmado(@PathParam("code") String validationCode) throws IOException, SQLException {
        System.out.println("downloadCertificadoFirmado " + validationCode);
        RegCertificado ce = certs.findByValidationCode(validationCode);
        if (ce != null) {
            finalizarTramite(ce);
            certs.countPrint(ce);
        }
        return "";
    }

    //ENTREGA DE TRAMITE
    private void finalizarTramite(RegCertificado certificado) {
        Boolean tramiteFinalizado = Boolean.FALSE;
        List<RegCertificado> certificados = em.findAll(Querys.getCertificados,
                new String[]{"tramite"}, new Object[]{certificado.getNumTramite()});
        if (Utils.isNotEmpty(certificados)) {
            for (RegCertificado ce : certificados) {
                if (ce.getPrintOnline() == 0) {
                    tramiteFinalizado = Boolean.FALSE;
                    break;
                } else {
                    tramiteFinalizado = Boolean.TRUE;
                }
            }
            if (!tramiteFinalizado) {
                Map map = new HashMap();
                map.put("numTramite", certificado.getNumTramite());
                HistoricoTramites ht = (HistoricoTramites) em.findObjectByParameter(HistoricoTramites.class, map);
                String taskId = rps.getTaskIdFromNumTramite(certificado.getNumTramite());
                map = new HashMap();
                map.put("numTramiteRp", certificado.getNumTramite());
                RegpLiquidacion liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
                String obs = "Se envio el documento al correo: "
                        + liquidacion.getSolicitante().getCorreo1() + " - " + liquidacion.getSolicitante().getNombreCompleto();

                if (!taskId.isEmpty()) {
                    rps.guardarObservaciones(ht, Constantes.tramiteDescargado, obs, "Entrega Certificado");
                    //FINALIZA LA TAREA DE ENTREGA DE TRAMITES
                    try {
                        HashMap<String, Object> par = new HashMap<>();
                        this.reasignarTarea(taskId, Constantes.tramiteDescargado);
                        this.completeTask(taskId, par);
                    } catch (Exception ex) {
                        Logger.getLogger(BalconServicios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (liquidacion.getCertificadoSinFlujo()) {
                        rps.guardarObservaciones(ht, Constantes.tramiteDescargado, obs, Constantes.certificadoExpress);
                    }
                }
            }
        }
    }

    @GET
    @Path("/certificado/validacion/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public CodeValidation validarCodigo(@PathParam("code") String validationCode) {
        RegCertificado cer1 = certs.findByValidationCode(validationCode);
        if (cer1 != null) {
            return new CodeValidation(true);
        }
        return new CodeValidation(false);
    }

    @GET
    @Path("/certificado/consulta/tramite/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public CertificadoTramite consultaCertificados(@PathParam("code") String code) {
        List<RegCertificado> certificados;
        List<Certificado> cers = new ArrayList<>();
        CertificadoTramite model;
        Certificado cer;
        Map map;
        try {
            Long tramite = new Long(code);
            map = new HashMap();
            map.put("numTramiteRp", tramite);
            RegpLiquidacion liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
            if (liquidacion != null) {
                model = new CertificadoTramite();
                model.setNumerotramite(liquidacion.getNumTramiteRp());
                model.setFechaingreso(liquidacion.getFechaIngreso().getTime());
                map = new HashMap();
                map.put("numTramite", tramite);

                //map.put("secuencia", 1);
                certificados = em.findObjectByParameterList(RegCertificado.class, map);
                for (RegCertificado ce : certificados) {
                    if (ce.getCodVerificacion() != null) {
                        cer = new Certificado();
                        cer.setCodeverificate(ce.getCodVerificacion());
                        cer.setFechaemision(ce.getFechaEmision().getTime());
                        cer.setNumcertificado(ce.getNumCertificado().longValue());
                        cer.setPrintonline(ce.getPrintOnline());
                        cer.setTipocertificado(ce.getTipoCertificado().intValue());
                        cer.setSecuencia(ce.getSecuencia());
                        cers.add(cer);
                    }
                }
                model.setCertificados(cers);
                return model;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @GET
    @Path("/consultar/tramite/cedula/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppTramite> tramitesPorCedula(@PathParam("cedula") String cedula) {
        try {
            return em.getSqlQueryValues(AppTramite.class, Querys.consultaEstadoTramite, new Object[]{cedula});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @GET
    @Path("/usoDocumentos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NamedItem> usosDocumentos() {
        List<NamedItem> namedItems = new ArrayList<>();
        NamedItem namedItem;
        List<CtlgItem> items = (List<CtlgItem>) em.findAllEntCopy(Querys.getCtlgItemListUsosDocs);
        for (CtlgItem item : items) {
            namedItem = new NamedItem();
            namedItem.setId(new BigInteger(item.getId().toString()));
            namedItem.setNombre(item.getValor());
            namedItems.add(namedItem);
        }
        return namedItems;
    }

    @GET
    @Path("/certificado/codigo/{code}")
    public String documentoOidCertificado(@PathParam("code") String validationCode) throws IOException, SQLException {
        try {
            return certs.findByValidationCode2(validationCode);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
    }

    //METODO PARA ACTUALIZAR EL ESTADO DEL TRAMITE
    @GET
    @Path("/entregaTramiteAPP/tramite/{tramite}/identificacionRetira/{identificacionRetira}/usuarioSGR/{usuarioSGR}/registrador/{registrador}")
    public String finalizarTramiteParaSiempreAPP(@PathParam("tramite") Long tramite,
            @PathParam("identificacionRetira") String identificacionRetira,
            @PathParam("usuarioSGR") String usuarioSGR,
            @PathParam("registrador") String registrador) {

        String tarea, obs;
        String taskId = rps.getTaskIdFromNumTramite(tramite);
        String nameTask = rps.getNameTaskFromNumTramite(tramite);

        map = new HashMap();
        map.put("numTramiteRp", tramite);
        RegpLiquidacion liquidacion = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
        if (liquidacion.getCertificado() && !liquidacion.getInscripcion()) {
            tarea = "Entrega Certificado";
        } else {
            tarea = "Entrega Tramite";
        }

        switch (registrador) {
            case "SI":
                if (!nameTask.isEmpty() && nameTask.contains("Firma")) {
                    par = new HashMap<>();
                    par.put("secretaria", itl.getCandidateUserByRolName("secretario_registral")); //FIRMA INSCRIPCION
                    par.put("revision", 1);
                    //as.enviarCorreoTramiteFinalizado(liquidacion);
                    as.generarFirmaDigital(liquidacion.getTramite().getNumTramite());
                    //FINALIZA LA TAREA DE ENTREGA DE TRAMITES
                    try {
                        this.reasignarTarea(taskId, usuarioSGR);
                        this.completeTask(taskId, par);
                    } catch (Exception ex) {
                        Logger.getLogger(BalconServicios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (liquidacion.getCertificadoSinFlujo()) {
                        List<Observaciones> htObservaciones = new ArrayList(liquidacion.getTramite().getObservacionesCollection());
                        if (!Utils.isEmpty(htObservaciones)) {
                            htObservaciones.sort((Observaciones o1, Observaciones o2) -> o2.getId().compareTo(o1.getId()));
                            if (!htObservaciones.get(0).getObservacion().contains("retira los documentos")) {
                                switch (htObservaciones.get(0).getObservacion()) {
                                    case "EMISIÓN DE CERTIFICADO":
                                    case "APROBADO":
                                        break;
                                    case "PENDIENTE DE APROBACIÓN":
                                        rps.guardarObservaciones(liquidacion.getTramite(), usuarioSGR, "APROBADO", Constantes.certificadoExpress);
                                        for (RegpLiquidacionDetalles detalles : liquidacion.getRegpLiquidacionDetallesCollection()) {
                                            for (RegpTareasTramite rtt : detalles.getRegpTareasTramiteCollection()) {
                                                rtt.setRevisado(Boolean.TRUE);
                                                rtt.setFechaRevision(new Date());
                                                manager.merge(rtt);
                                            }
                                        }
                                        //as.enviarCorreoTramiteFinalizado(liquidacion);
                                        break;
                                }
                            }
                        }
                    }
                }
                break;
            case "NO":
                map = new HashMap();
                map.put("ciRuc", identificacionRetira);
                CatEnte ente = (CatEnte) em.findObjectByParameter(CatEnte.class, map);
                obs = "Persona que retira los documentos, CI/RUC: " + ente.getCiRuc() + ", Nombre: " + ente.getNombreCompleto();
                if (!taskId.isEmpty()) {
                    rps.guardarObservaciones(liquidacion.getTramite(), usuarioSGR, obs, tarea);
                    //FINALIZA LA TAREA DE ENTREGA DE TRAMITES
                    try {
                        HashMap<String, Object> par = new HashMap<>();
                        this.reasignarTarea(taskId, usuarioSGR);
                        this.completeTask(taskId, par);
                    } catch (Exception ex) {
                        Logger.getLogger(BalconServicios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (liquidacion.getCertificadoSinFlujo()) {
                        rps.guardarObservaciones(liquidacion.getTramite(), usuarioSGR, obs, Constantes.certificadoExpress);
                    }
                }
                break;
            default:
                break;
        }
        return "RECIBIDO";
    }

    @POST
    @Path(value = "/iniciarTramiteBanca")
    @Consumes(MediaType.APPLICATION_JSON)
    public Pago iniciarTramiteBanca(String pagoJson) {
        Pago pago = null;
        RegpLiquidacion liquidacion = null;
        try {
            Gson gson = new Gson();
            pago = (Pago) gson.fromJson(pagoJson, Pago.class);
            if (pago.getIdDeuda() != null) {
                liquidacion = vp.iniciarTramiteBanca(pago);
                if (liquidacion != null) {
                    System.out.println("pago " + pago);
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                    try {
                        ConvenioBanco transaccion = new ConvenioBanco();
                        transaccion.setDataJs(pagoJson);
                        transaccion.setEstado(Boolean.TRUE);
                        transaccion.setFechaProcesoIr(format.parse(pago.getFechaProcesoIr()));
                        format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                        transaccion.setFechaTransaccion(format.parse(pago.getFechaTransaccion()));
                        transaccion.setIdPagoInsRec(pago.getIdPagoIr());
                        transaccion.setIdUsuario(pago.getIdUsuario());
                        transaccion.setIdServicio(pago.getIdServicio());
                        transaccion.setSecuencial(pago.getSecuencial());
                        transaccion.setValorDeuda(pago.getValorDeuda());

                        Map<String, Object> pm = new HashMap<>();
                        pm.put("codigo", pago.getIdInstitucion());
                        RenEntidadBancaria institucion = em.findObjectByParameter(RenEntidadBancaria.class, pm);
                        transaccion.setInstitucion(institucion);
                        transaccion.setLiquidacion(liquidacion);
                        pm = new HashMap<>();
                        CtlgItem canal = em.findObjectByParameter(CtlgItem.class, pm);
                        pm.put("codename", pago.getIdCanal());
                        transaccion.setCanal(canal);
                        em.persist(transaccion);
                    } catch (ParseException pe) {
                        LOGGER.log(Level.SEVERE, "Error al guardar datos de transaccion", pe);
                    }
                    pago.setEstado("P001");
                    pago.setObservacionPago("Pago realizado con exito");
                } else {
                    if (pago.getEstado().equals("P004")) {
                        pago.setObservacionPago("No se permite pago parcial");
                    } else {
                        pago.setEstado("P003");
                        pago.setObservacionPago("No existe datos para procesar pago");
                    }
                }
            } else {
                pago.setEstado("P003");
                pago.setObservacionPago("No existe datos para procesar pago");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            pago.setEstado("S001");
            pago.setObservacionPago("Error de aplicacion ");
        } finally {
            System.out.println(" liquidacion != null " + (liquidacion != null));
            if (liquidacion != null) {
                try {
                    //fac.emitirFacturaElectronica(liquidacion, vp.getCajaVentanilla());
                } catch (Exception e) {
                    System.out.println("No se pudo enviar factura");
                }
                vp.iniciarProcesoActiviti(liquidacion);
            }
        }
        return pago;
    }

    @POST
    @Path(value = "/reversoTramiteBanca")
    @Consumes(MediaType.APPLICATION_JSON)
    public PagoReverso reversoTramiteBanca(String reversoJson) {
        PagoReverso reverso = null;
        RegpLiquidacion liquidacion = null;
        try {
            Gson gson = new Gson();
            reverso = (PagoReverso) gson.fromJson(reversoJson, PagoReverso.class);
            reverso.setEstado(null);
            if (reverso.getIdDeuda() != null) {
                liquidacion = vp.reversarTramiteBanca(reverso);
                if (liquidacion != null) {
                    try {
                        if (reverso.getEstado() == null) {
                            reverso.setEstado("R001");
                            reverso.setObservacionPago("Pago realizado con exito");
                        }
                    } catch (Exception pe) {
                        reverso.setEstado("S001");
                        LOGGER.log(Level.SEVERE, "Error al guardar datos de transaccion", pe);
                    }
                } else {
                    System.out.println("Estado reverso " + reverso.getEstado());
                    if (reverso.getEstado() == null) {
                        reverso.setEstado("R002");
                    }
                }
            } else {
                reverso.setEstado("P003");
                reverso.setObservacionPago("No existe datos para procesar pago");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            reverso.setEstado("S001");
            reverso.setObservacionPago("Error de aplicacion ");
        }
        return reverso;
    }

    @GET
    @Path("/inscripciones")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActosEnLinea> consultarActosenLinea() {
        return (List<ActosEnLinea>) manager.getNativeQueryParameter(ActosEnLinea.class, Querys.getRegActoInscripcionesList,
                null);
    }

    @GET
    @Path("/inscripciones/acto/{acto}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ActosRequisito> consultarActosenLineaRequisitos(@PathParam("acto") Long acto) {
        try {
            return (List<ActosRequisito>) manager.getNativeQueryParameter(ActosRequisito.class, Querys.getRegActoRequisitos,
                    new Object[]{acto});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("/documento/codigo/{code}/tipo/{tipo}")
    public Long documentoOid(@PathParam("code") String validationCode, @PathParam("tipo") Integer tipo)
            throws IOException, SQLException {
        try {
            return certs.findByValidationCode(validationCode, tipo);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return 0L;
        }
    }

    @GET
    @Path("/documento/codigo/{code}/informacion/{tipo}")
    public String documentoInformacion(@PathParam("code") String validationCode, @PathParam("tipo") Integer tipo)
            throws IOException, SQLException {
        try {
            return certs.findByValidationCodeDetalle(validationCode, tipo);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            return "Datos no encontrados";
        }
    }

    @GET
    @Path("/facturacionElectronica/{identificacion}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FacturasVentanilla> consultarFacturasenLinea(@PathParam("identificacion") String identificacion) {
        System.out.println("identificacion: " + identificacion);
        List<FacturasVentanilla> list = null;
        List<RegpLiquidacion> liquidacions;
        try {
            liquidacions = (List<RegpLiquidacion>) manager.findAll(Querys.getComprobantesBySolicitanteVentanilla, new String[]{"codigosolicitante"}, new Object[]{identificacion});
            if (Utils.isNotEmpty(liquidacions)) {
                list = new ArrayList<>();
                for (RegpLiquidacion liq : liquidacions) {
                    FacturasVentanilla fv = new FacturasVentanilla();
                    fv.setNumTramite(liq.getNumTramiteRp());
                    fv.setClaveAcceso(liq.getClaveAcceso());
                    fv.setNumAutorizacion(liq.getNumeroAutorizacion());
                    fv.setNumFacturaFormato(liq.getCodigoComprobante());
                    fv.setValorTotal(liq.getTotalPagar().toString());
                    fv.setFechaAutorizacion(Utils.dateFormatPattern("dd-MM-yyyy HH:mm", liq.getFechaAutorizacion()));
                    list.add(fv);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(IngresoTramiteEjb.class
                    .getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;

    }

    @GET
    @Path("/facturacionElectronica/reenviar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FacturasVentanilla reenviarFacturacionElectronica(@PathParam("id") String id) {
        System.out.println("identificacion: " + id);
        map = new HashMap();
        map.put("numTramiteRp", Long.valueOf(id));
        RegpLiquidacion liq = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
        //fac.reenviarFacturaElectronica(liq, vp.getCajaVentanilla(), Boolean.FALSE);
        FacturasVentanilla fv = new FacturasVentanilla();
        fv.setNumTramite(liq.getNumTramiteRp());
        fv.setClaveAcceso(liq.getClaveAcceso());
        fv.setNumAutorizacion(liq.getNumeroAutorizacion());
        fv.setNumFacturaFormato(liq.getCodigoComprobante());
        fv.setValorTotal(liq.getTotalPagar().toString());
        fv.setFechaAutorizacion(Utils.dateFormatPattern("dd-MM-yyyy HH:mm", liq.getFechaAutorizacion()));
        return fv;
    }

}
