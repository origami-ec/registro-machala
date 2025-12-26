package org.origami.ws.dto;

public class TipoTramitePublicDTO {

    private Long id;
    private String descripcion;
    private Boolean estado;
    private String abreviatura;
    private String urlImagen;
    private String definicionTramite;
    private String color;

    public TipoTramitePublicDTO() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDefinicionTramite() {
        return definicionTramite;
    }

    public void setDefinicionTramite(String definicionTramite) {
        this.definicionTramite = definicionTramite;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
