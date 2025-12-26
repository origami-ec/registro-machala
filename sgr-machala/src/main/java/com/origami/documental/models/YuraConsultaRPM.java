/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eduar
 */
public class YuraConsultaRPM implements Serializable {

    private Integer numeroRepertorio;
    private Integer anioRepertorio;
    private Integer numeroInscripcion;
    private String fechaInscripcion;
    private String libro;
    private String tomo;
    private String identificacion1;
    private String identificacion2;

    public YuraConsultaRPM() {
    }

    public Integer getNumeroRepertorio() {
        return numeroRepertorio;
    }

    public void setNumeroRepertorio(Integer numeroRepertorio) {
        this.numeroRepertorio = numeroRepertorio;
    }

    public Integer getAnioRepertorio() {
        return anioRepertorio;
    }

    public void setAnioRepertorio(Integer anioRepertorio) {
        this.anioRepertorio = anioRepertorio;
    }

    public Integer getNumeroInscripcion() {
        return numeroInscripcion;
    }

    public void setNumeroInscripcion(Integer numeroInscripcion) {
        this.numeroInscripcion = numeroInscripcion;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getIdentificacion1() {
        return identificacion1;
    }

    public void setIdentificacion1(String identificacion1) {
        this.identificacion1 = identificacion1;
    }

    public String getIdentificacion2() {
        return identificacion2;
    }

    public void setIdentificacion2(String identificacion2) {
        this.identificacion2 = identificacion2;
    }

    public Map<String, Object> toHashMap() throws IllegalAccessException {
        Map<String, Object> mapa = new HashMap<>();
        Field[] campos = this.getClass().getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);
            mapa.put(campo.getName(), campo.get(this));
        }
        return mapa;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("YuraConsultaRPM{");
        sb.append("numeroRepertorio=").append(numeroRepertorio);
        sb.append(", anioRepertorio=").append(anioRepertorio);
        sb.append(", numeroInscripcion=").append(numeroInscripcion);
        sb.append(", fechaInscripcion=").append(fechaInscripcion);
        sb.append(", libro=").append(libro);
        sb.append(", tomo=").append(tomo);
        sb.append(", identificacion1=").append(identificacion1);
        sb.append(", identificacion2=").append(identificacion2);
        sb.append('}');
        return sb.toString();
    }

}
