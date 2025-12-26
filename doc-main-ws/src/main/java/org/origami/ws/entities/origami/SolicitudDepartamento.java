package org.origami.ws.entities.origami;


import java.util.Date;
import javax.persistence.*;

@Entity
@Table(schema = "public", name = "solicitud_departamento")
public class SolicitudDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String responsables;
    private String observacion;
    private Boolean estado;
    private Date fecha;
    private String usuarioIngreso;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento departamento;
    @JoinColumn(name = "solicitud_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudServicios solicitud;


    public SolicitudDepartamento() {
    }

    public SolicitudDepartamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponsables() {
        return responsables;
    }

    public void setResponsables(String responsables) {
        this.responsables = responsables;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public SolicitudServicios getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudServicios solicitud) {
        this.solicitud = solicitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SolicitudDepartamento)) {
            return false;
        }
        SolicitudDepartamento other = (SolicitudDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SolicitudDepartamento{" +
                "id=" + id +
                ", responsables='" + responsables + '\'' +
                ", observacion='" + observacion + '\'' +
                ", estado=" + estado +
                ", fecha=" + fecha +
                ", usuarioIngreso='" + usuarioIngreso + '\'' +
                ", departamento=" + departamento +
                ", solicitud=" + solicitud +
                '}';
    }
}
