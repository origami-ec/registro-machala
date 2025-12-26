/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Date;
import jdk.jfr.Description;
import org.bcbg.util.Variables;

/**
 *
 * @author ORIGAMI1
 */
public class ServiciosDepartamento implements Serializable {

    @Description("Clave primaria de ServiciosDepartamento")
    private Long id;
    @Description(Variables.omitirCampo)
    private Long padreItem;
    @Description("Nombre del trámite")
    private String nombre;
    @Description("Abreviatura del trámite")
    private String abreviatura;
    @Description(Variables.omitirCampo)
    private String usuario;
    @Description(Variables.omitirCampo)
    private Date fecha;
    @Description(Variables.omitirCampo)
    private Boolean online;
    @Description(Variables.omitirCampo)
    private Boolean validar;
    @Description(Variables.omitirCampo)
    private Departamento departamento;
    @Description(Variables.omitirCampo)
    private TipoTramite tipoTramite;
    @Description(Variables.omitirCampo)
    private Integer diasRespuesta;
    @Description(Variables.omitirCampo)
    private String urlImagen;
    @Description(Variables.omitirCampo)
    private Integer hora;
    @Description(Variables.omitirCampo)
    private Integer minutos;
    @Description(Variables.omitirCampo)
    private Integer segundos;
    @Description(Variables.omitirCampo)
    private Boolean interno;
    @Description(Variables.omitirCampo)
    private Boolean estado;
    @Description(Variables.omitirCampo)
    private Boolean externo;

    public ServiciosDepartamento() {
        this.online = Boolean.FALSE;
        this.interno = Boolean.FALSE;
        this.externo = Boolean.FALSE;
    }

    public ServiciosDepartamento(Long id) {
        this.id = id;
    }

    public ServiciosDepartamento(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public ServiciosDepartamento(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public ServiciosDepartamento(Departamento departamento, TipoTramite tipoTramite) {
        this.departamento = departamento;
        this.tipoTramite = tipoTramite;
        this.estado = Boolean.TRUE;
    }

//    public ServiciosDepartamentoItem(ServiciosDepartamento serviciosDepartamento, TipoTramite tipoTramite) {
//        this.serviciosDepartamento = serviciosDepartamento;
//        this.tipoTramite = tipoTramite;
//    }
//
//    public ServiciosDepartamentoItem(ServiciosDepartamento serviciosDepartamento) {
//        this.serviciosDepartamento = serviciosDepartamento;
//    }
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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

//    public ServiciosDepartamento getServiciosDepartamento() {
//        return serviciosDepartamento;
//    }
//
//    public void setServiciosDepartamento(ServiciosDepartamento serviciosDepartamento) {
//        this.serviciosDepartamento = serviciosDepartamento;
//    }
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

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
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

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public ServiciosDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosDepartamento)) {
            return false;
        }
        ServiciosDepartamento other = (ServiciosDepartamento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "ServiciosDepartamentoItem{"
                + "id=" + id
                + ", padreItem=" + padreItem
                + ", nombre=" + nombre
                + ", usuario=" + usuario
                + ", fecha=" + fecha
                + ", online=" + online
                //                + ", serviciosDepartamento=" + serviciosDepartamento
                + ", tipoTramite=" + tipoTramite
                + '}';
    }

}
