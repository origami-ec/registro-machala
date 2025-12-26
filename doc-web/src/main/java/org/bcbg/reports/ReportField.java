/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.reports;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author ORIGAMI
 */
public class ReportField {

    private String nombreTabla;
    private String detalle;
    private Boolean tablaHecho;
    private List<ReportFieldDet> campos;

    public ReportField() {
    }

    public ReportField(String nombreTabla, Boolean tablaHecho, String detalle) {
        this.nombreTabla = nombreTabla;
        this.tablaHecho = tablaHecho;
        this.detalle = detalle;
    }

    public ReportField(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public ReportField(String nombreTabla, Boolean tablaHecho, String detalle, List<ReportFieldDet> campos) {
        this.nombreTabla = nombreTabla;
        this.detalle = detalle;
        this.campos = campos;
        this.tablaHecho = tablaHecho;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<ReportFieldDet> getCampos() {
        return campos;
    }

    public void setCampos(List<ReportFieldDet> campos) {
        this.campos = campos;
    }

    public Boolean getTablaHecho() {
        return tablaHecho;
    }

    public void setTablaHecho(Boolean tablaHecho) {
        this.tablaHecho = tablaHecho;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.nombreTabla);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportField other = (ReportField) obj;
        return Objects.equals(this.nombreTabla, other.nombreTabla);
    }

    @Override
    public String toString() {
        return "ReportField{" + "nombreTabla=" + nombreTabla + ", detalle=" + detalle + ", tablaHecho=" + tablaHecho + ", campos=" + campos + '}';
    }

}
