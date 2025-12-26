package org.bcbg.documental.models;

import java.util.ArrayList;
import java.util.List;
import org.bcbg.util.Utils;

public class Imagen {

    private Integer indice; //es el indice del arreglo basicamente es el numero de la pagina cuando empieza en 0
    private String descripcion;
    private String apiUrl;
    private String ruta;
    private List<Nota> notas;

    public Imagen() {
    }

    public Imagen(String descripcion, String apiUrl, String ruta) {
        this.descripcion = descripcion;
        this.apiUrl = apiUrl;
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
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

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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

    @Override
    public String toString() {
        return "Imagen{" + "descripcion=" + descripcion + ", apiUrl=" + apiUrl + ", ruta=" + ruta + '}';
    }

}
