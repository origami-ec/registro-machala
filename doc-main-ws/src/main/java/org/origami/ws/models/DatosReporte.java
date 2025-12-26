/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.origami.ws.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Origami
 */
public class DatosReporte implements Serializable {

    private String usuario;
    private String departamento;
    private Long fechadesde;
    private Long fechahasta;
    private Boolean incluirFechas;
    private String nombreArchivoPDF;
    private Map parametros;
    private List<ReportFieldDet> campos;

    public String getCandidato() {
        return "%" + usuario + "%";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Long fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Long getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Long fechahasta) {
        this.fechahasta = fechahasta;
    }

    public Map getParametros() {
        return parametros;
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public String getNombreArchivoPDF() {
        return nombreArchivoPDF;
    }

    public void setNombreArchivoPDF(String nombreArchivoPDF) {
        this.nombreArchivoPDF = nombreArchivoPDF;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<ReportFieldDet> getCampos() {
        return campos;
    }

    public void setCampos(List<ReportFieldDet> campos) {
        this.campos = campos;
    }

    public Boolean getIncluirFechas() {
        return incluirFechas;
    }

    public void setIncluirFechas(Boolean incluirFechas) {
        this.incluirFechas = incluirFechas;
    }

    @Override
    public String toString() {
        return "DatosReporte{" +
                "usuario='" + usuario + '\'' +
                ", departamento='" + departamento + '\'' +
                ", fechadesde=" + fechadesde +
                ", fechahasta=" + fechahasta +
                ", nombreArchivoPDF='" + nombreArchivoPDF + '\'' +
                ", parametros=" + parametros +
                '}';
    }
}
