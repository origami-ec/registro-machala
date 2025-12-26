/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DEVELOPER
 */
public class FirmaElectronicaModel implements Serializable{
    private FirmaElectronica firmaElectronica;
    private Date fechaFirmar;
    private Date fechaCreacion;

    public FirmaElectronicaModel() {
    }
    
    

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public Date getFechaFirmar() {
        return fechaFirmar;
    }

    public void setFechaFirmar(Date fechaFirmar) {
        this.fechaFirmar = fechaFirmar;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "FirmaElectronicaModel{" + "firmaElectronica=" + firmaElectronica + ", fechaFirmar=" + fechaFirmar + ", fechaCreacion=" + fechaCreacion + '}';
    }
    
    
}
