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
public class Fila implements Serializable {

    protected Columnas columnas;

    public Fila() {
    }

    public Columnas getColumnas() {
        return columnas;
    }

    public void setColumnas(Columnas columnas) {
        this.columnas = columnas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fila{");
        sb.append("columnas=").append(columnas);
        sb.append('}');
        return sb.toString();
    }

}
