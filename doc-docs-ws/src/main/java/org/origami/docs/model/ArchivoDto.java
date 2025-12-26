package org.origami.docs.model;

import org.origami.docs.entity.ArchivoIndexacion;
import org.origami.docs.entity.Usuario;
import java.util.List;

public class ArchivoDto {

    private String id;
    private String nombre;
    private String tramite;
    private String numTramite;
    private String ruta;
    private String rutaNotas;
    private String fecha;
    private Integer numeroPaginas;
    private String peso;
    private String tipo;
    private Usuario usuario;
    private String formato;
    private Usuario usuarioModifica;
    private List<ImagenDto> imagenes;
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private String rutaConvertido;
    private List<ArchivoIndexacion> detalles;
    private String convertir;

    public ArchivoDto() {
    }

    public ArchivoDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<ImagenDto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenDto> imagenes) {
        this.imagenes = imagenes;
    }

    public Usuario getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(Usuario usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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

    public String getRutaNotas() {
        return rutaNotas;
    }

    public void setRutaNotas(String rutaNotas) {
        this.rutaNotas = rutaNotas;
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

    public String getRutaConvertido() {
        return rutaConvertido;
    }

    public void setRutaConvertido(String rutaConvertido) {
        this.rutaConvertido = rutaConvertido;
    }

    public String getConvertir() {
        return convertir;
    }

    public void setConvertir(String convertir) {
        this.convertir = convertir;
    }

    @Override
    public String toString() {
        return "ArchivoDto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tramite='" + tramite + '\'' +
                ", numTramite='" + numTramite + '\'' +
                ", ruta='" + ruta + '\'' +
                ", rutaNotas='" + rutaNotas + '\'' +
                ", fecha='" + fecha + '\'' +
                ", numeroPaginas=" + numeroPaginas +
                ", peso='" + peso + '\'' +
                ", tipo='" + tipo + '\'' +
                ", usuario=" + usuario +
                ", formato='" + formato + '\'' +
                ", usuarioModifica=" + usuarioModifica +
                ", imagenes=" + imagenes +
                ", tipoIndexacion='" + tipoIndexacion + '\'' +
                ", detalleDocumento='" + detalleDocumento + '\'' +
                ", estado=" + estado +
                ", rutaConvertido='" + rutaConvertido + '\'' +
                ", detalles=" + detalles +
                ", convertir='" + convertir + '\'' +
                '}';
    }
}
