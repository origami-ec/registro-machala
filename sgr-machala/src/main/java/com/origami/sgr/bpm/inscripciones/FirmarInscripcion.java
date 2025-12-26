/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.inscripciones;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RegpPronunciamientoJuridico;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class FirmarInscripcion extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FirmarInscripcion.class.getName());

    @Inject
    private RegistroPropiedadServices reg;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private AsynchronousService as;
    @Inject
    private SeqGenMan sec;
    @Inject
    private OmegaUploader ou;
    @Inject
    private FirmaDigitalLocal fd;

    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected List<RegMovimiento> inscripciones;
    protected Boolean hasDoc, online = false;
    protected RegRegistrador registrador;
    protected AclUser user;
    protected RegpTareasTramite rtt;
    protected List<Observaciones> observacionesTramites;
    protected RegMovimiento movimiento;
    protected String jefe_inscripcion;

    protected Boolean tieneNotaDevolutiva = false, tienePronunciamiento = false;
    protected List<RegpNotaDevolutiva> notaDevolutivaAnalisis;
    protected List<RegpPronunciamientoJuridico> pronunciamientos;
    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private ArchivoDocs archivo;

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    hasDoc = false;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }

                    observacionesTramites = reg.listarObservacionesPorTramite(ht);
                    tareas = reg.getTareasTramite(ht.getId());
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
                    inscripciones = manager.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{tramite});
                    jefe_inscripcion = itl.getUsuarioByRolName("jefe_inscripcion");
                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});
                    if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                        tieneNotaDevolutiva = Boolean.TRUE;
                    }
                    pronunciamientos = manager.findAll(Querys.getPronunciamientosByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});
                    if (Utils.isNotEmpty(pronunciamientos)) {
                        tienePronunciamiento = Boolean.TRUE;
                    }
                    // Traer documentos
                    lazyArchivos = new LazyModelWS<>(SisVars.urlOrigamiDocs + "misDocumentos?numTramite="
                            + ht.getNumTramite(), session.getToken());
                    lazyArchivos.setEntitiArray(ArchivoDocs[].class);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            ht.setEntregado(Boolean.TRUE);
            manager.update(ht);
            //if (this.validaInscripciones()) {
            //as.generarFirmaActaInscripcion(ht.getNumTramite(), session.getName_user());
            this.guardarObservacion();
            par = new HashMap<>();
            par.put("aprobado", 1);

            /*map = new HashMap<>();
                map.put("nombre", "foliador");
                AclRol rol = (AclRol) manager.findObjectByParameter(AclRol.class, map);
                if (rol != null) {
                    AclUser temp = sec.getUserForTramite(rol.getId(), 1);
                    if (temp != null) {
                        par.put("foliacion", temp.getUsuario());
                    }
                }*/
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
            /*} else {
                JsfUti.messageWarning(null, "Faltan de generar inscripciones.", "");
            }*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion() {
        try {
            if (observacion == null || observacion.isEmpty()) {
                reg.guardarObservaciones(ht, session.getName_user(), "TAREA REALIZADA",
                        this.getTaskDataByTaskID().getName());
            } else {
                reg.guardarObservaciones(ht, session.getName_user(), observacion,
                        this.getTaskDataByTaskID().getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgRegresarTramite() {
        JsfUti.update("formRegresar");
        JsfUti.executeJS("PF('dlgRegresar').show();");
    }

    public void regresarTramite() {
        try {
            this.guardarObservacion();
            par = new HashMap<>();
            par.put("aprobado", 2);
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo() + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocumentos(RegMovimiento mov) {
        movimiento = mov;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void actaInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                ss.agregarParametro("USUARIO", session.getName_user());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("P_MOVIMIENTO", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("ActaInscripcion");
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setEncuadernacion(Boolean.FALSE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void razonInscripcion() {
        try {
            //RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (movimiento != null) {
                ss.instanciarParametros();
                ss.agregarParametro("USUARIO", session.getName_user());
                ss.agregarParametro("ID_MOV", movimiento.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("REVISOR_LEGAL", movimiento.getUserCreador().getUsuario().toUpperCase());
                ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("RazonInscripcion");
                ss.setNombreSubCarpeta("registro");
                ss.setTieneDatasource(true);
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificado() {
        try {
            RegCertificado ce = reg.getCertificadoFromTarea(rtt.getId());
            if (ce != null) {
                this.llenarParametros(ce);
                switch (ce.getTipoDocumento()) {
                    case "C01": //NO POSEER BIENES
                        ss.setNombreReporte("CertificadoNoBienes");
                        break;
                    case "C02": //CERTIFICADO DE SOLVENCIA
                        ss.setNombreReporte("CertificadoSolvencia");
                        break;
                    case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                        ss.setNombreReporte("CertificadoHistoriaDominio");
                        break;
                    case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                        ss.setNombreReporte("CertificadoFichaPersonal");
                        break;
                    case "C05": //CERTIFICADO MERCANTIL
                        ss.setNombreReporte("CertificadoMercantil");
                        break;
                    case "C06": //CERTIFICADO DE RAZON DE INSCRIPCION
                        this.llenarParametrosRazon(ce);
                        break;
                    case "C07": //CERTIFICADO MERCANTIL
                        ss.setNombreReporte("CertificadoGeneral");
                        break;
                    default:
                        JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
                        return;
                }
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el certificado para el contrato.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, session.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("FDCopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion="
                    + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void nuevaObservacion() {
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");
    }

    public void guardarComentarioTramite() {
        try {
            if (observacion != null && !observacion.isEmpty()) {
                this.guardarObservacion();
                observacionesTramites = reg.listarObservacionesPorTramite(ht);
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observación.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void mantenerEstado() {
        this.guardarObservacion();
        this.continuar();
    }

    public void generarNroInscripcion(RegMovimiento mov) {
        try {
            //RegMovimiento mov = reg.getMovimientoFromTarea(tt.getId());
            if (mov.getNumInscripcion() == null || mov.getNumInscripcion() == 0) {
                mov = reg.generarInscripcion(mov);
                if (mov != null) {
                    //tareas = reg.getTareasTramite(ht.getId());
                    inscripciones = manager.findAll(Querys.getMovsByTramite,
                            new String[]{"numeroTramite"}, new Object[]{ht.getNumTramite()});
                    JsfUti.messageInfo(null, "Secuencia generada con éxito.", "");
                } else {
                    JsfUti.messageError(null, "No se pudo generar la secuencia de la inscripcion.", "");
                }
            } else {
                JsfUti.messageWarning(null, "La inscripcion ya tiene numero de inscripcion generado.", "");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaTareas() {
        for (RegpTareasTramite tt : tareas) {
            if (tt.getDetalle().getInscripcion() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean validaInscripciones() {
        for (RegMovimiento mo : inscripciones) {
            if (mo.getNumInscripcion() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean validaActasInscripciones() {
        for (RegMovimiento mo : inscripciones) {
            if (mo.getDocumentoActa() == null) {
                return false;
            }
        }
        return true;
    }

    public void imprimirNotaDevolutiva(RegpNotaDevolutiva nd) {
        try {
            if (nd.getDocumento() == null) {
                ss.borrarDatos();
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("NotaDevolutiva");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("ID_NOTA", nd.getId());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + nd.getDocumento()
                        + "&name=" + nd.getNumNotaDevolutiva() + ".pdf&tipo=3&content=application/pdf");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void generarPronunciamiento(Long idObjeto) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreReporte("PronunciamientoJuridico");
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("ID_NOTA", idObjeto);
        ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
        JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
    }

    public void updateLiquidacion() {
        try {
            manager.merge(liquidacion);
            JsfUti.redirectFaces("/procesos/inscripciones/firmarInscripcion.xhtml");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "Error al actualizar datos.", "");
        }
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            rtt = null;
            if (ta.getRevisado()) {
                JsfUti.messageWarning(null, "Tarea de trámite ya fue concluida.", "");
            } else {
                ss.instanciarParametros();
                ss.agregarParametro("tarea", ta.getId());
                ss.agregarParametro("taskID", this.getTaskId());
                ss.agregarParametro("habilitar", true);
                session.setTaskID(this.getTaskId());
                JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
            }

            /*ss.instanciarParametros();
            ss.agregarParametro("tarea", ta.getId());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("habilitar", true);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");*/
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarActaInscripcion(RegMovimiento mo) {
        try {
            if (mo.getNumInscripcion() != null && mo.getFechaInscripcion() != null) {
                File temp = fd.firmarActaInscripcion(mo, session.getName_user());
                if (temp != null) {
                    reg.guardarObservaciones(mo.getTramite().getTramite(), session.getName_user(),
                            "Firma de acta de inscripción # " + mo.getNumInscripcion(), "Firma de documento");
                    JsfUti.messageInfo(null, "Acta de inscripcion firmada con exito.", "");
                } else {
                    JsfUti.messageError(null, "No se pudo firmar el documento.", "");
                }
            } else {
                JsfUti.messageError(null, "Primero debe de generar el número de inscripción.", "");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarActa(RegMovimiento mov) {
        String ruta = SisVars.rutaFirmados;
        try {
            if (mov.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Código de verificación No encontrado.", "");
                return;
            }
            if (mov.getDocumentoActa() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + mov.getCodVerificacion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(mov.getDocumentoActa(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarRazonInscripcion() {
        try {
            if (this.validaActasInscripciones()) {
                File temp = fd.firmarRazonInscripcion(inscripciones, session.getName_user());
                if (temp != null) {
                    as.enviarCorreoFinTramiteInscripcion(liquidacion.getNumTramiteRp(), liquidacion.getCorreoTramite(),
                            session.getName_user(), temp);
                    JsfUti.messageInfo(null, "Razon de inscripcion firmada con exito.", "");
                    this.completarTarea();
                } else {
                    JsfUti.messageError(null, "No se pudo firmar el documento.", "");
                }
            } else {
                JsfUti.messageError(null, "Faltan de firmar Actas de Inscripción.", "");
            }
        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void verActaInscripcion(RegpTareasTramite tt) {
        rtt = tt;
        this.actaInscripcion();
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

    public Boolean getHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(Boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegpTareasTramite getRtt() {
        return rtt;
    }

    public void setRtt(RegpTareasTramite rtt) {
        this.rtt = rtt;
    }

    public List<RegMovimiento> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<RegMovimiento> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public Boolean getTieneNotaDevolutiva() {
        return tieneNotaDevolutiva;
    }

    public void setTieneNotaDevolutiva(Boolean tieneNotaDevolutiva) {
        this.tieneNotaDevolutiva = tieneNotaDevolutiva;
    }

    public Boolean getTienePronunciamiento() {
        return tienePronunciamiento;
    }

    public void setTienePronunciamiento(Boolean tienePronunciamiento) {
        this.tienePronunciamiento = tienePronunciamiento;
    }

    public List<RegpNotaDevolutiva> getNotaDevolutivaAnalisis() {
        return notaDevolutivaAnalisis;
    }

    public void setNotaDevolutivaAnalisis(List<RegpNotaDevolutiva> notaDevolutivaAnalisis) {
        this.notaDevolutivaAnalisis = notaDevolutivaAnalisis;
    }

    public List<RegpPronunciamientoJuridico> getPronunciamientos() {
        return pronunciamientos;
    }

    public void setPronunciamientos(List<RegpPronunciamientoJuridico> pronunciamientos) {
        this.pronunciamientos = pronunciamientos;
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

}
