/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.validation.constraints.Size;

/**
 *
 * @author andysanchez
 */
@Entity
@Table(name = "regp_pronunciamiento_juridico", schema = "flow")
public class RegpPronunciamientoJuridico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "para", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CatEnte para;
    @Size(max = 200)
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Size(max = 2147483647)
    @Column(name = "asunto")
    private String asunto;
    @Size(max = 2147483647)
    @Column(name = "detalle")
    private String detalle;
    @Size(max = 200)
    @Column(name = "elaborado")
    private String elaborado;
    @Size(max = 200)
    @Column(name = "realizado")
    private String realizado;
    @Size(max = 200)
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites tramite;
    @Column(name = "fecha_retiro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRetiro;
    @Column(name = "fecha_reingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReingreso;
    @Column(name = "esta_retirado")
    private Boolean estaRetirado;
    @Column(name = "esta_reingresado")
    private Boolean estaReingresado;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "fecha_entrega_tramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntregaTramite;
    @JoinColumn(name = "quien_retira", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CatEnte quienRetira;
    @JoinColumn(name = "quien_reingresa", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CatEnte quienReingresa;
    @Column(name = "usuario_reingresa")
    private String usuarioReingresa;
    @Column(name = "usuario_mantiene_estado")
    private String usuarioMantieneEstado;
    @Column(name = "firma")
    private String firma;
    @Column(name = "firma_reingreso")
    private byte[] firmaReingreso;

    public RegpPronunciamientoJuridico() {
    }

    public RegpPronunciamientoJuridico(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatEnte getPara() {
        return para;
    }

    public void setPara(CatEnte para) {
        this.para = para;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getRealizado() {
        return realizado;
    }

    public void setRealizado(String realizado) {
        this.realizado = realizado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    public Boolean getEstaRetirado() {
        if (estaRetirado == null) {
            estaReingresado = Boolean.FALSE;
        }
        return estaRetirado;
    }

    public void setEstaRetirado(Boolean estaRetirado) {
        this.estaRetirado = estaRetirado;
    }

    public Boolean getEstaReingresado() {
        if (estaReingresado == null) {
            estaReingresado = Boolean.FALSE;
        }
        return estaReingresado;
    }

    public void setEstaReingresado(Boolean estaReingresado) {
        this.estaReingresado = estaReingresado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaEntregaTramite() {
        return fechaEntregaTramite;
    }

    public void setFechaEntregaTramite(Date fechaEntregaTramite) {
        this.fechaEntregaTramite = fechaEntregaTramite;
    }

    public CatEnte getQuienRetira() {
        return quienRetira;
    }

    public void setQuienRetira(CatEnte quienRetira) {
        this.quienRetira = quienRetira;
    }

    public CatEnte getQuienReingresa() {
        return quienReingresa;
    }

    public void setQuienReingresa(CatEnte quienReingresa) {
        this.quienReingresa = quienReingresa;
    }

    public String getUsuarioReingresa() {
        return usuarioReingresa;
    }

    public void setUsuarioReingresa(String usuarioReingresa) {
        this.usuarioReingresa = usuarioReingresa;
    }

    public String getUsuarioMantieneEstado() {
        return usuarioMantieneEstado;
    }

    public void setUsuarioMantieneEstado(String usuarioMantieneEstado) {
        this.usuarioMantieneEstado = usuarioMantieneEstado;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public byte[] getFirmaReingreso() {
        return firmaReingreso;
    }

    public void setFirmaReingreso(byte[] firmaReingreso) {
        this.firmaReingreso = firmaReingreso;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegpPronunciamientoJuridico other = (RegpPronunciamientoJuridico) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RegpPronunciamientoJuridico{");
        sb.append("id=").append(id);
        sb.append(", para=").append(para);
        sb.append(", numeroDocumento=").append(numeroDocumento);
        sb.append(", asunto=").append(asunto);
        sb.append(", detalle=").append(detalle);
        sb.append(", elaborado=").append(elaborado);
        sb.append(", realizado=").append(realizado);
        sb.append(", fecha=").append(fecha);
        sb.append(", fechaIngreso=").append(fechaIngreso);
        sb.append(", tramite=").append(tramite);
        sb.append(", fechaRetiro=").append(fechaRetiro);
        sb.append(", fechaReingreso=").append(fechaReingreso);
        sb.append(", estaRetirado=").append(estaRetirado);
        sb.append(", estaReingresado=").append(estaReingresado);
        sb.append(", observacion=").append(observacion);
        sb.append(", fechaEntregaTramite=").append(fechaEntregaTramite);
        sb.append(", quienRetira=").append(quienRetira);
        sb.append(", quienReingresa=").append(quienReingresa);
        sb.append(", usuarioReingresa=").append(usuarioReingresa);
        sb.append(", usuarioMantieneEstado=").append(usuarioMantieneEstado);
        sb.append(", firma=").append(firma);
        sb.append(", firmaReingreso=").append(firmaReingreso);
        sb.append('}');
        return sb.toString();
    }

}
