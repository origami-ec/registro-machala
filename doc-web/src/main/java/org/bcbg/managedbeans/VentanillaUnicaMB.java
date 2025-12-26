/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.UsuarioDocs;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
import org.bcbg.entities.UsuarioResponsable;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.Correo;
import org.bcbg.models.CorreoArchivo;
import org.bcbg.models.EventPrimefaces;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class VentanillaUnicaMB extends BpmManageBeanBaseRoot implements Serializable {

    private HistoricoTramites historicoTramites;
    private SolicitudServicios solicitudServicios;
    /*  ESTE ES EL DEPARTAMENTO ULTIMO INGRESADO A QUIEN LE TOCO LA TAREA
        HABRAN MAS DE DOS DEPARTAMENTOS CUANDO EXISTAN VARIAS REASIGNACIONES
     */
    private SolicitudDepartamento solicitudDepartamento;

    private SolicitudDepartamento solicitudDepartamentoDevolucionFinal;
    /*  ESTE ES EL LISTADO DE SOLICITUDES DEPARTAMENTOS POR TRÁMITE
        TIENE COMO OBJETIVO BUSCAR EN TODA LA LISTA ALGUNO QUE TENGA
        LA OBSERVACIÓN "DEVOLUCION DE TRAMITE POR" PARA RESOLVER LAS DEVOLUCIONES
        DE TRAMITE A DIFERENTES ÁREAS
     */
    private List<SolicitudDepartamento> solicitudesDepartamentos;
    private List<SolicitudDepartamento> solicitudesDepartamentosUsuarios;
    private Boolean devolucionTramite;
    private List<Departamento> departamentos, departamentosRechazo;
    private Departamento departamento;
    private String observacionReasignacion;
    private String observacionAprobar;

    private List<User> userList, usuariosSeleccionados, userListRechazo, userListDevolucion,
            usuariosSeleccionadosRechazo, usuariosSeleccionadosTemp, usuariosSeleccionadosTempRechazo;
    private User usuarioSeleccionado;
    private List<User> listRolUsuario;

    private List<SolicitudDocumentos> documentos, documentosNuevos;
    private List<Observaciones> observaciones;
    private List<Departamento> departamentosSolicitud;
    private File file;
    private UploadedFile uploadedFile;
    private Observaciones ultimaObservacion;
    private Boolean responsable;
    private TreeNode selectedNode;
    private TreeNode treeDepartamento;
    private List<EventPrimefaces> events;
    private SolicitudDocumentos documento;
    private List<ServiciosDepartamentoRequisitos> requisitos;
    private String urls, iframeUrl;
    private List<Indexacion> indices;
    private Indexacion indice;
    private String txtAdicional;
    private Boolean tipoDevolucion;

    @PostConstruct
    public void init() {
        if (!JsfUti.isAjaxRequest()) {
            loadModel();
        }
    }

    private void loadModel() {
        if (session.getTaskID() != null) {
            System.out.println("usuario actual:" + session.getName_user());
            User usuarioRechazo;
            tipoDevolucion = true;
            userListRechazo = new ArrayList<>();
            departamentosRechazo = new ArrayList<>();
            userListDevolucion = new ArrayList<>();
            solicitudesDepartamentosUsuarios = new ArrayList<>();
            usuariosSeleccionadosTemp = new ArrayList<>();
            usuariosSeleccionadosTempRechazo = new ArrayList<>();
            responsable = Boolean.TRUE;
            devolucionTramite = Boolean.FALSE;
            this.setTaskId(session.getTaskID());
            if (session.getNumTramite() != null) {
                try {
                    TareasActivas ta = appServices.getTareaActiva(session.getNumTramite());
                    if (ta != null) {
                        if (!ta.getCandidate().toLowerCase().trim().contains(session.getName_user().toLowerCase().trim())) {
                            session.setCodigoTramite(ta.getCodigo());
                            this.tareaResuelta();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                usuarioSeleccionado = new User();
                observacionReasignacion = "";
                departamentos = appServices.getListDepartamentosHijos();

                if (Utils.isNotEmpty(session.getDepts())) {
                    for (Departamento dep : departamentos) {
                        if (dep.getId().equals(session.getDepts().get(0))) {
                            departamento = dep;
                            break;
                        }
                    }
                }
                initServiciosDepartamentos();
                solicitudServicios = appServices.buscarSolicitudServicios(new SolicitudServicios(new HistoricoTramites(null, session.getNumTramite())));
                solicitudesDepartamentos = solicitudServicios.getSolicitudDepartamentos();
                for (SolicitudDepartamento solicitud : solicitudesDepartamentos) {
                    if (solicitud.getObservacion() != null && solicitud.getObservacion().contains("DEVOLUCION DE TRAMITE POR")) {
                        /*Variable que verifica si hay solicitudes departamento en estado de devolucion de tramite*/
                        devolucionTramite = Boolean.TRUE;
                        if (!session.getName_user().trim().equalsIgnoreCase(solicitud.getResponsables().trim())) {
                            solicitudesDepartamentosUsuarios.add(solicitud);
                        }
                        /*Si en la observacion de la solicitud departamento
                                termina con el nombre del mismo usuario que esta logueado*/
                        if (solicitud.getObservacion().contains(session.getName_user())) {
                            solicitudDepartamento = solicitud;
                        }
                        /*Si aun hay usuarios resolviendo la devolución del trámite*/
                        if (solicitudesDepartamentosUsuarios.size() < 0) {
                            /*Si ya todos resolvieron la devolución del tramite se obtiene la solicitud del departamento que devolvio el
                                trámite y la última devolución que se esta resolviendo*/
                            solicitudDepartamentoDevolucionFinal = solicitud;
                            solicitudDepartamento = solicitudServicios.getSolicitudDepartamentos().get(0);
                        }
                    }
                }
                if (solicitudDepartamento != null) {
                    System.out.println("solicitud departamento a actualizar:" + solicitudDepartamento.toString());
                }
                loadDepartamentos();
                documentos = appServices.buscarSolicitudDocumentos(new SolicitudDocumentos(new SolicitudServicios(solicitudServicios.getId())));
                documentosNuevos = new ArrayList<>();
                solicitudServicios.setDocumentos(documentos);
                historicoTramites = solicitudServicios.getTramite();
                observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(historicoTramites.getId())));
                if (Utils.isNotEmpty(observaciones)) {
                    events = new ArrayList<>();
                    ultimaObservacion = observaciones.get(observaciones.size() - 1);
                    for (Observaciones obs : observaciones) {
                        events.add(new EventPrimefaces(obs.getTarea(), obs.getObservacion(), obs.getUserCre(), Utils.dateFormatPattern("yyyy-MM-dd HH:mm", obs.getFecCre()), "pi pi-user", "#FFFFF", ""));
                    }
                }
                /*Si el trámite esta en estado de devolución*/
                if (devolucionTramite) {
                    if (!solicitudesDepartamentosUsuarios.isEmpty()) {
                        for (SolicitudDepartamento solicitudUsuario : solicitudesDepartamentosUsuarios) {
                            usuarioRechazo = appServices.getUsuario(new User(solicitudUsuario.getResponsables()));
                            System.out.println("usuario rechazo:" + usuarioRechazo);
                            if (!session.getName_user().equalsIgnoreCase(usuarioRechazo.getUsuarioNombre())) {
                                userListDevolucion.add(usuarioRechazo);
                            }
                            userListDevolucion = userListDevolucion.stream().distinct().collect(Collectors.toList());
                        }
                    } else {
                        usuarioRechazo = appServices.getUsuario(new User(solicitudDepartamento.getUsuarioIngreso()));
                        userListDevolucion.add(usuarioRechazo);
                    }
                } else {
                    /*Events listado de los usuarios que han intervenido en el trámite*/
                    for (EventPrimefaces event : events) {
                        usuarioRechazo = appServices.getUsuario(new User(event.getUsers()));
                        if (!session.getName_user().equalsIgnoreCase(usuarioRechazo.getUsuarioNombre())) {
                            userListRechazo.add(usuarioRechazo);
                        }
                        userListRechazo = userListRechazo.stream().distinct().collect(Collectors.toList());
                    }
                    solicitudDepartamento = solicitudServicios.getSolicitudDepartamentos().get(0);
                    for (Departamento depto : departamentos) {
                        for (User usuario : userListRechazo) {
                            if (depto.getId().equals(usuario.getDepartamentoId())) {
                                departamentosRechazo.add(depto);
                            }

                        }
                    }
                }
                usuariosSeleccionados = new ArrayList<>();
                indices = documentalService.getIndices();
            } else {
                this.continuar();
            }
        } else {
            this.continuar();
        }
    }

    private void loadDepartamentos() {
        departamentosSolicitud = new ArrayList<>();
        for (SolicitudDepartamento sd : solicitudServicios.getSolicitudDepartamentos()) {
            if (!departamentosSolicitud.contains(sd.getDepartamento())) {
                departamentosSolicitud.add(sd.getDepartamento());
            }
        }
    }

    public void initListUsuarios() {
        if (selectedNode != null) {
            departamento = (Departamento) selectedNode.getData();
//            userList = appServices.getUsuariosXDepts(new RolUsuario(responsable, null, new User(Boolean.TRUE), new Rol(new Departamento(departamento.getId())), Boolean.TRUE));
            JsfUti.executeJS("PF('dtUsuarios').clearFilters()");
        }
    }

    public void initServiciosDepartamentos() {
        try {

            if (departamento != null && departamento.getId() != null) {

                // getUserSeleccionados(usuariosSeleccionados, usuariosSeleccionadosTemp);
                getUserSeleccionados(usuariosSeleccionadosRechazo, usuariosSeleccionadosTempRechazo);
                System.out.println("usuarios seleccionados temo:"+usuariosSeleccionadosTemp.size());
                System.out.println("usuarios seleccionados temo rechazo:"+usuariosSeleccionadosTempRechazo.size());
                userList = appServices.getUserXDepts(departamento.getId());
            }
        } catch (Exception e) {
            System.out.println("//Exception initServiciosDepartamento: " + e.getMessage());
        }
    }

    public void getUserSeleccionados(List<User> usuariosSeleccionados, List<User> usuariosSeleccionadosTemp) {
        if (Utils.isNotEmpty(usuariosSeleccionados)) {
            usuariosSeleccionadosTemp.addAll(usuariosSeleccionados);
            usuariosSeleccionadosTemp = usuariosSeleccionadosTemp.stream().distinct().collect(Collectors.toList());
        }
        if (Utils.isNotEmpty(usuariosSeleccionadosTemp)) {
            for (User usuarioSelect : usuariosSeleccionadosTemp) {
                if (usuarioSelect.getDepartamentoId().equals(departamento.getId())   )  {
                    usuariosSeleccionados.add(usuarioSelect);
                }
            }
        }
    }

    public void validarReasignar() {
        if (usuarioSeleccionado != null && usuarioSeleccionado.getId() != null) {
            JsfUti.update("frmReasignar");
            JsfUti.executeJS("PF('reasignarDlg').show()");
        } else {
            JsfUti.messageError(null, "Debe asignar a un usuario.", "");
        }
    }

    public void completarTareaAsignarAtenderSolicitud(Integer tipo) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            Integer asignar = 0;
            Correo correo = new Correo();
            String usuarioAsignado = "";
            correo.setAsunto("Trámite: " + solicitudServicios.getTipoServicio().getNombre());
            correo.setNumeroTramite(historicoTramites.getCodigo());
            correo.setRemitente(session.getUsuarioDocs());
            String mensaje = "";
            Boolean enviarCorreo = Boolean.TRUE;
            switch (tipo) {
                case 1://ATENDER
                    if (solicitudServicios.getInforme() == null || solicitudServicios.getInforme().isEmpty()) {
                        JsfUti.messageError(null, "Ingrese una Respuesta/Informe", "");
                        return;
                    }
                    //¿Require validación del trámite?
                    if (solicitudServicios.getTipoServicio().getValidar()) {
                        //ENVIAR A LA TAREA DE VALIDAR
                        asignar = 3;
                        //BUSCA EL USUARIO VALIDADOR
                        if (Utils.isNotEmptyString(solicitudServicios.getCorreoNotificacion())) {
                            correo.setDestinatario(solicitudServicios.getCorreoNotificacion());
                            correo.setDestino(new UsuarioDocs(solicitudServicios.getUsuarioIngreso().getId(), solicitudServicios.getUsuarioIngreso().getNombreUsuario(), solicitudServicios.getUsuarioIngreso().getUsuarioNombre()));
                            mensaje = Utils.mailHtmlNotificacion("Trámite: " + solicitudServicios.getTipoServicio().getNombre(),
                                    "¡Hola! " + solicitudServicios.getEnteSolicitante().getNombreCompleto()
                                    + " se le notifica que el trámite N° " + historicoTramites.getCodigo()
                                    + " se encuentra en validación de solicitud, por favor mantengase atento a su bandeja de correo electrónico.",
                                    "Gracias por la Atención Brindada",
                                    "Este correo fue enviado de forma automática y no requiere respuesta.");
                        }
                        appServices.guardarObservacionHT(historicoTramites, this.getTaskDataByTaskID().getName(), solicitudServicios.getInforme());
                    } else {
                        //FINALIZA EL TRÁMITE
                        asignar = 1;
                        if (Utils.isNotEmptyString(solicitudServicios.getCorreoNotificacion())) {
                            correo.setDestinatario(solicitudServicios.getCorreoNotificacion());
                            correo.setRemitente(session.getUsuarioDocs());
                            correo.setDestino(new UsuarioDocs(solicitudServicios.getUsuarioIngreso().getId(), solicitudServicios.getUsuarioIngreso().getNombreUsuario(), solicitudServicios.getUsuarioIngreso().getUsuarioNombre()));
                            mensaje = Utils.mailHtmlNotificacion("Trámite: " + solicitudServicios.getTipoServicio().getNombre(),
                                    "¡Hola! " + solicitudServicios.getEnteSolicitante().getNombres()
                                    + " se le notifica la finalización del trámite N° " + historicoTramites.getCodigo(),
                                    "Gracias por la Atención Brindada",
                                    "Este correo fue enviado de forma automática y no requiere respuesta.");
                        }
                        solicitudServicios.setFinalizado(Boolean.TRUE);
                        appServices.actualizarSolicitudServicios(solicitudServicios);
                        appServices.guardarObservacionHT(historicoTramites, SisVars.finalizaSolicitud, solicitudServicios.getInforme());
                    }
                    break;
                case 2://REASIGNAR
                    if (observacionReasignacion == null || observacionReasignacion.isEmpty()) {
                        JsfUti.messageError(null, "Ingrese una observación", "");
                        return;
                    }

                    if (Utils.isEmpty(usuariosSeleccionados)) {
                        JsfUti.messageError(null, "Debe reasignar al menos a un usuario al trámite", "");
                        return;
                    }
                    for (User ur : usuariosSeleccionados) {
                        if (!ur.getDisponible()) {
                            JsfUti.messageError(null, ur.getEstadoDisponible(), "");
                            return;
                        }

                    }

                    asignar = 2;
                    // usuariosSeleccionados = usuariosSeleccionadosTemp;
                    usuarioAsignado = appServices.getCandidateUserByList(usuariosSeleccionados);
                    System.out.println("usuarios asignados:" + usuarioAsignado);
                    params.put("asignarUsuario", usuarioAsignado);
                    observacionReasignacion = observacionReasignacion + "\n Usuario asignado: " + usuarioAsignado;
                    enviarCorreo = Boolean.FALSE;
                    appServices.guardarObservacionHT(historicoTramites, SisVars.reasignacionSolicitud, observacionReasignacion);
                    break;
                case 3://RECHAZAR
                    if (observacionReasignacion == null || observacionReasignacion.isEmpty()) {
                        JsfUti.messageError(null, "Ingrese una observación", "");
                        return;
                    }

                    if (Utils.isEmpty(usuariosSeleccionados) && Utils.isEmpty(usuariosSeleccionadosRechazo)) {
                        JsfUti.messageError(null, "Debe reasignar al menos a un usuario al trámite", "");
                        return;
                    }
                    for (User ur : usuariosSeleccionados) {
                        if (!ur.getDisponible()) {
                            JsfUti.messageError(null, ur.getEstadoDisponible(), "");
                            return;
                        }

                    }
//                    if (tipoDevolucion == null) {
//                        JsfUti.messageError(null, "Debe seleccionar un tipo de devolución", "");
//                        return;
//                    }

                    //LA TAREA SE ASIGNA A LA PERSONA QUE INICIO EL TRÁMITE
                    asignar = 2;
                    usuariosSeleccionadosRechazo = usuariosSeleccionadosTempRechazo;
                    if (!usuariosSeleccionadosRechazo.isEmpty()) {
                        if (!usuariosSeleccionados.isEmpty()) {
                            usuariosSeleccionados.addAll(usuariosSeleccionadosRechazo);
                            usuariosSeleccionados = usuariosSeleccionados.stream().distinct().collect(Collectors.toList());
                        } else {
                            usuariosSeleccionados = usuariosSeleccionadosRechazo.stream().distinct().collect(Collectors.toList());
                        }
                    }
                    usuarioAsignado = appServices.getCandidateUserByList(usuariosSeleccionados);
                    System.out.println("usuario asignado activity:" + usuarioAsignado);
                    params.put("asignarUsuario", usuarioAsignado);
                    observacionReasignacion = observacionReasignacion + "\n Usuario asignado: " + usuarioAsignado;
                    enviarCorreo = Boolean.FALSE;
                    appServices.guardarObservacionHT(historicoTramites, SisVars.rechazoSolicitud, observacionReasignacion);
                    break;
                //RESOLUCIÓN DE DEVOLUCIONES DE TRÁMITES
                case 4:
                    if (observacionReasignacion == null || observacionReasignacion.isEmpty()) {
                        JsfUti.messageError(null, "Ingrese una observación", "");
                        return;
                    }
                    if (solicitudDepartamento.getObservacion().contains("INDIVIDUALMENTE")) {
                        if (!userListDevolucion.isEmpty()) {
                            for (int i = 0; i < userListDevolucion.size(); i++) {
                                /*Comparar el departamento actual de la persona que esta resolviendo el trámite
                                con el departamento de la lista de usuario quienes tienen que resolver la devolución
                                del trámite*/
                                if (session.getDepts().get(0).equals(userListDevolucion.get(i).getDepartamentoId())) {
                                    System.out.println("usuario devolucion: " + userListDevolucion.get(i).getRecursoHumano().getPersona().getNombres());
                                    /*En caso de encontrar coincidencias se procede a deshabilitar las solicitudes departamento 
                                    de los usuarios encontrados.*/
                                    if (!solicitudesDepartamentosUsuarios.isEmpty()) {
                                        for (SolicitudDepartamento solicitudesDisabled : solicitudesDepartamentosUsuarios) {
                                            if (solicitudesDisabled.getResponsables().trim().equalsIgnoreCase(userListDevolucion.get(i).getUsuarioNombre())) {
                                                System.out.println("solicitudes disabled:" + solicitudesDisabled.toString());
                                                SolicitudDepartamento soli;
                                                soli = solicitudesDisabled;
                                                soli.setEstado(Boolean.FALSE);
                                                soli.setFecha(new Date());
                                                appServices.actualizarSolicitudDepartamento(soli);
                                            }
                                        }
                                    }
                                    userListDevolucion.remove(i--);
                                }
                            }
                            usuarioAsignado = appServices.getCandidateUserByList(userListDevolucion);
                        }
                        System.out.println("usuario asignado activity:" + usuarioAsignado);
                        params.put("asignarUsuario", usuarioAsignado);
                    } else {
                        usuarioAsignado = solicitudDepartamento.getUsuarioIngreso();
                        System.out.println("usuario asignado activity:" + usuarioAsignado);
                        params.put("asignarUsuario", usuarioAsignado);
                    }

                    asignar = 2;
                    observacionReasignacion = observacionReasignacion + "\n Usuario asignado: " + usuarioAsignado;
                    enviarCorreo = Boolean.FALSE;
                    appServices.guardarObservacionHT(historicoTramites, SisVars.reasignacionSolicitud, observacionReasignacion);

                    break;
            }
            if (asignar == 0) {
                JsfUti.messageError(null, "Error de la aplicación, intente nuevamente", "");
                return;
            }
            params.put("asignar", asignar);
            if (actualizarSolicitud(usuarioAsignado, tipo)) {
                if (Utils.isNotEmptyString(correo.getDestinatario()) && enviarCorreo) {
                    correo.setMensaje(mensaje);
                    appServices.enviarCorreo(correo);
                }
                if (!enviarCorreo) {
                    for (User ur : usuariosSeleccionados) {
                        if (ur.getRecursoHumano() != null && ur.getRecursoHumano().getPersona() != null && Utils.isNotEmptyString(ur.getRecursoHumano().getPersona().getCorreo())) {
                            correo.setDestinatario(ur.getRecursoHumano().getPersona().getCorreo());
                            correo.setRemitente(session.getUsuarioDocs());
                            correo.setDestino(new UsuarioDocs(ur.getId(), ur.getNombreUsuario(), ur.getUsuarioNombre()));
                            if (tipo == 2) {
                                mensaje = Utils.mailHtmlNotificacion("Trámite: " + solicitudServicios.getTipoServicio().getNombre(),
                                        "¡Hola! " + usuarioSeleccionado.getRecursoHumano().getPersona().getDetalleNombre()
                                        + " se le notifica la asignación del trámite N° " + historicoTramites.getCodigo()
                                        + (solicitudServicios.getFechaMaximaRespuesta() != null
                                        ? " <strong>con fecha máxima de respuesta: </strong> " + new SimpleDateFormat("dd-MM-yyyy").format(solicitudServicios.getFechaMaximaRespuesta()) : ""),
                                        "Gracias por la Atención Brindada",
                                        "Este correo fue enviado de forma automática y no requiere respuesta.");
                            } else {
                                mensaje = Utils.mailHtmlNotificacion("Trámite: " + solicitudServicios.getTipoServicio().getNombre(),
                                        "¡Hola! " + solicitudServicios.getUsuarioIngreso().getRecursoHumano().getPersona().getDetalleNombre()
                                        + " se le notifica que el trámite N° " + historicoTramites.getCodigo() + "presenta novedades"
                                        + "<br><strong>Observacion:   </strong>" + txtAdicional,
                                        "Gracias por la Atención Brindada",
                                        "Este correo fue enviado de forma automática y no requiere respuesta.");
                            }
                            correo.setMensaje(mensaje);
                            appServices.enviarCorreo(correo);
                        }
                    }

                }
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), params);
                this.continuar();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaValidarSolicitud(Boolean aprobar) {
        try {
            HashMap<String, Object> par = new HashMap<>();
            if (aprobar) {
                if (Utils.isEmptyString(observacionAprobar)) {
                    JsfUti.messageError(null, "Debe ingresar una observación", "");
                    return;
                }
                solicitudServicios.setFinalizado(Boolean.TRUE);
                appServices.actualizarSolicitudServicios(solicitudServicios);
                appServices.guardarObservacionHT(historicoTramites, SisVars.finalizaSolicitud, observacionAprobar);
                par.put("aprobado", 0);
                enviarCorrreo();

            } else {
                if (Utils.isEmptyString(observacionReasignacion)) {
                    JsfUti.messageError(null, "Debe ingresar una observación", "");
                    return;
                }
                par.put("aprobado", 1);
                appServices.guardarObservacionHT(historicoTramites, SisVars.rechazoSolicitud, observacionReasignacion);
            }
            actualizarDocumentos();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void enviarCorrreo() {
        if (Utils.isNotEmptyString(solicitudServicios.getCorreoNotificacion())) {
            Correo correo = new Correo();
            correo.setAsunto("Trámite: " + solicitudServicios.getTipoServicio().getNombre());
            correo.setDestinatario(solicitudServicios.getCorreoNotificacion());
            correo.setMensaje(Utils.mailHtmlFinTramite(historicoTramites, solicitudServicios.getEnteSolicitante().getNombresApellidos()));
            appServices.enviarCorreo(correo);
        }
    }

    private Boolean actualizarSolicitud(String usuarioAsignado, Integer tipo) {
        solicitudDepartamento.setUsuarioIngreso(session.getName_user());
        if (!devolucionTramite) {
            solicitudDepartamento.setObservacion(solicitudServicios.getDescripcionInconveniente());
        } else if (devolucionTramite && solicitudDepartamentoDevolucionFinal == null) {
            solicitudDepartamento.setEstado(Boolean.FALSE);
        }
        solicitudDepartamento.setFecha(new Date());
        System.out.println("usuario asignado db:" + usuarioAsignado);
        solicitudDepartamento.setResponsables(usuarioAsignado);

        SolicitudServicios solicitudREST = solicitudServicios;
        solicitudREST.setDocumentos(null);
        solicitudREST.setSolicitudDepartamentos(null);
        if (appServices.actualizarSolicitudServicios(solicitudREST) != null) {
            System.out.println("entro solicitud rest != null");
            if (appServices.actualizarSolicitudDepartamento(solicitudDepartamento) != null) {
                System.out.println("entro actualizar solicitud departamento != null");
                switch (tipo) {
                    case 2://ASIGNAR
                        solicitudDepartamento = new SolicitudDepartamento();
                        solicitudDepartamento.setEstado(Boolean.TRUE);
                        solicitudDepartamento.setFecha(new Date());
                        solicitudDepartamento.setDepartamento(departamento);
                        solicitudDepartamento.setSolicitud(solicitudREST);
                        solicitudDepartamento.setResponsables(usuarioAsignado);
                        solicitudDepartamento.setUsuarioIngreso(session.getName_user());
                        appServices.guardarSolicitudDepartamento(solicitudDepartamento);
                        break;
                    case 3://REACHAZAR
                        for (User usuario : usuariosSeleccionados) {
                            String tipoDevolucionString;
                            if (tipoDevolucion) {
                                tipoDevolucionString = ",INDIVIDUALMENTE";
                            } else {
                                tipoDevolucionString = ",CUALQUIERA";
                            }

                            System.out.println("entro creacion solicitud departamento");
                            //SOLICITUD PARA RESOLVER LAS DEVOLUCIONES DE LOS TRÁMITES
                            SolicitudDepartamento solicitudDepartamentoDevolucion = new SolicitudDepartamento();
                            solicitudDepartamentoDevolucion.setUsuarioIngreso(session.getName_user());
                            solicitudDepartamentoDevolucion.setObservacion("DEVOLUCION DE TRAMITE POR," + usuario.getUsuarioNombre().trim() + tipoDevolucionString);
                            solicitudDepartamentoDevolucion.setFecha(new Date());
                            solicitudDepartamentoDevolucion.setResponsables(usuario.getUsuarioNombre());
                            solicitudDepartamentoDevolucion.setDepartamento(departamento);
                            solicitudDepartamentoDevolucion.setEstado(Boolean.TRUE);
                            solicitudDepartamentoDevolucion.setSolicitud(solicitudREST);
                            appServices.guardarSolicitudDepartamento(solicitudDepartamentoDevolucion);
                        }
                        break;
                    //RESOLVER DEVOLUCIÓN DE TRÁMITE 
                    case 4:
                        System.out.println("entro caso 4");
                        if (solicitudDepartamentoDevolucionFinal != null) {
                            /*Cuando ya todos los usuarios han  resuelto la devolución del tramite
                            se actualiza la solicitud departamento del ultimo usuario que resolvio*/
                            solicitudDepartamentoDevolucionFinal.setUsuarioIngreso(session.getName_user());
                            solicitudDepartamentoDevolucionFinal.setEstado(Boolean.FALSE);
                            solicitudDepartamentoDevolucionFinal.setFecha(new Date());
                            solicitudDepartamentoDevolucionFinal.setResponsables(usuarioAsignado);
                            appServices.actualizarSolicitudDepartamento(solicitudDepartamentoDevolucionFinal);
                        }
                        if (solicitudDepartamento.getObservacion().contains("CUALQUIERA")) {
                            System.out.println("entro cualquiera");
                            for (SolicitudDepartamento solicitud : solicitudesDepartamentosUsuarios) {
                                solicitud.setUsuarioIngreso(session.getName_user());
                                solicitud.setEstado(Boolean.FALSE);
                                solicitud.setFecha(new Date());
                                solicitud.setResponsables(usuarioAsignado);
                                appServices.actualizarSolicitudDepartamento(solicitud);
                            }

                        }
                        break;
                }
                return actualizarDocumentos();
            } else {
                JsfUti.messageError(null, Messages.error, "");
                return Boolean.FALSE;
            }
        } else {
            JsfUti.messageError(null, Messages.error, "");
            return Boolean.FALSE;
        }
    }

    public Boolean actualizarDocumentos() {
        if (Utils.isNotEmpty(documentosNuevos)) {
            documentosNuevos.forEach(docs -> {
                docs.setSolicitud(new SolicitudServicios(solicitudServicios.getId()));
            });
            if (Utils.isNotEmpty(appServices.actualizarSolicitudDocumentos(documentosNuevos))) {
                return Boolean.TRUE;
            } else {
                JsfUti.messageError(null, Messages.error, "");
                return Boolean.FALSE;
            }
        } else {
            return Boolean.TRUE;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (indice != null) {

                for (IndexacionCampo ic : indice.getCampos()) {
                    if (ic.getObligatorio()) {
                        if (Utils.isEmptyString(ic.getDetalle())) {
                            JsfUti.messageError(null, Messages.camposObligatorios, "");
                            return;
                        }
                    }
                }

                uploadedFile = event.getFile();
                file = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
                JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");

                ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
                archivoIndex.setTramite(solicitudServicios.getTipoServicio().getNombre());
                archivoIndex.setNumTramite(historicoTramites.getCodigo());
                archivoIndex.setEstado(Boolean.TRUE);

                if (documento.getRequisito() != null) {
                    if (Utils.isNotEmptyString(documento.getDescripcion())) {
                        archivoIndex.setDetalleDocumento(documento.getDescripcion() + " " + documento.getRequisito().getNombre());
                    } else {
                        archivoIndex.setDetalleDocumento(documento.getRequisito().getNombre());
                    }
                } else if (Utils.isNotEmptyString(documento.getDescripcion())) {
                    archivoIndex.setDetalleDocumento(documento.getDescripcion());
                } else {
                    archivoIndex.setDetalleDocumento(solicitudServicios.getTipoServicio().getNombre());
                }

                archivoIndex.setTipoIndexacion(indice.getDescripcion());
                List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
                for (IndexacionCampo ic : indice.getCampos()) {
                    detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
                }
                archivoIndex.setDetalles(detalles);
                ArchivoDocs docs = documentalService.guardarArchivo(uploadedFile, archivoIndex);
                if (docs != null) {
                    documento.setCodigoVerificacion(docs.getId());
                }
            } else {
                JsfUti.messageError(null, "Debe escoger el tipo de índexación", "");
            }

        } catch (Exception e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }

    public void agregarDocumento(Boolean tieneFirma, String archivo) {
        try {
            if (file != null && uploadedFile != null && documento != null) {
//            SolicitudDocumentos documento = new SolicitudDocumentos();
                documento.setEstado("A");
                documento.setFechaCreacion(new Date());
                documento.setNombreArchivo(uploadedFile.getFileName());
                documento.setTipoArchivo(uploadedFile.getContentType());
                documento.setTieneFirmaElectronica(tieneFirma);
                documento.setRutaArchivo(!archivo.isEmpty() ? archivo : file.getAbsolutePath());
                documento.setUsuario(session.getName_user());
                documento.setSolicitud(new SolicitudServicios(solicitudServicios.getId()));
                documentosNuevos.add(documento);

                JsfUti.executeJS("PF('DlgoDocumento').hide()");
                file = null;
                uploadedFile = null;
            } else {
                JsfUti.messageError(null, "Debe subir un documento para continuar y escribir una despcripción", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarFirmarDocumento(Boolean imagenPDF) {
        try {
            if (file != null && uploadedFile != null) {
                if (session.getFirmaElectronica() != null) {
                    FirmaElectronica fe = session.getFirmaElectronica();
                    fe.setArchivoFirmar(file.getAbsolutePath());
                    fe.setMotivo(solicitudServicios.getTipoServicio().getNombre().toUpperCase());

                    fe.setNumeroPagina(1);
                    fe.setUrlQr("");

                    FirmaElectronicaModel firma = new FirmaElectronicaModel();
                    firma.setFechaCreacion(new Date());
                    firma.setFechaFirmar(new Date());
                    firma.setFirmaElectronica(fe);

                    ss.instanciarParametros();

                    ss.agregarParametro("firmaElectronicaModel", firma);
                    ss.agregarParametro("imagenPDF", imagenPDF);
                    ss.agregarParametro("fechaFirma", new Date());
                    JsfUti.openDialogFrame("/resources/dialog/dlgFirmaElectronicaFecha.xhtml", "95%", "95%");

                } else {
                    JsfUti.messageError(null, "Debe cargar y validar su firma electrónica para continuar", "");
                }
            } else {
                JsfUti.messageError(null, "Debe subir un documento para continuar", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void archivoFirmado(SelectEvent event) {
        FirmaElectronica fe = (FirmaElectronica) event.getObject();
        if (fe != null) {
            File f = new File(fe.getArchivoFirmar());
            if (f.exists()) {
                f.delete();
            }
            agregarDocumento(Boolean.TRUE, fe.getArchivoFirmado());
        } else {
            JsfUti.messageError(null, "Error al firmar electrónicamente", "Intente nuevamente");
        }

    }

    public void enviarCorreo() {
        System.out.println("texto adicional:" + txtAdicional);
        List<CorreoArchivo> listCorreoArchivo = new ArrayList<>();
        CorreoArchivo correoArchivo;
        Boolean validar = false;
        if (!documentos.isEmpty() || !documentosNuevos.isEmpty()) {

            for (SolicitudDocumentos solic : documentos) {
                if (solic.getCheck()) {
                    validar = true;
                    correoArchivo = new CorreoArchivo(solic.getRutaArchivo(), solic.getTipoArchivo());
                    listCorreoArchivo.add(correoArchivo);
                }
            }
            for (SolicitudDocumentos sol : documentosNuevos) {
                if (sol.getCheck()) {
                    validar = true;
                    correoArchivo = new CorreoArchivo(sol.getRutaArchivo(), sol.getTipoArchivo());
                    listCorreoArchivo.add(correoArchivo);
                }
            }
            if (!validar) {
                JsfUti.messageError(null, "Adjunte un requisito", "Información");
                return;
            }
            Correo correo = new Correo(
                    solicitudServicios.getCorreoNotificacion(),
                    "Trámite: " + solicitudServicios.getTipoServicio().getNombre(),
                    Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + solicitudServicios.getTipoServicio().getNombre(),
                            "<strong>¡Hola! </strong><br><strong>" + solicitudServicios.getEnteSolicitante().getNombreCompleto() + "</strong>"
                            + " Se envía los archivos correspondientes al trámite generado.",
                            "<br><strong>Observacion:   </strong><br>" + txtAdicional
                            + "<strong>Gracias por la atención brindada</strong><br><br>",
                            "Este correo fue enviado de forma automática y no requiere respuesta."),
                    listCorreoArchivo,
                    historicoTramites.getCodigo(),
                    solicitudServicios.getEnteSolicitante().getNombreCompleto(),
                    session.getUsuarioDocs(), session.getUsuarioDocs());
            appServices.enviarCorreo(correo);
            JsfUti.messageInfo(null, "Se realizo el envio de Correo", "Información");
            JsfUti.executeJS("PF('correoDlg').hide()");
        } else {
            JsfUti.messageError(null, "Adjunte un requisito", "Información");
        }
    }

    public void eliminarDocumento(int index) {
        SolicitudDocumentos sd = documentosNuevos.get(index);
        File f = new File(sd.getRutaArchivo());
        if (f.exists()) {
            f.delete();
        }
        documentosNuevos.remove(index);
    }

    public void eliminarDocumentoBD(int index) {
        try {
            SolicitudDocumentos sd = documentos.get(index);
            if (sd != null) {
                File f = new File(sd.getRutaArchivo());
                if (f.exists()) {
                    f.delete();
                }
                eliminar(sd);
                documentos = appServices.buscarSolicitudDocumentos(new SolicitudDocumentos(new SolicitudServicios(solicitudServicios.getId())));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirDlgArchivos(SolicitudDocumentos solicitudDocumentos, int index) {
        if (solicitudDocumentos != null) {
            documento = solicitudDocumentos;
        } else {
            documento = new SolicitudDocumentos();
        }
        ServiciosDepartamentoRequisitos req = null;
        if (solicitudServicios.getTipoServicio() != null) {
            req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(solicitudServicios.getTipoServicio().getId()));
        } else if (solicitudServicios.getTipoTramite() != null && solicitudServicios.getTipoServicio() == null) {
            req = new ServiciosDepartamentoRequisitos(new TipoTramite(solicitudServicios.getTipoTramite().getId()));
        }
        if (req != null) {
            requisitos = appServices.getDeptsItemsRequisito(req);
        }
        JsfUti.executeJS("PF('DlgoDocumento').show()");
        JsfUti.update("formDocumento");
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
        solicitudDocumentos.setSolicitud(solicitudServicios);
        solicitudDocumentos.setUsuario(session.getName_user());
        solicitudDocumentos.setFechaCreacion(new Date());
        solicitudDocumentos.setNombreArchivo(file.getName());
        solicitudDocumentos.setTipoArchivo(upLoadedFile.getContentType());
        solicitudDocumentos.setRutaArchivo(file.getAbsolutePath());
        solicitudDocumentos.setTieneFirmaElectronica(Boolean.FALSE);
        return solicitudDocumentos;
    }

    public void verUrl(String url) {
        iframeUrl = url;
    }

    public void abrirNuevaPagina() {
        JsfUti.redirectNewTab(iframeUrl);
    }

    public void agregarUrl() {
        try {
            if (Utils.isNotEmptyString(urls)) {
                if (!solicitudServicios.getUrls().contains(urls)) {
                    if (Utils.isValidUrl(urls)) {
                        if (Utils.isNotEmptyString(solicitudServicios.getNotaGuia())) {
                            solicitudServicios.setNotaGuia(urls + ";" + solicitudServicios.getNotaGuia());
                        } else {
                            solicitudServicios.setNotaGuia(urls);
                        }
                        urls = "";
                        JsfUti.update("formMain");
                    } else {
                        JsfUti.messageError(null, "Ingrese una URL valida", "");
                    }
                } else {
                    JsfUti.messageError(null, "URL ya esta ingresada", "");
                }
            } else {
                JsfUti.messageError(null, "Ingrese una URL valida", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarUrl(String index) {
        System.out.println("eliminarUrl");
        try {
            List<String> temp = solicitudServicios.getUrls();
            temp.remove(index);
            Collections.sort(temp, Collections.reverseOrder());

            if (Utils.isNotEmpty(temp)) {
                solicitudServicios.setNotaGuia("");
                for (String s : temp) {
                    solicitudServicios.setNotaGuia(s + ";" + solicitudServicios.getNotaGuia());
                }
            } else {
                solicitudServicios.setNotaGuia(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {

    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public void editarArchivos(String archivoId) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("archivoId", archivoId);
        JsfUti.redirectFacesNewTab("/procesos/documentos/documento.xhtml");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(Boolean responsable) {
        this.responsable = responsable;
    }

    public HistoricoTramites getHistoricoTramites() {
        return historicoTramites;
    }

    public void setHistoricoTramites(HistoricoTramites historicoTramites) {
        this.historicoTramites = historicoTramites;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getListRolUsuario() {
        return listRolUsuario;
    }

    public void setListRolUsuario(List<User> listRolUsuario) {
        this.listRolUsuario = listRolUsuario;
    }

    public List<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public String getObservacionReasignacion() {
        return observacionReasignacion;
    }

    public void setObservacionReasignacion(String observacionReasignacion) {
        this.observacionReasignacion = observacionReasignacion;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public List<SolicitudDocumentos> getDocumentosNuevos() {
        return documentosNuevos;
    }

    public void setDocumentosNuevos(List<SolicitudDocumentos> documentosNuevos) {
        this.documentosNuevos = documentosNuevos;
    }

    public List<User> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    public void setUsuariosSeleccionados(List<User> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
    }

    public List<Departamento> getDepartamentosSolicitud() {
        return departamentosSolicitud;
    }

    public void setDepartamentosSolicitud(List<Departamento> departamentosSolicitud) {
        this.departamentosSolicitud = departamentosSolicitud;
    }

    public User getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(User usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public TreeNode getTreeDepartamento() {
        return treeDepartamento;
    }

    public void setTreeDepartamento(TreeNode treeDepartamento) {
        this.treeDepartamento = treeDepartamento;
    }

    public List<EventPrimefaces> getEvents() {
        return events;
    }

    public void setEvents(List<EventPrimefaces> events) {
        this.events = events;
    }

    public String getObservacionAprobar() {
        return observacionAprobar;
    }

    public void setObservacionAprobar(String observacionAprobar) {
        this.observacionAprobar = observacionAprobar;
    }

//</editor-fold>
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

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getIframeUrl() {
        return iframeUrl;
    }

    public void setIframeUrl(String iframeUrl) {
        this.iframeUrl = iframeUrl;
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

    public String getTxtAdicional() {
        return txtAdicional;
    }

    public void setTxtAdicional(String txtAdicional) {
        this.txtAdicional = txtAdicional;
    }

    public List<User> getUserListRechazo() {
        return userListRechazo;
    }

    public void setUserListRechazo(List<User> userListRechazo) {
        this.userListRechazo = userListRechazo;
    }

    public List<SolicitudDepartamento> getSolicitudesDepartamentos() {
        return solicitudesDepartamentos;
    }

    public void setSolicitudesDepartamentos(List<SolicitudDepartamento> solicitudesDepartamentos) {
        this.solicitudesDepartamentos = solicitudesDepartamentos;
    }

    public Boolean getDevolucionTramite() {
        return devolucionTramite;
    }

    public void setDevolucionTramite(Boolean devolucionTramite) {
        this.devolucionTramite = devolucionTramite;
    }

    public List<SolicitudDepartamento> getSolicitudesDepartamentosUsuarios() {
        return solicitudesDepartamentosUsuarios;
    }

    public void setSolicitudesDepartamentosUsuarios(List<SolicitudDepartamento> solicitudesDepartamentosUsuarios) {
        this.solicitudesDepartamentosUsuarios = solicitudesDepartamentosUsuarios;
    }

    public SolicitudDepartamento getSolicitudDepartamentoDevolucionFinal() {
        return solicitudDepartamentoDevolucionFinal;
    }

    public void setSolicitudDepartamentoDevolucionFinal(SolicitudDepartamento solicitudDepartamentoDevolucionFinal) {
        this.solicitudDepartamentoDevolucionFinal = solicitudDepartamentoDevolucionFinal;
    }

    public List<User> getUserListDevolucion() {
        return userListDevolucion;
    }

    public void setUserListDevolucion(List<User> userListDevolucion) {
        this.userListDevolucion = userListDevolucion;
    }

    public Boolean getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(Boolean tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
    }

    public List<User> getUsuariosSeleccionadosRechazo() {
        return usuariosSeleccionadosRechazo;
    }

    public void setUsuariosSeleccionadosRechazo(List<User> usuariosSeleccionadosRechazo) {
        this.usuariosSeleccionadosRechazo = usuariosSeleccionadosRechazo;
    }

    public List<Departamento> getDepartamentosRechazo() {
        return departamentosRechazo;
    }

    public void setDepartamentosRechazo(List<Departamento> departamentosRechazo) {
        this.departamentosRechazo = departamentosRechazo;
    }

}
