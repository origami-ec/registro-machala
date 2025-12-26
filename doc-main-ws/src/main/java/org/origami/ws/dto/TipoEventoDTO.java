package org.origami.ws.dto;

import java.util.Date;

public class TipoEventoDTO {
    private Long id;
    private String descripcion;
    private String color;
    private Boolean req_doc;
    private String url;
    private Date fecCre;
    private Date fecAct;
    private Boolean estado;

    public TipoEventoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getReq_doc() {
        return req_doc;
    }

    public void setReq_doc(Boolean req_doc) {
        this.req_doc = req_doc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public Date getFecAct() {
        return fecAct;
    }

    public void setFecAct(Date fecAct) {
        this.fecAct = fecAct;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "TipoEventoDTO{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", color='" + color + '\'' +
                ", req_doc=" + req_doc +
                ", url='" + url + '\'' +
                ", fecCre=" + fecCre +
                ", fecAct=" + fecAct +
                ", estado=" + estado +
                '}';
    }
}
