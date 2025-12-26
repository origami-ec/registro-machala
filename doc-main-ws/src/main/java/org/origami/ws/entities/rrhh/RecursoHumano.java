package org.origami.ws.entities.rrhh;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RH_RECURSO_HUMANO", schema = "recurso_humano")
public class RecursoHumano implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PCT_ID")
    private Long id;
    @JoinColumn(name = "PCT_PERSONA", referencedColumnName = "PES_ID")
    @ManyToOne
    private PersonaRH personaRH;
    @Column(name = "PCT_CATEGORIA")
    private Long categoria;
    @Column(name = "PCT_CODIGO")
    private String codigo;
    @JoinColumn(name = "PCT_CARGO", referencedColumnName = "CRG_ID")
    @ManyToOne
    private Cargo cargo;
    @Column(name = "PCT_ESTADO")
    private Boolean estado;
    @Column(name = "PCT_ELIMINADO")
    private Boolean eliminado;
    @Column(name = "PCT_TIPO_LUGAR")
    private Long tipoLugar;
    @Column(name = "PCT_LUGAR_TRABAJO")
    private Long lugarTrabajo;

    public RecursoHumano() {
    }

    public RecursoHumano(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonaRH getPersona() {
        return personaRH;
    }

    public void setPersona(PersonaRH personaRH) {
        this.personaRH = personaRH;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }


    public PersonaRH getPersonaRH() {
        return personaRH;
    }

    public void setPersonaRH(PersonaRH personaRH) {
        this.personaRH = personaRH;
    }

    public Long getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(Long tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public Long getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(Long lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    @Override
    public String toString() {
        return "RecursoHumano{" +
                "id=" + id +
                ", persona=" + personaRH +
                ", categoria=" + categoria +
                ", codigo='" + codigo + '\'' +
                ", cargo=" + cargo +
                ", estado=" + estado +
                ", eliminado=" + eliminado +
                '}';
    }
}
