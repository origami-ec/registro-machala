/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import org.bcbg.documental.models.Notificacion;
import java.io.Serializable;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class RequisitosErrores implements Serializable {

    private Long id;
    private Long idRequisito;
    private Long idSolicitudVentanilla;
    private Boolean subido;
    private ServiciosDepartamentoRequisitos requisito;
    private Long idNotificacion;
    private Notificacion notificacion;
    private Boolean faltante;

    public RequisitosErrores() {
        faltante = Boolean.FALSE;
    }

    public Boolean getFaltante() {
        return faltante;
    }

    public void setFaltante(Boolean faltante) {
        this.faltante = faltante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRequisito() {
        return idRequisito;
    }

    public void setIdRequisito(Long idRequisito) {
        this.idRequisito = idRequisito;
    }

    public Long getIdSolicitudVentanilla() {
        return idSolicitudVentanilla;
    }

    public void setIdSolicitudVentanilla(Long idSolicitudVentanilla) {
        this.idSolicitudVentanilla = idSolicitudVentanilla;
    }

    public Boolean getSubido() {
        return subido;
    }

    public void setSubido(Boolean subido) {
        this.subido = subido;
    }

    public ServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    @Override
    public String toString() {
        return "RequisitosErrores{" + "id=" + id + ", idRequisito=" + idRequisito + ", idSolicitudVentanilla=" + idSolicitudVentanilla + ", subido=" + subido + ", requisito=" + requisito.getId() + ", idNotificacion=" + idNotificacion + ", notificacion=" + notificacion.getId() + '}';
    }

}
