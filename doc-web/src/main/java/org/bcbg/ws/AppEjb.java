/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.ws;

import com.ibm.icu.text.SimpleDateFormat;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.CorreoSettings;
import org.bcbg.entities.CtlgCatalogo;
import org.bcbg.entities.CtlgItem;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Imagenes;
import org.bcbg.entities.Motivaciones;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.Persona;
import org.bcbg.entities.Reuniones;
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
import org.bcbg.entities.UsuarioResponsable;
import org.bcbg.entities.UsuarioResponsableServicio;
import org.bcbg.entities.Valores;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.Correo;
import org.bcbg.models.CorreoModel;
import org.bcbg.models.Data;
import org.bcbg.models.DatosReporte;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.models.TareasAsignadas;
import org.bcbg.models.TramiteTareas;
import org.bcbg.models.TramiteUsuario;
import org.bcbg.models.TramiteUsuarioDetalle;
import org.bcbg.models.UsuarioTareas;
import org.bcbg.session.UserSession;
import org.bcbg.util.Utils;
import org.primefaces.model.DefaultTreeNode;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author ORIGAMI1
 */
@Singleton(name = "appEjb")
@ApplicationScoped
public class AppEjb implements AppServices {

    @Inject
    private UserSession us;
    @Inject
    private BcbgService service;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Async
    public void enviarCorreo(Correo correo) {
        service.methodPOST(correo, SisVars.wsMail + "enviarCorreo", String.class);
    }

    @Override
    public CorreoSettings guardarConfiguracionesCorreo(CorreoSettings correoSettings) {
        return (CorreoSettings) service.methodPOST(correoSettings, SisVars.wsMail + "guardarConfiguraciones", CorreoSettings.class);
    }

    @Override
    public List<TipoTramite> getTipoTramites(TipoTramite tipoTramite) {
        return service.methodListGET(SisVars.ws + "tiposTramite" + Utils.armarUrlCamposObj(tipoTramite, TipoTramite.class, null), TipoTramite[].class);
    }

    @Override
    public List<TipoTramite> getTipoTramites() {
        return service.methodListGET(SisVars.ws + "tiposTramiteBPM", TipoTramite[].class);
    }

    @Override
    public List<User> getUsuariosByRolName(String nombre, Departamento departamento) {
        RolUsuario rolUsuario = new RolUsuario(null, new Rol(nombre, Boolean.TRUE, departamento));
        return service.methodListGET(SisVars.ws + "usuariosRol/finds" + Utils.armarUrlCamposObj(rolUsuario, RolUsuario.class, null),
                User[].class);
    }

    @Override
    public Departamento getDepartamentoByRolName(String codigo) {
        return (Departamento) service.methodGET(SisVars.ws + "departamentoNombre/find/" + codigo, Departamento.class);
    }

    @Override
    public List<RolUsuario> getRolesUsuarios(RolUsuario rolUsuario) {
        return service.methodListGET(SisVars.ws + "rolusuario/finds" + Utils.armarUrlCamposObj(rolUsuario, RolUsuario.class, null),
                RolUsuario[].class);
    }

    @Override
    public List<Rol> getListRolsActivos() {
        return service.methodListGET(SisVars.ws + "roles/find?estado=true", Rol[].class);
    }

    @Override
    public Rol getRol(Long idRol) {
        return (Rol) service.methodGET(SisVars.ws + "rol/find?id=" + idRol + "&estado=true", Rol.class);
    }

    @Override
    public List<ServiciosDepartamentoRequisitos> getDeptsItemsRequisito(ServiciosDepartamentoRequisitos serviciosDepartamentoItemRequisitos) {
        return service.methodListGET(SisVars.ws + "serviciosDepartamentoItemsRequisitos/find" + Utils.armarUrlCamposObj(serviciosDepartamentoItemRequisitos, ServiciosDepartamentoRequisitos.class, null), ServiciosDepartamentoRequisitos[].class);
    }

    @Override
    public List<Rol> getRolesDepartamento(Long id) {
        if (id == null) {
            return getListRolsActivos();
        } else {
            return service.methodListGET(SisVars.ws + "roles/find?estado=true&departamento.id=" + id, Rol[].class);
        }
    }

    @Override
    public Persona registrarActualizarCatEnte(Persona ente) {
        try {
            if (ente.getId() == null) {
                ente = (Persona) service.methodPOST(ente, SisVars.ws + "personas/guardar", Persona.class);
            } else {
                ente = (Persona) service.methodPUT(ente, SisVars.ws + "personas/actualizar", Persona.class);
            }
            return ente;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Persona buscarCatEnte(Persona ente) {
        return (Persona) service.methodGET(SisVars.ws + "persona/find" + Utils.armarUrlCamposObj(ente, Persona.class, null), Persona.class);
    }

    @Override
    public Persona buscarEnteByDocumento(String documento) {
        return (Persona) service.methodGET(SisVars.ws + "persona/documento/" + documento, Persona.class);
    }

    @Override
    public Persona buscarPersonaXusuario(String usuario) {
        return (Persona) service.methodGET(SisVars.ws + "persona/usuario/" + usuario, Persona.class);
    }

    @Override
    public HistoricoTramites buscarHistoricoTramite(HistoricoTramites historicoTramites) {
        return (HistoricoTramites) service.methodGET(SisVars.ws + "historicoTramite/find" + Utils.armarUrlCamposObj(historicoTramites, HistoricoTramites.class, null), HistoricoTramites.class);
    }

    @Override
    public HistoricoTramites historicoTramiteProcessId(String id) {
        return (HistoricoTramites) service.methodGET(SisVars.ws + "historicoTramite/findProcessId/" + id, HistoricoTramites.class);
    }

    @Override
    public HistoricoTramites historicoTramiteProcessTemp(String id) {
        return (HistoricoTramites) service.methodGET(SisVars.ws + "historicoTramite/findProcessTemp/" + id, HistoricoTramites.class);
    }

    @Override
    public List<Departamento> getListDepartamentos() {
        return service.methodListGET(SisVars.ws + "departamentos/find?estado=true", Departamento[].class);
    }

    @Override
    public List<Departamento> findAllDepartamentos(Departamento data) {
        return service.methodListGET(SisVars.ws + "departamentos/find" + Utils.armarUrlCamposObj(data, Departamento.class, null), Departamento[].class);
    }

    @Override
    public List<Departamento> findAllDepartamentoByServiciosDepartamento() {
        return service.methodListGET(SisVars.ws + "departamentos/serviciosDepartamento/findAll", Departamento[].class);
    }

    @Override
    public List<Departamento> getListDepartamentosHijos() {
        return service.methodListGET(SisVars.ws + "departamentosHijos", Departamento[].class);
    }

    @Override
    public List<Departamento> getListDepartamentosConUsuario() {
        return service.methodListGET(SisVars.ws + "direccionConUsuario", Departamento[].class);
    }

    @Override
    public Departamento getDireccionByDepartamentoId(Long idDepartamento) {
        return (Departamento) service.methodPOST(SisVars.ws + "find/direccion/" + idDepartamento, Departamento.class);
    }

    @Override
    public Departamento buscarDepartamento(Departamento departamento) {
        return (Departamento) service.methodGET(SisVars.ws + "departamento/find" + Utils.armarUrlCamposObj(departamento, Departamento.class, null), Departamento.class);
    }

    @Override
    public Departamento buscarDepartamentoXusuario(String usuario) {
        return (Departamento) service.methodGET(SisVars.ws + "departamentoXusuario/" + usuario, Departamento.class);
    }

    @Override
    public List<ServiciosDepartamento> getListItems(ServiciosDepartamento item) {
        String params = Utils.armarUrlCamposObj(item, ServiciosDepartamento.class, null);
        String page = "page=0&size=1000&sort=nombre,asc";
        return service.methodListGET(SisVars.ws + "serviciosDepartamentoItems/find" + ((params != null && !params.isEmpty()) ? params + "&" + page : "?" + page), ServiciosDepartamento[].class);
    }

    /**
     * Se pone 1000 porque despues cogen 10 =v
     *
     * @return
     */
    @Override
    public List<User> getUsuarios() {
        return service.methodListGET(SisVars.ws + "usuarios?page=0&size=1000", User[].class);
    }

    @Override
    public List<RolUsuario> getRolUsuario(Long idUser) {
        return service.methodListGET(SisVars.ws + "rol/usuario/" + idUser, RolUsuario[].class);
    }

    @Override
    public List<UsuarioResponsable> getUsuarioXDepts(Long departamento) {
        return service.methodListGET(SisVars.ws + "usuariosXdepartamentos/" + departamento, UsuarioResponsable[].class);
    }

    @Override
    public List<User> getUserXDepts(Long departamento) {
        return service.methodListGET(SisVars.ws + "usersXdepartamentos/" + departamento, User[].class);
    }

    @Override
    public List<User> getUserXDeptsXrevisores(Long departamento) {
        return service.methodListGET(SisVars.ws + "usersXdepartamentosXrevisores/" + departamento, User[].class);
    }

    @Override
    public List<UsuarioResponsable> getUsuariosResponsables(Long departamento) {
        return service.methodListGET(SisVars.ws + "usuariosXdepartamentosXresponsable/" + departamento, UsuarioResponsable[].class);
    }

    @Override
    public User getUsuario(User usuario) {
        return (User) service.methodGET(SisVars.ws + "usuario/find" + Utils.armarUrlCamposObj(usuario, User.class, null), User.class);
    }

    @Override
    public List<Observaciones> getObservaciones(Observaciones observaciones) {
        return service.methodListGET(SisVars.ws + "observaciones/find" + Utils.armarUrlCamposObj(observaciones, Observaciones.class, null), Observaciones[].class);
    }

    @Override
    public Observaciones guardarActualizarObservacion(Observaciones observaciones) {
        try {
            if (observaciones.getId() == null) {
                observaciones = (Observaciones) service.methodPOST(observaciones, SisVars.ws + "observaciones/guardar", Observaciones.class);
            } else {
                observaciones = (Observaciones) service.methodPUT(observaciones, SisVars.ws + "observaciones/actualizar", Observaciones.class);
            }
            return observaciones;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Observaciones guardarObservacionHT(HistoricoTramites tramites, String tarea, String observacion) {
        tramites.setNameUser(us.getName_user());
        tramites.setTarea(tarea);
        tramites.setObservacion(observacion);
        return (Observaciones) service.methodPOST(tramites, SisVars.ws + "create/observacion", Observaciones.class);
    }

    @Override
    public SolicitudServicios buscarSolicitudServicios(SolicitudServicios servicios) {
        servicios = (SolicitudServicios) service.methodGET(SisVars.ws + "solicitudServicio/find" + Utils.armarUrlCamposObj(servicios, SolicitudServicios.class, null), SolicitudServicios.class);
        if (servicios != null) {
            servicios.setSolicitudDepartamentos(buscarSolicitudDepartamentos(new SolicitudDepartamento(new SolicitudServicios(servicios.getId()), Boolean.TRUE)));
        }
        return servicios;
    }

    @Override
    public SolicitudServicios actualizarSolicitudServicios(SolicitudServicios servicios) {
        return (SolicitudServicios) service.methodPUT(servicios, SisVars.ws + "solicitudServicios/actualizar", SolicitudServicios.class);
    }

    @Override
    public SolicitudServicios guardarSolicitudServicios(SolicitudServicios servicios) {
        return (SolicitudServicios) service.methodPOST(servicios, SisVars.ws + "solicitudServicios/guardar", SolicitudServicios.class);
    }

    @Override
    public List<SolicitudDepartamento> buscarSolicitudDepartamentos(SolicitudDepartamento solicitudDepartamento) {
        return service.methodListGET(SisVars.ws + "solicitudDepartamentos/find" + Utils.armarUrlCamposObj(solicitudDepartamento, SolicitudDepartamento.class, null), SolicitudDepartamento[].class);
    }

    @Override
    public List<SolicitudDepartamento> actualizarSolicitudDepartamento(List<SolicitudDepartamento> solicitudDepartamentos) {
        return service.methodListPUT(solicitudDepartamentos, SisVars.ws + "solicitudDepartamentos/actualizarList", SolicitudDepartamento[].class);
    }

    @Override
    public SolicitudDepartamento guardarSolicitudDepartamento(SolicitudDepartamento solicitudDepartamentos) {
        return (SolicitudDepartamento) service.methodPOST(solicitudDepartamentos, SisVars.ws + "solicitudDepartamentos/guardar", SolicitudDepartamento.class);
    }

    @Override
    public SolicitudDepartamento actualizarSolicitudDepartamento(SolicitudDepartamento solicitudDepartamentos) {
        return (SolicitudDepartamento) service.methodPUT(solicitudDepartamentos, SisVars.ws + "solicitudDepartamentos/actualizar", SolicitudDepartamento.class);
    }

    @Override
    public List<SolicitudDocumentos> buscarSolicitudDocumentos(SolicitudDocumentos solicitudDocumentos) {
        return service.methodListGET(SisVars.ws + "solicitudDocumentos/find" + Utils.armarUrlCamposObj(solicitudDocumentos, SolicitudDocumentos.class, null), SolicitudDocumentos[].class);
    }

    @Override
    public SolicitudDocumentos buscarSolicitudDocumento(SolicitudDocumentos solicitudDocumentos) {
        return (SolicitudDocumentos) service.methodGET(SisVars.ws + "solicitudDocumentos/findOne" + Utils.armarUrlCamposObj(solicitudDocumentos, SolicitudDocumentos.class, null), SolicitudDocumentos.class);
    }

    @Override
    public List<SolicitudDocumentos> actualizarSolicitudDocumentos(List<SolicitudDocumentos> solicitudDocumentos) {
        return service.methodListPUT(solicitudDocumentos, SisVars.ws + "solicitudDocumentos/actualizar", SolicitudDocumentos[].class);
    }

    @Override
    public List<SolicitudDocumentos> guardarSolicitudDocumentos(List<SolicitudDocumentos> solicitudDocumentos) {
        return service.methodListPOST(solicitudDocumentos, SisVars.ws + "solicitudDocumentos/guardar", SolicitudDocumentos[].class);
    }

    @Override
    public SolicitudDocumentos guardarSolicitudDocumentos(SolicitudDocumentos solicitudDocumentos) {
        return (SolicitudDocumentos) service.methodPOST(solicitudDocumentos, SisVars.ws + "solicitudDocumento/guardar", SolicitudDocumentos.class);
    }

    @Override
    public CorreoModel reenviarCorreo(CorreoModel correo) {
        return (CorreoModel) service.methodPOST(correo, SisVars.wsMail + "correo/reenviar", CorreoModel.class);

    }

    @Override
    public Observaciones guardarObservaciones(HistoricoTramites ht, String nameUser, String observaciones, String taskDefinitionKey) {
        try {
            Observaciones observ = new Observaciones();
            observ.setEstado(true);
            observ.setFecCre(new Date());
            observ.setTarea(taskDefinitionKey);
            observ.setIdTramite(ht);
            observ.setUserCre(nameUser);
            observ.setObservacion(observaciones);
            return guardarActualizarObservacion(observ);
        } catch (Exception e) {
            Logger.getLogger(AppEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String getCandidateUsuarioResponsable(List<UsuarioResponsable> usuarios) {
        String cadena = "";
        for (UsuarioResponsable user : usuarios) {
            cadena = cadena + "," + user.getUsuario().getUsuarioNombre();
        }
        return cadena.substring(1);
    }

    @Override
    public String getCandidateUserByList(List<User> usuarios) {
        String cadena = "";
        if (Utils.isNotEmpty(usuarios)) {
            for (User user : usuarios) {
                cadena = cadena + "," + user.getUsuarioNombre();
            }
            return cadena.substring(1);
        }
        return cadena;
    }

    @Override
    public String getCandidateUserByListNombres(List<User> usuarios) {
        String cadena = "";
        if (Utils.isNotEmpty(usuarios)) {
            for (User user : usuarios) {
                cadena = cadena + "," + user.getNombreUsuario() + " " + user.getRecursoHumano().getPersona().getDetalleNombre();
            }
            return cadena.substring(1);
        }
        return cadena;
    }

    @Override
    public Byte[] viewDocumentoPdf(String pathName, String descarga) {
        return (Byte[]) service.methodGET(SisVars.wsMedia + "resource/pdf/" + pathName + "/descarga/" + descarga, Byte[].class);
    }

    @Override
    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica) {
        return (FirmaElectronica) service.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/generar", FirmaElectronica.class);
    }

    @Override
    public FirmaElectronica firmarDigitalmente(FirmaElectronica firmaElectronica) {
        return (FirmaElectronica) service.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaDigital/generar", FirmaElectronica.class);
    }

    @Override
    public FirmaElectronica firmarElectronicamente(FirmaElectronicaModel firmaElectronicaModel) {
        return (FirmaElectronica) service.methodPOST(firmaElectronicaModel, SisVars.wsFirmaEC + "firmaElectronica/generarDocumentoAnterior", FirmaElectronica.class);
    }

    @Override
    public FirmaElectronica validarFirmaElectronica(String archivo, String clave) {
        FirmaElectronica firmaElectronicaValidar = new FirmaElectronica();
        firmaElectronicaValidar.setArchivo(archivo);
        firmaElectronicaValidar.setClave(clave);
        firmaElectronicaValidar = (FirmaElectronica) service.methodPOST(firmaElectronicaValidar, SisVars.wsFirmaEC + "firmaElectronica/validar", FirmaElectronica.class);
        return firmaElectronicaValidar;
    }

    @Override
    public List<TareasActivas> getTareaActiva(Integer numTramite) {
        return (List<TareasActivas>) service.methodListGET(SisVars.ws + "tareasActivas/find?numTramite=" + numTramite, TareasActivas[].class);
    }

    @Override
    public TareasActivas getTareaActiva(Long numTramite) {
        return (TareasActivas) service.methodGET(SisVars.ws + "tareaActiva/find?numTramite=" + numTramite, TareasActivas.class);
    }

    @Override
    public RolUsuario getRolUsuario(RolUsuario rol) {
        return (RolUsuario) service.methodGET(SisVars.ws + "rolusuario/find" + Utils.armarUrlCamposObj(rol, RolUsuario.class, null), RolUsuario.class);
    }

    @Override
    public List<Imagenes> obtenerImagenesDesdePDF(String ruta, Integer pagina) {
        return (List<Imagenes>) service.methodListGET(SisVars.wsMedia + "resource/pdfImagenes/" + Utils.getFilterRuta(ruta) + "/pagina/" + pagina, Imagenes[].class);
    }

    @Override
    public DefaultTreeNode getTreeNodesDeparamento() {
        try {
            DefaultTreeNode root = new DefaultTreeNode(new Departamento("Entidades de la empresa", Boolean.TRUE, Boolean.TRUE, "EMPRESA"));
            DefaultTreeNode node;
            List<Departamento> list = service.methodListGET(SisVars.ws + "departamento/padres", Departamento[].class);
            for (Departamento arc : list) {
                node = new DefaultTreeNode(arc, root);
                this.fillSons(node, arc.getId());
            }
            return root;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public DefaultTreeNode getTreeNodesDeparamento(Departamento de) {
        try {
            DefaultTreeNode root = new DefaultTreeNode(new Departamento("Entidades de la empresa", Boolean.TRUE, Boolean.TRUE, "EMPRESA"));
            DefaultTreeNode node;
            node = new DefaultTreeNode(de, root);
            this.fillSons(node, de.getId());
            return root;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void fillSons(DefaultTreeNode node, Long departamento) {
        DefaultTreeNode temp;
        List<Departamento> list = service.methodListGET(SisVars.ws + "departamento/hijos/" + departamento, Departamento[].class);
        if (!list.isEmpty()) {
            for (Departamento arc : list) {
                temp = new DefaultTreeNode(arc, node);
                this.fillSons(temp, arc.getId());
            }
        }
    }

    @Override
    public List<CtlgItem> listaUnidadesCatalogo(String nombre) {
        return (List<CtlgItem>) service.methodListGET(SisVars.ws, CtlgItem[].class);
    }

    @Override
    public CtlgItem guardarItme(CtlgItem data) {
        try {
            data = (CtlgItem) service.methodPOST(data, SisVars.ws + "catalogoItem/guardar", CtlgItem.class);
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CtlgCatalogo finByCatalogo(String code) {
        return (CtlgCatalogo) service.methodGET(SisVars.ws + "catalogoItem/find/" + code, CtlgCatalogo.class);
    }

    @Override
    public List<CtlgItem> finByCatalogoItem(String code) {
        return (List<CtlgItem>) service.methodListGET(SisVars.ws + "catalogoItem/buscar/" + code, CtlgItem[].class);
    }

    @Override
    public CtlgItem findCatalogoItem(CtlgItem data) {
        return (CtlgItem) service.methodPOST(data, SisVars.ws + "catalogoItem/find" + Utils.armarUrlCamposObj(data, CtlgItem.class, null), CtlgItem.class);
    }

    @Override
    public String getIdProcessActiviti(String codigo) {
        return (String) service.methodGET(SisVars.ws + "historicoTramite/codigo/" + codigo, String.class);
    }

    @Override
    public InputStream getInputStreamReporte(String url) {
        try {
            InputStream is = new ByteArrayInputStream((byte[]) service.methodGET(url, byte[].class));
            return is;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Persona getUserIncioTramite(String user) {
        return (Persona) service.methodGET(SisVars.ws + "inicio/tramiteFind/" + user, Persona.class);
    }

    @Override
    public String getOneNameUserByRolName(String nombre, Departamento departamento) {
        try {
            RolUsuario rolUsuario = new RolUsuario(null, new Rol(nombre, Boolean.TRUE, departamento));
            List<User> usuarios = service.methodListGET(SisVars.ws + "usuariosRol/finds"
                    + Utils.armarUrlCamposObj(rolUsuario, RolUsuario.class, null), User[].class);
            String cadena = usuarios.get(0).getNombreUsuario();
            return cadena.isEmpty() ? null : cadena;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getNameUsersByRolName(String nombre, Departamento departamento) {
        try {
            RolUsuario rolUsuario = new RolUsuario(null, new Rol(nombre, Boolean.TRUE, departamento));
            List<User> usuarios = service.methodListGET(SisVars.ws + "usuariosRol/finds"
                    + Utils.armarUrlCamposObj(rolUsuario, RolUsuario.class, null), User[].class);
            String cadena = "";
            for (User user : usuarios) {
                cadena = cadena + "," + user.getNombreUsuario();
            }
            return cadena.isEmpty() ? null : cadena.substring(1);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public SolicitudServicios notificarSolicitudPago(SolicitudServicios data) {
        return (SolicitudServicios) service.methodPUT(data, SisVars.ws + "solicitudServicios/notificarSolicitudPago", SolicitudServicios.class);
    }

    @Override
    public List<TareasAsignadas> findAllTasksAssignees(TareasAsignadas tasksAssignees) {
        return service.methodListGET(SisVars.ws + "tasks/assigns/findAll"
                + Utils.armarUrlCamposObj(tasksAssignees, TareasAsignadas.class, null), TareasAsignadas[].class);
    }

    @Override
    public TramiteUsuario findTramiteUsuario(TramiteUsuario tramiteUsuario) {
        return (TramiteUsuario) service.methodPOST(SisVars.ws + "tramite/usuario/find"
                + Utils.armarUrlCamposObj(tramiteUsuario, TramiteUsuario.class, null), TramiteUsuario.class);
    }

    @Override
    public List<TareasAsignadas> getTasksAssignees(List<TramiteUsuarioDetalle> tramiteUsuarioDetalles) {
        List<TareasAsignadas> tasksAssignees = new ArrayList<>();
        Date dayMonday = Utils.getDayMonday();
        for (TramiteUsuarioDetalle tud : tramiteUsuarioDetalles) {
            TareasAsignadas taskAssignee = findAllTaskAssignee(new TareasAsignadas(new UsuarioTareas(tud.getUsuarioTareas().getId()),
                    format.format(dayMonday)));
            if (taskAssignee != null && taskAssignee.getId() != null) {
                tasksAssignees.add(taskAssignee);
            }
        }
        return tasksAssignees;
    }

    @Override
    public TareasAsignadas findAllTaskAssignee(TareasAsignadas tasksAssignees) {
        return (TareasAsignadas) service.methodPOST(SisVars.ws + "task/assign/find"
                + Utils.armarUrlCamposObj(tasksAssignees, TareasAsignadas.class, null),
                TareasAsignadas.class);
    }

    @Override
    public TareasAsignadas saveTaskAssignee(TareasAsignadas data) {
        return (TareasAsignadas) service.methodPOST(data, SisVars.ws + "tasks/assigns/save", TareasAsignadas.class);
    }

    @Override
    public TipoTramite findTipoTramite(TipoTramite data) {
        return (TipoTramite) service.methodPOST(SisVars.ws + "tipo/tramite/find"
                + Utils.armarUrlCamposObj(data, TipoTramite.class, null), TipoTramite.class);
    }

    @Override
    public List<UsuarioTareas> findAllUsuarioTareas(UsuarioTareas data) {
        return service.methodListPOST(SisVars.ws + "usuarios/tareas" + Utils.armarUrlCamposObj(data, UsuarioTareas.class, null), UsuarioTareas[].class);
    }

    @Override
    public List<ServiciosDepartamentoRequisitos> getRequisitosByDepartamentoAbrev(Data data) {
        return service.methodListPOST(data, SisVars.ws + "serviciosDepartamentoItemsRequisitos/findAbrev", ServiciosDepartamentoRequisitos[].class);
    }

    @Override
    public SolicitudServicios reporteInformesSolicitudSave(Data data) {
        return (SolicitudServicios) service.methodPOST(data, SisVars.ws + "reportes/informes", SolicitudServicios.class);
    }

    @Override
    public String consultarValorTexto(String codigo) {
        Valores valor = (Valores) service.methodGETwithouAuth(SisVars.ws + "valor/code/" + codigo, Valores.class);
        if (valor != null) {
            return valor.getValorString();
        }
        return "";
    }

    @Override
    public Motivaciones consultarFraseMotivadora() {
        return (Motivaciones) service.methodGET(SisVars.ws + "motivacion", Motivaciones.class);
    }

    @Override
    public Data actualizarDepartamentos() {
        return (Data) service.methodGET(SisVars.ws + "departamentos/sincronizar", Data.class);
    }

    @Override
    public List<Valores> getValoresPredeterminados() {
        return service.methodListGET(SisVars.ws + "valores/find", Valores[].class);
    }

    @Override
    public List<Valores> getValores(String codigo) {
        return service.methodListGET(SisVars.ws + "valores/find/" + codigo, Valores[].class);
    }

    @Override
    public Valores guardarValores(Valores valores) {
        return (Valores) service.methodPOST(valores, SisVars.ws + "valores/guardar", Valores.class);
    }

    @Override
    public Valores eliminarValores(Valores valores) {
        return (Valores) service.methodPOST(valores, SisVars.ws + "valores/eliminar", Valores.class);
    }

    @Override
    public Data generarReporteDinamico(DatosReporte datosReporte) {
        return (Data) service.methodPOST(datosReporte, SisVars.ws + "reporte/dinamico", Data.class);
    }

    @Override
    public UsuarioResponsableServicio guardarUsuarioResponsableServicio(UsuarioResponsableServicio usuarioResponsableServicio) {
        return (UsuarioResponsableServicio) service.methodPOST(usuarioResponsableServicio, SisVars.ws + "guardar/usuario/responsableServicio", UsuarioResponsableServicio.class);
    }

    @Override
    public UsuarioResponsableServicio elimnarUsuarioResponsableServicio(UsuarioResponsableServicio usuarioResponsableServicio) {
        return (UsuarioResponsableServicio) service.methodPOST(usuarioResponsableServicio, SisVars.ws + "eliminar/usuario/responsableServicio", UsuarioResponsableServicio.class);
    }

    @Override
    public List<Reuniones> getReuniones() {
        return service.methodListGET(SisVars.ws + "reuniones/find", Reuniones[].class);
    }

    @Override
    public Reuniones guardarReunion(Reuniones reunion) {
        return (Reuniones) service.methodPOST(reunion, SisVars.ws + "reuniones/guardar", Reuniones.class);
    }

    @Override
    public Reuniones eliminarReunion(Reuniones reunion) {
        return (Reuniones) service.methodPOST(reunion, SisVars.ws + "reuniones/eliminar", Reuniones.class);
    }
}
