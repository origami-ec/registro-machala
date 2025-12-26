/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.util.Date;

/**
 *
 * @author Ricardo
 */
public class SecuenciaTramites {

    private Long id;
    private Long numeroTramite;
    private Date fecha;

    public SecuenciaTramites() {
    }

    public SecuenciaTramites(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "SecuenciaTramites{" + "id=" + id + ", numeroTramite=" + numeroTramite + ", fecha=" + fecha + '}';
    }

}
