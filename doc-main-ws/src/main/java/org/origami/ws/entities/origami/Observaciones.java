package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( schema = "public", name = "observacion")
public class Observaciones implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Long idProceso;
    private String observacion;
    private Date fecCre;
    private String userCre;
    private Boolean estado;
    private String tarea;
    @JoinColumn(name = "id_tramite")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramites idTramite;

    public Observaciones() {
    }

    public Observaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public HistoricoTramites getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(HistoricoTramites idTramite) {
        this.idTramite = idTramite;
    }

    @Override
    public String toString() {
        return "Observaciones{" +
                "id=" + id +
                ", idProceso=" + idProceso +
                ", observacion='" + observacion + '\'' +
                ", fecCre=" + fecCre +
                ", userCre='" + userCre + '\'' +
                ", estado=" + estado +
                ", tarea='" + tarea + '\'' +
                ", idTramite=" + idTramite +
                '}';
    }
}
