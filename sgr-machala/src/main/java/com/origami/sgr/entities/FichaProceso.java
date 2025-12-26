/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author eduar
 */
@Entity
@Table(name = "ficha_proceso", schema = "catastro")
public class FichaProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "reg_ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegFicha regFicha;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem estado;
    @Column(name = "vinculado")
    private Boolean vinculado = false;
    @Column(name = "revisado")
    private Boolean revisado = false;
    @JoinColumn(name = "unidad_medida", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem unidadMedida;
    @Column(name = "area")
    private BigDecimal area;
    @Column(name = "alicuota")
    private BigDecimal alicuota;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "usuario_control")
    private String usuarioControl;
    @Column(name = "fecha_control")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaControl;
    @Column(name = "usuario_supervisor")
    private String usuarioSupervisor;
    @Column(name = "fecha_supervision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSupervision;

    public FichaProceso() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegFicha getRegFicha() {
        return regFicha;
    }

    public void setRegFicha(RegFicha regFicha) {
        this.regFicha = regFicha;
    }

    public CtlgItem getEstado() {
        return estado;
    }

    public void setEstado(CtlgItem estado) {
        this.estado = estado;
    }

    public Boolean getVinculado() {
        return vinculado;
    }

    public void setVinculado(Boolean vinculado) {
        this.vinculado = vinculado;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public CtlgItem getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(CtlgItem unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioControl() {
        return usuarioControl;
    }

    public void setUsuarioControl(String usuarioControl) {
        this.usuarioControl = usuarioControl;
    }

    public Date getFechaControl() {
        return fechaControl;
    }

    public void setFechaControl(Date fechaControl) {
        this.fechaControl = fechaControl;
    }

    public String getUsuarioSupervisor() {
        return usuarioSupervisor;
    }

    public void setUsuarioSupervisor(String usuarioSupervisor) {
        this.usuarioSupervisor = usuarioSupervisor;
    }

    public Date getFechaSupervision() {
        return fechaSupervision;
    }

    public void setFechaSupervision(Date fechaSupervision) {
        this.fechaSupervision = fechaSupervision;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FichaProceso{");
        sb.append("id=").append(id);
        sb.append(", regFicha=").append(regFicha);
        sb.append(", estado=").append(estado);
        sb.append(", vinculado=").append(vinculado);
        sb.append(", revisado=").append(revisado);
        sb.append(", unidadMedida=").append(unidadMedida);
        sb.append(", area=").append(area);
        sb.append(", alicuota=").append(alicuota);
        sb.append(", usuarioCreacion=").append(usuarioCreacion);
        sb.append(", fechaCreacion=").append(fechaCreacion);
        sb.append(", usuarioModificacion=").append(usuarioModificacion);
        sb.append(", fechaModificacion=").append(fechaModificacion);
        sb.append('}');
        return sb.toString();
    }

}
