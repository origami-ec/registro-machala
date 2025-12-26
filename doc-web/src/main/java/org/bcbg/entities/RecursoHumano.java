/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.entities;

/**
 *
 * @author ricar
 */
public class RecursoHumano {

    private Long id;
    private PersonaRH persona;
    private Long categoria;
    private String codigo;
    private Cargo cargo;
    private Boolean estado;
    private Boolean eliminado;
    private Long lugarTrabajo;
    private Long tipoLugar;

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
        return persona;
    }

    public void setPersona(PersonaRH persona) {
        this.persona = persona;
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

    public Long getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(Long lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public Long getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(Long tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    @Override
    public String toString() {
        return "RecursoHumano{"
                + "id=" + id
                + ", persona=" + persona
                + ", categoria=" + categoria
                + ", codigo='" + codigo + '\''
                + ", cargo=" + cargo
                + ", estado=" + estado
                + ", eliminado=" + eliminado
                + '}';
    }
}
