/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.dto;

import java.io.Serializable;

/**
 *
 * @author Origami
 */
public class TipoDatoCompuestoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoDatoDTO tipoDato;
    private String valor;
    private String descripcion;
    private String estado;
    private Integer orden;

    public TipoDatoCompuestoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDatoDTO getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDatoDTO tipoDato) {
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

}
