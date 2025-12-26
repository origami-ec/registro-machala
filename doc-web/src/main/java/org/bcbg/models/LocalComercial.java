/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

import java.util.Date;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class LocalComercial {

    private String ruc;
    private String razonSocialEstablecimiento;
    private String datosPropietario;
    private String provincia;
    private String canton;
    private String parroquia;
    private String barrio;
    private String direccion;
    private String activity;
    private String codeEstablecimiento;
    private Date emissionDate;
    private Date dateValidity;

    public LocalComercial() {

    }

    public LocalComercial(String ruc, String codeEstablecimiento) {
        this.ruc = ruc;
        this.codeEstablecimiento = codeEstablecimiento;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocialEstablecimiento() {
        return razonSocialEstablecimiento;
    }

    public void setRazonSocialEstablecimiento(String razonSocialEstablecimiento) {
        this.razonSocialEstablecimiento = razonSocialEstablecimiento;
    }

    public String getDatosPropietario() {
        return datosPropietario;
    }

    public void setDatosPropietario(String datosPropietario) {
        this.datosPropietario = datosPropietario;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCodeEstablecimiento() {
        return codeEstablecimiento;
    }

    public void setCodeEstablecimiento(String codeEstablecimiento) {
        this.codeEstablecimiento = codeEstablecimiento;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    public Date getDateValidity() {
        return dateValidity;
    }

    public void setDateValidity(Date dateValidity) {
        this.dateValidity = dateValidity;
    }

    @Override
    public String toString() {
        return "LocalComercial{" + "ruc=" + ruc + ", razonSocialEstablecimiento=" + razonSocialEstablecimiento + ", datosPropietario=" + datosPropietario + ", provincia=" + provincia + ", canton=" + canton + ", parroquia=" + parroquia + ", barrio=" + barrio + ", direccion=" + direccion + ", activity=" + activity + ", codeEstablecimiento=" + codeEstablecimiento + ", emissionDate=" + emissionDate + ", dateValidity=" + dateValidity + '}';
    }

}
