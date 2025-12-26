package org.origami.docs.model;

import java.util.List;

public class ImagenDto {

    private Integer indice; //es el indice del arreglo basicamente es el numero de la pagina cuando empieza en 0
    private String descripcion;
    private String nombreImagen;
    private String apiUrl;
    private String ruta;
    private List<NotaDto> notas;

    public ImagenDto() {
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public List<NotaDto> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaDto> notas) {
        this.notas = notas;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImagenDto{");
        sb.append("indice=").append(indice);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", nombreImagen=").append(nombreImagen);
        sb.append(", apiUrl=").append(apiUrl);
        sb.append(", ruta=").append(ruta);
        sb.append(", notas=").append(notas);
        sb.append('}');
        return sb.toString();
    }

}
