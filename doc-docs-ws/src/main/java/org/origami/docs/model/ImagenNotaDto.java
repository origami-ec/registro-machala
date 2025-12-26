package org.origami.docs.model;

public class ImagenNotaDto {

    private String archivoId;
    private Integer indiceImagen;
    private Integer indiceNota;
    private String tipo;
    private NotaDto nota;

    public ImagenNotaDto() {
    }

    public String getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(String archivoId) {
        this.archivoId = archivoId;
    }

    public Integer getIndiceImagen() {
        return indiceImagen;
    }

    public void setIndiceImagen(Integer indiceImagen) {
        this.indiceImagen = indiceImagen;
    }

    public Integer getIndiceNota() {
        return indiceNota;
    }

    public void setIndiceNota(Integer indiceNota) {
        this.indiceNota = indiceNota;
    }

    public NotaDto getNota() {
        return nota;
    }

    public void setNota(NotaDto nota) {
        this.nota = nota;
    }

    /**
     *
     * @return A: actualizar D: borrar
     */
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
