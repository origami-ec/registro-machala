/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.TipoTramite;

/**
 *
 * @author Ricardo
 */
public class AnalisisTramite {

    private TipoTramite tipoTramite;
    private Long pendiente;
    private Long finalizado;
    private String nombreUsuario;

    public AnalisisTramite() {
    }

    public AnalisisTramite(TipoTramite tipoTramite, Long pendiente, Long finalizado) {
        this.tipoTramite = tipoTramite;
        this.pendiente = pendiente;
        this.finalizado = finalizado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Long getPendiente() {
        return pendiente;
    }

    public void setPendiente(Long pendiente) {
        this.pendiente = pendiente;
    }

    public Long getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Long finalizado) {
        this.finalizado = finalizado;
    }

    @Override
    public String toString() {
        return "Analisis{" + "tipoTramite=" + tipoTramite + ", pendiente=" + pendiente + ", finalizado=" + finalizado + '}';
    }
}
