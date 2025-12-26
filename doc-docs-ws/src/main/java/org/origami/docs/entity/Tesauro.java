package org.origami.docs.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "tesauro")
public class Tesauro {

    @Id
    public String _id;
    private String palabra;
    private Boolean estado;
    private Date fecha;
    private Usuario usuario;
    private Usuario usuarioEdita;
    private List<String> sugerencias;

    public Tesauro() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public List<String> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(List<String> sugerencias) {
        this.sugerencias = sugerencias;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioEdita() {
        return usuarioEdita;
    }

    public void setUsuarioEdita(Usuario usuarioEdita) {
        this.usuarioEdita = usuarioEdita;
    }
}
