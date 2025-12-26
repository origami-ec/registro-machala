/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.models;

import java.util.List;

/**
 *
 * @author ORIGAMI
 */
public class IndexacionCampo {

    private String tipoDato;
    private String descripcion;
    private Boolean obligatorio;
    private List<String> categorias; //para cuando el tipo de dato sea categorico
    private String detalle; //campo que contiene la informacion  d la respuesta del campo 

    public IndexacionCampo() {
    }

    public IndexacionCampo(String tipoDato, String descripcion, List<String> categorias,Boolean obligatorio) {
        this.tipoDato = tipoDato;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.obligatorio = obligatorio;
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
