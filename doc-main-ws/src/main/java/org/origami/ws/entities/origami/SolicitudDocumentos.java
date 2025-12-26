package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "solicitud_documento")
public class SolicitudDocumentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean tieneQr;
    private String codigoVerificacion;
    private String nombreArchivo;
    private String tipoArchivo;
    private String rutaArchivo;
    private Date fechaCreacion;
    private String usuario;
    private String estado;
    private Boolean tieneFirmaElectronica;
    private String descripcion;
    private Boolean rechazado;
    @JoinColumn(name = "solicitud_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private SolicitudServicios solicitud;
    @JoinColumn(name = "requisito_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiciosDepartamentoRequisitos requisito;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Departamento departamento;


    public SolicitudDocumentos() {
    }

    public SolicitudDocumentos(Long idSolicitud, Long idRequisito) {
        this.solicitud = new SolicitudServicios(idSolicitud);
        this.requisito = new ServiciosDepartamentoRequisitos(idRequisito);
    }

    public SolicitudDocumentos(Long id, Long idSolicitud, Long idRequisito) {
        this.id = id;
        this.solicitud = new SolicitudServicios(idSolicitud);
        this.requisito = new ServiciosDepartamentoRequisitos(idRequisito);
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Boolean getTieneQr() {
        return tieneQr;
    }

    public void setTieneQr(Boolean tieneQr) {
        this.tieneQr = tieneQr;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getRechazado() {
        return rechazado;
    }

    public void setRechazado(Boolean rechazado) {
        this.rechazado = rechazado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public Boolean getTieneFirmaElectronica() {
        return tieneFirmaElectronica;
    }

    public void setTieneFirmaElectronica(Boolean tieneFirmaElectronica) {
        this.tieneFirmaElectronica = tieneFirmaElectronica;
    }

    public ServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "SolicitudDocumentos{" +
                "id=" + id +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", tipoArchivo='" + tipoArchivo + '\'' +
                ", rutaArchivo='" + rutaArchivo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", usuario='" + usuario + '\'' +
                ", estado='" + estado + '\'' +
                ", tieneFirmaElectronica=" + tieneFirmaElectronica +
                ", solicitud=" + solicitud +
                ", requisito=" + requisito +
                ", descripcion='" + descripcion + '\'' +
                ", rechazado=" + rechazado +
                ", departamento=" + departamento +
                '}';
    }
}
