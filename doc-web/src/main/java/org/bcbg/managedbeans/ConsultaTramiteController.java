/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Persona;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelTareas;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.TareasHistoricas;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author jesus
 */
@Named
@ViewScoped
public class ConsultaTramiteController implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private UserSession us;
    private String identificacion;
    private String nombreUsuario;
    private LazyModelTareas<TareasHistoricas> lazyTareas;
    private Date fechaInicio;
    private Date fechaFin;
    private String estadoTramite;
    private Boolean renderedButton;
    private SimpleDateFormat format;
    private String nombreUsuarioFuncionario;
    private Date fechaInicioFuncionario;
    private Date fechaFinFuncionario;
    private Boolean renderedButtonFuncionario;
    private String estadoTramiteFuncionario;
    private LazyModelWS<User> usuarios;
    private LazyModelWS<Persona> catEntes;

    @PostConstruct
    public void init() {
        loadModel();
        loadDataFuncionario();
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void loadLazyUsuario() {
        usuarios = new LazyModelWS<>(SisVars.ws + "usuarios?sort=user", User[].class, us.getToken());
        JsfUti.executeJS("PF('dialogFuncionarios').show()");
    }

    public void loadLazySolicitante() {
        catEntes = new LazyModelWS<>(SisVars.ws + "personas/find", Persona[].class, us.getToken());
        JsfUti.executeJS("PF('dialogSolicitantes').show()");
    }

    public void loadModel() {
        identificacion = "";
        nombreUsuario = "";
        estadoTramite = "PENDIENTE";
        renderedButton = Boolean.FALSE;
        lazyTareas = null;
        fechaInicio = null;
        fechaFin = null;
        System.out.println("fecha " + fechaInicio);
    }

    public void loadDataFuncionario() {
        nombreUsuarioFuncionario = "";
        fechaInicioFuncionario = null;
        fechaFinFuncionario = null;
        estadoTramiteFuncionario = "PENDIENTE";
        renderedButtonFuncionario = Boolean.FALSE;
        lazyTareas = null;
    }

    public void search() {
        renderedButton = Boolean.TRUE;
        System.out.println("entra al metodo? ");
        if (!identificacion.isEmpty() && nombreUsuario.isEmpty() && fechaInicio == null && fechaFin == null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=ciRucSolicitante:*" + identificacion + "*",
                    TareasHistoricas[].class, us.getToken());
        } else if (identificacion.isEmpty() && !nombreUsuario.isEmpty() && fechaInicio == null && fechaFin == null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=nombreSolicitante:*" + nombreUsuario + "*",
                    TareasHistoricas[].class, us.getToken());
        } else if (identificacion.isEmpty() && nombreUsuario.isEmpty() && fechaInicio != null && fechaFin == null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicio).replace("-", ""),
                    TareasHistoricas[].class, us.getToken());
        } else if (identificacion.isEmpty() && nombreUsuario.isEmpty() && fechaInicio != null && fechaFin != null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicio).replace("-", "") + " AND fechaInicioInt<" + format.format(fechaFin).replace("-", ""),
                    TareasHistoricas[].class, us.getToken());
        } else if (!identificacion.isEmpty() && nombreUsuario.isEmpty() && fechaInicio != null && fechaFin == null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicio).replace("-", "")
                    + " AND ciRucSolicitante:*" + identificacion + "*",
                    TareasHistoricas[].class, us.getToken());
        } else if (identificacion.isEmpty() && !nombreUsuario.isEmpty() && fechaInicio != null && fechaFin == null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=nombreSolicitante:*"
                    + nombreUsuario + "* AND fechaInicioInt>" + format.format(fechaInicio).replace("-", ""),
                    TareasHistoricas[].class, us.getToken());
        } else if (identificacion.isEmpty() && !nombreUsuario.isEmpty() && fechaInicio != null && fechaFin != null) {
            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=nombreSolicitante:*"
                    + nombreUsuario + "* AND fechaInicioInt>" + format.format(fechaInicio).replace("-", "")
                    + " AND fechaInicioInt<" + format.format(fechaFin).replace("-", ""),
                    TareasHistoricas[].class, us.getToken());
        } else if (!identificacion.isEmpty() && nombreUsuario.isEmpty() && fechaInicio != null && fechaFin != null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/" + estadoTramite + "/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicio).replace("-", "")
                    + " AND fechaInicioInt<" + format.format(fechaFin).replace("-", "")
                    + " AND ciRucSolicitante:*" + identificacion + "*",
                    TareasHistoricas[].class, us.getToken());
        } else {
            lazyTareas = null;
        }
    }

    public void searchTramiteFuncionario() {
        renderedButtonFuncionario = Boolean.TRUE;
        System.out.println("entra al metodo? ");
        if (!nombreUsuarioFuncionario.isEmpty() && fechaInicioFuncionario == null && fechaFinFuncionario == null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/historicas?search=(candidate:*"
                    + nombreUsuarioFuncionario + "* OR assignee:*" + nombreUsuarioFuncionario + "*)"
                    + " AND terminado:" + estadoTramiteFuncionario,
                    TareasHistoricas[].class, us.getToken());

        } else if (nombreUsuarioFuncionario.isEmpty() && fechaInicioFuncionario != null && fechaFinFuncionario == null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicioFuncionario).replace("-", "")
                    + " AND terminado:" + estadoTramiteFuncionario,
                    TareasHistoricas[].class, us.getToken());

        } else if (!nombreUsuarioFuncionario.isEmpty() && fechaInicioFuncionario != null && fechaFinFuncionario == null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicioFuncionario).replace("-", "")
                    + " AND (candidate:*" + nombreUsuarioFuncionario + "* OR assignee:*" + nombreUsuarioFuncionario + "*)"
                    + " AND terminado:" + estadoTramiteFuncionario,
                    TareasHistoricas[].class, us.getToken());

        } else if (!nombreUsuarioFuncionario.isEmpty() && fechaInicioFuncionario != null && fechaFinFuncionario != null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/historicas?search=(candidate:*"
                    + nombreUsuarioFuncionario + "* OR assignee:*" + nombreUsuarioFuncionario
                    + "*) AND fechaInicioInt>" + format.format(fechaInicioFuncionario).replace("-", "")
                    + " AND fechaInicioInt<" + format.format(fechaFinFuncionario).replace("-", "")
                    + " AND terminado:" + estadoTramiteFuncionario,
                    TareasHistoricas[].class, us.getToken());
        } else if (nombreUsuarioFuncionario.isEmpty() && fechaInicioFuncionario != null && fechaFinFuncionario != null) {

            lazyTareas = new LazyModelTareas<>(SisVars.ws + "tareas/historicas?search=fechaInicioInt>"
                    + format.format(fechaInicioFuncionario).replace("-", "")
                    + " AND fechaInicioInt<" + format.format(fechaFinFuncionario).replace("-", "")
                    + " AND terminado:" + estadoTramiteFuncionario,
                    TareasHistoricas[].class, us.getToken());
        } else {
            lazyTareas = null;
        }
    }

    public void selectUserFuncionario(User user) {
        nombreUsuarioFuncionario = user.getNombreUsuario();
        JsfUti.executeJS("PF('dialogFuncionarios').hide()");
        JsfUti.update("formMain");
    }

    public void selectSolicitante(Persona persona) {
        identificacion = persona.getIdentificacion();
        JsfUti.executeJS("PF('dialogSolicitantes').hide()");
        JsfUti.update("formMain");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModelWS<Persona> getCatEntes() {
        return catEntes;
    }

    public void setCatEntes(LazyModelWS<Persona> catEntes) {
        this.catEntes = catEntes;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaFinFuncionario() {
        return fechaFinFuncionario;
    }

    public void setFechaFinFuncionario(Date fechaFinFuncionario) {
        this.fechaFinFuncionario = fechaFinFuncionario;
    }

    public LazyModelWS<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(LazyModelWS<User> usuarios) {
        this.usuarios = usuarios;
    }

    public String getEstadoTramiteFuncionario() {
        return estadoTramiteFuncionario;
    }

    public void setEstadoTramiteFuncionario(String estadoTramiteFuncionario) {
        this.estadoTramiteFuncionario = estadoTramiteFuncionario;
    }

    public Boolean getRenderedButtonFuncionario() {
        return renderedButtonFuncionario;
    }

    public void setRenderedButtonFuncionario(Boolean renderedButtonFuncionario) {
        this.renderedButtonFuncionario = renderedButtonFuncionario;
    }

    public String getNombreUsuarioFuncionario() {
        return nombreUsuarioFuncionario;
    }

    public void setNombreUsuarioFuncionario(String nombreUsuarioFuncionario) {
        this.nombreUsuarioFuncionario = nombreUsuarioFuncionario;
    }

    public Date getFechaInicioFuncionario() {
        return fechaInicioFuncionario;
    }

    public void setFechaInicioFuncionario(Date fechaInicioFuncionario) {
        this.fechaInicioFuncionario = fechaInicioFuncionario;
    }

    public Boolean getRenderedButton() {
        return renderedButton;
    }

    public void setRenderedButton(Boolean renderedButton) {
        this.renderedButton = renderedButton;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public LazyModelTareas<TareasHistoricas> getLazyTareas() {
        return lazyTareas;
    }

    public void setLazyTareas(LazyModelTareas<TareasHistoricas> lazyTareas) {
        this.lazyTareas = lazyTareas;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getEstadoTramite() {
        return estadoTramite;
    }

    public void setEstadoTramite(String estadoTramite) {
        this.estadoTramite = estadoTramite;
    }
//</editor-fold>
}
