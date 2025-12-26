/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.ws;

import org.bcbg.entities.Persona;
import org.bcbg.entities.CtlgCatalogo;
import org.bcbg.entities.CtlgItem;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Imagenes;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.Rol;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.SolicitudDepartamento;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.SolicitudServicios;
import org.bcbg.entities.TareasActivas;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.User;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.Correo;
import org.bcbg.models.CorreoModel;
import org.bcbg.models.Data;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.models.TareasAsignadas;
import org.bcbg.models.TramiteUsuario;
import org.bcbg.models.TramiteUsuarioDetalle;
import org.bcbg.models.UsuarioTareas;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Local;
import org.bcbg.documental.models.CorreoSettings;
import org.bcbg.entities.Motivaciones;
import org.bcbg.entities.Reuniones;
import org.bcbg.entities.UsuarioResponsable;
import org.bcbg.entities.UsuarioResponsableServicio;
import org.bcbg.entities.Valores;
import org.bcbg.models.DatosReporte;
import org.primefaces.model.DefaultTreeNode;

/**
 *
 * @author ORIGAMI1
 */
@Local
public interface AppServices {

    public void enviarCorreo(Correo correo);

    public CorreoSettings guardarConfiguracionesCorreo(CorreoSettings correoSettings);

    /**
     * Este metodo es mas lento xk tambien carga a los departamentos padres
     * etc...
     *
     * @param tipoTramite
     * @return
     */
    public List<TipoTramite> getTipoTramites(TipoTramite tipoTramite);

    /**
     * Este metodo es mas rapido porque solo llega al departamento hijo
     *
     * @return
     */
    public List<TipoTramite> getTipoTramites();

    /**
     * devuelve todos los usarios que tienen relacionado un rol que se busca por
     * el nombre
     *
     * @param nombre Object
     * @param departamento Object
     * @return Object
     */
    public List<User> getUsuariosByRolName(String nombre, Departamento departamento);

    public Departamento getDepartamentoByRolName(String nombre);

    public List<RolUsuario> getRolesUsuarios(RolUsuario rolUsuario);

    public List<Rol> getListRolsActivos();

    public Rol getRol(Long idRol);

    public List<Rol> getRolesDepartamento(Long id);

    public Persona registrarActualizarCatEnte(Persona ente);

    public Persona buscarCatEnte(Persona ente);

    public Persona buscarEnteByDocumento(String documento);

    public Persona buscarPersonaXusuario(String usuario);

    public HistoricoTramites buscarHistoricoTramite(HistoricoTramites historicoTramites);

    public HistoricoTramites historicoTramiteProcessId(String id);

    public HistoricoTramites historicoTramiteProcessTemp(String id);

    public List<User> getUsuarios();

    public List<RolUsuario> getRolUsuario(Long idUser);

    public User getUsuario(User usuario);

    public List<Departamento> getListDepartamentos();

    public List<Departamento> findAllDepartamentos(Departamento data);

    /**
     * Este metodo es mas rapido xk no carga la relacion con el padre
     *
     * @return
     */
    public List<Departamento> getListDepartamentosHijos();

    public List<Departamento> getListDepartamentosConUsuario();

    public Departamento getDireccionByDepartamentoId(Long id);

    public List<Departamento> findAllDepartamentoByServiciosDepartamento();

    public Departamento buscarDepartamento(Departamento departamento);

    public Departamento buscarDepartamentoXusuario(String usuario);

//    public List<ServiciosDepartamento> getListServiciosDepartamentos(ServiciosDepartamento serviciosDepartamento);
    public List<ServiciosDepartamento> getListItems(ServiciosDepartamento item);

    public List<ServiciosDepartamentoRequisitos> getDeptsItemsRequisito(ServiciosDepartamentoRequisitos serviciosDepartamentoItemRequisitos);

    public List<UsuarioResponsable> getUsuarioXDepts(Long departamento);

    public List<User> getUserXDepts(Long departamento);

    public List<User> getUserXDeptsXrevisores(Long departamento);

    public List<UsuarioResponsable> getUsuariosResponsables(Long departamento);

    public List<Observaciones> getObservaciones(Observaciones observaciones);

    public Observaciones guardarActualizarObservacion(Observaciones observaciones);

    public Observaciones guardarObservacionHT(HistoricoTramites tramites, String tarea, String observacion);

    public SolicitudServicios buscarSolicitudServicios(SolicitudServicios servicios);

    public SolicitudServicios actualizarSolicitudServicios(SolicitudServicios servicios);

    public SolicitudServicios guardarSolicitudServicios(SolicitudServicios servicios);

    public List<SolicitudDepartamento> buscarSolicitudDepartamentos(SolicitudDepartamento solicitudDepartamento);

    public List<SolicitudDepartamento> actualizarSolicitudDepartamento(List<SolicitudDepartamento> solicitudDepartamentos);

    public SolicitudDepartamento actualizarSolicitudDepartamento(SolicitudDepartamento solicitudDepartamentos);

    public SolicitudDepartamento guardarSolicitudDepartamento(SolicitudDepartamento solicitudDepartamentos);

    public List<SolicitudDocumentos> buscarSolicitudDocumentos(SolicitudDocumentos solicitudDocumentos);

    public SolicitudDocumentos buscarSolicitudDocumento(SolicitudDocumentos solicitudDocumentos);

    public List<SolicitudDocumentos> guardarSolicitudDocumentos(List<SolicitudDocumentos> solicitudDocumentos);

    public SolicitudDocumentos guardarSolicitudDocumentos(SolicitudDocumentos solicitudDocumentos);

    public List<SolicitudDocumentos> actualizarSolicitudDocumentos(List<SolicitudDocumentos> solicitudDocumentos);

    public CorreoModel reenviarCorreo(CorreoModel correo);

    public Byte[] viewDocumentoPdf(String pathName, String descarga);

    public String getCandidateUsuarioResponsable(List<UsuarioResponsable> usuarios);

    public String getCandidateUserByList(List<User> usuarios);

    public String getCandidateUserByListNombres(List<User> usuarios);

    /**
     * registra en la base de datos las observaciones que se ingresan para los
     * tramites
     *
     * @param ht Object
     * @param nameUser Object
     * @param observaciones Object
     * @param taskDefinitionKey Object
     * @return Object
     */
    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey);

    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica);

    public FirmaElectronica firmarDigitalmente(FirmaElectronica firmaElectronica);

    public FirmaElectronica firmarElectronicamente(FirmaElectronicaModel firmaElectronica);

    public FirmaElectronica validarFirmaElectronica(String archivo, String clave);

    public List<Imagenes> obtenerImagenesDesdePDF(String ruta, Integer pagina);

    public List<TareasActivas> getTareaActiva(Integer numTramite);

    public TareasActivas getTareaActiva(Long numTramite);

    public RolUsuario getRolUsuario(RolUsuario rol);

    public CtlgItem findCatalogoItem(CtlgItem data);

    public DefaultTreeNode getTreeNodesDeparamento();

    public List<CtlgItem> listaUnidadesCatalogo(String nombre);

    public CtlgItem guardarItme(CtlgItem data);

    public CtlgCatalogo finByCatalogo(String code);

    public List<CtlgItem> finByCatalogoItem(String code);

    public DefaultTreeNode getTreeNodesDeparamento(Departamento de);

    /**
     *
     * @param codigo Es el codigo del tramite X ejemplo: IPFI-00031-2021
     * @return Devuelve la instancia del proceso de activiti para k pueda ser
     * buscado
     */
    public String getIdProcessActiviti(String codigo);

    /**
     * Este metodo devuelve el InputStream de la url de spring boot para que
     * pueda ser mostrado en el navegador
     *
     * @param url WS-MEDIA
     * @return InputStream
     */
    public InputStream getInputStreamReporte(String url);

    public Persona getUserIncioTramite(String user);

    public String getOneNameUserByRolName(String nombre, Departamento departamento);

    public String getNameUsersByRolName(String nombre, Departamento departamento);

    /**
     * Actualiza a estado de la solicitud y enviarNotificacion de pago
     *
     * @param data
     * @return
     */
    public SolicitudServicios notificarSolicitudPago(SolicitudServicios data);

    //ASIGNACION AUTOMATICA DE USUARIO
    public List<TareasAsignadas> findAllTasksAssignees(TareasAsignadas tasksAssignees);

    public TareasAsignadas findAllTaskAssignee(TareasAsignadas tasksAssignees);

    public TramiteUsuario findTramiteUsuario(TramiteUsuario tramiteUsuario);

    public List<TareasAsignadas> getTasksAssignees(List<TramiteUsuarioDetalle> tramiteUsuarioDetalles);

    public TareasAsignadas saveTaskAssignee(TareasAsignadas data);

    public TipoTramite findTipoTramite(TipoTramite data);

    public List<UsuarioTareas> findAllUsuarioTareas(UsuarioTareas data);

    public List<ServiciosDepartamentoRequisitos> getRequisitosByDepartamentoAbrev(Data data);

    public SolicitudServicios reporteInformesSolicitudSave(Data data);

    /**
     *
     * @param codigo del la tabla valores
     * @return retor el texto de la variable consultada
     */
    public String consultarValorTexto(String codigo);

    public Motivaciones consultarFraseMotivadora();

    public Data actualizarDepartamentos();

    /**
     * Cargan todos los valores por predeterminados en la app
     *
     * @return
     */
    public List<Valores> getValoresPredeterminados();

    public List<Valores> getValores(String codigo);

    public Valores guardarValores(Valores valores);

    public Valores eliminarValores(Valores valores);

    public Data generarReporteDinamico(DatosReporte datosReporte);

    public UsuarioResponsableServicio guardarUsuarioResponsableServicio(UsuarioResponsableServicio usuarioResponsableServicio);

    public UsuarioResponsableServicio elimnarUsuarioResponsableServicio(UsuarioResponsableServicio usuarioResponsableServicio);

    public List<Reuniones> getReuniones();

    public Reuniones guardarReunion(Reuniones reunion);

    public Reuniones eliminarReunion(Reuniones reunion);

}
