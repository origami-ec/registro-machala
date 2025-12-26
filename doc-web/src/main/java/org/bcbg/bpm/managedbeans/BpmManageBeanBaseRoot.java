/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.bpm.managedbeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.chemistry.opencmis.client.api.Document;
import org.bcbg.bpm.models.DetalleProceso;
import org.bcbg.bpm.models.TareaWF;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.Notificacion;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
import org.bcbg.models.Data;
import org.bcbg.models.TramiteTareas;
import org.bcbg.models.TramiteUsuario;
import org.bcbg.services.interfaces.BpmBaseEngine;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.ApplicationContextUtils;
import org.bcbg.util.Archivo;
import org.bcbg.util.CmisUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.NumeroLetra;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.bcbg.ws.DocumentalService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Fernando
 */
public abstract class BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 5L;
    public static final Logger LOG = Logger.getLogger(BpmManageBeanBaseRoot.class.getName());

    @Inject
    protected BpmBaseEngine engine;

    @Inject
    protected UserSession session;

    @Inject
    protected ServletSession ss;
    @Inject
    protected AppServices appServices;
    @Inject
    protected BcbgService service;
    @Inject
    protected DocumentalService documentalService;

    private ArrayList<Archivo> files = new ArrayList<>();
    private String taskId;
    private HistoricoTramites bpmTramite;
    private List<Attachment> listAttachment = new ArrayList<>();
    private CmisUtil cmis;
    protected HashMap<String, Object> map;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ProcessEngine getProcessEngine() {
        return engine.getProcessEngine();
    }

    public String obtenerFormKey(String processId) {
        return engine.getProcessEngine().getFormService().getStartFormData(processId).getFormKey();
    }

    public String obtenerKeyProceso(String processId) {
        return engine.getProcessKey(processId);
    }

    public void loadProcessByClassPath(String path) {
        engine.loadSingleProcessByClassPath(path);
    }

    public List<ProcessDefinition> getProcesosDesplegados() {
        return engine.getProcessDesployedList();
    }

    public List<ProcessDefinition> getAllProcesosDesplegados() {
        return engine.getAllProcessDesployedList();
    }

    public Map<String, String> getProcesos() {
        Map<String, String> lproc = new HashMap<>();
        for (ProcessDefinition p : getProcesosDesplegados()) {
            lproc.put(p.getName(), p.getName());
        }
        return lproc;
    }

    public String generadorCeroALaIzquierda(Long n) {
        int cont = 0;
        Long num = n;
        String salida = "";
        while (num > 0) {
            num = num / 10;
            cont++;
        }
        for (int i = 0; i < 8 - cont; i++) {
            salida = salida + "0";
        }
        salida = salida + n;
        return salida;
    }

    public List<DetalleProceso> getInstanciaProcesos() {
        List<DetalleProceso> ldet = new ArrayList<>();
        DetalleProceso det;
        HistoricoTramites ht = null;
        List<HistoricProcessInstance> h = engine.getProcessInstanceHistoric();
        try {
            for (HistoricProcessInstance hpi : h) {
                ProcessDefinition p = engine.getProcessDataByDefID(hpi.getProcessDefinitionId());
                ProcessInstance pi = engine.getProcessInstanceById(hpi.getId());
                det = new DetalleProceso();
                //ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                if (ht == null) {
                    ///ht = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, new String[]{"idprocess"}, new Object[]{hpi.getId()});
                }
                if (ht != null) {
                    if (p.getName() != null) {
                        det.setNombreProceso(p.getName() + " (" + ht.getId() + ")");
                    } else {
                        det.setNombreProceso(p.getKey() + " (" + ht.getId() + ")");
                    }
                    if (pi != null) {
                        det.setInstancia(pi.getId());
                    }
                    if (ht.getId() != null) {
                        det.setIdProceso(ht.getId().toString());
                    }
                    det.setFechaInicio(hpi.getStartTime());
                    det.setFechaFin(hpi.getEndTime());
                    det.setTasks(engine.getTaskByProcessInstanceId(hpi.getId()));
                    ldet.add(det);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return ldet;
    }

    public String getProcessInstanceByTareaID(String tareaID) {
        if (tareaID != null) {
            Task tarea = engine.getTaskDataByTaskID(tareaID);
            if (tarea != null) {
                return tarea.getProcessInstanceId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public List<TareaWF> getListaTareasPersonales(String usuario, String keyTask) {
        ArrayList<Task> tareasAct = (ArrayList<Task>) engine.getUsertasksList(usuario, keyTask);
        ArrayList<Task> temp = (ArrayList<Task>) engine.getCandidateUsertasksList(usuario);
        for (Task t : temp) {
            tareasAct.add(t);
        }
        List<TareaWF> tareasWF = new ArrayList<>();
        HistoricProcessInstance p;
        HistoricoTramites historicoTramites1 = null;
        try {
            if (tareasAct.size() > 0) {
                for (Task task : tareasAct) {
                    p = engine.getHistoricProcessInstanceByInstanceID(task.getProcessInstanceId());
                    if (p != null && p.getSuperProcessInstanceId() != null) {
                        //      historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{p.getSuperProcessInstanceId()});
                        if (historicoTramites1 == null) {
                            //        historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, new String[]{"idprocess"}, new Object[]{p.getSuperProcessInstanceId()});
                            /*if (historicoTramites1 != null) {
                                historicoTramites1.setIdProceso(p.getId());
                                historicoTramites1.setCarpetaRep(historicoTramites1.getId() + "-" + p.getId());
                                acl.updateAndPersistEntity(historicoTramites1);
                            }*/
                        }
                    } else {
                        //historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                        if (historicoTramites1 == null) {
                            //  historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcIdTemp, new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                        }
                    }
                    if (historicoTramites1 != null) {
                        TareaWF tareawf = new TareaWF();
                        tareawf.setIdTramite(historicoTramites1.getNumTramite());
                        tareawf.setTarea(task);
                        /*if (task.getDescription() != null) {
                            if (task.getDescription().length() > 50) {
                                tareawf.setDescripcionTareaMayor50char(true);
                            } else {
                                tareawf.setDescripcionTareaMayor50char(false);
                            }
                        }*/
                        tareawf.setTramite(historicoTramites1);
                        //tareawf.setTramiteRp(Long.parseLong(serv.getEntityByParameters(Querys.getProcedureNumberById, new String[]{"tramite"}, new Object[]{historicoTramites1.getId()}).toString()));
                        tareasWF.add(tareawf);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return tareasWF;
    }

    public List<TareaWF> getListaTareasPersonales(String usuario, int limite, String taskDefKey) {
        List<TareaWF> tareasWF = new ArrayList<>();
        HistoricProcessInstance p;
        HistoricoTramites historicoTramites1 = null;
        TareaWF tareawf;
        List<Task> tareasAct;
        try {
            if (taskDefKey == null) {
                tareasAct = engine.getAllTasksUser(usuario, limite);
            } else {
                tareasAct = engine.getTasksUserByNameTask(usuario, limite, taskDefKey);
            }
            if (tareasAct.size() > 0) {
                for (Task task : tareasAct) {
                    /*p = engine.getHistoricProcessInstanceByInstanceID(task.getProcessInstanceId());
                    if (p != null) {
                        historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                    }*/
                    //historicoTramites1 = (HistoricoTramites) manager.find(Querys.getHistoricProceduresByProcId, new String[]{"idprocess"}, new Object[]{task.getProcessInstanceId()});
                    if (historicoTramites1 != null) {
                        tareawf = new TareaWF();
                        tareawf.setIdTramite(historicoTramites1.getNumTramite());
                        tareawf.setTarea(task);
                        tareawf.setTramite(historicoTramites1);
                        tareasWF.add(tareawf);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return tareasWF;
    }

    public List<Task> obtenerTareasUsuarios(String usuario) {
        try {
            return this.engine.getUsertasksList(usuario, null);
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List<HistoricTaskInstance> obtenerTareasUsuariosCompletadas() {
        return this.engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().finished().list();
    }

    public Boolean reasignarTarea(String id, String usuario) throws Exception {
        return this.engine.setAssigneeTask(id, usuario);
    }

    public Boolean setCandidateUsers(String id, String usuario) throws Exception {
        return this.engine.setCandidateUser(id, usuario);
    }

    public Boolean eliminarCandidateUsers(String id, String usuario) throws Exception {
        return this.engine.deleteCandidateUser(id, usuario);
    }

    public void asignarTareaPriority(List<Task> tareas, Integer prioridad) throws Exception {
        for (Task t : tareas) {
            this.engine.setTaskPriority(t.getId(), prioridad);
        }
    }

    public Task obtenerTarea(String id) {
        return engine.getTaskDataByTaskID(id);
    }

    /**
     * Lista los Archivo que a tenido el flujo durante lo que va del proceso.
     *
     * @param id_proceso String
     * @return possible object is {@link List }
     */
    public List<Archivo> documentosAdjuntados(String id_proceso) {
        List<Archivo> archi = (List<Archivo>) engine.getProcessEngine().getRuntimeService().getVariable(id_proceso, "listaArchivosFinal");
        return archi;
    }

    /**
     * Lista los Archivo que a tenido el flujo, cuando este esta finalizado.
     *
     * @param id_proceso String
     * @return possible object is {@link List }
     */
    public List<Archivo> documentosAdjuntadosTramitesFinalizados(String id_proceso) {
        List<Archivo> archi = new ArrayList<>();
        List<HistoricVariableInstance> variableInstanceQuerys = engine.getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(id_proceso).variableName("listaArchivosFinal").list();
        for (HistoricVariableInstance acc : variableInstanceQuerys) {
            archi = (List<Archivo>) acc.getValue();
        }
        return archi;
    }

    public HashMap<String, Object> variableTramitesFinalizado(String id_proceso) {
        HashMap<String, Object> map = new HashMap();
        List<HistoricVariableInstance> variableInstanceQuerys = engine.getProcessEngine().getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(id_proceso).list();
        for (HistoricVariableInstance hvi : variableInstanceQuerys) {
            map.put(hvi.getVariableName(), hvi.getValue());
            System.out.println(hvi.getVariableName() + " " + hvi.getValue());
        }
        return map;
    }

    public boolean validarEntradaDeRequisitos() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (!files.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", "Se ingresaron los requisitos con éxito!");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return true;
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje", "No ingresó correctamente los requisitos.");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return false;
        }
    }

    public Boolean validaFiles() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (files.isEmpty()) {
            JsfUti.messageWarning(null, "Debe cargar los Documentos.", "");
            return false;
        } else {
            return true;
        }
    }

    public Boolean validaUnArchivo() {
        if (files == null) {
            files = new ArrayList<>();
        }
        if (files.size() != 1) {
            JsfUti.messageWarning(null, "Debe cargar solo un Documento.", "");
            return false;
        } else {
            return true;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            Date d = new Date();
            String rutaArchivo = SisVars.rutaRepositorioArchivo + d.getTime() + event.getFile().getFileName();
            File file = new File(rutaArchivo);
            InputStream is;
            is = event.getFile().getInputStream();
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            is.close();
            out.close();
            Archivo documento = new Archivo();
            documento.setNombre(d.getTime() + "_" + event.getFile().getFileName());
            documento.setTipo(event.getFile().getContentType());
            documento.setRuta(rutaArchivo);
            this.files.add(documento);

        } catch (IOException e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ProcessInstance startProcessByDefinitionKey(String processDefinitionKey, HashMap<String, Object> parameters) {
        ProcessInstance f;
        try {
            f = engine.startProcessByDefinitionKey(processDefinitionKey, parameters);
        } catch (Exception e) {
            f = null;
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return f;
    }

    public Task getTaskDataByTaskID(String taskId) {
        return engine.getTaskDataByTaskID(taskId);
    }

    public Task getTaskDataByTaskID() {
        if (this.taskId != null) {
            return engine.getTaskDataByTaskID(this.taskId);
        }
        return null;
    }

    public void completeTask(String taskid, HashMap<String, Object> parameters) {
        try {
            engine.completeTask(taskid, parameters);
        } catch (Exception ex) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirecPag(TareaWF tarea) {
        session.setTaskID(tarea.getTarea().getId());
        session.setTitlePage(tarea.getTarea().getName());
        JsfUti.redirectFaces(tarea.getTarea().getFormKey());
    }

    public void redirecTask(TareasActivas tarea) {
        session.setTaskID(tarea.getTaskId());
        session.setTitlePage(tarea.getName_());
        session.setNumTramite(tarea.getNumTramite().longValue());
        session.setTaskDefKey(tarea.getTaskDefKey());
        JsfUti.redirectFaces(tarea.getFormKey());
    }

    public void redirectLogin() {
        JsfUti.redirectFaces2(SisVars.urlbase + "/login");
    }

    public void continuar() {
        JsfUti.redirectFaces("/procesos/dashBoard");
    }

    public void tareaResuelta() {
        JsfUti.redirectFaces("/procesos/tareaResuelta");
    }

    public void eliminarFile(Archivo doc) {
        if (Utils.isNotEmpty(files)) {
            files.remove(doc);
        }
    }

    public String usuarioObs(String user) {
        try {
            if (!user.equals("admin")) {
                User u = (User) appServices.getUsuario(new User(user));
                return u.getRecursoHumano().getPersona().getCedula() + " " + u.getRecursoHumano().getPersona().getDetalleNombre();
            } else {
                return "sistemas";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return user;
        }
    }

    public ArrayList<Archivo> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<Archivo> files) {
        this.files = files;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public List<Attachment> getAttachmentsFiles() {
        return this.engine.getAttachmentsFiles(this.getTaskId());
    }

    public List<Attachment> getAttachmentsFiles(String taskId) {
        return this.engine.getAttachmentsFiles(taskId);
    }

    public void showDocument(String url) {
        JsfUti.redirectNewTab(url);
    }

    public Object getVariable(String taskid, String varName) {
        return this.engine.getvariable(taskid, varName);
    }

    public Object getVariableByPorcessIntance(String processInstanceId, String varName) {
        return this.engine.getVariableByProcessInstance(processInstanceId, varName);
    }

    public void setVariableByProcessInstance(String processInstanceId, String varName, Object value) {
        this.engine.setVariableProcessInstance(processInstanceId, varName, value);
    }

    public List<HistoricTaskInstance> getEndedUsertasksList(String asignee) {
        return this.engine.getEndedUsertasksList(asignee);
    }

    /*public ResTareasUsuarios getResumenTareasUsuarios(String usuario, String mail) {
        ResTareasUsuarios res = new ResTareasUsuarios();
        try {
            res.setUser(usuario);
            res.setMailUser(mail);
            res.setCompletedTasks(this.getEndedUsertasksList(usuario).size());
            res.setCurrentTasks(this.obtenerTareasUsuarios(usuario).size());
            res.setTasks(this.getListaTareasPersonales(usuario, null));
        } catch (Exception ex) {
            res = null;
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }*/
    public List<HistoricTaskInstance> getTaskByProcessInstanceId(String processInstanceId) {
        return engine.getTaskByProcessInstanceId(processInstanceId);
    }

    public List<HistoricTaskInstance> getTaskByProcessInstanceIdMain(String processInstanceId) {
        return engine.getTaskByProcessInstanceIdMain(processInstanceId);
    }

    public HistoricTaskInstance getTaskByProcessInstance(String processInstanceId) {
        return engine.getLastTaskByProcessInstance(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAttachmentsFiles(String processInstanceId) {
        return this.engine.getProcessInstanceAttachmentsFiles(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAllAttachmentsFiles(String processInstanceId) {
        return this.engine.getAttachmentsFilesByProcessInstanceIdMain(processInstanceId);
    }

    public List<Attachment> getProcessInstanceAttachmentsFiles() {
        if (this.getTaskDataByTaskID() != null) {
            return this.engine.getProcessInstanceAttachmentsFiles(this.getTaskDataByTaskID().getProcessInstanceId());
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachmentsFiles() {
        if (this.getTaskDataByTaskID() != null) {
            Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
            //HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{idHistTram});
            map = new HashMap();
            map.put("numTramite", tramite);
//            HistoricoTramites ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
//            return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
            return null;
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachments() {
        if (this.getTaskDataByTaskID() != null) {
            Long idHistTram = (Long) this.getVariable(session.getTaskID(), "tramite");
            //HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{idHistTram});
            //return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
            return null;
        } else {
            return null;
        }
    }

    public List<Attachment> getProcessInstanceAllAttachments(Long id) {
        //HistoricoTramites ht = (HistoricoTramites) manager.find(Querys.getHistoricoTramiteById, new String[]{"id"}, new Object[]{id});
        //return this.engine.getAttachmentsFilesByProcessInstanceIdMain(ht.getIdProceso());
        return null;
    }

    public Object getvariableByExecutionId(String taskId, String varName) {
        return engine.getvariableByExecutionId(taskId, varName);
    }

    public Document attachDocument(String carpeta, String fileName, String mimetype, byte[] content) {
        CmisUtil cmisu;
        try {
            cmisu = (CmisUtil) ApplicationContextUtils.getBean("cmisUtil");
            if (cmisu != null) {
                return cmisu.createDocument(cmisu.getFolder(carpeta), fileName, mimetype, content);
            }
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public void descargarDocumento(String url) {
        String s[] = url.split("nodeRef=");
        JsfUti.redirectNewTab("/sgr/DescargarDocsRepositorio?id=" + s[1]);
    }

    public void mostrarDocumento(String url) {
        String s[] = url.split("nodeRef=");
        JsfUti.redirectNewTab("/sgr/AbrirDocsRepositorio?id=" + s[1]);
    }

    public List<Task> obtenerTareasActivasProcessInstance(String processInstanceId) {
        return this.engine.getListTaskActiveByProcessInstance(processInstanceId);
    }

    public List<IdentityLink> obtenerIdentityLinkByIdTask(String taskId) {
        return this.engine.identityLinkPorTareaId(taskId);
    }

    public List<HistoricIdentityLink> obtenerHistoricIdentityLinkByIdTask(String taskId) {
        return this.engine.HistoricidentityLinkPorTareaId(taskId);
    }

    public List<String> obtenerProcessInstanceByProcessInstaceIdMain(String processInstaceId) {
        return this.engine.getListProcessInstanceIdsByProcessInstanceIdMain(processInstaceId);
    }

    public List<Attachment> getListAttachment() {
        return listAttachment;
    }

    public void setListAttachment(List<Attachment> listAttachment) {
        this.listAttachment = listAttachment;
    }

    public List<Attachment> getListAttAttachmentByProcessInstance() {
        String processInstance = this.getProcessInstanceByTareaID(taskId);
        return this.getProcessInstanceAllAttachmentsFiles(processInstance);
    }

    public String getNombresUsuario(String assignee) {
        return appServices.getUsuario(new User(assignee)).getRecursoHumano().getPersona().getDetalleNombre();
    }

    public String usuariosCandidatos(String idTask) {
        try {
            String candidatosAsignados = "";
            if (idTask != null) {
                for (IdentityLink identityLink : this.obtenerIdentityLinkByIdTask(idTask)) {

                    candidatosAsignados = candidatosAsignados + ", " + usuarioObs(identityLink.getUserId());
                }
                if (!candidatosAsignados.isEmpty()) {
                    return candidatosAsignados.substring(2);
                }
//                String[] cedulas = candidatosAsignados.split(",");
//                System.out.println("cedulasss:   "+ cedulas);
//                String nameCandidatosAsigne = "";
//                for (String s : cedulas) {
//                    System.out.println("cedula : " + s);
//                    nameCandidatosAsigne = nameCandidatosAsigne + ", " + usuarioObs(s);
//                }
//                if (!nameCandidatosAsigne.isEmpty()) {
//                    return nameCandidatosAsigne.substring(2);
//                }
            }

            return "";
        } catch (Exception e) {
            Logger.getLogger(BpmManageBeanBase.class.getName()).log(Level.SEVERE, ("Error en tarea " + idTask), e);
            return "";
        }
    }

    public String obtenerDepartamentoByUsuario(String usuario, String idTask) {
        String[] ua = usuario != null && !usuario.isEmpty() ? usuario.split(" ") : usuariosCandidatos(idTask).split(" ");
        String u = ua.length > 0 ? ua[0] : "";
        if (u != null && !u.isEmpty()) {
            Departamento rest = appServices.buscarDepartamentoXusuario(u);
            if (rest != null) {
                return rest.getNombre();
            }
        }
        return "";
    }

    public Object getVariables(String taskId, String variable) {
        if (taskId != null) {
            Map<String, Object> v = engine.getVar(taskId);
            for (Map.Entry<String, Object> entrySet : v.entrySet()) {
                if (entrySet.getKey().compareTo(variable) == 0) {
                    return entrySet.getValue();
                }
            }
        }

        return null;
    }

    public Integer getCantidadTareasUser(String usuario) {
        return engine.getNumberTasksUser(usuario, null);
    }

    public Integer getCantidadTareasUser(String usuario, String taskDefKey) {
        return engine.getNumberTasksUser(usuario, taskDefKey);
    }

    public void deleteProcessInstance(String instance) {
        this.engine.deleteProcessInstance(instance, "Eliminado por usuario - " + (new Date()).getTime());
    }

    public byte[] getByteArray(String ruta) {
        File file = new File(ruta);
        return file.getAbsolutePath().getBytes();
    }

    public Task getTaskByProcessId(String processId) {
        return engine.getTaskDataByProcessID(processId);
    }

    public Object getExecutionVariable(String taskid, String instanceId, String varName) {
        return this.engine.getvariableByExecutionId(taskid, instanceId, varName);
    }

    public HistoricoTramites getBpmTramite() {
        return bpmTramite;
    }

    public void setBpmTramite(HistoricoTramites bpmTramite) {
        this.bpmTramite = bpmTramite;
    }

    public void removerContenido(Object valor, Collection listado) {
        listado.remove(valor);
    }

    public CmisUtil getCmis() {
        cmis = (CmisUtil) ApplicationContextUtils.getBean("cmisUtil");
        return cmis;
    }

    public byte[] leerArchivo(String ruta) throws Exception {
        Path path = Paths.get(ruta);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    protected void initDocEsc(String numTramite) {
        try {

        } catch (Exception e) {
            e.toString();
        }
    }

    public void descargarDocHabilitante() {
        try {
            ss.instanciarParametros();
            //ss.agregarParametro("idTransaccion", data.getIdTransaccion());
            //ss.agregarParametro("libreria", base);
            JsfUti.redirectNewTab(SisVars.urlbase + "docsPdf");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(BpmManageBeanBaseRoot.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlg(String urlFacelet) {
        try {
            if (!urlFacelet.startsWith("/")) {
                urlFacelet = "/" + urlFacelet;
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "70%");
            options.put("closable", true);
            options.put("closeOnEscape", true);
            options.put("contentWidth", "100%");
            PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
//            RequestContext.getCurrentInstance().openDialog(urlFacelet, options, null);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, taskId, e);
        }
    }

    public void showDlgWith(String urlFacelet) {
        if (!urlFacelet.startsWith("/")) {
            urlFacelet = "/" + urlFacelet;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "75%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, null);
//        RequestContext.getCurrentInstance().openDialog(urlFacelet, options, null);
    }

    public void showDlgWithParams(String urlFacelet, Map<String, List<String>> params) {
        if (!urlFacelet.startsWith("/")) {
            urlFacelet = "/" + urlFacelet;
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("draggable", false);
        options.put("modal", true);
        options.put("width", "75%");
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("contentWidth", "100%");
        PrimeFaces.current().dialog().openDynamic(urlFacelet, options, params);
    }

    protected List<String> addParam(Object b) {
        if (b == null) {
            return null;
        }
        List<String> p = new ArrayList<>(1);
        p.add(b.toString());
        return p;
    }

    public String cantidadstring(String cantidad) {
        NumeroLetra n = new NumeroLetra();
        return n.convertir(cantidad, true) + " CON " + cantidad.substring(cantidad.indexOf('.') + 1) + "/100";
    }

    public StreamedContent imagenProceso(String id, String name) {
        try {
            return DefaultStreamedContent.builder().contentType("image/png").stream(() -> engine.getProcessDiagram(id, name)).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public void getPDF(SolicitudDocumentos doc) {
        if (doc.getRutaArchivo() == null) {
            JsfUti.messageInfo(null, "Error.", "No existe el documento");
            return;
        }
        String[] tipo = doc.getRutaArchivo().split("\\.");
        if (tipo[1].equals("zip")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo ZIP no se puede visualizar, Debe descargarlo");
            return;
        } else if (tipo[1].equals("doc") || tipo[1].equals("docx") || tipo[1].equals("xls") || tipo[1].equals("xlsx")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo " + tipo[1] + " no se puede visualizar, Debe descargarlo");
            return;
        }
        ss.borrarDatos();
        ss.setUrlWebService(Utils.getUrlViewFile(doc.getRutaArchivo()));
        JsfUti.redirectNewTab("/origamiGT/ReporteWS");
    }

    public void getPDF(String ruta) {
        if (ruta == null) {
            JsfUti.messageInfo(null, "Error.", "No existe el documento");
            return;
        }
        String[] tipo = ruta.split("\\.");
        String lastOne = tipo[tipo.length - 1];

        if (lastOne.equals("zip")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo ZIP no se puede visualizar, Debe descargarlo");
            return;
        } else if (lastOne.equals("doc") || lastOne.equals("docx") || lastOne.equals("xls") || lastOne.equals("xlsx")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo " + tipo[1] + " no se puede visualizar, Debe descargarlo");
            getDownloadFile(ruta);
            return;
        }
        ss.borrarDatos();
        ss.setUrlWebService(Utils.getUrlViewFile(ruta));
        JsfUti.redirectNewTab("/origamiGT/ReporteWS");
    }

    public void getDownloadFile(SolicitudDocumentos doc) {

        if (doc.getRutaArchivo() == null) {
            JsfUti.messageInfo(null, "Error.", "No existe el documento");
            return;
        }
        JsfUti.redirectNewTab(Utils.getUrlDownloadFile(doc.getRutaArchivo()));
    }

    public void getDownloadFile(String ruta) {

        JsfUti.redirectNewTab(Utils.getUrlDownloadFile(ruta));
    }

    public void visualizarNotificacion(Notificacion notificacion) {
        try {
            ss.borrarDatos();
            ss.setUrlWebService(SisVars.ws + "reportes/notificacion/" + notificacion.getId());
            JsfUti.redirectFacesNewTab("/ReporteWS");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "Error al generar el documento.", "");
        }
    }

    /**
     *
     * @param definitionKey
     * @return True if TaskDefinitionKey is iquals param definitionKey
     */
    public Boolean validarTareaByTaskDefinitionKey(String definitionKey) {
        if (getTaskDataByTaskID().getTaskDefinitionKey().equals(definitionKey)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public SolicitudDocumentos crearSolicitudDocumento(File file, UploadedFile upLoadedFile, SolicitudServicios solicitudServicios) {
        SolicitudDocumentos solicitudDocumentos = new SolicitudDocumentos();
        solicitudDocumentos.setEstado("A");
        solicitudDocumentos.setSolicitud(solicitudServicios);
        solicitudDocumentos.setUsuario(session.getName_user());
        solicitudDocumentos.setFechaCreacion(new Date());
        solicitudDocumentos.setNombreArchivo(file.getName());
        solicitudDocumentos.setTipoArchivo(upLoadedFile.getContentType());
        solicitudDocumentos.setRutaArchivo(file.getAbsolutePath());
        solicitudDocumentos.setTieneFirmaElectronica(Boolean.FALSE);
        return solicitudDocumentos;
    }

    public void eliminar(SolicitudDocumentos doc) {
        try {
            service.methodPUT(doc, SisVars.ws + "solicitudDocumento/eliminar", SolicitudDocumentos.class);
            JsfUti.messageInfo(null, "Información.", "Documento eliminado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

}
