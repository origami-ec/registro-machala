/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpEstadoLiquidacion;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.Archivo;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class EntregaTramiteRp extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(EntregaTramiteRp.class.getName());

    @Inject
    private IngresoTramiteLocal itl;

    @Inject
    private RegistroPropiedadServices reg;

    @Inject
    private AsynchronousService as;

    protected ContenidoReportes comprobante;
    protected HistoricoTramites ht;
    protected LazyModel<RegpLiquidacion> liquidaciones;
    protected RegpLiquidacion liquidacion;
    protected ContenidoReportes proforma;
    protected RegRegistrador registrador;
    protected Boolean permitido = false;
    protected Boolean prioridad = false;
    protected List<Attachment> listAttach = new ArrayList<>();
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected HistoricTaskInstance tareaActual;
    protected List<AclUser> users = new ArrayList<>();
    protected Boolean showUsers = false;
    protected Boolean xpress = false;
    protected Long usuario = 0L;
    protected String observaciones;
    protected String formatoArchivos;
    protected int priority = 0;
    protected String obsDesblock;
    protected String obsPriority;
    private Boolean tramiteFinalizado;

    protected List<RegpDocsTarea> docs = new ArrayList<>();
    protected Boolean hasDoc = false, online = false;
    private List<Observaciones> htObservaciones;
    protected String descripcionTareaActual;
    protected String observacion;
    protected String email;

    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private ArchivoDocs archivo;

    @PostConstruct
    protected void iniView() {
        try {
            tramiteFinalizado = Boolean.FALSE;
            formatoArchivos = SisVars.formatoArchivos;
            usuario = session.getUserId();
            ht = new HistoricoTramites();
            liquidacion = new RegpLiquidacion();
            liquidaciones = new LazyModel(RegpLiquidacion.class, "numTramiteRp", "DESC");
            map = new HashMap();
            map.put("code", Constantes.piePaginaProforma);
            proforma = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
            map = new HashMap();
            map.put("code", Constantes.piePaginaComprobante);
            comprobante = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);
            this.cargarUsuariosRegistro();
            this.validaRoles();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        for (Long l : session.getRoles()) {
            if (l == 1 || l == 18) { //administrador //supervisor_registral
                permitido = true;
            }
            if (l == 28) { //gerente_general
                prioridad = true;
            }
        }
    }

    public void showProforma(RegpLiquidacion re) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("comprobante_proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", re.getId());
            ss.agregarParametro("FOOTER", proforma.getValor());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreCompleto());
            ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
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
                ss.setNombreReporte("comprobanteIngreso");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_LIQUIDACION", re.getId());
                ss.agregarParametro("FOOTER", comprobante.getValor());
                ss.agregarParametro("VALOR_STRING", this.cantidadstring(re.getTotalPagar().toString()));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "La proforma no ha sido ingresada.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showInfo(RegpLiquidacion re) {
        try {
            descripcionTareaActual = "";
            tramiteFinalizado = Boolean.FALSE;
            online = false;
            ht = re.getTramite();
            liquidacion = re;
            if (!liquidacion.getCertificadoSinFlujo()) {
                exec();
            } else if (liquidacion.getCertificadoSinFlujo() && liquidacion.getTramiteOnline()) {
                exec();
            } else {
                xpress = true;
                map = new HashMap();
                map.put("numTramite", liquidacion.getNumTramiteRp());
                HistoricoTramites historicoTramites = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                htObservaciones = new ArrayList(historicoTramites.getObservacionesCollection());
                if (Utils.isEmpty(htObservaciones)) {
                    tramiteFinalizado = Boolean.FALSE;
                } else {
                    htObservaciones.sort((Observaciones o1, Observaciones o2) -> o2.getId().compareTo(o1.getId()));
                    if (htObservaciones.get(0).getObservacion().contains("retira los documentos")) {
                        tramiteFinalizado = Boolean.TRUE;
                        descripcionTareaActual = "TRÁMITE ENTREGADO";
                    } else {
                        switch (htObservaciones.get(0).getObservacion()) {
                            case "EMISIÓN DE CERTIFICADO":
                            case "PENDIENTE DE APROBACIÓN":
                                tramiteFinalizado = Boolean.TRUE;
                                break;
                            case "APROBADO":
                                tramiteFinalizado = Boolean.FALSE;
                                break;
                        }
                        descripcionTareaActual = htObservaciones.get(0).getObservacion().toUpperCase();
                    }
                }
            }
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDocsD(RegpLiquidacion re) {
        try {
            online = false;
            ht = re.getTramite();
            liquidacion = re;

            lazyArchivos = new LazyModelWS<>(SisVars.urlOrigamiDocs + "misDocumentos?numTramite="
                    + ht.getNumTramite(), session.getToken());
            lazyArchivos.setEntitiArray(ArchivoDocs[].class);

            JsfUti.update("formInformDocsDT");
            JsfUti.executeJS("PF('dlgVerDocsRp').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showEnvTramiteD(RegpLiquidacion re) {
        try {
            online = false;
            liquidacion = re;
            email = liquidacion.getCorreoTramite();
            this.cargarDatosReporte();
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            JsfUti.update("formEnvioProfMail");
            JsfUti.executeJS("PF('dlgReenvioProf').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void exec() {
        xpress = false;
        htObservaciones = new ArrayList(ht.getObservacionesCollection());
        if (liquidacion.getTramite().getIdProceso() == null) {
            tareas = new ArrayList<>();
            listAttach = new ArrayList<>();
        } else {
            tareas = this.getTaskByProcessInstanceIdMain(liquidacion.getTramite().getIdProceso());
            if (!tareas.isEmpty()) {
                tareaActual = tareas.get(0);
                switch (tareaActual.getName()) {
                    case "Entregar Devolutiva":
                    case "Entregar Certificado":
                    case "Entregar Trámite":
                    case "Entregar Nota Devolutiva":
                    case "Entrega Trámite":
                    case "Entrega Devolutiva":
                        if (tareaActual.getEndTime() == null) {
                            tramiteFinalizado = Boolean.TRUE;
                        }
                        break;
                    default:
                        tramiteFinalizado = Boolean.FALSE;
                        break;
                }
                priority = tareaActual.getPriority();
                descripcionTareaActual = tareaActual.getName().toUpperCase();
            }
            if (liquidacion.getEstadoPago().getId() == 7L) {
                online = true;
            }
        }
    }

    public void realizarTarea() {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("tramite", liquidacion.getNumTramiteRp());
            session.setTaskID(this.getTaskIdFromNumTramite(liquidacion.getNumTramiteRp()));
            session.setTitlePage(tareaActual.getName());
            JsfUti.redirectFaces(tareaActual.getFormKey());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getTaskIdFromNumTramite(Long tramite) {
        String query = "SELECT task_id FROM flow.tareas_activas ta WHERE ta.num_tramite = " + tramite;
        return (String) manager.getNativeQuery(query);
    }

    public void downloadDocHabilitante() {
        try {
            if (ht.getId() != null) {
                RegpDocsTramite rdt = (RegpDocsTramite) manager.find(Querys.getDocsTramiteByTramite, new String[]{"idTramite"}, new Object[]{ht.getId()});
                if (rdt != null) {
                    JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo() + "&tipo=3&content=" + rdt.getContentType());
                } else {
                    JsfUti.messageWarning(null, "NO se encuentra documento habilitante!!!", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocTareas(RegpDocsTarea rdt) {
        try {
            if (rdt.getId() != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo() + "&tipo=2&content=" + rdt.getContentType());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarPrioridad() {
        try {
            if (tareaActual.getProcessInstanceId() != null && priority > 0) {
                this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), "prioridad", prioridad);
                List<Task> tareasActivas = this.obtenerTareasActivasProcessInstance(tareaActual.getProcessInstanceId());
                this.asignarTareaPriority(tareasActivas, priority);
                reg.guardarObservaciones(ht, session.getName_user(), obsPriority == null ? "PRIORIDAD: " + priority : obsPriority, "PRIORIDAD TRAMITE");
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void updateBlockHt() {
        try {
            if (ht.getId() != null) {
                ht.setUserDesblock(session.getUserId());
                ht.setFechaDesblock(new Date());
                manager.update(ht);
                reg.guardarObservaciones(ht, session.getName_user(), obsDesblock == null ? "DESBLOQUEAR TRAMITE" : obsDesblock, "DESBLOQUEAR TRAMITE");
                JsfUti.messageInfo(null, "Prioridad de tramite actualizada.", "");
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR.", "");
        }
    }

    public void editProforma(RegpLiquidacion re) {
        if (re.getEstadoLiquidacion().getId() == 1L) { // ESTADO LIQUIDACION INGRESADA
            ss.instanciarParametros();
            ss.agregarParametro("proforma", re.getId());
            JsfUti.redirectFaces("/procesos/registro/editarProforma.xhtml");
        } else {
            JsfUti.messageWarning(null, "Liquidacion no se puede Editar.", "");
        }
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

    public void cargarUsuariosRegistro() {
        try {
            users = reg.getUsuariosByRolName("certificador");
            List<AclUser> temp = reg.getUsuariosByRolName("inscriptor");
            for (AclUser u : temp) {
                users.add(u);
            }
            users.size();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgReAsignarUser(RegpLiquidacion re) {
        try {
            showUsers = true;
            ht = re.getTramite();
            if (ht.getIdProceso() == null) {
                JsfUti.messageError(null, "El tramite no ha sido Ingresado. No hay tarea para re-asignar.", "");
            } else {
                tareas = this.getTaskByProcessInstanceIdMain(ht.getIdProceso());
                if (tareas.size() > 0) {
                    tareaActual = tareas.get(0);
                    if (tareaActual.getTaskDefinitionKey().equalsIgnoreCase("analisisProcesoRegistral")
                            || tareaActual.getTaskDefinitionKey().equalsIgnoreCase("inscribirCertificar")) {
                        if (tareaActual.getEndTime() == null) {
                            if (tareaActual.getAssignee() == null) {
                                showUsers = false;
                                JsfUti.messageError(null, "Esta tarea no se puede re-asignar. Tiene usuarios candidatos.", "");
                            }
                            JsfUti.update("formreasignar");
                            JsfUti.executeJS("PF('dlgReasignar').show();");
                        } else {
                            JsfUti.messageError(null, "No se puede re asignar Tarea. Tarea Finalizada.", "");
                        }
                    } else {
                        JsfUti.messageWarning(null, "No se puede re asignar Tarea.", "Tarea actual: " + tareaActual.getName());
                    }
                } else {
                    JsfUti.messageError(null, "No se encontro tarea para reasignar.", "");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reasignarTarea(AclUser user) {
        try {
            String obs = "TAREA: " + tareaActual.getName() + ", USUARIO ANTERIOR: " + tareaActual.getAssignee() + ", USUARIO ACTUAL: " + user.getUsuario();
            reg.guardarObservaciones(ht, session.getName_user(), obs, "REASIGNACION DE USUARIO");
            this.reasignarTarea(tareaActual.getId(), user.getUsuario());
            Map<String, Object> v = this.engine.getvariables(tareaActual.getProcessInstanceId());
            for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                if (entrySet.getValue() != null && entrySet.getValue().equals(tareaActual.getAssignee())) {
                    this.setVariableByProcessInstance(tareaActual.getProcessInstanceId(), entrySet.getKey(), user.getUsuario());
                }
            }
            JsfUti.executeJS("PF('dlgReasignar').hide();");
            JsfUti.update("mainForm:dtTramites");
            JsfUti.messageInfo(null, "Tarea Re-Asignada con exito.", "");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cargaDocumento() {
        String url = "";
        //String url = SisVars.urlServidorAlfrescoPublica + "share/page/site/smbworkflow/document-details?nodeRef=";
        Document doc;
        Folder folder;
        byte[] data;
        try {
            if (ht.getCarpetaRep() != null && ht.getIdProceso() != null) {
                if (this.getCmis() != null) {
                    folder = this.getCmis().getFolder(ht.getCarpetaRep());
                    if (folder == null) {
                        Folder carpeta = this.getCmis().getFolder(ht.getTipoTramite().getCarpeta());
                        if (carpeta != null) {
                            folder = this.getCmis().createSubFolder(carpeta, ht.getCarpetaRep());
                        }
                    }
                    if (folder != null) {
                        for (Archivo f : this.getFiles()) {
                            data = this.leerArchivo(f.getRuta());
                            doc = this.getCmis().createDocument(folder, f.getNombre(), f.getTipo(), data);
                            f.setUrl(url + doc.getId());
                            if (f.getTipo() != null) {
                                this.getProcessEngine().getTaskService().createAttachment(f.getTipo(), null, ht.getIdProceso(), f.getNombre(), "Archivo Adjunto de tarea " + (new Date()).getTime() + "(" + ht.getIdProceso() + ")" + f.getNombre(), f.getUrl());
                            } else {
                                this.getProcessEngine().getTaskService().createAttachment("url", null, ht.getIdProceso(), f.getNombre(), "Archivo Adjunto de tarea " + (new Date()).getTime() + "(" + ht.getIdProceso() + ")" + f.getNombre(), f.getUrl());
                            }
                            reg.guardarObservaciones(ht, session.getName_user(), "SE ADJUNTARON " + this.getFiles().size() + " DOCUMENTO(S) AL TRAMITE.", "ASOCIAR DOCUMENTO");
                            JsfUti.messageInfo(null, "Se adjuntaron documentos al tramites correctamente...", "");
                            JsfUti.update("mainForm");
                            JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
                        }
                        this.setFiles(new ArrayList<Archivo>());
                    }
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showFormularioOnline() {
        try {
            if (ht.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("FormularioOnline");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_TRAMITE", ht.getId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + " OmegaDownDocs?code= " + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    //Anula una factura: En el la tabla de regLiquidaciones en el estado de la factura le pone False
    public void inactivateInvoice(String observacion) {
        try {//valida que la observacion haya sido escrita
            if (observacion == null || observacion.equals("")) {
                JsfUti.messageError(null, "Debe Ingresar Observaciones", "");
                JsfUti.update("mainFormAnulada:tableAnulada");
            } else {
                RegpEstadoLiquidacion estadoLiquidacion = new RegpEstadoLiquidacion();
                estadoLiquidacion.setId(Long.parseLong("3"));
                estadoLiquidacion.setCode("ANULADA");
                liquidacion.setEstadoLiquidacion(estadoLiquidacion);
                liquidacion.setUserAnula(session.getUserId());
                liquidacion.setInfAdicional(observaciones);
                manager.update(liquidacion);
                JsfUti.messageInfo(null, Messages.liquidacionAnulada, "");
                JsfUti.update("mainFormAnulada:tableAnulada");
                observaciones = "";
            }

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }

    }

    //busca liquidacion
    public void selectInvoice(RegpLiquidacion rl) {
        try {
            liquidacion = manager.find(RegpLiquidacion.class, rl.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgObservacion() {
        JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");
    }

    public void guardarObservacion() {
        try {
            if (observacion == null || observacion.isEmpty()) {
                JsfUti.messageError(null, "Debe Ingresar Observaciones", "");
            } else {
                reg.guardarObservaciones(ht, session.getName_user(), observacion, "VENTANILLA");
                JsfUti.redirectFaces("/procesos/manage/entregaDocumentos.xhtml");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgReenvioCorreo() {
        try {
            if (liquidacion.getTramite().getEstadoTramite() == 2) {
                //email = liquidacion.getBeneficiario().getCorreo1();
                email = liquidacion.getCorreoTramite();
                JsfUti.executeJS("PF('dlgVerInfoRp').hide();");
                JsfUti.update("formEnvioMail");
                JsfUti.executeJS("PF('dlgReenvioCorreo').show();");
            } else {
                JsfUti.messageError(null, "El trámite debe estar marcado como finalizado.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reenviarDocsEmail() {
        try {
            if (email == null || email.isEmpty()) {
                JsfUti.messageError(null, "Debe Ingresar un email válido", "");
            } else {
                as.reenviarCorreoDocumentos(liquidacion.getNumTramiteRp(), email, session.getName_user());
                observacion = "Reenvio de documentos firmados al correo: " + email + ".";
                reg.guardarObservaciones(ht, session.getName_user(), observacion, "REENVIO DE CORREO");
                JsfUti.executeJS("PF('dlgReenvioCorreo').hide();");
                JsfUti.messageInfo(null, "Correo enviado.", "Puede demorar unos minutos que le llegue al usuario.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void continuarNuevaProforma() {
        try {
            if (liquidacion.getId() != null) {
                String rutaDocumento = SisVars.rutaTemporales + "proforma-" + liquidacion.getNumTramiteRp() + ".pdf";
                as.enviarCorreoProformaTramite(liquidacion, rutaDocumento, session.getName_user());
                JsfUti.redirectFaces("/procesos/manage/entregaDocumentos.xhtml");
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void cargarDatosReporte() {
        try {
            ss.instanciarParametros();
            ss.setGeneraFile(true);
            ss.setRutaDocumento(SisVars.rutaTemporales + "proforma-" + liquidacion.getNumTramiteRp() + ".pdf");
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("proforma");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", liquidacion.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            ss.agregarParametro("IMG_CUENTAS", JsfUti.getRealPath("/resources/image/bancos_registro.png"));
        } catch (Exception e) {
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

    public LazyModel<RegpLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(LazyModel<RegpLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<AclUser> getUsers() {
        return users;
    }

    public void setUsers(List<AclUser> users) {
        this.users = users;
    }

    public Boolean getShowUsers() {
        return showUsers;
    }

    public void setShowUsers(Boolean showUsers) {
        this.showUsers = showUsers;
    }

    public HistoricTaskInstance getTareaActual() {
        return tareaActual;
    }

    public void setTareaActual(HistoricTaskInstance tareaActual) {
        this.tareaActual = tareaActual;
    }

    public List<Attachment> getListAttach() {
        return listAttach;
    }

    public void setListAttach(List<Attachment> listAttach) {
        this.listAttach = listAttach;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public Boolean getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Boolean prioridad) {
        this.prioridad = prioridad;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getObsDesblock() {
        return obsDesblock;
    }

    public void setObsDesblock(String obsDesblock) {
        this.obsDesblock = obsDesblock;
    }

    public String getObsPriority() {
        return obsPriority;
    }

    public void setObsPriority(String obsPriority) {
        this.obsPriority = obsPriority;
    }

    public List<RegpDocsTarea> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTarea> docs) {
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

    public Boolean getTramiteFinalizado() {
        return tramiteFinalizado;
    }

    public void setTramiteFinalizado(Boolean tramiteFinalizado) {
        this.tramiteFinalizado = tramiteFinalizado;
    }

    public List<Observaciones> getHtObservaciones() {
        return htObservaciones;
    }

    public void setHtObservaciones(List<Observaciones> htObservaciones) {
        this.htObservaciones = htObservaciones;
    }

    public Boolean getXpress() {
        return xpress;
    }

    public void setXpress(Boolean xpress) {
        this.xpress = xpress;
    }

    public String getDescripcionTareaActual() {
        return descripcionTareaActual;
    }

    public void setDescripcionTareaActual(String descripcionTareaActual) {
        this.descripcionTareaActual = descripcionTareaActual;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
