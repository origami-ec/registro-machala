package org.origami.ws.dto;

import java.util.Date;

public class ServiciosDepartamentoItemsDTO {

    private Long id;
    private Long padreItem;
    private String nombre;
    private String clasificacion;
    private String usuario;
    private Date fecha;
    private Boolean online;
    private Integer diasRespuesta;
    private Boolean validar;
    private DepartamentoDTO departamento;
    private String urlImagen;
    private Integer hora;
    private Integer minutos;
    private Integer segundos;

    public ServiciosDepartamentoItemsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPadreItem() {
        return padreItem;
    }

    public void setPadreItem(Long padreItem) {
        this.padreItem = padreItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
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

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
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

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }

    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }
}
