/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "firma_electronica", schema = "administrativo")
public class FirmaElectronica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "funcionario")
    private String funcionario;
    @Column(name = "uid")
    private String uid;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    //Certificado revocado - Certificado caducado  - Certificado emitido por entidad certificadora
    @Column(name = "estado_firma")
    private String estadoFirma; 
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    @Column(name = "isuser")
    private String isuser;
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "cn")
    private String cn;
    @Column(name = "emision")
    private String emision;
    @Column(name = "clave")
    private String clave;

    public FirmaElectronica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getIsuser() {
        return isuser;
    }

    public void setIsuser(String isuser) {
        this.isuser = isuser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final FirmaElectronica other = (FirmaElectronica) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FirmaElectronica{");
        sb.append("id=").append(id);
        sb.append(", funcionario=").append(funcionario);
        sb.append(", uid=").append(uid);
        sb.append(", estado=").append(estado);
        sb.append(", fechaCreacion=").append(fechaCreacion);
        sb.append(", estadoFirma=").append(estadoFirma);
        sb.append(", fechaEmision=").append(fechaEmision);
        sb.append(", fechaExpiracion=").append(fechaExpiracion);
        sb.append(", isuser=").append(isuser);
        sb.append(", ubicacion=").append(ubicacion);
        sb.append(", cn=").append(cn);
        sb.append(", emision=").append(emision);
        sb.append('}');
        return sb.toString();
    }

}
