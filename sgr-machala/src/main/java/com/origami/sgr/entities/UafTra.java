/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author dfcalderio
 */
@Entity
@Table(name = "uaf_tra", schema = "ctlg")
@NamedQueries({
    @NamedQuery(name = "UafTra.findAll", query = "SELECT u FROM UafTra u"),
    @NamedQuery(name = "UafTra.findById", query = "SELECT u FROM UafTra u WHERE u.id = :id"),
    @NamedQuery(name = "UafTra.findByMovimiento", query = "SELECT u FROM UafTra u WHERE u.movimiento.id = :idMovimiento AND u.periodo = :periodo"),
    @NamedQuery(name = "UafTra.findByNumRepertorio", query = "SELECT u FROM UafTra u WHERE u.numRepertorio = :numRepertorio"),
    @NamedQuery(name = "UafTra.findByNumInscripcion", query = "SELECT u FROM UafTra u WHERE u.numInscripcion = :numInscripcion"),
    @NamedQuery(name = "UafTra.findByFechaInscripcion", query = "SELECT u FROM UafTra u WHERE u.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "UafTra.findByPeriodo", query = "SELECT u FROM UafTra u WHERE u.periodo = :periodo AND u.registro = :tipo"),
    @NamedQuery(name = "UafTra.findByNit", query = "SELECT u FROM UafTra u WHERE u.nit = :nit"),
    @NamedQuery(name = "UafTra.findByFcr", query = "SELECT u FROM UafTra u WHERE u.fcr = :fcr"),
    @NamedQuery(name = "UafTra.findByDtm", query = "SELECT u FROM UafTra u WHERE u.dtm = :dtm"),
    @NamedQuery(name = "UafTra.findByCca", query = "SELECT u FROM UafTra u WHERE u.cca = :cca"),
    @NamedQuery(name = "UafTra.findByVcc", query = "SELECT u FROM UafTra u WHERE u.vcc = :vcc"),
    @NamedQuery(name = "UafTra.findByTtb", query = "SELECT u FROM UafTra u WHERE u.ttb = :ttb"),
    @NamedQuery(name = "UafTra.findByDrb", query = "SELECT u FROM UafTra u WHERE u.drb = :drb")})
public class UafTra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_repertorio")
    private Integer numRepertorio;
    @Column(name = "num_inscripcion")
    private Integer numInscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "libro")
    private String libro;
    @Column(name = "acto")
    private String acto;
    @Column(name = "nit")
    private String nit;
    @Column(name = "fcr")
    private String fcr;
    @Column(name = "dtm")
    private String dtm;
    @Column(name = "cca")
    private String cca;
    @Column(name = "vcc")
    private String vcc;
    @Column(name = "ttb")
    private String ttb = "OTR";
    @Column(name = "drb")
    private String drb;
    @Column(name = "seleccionado")
    private boolean seleccionado;
    @Column(name = "registro")
    private boolean registro;
    @JoinColumn(name = "uaf_tramite", referencedColumnName = "id")
    @ManyToOne
    private UafTramite uafTramite;
    @JoinColumn(name = "movimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegMovimiento movimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uafTra", fetch = FetchType.LAZY)
    private Collection<UafInt> uafNitList;

    @JoinColumn(name = "ficha", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegFicha ficha;

    @Column(name = "valor_debito")
    private BigDecimal valorDebito = new BigDecimal("0.00");
    @Column(name = "valor_credito")
    private BigDecimal valorCredito = new BigDecimal("0.00");
    @Column(name = "valor_efectivo")
    private BigDecimal valorEfectivo = new BigDecimal("0.00");
    @Column(name = "valor_cheque")
    private BigDecimal valorCheque = new BigDecimal("0.00");
    @Column(name = "valor_bienes")
    private BigDecimal valorBienes = new BigDecimal("0.00");
    @Column(name = "valor_servicios")
    private BigDecimal valorServicios = new BigDecimal("0.00");
    @Column(name = "valor_tcredito")
    private BigDecimal valorTcredito = new BigDecimal("0.00");
    @Column(name = "total")
    private BigDecimal total = new BigDecimal("0.00");

    @Transient
    private String codigo;

    public UafTra() {
        this.seleccionado = true;
    }

    public UafTra(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public Integer getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(Integer numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getFcr() {
        return fcr;
    }

    public void setFcr(String fcr) {
        this.fcr = fcr;
    }

    public String getDtm() {
        return dtm;
    }

    public void setDtm(String dtm) {
        this.dtm = dtm;
    }

    public String getCca() {
        return cca;
    }

    public void setCca(String cca) {
        this.cca = cca;
    }

    public String getVcc() {
        return vcc;
    }

    public void setVcc(String vcc) {
        this.vcc = vcc;
    }

    public String getTtb() {
        return ttb;
    }

    public void setTtb(String ttb) {
        this.ttb = ttb;
    }

    public String getDrb() {
        return drb;
    }

    public void setDrb(String drb) {
        this.drb = drb;
    }

    public Collection<UafInt> getUafNitList() {
        return uafNitList;
    }

    public void setUafNitList(Collection<UafInt> uafNitList) {
        this.uafNitList = uafNitList;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public UafTramite getUafTramite() {
        return uafTramite;
    }

    public void setUafTramite(UafTramite uafTramite) {
        this.uafTramite = uafTramite;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public boolean isRegistro() {
        return registro;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public BigDecimal getValorDebito() {
        return valorDebito;
    }

    public void setValorDebito(BigDecimal valorDebito) {
        this.valorDebito = valorDebito;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public BigDecimal getValorEfectivo() {
        return valorEfectivo;
    }

    public void setValorEfectivo(BigDecimal valorEfectivo) {
        this.valorEfectivo = valorEfectivo;
    }

    public BigDecimal getValorCheque() {
        return valorCheque;
    }

    public void setValorCheque(BigDecimal valorCheque) {
        this.valorCheque = valorCheque;
    }

    public BigDecimal getValorBienes() {
        return valorBienes;
    }

    public void setValorBienes(BigDecimal valorBienes) {
        this.valorBienes = valorBienes;
    }

    public BigDecimal getValorServicios() {
        return valorServicios;
    }

    public void setValorServicios(BigDecimal valorServicios) {
        this.valorServicios = valorServicios;
    }

    public BigDecimal getValorTcredito() {
        return valorTcredito;
    }

    public void setValorTcredito(BigDecimal valorTcredito) {
        this.valorTcredito = valorTcredito;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
        if (!(object instanceof UafTra)) {
            return false;
        }
        UafTra other = (UafTra) object;
        if (this.movimiento != null && other.getMovimiento() != null) {
            return this.movimiento.getId().equals(other.getMovimiento().getId());

        }

        return false;
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.UafTra[ id=" + id + " ]";
    }

}
