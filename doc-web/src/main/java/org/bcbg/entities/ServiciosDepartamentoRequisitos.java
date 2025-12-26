/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;

/**
 *
 * @author OrigamiEC
 */
public class ServiciosDepartamentoRequisitos implements Serializable {

    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean requerido;
    private Boolean estado;
    private String formato_archivo;
    private ServiciosDepartamento serviciosDepartamento;
    private TipoTramite tipoTramite;
    private Long tamanioArchivo;
    private String tamanio;
    private Integer orden;
    private String urlDocumento;
    private String idOrigamiDocs;

    public ServiciosDepartamentoRequisitos() {
        this.estado = Boolean.TRUE;
        this.requerido = Boolean.FALSE;
    }

    public ServiciosDepartamentoRequisitos(ServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }

    public ServiciosDepartamentoRequisitos(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public ServiciosDepartamentoRequisitos(Long id) {
        this.id = id;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public Long getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Long tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getFormato_archivo() {
        return formato_archivo;
    }

    public void setFormato_archivo(String formato_archivo) {
        this.formato_archivo = formato_archivo;
    }

    public ServiciosDepartamento getServiciosDepartamento() {
        return serviciosDepartamento;
    }

    public void setServiciosDepartamento(ServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getUrlDocumento() {
        return urlDocumento;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public String getIdOrigamiDocs() {
        return idOrigamiDocs;
    }

    public void setIdOrigamiDocs(String idOrigamiDocs) {
        this.idOrigamiDocs = idOrigamiDocs;
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
        if (!(object instanceof ServiciosDepartamentoRequisitos)) {
            return false;
        }
        ServiciosDepartamentoRequisitos other = (ServiciosDepartamentoRequisitos) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "ServiciosDepartamentoRequisitos{" + "id=" + id + ", nombre=" + nombre
                + ", descripcion=" + descripcion + ", requerido=" + requerido + ", estado=" + estado
                + ", formato_archivo=" + formato_archivo + ", serviciosDepartamento=" + serviciosDepartamento
                + ", tipoTramite=" + tipoTramite + ", tamanioArchivo=" + tamanioArchivo + '}';
    }

}
