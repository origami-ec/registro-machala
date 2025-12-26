/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.dinarp.models;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Base64;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author eduar
 */
public class RespuestaDinarp implements Serializable {

    // Datos Demográficos (Registro Civil)
    // codigo paquete: 5362
    private String actaDefuncion;
    private String anioInscripcionNacimiento;
    private String cedula;
    private String condicionCiudadano;
    private String conyuge;
    private String estadoCivil;
    private String fechaDefuncion;
    private String fechaInscripcionDefuncion;
    private String fechaMatrimonio;
    private String fechaNacimiento;
    private String individualDactilar;
    private String nombre;
    private String nombreMadre;
    private String nombrePadre;
    private String profesion;

    // Datos Biométrico (Registro Civil)
    // codigo paquete: 5470
    private String firma;
    private String foto;

    public RespuestaDinarp() {
    }

    public StreamedContent getFotoCedula() {
        try {
            if (foto != null) {
                byte[] imagenBytes = Base64.getDecoder().decode(foto);
                return DefaultStreamedContent.builder()
                        .contentType("image/jpeg")
                        .stream(() -> new ByteArrayInputStream(imagenBytes))
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public StreamedContent getImagenFirma() {
        try {
            if (firma != null) {
                byte[] imagenBytes = Base64.getDecoder().decode(firma);
                return DefaultStreamedContent.builder()
                        .contentType("image/jpeg")
                        .stream(() -> new ByteArrayInputStream(imagenBytes))
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String getActaDefuncion() {
        return actaDefuncion;
    }

    public void setActaDefuncion(String actaDefuncion) {
        this.actaDefuncion = actaDefuncion;
    }

    public String getAnioInscripcionNacimiento() {
        return anioInscripcionNacimiento;
    }

    public void setAnioInscripcionNacimiento(String anioInscripcionNacimiento) {
        this.anioInscripcionNacimiento = anioInscripcionNacimiento;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getConyuge() {
        return conyuge;
    }

    public void setConyuge(String conyuge) {
        this.conyuge = conyuge;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public String getFechaInscripcionDefuncion() {
        return fechaInscripcionDefuncion;
    }

    public void setFechaInscripcionDefuncion(String fechaInscripcionDefuncion) {
        this.fechaInscripcionDefuncion = fechaInscripcionDefuncion;
    }

    public String getFechaMatrimonio() {
        return fechaMatrimonio;
    }

    public void setFechaMatrimonio(String fechaMatrimonio) {
        this.fechaMatrimonio = fechaMatrimonio;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getIndividualDactilar() {
        return individualDactilar;
    }

    public void setIndividualDactilar(String individualDactilar) {
        this.individualDactilar = individualDactilar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreMadre() {
        return nombreMadre;
    }

    public void setNombreMadre(String nombreMadre) {
        this.nombreMadre = nombreMadre;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RespuestaDinarp{");
        sb.append("actaDefuncion=").append(actaDefuncion);
        sb.append(", anioInscripcionNacimiento=").append(anioInscripcionNacimiento);
        sb.append(", cedula=").append(cedula);
        sb.append(", condicionCiudadano=").append(condicionCiudadano);
        sb.append(", conyuge=").append(conyuge);
        sb.append(", estadoCivil=").append(estadoCivil);
        sb.append(", fechaDefuncion=").append(fechaDefuncion);
        sb.append(", fechaInscripcionDefuncion=").append(fechaInscripcionDefuncion);
        sb.append(", fechaMatrimonio=").append(fechaMatrimonio);
        sb.append(", fechaNacimiento=").append(fechaNacimiento);
        sb.append(", individualDactilar=").append(individualDactilar);
        sb.append(", nombre=").append(nombre);
        sb.append(", nombreMadre=").append(nombreMadre);
        sb.append(", nombrePadre=").append(nombrePadre);
        sb.append(", profesion=").append(profesion);
        sb.append(", firma=").append(firma);
        sb.append(", foto=").append(foto);
        sb.append('}');
        return sb.toString();
    }

}
