package org.origami.ws.models;

public class DetalleQR {
    private String archivo;//archivo donde se va almacenar
    private String idAndCodigo; // nombre de la imagen, barcode // turno codificado
    private String rutaArchivoPlantilla; //ruta de la plantilla para construir la imagen
    private String nombreImagen;

    public DetalleQR() {
    }

    public DetalleQR(String archivo, String idAndCodigo, String rutaArchivoPlantilla, String nombreImagen) {
        this.archivo = archivo;
        this.idAndCodigo = idAndCodigo;
        this.rutaArchivoPlantilla = rutaArchivoPlantilla;
        this.nombreImagen = nombreImagen;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getIdAndCodigo() {
        return idAndCodigo;
    }

    public void setIdAndCodigo(String idAndCodigo) {
        this.idAndCodigo = idAndCodigo;
    }

    public String getRutaArchivoPlantilla() {
        return rutaArchivoPlantilla;
    }

    public void setRutaArchivoPlantilla(String rutaArchivoPlantilla) {
        this.rutaArchivoPlantilla = rutaArchivoPlantilla;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    @Override
    public String toString() {
        return "DetalleQR{" +
                "archivo='" + archivo + '\'' +
                ", idAndCodigo='" + idAndCodigo + '\'' +
                ", rutaArchivoPlantilla='" + rutaArchivoPlantilla + '\'' +
                ", nombreImagen='" + nombreImagen + '\'' +
                '}';
    }
}
