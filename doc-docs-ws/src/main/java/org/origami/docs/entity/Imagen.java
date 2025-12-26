package org.origami.docs.entity;

import org.origami.docs.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Imagen {

    private Integer indice; //es el indice del arreglo basicamente es el numero de la pagina cuando empieza en 0
    private String descripcion;
    private String nombreImagen;
    private String ruta;
    private List<Nota> notas;

    public Imagen() {
    }

    public Imagen(Integer indice, String descripcion, String nombreImagen, String ruta) {
        this.indice = indice;
        this.descripcion = descripcion;
        this.nombreImagen = nombreImagen;
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    public List<Nota> getNotas() {
        if (Utils.isEmpty(notas)) {
            notas = new ArrayList<>();
        }
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    /**
     * @return Es el indice del arreglo basicamente es el numero de la pagina cuando empieza en 0
     */

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return "Imagen{" +
                "descripcion='" + descripcion + '\'' +
                ", nombreImagen='" + nombreImagen + '\'' +
                ", ruta='" + ruta + '\'' +

                '}';
    }
}