/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.RolesEspeciales;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.Notificacion;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.RequisitosErrores;
import org.bcbg.entities.Rol;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
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
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.bcbg.models.DatosReporte;
import org.bcbg.models.EventPrimefaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author OrigamiEC
 */
@Named
@ViewScoped
public class ConsultaServiciosMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<SolicitudServicios> lazyVentanilla, lazySolicitudesProceso, lazyMiVentanilla, lazyVentanillaTodos;
    private SolicitudServicios solicitudServicio;

    private Integer indexDocumento;
    private SolicitudDocumentos documento, documentoGuardar;
    private ArrayList<SolicitudDocumentos> documentos;
    private List<RequisitosErrores> listRequisitosErrores;
    private Notificacion notificacion;
    private List<ServiciosDepartamentoRequisitos> requisitos;

    private List<Departamento> departamentos;
    private List<SolicitudDepartamento> solicitudDepartamentos;
    private Departamento departamento;

    private Boolean renderedView;
    private Boolean servicioActivo, notificacionActiva;
    private List<Data> listData;
    private String ciUser;
    private StreamedContent imageProcessInstance;
    private List<ServiciosDepartamento> serviciosDepartamentos;
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    private List<Observaciones> observaciones = new ArrayList<>();
    private List<TipoTramite> tiposTramites = new ArrayList<>();

    private List<EventPrimefaces> events;
    private Date fechaActual;

    @PostConstruct
    public void init() {
        try {
            System.out.println("init");
            renderedView = Boolean.FALSE;
            servicioActivo = Boolean.FALSE;
            notificacionActiva = Boolean.FALSE;
            this.fechaActual = new Date();
            initServiciosDepartamentos();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadModel() {
        renderedView = Boolean.FALSE;
        solicitudServicio = new SolicitudServicios();
        documentos = new ArrayList<>();
        listRequisitosErrores = new ArrayList<>();
        solicitudDepartamentos = new ArrayList<>();
        serviciosDepartamentos = new ArrayList<>();
        if (departamento != null && departamento.getId() != null) {
            //SERVICIOS X PROCESOS DE VENTANILLA UNICA
            initSolicituVU(new SolicitudServicios(new ServiciosDepartamento(new Departamento(departamento.getId()))), Boolean.FALSE);
        } else {
            lazyVentanilla = null;
            lazySolicitudesProceso = null;
            JsfUti.messageError(null, "Debe seleccionar un departamento", "");
        }
    }

    public void getTitulosCreditosDeuda() {
        if (ciUser.equals("")) {
            JsfUti.messageError(null, "Agregue la identificación de la persona a consultar los Títulos créditos.", "");
            return;
        }
        listData = new ArrayList<>();
//        listData = appServices.getEmisionesGim(new Data("1102190343", solicitudServicio.getId()));
        JsfUti.executeJS("PF('dlgTituloCredito').show()");
        JsfUti.update("formTituloCredito");
    }

    public void tareaActiva(Integer numTramite) {
        List<TareasActivas> tareas = (List<TareasActivas>) appServices.getTareaActiva(numTramite);
        if (Utils.isNotEmpty(tareas)) {
            setServicioActivo(Boolean.TRUE);
        } else {
            setServicioActivo(Boolean.FALSE);
        }
    }

    private void initServiciosDepartamentos() {
        Rol rol;
        ciUser = "";
        departamentos = new ArrayList<>();
        departamento = new Departamento();
        try {

            if (session.getIsUserAdmin()) {
                departamentos = appServices.getListDepartamentos();
            } else {
                departamento = appServices.buscarDepartamento(new Departamento(session.getDepts().get(0)));
                departamentos.add(departamento);
            }
            System.out.println("departamento: " + departamento.getId());
        } catch (Exception e) {
            e.printStackTrace();
            JsfUti.messageError(null, "Su usuario no tiene asociado un departamento", "");
        }

        loadModel();
    }

    public String obtenerDepartamentoActual(Long SolicitudServicioId) {
        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento(new SolicitudServicios(SolicitudServicioId));
        solicitudDepartamentos = appServices.buscarSolicitudDepartamentos(solicitudDepartamento);
        return solicitudDepartamentos.get(solicitudDepartamentos.size() - 1).getDepartamento().getNombre();
    }

    public void detalleSolicitudServicio(SolicitudServicios servicio) {
        try {
            solicitudServicio = servicio;
            tareaActiva(servicio.getTramite().getNumTramite().intValue());
            tareas = this.getTaskByProcessInstanceIdMain(servicio.getTramite().getIdProceso());
            observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(servicio.getTramite().getId())));
            SolicitudDocumentos solicitudDocumentos = new SolicitudDocumentos(new SolicitudServicios(solicitudServicio.getId()));
            documentos = new ArrayList<>(appServices.buscarSolicitudDocumentos(solicitudDocumentos));

            getListRequisitosErrores(servicio);
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

            events = new ArrayList<>();

            if (Utils.isNotEmpty(observaciones)) {
                for (Observaciones obs : observaciones) {
                    events.add(new EventPrimefaces(obs.getTarea(), obs.getObservacion(), obs.getUserCre(), Utils.dateFormatPattern("yyyy-MM-dd HH:mm", obs.getFecCre()), "pi pi-user", "#FFFFF", ""));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFlujo() {
        InputStream diagram = engine.getProcessInstanceDiagram(solicitudServicio.getTramite().getIdProceso());
        if (diagram != null) {
            imageProcessInstance = DefaultStreamedContent.builder().stream(() -> diagram).build();
        }
    }

    private void getListRequisitosErrores(SolicitudServicios servicio) {
//        notificacion = appServices.findNotificacionNoRevisadaNoAprobada(new Data(servicio.getId()));
//        if (notificacion != null) {
//            listRequisitosErrores = appServices.findRequisitosByNotificacion(notificacion.getId());
////            System.out.println("tamaño " + listRequisitosErrores.size());
//            notificacionActiva = Boolean.TRUE;
//        } else {
//            notificacionActiva = Boolean.FALSE;
//        }
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

    public void showReporteTicket(SolicitudServicios solicitud) {
        try {
            String url;
            ss.borrarDatos();
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

//    public void enviarReimpresionTramite() {
//        try {
//            if (Utils.isNotEmptyString(solicitudServicio.getCorreoNotificacion())) {
//                Correo correo = new Correo();
//                correo.setAsunto("Trámite: " + solicitudServicio.getTipoServicio().getNombre());
//                correo.setDestinatario(solicitudServicio.getCorreoNotificacion());
//                correo.setMensaje(Utils.mailHtmlNotificacion("Trámite N° " + solicitudServicio.getTramite().getCodigo() + "-" + solicitudServicio.getTipoServicio().getNombre(),
//                        "<strong>¡Hola! </strong><br><strong>" + solicitudServicio.getEnteSolicitante().getNombresApellidos() + "</strong>"
//                        + " se le notifica el reingreso de sus documentos del Trámite con número: <strong>" + solicitudServicio.getTramite().getCodigo() + " </strong>",
//                        "<strong>Gracias por la Atención Brindada</strong><br>",
//                        "Este correo fue enviado de forma automática y no requiere respuesta."));
//                appServices.enviarCorreo(correo);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void generarPDF() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            ss.borrarDatos();
            ss.borrarParametros();

            solicitudServicio.setUsersTasks(new ArrayList<>());
            if (tareas != null && !tareas.isEmpty()) {
                for (Observaciones obs : observaciones) {
                    UserTask ut = new UserTask(obs.getTarea() + ": " + obs.getObservacion(), obs.getUserCre() != null ? (obs.getUserCre() + " " + getNombresUsuario(obs.getUserCre())) : "", obs.getFecCre(), obs.getFecCre());
                    solicitudServicio.getUsersTasks().add(ut);
                }
                /* for (HistoricTaskInstance hti : tareas) {
                    UserTask ut = new UserTask(hti.getName(), hti.getAssignee() != null ? hti.getAssignee() + " " + getNombresUsuario(hti.getAssignee()) : (hti.getId() != null ? (usuariosCandidatos(hti.getId()) != null ? usuariosCandidatos(hti.getId()) : "") : ""),
                            hti.getCreateTime(), hti.getEndTime());
                    solicitudServicio.getUsersTasks().add(ut);
                }*/
            }
            ss.setData(solicitudServicio);
            ss.setUrlWebService(SisVars.ws + "reportes/solicitudServicio/consultaServicios");
            JsfUti.redirectFacesNewTab("/DocumentoWs");
        } catch (Exception e) {
            System.out.println("exception generarPDF " + e.getMessage());
        }
    }

    public void onTabChange(TabChangeEvent event) {
        SolicitudServicios solicitud;
        switch (event.getTab().getId()) {
            case "tabVU"://SERVICIOS X PROCESOS DE VENTANILLA UNICA
                initSolicituVU(new SolicitudServicios(new ServiciosDepartamento(new Departamento(departamento.getId()))), Boolean.FALSE);
                break;
            case "tabTI":
                initSolicituVU(new SolicitudServicios(new User(session.getUserId())), Boolean.TRUE);
                break;
            case "tabPD"://SERVICIOS POR PROCESOS BPMN
                solicitud = new SolicitudServicios(new TipoTramite(new Departamento(departamento.getId())));
                lazySolicitudesProceso = new LazyModelWS<>(SisVars.ws + "solicitudServicios/mapper" + Utils.armarUrlCamposObj(solicitud, SolicitudServicios.class, null)
                        + "&sort=id,DESC", SolicitudServicios[].class, session.getToken());
                tiposTramites = new ArrayList<>();
                List<TipoTramite> tempTipo = appServices.getTipoTramites(new TipoTramite(new Departamento(departamento.getId())));
                if (Utils.isNotEmpty(tempTipo)) {
                    tiposTramites.addAll(tempTipo);
                }
                break;
            case "tabArchivo":
                lazyVentanillaTodos = new LazyModelWS<>(SisVars.ws + "solicitudServicios/mapper?sort=id,DESC", SolicitudServicios[].class, session.getToken());
                tiposTramites = new ArrayList<>();
                List<ServiciosDepartamento> temp = appServices.getListItems(new ServiciosDepartamento(null, null));
                if (Utils.isNotEmpty(temp)) {
                    serviciosDepartamentos.addAll(temp);
                }
                break;
        }
    }

    private void initSolicituVU(SolicitudServicios solicitud, Boolean misTramitesIniciados) {

        List<ServiciosDepartamento> temp = appServices.getListItems(new ServiciosDepartamento(new Departamento(departamento.getId())));
        if (Utils.isNotEmpty(temp)) {
            serviciosDepartamentos.addAll(temp);
        }
        if (misTramitesIniciados) {
            lazyMiVentanilla = new LazyModelWS<>(SisVars.ws + "solicitudServicios/mapper" + Utils.armarUrlCamposObj(solicitud, SolicitudServicios.class, null)
                    + "&sort=id,DESC&usuarioCreacion=" + session.getUserId(), SolicitudServicios[].class, session.getToken());
        } else {
            lazyVentanilla = new LazyModelWS<>(SisVars.ws + "solicitudServicios/mapper" + Utils.armarUrlCamposObj(solicitud, SolicitudServicios.class, null)
                    + "&sort=id,DESC", SolicitudServicios[].class, session.getToken());
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModelWS<SolicitudServicios> getLazyVentanillaTodos() {
        return lazyVentanillaTodos;
    }

    public void setLazyVentanillaTodos(LazyModelWS<SolicitudServicios> lazyVentanillaTodos) {
        this.lazyVentanillaTodos = lazyVentanillaTodos;
    }

    public LazyModelWS<SolicitudServicios> getLazyMiVentanilla() {
        return lazyMiVentanilla;
    }

    public void setLazyMiVentanilla(LazyModelWS<SolicitudServicios> lazyMiVentanilla) {
        this.lazyMiVentanilla = lazyMiVentanilla;
    }

    public List<TipoTramite> getTiposTramites() {
        return tiposTramites;
    }

    public void setTiposTramites(List<TipoTramite> tiposTramites) {
        this.tiposTramites = tiposTramites;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public List<ServiciosDepartamento> getServiciosDepartamentos() {
        return serviciosDepartamentos;
    }

    public void setServiciosDepartamentos(List<ServiciosDepartamento> serviciosDepartamentos) {
        this.serviciosDepartamentos = serviciosDepartamentos;
    }

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

    public LazyModelWS<SolicitudServicios> getLazyVentanilla() {
        return lazyVentanilla;
    }

    public void setLazyVentanilla(LazyModelWS<SolicitudServicios> lazyVentanilla) {
        this.lazyVentanilla = lazyVentanilla;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getRenderedView() {
        return renderedView;
    }

    public void setRenderedView(Boolean renderedView) {
        this.renderedView = renderedView;
    }

    public SolicitudServicios getSolicitudServicio() {
        return solicitudServicio;
    }

    public void setSolicitudServicio(SolicitudServicios solicitudServicio) {
        this.solicitudServicio = solicitudServicio;
    }

    public ArrayList<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public List<SolicitudDepartamento> getSolicitudDepartamentos() {
        return solicitudDepartamentos;
    }

    public void setSolicitudDepartamentos(List<SolicitudDepartamento> solicitudDepartamentos) {
        this.solicitudDepartamentos = solicitudDepartamentos;
    }

    public Boolean getServicioActivo() {
        return servicioActivo;
    }

    public void setServicioActivo(Boolean servicioActivo) {
        this.servicioActivo = servicioActivo;
    }

    public LazyModelWS<SolicitudServicios> getLazySolicitudesProceso() {
        return lazySolicitudesProceso;
    }

    public void setLazySolicitudesProceso(LazyModelWS<SolicitudServicios> lazySolicitudesProceso) {
        this.lazySolicitudesProceso = lazySolicitudesProceso;
    }

    public SolicitudDocumentos getDocumento() {
        return documento;
    }

    public void setDocumento(SolicitudDocumentos documento) {
        this.documento = documento;
    }

    public List<ServiciosDepartamentoRequisitos> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<ServiciosDepartamentoRequisitos> requisitos) {
        this.requisitos = requisitos;
    }

    public List<RequisitosErrores> getListRequisitosErrores() {
        return listRequisitosErrores;
    }

    public void setListRequisitosErrores(List<RequisitosErrores> listRequisitosErrores) {
        this.listRequisitosErrores = listRequisitosErrores;
    }

    public Boolean getNotificacionActiva() {
        return notificacionActiva;
    }

    public void setNotificacionActiva(Boolean notificacionActiva) {
        this.notificacionActiva = notificacionActiva;
    }

    public List<Data> getListData() {
        return listData;
    }

    public void setListData(List<Data> listData) {
        this.listData = listData;
    }

    public String getCiUser() {
        return ciUser;
    }

    public void setCiUser(String ciUser) {
        this.ciUser = ciUser;
    }

    public List<EventPrimefaces> getEvents() {
        return events;
    }

    public void setEvents(List<EventPrimefaces> events) {
        this.events = events;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

//</editor-fold>
}
