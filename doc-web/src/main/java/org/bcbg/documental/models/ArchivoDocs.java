package org.bcbg.documental.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ArchivoDocs implements Serializable {

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
    private String formato;
    private UsuarioDocs usuario;
    private UsuarioDocs usuarioModifica;
    private List<Imagen> imagenes;
    private String rutaConvertido;
    /**
     * campos para indexacion del archivo
     */
    private String tipoIndexacion;
    private String detalleDocumento;//CAMPO PARA AGREGAR AL ARCHIVO como criterio de busqueda
    private Boolean estado;
    private List<IndexacionCampo> detalles;
    private String convertir;

    public ArchivoDocs() {
    }

    public ArchivoDocs(String id) {
        this.id = id;
    }

    public ArchivoDocs(String id, String convertir) {
        this.id = id;
        this.convertir = convertir;
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

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public UsuarioDocs getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDocs usuario) {
        this.usuario = usuario;
    }

    public UsuarioDocs getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(UsuarioDocs usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
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

    public List<IndexacionCampo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<IndexacionCampo> detalles) {
        this.detalles = detalles;
    }

    public String getRutaNotas() {
        return rutaNotas;
    }

    public void setRutaNotas(String rutaNotas) {
        this.rutaNotas = rutaNotas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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
        final ArchivoDocs other = (ArchivoDocs) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ArchivoDocs{" + "id=" + id + ", nombre=" + nombre + ", tramite=" + tramite + ", numTramite=" + numTramite + ", ruta=" + ruta + ", rutaNotas=" + rutaNotas + ", fecha=" + fecha + ", numeroPaginas=" + numeroPaginas + ", peso=" + peso + ", tipo=" + tipo + ", formato=" + formato + ", usuario=" + usuario + ", usuarioModifica=" + usuarioModifica + ", imagenes=" + imagenes + ", rutaConvertido=" + rutaConvertido + ", tipoIndexacion=" + tipoIndexacion + ", detalleDocumento=" + detalleDocumento + ", estado=" + estado + ", detalles=" + detalles + ", convertir=" + convertir + '}';
    }

}
