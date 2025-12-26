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
@Table(name = "lib_registro_mercantil", schema = "public")
public class LibRegistroMercantil implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Column(name = "id_gab")
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
    @Column(name = "tipo_de_compania")
    private String tipo_de_compania;
    @Size(max = 250)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_de_cancelacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCancelacion;
    @Size(max = 250)
    @Column(name = "tipo_de_bien")
    private String tipoBien;
    @Column(name = "chasis_serie")
    private String chasisSerie;
    @Size(max = 250)
    @Column(name = "motor")
    private String motor;
    @Size(max = 250)
    @Column(name = "marca")
    private String marca;
    @Size(max = 250)
    @Column(name = "modelo")
    private String modelo;
    @Size(max = 4)
    @Column(name = "ano_de_fabrica")
    private String anioFabrica;
    @Size(max = 20)
    @Column(name = "placa")
    private String placa;
    @Column(name = "autoridad")
    private String autoridad;
    @Column(name = "fecha_de_disposicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDisposicion;
    @Size(max = 50)
    @Column(name = "numero_de_disposicion")
    private String numeroDisposicion;
    @Column(name = "fecha_de_escritura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEscritura;
    @Size(max = 250)
    @Column(name = "notaria")
    private String notaria;
    @Size(max = 250)
    @Column(name = "canton")
    private String canton;
    @Size(max = 250)
    @Column(name = "ubicacion_del_dato")
    private String ubicacionDato;
    @Column(name = "ultima_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaModificacion;
    @Column(name = "identificador_unico")
    private String identificadorUnico;
    @Size(max = 250)
    @Column(name = "notaria_juzgado")
    private String notariaJuzgado;
    @Size(max = 250)
    @Column(name = "canton_notaria")
    private String cantonNotaria;
    @Column(name = "fecha_de_escritura_notaria")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEscrituraNotaria;
    @Size(max = 20)
    @Column(name = "estado_de_inscripcion")
    private String estado_de_inscripcion;
    @Column(name = "representante_administrador_cia")
    private String representanteAdministradorCia;
    @Size(max = 1)
    @Column(name = "plano")
    private String plano;

    public LibRegistroMercantil() {
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

    public String getTipo_de_compania() {
        return tipo_de_compania;
    }

    public void setTipo_de_compania(String tipo_de_compania) {
        this.tipo_de_compania = tipo_de_compania;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public String getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(String tipoBien) {
        this.tipoBien = tipoBien;
    }

    public String getChasisSerie() {
        return chasisSerie;
    }

    public void setChasisSerie(String chasisSerie) {
        this.chasisSerie = chasisSerie;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnioFabrica() {
        return anioFabrica;
    }

    public void setAnioFabrica(String anioFabrica) {
        this.anioFabrica = anioFabrica;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public Date getFechaDisposicion() {
        return fechaDisposicion;
    }

    public void setFechaDisposicion(Date fechaDisposicion) {
        this.fechaDisposicion = fechaDisposicion;
    }

    public String getNumeroDisposicion() {
        return numeroDisposicion;
    }

    public void setNumeroDisposicion(String numeroDisposicion) {
        this.numeroDisposicion = numeroDisposicion;
    }

    public Date getFechaEscritura() {
        return fechaEscritura;
    }

    public void setFechaEscritura(Date fechaEscritura) {
        this.fechaEscritura = fechaEscritura;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
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

    public String getIdentificadorUnico() {
        return identificadorUnico;
    }

    public void setIdentificadorUnico(String identificadorUnico) {
        this.identificadorUnico = identificadorUnico;
    }

    public String getNotariaJuzgado() {
        return notariaJuzgado;
    }

    public void setNotariaJuzgado(String notariaJuzgado) {
        this.notariaJuzgado = notariaJuzgado;
    }

    public String getCantonNotaria() {
        return cantonNotaria;
    }

    public void setCantonNotaria(String cantonNotaria) {
        this.cantonNotaria = cantonNotaria;
    }

    public Date getFechaEscrituraNotaria() {
        return fechaEscrituraNotaria;
    }

    public void setFechaEscrituraNotaria(Date fechaEscrituraNotaria) {
        this.fechaEscrituraNotaria = fechaEscrituraNotaria;
    }

    public String getEstado_de_inscripcion() {
        return estado_de_inscripcion;
    }

    public void setEstado_de_inscripcion(String estado_de_inscripcion) {
        this.estado_de_inscripcion = estado_de_inscripcion;
    }

    public String getRepresentanteAdministradorCia() {
        return representanteAdministradorCia;
    }

    public void setRepresentanteAdministradorCia(String representanteAdministradorCia) {
        this.representanteAdministradorCia = representanteAdministradorCia;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    @Override
    public String toString() {
        return "LibRegistroMercantil{" + "idGab=" + idGab 
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
                + ", fechaInscripcion=" + fechaInscripcion 
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
                + ", tipo_de_compania=" + tipo_de_compania 
                + ", estado=" + estado 
                + ", fechaCancelacion=" + fechaCancelacion 
                + ", tipoBien=" + tipoBien 
                + ", chasisSerie=" + chasisSerie 
                + ", motor=" + motor 
                + ", marca=" + marca 
                + ", modelo=" + modelo 
                + ", anioFabrica=" + anioFabrica 
                + ", placa=" + placa 
                + ", autoridad=" + autoridad 
                + ", fechaDisposicion=" + fechaDisposicion 
                + ", numeroDisposicion=" + numeroDisposicion 
                + ", fechaEscritura=" + fechaEscritura 
                + ", notaria=" + notaria 
                + ", canton=" + canton 
                + ", ubicacionDato=" + ubicacionDato 
                + ", ultimaModificacion=" + ultimaModificacion 
                + ", identificadorUnico=" + identificadorUnico 
                + ", notariaJuzgado=" + notariaJuzgado 
                + ", cantonNotaria=" + cantonNotaria 
                + ", fechaEscrituraNotaria=" + fechaEscrituraNotaria 
                + ", estado_de_inscripcion=" + estado_de_inscripcion 
                + ", representanteAdministradorCia=" + representanteAdministradorCia 
                + ", plano=" + plano + '}';
    }
    
}
