package org.origami.ws.dto;

public class DepartamentoDTO {

    private Long id;
    private Long idBcbg;
    private String nombre;
    private Boolean direccion;
    private Long tipo;
    private Boolean estado;
    private String codigo;

    public DepartamentoDTO() {
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

    public Boolean getDireccion() {
        return direccion;
    }

    public void setDireccion(Boolean direccion) {
        this.direccion = direccion;
    }

    /*  public Long getPadre() {
          return padre;
      }

      public void setPadre(Long padre) {
          this.padre = padre;
      }
  */
    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getIdBcbg() {
        return idBcbg;
    }

    public void setIdBcbg(Long idBcbg) {
        this.idBcbg = idBcbg;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }
}
