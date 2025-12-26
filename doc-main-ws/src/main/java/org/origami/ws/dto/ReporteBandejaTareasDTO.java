package org.origami.ws.dto;

import java.io.Serializable;
import java.util.Date;

public class ReporteBandejaTareasDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer numTramite;
    private String idProcesoTemp;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private String nombreSolicitante;
    private String name_;
    private String codigo;
    private String descripcion;

    public ReporteBandejaTareasDTO() {
    }

    public Integer getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Integer numTramite) {
        this.numTramite = numTramite;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
