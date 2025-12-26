package org.origami.ws.dto;

import java.util.Date;

public class ServiciosDepartamentoDTO {

    private Long id;
    private String nombre;
    private String usuario;
    private Date fecha;
    private Boolean online;
    private DepartamentoDTO departamento;
    private String urlImagen;
    private Boolean estado;
    private Boolean interno;
    private Boolean externo;

    public ServiciosDepartamentoDTO() {
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public Boolean getExterno() {
        return externo;
    }

    public void setExterno(Boolean externo) {
        this.externo = externo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
