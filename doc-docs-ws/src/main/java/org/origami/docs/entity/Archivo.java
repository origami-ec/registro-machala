package org.origami.docs.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "archivo")
public class Archivo {
    @Id
    public String _id;
    private String nombre;
    private String tramite;
    private String numTramite;
    private String ruta;
    private Usuario usuario;
    private String fecha;
    private Integer numeroPaginas;
    private String peso;
    private String tipo;
    private String formato;
    private String tipoContenido;
    private List<Imagen> imagenes;
    private Date fechaCreacion;
    /**
     * campos para indexacion del archivo
     */
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private List<ArchivoIndexacion> detalles;

    public Archivo() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<ArchivoIndexacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ArchivoIndexacion> detalles) {
        this.detalles = detalles;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getNumTramite() {
        return numTramite;
    }
    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Archivo archivo = (Archivo) o;
        return _id.equals(archivo._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Archivo{" +
                "_id=" + _id +
                ", nombre='" + nombre + '\'' +
                ", ruta='" + ruta + '\'' +
                ", usuario=" + usuario +
                ", fecha=" + fecha +
                ", numeroPaginas=" + numeroPaginas +
                ", peso='" + peso + '\'' +
                ", tipo='" + tipo + '\'' +
                ", imagenes=" + imagenes +
                '}';
    }
}

