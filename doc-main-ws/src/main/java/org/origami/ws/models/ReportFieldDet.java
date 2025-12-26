package org.origami.ws.models;

import java.util.Objects;

public class ReportFieldDet {
    private String clazz;
    private String field;
    private String typeField;
    private String detailField;
    private Boolean esObjecto;
    private Boolean tablaHecho;
    private String operador;
    private String valor;

    public ReportFieldDet() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDetailField() {
        return detailField;
    }

    public void setDetailField(String detailField) {
        this.detailField = detailField;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getTypeField() {
        return typeField;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    public Boolean getEsObjecto() {
        return esObjecto;
    }

    public void setEsObjecto(Boolean esObjecto) {
        this.esObjecto = esObjecto;
    }

    public Boolean getTablaHecho() {
        return tablaHecho;
    }

    public void setTablaHecho(Boolean tablaHecho) {
        this.tablaHecho = tablaHecho;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.clazz);
        hash = 83 * hash + Objects.hashCode(this.field);
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
        final ReportFieldDet other = (ReportFieldDet) obj;
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        return Objects.equals(this.field, other.field);
    }

    @Override
    public String toString() {
        return "ReportFieldDet{" +
                "clazz='" + clazz + '\'' +
                ", field='" + field + '\'' +
                ", typeField='" + typeField + '\'' +
                ", detailField='" + detailField + '\'' +
                ", esObjecto=" + esObjecto +
                ", tablaHecho=" + tablaHecho +
                ", operador='" + operador + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
