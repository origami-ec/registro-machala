package org.origami.ws.dto;

public class ServiciosDepartamentoItemsRequisitosDTO {


    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean requerido;
    private Boolean estado;
    private String formato_archivo;
    private Long tamanioArchivo;
    private String tamanio;

    private Integer orden;

    public ServiciosDepartamentoItemsRequisitosDTO() {
    }

    public Long getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Long tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getFormato_archivo() {
        return formato_archivo;
    }

    public void setFormato_archivo(String formato_archivo) {
        this.formato_archivo = formato_archivo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

}
