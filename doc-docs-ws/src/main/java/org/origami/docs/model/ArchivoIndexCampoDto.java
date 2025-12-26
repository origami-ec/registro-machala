package org.origami.docs.model;

import java.util.List;

public class ArchivoIndexCampoDto {
    private String tipoDato;
    private String descripcion;
    private List<String> categorias; //para cuando el tipo de dato sea categorico
    private String detalle; //campo que contiene la informacion  d la respuesta del campo
    private Boolean obligatorio;

    public ArchivoIndexCampoDto() {
    }

    public ArchivoIndexCampoDto(String tipoDato, String descripcion, List<String> categorias, String detalle) {
        this.tipoDato = tipoDato;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.detalle = detalle;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }
}
