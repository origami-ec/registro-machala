/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.Rol;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.UsuarioResponsable;
import org.bcbg.entities.UsuarioResponsableServicio;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named
@ViewScoped
public class AsignarResponsableMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private UserSession session;
    @Inject
    private AppServices appServices;

    private List<Departamento> departamentos;
    private Departamento departamento;
    private List<UsuarioResponsable> listRolUsuario;
    private List<ServiciosDepartamento> serviciosDepartamentos;
    private List<ServiciosDepartamento> servicios, serviciosSeleccionados;
    private TipoTramite tipoTramite;
    private UsuarioResponsable usuarioSeleccionado;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        departamento = new Departamento();
        listRolUsuario = new ArrayList<>();

        tipoTramite = appServices.findTipoTramite(new TipoTramite(Boolean.TRUE, "VU"));
        initDepartamentos();
    }

    private void initDepartamentos() {
        Rol rol;
        departamentos = new ArrayList<>();
        if (session.getIsUserAdmin()) {
            departamentos = appServices.findAllDepartamentoByServiciosDepartamento();
        } else {
            departamento = appServices.buscarDepartamento(new Departamento(session.getDepts().get(0)));
            departamentos.add(departamento);
            ServiciosDepartamento i = new ServiciosDepartamento(new Departamento(departamento.getId()), new TipoTramite(tipoTramite.getId()));
            servicios = appServices.getListItems(i);
            initListUsuarios();
        }
    }

    public void initListUsuarios() {
        listRolUsuario = appServices.getUsuarioXDepts(departamento.getId());
        ServiciosDepartamento i = new ServiciosDepartamento(new Departamento(departamento.getId()), new TipoTramite(tipoTramite.getId()));
        servicios = appServices.getListItems(i);
    }

    public void editResponsable(int index, UsuarioResponsable rol) {
        UsuarioResponsable rolEdit = (UsuarioResponsable) service.methodPUT(rol, SisVars.ws + "guardar/usuario/responsable", UsuarioResponsable.class);
        if (rolEdit != null) {
            listRolUsuario.set(index, rolEdit);
            JsfUti.messageInfo(null, Messages.correcto, "");
            JsfUti.update("mainForm");
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
            JsfUti.update("mainForm");
        }
    }

    public void asignarServicios(UsuarioResponsable data) {
        usuarioSeleccionado = data;
        serviciosSeleccionados = new ArrayList<>();
        if (Utils.isNotEmpty(data.getServicios())) {
            for (UsuarioResponsableServicio urs : data.getServicios()) {
                serviciosSeleccionados.add(urs.getServicio());
            }
        }
        JsfUti.executeJS("PF('dlgUsuarioServicio').show()");
    }

    public void guardarUsuarioServicios() {
        JsfUti.executeJS("PF('dlgUsuarioServicio').hide()");
        initListUsuarios();
    }

    public void eliminarUsuarioServicio(UnselectEvent event) {
        System.out.println("eliminarUsuarioServicio: ");
        UsuarioResponsableServicio urs = (UsuarioResponsableServicio) event.getObject();
        appServices.elimnarUsuarioResponsableServicio(urs);
        JsfUti.messageInfo(null, "Servicio eliminado correctamente", "");
    }

    public void onRowSelect(SelectEvent<ServiciosDepartamento> event) {
        UsuarioResponsableServicio urs = new UsuarioResponsableServicio();
        urs.setUsuarioResponsable(new UsuarioResponsable(usuarioSeleccionado.getId()));
        urs.setServicio(new ServiciosDepartamento(event.getObject().getId()));
        urs.setEstado(Boolean.TRUE);
        appServices.guardarUsuarioResponsableServicio(urs);
        JsfUti.messageInfo(null, Messages.correcto, "");
    }

    public void onRowUnselect(UnselectEvent<ServiciosDepartamento> event) {
        UsuarioResponsableServicio urs = new UsuarioResponsableServicio();
        urs.setUsuarioResponsable(new UsuarioResponsable(usuarioSeleccionado.getId()));
        urs.setServicio(new ServiciosDepartamento(event.getObject().getId()));
        urs.setEstado(Boolean.TRUE);
        appServices.elimnarUsuarioResponsableServicio(urs);
        JsfUti.messageInfo(null, "Servicio eliminado correctamente", "");
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

    public List<UsuarioResponsable> getListRolUsuario() {
        return listRolUsuario;
    }

    public void setListRolUsuario(List<UsuarioResponsable> listRolUsuario) {
        this.listRolUsuario = listRolUsuario;
    }

    public List<ServiciosDepartamento> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServiciosDepartamento> servicios) {
        this.servicios = servicios;
    }

    public UsuarioResponsable getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioResponsable usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<ServiciosDepartamento> getServiciosSeleccionados() {
        return serviciosSeleccionados;
    }

    public void setServiciosSeleccionados(List<ServiciosDepartamento> serviciosSeleccionados) {
        this.serviciosSeleccionados = serviciosSeleccionados;
    }

}
