/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Tesauro;
import org.bcbg.documental.models.UsuarioDocs;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.Dominio;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Rol;
import org.bcbg.entities.SecuenciaTramites;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
import org.bcbg.entities.UsuarioResponsable;
import org.bcbg.entities.UsuarioResponsableServicio;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.Correo;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.models.ListaRequisitosModel;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author jesus
 */
@Named
@ViewScoped
public class IniciarTramiteMB extends BpmManageBeanBaseRoot implements Serializable {

    private List<TipoTramite> tipoTramites;
    private TipoTramite tipoTramite;
    private HistoricoTramites historicoTramites;
    private ServiciosDepartamentoRequisitos serviciosDepartamentoItemRequisito;
    private List<ServiciosDepartamentoRequisitos> serviciosRequisitoList;
    private ServiciosDepartamento item;
    private List<ServiciosDepartamento> itemList;
    private List<Departamento> departamentos;
    private Departamento departamento;
    private Departamento departamentoNotificar;
    private ListaRequisitosModel requisitosModel;
    private List<ListaRequisitosModel> requisitosModelList;
    private SolicitudDocumentos solicitudDocumentos;

    private List<UsuarioResponsable> userList;
    private UsuarioResponsable userSelect;
    private List<UsuarioResponsable> usuariosSelect;
    //private UploadedFile file;
    private List<UsuarioResponsable> usuariosNotif;
    private List<UploadedFile> files;
    private Persona solicitante;
    private String observacion, asunto;
    private Boolean pasoUno, pasoDos, validarTipoServicio, categoriaTramite, tramiteInterno, eleccionUsuarios, existeResponsable;
    private String activarPaso;
    private SolicitudDepartamento solicitudDepartamentoDB;
    private List<Dominio> dominios;
    private String dominio, correo, correoSecundario, dominioSecundario, campoReferencia;
    private int size;
    private Boolean solicitudInterna, selecionarCorreoSecundario, seleccionarCorreo;
    private List<Indexacion> indices;
    private Indexacion indice;
    private List<Tesauro> sugerencias;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        loadData();
        initDepartamentos();
        initListTramite();
        this.loadDominios();
        initServicios();
    }

    public void loadData() {
        solicitudDepartamentoDB = new SolicitudDepartamento();
        departamento = null;
        indices = documentalService.getIndices();
        selecionarCorreoSecundario = Boolean.FALSE;
        seleccionarCorreo = Boolean.FALSE;
        dominio = "@outlook.com";
        dominioSecundario = "@outlook.com";
        correo = "";
        correoSecundario = "";
        activarPaso = "0";
        pasoUno = Boolean.TRUE;
        pasoDos = Boolean.FALSE;
        validarTipoServicio = Boolean.TRUE;
        solicitante = new Persona();
        if (session.getUserId() != null) {
            solicitante = appServices.buscarPersonaXusuario(session.getName_user());
            if (solicitante != null) {
                obtenerCorreoConDominio();
            } else {
                solicitante = new Persona();
            }
        }
        tipoTramite = new TipoTramite();
        requisitosModelList = new ArrayList<>();
        userList = new ArrayList<>();
        usuariosSelect = new ArrayList<>();
        categoriaTramite = Boolean.FALSE;
        eleccionUsuarios = Boolean.TRUE;
        existeResponsable = Boolean.FALSE;
        userSelect = new UsuarioResponsable();
        solicitudInterna = Boolean.FALSE;
    }

    public void mostrarPasoUno() {
        activarPaso = "0";
        pasoUno = Boolean.TRUE;
        pasoDos = Boolean.FALSE;
    }

    public void mostrarPasoDos() {
        try {
            if (validarPasoUno()) {
                activarPaso = "1";
                pasoUno = Boolean.FALSE;
                pasoDos = Boolean.TRUE;
                generarNumTramite();
                JsfUti.update("formMain");
                JsfUti.update("dtUsuariosSelect");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void generarNumTramite() {
        Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        Formatter fmt = new Formatter();
        SecuenciaTramites num = new SecuenciaTramites();
        num.setFecha(new Date());
        num.setNumeroTramite(null);
        num = (SecuenciaTramites) service.methodPOST(num, SisVars.ws + "secuenciaTramites/save", SecuenciaTramites.class);
        Integer secuencia = (Integer) service.methodPOST(SisVars.ws + "secuencial/tramite/code/NUMERO_TRAMITE", Integer.class);
        historicoTramites = new HistoricoTramites();
        historicoTramites.setSecuencial(secuencia.longValue());
        historicoTramites.setNumTramite(num.getId());
        String codigoExterno = solicitudInterna ? "INT-" : "EXT-";
        historicoTramites.setCodigo(codigoExterno + item.getAbreviatura() + "-" + fmt.format("%05d", historicoTramites.getSecuencial()) + "-" + anio);

    }

    private Boolean validarPasoUno() {
        if (solicitante.getId() == null) {
            JsfUti.messageError(null, "Debe ingresar los datos el solicitante", "");
            return Boolean.FALSE;
        }
        if (!validarSolicitante()) {
            return Boolean.FALSE;
        }
        if (departamento == null || departamento.getId() == null) {
            JsfUti.messageError(null, "Debe escoger el departamento", "");
            return Boolean.FALSE;
        }
        if (validarTipoServicio) {
            if (tipoTramite == null || tipoTramite.getId() == null) {
                JsfUti.messageError(null, "Debe escoger el tipo de trámite", "");
                return Boolean.FALSE;
            }
            if (item == null || item.getId() == null) {
                JsfUti.messageError(null, "Debe escoger el servicio", "");
                return Boolean.FALSE;
            }
        }
        if (solicitante.getCorreo() != null && !solicitante.getCorreo().isEmpty()) {
            if (!Utils.validacionCorreos(solicitante.getCorreo())) {
                JsfUti.messageError(null, "Ingrese un email correcto", "");
                return Boolean.FALSE;
            }
        }
        if (solicitudInterna == null) {
            JsfUti.messageError(null, "Especifique si es un trámite interno o externo", "");
            return Boolean.FALSE;
        }
        if (habilitarCorreoSecundario()) {
            if (!seleccionarCorreo && !selecionarCorreoSecundario) {
                JsfUti.messageError(null, "Debe seleccionar el correo para las notificaciones", "");
                return Boolean.FALSE;
            }
//            if (seleccionarCorreo && selecionarCorreoSecundario) {
//                JsfUti.messageError(null, "Debe seleccionar solo un correo.", "");
//                return Boolean.FALSE;
//            }
        }
        if (Utils.isEmpty(usuariosSelect)) {
            JsfUti.messageError(null, "Debe asignar al menos a un usuario al trámite", "");
            return Boolean.FALSE;
        }
        for (UsuarioResponsable ur : usuariosSelect) {
            if (!ur.getUsuario().getDisponible()) {
                JsfUti.messageError(null, ur.getUsuario().getEstadoDisponible(), "");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void enviarCorrreo() {
        if (Utils.isNotEmpty(usuariosSelect)) {
            for (UsuarioResponsable ur : usuariosSelect) {
                if (ur.getUsuario() != null && ur.getUsuario().getRecursoHumano() != null && ur.getUsuario().getRecursoHumano().getPersona() != null) {
                    if (Utils.isNotEmptyString(ur.getUsuario().getRecursoHumano().getPersona().getCorreo())) {
                        Correo correoEnviar = new Correo();
                        correoEnviar.setAsunto("Trámite: " + item.getNombre());
                        correoEnviar.setNumeroTramite(historicoTramites.getCodigo());
                        correoEnviar.setNombresDetinatario(ur.getUsuario().getNombreUsuario());
                        correoEnviar.setDestinatario(ur.getUsuario().getRecursoHumano().getPersona().getCorreo());
                        correoEnviar.setMensaje(Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + item.getNombre().toUpperCase(),
                                "<strong>¡Hola! </strong><br><strong>" + ur.getUsuario().getRecursoHumano().getPersona().getDetalleNombre() + "</strong>"
                                + " se le notifica el inicio del Tramite con número: <strong>" + historicoTramites.getCodigo() + " </strong>, el cual puede consultar en su bandeja de tareas.",
                                "<strong>Gracias por la Atención Brindada</strong><br>",
                                "Este correo fue enviado de forma automática y no requiere respuesta."));
                        correoEnviar.setRemitente(session.getUsuarioDocs());
                        correoEnviar.setDestino(new UsuarioDocs(ur.getUsuario().getId(), ur.getUsuario().getNombreUsuario(), ur.getUsuario().getUsuarioNombre()));
                        appServices.enviarCorreo(correoEnviar);
                    }
                }
            }

        }

        if (Utils.isNotEmptyString(solicitante.getCorreo())) {
            Correo correoEnviar = new Correo();
            correoEnviar.setNumeroTramite(historicoTramites.getCodigo());
            correoEnviar.setNombresDetinatario(solicitante.getNombreCompleto());
            correoEnviar.setAsunto("Trámite: " + item.getNombre());
            if (seleccionarCorreo && !selecionarCorreoSecundario) {
                correoEnviar.setDestinatario(solicitante.getCorreo());
            } else if (seleccionarCorreo && selecionarCorreoSecundario) {
                String solicitantes = solicitante.getCorreo() + ";" + solicitante.getCorreoSecundario();
                correoEnviar.setDestinatario(solicitantes);
            } else {
                correoEnviar.setDestinatario(solicitante.getCorreoSecundario());
            }

            correoEnviar.setMensaje(Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + item.getNombre().toUpperCase(),
                    "<strong>¡Hola! </strong><br><strong>" + solicitante.getNombresApellidos() + "</strong>"
                    + " se le notifica el inicio del Tramite con número: <strong>" + historicoTramites.getCodigo() + " </strong>",
                    "<strong>Gracias por la Atención Brindada</strong><br>",
                    "Este correo fue enviado de forma automática y no requiere respuesta."));
            correoEnviar.setRemitente(session.getUsuarioDocs());

            //if(solicitante.getIdentificacion().equals(session.get))
            if (solicitante.getUsuario() != null) {
                correoEnviar.setDestino(new UsuarioDocs(solicitante.getUsuario().getId(), solicitante.getNombreCompleto(), solicitante.getUsuario().getData()));
            }

            appServices.enviarCorreo(correoEnviar);
        }
    }

    public void initListUsuarios() {
        try {
            userList = new ArrayList<>();
            usuariosSelect = new ArrayList<>();
            existeResponsable = Boolean.TRUE;
            List<UsuarioResponsable> users = appServices.getUsuariosResponsables(departamento.getId());
            if (!users.isEmpty()) {
                userList.addAll(users);
            } else {
                userList = appServices.getUsuarioXDepts(departamento.getId());
            }
            for (UsuarioResponsable ur : userList) {
                if (Utils.isNotEmpty(ur.getServicios())) {
                    for (UsuarioResponsableServicio urs : ur.getServicios()) {
                        if (urs.getServicio().getId().equals(item.getId())) {
                            usuariosSelect.add(ur);
                        }
                    }
                }
            }
            System.out.println("usuariosSelect: " + usuariosSelect.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarTramiteActiviti() {
        try {
            String usuarioJefe = appServices.getCandidateUserByList(appServices.getUserXDeptsXrevisores(departamento.getId()));
            if (usuarioJefe != null && !usuarioJefe.isEmpty()) {
                if (validacionesTramite()) {
                    String usuariosAsignados = appServices.getCandidateUsuarioResponsable(usuariosSelect);
                    HashMap<String, Object> pars = this.getParam(usuariosAsignados, usuarioJefe);
                    ProcessInstance p = this.startProcessByDefinitionKey(tipoTramite.getActivitykey(), pars);
                    if (p != null) {
                        historicoTramites.setNameUser(usuariosAsignados);
                        historicoTramites.setIdProceso(p.getId());
                        historicoTramites.setTipoTramite(tipoTramite);
                        historicoTramites.setSolicitante(solicitante);
                        historicoTramites.setNombreSolicitante(solicitante.getNombreCompleto());
                        //usuario_ingreso
                        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento();
                        solicitudDepartamento.setEstado(Boolean.TRUE);
                        solicitudDepartamento.setDepartamento(departamento);
                        solicitudDepartamento.setSolicitud(this.crearSolicitudServicio(usuariosAsignados));
                        solicitudDepartamento.setResponsables(usuariosAsignados);
                        solicitudDepartamento.setFecha(new Date());
                        solicitudDepartamento = (SolicitudDepartamento) service.methodPOST(solicitudDepartamento, SisVars.ws + "solicitudDepartamentos/guardar", SolicitudDepartamento.class);
                        observacion = "";
                        asunto = "";
                        if (solicitudDepartamento != null && solicitudDepartamento.getId() != null) {
                            solicitudDepartamentoDB = solicitudDepartamento;
                            historicoTramites = solicitudDepartamento.getSolicitud().getTramite();
                            JsfUti.messageInfo(null, Messages.iniciarProcesoOk, "");
                            JsfUti.executeJS("PF('continuarDlg').show()");
                            enviarCorrreo();
                            item = new ServiciosDepartamento();
                            JsfUti.update("formMain");
                            JsfUti.update("frmContinuar");
                        } else {
                            JsfUti.messageError(null, Messages.error, "");
                        }

                    } else {
                        JsfUti.messageError(null, Messages.error, "");
                    }
                }
            } else {
                JsfUti.messageError(null, "Error", "No existe un usuario asignado para la validación de la solicitud, por favor asigne a un revisor antes de continuar con el trámite.");
            }

        } catch (Exception e) {
            System.out.println("//EXCEPTION " + e.getMessage());
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validacionesTramite() {
        try {
            if (tipoTramite.getId() == null) {
                JsfUti.messageInfo(null, Messages.iniciarProcesoError, "");
                return Boolean.FALSE;
            }
            if (validarRequisitos()) {
                JsfUti.messageError(null, Messages.iniciarProcesoRequisitosError, "");
                return Boolean.FALSE;
            }

            if (asunto == null || asunto.isEmpty()) {
                JsfUti.messageError(null, "", "Ingrese un asunto");
                return Boolean.FALSE;
            }
            if (observacion == null || observacion.isEmpty()) {
                JsfUti.messageError(null, "", "Ingrese una observacion");
                return Boolean.FALSE;
            }
            if (solicitudInterna == null) {
                JsfUti.messageError(null, "", "Especifique si es un trámite externo o interno");
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            System.out.println("//Exception validacionesTramite " + e.getMessage());
            return false;
        }
    }

    public SolicitudServicios crearSolicitudServicio(String usuariosAsignados) {
        SolicitudServicios solicitudServicios = new SolicitudServicios();
        solicitudServicios.setSolicitudInterna(solicitudInterna);
        solicitudServicios.setDescripcionInconveniente(observacion.toUpperCase());
        solicitudServicios.setAsunto(asunto.toUpperCase());
        solicitudServicios.setCampoReferencia(campoReferencia != null ? campoReferencia.toUpperCase() : null);
        solicitudServicios.setAsignados(usuariosAsignados);
        solicitudServicios.setAsignado(Boolean.TRUE);
        solicitudServicios.setFechaCreacion(new Date());
        solicitudServicios.setStatus("A");
        solicitudServicios.setPrioridad(50);
        if (habilitarCorreoSecundario()) {
            if (seleccionarCorreo && !selecionarCorreoSecundario) {
                solicitudServicios.setCorreoNotificacion(solicitante.getCorreo());
            } else {
                solicitudServicios.setCorreoNotificacion(solicitante.getCorreoSecundario());
            }
        } else {
            solicitudServicios.setCorreoNotificacion(solicitante.getCorreo());
        }
        historicoTramites.setCorreoNotificacion(solicitudServicios.getCorreoNotificacion());
        solicitudServicios.setDepartamento(departamento);
        if (item != null) {
            solicitudServicios.setTipoServicio(item);
        }
        solicitudServicios.setTipoTramite(tipoTramite);
        solicitudServicios.setRepresentante(solicitante.getNombreCompleto());
        solicitudServicios.setTramite(historicoTramites);
        solicitudServicios.setEnteSolicitante(solicitante);
        solicitudServicios.setDocumentos(initDocumentos());
        solicitudServicios.setUsuarioIngreso(appServices.getUsuario(new User(session.getUserId())));
        return solicitudServicios;
    }

    public HashMap<String, Object> getParam(String usuariosAsignados, String jefe) {
        try {
            HashMap<String, Object> pars = new HashMap<>();
            pars.put("asignarUsuario", usuariosAsignados);
            pars.put("validarSolicitud", jefe);
            pars.put("prioridad", 50);
            return pars;
        } catch (Exception e) {
            System.out.println("//Exception getParam " + e.getMessage());
            return new HashMap<>();
        }
    }

    public Boolean validarRequisitos() {
        if (Utils.isEmpty(requisitosModelList)) {
            return Boolean.FALSE;
        } else {
            for (ListaRequisitosModel rq : requisitosModelList) {
                if (rq.getServiciosRequisitos().getRequerido() && Utils.isEmpty(rq.getFile())) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public void continuarProceso() {
        JsfUti.redirectFaces("/procesos/dashBoard.xhtml");
    }

    public void initListTramite() {
        tipoTramite = appServices.findTipoTramite(new TipoTramite(Boolean.TRUE, "VU"));
        if (tipoTramite == null || tipoTramite.getId() == null) {
            JsfUti.messageError("", "", "No existe un trámite de ventanilla única");
        }
        tramiteInterno = tipoTramite.getInterno();
    }

    private void initDepartamentos() {
        departamentos = appServices.findAllDepartamentoByServiciosDepartamento();
    }

    public void initDepartamentoDesdeTipoTramite() {
        tramiteInterno = tipoTramite.getInterno();
        categoriaTramite = Boolean.FALSE;
        if (tipoTramite != null && tipoTramite.getId() != null && tipoTramite.getDepartamento() != null) {
            departamento = tipoTramite.getDepartamento();
            initServiciosDepartamentos();
            validarTipoServicio = tipoTramite.getAbreviatura().equals("PAPC") ? Boolean.TRUE : Boolean.FALSE;
        } else {
            validarTipoServicio = Boolean.TRUE;
            departamento = null;
        }
        initRequisitos();
    }

    public void initServiciosDepartamentos() {
        try {
            categoriaTramite = Boolean.FALSE;
            if (departamento != null && departamento.getId() != null) {
                itemList = new ArrayList<>();
                ServiciosDepartamento i = new ServiciosDepartamento(new Departamento(departamento.getId()), new TipoTramite(tipoTramite.getId()));
                itemList = appServices.getListItems(i);

            }
        } catch (Exception e) {
            System.out.println("//Exception initServiciosDepartamento: " + e.getMessage());
        }
    }

    public void initServicios() {
        ServiciosDepartamento i = new ServiciosDepartamento(new TipoTramite(tipoTramite.getId()));
        itemList = appServices.getListItems(i);
    }

    public void initRequisitos() {
        try {
            serviciosRequisitoList = new ArrayList<>();
            requisitosModelList = new ArrayList<>();
            departamento = item.getDepartamento();
            solicitudInterna = (item.getInterno() && !item.getExterno()) ? Boolean.TRUE
                    : (!item.getInterno() && item.getExterno()) ? Boolean.FALSE : null;

            ServiciosDepartamentoRequisitos req = null;
            if (validarTipoServicio && item != null) {
                req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(item.getId()));
            } else if (!validarTipoServicio && item == null) {
                req = new ServiciosDepartamentoRequisitos(new TipoTramite(tipoTramite.getId()));
            }
            if (req != null) {
                serviciosRequisitoList = appServices.getDeptsItemsRequisito(req);
                for (ServiciosDepartamentoRequisitos sv : serviciosRequisitoList) {
                    requisitosModel = new ListaRequisitosModel();
                    requisitosModel.setServiciosRequisitos(sv);
                    requisitosModel.setFile(requisitosModel.getFile());
                    requisitosModelList.add(requisitosModel);
                }
            }
            initListUsuarios();
        } catch (Exception e) {
            System.out.println("//Exception Requisitos " + e.getMessage());
        }

    }

    public void cargarArchivos(FilesUploadEvent event) {
        try {
            System.out.println("cargarArchivos");
            if (files == null) {
                files = new ArrayList<>();
            }

            for (UploadedFile uploadedFile : event.getFiles().getFiles()) {
                File f = FilesUtil.copyFileServer(uploadedFile, SisVars.rutaRepositorioArchivo);
                anadirFiles(f, uploadedFile, false);
            }
            JsfUti.update("messages");
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");
            JsfUti.executeJS("PF('DlgoDocumento').hide()");
            JsfUti.update("formDocumento");
        } catch (IOException e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }

    @Override
    public void handleFileUpload(FileUploadEvent event) {
        if (indice != null) {
            for (IndexacionCampo ic : indice.getCampos()) {
                if (ic.getObligatorio()) {
                    if (Utils.isEmptyString(ic.getDetalle())) {
                        JsfUti.messageError(null, Messages.camposObligatorios, "");
                        return;
                    }
                }
            }
            try {
                File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
                anadirFiles(f, event.getFile(), false);
                JsfUti.update("messages");
                JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");

            } catch (IOException e) {
                JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
            }
        } else {
            JsfUti.messageError(null, "Debe escoger el tipo de índexación", "");
        }
    }

    public void aceptarDocumento() {
        JsfUti.executeJS("PF('DlgoDocumento').hide()");
        JsfUti.update("formDocumento");
    }

    public void anadirFiles(File f, UploadedFile uploadedFile, Boolean estaFirmado) {
        try {
            if (f != null) {
                requisitosModel.setFile(f);

                ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
                archivoIndex.setTramite(item.getNombre() + "\n" + requisitosModel.getServiciosRequisitos().getNombre());
                archivoIndex.setNumTramite(historicoTramites.getCodigo());
                archivoIndex.setEstado(Boolean.TRUE);
                archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo());
                archivoIndex.setTipoIndexacion(indice.getDescripcion());
                List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
                for (IndexacionCampo ic : indice.getCampos()) {
                    detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
                }
                archivoIndex.setDetalles(detalles);
                ArchivoDocs docs;
                if (!estaFirmado) {
                    docs = documentalService.guardarArchivo(uploadedFile, archivoIndex);
                } else {
                    String contentType = "application/pdf";
                    String nombreArchivo = new Date().getTime() + "_" + StringUtils.stripAccents(indice.getDescripcionArchivo()).replace(" ", "_").replace("-", "_").concat(".pdf");
                    docs = documentalService.guardarArchivo(contentType, nombreArchivo, Files.readAllBytes(f.toPath()), archivoIndex);
                }
                if (docs != null) {
                    System.out.println("docs.getId(): " + docs.getId());
                    requisitosModel.getServiciosRequisitos().setIdOrigamiDocs(docs.getId());
                }
                this.requisitosModelList.add(this.requisitosModelList.indexOf(requisitosModel), requisitosModel);
                this.requisitosModelList.remove(this.requisitosModelList.indexOf(requisitosModel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDlgNotificar() {
        loadUserNotificacion();
        JsfUti.executeJS("PF('dlgNotificar').show()");
        JsfUti.update("formNotificar");
    }

    public void loadUserNotificacion() {
        departamentoNotificar = new Departamento();
//        usuariosNotificacion = new ArrayList<>();
//        usuariosListaDlg = new ArrayList<>();
    }

    public void initUsuariosNotificar() {
        usuariosNotif = appServices.getUsuarioXDepts(departamentoNotificar.getId());
    }

    public Boolean validarResponsable(List<UsuarioResponsable> roles) {
        if (!Utils.isEmpty(roles)) {
            for (UsuarioResponsable r : roles) {
                if (r.getDepartamento().equals(departamento)
                        && r.getResponsable()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public List<SolicitudDocumentos> initDocumentos() {
        List<SolicitudDocumentos> documentos = new ArrayList<>();
        for (ListaRequisitosModel model : requisitosModelList) {

            if (model.getFile() != null) {
                for (int i = 0; i < model.getFile().size(); i++) {
                    solicitudDocumentos = new SolicitudDocumentos();
                    solicitudDocumentos.setEstado("A");
                    solicitudDocumentos.setUsuario(session.getName_user());
                    solicitudDocumentos.setFechaCreacion(new Date());
                    solicitudDocumentos.setTieneFirmaElectronica(Boolean.FALSE);
                    solicitudDocumentos.setRequisito(model.getServiciosRequisitos());
                    solicitudDocumentos.setNombreArchivo(model.getFile().get(i).getName());
                    solicitudDocumentos.setDescripcion(model.getServiciosRequisitos().getUrlDocumento());
                    try {
                        solicitudDocumentos.setTipoArchivo(Files.probeContentType(model.getFile().get(i).toPath()));
                    } catch (Exception e) {
                        solicitudDocumentos.setTipoArchivo(model.getServiciosRequisitos().getFormato_archivo());
                    }
                    solicitudDocumentos.setRutaArchivo(model.getFile().get(i).getAbsolutePath());
                    solicitudDocumentos.setCodigoVerificacion(model.getServiciosRequisitos().getIdOrigamiDocs());
                    documentos.add(solicitudDocumentos);
                }
            } else {
                solicitudDocumentos = new SolicitudDocumentos();
                solicitudDocumentos.setEstado("A");
                solicitudDocumentos.setUsuario(session.getName_user());
                solicitudDocumentos.setFechaCreacion(new Date());
                solicitudDocumentos.setTieneFirmaElectronica(Boolean.FALSE);
                solicitudDocumentos.setRequisito(model.getServiciosRequisitos());
                solicitudDocumentos.setNombreArchivo("Archivo no cargado");
                solicitudDocumentos.setDescripcion(model.getServiciosRequisitos().getUrlDocumento());
                documentos.add(solicitudDocumentos);
            }

        }

        return Utils.isNotEmpty(documentos) ? documentos : null;
    }

    public void abrirDlgBuscarDocs(ListaRequisitosModel model) {
        this.requisitosModel = model;
        JsfUti.openDialogFrame("/resources/dialog/dlgDocumentos.xhtml", "80%", "95%");
    }

    public void documentoSeleccionado(SelectEvent event) {
        try {
            ArchivoDocs docs = (ArchivoDocs) event.getObject();
            File f = new File(docs.getRuta());
            requisitosModel.setFile(f);
            this.requisitosModelList.add(this.requisitosModelList.indexOf(requisitosModel), requisitosModel);
            this.requisitosModelList.remove(this.requisitosModelList.indexOf(requisitosModel));

            ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
            archivoIndex.setTramite(item.getNombre() + "\n" + requisitosModel.getServiciosRequisitos().getNombre());
            archivoIndex.setNumTramite(historicoTramites.getCodigo());
            archivoIndex.setEstado(Boolean.TRUE);
            archivoIndex.setDetalleDocumento(docs.getDetalleDocumento());
            archivoIndex.setTipoIndexacion(docs.getTipoIndexacion());
            List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
            for (IndexacionCampo ic : docs.getDetalles()) {
                detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
            }
            archivoIndex.setDetalles(detalles);
            documentalService.guardarArchivo(f, archivoIndex);
            JsfUti.update("messages");
            JsfUti.messageInfo(null, "Información", "Los archivos se cargaron correctamente");
            JsfUti.update(":formMain:dtRequisitos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirDlgArchivos(ListaRequisitosModel model) {
        this.requisitosModel = model;
        JsfUti.executeJS("PF('DlgoDocumento').show()");
        JsfUti.update("formDocumento");
    }

    public void setFile(ListaRequisitosModel model) {
        model.setFile(new ArrayList<>());
    }

    public void buscarSolicitante() {
        try {
            if (solicitante.getIdentificacion() != null) {
                if (!solicitante.getIdentificacion().isEmpty()) {
                    Persona enteREST = appServices.buscarCatEnte(new Persona(solicitante.getIdentificacion()));
                    if (enteREST == null) {
                        ss.instanciarParametros();
                        ss.agregarParametro("ciRuc_", solicitante.getIdentificacion());
                        showDlg("/resources/dialog/dlglazyente");
                    } else {
                        solicitante = enteREST;
                        obtenerCorreoConDominio();
                    }
                    JsfUti.update("formMain:pnlSolicitante");
                } else {
                    ss.instanciarParametros();
                    ss.agregarParametro("ciRuc_", solicitante.getIdentificacion());
                    showDlg("/resources/dialog/dlglazyente");
                }

            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
    }

    private void obtenerCorreoConDominio() {
        if (solicitante != null && solicitante.getCorreo() != null && !solicitante.getCorreo().isEmpty()) {
            String[] dom = solicitante.getCorreo().split("@");
            correo = dom[0];
            dominio = "@" + dom[1];
            size = 23;
        } else {
            size = 38;
        }
        if (solicitante != null && solicitante.getCorreoSecundario() != null && !solicitante.getCorreoSecundario().isEmpty()) {
            String[] dom = solicitante.getCorreoSecundario().split("@");
            correoSecundario = dom[0];
            dominioSecundario = "@" + dom[1];
        }
    }

    public Boolean deshabilitarCorreo() {
        if (dominio.contains(Variables.dominioCorreo)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean habilitarCorreoSecundario() {
        if (dominio.contains(Variables.dominioCorreo)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("requisitos")) {
            if (solicitante.getId() == null) {
                JsfUti.messageError(null, "Debe ingresar los datos el solicitante", "");
                return event.getOldStep();
            }
            if (departamento == null || departamento.getId() == null) {
                JsfUti.messageError(null, "Debe escoger el departamento", "");
                return event.getOldStep();
            }
            if (tipoTramite == null || tipoTramite.getId() == null) {
                JsfUti.messageError(null, "Debe escoger el tipo de trámite", "");
                return event.getOldStep();
            }
            if (item == null || item.getId() == null) {
                JsfUti.messageError(null, "Debe escoger el servicio", "");
                return event.getOldStep();
            }
        }
        return event.getNewStep();
    }

    /**
     * Se ejecuta una vez que el dialog framework invocado se cierra y devuelve
     * el objecto CatEnte seleccionado y setea el ente como solicitante
     *
     * @param event Evento con los datos de la vista y el objecto que retorna el
     * dialogFramework
     */
    public void selectSolicitante(SelectEvent event) {
        solicitante = (Persona) event.getObject();
        obtenerCorreoConDominio();
    }

    public void limpiarDatosSolicitante() {
        solicitante = new Persona();
        correo = "";
        dominio = "@outlook.com";
        correoSecundario = "";
    }

    public void actualizarDatosSolicitante() {
        if (validarSolicitante()) {
            if (habilitarCorreoSecundario()) {
                solicitante.setCorreoSecundario(correoSecundario.concat(dominioSecundario));
                if (solicitante.getCorreoSecundario() != null && !solicitante.getCorreoSecundario().isEmpty()
                        && !Utils.validacionCorreos(solicitante.getCorreoSecundario())) {
                    JsfUti.messageError(null, "Ingrese un email secundario correcto", "");
                    return;
                }
            }

            solicitante.setCorreo(correo.concat(dominio));
            if (solicitante.getCorreo() != null && !solicitante.getCorreo().isEmpty() && !Utils.validacionCorreos(solicitante.getCorreo())) {
                JsfUti.messageError(null, "Ingrese un email correcto", "");
                return;
            }

            solicitante.setFechaMod(new Date());
            solicitante.setUserMod(session.getName_user());
            Persona enteREST = appServices.registrarActualizarCatEnte(solicitante);
            if (enteREST != null) {
                String[] dom = solicitante.getCorreo().split("@");
                correo = dom[0];
                dominio = "@" + dom[1];
                size = 23;
                if (habilitarCorreoSecundario()) {
                    dom = solicitante.getCorreoSecundario().split("@");
                    correoSecundario = dom[0];
                    dominioSecundario = "@" + dom[1];
                }
                JsfUti.messageInfo(null, "Datos actualizados", "");
            } else {
                JsfUti.messageError(null, "Intente nuevamente", "");
            }
        }
    }

    public Boolean validarSolicitante() {
        if (solicitante.getIdentificacion() == null || solicitante.getIdentificacion().isEmpty()) {
            JsfUti.messageError(null, "Ingrese una identificación", "");
            return Boolean.FALSE;
        }
        if (solicitante.getIdentificacion().length() == 13) {
            if (solicitante.getRazonSocial() == null || solicitante.getRazonSocial().isEmpty()) {
                JsfUti.messageError(null, "Ingrese la razón social", "");
                return Boolean.FALSE;
            }
            if (solicitante.getNombreComercial() == null || solicitante.getNombreComercial().isEmpty()) {
                JsfUti.messageError(null, "Ingrese el nombre comercial", "");
                return Boolean.FALSE;
            }
        } else if (solicitante.getIdentificacion().length() == 10) {
            if (solicitante.getNombres() == null || solicitante.getNombres().isEmpty()) {
                JsfUti.messageError(null, "Ingrese los nombres del solicitante", "");
                return Boolean.FALSE;
            }
            if (solicitante.getApellidos() == null || solicitante.getApellidos().isEmpty()) {
                JsfUti.messageError(null, "Ingrese los apellidos del solicitante", "");
                return Boolean.FALSE;
            }
        }
        // if (habilitarCorreoSecundario()) {
        //     if (correoSecundario == null || correoSecundario.isEmpty()) {
        //         JsfUti.messageError(null, "Ingrese el correo secundario del solicitante", "");
        //         return Boolean.FALSE;
        //     }
        // }
        if (correo == null || correo.isEmpty()) {
            JsfUti.messageError(null, "Ingrese el correo del solicitante", "");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void redirectTab() {
        if (solicitudDepartamentoDB != null && solicitudDepartamentoDB.getId() != null) {
            ss.borrarDatos();
            ss.borrarParametros();
            System.out.println("URL REPORTE " + SisVars.ws + "documento/solicitud/" + solicitudDepartamentoDB.getId());
            ss.setUrlWebService(SisVars.ws + "documento/solicitud/" + solicitudDepartamentoDB.getId());
            JsfUti.executeJS("PF('dlgTicked').show()");
            JsfUti.update("frmTicked");
        } else {
            JsfUti.messageError(null, "Error al imprimir, por favor intente nuevamente", "");
        }
    }

    public void updateCategoriatramite() {
        if (item == null) {
            categoriaTramite = Boolean.FALSE;
        } else {
            categoriaTramite = Boolean.TRUE;
        }
    }

    public void closeDialogNotificacion() {
        JsfUti.executeJS("PF('dlgNotificar').hide()");
        JsfUti.update("formNotificar");
        JsfUti.update("formMain:dataListUserNotif");
    }

    public void closeDialogDoc() {
        JsfUti.executeJS("PF('DlgoDocumento').hide()");
        JsfUti.update("formDocumento");
        JsfUti.update("formMain");
    }

    public String getTipoArchivo() {
        if (requisitosModel != null) {
            return requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("pdf") ? "/(\\.|\\/)(pdf)$/"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("zip") ? "/(\\.|\\/)(zip)$/"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("word") ? "/(\\.|\\/)(doc|docx)$/"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("excel") ? "/(\\.|\\/)(xls|xlsx)$/"
                    : "/(\\.|\\/)(jpe?g)$/";
        }
        return "/(\\.|\\/)(gif|jpe?g|png|pdf)$/i";
    }

    public String getArchivoPermitido() {
        if (requisitosModel != null) {
            return requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("pdf") ? "PDF"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("zip") ? "ZIP"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("word") ? "WORD"
                    : requisitosModel.getServiciosRequisitos().getFormato_archivo().contains("excel") ? "EXCEL"
                    : "JPEG";
        }
        return "";
    }

    public void loadDominios() {
        dominios = service.methodListGET(SisVars.ws + "dominio/findAll", Dominio[].class);
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {

    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public void validarCampoTesauro() {
        if (indice != null && Utils.isNotEmptyString(indice.getDescripcionArchivo())) {
            String[] palabras = indice.getDescripcionArchivo().split(" ");
            for (String s : palabras) {
                s = s.trim().toLowerCase();
                Tesauro tes = documentalService.validarIndexacionTesauro(new Tesauro(s));
                if (tes != null) {
                    if (!sugerencias.contains(tes)) {
                        sugerencias.add(tes);
                    }
                }
            }
        }
    }

    public void agregarFirmarDocumento() {
        try {
            if (requisitosModel != null && Utils.isNotEmpty(requisitosModel.getFile())) {
                if (requisitosModel.getFile().size() == 1) {
                    if (session.getFirmaElectronica() != null) {

                        FirmaElectronica fe = session.getFirmaElectronica();
                        File f = requisitosModel.getFile().get(0);
                        fe.setArchivoFirmar(f.getAbsolutePath());
                        fe.setMotivo(requisitosModel.getServiciosRequisitos().getNombre().toUpperCase());

                        FilesUtil.decrypt(SisVars.keyFiles, f.getAbsolutePath());

                        PdfReader reader = new PdfReader(new FileInputStream(f));
                        Integer numeroPagina = reader.getNumberOfPages();
                        reader.close();
                        FilesUtil.encrypt(SisVars.keyFiles, f.getAbsolutePath());
                        fe.setNumeroPagina(numeroPagina);
                        fe.setUrlQr("");

                        FirmaElectronicaModel firma = new FirmaElectronicaModel();
                        firma.setFechaCreacion(new Date());
                        firma.setFechaFirmar(new Date());
                        firma.setFirmaElectronica(fe);

                        ss.instanciarParametros();

                        ss.agregarParametro("firmaElectronicaModel", firma);
                        ss.agregarParametro("imagenPDF", Boolean.TRUE);
                        ss.agregarParametro("fechaFirma", new Date());
                        JsfUti.openDialogFrame("/resources/dialog/dlgFirmaElectronicaFecha.xhtml", "95%", "95%");

                    } else {
                        JsfUti.messageError(null, "Debe cargar y validar su firma electrónica para continuar", "");
                    }
                } else {
                    JsfUti.messageError(null, "Solo se puede firmar un documento", "");
                }
            } else {
                JsfUti.messageError(null, "Debe subir un documento para continuar", "");
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void archivoFirmado(SelectEvent event) {
        FirmaElectronica fe = (FirmaElectronica) event.getObject();
        if (fe != null) {
            requisitosModel.getFile().remove(0);
            File f = new File(fe.getArchivoFirmado());
            anadirFiles(f, null, Boolean.TRUE);
            JsfUti.executeJS("PF('DlgoDocumento').hide()");
            JsfUti.update("formDocumento");
            JsfUti.update("formMain");
        } else {
            JsfUti.messageError(null, "Error al firmar electrónicamente", "Intente nuevamente");
        }

    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Boolean getSelecionarCorreoSecundario() {
        return selecionarCorreoSecundario;
    }

    public void setSelecionarCorreoSecundario(Boolean selecionarCorreoSecundario) {
        this.selecionarCorreoSecundario = selecionarCorreoSecundario;
    }

    public Boolean getSeleccionarCorreo() {
        return seleccionarCorreo;
    }

    public void setSeleccionarCorreo(Boolean seleccionarCorreo) {
        this.seleccionarCorreo = seleccionarCorreo;
    }

    public String getDominioSecundario() {
        return dominioSecundario;
    }

    public void setDominioSecundario(String dominioSecundario) {
        this.dominioSecundario = dominioSecundario;
    }

    public String getCorreoSecundario() {
        return correoSecundario;
    }

    public void setCorreoSecundario(String correoSecundario) {
        this.correoSecundario = correoSecundario;
    }

    public Boolean getSolicitudInterna() {
        return solicitudInterna;
    }

    public void setSolicitudInterna(Boolean solicitudInterna) {
        this.solicitudInterna = solicitudInterna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getExisteResponsable() {
        return existeResponsable;
    }

    public void setExisteResponsable(Boolean existeResponsable) {
        this.existeResponsable = existeResponsable;
    }

    public ListaRequisitosModel getRequisitosModel() {
        return requisitosModel;
    }

    public void setRequisitosModel(ListaRequisitosModel requisitosModel) {
        this.requisitosModel = requisitosModel;
    }

    public List<Dominio> getDominios() {
        return dominios;
    }

    public void setDominios(List<Dominio> dominios) {
        this.dominios = dominios;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HistoricoTramites getHistoricoTramites() {
        return historicoTramites;
    }

    public void setHistoricoTramites(HistoricoTramites historicoTramites) {
        this.historicoTramites = historicoTramites;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<TipoTramite> getTipoTramites() {
        return tipoTramites;
    }

    public void setTipoTramites(List<TipoTramite> tipoTramites) {
        this.tipoTramites = tipoTramites;
    }

    public ServiciosDepartamentoRequisitos getServiciosDepartamentoItemRequisito() {
        return serviciosDepartamentoItemRequisito;
    }

    public void setServiciosDepartamentoItemRequisito(ServiciosDepartamentoRequisitos serviciosDepartamentoItemRequisito) {
        this.serviciosDepartamentoItemRequisito = serviciosDepartamentoItemRequisito;
    }

    public List<ServiciosDepartamentoRequisitos> getServiciosRequisitoList() {
        return serviciosRequisitoList;
    }

    public void setServiciosRequisitoList(List<ServiciosDepartamentoRequisitos> serviciosRequisitoList) {
        this.serviciosRequisitoList = serviciosRequisitoList;
    }

    public ServiciosDepartamento getItem() {
        return item;
    }

    public void setItem(ServiciosDepartamento item) {
        this.item = item;
    }

    public List<ServiciosDepartamento> getItemList() {
        return itemList;
    }

    public void setItemList(List<ServiciosDepartamento> itemList) {
        this.itemList = itemList;
    }

//    public List<ServiciosDepartamento> getServiciosDepartamentos() {
//        return serviciosDepartamentos;
//    }
//
//    public void setServiciosDepartamentos(List<ServiciosDepartamento> serviciosDepartamentos) {
//        this.serviciosDepartamentos = serviciosDepartamentos;
//    }
    public List<ListaRequisitosModel> getRequisitosModelList() {
        return requisitosModelList;
    }

    public void setRequisitosModelList(List<ListaRequisitosModel> requisitosModelList) {
        this.requisitosModelList = requisitosModelList;
    }

    public List<UsuarioResponsable> getUserList() {
        return userList;
    }

    public void setUserList(List<UsuarioResponsable> userList) {
        this.userList = userList;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Persona getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Persona solicitante) {
        this.solicitante = solicitante;
    }

    public Departamento getDepartamentoNotificar() {
        return departamentoNotificar;
    }

    public void setDepartamentoNotificar(Departamento departamentoNotificar) {
        this.departamentoNotificar = departamentoNotificar;
    }

    public String getObservacion() {
        return observacion;
    }

    public List<UsuarioResponsable> getUsuariosSelect() {
        return usuariosSelect;
    }

    public void setUsuariosSelect(List<UsuarioResponsable> usuariosSelect) {
        this.usuariosSelect = usuariosSelect;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Boolean getPasoUno() {
        return pasoUno;
    }

    public void setPasoUno(Boolean pasoUno) {
        this.pasoUno = pasoUno;
    }

    public Boolean getPasoDos() {
        return pasoDos;
    }

    public void setPasoDos(Boolean pasoDos) {
        this.pasoDos = pasoDos;
    }

    public List<UsuarioResponsable> getUsuariosNotif() {
        return usuariosNotif;
    }

    public void setUsuariosNotif(List<UsuarioResponsable> usuariosNotif) {
        this.usuariosNotif = usuariosNotif;
    }

    public String getActivarPaso() {
        return activarPaso;
    }

    public void setActivarPaso(String activarPaso) {
        this.activarPaso = activarPaso;
    }

    public Boolean getValidarTipoServicio() {
        return validarTipoServicio;
    }

    public void setValidarTipoServicio(Boolean validarTipoServicio) {
        this.validarTipoServicio = validarTipoServicio;
    }

    public Boolean getCategoriaTramite() {
        return categoriaTramite;
    }

    public void setCategoriaTramite(Boolean categoriaTramite) {
        this.categoriaTramite = categoriaTramite;
    }

    public Boolean getTramiteInterno() {
        return tramiteInterno;
    }

    public void setTramiteInterno(Boolean tramiteInterno) {
        this.tramiteInterno = tramiteInterno;
    }

    public Boolean getEleccionUsuarios() {
        return eleccionUsuarios;
    }

    public void setEleccionUsuarios(Boolean eleccionUsuarios) {
        this.eleccionUsuarios = eleccionUsuarios;
    }

    public UsuarioResponsable getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(UsuarioResponsable userSelect) {
        this.userSelect = userSelect;
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

    public String getCampoReferencia() {
        return campoReferencia;
    }

    public void setCampoReferencia(String campoReferencia) {
        this.campoReferencia = campoReferencia;
    }

    public List<Tesauro> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<Tesauro> sugerencias) {
        this.sugerencias = sugerencias;
    }
//</editor-fold>

}
