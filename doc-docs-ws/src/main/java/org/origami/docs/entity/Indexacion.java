package org.origami.docs.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "indexacion")
public class Indexacion {
    @Id
    public String _id;
    private String descripcion;
    private Boolean estado;
    private String fecha;
    private Usuario usuario;
    private Usuario usuarioEdita;
    private List<IndexacionCampo> campos;

    public Indexacion() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public List<IndexacionCampo> getCampos() {
        return campos;
    }

    public void setCampos(List<IndexacionCampo> campos) {
        this.campos = campos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Usuario getUsuarioEdita() {
        return usuarioEdita;
    }

    public void setUsuarioEdita(Usuario usuarioEdita) {
        this.usuarioEdita = usuarioEdita;
    }
}
