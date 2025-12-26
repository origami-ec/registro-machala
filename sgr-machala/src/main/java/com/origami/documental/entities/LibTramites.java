/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "lib_tramites", schema = "public")
public class LibTramites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id_gab", nullable = false)
    private Long idGab;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tran", nullable = false)
    private Long idTran;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "fecha_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsulta;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 7)
    @Column(name = "paginas")
    private String paginas;
    @Size(max = 50)
    @Column(name = "peso")
    private String peso;
    @Size(max = 5)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "version")
    private Long version;
    @Size(max = 1)
    @Column(name = "status")
    private String status;
    /*@Column(name = "imagen")
    private byte[] imagen;*/
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "numero_de_tramite")
    private Long tramite;
    @Column(name = "fecha_ingreso_tramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "observacion")
    private String observacion;

    public LibTramites() {
    }

    public Long getIdGab() {
        return idGab;
    }

    public void setIdGab(Long idGab) {
        this.idGab = idGab;
    }

    public Long getIdTran() {
        return idTran;
    }

    public void setIdTran(Long idTran) {
        this.idTran = idTran;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }*/

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Long getTramite() {
        return tramite;
    }

    public void setTramite(Long tramite) {
        this.tramite = tramite;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "LibTramites{" + "idGab=" + idGab
                + ", idTran=" + idTran
                + ", fechaCreacion=" + fechaCreacion
                + ", fechaModificacion=" + fechaModificacion
                + ", fechaConsulta=" + fechaConsulta
                + ", usuario=" + usuario
                + ", paginas=" + paginas
                + ", peso=" + peso
                + ", tipo=" + tipo
                + ", version=" + version
                + ", status=" + status
                + ", contenido=" + contenido
                + ", tramite=" + tramite
                + ", fechaIngreso=" + fechaIngreso
                + ", observacion=" + observacion + '}';
    }

}
