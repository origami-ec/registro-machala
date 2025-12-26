package org.origami.docs.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "documentoFirmado")
public class DocumentoFirmado {

    @Id
    private String archivo; //FIRMA ELECTRONICA .P12
    //private String clave; // CLAVE DE LA FIRMA ELECTRONICA
    private Boolean estado;
    private Date fechaCreacion;
    private String tipoFirma; // information2 - QR { INFORMACION SALE SOLO INFO D LA FIRMA QR SALE LA INFO D LA FIRMA EN EL QR MAS LA URL DE CONSULTA }
    private String ubicacion; //DESDE DONDE SE FIRMA =V
    private String motivo; //NOMBRE DEL TRAMITE
    private String archivoFirmar; //ARCHIVO EN PDF
    private String archivoFirmado; //ARCHIVO EN PDF FIRMADO
    private String urlArchivoFirmado; //URL DE ARCHIVO EN PDF FIRMADO
    private String urlQr; //URL DEL ARCHIVO
    private Integer numeroPagina; //NUMERO DE LA PAGINA QUE NECESITA SER FIRMADA -- SI NO TIENE POR DEFAULT COJE LA ULTIMA
    private Usuario usuario;
    private Catalogo tipo;
    private String firmaDigital; //ARCHIVO DE RUTA DE IMAGEN DE LA FIRMA DIGITAL DE LA PERSONA
    private Integer numeroFirma; //Numero de veces que se ha firmado el documento por defecto debe ser 0
    /*
        Campos FirmaEc
     */
    private String estadoFirma; //Certificado revocado - Certificado caducado  - Certificado emitido por entidad certificadora
    private String uid;
    private String cn;
    private String emision;
    private Date fechaEmision;
    private Date fechaExpiracion;
    private String isuser;

    /*
        Posicion de la firma
     */
    private String posicionX1;
    private String posicionX2;
    private String posicionY1;
    private String posicionY2;


    private Boolean firmaCaducada;
    private String estadofirmaCaducada;


    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private List<ArchivoIndexacion> detalles;

    @Transient
    private byte[] archivoDesk;

    public DocumentoFirmado() {
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
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

    public String getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getArchivoFirmar() {
        return archivoFirmar;
    }

    public void setArchivoFirmar(String archivoFirmar) {
        this.archivoFirmar = archivoFirmar;
    }

    public String getArchivoFirmado() {
        return archivoFirmado;
    }

    public void setArchivoFirmado(String archivoFirmado) {
        this.archivoFirmado = archivoFirmado;
    }

    public String getUrlArchivoFirmado() {
        return urlArchivoFirmado;
    }

    public void setUrlArchivoFirmado(String urlArchivoFirmado) {
        this.urlArchivoFirmado = urlArchivoFirmado;
    }

    public String getUrlQr() {
        return urlQr;
    }

    public void setUrlQr(String urlQr) {
        this.urlQr = urlQr;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Integer getNumeroFirma() {
        return numeroFirma;
    }

    public void setNumeroFirma(Integer numeroFirma) {
        this.numeroFirma = numeroFirma;
    }

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPosicionX1() {
        return posicionX1;
    }

    public void setPosicionX1(String posicionX1) {
        this.posicionX1 = posicionX1;
    }

    public String getPosicionX2() {
        return posicionX2;
    }

    public void setPosicionX2(String posicionX2) {
        this.posicionX2 = posicionX2;
    }

    public String getPosicionY1() {
        return posicionY1;
    }

    public void setPosicionY1(String posicionY1) {
        this.posicionY1 = posicionY1;
    }

    public String getPosicionY2() {
        return posicionY2;
    }

    public void setPosicionY2(String posicionY2) {
        this.posicionY2 = posicionY2;
    }

    public Boolean getFirmaCaducada() {
        return firmaCaducada;
    }

    public void setFirmaCaducada(Boolean firmaCaducada) {
        this.firmaCaducada = firmaCaducada;
    }

    public String getEstadofirmaCaducada() {
        return estadofirmaCaducada;
    }

    public void setEstadofirmaCaducada(String estadofirmaCaducada) {
        this.estadofirmaCaducada = estadofirmaCaducada;
    }

    public Catalogo getTipo() {
        return tipo;
    }

    public void setTipo(Catalogo tipo) {
        this.tipo = tipo;
    }

    public String getTipoIndexacion() {
        return tipoIndexacion;
    }

    public void setTipoIndexacion(String tipoIndexacion) {
        this.tipoIndexacion = tipoIndexacion;
    }

    public String getDetalleDocumento() {
        return detalleDocumento;
    }

    public void setDetalleDocumento(String detalleDocumento) {
        this.detalleDocumento = detalleDocumento;
    }

    public List<ArchivoIndexacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ArchivoIndexacion> detalles) {
        this.detalles = detalles;
    }

    public byte[] getArchivoDesk() {
        return archivoDesk;
    }

    public void setArchivoDesk(byte[] archivoDesk) {
        this.archivoDesk = archivoDesk;
    }
}
