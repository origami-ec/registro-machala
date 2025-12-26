/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.Notificacion;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.RequisitosErrores;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.Data;
import org.bcbg.models.UserTask;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author ricardo
 */
@Named
@ViewScoped
public class TramitesGeneralesMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<SolicitudServicios> lazyVentanillaTodos;
    private Boolean servicioActivo, notificacionActiva, renderedView;
    private SolicitudDocumentos documento, documentoGuardar;
    private ArrayList<SolicitudDocumentos> documentos;
    private List<RequisitosErrores> listRequisitosErrores;
    private Notificacion notificacion;
    private List<ServiciosDepartamentoRequisitos> requisitos;
    private SolicitudServicios solicitudServicio;
    private List<SolicitudDepartamento> solicitudDepartamentos;
    private List<ServiciosDepartamento> serviciosDepartamentos;
    private Departamento departamento;
    private List<TipoTramite> tiposTramites = new ArrayList<>();
    private StreamedContent imageProcessInstance;
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    private List<Observaciones> observaciones = new ArrayList<>();

    private String ciUser;
    private Integer indexDocumento;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        servicioActivo = Boolean.FALSE;
        notificacionActiva = Boolean.FALSE;
        renderedView = Boolean.FALSE;
        solicitudServicio = new SolicitudServicios();
        documentos = new ArrayList<>();
        listRequisitosErrores = new ArrayList<>();
        solicitudDepartamentos = new ArrayList<>();
        serviciosDepartamentos = new ArrayList<>();
        initSolicituVU();
    }

    private void initSolicituVU() {
        lazyVentanillaTodos = new LazyModelWS<>(SisVars.ws + "solicitudServicios/mapper?sort=id,DESC", SolicitudServicios[].class, session.getToken());
        tiposTramites = new ArrayList<>();
        List<ServiciosDepartamento> temp = appServices.getListItems(new ServiciosDepartamento(null, null));
        if (Utils.isNotEmpty(temp)) {
            serviciosDepartamentos.addAll(temp);
        }
    }

    public void showReporteTicket(SolicitudServicios solicitud) {
        try {
            ss.borrarDatos();
            String url;
            if (solicitud != null) {
                url = SisVars.ws + "reportes/inicioTramite/ticket/" + solicitud.getId();
                ss.setNombreDocumento(solicitud.getTramite().getCodigo());
            } else {
                url = SisVars.ws + "reportes/inicioTramite/reimprimirTicket/" + solicitudServicio.getId();
                ss.setNombreDocumento(solicitudServicio.getTramite().getCodigo());
            }
            ss.setUrlWebService(url);
            JsfUti.redirectFacesNewTab("/ReporteWS");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void detalleSolicitudServicio(SolicitudServicios servicio) {
        solicitudServicio = servicio;
        tareaActiva(servicio.getTramite().getNumTramite().intValue());
        tareas = this.getTaskByProcessInstanceIdMain(servicio.getTramite().getIdProceso());
        observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(servicio.getTramite().getId())));
        SolicitudDocumentos solicitudDocumentos = new SolicitudDocumentos(new SolicitudServicios(solicitudServicio.getId()));
        documentos = new ArrayList<>(appServices.buscarSolicitudDocumentos(solicitudDocumentos));
        getTituloCredito(servicio);
        ServiciosDepartamentoRequisitos req = null;
        if (solicitudServicio.getTipoServicio() != null) {
            req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(solicitudServicio.getTipoServicio().getId()));
        } else if (solicitudServicio.getTipoTramite() != null && solicitudServicio.getTipoServicio() == null) {
            req = new ServiciosDepartamentoRequisitos(new TipoTramite(solicitudServicio.getTipoTramite().getId()));
        }
        if (req != null) {
            requisitos = appServices.getDeptsItemsRequisito(req);
        }
        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento(new SolicitudServicios(servicio.getId()));
        solicitudDepartamentos = appServices.buscarSolicitudDepartamentos(solicitudDepartamento);
        loadFlujo();
        renderedView = Boolean.TRUE;
    }

    public void loadFlujo() {
        InputStream diagram = engine.getProcessInstanceDiagram(solicitudServicio.getTramite().getIdProceso());
        if (diagram != null) {
            imageProcessInstance = DefaultStreamedContent.builder().stream(() -> diagram).build();
        }
    }

    public void tareaActiva(Integer numTramite) {
        List<TareasActivas> tareas = (List<TareasActivas>) appServices.getTareaActiva(numTramite);
        if (Utils.isNotEmpty(tareas)) {
            setServicioActivo(Boolean.TRUE);
        } else {
            setServicioActivo(Boolean.FALSE);
        }
    }

    private void getTituloCredito(SolicitudServicios s) {

        ciUser = "";

    }

    public void generarPDF() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            ss.borrarDatos();
            ss.borrarParametros();
            solicitudServicio.setUsersTasks(new ArrayList<>());
            if (tareas != null && !tareas.isEmpty()) {
                for (HistoricTaskInstance hti : tareas) {
                    UserTask ut = new UserTask(hti.getName(), hti.getAssignee() != null ? hti.getAssignee() + " " + getNombresUsuario(hti.getAssignee()) : (hti.getId() != null ? (usuariosCandidatos(hti.getId()) != null ? usuariosCandidatos(hti.getId()) : "") : ""),
                            hti.getCreateTime(), hti.getEndTime());
                    solicitudServicio.getUsersTasks().add(ut);
                }
            }
            ss.setData(solicitudServicio);
            ss.setUrlWebService(SisVars.ws + "reportes/solicitudServicio/consultaServicios");
            JsfUti.redirectFacesNewTab("/DocumentoWs");
        } catch (Exception e) {
            System.out.println("exception generarPDF " + e.getMessage());
        }
    }

    public void aprobarNotificacion() {
        if (listRequisitosErrores != null) {
            if (!listRequisitosErrores.isEmpty()) {
                for (RequisitosErrores req : listRequisitosErrores) {
                    if (req.getFaltante()) {
                        JsfUti.messageError(null, "Error", "Subir Documentos Faltantes.");
                    }
                    if (!req.getSubido()) {
                        JsfUti.messageError(null, "Error", "Actualizar todos los documentos para poder DAR POR REVISADO.");
                        return;
                    }
                }
            }
        }
        
    }

    @Override
    public void handleFileUpload(FileUploadEvent event) {
        try {
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
            documentoGuardar = crearSolicitudDocumento(f, event.getFile());
            SolicitudDocumentos reques = (SolicitudDocumentos) service.methodPOST(documentoGuardar, SisVars.ws + "solicitudDocumento/Ventanilla/guardar", SolicitudDocumentos.class);
            if (reques != null) {
                if (indexDocumento != null) {
                    try {
                        if (documento.getId() != null) {
                            documentos.remove(indexDocumento.intValue());
                            documentos.add(reques);
                        } else {
                            documentos.add(reques);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
                    }
                }
                JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");
            } else {
                JsfUti.messageError(null, "Error al subir el archivo", "");
            }
            JsfUti.executeJS("PF('DlgoDocumento').hide()");
        } catch (IOException e) {
            e.printStackTrace();
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }

    public SolicitudDocumentos crearSolicitudDocumento(File file, UploadedFile upLoadedFile) {
        SolicitudDocumentos solicitudDocumentos = new SolicitudDocumentos();
        if (documento != null && documento.getId() != null) {
            solicitudDocumentos.setId(documento.getId());
            solicitudDocumentos.setRequisito(documento.getRequisito());
//            System.out.println("id documento" + documento.getId());
        }
        if (documento.getRequisito() != null && documento.getRequisito().getId() != null) {
            solicitudDocumentos.setRequisito(new ServiciosDepartamentoRequisitos(documento.getRequisito().getId()));
        }
        solicitudDocumentos.setEstado("A");
        solicitudDocumentos.setSolicitud(solicitudServicio);
        solicitudDocumentos.setUsuario(session.getName_user());
        solicitudDocumentos.setFechaCreacion(new Date());
        solicitudDocumentos.setNombreArchivo(file.getName());
        solicitudDocumentos.setTipoArchivo(upLoadedFile.getContentType());
        solicitudDocumentos.setRutaArchivo(file.getAbsolutePath());
        solicitudDocumentos.setTieneFirmaElectronica(Boolean.FALSE);
        return solicitudDocumentos;
    }

    public void abrirDlgArchivos(SolicitudDocumentos solicitudDocumentos, int index) {
        if (solicitudDocumentos != null) {
            documento = solicitudDocumentos;
//            System.out.println("documento seleccionado " + documento.getRequisito().getDescripcion() == null ? documento.getRequisito().getNombre() : documento.getRequisito().getDescripcion());
            indexDocumento = index;
        } else {
            documento = new SolicitudDocumentos();
        }
        JsfUti.executeJS("PF('DlgoDocumento').show()");
        JsfUti.update("formDocumento");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public String getCiUser() {
        return ciUser;
    }

    public void setCiUser(String ciUser) {
        this.ciUser = ciUser;
    }

    public LazyModelWS<SolicitudServicios> getLazyVentanillaTodos() {
        return lazyVentanillaTodos;
    }

    public void setLazyVentanillaTodos(LazyModelWS<SolicitudServicios> lazyVentanillaTodos) {
        this.lazyVentanillaTodos = lazyVentanillaTodos;
    }

    public Boolean getServicioActivo() {
        return servicioActivo;
    }

    public void setServicioActivo(Boolean servicioActivo) {
        this.servicioActivo = servicioActivo;
    }

    public Boolean getNotificacionActiva() {
        return notificacionActiva;
    }

    public void setNotificacionActiva(Boolean notificacionActiva) {
        this.notificacionActiva = notificacionActiva;
    }

    public Boolean getRenderedView() {
        return renderedView;
    }

    public void setRenderedView(Boolean renderedView) {
        this.renderedView = renderedView;
    }

    public SolicitudDocumentos getDocumento() {
        return documento;
    }

    public void setDocumento(SolicitudDocumentos documento) {
        this.documento = documento;
    }

    public SolicitudDocumentos getDocumentoGuardar() {
        return documentoGuardar;
    }

    public void setDocumentoGuardar(SolicitudDocumentos documentoGuardar) {
        this.documentoGuardar = documentoGuardar;
    }

    public ArrayList<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public List<RequisitosErrores> getListRequisitosErrores() {
        return listRequisitosErrores;
    }

    public void setListRequisitosErrores(List<RequisitosErrores> listRequisitosErrores) {
        this.listRequisitosErrores = listRequisitosErrores;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    public List<ServiciosDepartamentoRequisitos> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<ServiciosDepartamentoRequisitos> requisitos) {
        this.requisitos = requisitos;
    }

    public SolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

    public List<SolicitudDepartamento> getSolicitudDepartamentos() {
        return solicitudDepartamentos;
    }

    public void setSolicitudDepartamentos(List<SolicitudDepartamento> solicitudDepartamentos) {
        this.solicitudDepartamentos = solicitudDepartamentos;
    }

    public List<ServiciosDepartamento> getServiciosDepartamentos() {
        return serviciosDepartamentos;
    }

    public void setServiciosDepartamentos(List<ServiciosDepartamento> serviciosDepartamentos) {
        this.serviciosDepartamentos = serviciosDepartamentos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<TipoTramite> getTiposTramites() {
        return tiposTramites;
    }

    public void setTiposTramites(List<TipoTramite> tiposTramites) {
        this.tiposTramites = tiposTramites;
    }
//</editor-fold>
}
