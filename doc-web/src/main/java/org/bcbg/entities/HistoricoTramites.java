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
 * @author Anyelo
 */
public class HistoricoTramites implements Serializable {

    @Description("Clave primaria HistoricoTramites")
    private Long id;
    @Description("Número de trámite")
    private Long numTramite;
    @Description(Variables.omitirCampo)
    private String idProceso;
    @Description(Variables.omitirCampo)
    private String idProcesoTemp;
    @Description("Nombre solicitante del trámite")
    private String nombreSolicitante;
    @Description("Fecha del trámite")
    private Date fecha;
    @Description("Fecha de ingreso del trámite")
    private Date fechaIngreso;
    @Description("Fecha de entrega del trámite")
    private Date fechaEntrega;
    @Description(Variables.omitirCampo)
    private String carpetaRep;
    @Description(Variables.omitirCampo)
    private Boolean entregado;
    @Description(Variables.omitirCampo)
    private Persona solicitante;
    @Description(Variables.omitirCampo)
    private TipoTramite tipoTramite;
    @Description(Variables.omitirCampo)
    private Boolean blocked;
    @Description(Variables.omitirCampo)
    private Long userDesblock;
    @Description(Variables.omitirCampo)
    private Boolean documento;
    @Description(Variables.omitirCampo)
    private String ultimaTarea;
    @Description("Observación del trámite")
    private String observacion;
    @Description("Nombre de la tarea del trámite")
    private String tarea;
    @Description(Variables.omitirCampo)
    private String nameUser;
    @Description("Código del número de trámite")
    private String codigo;
    @Description(Variables.omitirCampo)
    private Boolean notificacion;
    @Description(Variables.omitirCampo)
    private Long secuencial;
    @Description(Variables.omitirCampo)
    private String correoNotificacion;

    public HistoricoTramites() {
    }

    public HistoricoTramites(String codigo) {
        this.codigo = codigo;
    }

    public HistoricoTramites(Long id, Long numTramite) {
        this.id = id;
        this.numTramite = numTramite;
    }

    public HistoricoTramites(String idProceso, String idProcesoTemp) {
        this.idProceso = idProceso;
        this.idProcesoTemp = idProcesoTemp;
    }

    public HistoricoTramites(Long id) {
        this.id = id;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    public Persona getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Persona solicitante) {
        this.solicitante = solicitante;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Long getUserDesblock() {
        return userDesblock;
    }

    public void setUserDesblock(Long userDesblock) {
        this.userDesblock = userDesblock;
    }

    public Boolean getDocumento() {
        return documento;
    }

    public void setDocumento(Boolean documento) {
        this.documento = documento;
    }

    public String getUltimaTarea() {
        return ultimaTarea;
    }

    public void setUltimaTarea(String ultimaTarea) {
        this.ultimaTarea = ultimaTarea;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }

    public Long getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Long secuencial) {
        this.secuencial = secuencial;
    }

    public Integer getEstadoTramite() {
        if (ultimaTarea == null) {
            return 0;
        }
        if (ultimaTarea.toUpperCase().contains("FIRMA")) {
            return 1;
        }
        if (ultimaTarea.toUpperCase().contains("FINALIZADO")) {
            return 2;
        }
        if (ultimaTarea.toUpperCase().contains("ENTREGA")) {
            return 1;
        }
        if (ultimaTarea.toUpperCase().contains("NOTA DEVOLU")) {
            return 1;
        }
        if (ultimaTarea.toUpperCase().startsWith("APROBADO")) {
            return 1;
        }
        if (ultimaTarea.toUpperCase().contains("retira los documento".toUpperCase())) {
            return 2;
        }
        return 0;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoTramites)) {
            return false;
        }
        HistoricoTramites other = (HistoricoTramites) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "HistoricoTramites{" + "id=" + id + ", numTramite=" + numTramite + ", idProceso=" + idProceso
                + ", idProcesoTemp=" + idProcesoTemp + ", nombreSolicitante=" + nombreSolicitante + ", fecha=" + fecha
                + ", carpetaRep=" + carpetaRep + ", entregado=" + entregado + ", solicitante=" + solicitante
                + ", tipoTramite=" + tipoTramite + ", blocked=" + blocked + ", userDesblock=" + userDesblock + ", documento=" + documento
                + ", ultimaTarea=" + ultimaTarea + ", observacion=" + observacion + ", tarea=" + tarea + ", nameUser=" + nameUser + '}';
    }

}
