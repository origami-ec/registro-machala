/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "lib_registro_propiedad", schema = "public")
public class LibRegistroPropiedad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id_gab", nullable = false)
    private Long idGab;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tran", nullable = false)
    private Long idTran;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "fecha_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsulta;
    @Basic(optional = false)
    @Size(max = 50)
    @Column(name = "usuario", nullable = false)
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
    @Basic(optional = false)
    @Size(max = 1)
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "imagen")
    private byte[] imagen;
    @Column(name = "contenido")
    private String contenido;
    @Size(max = 50)
    @Column(name = "libro")
    private String libro;
    @Size(max = 5)
    @Column(name = "tomo")
    private String tomo;
    @Size(max = 250)
    @Column(name = "tipo_de_documento")
    private String tipoDocumento;
    @Size(max = 16)
    @Column(name = "folio_inicial")
    private String folioInicial;
    @Size(max = 15)
    @Column(name = "numero_de_repertorio")
    private String repertorio;
    @Column(name = "numero_de_inscripcion")
    private Integer inscripcion;
    @Column(name = "fecha_de_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "beneficiario")
    private String beneficiario;
    @Column(name = "ced_ruc_beneficiario")
    private String cedRucBeneficiario;
    @Column(name = "otorgante")
    private String otorgante;
    @Column(name = "ced_ruc_otorgante")
    private String cedRucOtorgante;
    @Size(max = 18)
    @Column(name = "codigo_catastral")
    private String codigoCatastral;
    @Size(max = 10)
    @Column(name = "lote")
    private String lote;
    @Column(name = "anotacion_marginal")
    private String anotacionMarginal;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "secuencia")
    private BigDecimal secuencia;
    @Column(name = "control")
    private String control;
    @Column(name = "tipo_de_compareciente")
    private String tipoCompareciente;
    @Column(name = "descripcion_del_bien")
    private String descripcionBien;
    @Size(max = 250)
    @Column(name = "provincia")
    private String provincia;
    @Size(max = 250)
    @Column(name = "zona")
    private String zona;
    @Column(name = "superficie")
    private String superficie;
    @Column(name = "lindero_orientacion")
    private String linderoOrientacion;
    @Column(name = "lindero_descripcion")
    private String linderoDescripcion;
    @Size(max = 200)
    @Column(name = "parroquia")
    private String parroquia;
    @Size(max = 200)
    @Column(name = "canton")
    private String canton;
    @Column(name = "cuantia")
    private BigDecimal cuantia;
    @Column(name = "identificador_unico")
    private String identificadorUnico;
    @Size(max = 250)
    @Column(name = "numero_de_juicio")
    private String numeroJuicio;
    @Size(max = 250)
    @Column(name = "estado")
    private String estado;
    @Size(max = 200)
    @Column(name = "ubicacion_del_dato")
    private String ubicacionDato;
    @Column(name = "ultima_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaModificacion;
    @Size(max = 250)
    @Column(name = "notaria")
    private String notaria;
    @Size(max = 250)
    @Column(name = "canton_de_notaria")
    private String cantonNotaria;
    @Column(name = "fecha_de_escritura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEscritura;
    @Size(max = 1)
    @Column(name = "plano")
    private String plano;
    
    public LibRegistroPropiedad() {
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFolioInicial() {
        return folioInicial;
    }

    public void setFolioInicial(String folioInicial) {
        this.folioInicial = folioInicial;
    }

    public String getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(String repertorio) {
        this.repertorio = repertorio;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getCedRucBeneficiario() {
        return cedRucBeneficiario;
    }

    public void setCedRucBeneficiario(String cedRucBeneficiario) {
        this.cedRucBeneficiario = cedRucBeneficiario;
    }

    public String getOtorgante() {
        return otorgante;
    }

    public void setOtorgante(String otorgante) {
        this.otorgante = otorgante;
    }

    public String getCedRucOtorgante() {
        return cedRucOtorgante;
    }

    public void setCedRucOtorgante(String cedRucOtorgante) {
        this.cedRucOtorgante = cedRucOtorgante;
    }

    public String getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getAnotacionMarginal() {
        return anotacionMarginal;
    }

    public void setAnotacionMarginal(String anotacionMarginal) {
        this.anotacionMarginal = anotacionMarginal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getTipoCompareciente() {
        return tipoCompareciente;
    }

    public void setTipoCompareciente(String tipoCompareciente) {
        this.tipoCompareciente = tipoCompareciente;
    }

    public String getDescripcionBien() {
        return descripcionBien;
    }

    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public String getLinderoOrientacion() {
        return linderoOrientacion;
    }

    public void setLinderoOrientacion(String linderoOrientacion) {
        this.linderoOrientacion = linderoOrientacion;
    }

    public String getLinderoDescripcion() {
        return linderoDescripcion;
    }

    public void setLinderoDescripcion(String linderoDescripcion) {
        this.linderoDescripcion = linderoDescripcion;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public String getIdentificadorUnico() {
        return identificadorUnico;
    }

    public void setIdentificadorUnico(String identificadorUnico) {
        this.identificadorUnico = identificadorUnico;
    }

    public String getNumeroJuicio() {
        return numeroJuicio;
    }

    public void setNumeroJuicio(String numeroJuicio) {
        this.numeroJuicio = numeroJuicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacionDato() {
        return ubicacionDato;
    }

    public void setUbicacionDato(String ubicacionDato) {
        this.ubicacionDato = ubicacionDato;
    }

    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public String getCantonNotaria() {
        return cantonNotaria;
    }

    public void setCantonNotaria(String cantonNotaria) {
        this.cantonNotaria = cantonNotaria;
    }

    public Date getFechaEscritura() {
        return fechaEscritura;
    }

    public void setFechaEscritura(Date fechaEscritura) {
        this.fechaEscritura = fechaEscritura;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    @Override
    public String toString() {
        return "LibRegistroPropiedad{" + "idGab=" + idGab
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
                + ", libro=" + libro
                + ", tomo=" + tomo
                + ", tipoDocumento=" + tipoDocumento
                + ", folioInicial=" + folioInicial
                + ", repertorio=" + repertorio
                + ", inscripcion=" + inscripcion
                + ", beneficiario=" + beneficiario
                + ", cedRucBeneficiario=" + cedRucBeneficiario
                + ", otorgante=" + otorgante
                + ", cedRucOtorgante=" + cedRucOtorgante
                + ", codigoCatastral=" + codigoCatastral
                + ", lote=" + lote
                + ", anotacionMarginal=" + anotacionMarginal
                + ", observaciones=" + observaciones
                + ", secuencia=" + secuencia
                + ", control=" + control
                + ", tipoCompareciente=" + tipoCompareciente
                + ", descripcionBien=" + descripcionBien
                + ", provincia=" + provincia
                + ", zona=" + zona
                + ", superficie=" + superficie
                + ", linderoOrientacion=" + linderoOrientacion
                + ", linderoDescripcion=" + linderoDescripcion
                + ", parroquia=" + parroquia
                + ", canton=" + canton
                + ", cuantia=" + cuantia
                + ", identificadorUnico=" + identificadorUnico
                + ", numeroJuicio=" + numeroJuicio
                + ", estado=" + estado
                + ", ubicacionDato=" + ubicacionDato
                + ", ultimaModificacion=" + ultimaModificacion
                + ", notaria=" + notaria
                + ", cantonNotaria=" + cantonNotaria
                + ", fechaEscritura=" + fechaEscritura
                + ", plano=" + plano + '}';
    }

}
