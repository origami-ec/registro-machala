package org.origami.ws.dto;

import org.origami.ws.entities.origami.TipoTramite;

public class AnalisisTramiteDTO {

    private TipoTramite tipoTramite;
    private Long pendiente;
    private Long finalizado;

    public AnalisisTramiteDTO() {
    }

    public AnalisisTramiteDTO(Long finalizado) {
        this.finalizado = finalizado;
    }

    public AnalisisTramiteDTO(TipoTramite tipoTramite, Long pendiente) {
        this.tipoTramite = tipoTramite;
        this.pendiente = pendiente;
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
        return "AnalisisTramiteDTO{" +
                "tipoTramite=" + tipoTramite +
                ", pendiente=" + pendiente +
                ", finalizado=" + finalizado +
                '}';
    }
}
