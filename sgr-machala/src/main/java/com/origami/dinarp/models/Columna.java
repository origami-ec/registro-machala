/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.dinarp.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class Columna implements Serializable {

    protected String campo;
    protected String valor;

    public Columna() {
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Columna{");
        sb.append("campo=").append(campo);
        sb.append(", valor=").append(valor);
        sb.append('}');
        return sb.toString();
    }

}
