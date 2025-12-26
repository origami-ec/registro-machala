package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "tipo_notificacion")
public class TipoNotificacion {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String abreviatura;
    private String codigoSecuencia;
    private Integer clase;

    public TipoNotificacion() {
    }

    public TipoNotificacion(Long id) {
        this.id = id;
    }

    public TipoNotificacion(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getCodigoSecuencia() {
        return codigoSecuencia;
    }

    public void setCodigoSecuencia(String codigoSecuencia) {
        this.codigoSecuencia = codigoSecuencia;
    }

    public Integer getClase() {
        return clase;
    }

    public void setClase(Integer clase) {
        this.clase = clase;
    }

    @Override
    public String toString() {
        return "TipoNotificacion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                ", abreviatura=" + abreviatura +
                '}';
    }
}
