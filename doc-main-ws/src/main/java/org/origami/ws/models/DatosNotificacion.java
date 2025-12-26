package org.origami.ws.models;

import java.io.Serializable;
import java.util.Date;

public class DatosNotificacion implements Serializable {

    private Date fecha;
    private String documento;
    private String codigo;
    private String contenido;
    private String departamento;

    public DatosNotificacion() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "DatosNotificacion{" +
                "fecha=" + fecha +
                ", documento='" + documento + '\'' +
                ", codigo='" + codigo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", departamento='" + departamento + '\'' +
                '}';
    }
}
