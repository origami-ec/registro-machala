package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "requisito_error")
public class RequisitosErrores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "requisito_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiciosDepartamentoRequisitos requisito;
    @JoinColumn(name = "notificacion_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Notificacion notificacion;
    private Boolean faltante;

    public RequisitosErrores() {

    }

    public RequisitosErrores(Long id, ServiciosDepartamentoRequisitos requisito) {
        this.id = id;
        this.requisito = requisito;
    }

    public Boolean getFaltante() {
        return faltante;
    }

    public void setFaltante(Boolean faltante) {
        this.faltante = faltante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoRequisitos requsito) {
        this.requisito = requsito;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    @Override
    public String toString() {
        return "RequisitosErrores{" +
                "id=" + id +
                ", requisito=" + requisito +
                ", notificacion=" + notificacion +
                '}';
    }
}
