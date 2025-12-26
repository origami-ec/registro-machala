/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
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
 * @author Anyelo
 */
@Entity
@Table(name = "codigos_ficha", schema = "catastro")
public class CodigosFicha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "ficha", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RegFicha ficha;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CtlgItem tipo;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "codigo")
    private String codigo;
    @JoinColumn(name = "urb", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Barrios urb;
    @Column(name = "etapa")
    private String etapa;
    @Column(name = "mz")
    private String mz;
    @Column(name = "villa")
    private String villa;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "edificio")
    private String edificio;
    @Column(name = "piso")
    private String piso;
    @Column(name = "departamento")
    private String departamento;
    @Column(name = "oficina")
    private String oficina;
    @Column(name = "nro_local")
    private String local;
    @Column(name = "parqueo")
    private String parqueo;
    @Column(name = "townhouse")
    private String townhouse;

    public CodigosFicha() {
    }

    public String getCodigoCompleto() {
        String temp;
        if (this.tipo.getCodename().equalsIgnoreCase("URBANISTICO")) {
            temp = urb.getNombre();
            if (etapa != null && !etapa.isEmpty()) {
                temp = temp + "; Etapa: " + etapa;
            }
            if (mz != null && !mz.isEmpty()) {
                temp = temp + "; Mz: " + mz;
            }
            if (villa != null && !villa.isEmpty()) {
                temp = temp + "; Lote: " + villa;
            }
            if (edificio != null && !edificio.isEmpty()) {
                temp = temp + "; Edificio: " + edificio;
            }
            if (piso != null && !piso.isEmpty()) {
                temp = temp + "; Piso: " + piso;
            }
            if (departamento != null && !departamento.isEmpty()) {
                temp = temp + "; Departamento: " + departamento;
            }
            if (oficina != null && !oficina.isEmpty()) {
                temp = temp + "; Oficina: " + oficina;
            }
            if (local != null && !local.isEmpty()) {
                temp = temp + "; Local: " + local;
            }
            if (referencia != null && !referencia.isEmpty()) {
                temp = temp + "; Referencia: " + referencia;
            }
            if (parqueo != null && !parqueo.isEmpty()) {
                temp = temp + "; Parqueo: " + parqueo;
            }
            if (townhouse != null && !townhouse.isEmpty()) {
                temp = temp + "; Townhouse: " + townhouse;
            }
        } else {
            temp = codigo;
        }
        return temp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public CtlgItem getTipo() {
        return tipo;
    }

    public void setTipo(CtlgItem tipo) {
        this.tipo = tipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Barrios getUrb() {
        return urb;
    }

    public void setUrb(Barrios urb) {
        this.urb = urb;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getVilla() {
        return villa;
    }

    public void setVilla(String villa) {
        this.villa = villa;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getParqueo() {
        return parqueo;
    }

    public void setParqueo(String parqueo) {
        this.parqueo = parqueo;
    }

    public String getTownhouse() {
        return townhouse;
    }

    public void setTownhouse(String townhouse) {
        this.townhouse = townhouse;
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
        if (!(object instanceof CodigosFicha)) {
            return false;
        }
        CodigosFicha other = (CodigosFicha) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.CodigosFicha[ id=" + id + " ]";
    }
}
