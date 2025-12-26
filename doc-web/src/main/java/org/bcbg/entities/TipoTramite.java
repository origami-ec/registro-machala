/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author Anyelo
 */
public class TipoTramite implements Serializable {

    private Long id;
    private String descripcion;
    private String activitykey;
    private Boolean estado;
    private String archivoBpmn;
    private String abreviatura;
    private Departamento departamento;
    private Rol rol;
    private Integer dias;
    private Integer horas;
    private Integer minutos;
    private Integer segundos;
    private Boolean interno;
    private String urlImagen;
    private String definicionTramite;
    private String color;

    public TipoTramite(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public TipoTramite() {
        this.interno = Boolean.TRUE;
    }

    public TipoTramite(Boolean estado, String abreviatura) {
        this.estado = estado;
        this.abreviatura = abreviatura;
    }

    public TipoTramite(Departamento departamento) {
        this.estado = Boolean.TRUE;
        this.departamento = departamento;
    }

    public TipoTramite(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDefinicionTramite() {
        return definicionTramite;
    }

    public void setDefinicionTramite(String definicionTramite) {
        this.definicionTramite = definicionTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
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

    @Override
    public String toString() {
        return "TipoTramite{"
                + "id=" + id
                + ", descripcion=" + descripcion
                + ", activitykey=" + activitykey
                + ", estado=" + estado
                + ", archivoBpmn=" + archivoBpmn
                + ", abreviatura=" + abreviatura
                + ", departamento=" + departamento
                + ", rol=" + rol + ", definicionTramite=" + definicionTramite
                + '}';
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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
        if (!(object instanceof TipoTramite)) {
            return false;
        }
        TipoTramite other = (TipoTramite) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

}
