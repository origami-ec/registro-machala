package org.origami.docs.model;

import org.origami.docs.entity.Usuario;

public class NotaDto {

    private String titulo;
    private String nota;
    private String tipo;
    private String fecha;
    private Usuario usuario;
    private Integer ancho;
    private Integer alto;
    private Integer posicionX1;
    private Integer posicionX2;
    private Integer posicionY1;
    private Integer posicionY2;
    private String color;

    public NotaDto() {
    }


    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getAncho() {
        return ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getAlto() {
        return alto;
    }

    public void setAlto(Integer alto) {
        this.alto = alto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(Integer posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public Integer getPosicionX2() {
        return posicionX2;
    }

    public void setPosicionX2(Integer posicionX2) {
        this.posicionX2 = posicionX2;
    }

    public Integer getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(Integer posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public Integer getPosicionY2() {
        return posicionY2;
    }

    public void setPosicionY2(Integer posicionY2) {
        this.posicionY2 = posicionY2;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "NotaDto{" +
                "titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                ", fecha='" + fecha + '\'' +
                ", usuario=" + usuario +
                ", ancho=" + ancho +
                ", alto=" + alto +
                ", posicionX1=" + posicionX1 +
                ", posicionX2=" + posicionX2 +
                ", posicionY1=" + posicionY1 +
                ", posicionY2=" + posicionY2 +
                ", color='" + color + '\'' +
                '}';
    }
}
