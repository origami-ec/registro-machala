package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.origami.ws.entities.security.Usuario;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "public", name = "solicitud")
public class Solicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private HistoricoTramites tramite;
    /* @Column(name = "tipo_formulario")
     private String payframeUrl; //TIPO DE APLICACION WEB O VENTANILLA*/
    private Boolean tipoPago;  //TRUE: VENTANILLA FALSE: PAGO EN LINEA
    @ManyToOne
    @JoinColumn(name = "tipo_bien")
    private CatalogoItem tipoBien;
    @ManyToOne
    @JoinColumn(name = "tipo_certificado")
    private CatalogoItem tipoCertificado;
    private String solApellidos;
    private String solNombres;
    private String solCedula;
    private String solDireccion;
    private String sector;
    private String solParroquia;
    private String manzana;
    private String solCelular;
    private String solConvencional;
    private String solCorreo;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date fechaSolicitud;
    private String otroMotivo;
    private Integer numeroFicha;
    private String observacion;
    private String estado;
    private Double total;
    private String solEstadoCivil;
    private String codigoVerificacion;
    private Integer banco;
    private String codigoComprobante;
    private Long idSolicitudVentanilla;
    private String bancoDesde;
    private String propietarioCuenta;
    private String numeroCuenta;
    private String fechaTransferencia;
    @Transient
    private Usuario usuario;
    @Transient
    private String activityKey;

    public Solicitud() {
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Solicitud(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Boolean getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Boolean tipoPago) {
        this.tipoPago = tipoPago;
    }

    public CatalogoItem getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(CatalogoItem tipoBien) {
        this.tipoBien = tipoBien;
    }

    public CatalogoItem getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(CatalogoItem tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getSolApellidos() {
        return solApellidos;
    }

    public void setSolApellidos(String solApellidos) {
        this.solApellidos = solApellidos;
    }

    public String getSolNombres() {
        return solNombres;
    }

    public void setSolNombres(String solNombres) {
        this.solNombres = solNombres;
    }

    public String getSolCedula() {
        return solCedula;
    }

    public void setSolCedula(String solCedula) {
        this.solCedula = solCedula;
    }

    public String getSolDireccion() {
        return solDireccion;
    }

    public void setSolDireccion(String solDireccion) {
        this.solDireccion = solDireccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSolParroquia() {
        return solParroquia;
    }

    public void setSolParroquia(String solParroquia) {
        this.solParroquia = solParroquia;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSolCelular() {
        return solCelular;
    }

    public void setSolCelular(String solCelular) {
        this.solCelular = solCelular;
    }

    public String getSolConvencional() {
        return solConvencional;
    }

    public void setSolConvencional(String solConvencional) {
        this.solConvencional = solConvencional;
    }

    public String getSolCorreo() {
        return solCorreo;
    }

    public void setSolCorreo(String solCorreo) {
        this.solCorreo = solCorreo;
    }


    public String getOtroMotivo() {
        return otroMotivo;
    }

    public void setOtroMotivo(String otroMotivo) {
        this.otroMotivo = otroMotivo;
    }

    public Integer getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(Integer numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getSolEstadoCivil() {
        return solEstadoCivil;
    }

    public void setSolEstadoCivil(String solEstadoCivil) {
        this.solEstadoCivil = solEstadoCivil;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getCodigoComprobante() {
        return codigoComprobante;
    }

    public void setCodigoComprobante(String codigoComprobante) {
        this.codigoComprobante = codigoComprobante;
    }

    public Long getIdSolicitudVentanilla() {
        return idSolicitudVentanilla;
    }

    public void setIdSolicitudVentanilla(Long idSolicitudVentanilla) {
        this.idSolicitudVentanilla = idSolicitudVentanilla;
    }

    public String getBancoDesde() {
        return bancoDesde;
    }

    public void setBancoDesde(String bancoDesde) {
        this.bancoDesde = bancoDesde;
    }

    public String getPropietarioCuenta() {
        return propietarioCuenta;
    }

    public void setPropietarioCuenta(String propietarioCuenta) {
        this.propietarioCuenta = propietarioCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(String fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", tramite=" + tramite +
                ", tipoPago=" + tipoPago +
                ", tipoBien=" + tipoBien +
                ", tipoCertificado=" + tipoCertificado +
                ", solApellidos='" + solApellidos + '\'' +
                ", solNombres='" + solNombres + '\'' +
                ", solCedula='" + solCedula + '\'' +
                ", solDireccion='" + solDireccion + '\'' +
                ", sector='" + sector + '\'' +
                ", solParroquia='" + solParroquia + '\'' +
                ", manzana='" + manzana + '\'' +
                ", solCelular='" + solCelular + '\'' +
                ", solConvencional='" + solConvencional + '\'' +
                ", solCorreo='" + solCorreo + '\'' +
                ", otroMotivo='" + otroMotivo + '\'' +
                ", numeroFicha=" + numeroFicha +
                ", observacion='" + observacion + '\'' +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                ", solEstadoCivil='" + solEstadoCivil + '\'' +
                ", codigoVerificacion='" + codigoVerificacion + '\'' +
                ", banco=" + banco +
                ", codigoComprobante='" + codigoComprobante + '\'' +
                ", idSolicitudVentanilla=" + idSolicitudVentanilla +
                ", bancoDesde='" + bancoDesde + '\'' +
                ", propietarioCuenta='" + propietarioCuenta + '\'' +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", fechaTransferencia='" + fechaTransferencia + '\'' +
                '}';
    }
}
