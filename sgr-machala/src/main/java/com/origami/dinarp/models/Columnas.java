/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.dinarp.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduar
 */
public class Columnas implements Serializable {

    protected List<Columna> columna;

    public Columnas() {
    }

    public List<Columna> getColumna() {
        return columna;
    }

    public void setColumna(List<Columna> columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Columnas{");
        sb.append("columna=").append(columna);
        sb.append('}');
        return sb.toString();
    }

}
