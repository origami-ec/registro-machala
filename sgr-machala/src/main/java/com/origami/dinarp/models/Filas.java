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
public class Filas implements Serializable {

    protected List<Fila> fila;

    public Filas() {
    }

    public List<Fila> getFila() {
        return fila;
    }

    public void setFila(List<Fila> fila) {
        this.fila = fila;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filas{");
        sb.append("fila=").append(fila);
        sb.append('}');
        return sb.toString();
    }

}
