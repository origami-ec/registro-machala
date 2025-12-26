package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "public", name = "servicio_departamento_requisito")
public class ServiciosDepartamentoRequisitos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean requerido;
    private Boolean estado;
    private String formato_archivo;
    @JoinColumn(name = "servicios_departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ServiciosDepartamento serviciosDepartamento;
    @JoinColumn(name = "tipo_tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TipoTramite tipoTramite;
    private Long tamanioArchivo;
    @Transient
    private String tamanio;

    private Integer orden;

    public ServiciosDepartamentoRequisitos() {
    }

    public ServiciosDepartamentoRequisitos(Long id, String nombre, String descripcion, Boolean requerido, Boolean estado, String formato_archivo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.requerido = requerido;
        this.estado = estado;
        this.formato_archivo = formato_archivo;
    }

    public Long getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Long tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public String getTamanio() {
        if (this.tamanioArchivo != null) {
            if (this.tamanioArchivo == 1048576) {
                return "1 MB";
            } else if (this.tamanioArchivo == 5242880) {
                return "5 MB";
            } else if (this.tamanioArchivo == 10485760) {
                return "10 MB";
            } else if (this.tamanioArchivo == 15728640) {
                return "15 MB";
            } else if (this.tamanioArchivo == 31457280) {
                return "30 MB";
            } else if (this.tamanioArchivo == 52428800) {
                return "50 MB";
            } else if (this.tamanioArchivo == 104857600) {
                return "100 MB";
            } else if (this.tamanioArchivo == 1073741824) {
                return "1 GB";
            }
        }
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public ServiciosDepartamentoRequisitos(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public ServiciosDepartamentoRequisitos(Long id) {
        this.id = id;
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

    public String getFormato_archivo() {
        return formato_archivo;
    }

    public void setFormato_archivo(String formato_archivo) {
        this.formato_archivo = formato_archivo;
    }

    public ServiciosDepartamento getServiciosDepartamento() {
        return serviciosDepartamento;
    }

    public void setServiciosDepartamento(ServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "ServiciosDepartamentoRequisitos{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", requerido=" + requerido +
                ", estado=" + estado +
                ", formato_archivo='" + formato_archivo + '\'' +
                ", serviciosDepartamento=" + serviciosDepartamento +
                ", tipoTramite=" + tipoTramite +
                ", orden=" + orden +
                '}';
    }
}
