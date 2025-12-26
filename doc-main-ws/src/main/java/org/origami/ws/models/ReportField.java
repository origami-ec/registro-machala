package org.origami.ws.models;

import java.util.List;
import java.util.Objects;

public class ReportField {
    private String nombreTabla;
    private String detalle;
    private List<ReportFieldDet> campos;

    public ReportField() {
    }

    public ReportField(String nombreTabla, String detalle) {
        this.nombreTabla = nombreTabla;
        this.detalle = detalle;
    }

    public ReportField(String nombreTabla, String detalle, List<ReportFieldDet> campos) {
        this.nombreTabla = nombreTabla;
        this.detalle = detalle;
        this.campos = campos;
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
}
