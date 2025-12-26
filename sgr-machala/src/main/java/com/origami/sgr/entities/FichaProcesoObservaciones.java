/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
@Table(name = "ficha_proceso_observaciones", schema = "catastro")
public class FichaProcesoObservaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ficha_proceso", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FichaProceso fichaProceso;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public FichaProcesoObservaciones() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FichaProceso getFichaProceso() {
        return fichaProceso;
    }

    public void setFichaProceso(FichaProceso fichaProceso) {
        this.fichaProceso = fichaProceso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final FichaProcesoObservaciones other = (FichaProcesoObservaciones) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FichaProcesoObservaciones{");
        sb.append("id=").append(id);
        sb.append(", fichaProceso=").append(fichaProceso);
        sb.append(", observacion=").append(observacion);
        sb.append(", usuario=").append(usuario);
        sb.append(", fecha=").append(fecha);
        sb.append('}');
        return sb.toString();
    }

}
