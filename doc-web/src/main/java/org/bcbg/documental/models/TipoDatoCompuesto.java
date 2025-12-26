/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.models;

import java.io.Serializable;

/**
 *
 * @author Origami
 */
public class TipoDatoCompuesto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoDato tipoDato;
    private String valor;
    private String descripcion;
    private String estado;
    private Integer orden;

    public TipoDatoCompuesto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "TipoDatoCompuesto{" + "id=" + id + ", tipoDato=" + tipoDato + ", valor=" + valor + ", descripcion=" + descripcion + ", estado=" + estado + ", orden=" + orden + '}';
    }

}
