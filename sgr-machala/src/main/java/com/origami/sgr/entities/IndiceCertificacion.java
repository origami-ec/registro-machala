/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class IndiceCertificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String nombreFuncionario;

    private BigInteger asignados;
    private BigInteger reasignadosQuitar;
    private BigInteger reasignadosMas;
    private BigInteger entregadosTiempo;
    private BigInteger entregadosAtras;
    private BigInteger pendientes;

    private BigDecimal porcentRendimiento;

    public IndiceCertificacion() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }

    public BigInteger getAsignados() {
        return asignados;
    }

    public void setAsignados(BigInteger asignados) {
        this.asignados = asignados;
    }

    public BigInteger getReasignadosQuitar() {
        return reasignadosQuitar;
    }

    public void setReasignadosQuitar(BigInteger reasignadosQuitar) {
        this.reasignadosQuitar = reasignadosQuitar;
    }

    public BigInteger getReasignadosMas() {
        return reasignadosMas;
    }

    public void setReasignadosMas(BigInteger reasignadosMas) {
        this.reasignadosMas = reasignadosMas;
    }

    public BigInteger getEntregadosTiempo() {
        return entregadosTiempo;
    }

    public void setEntregadosTiempo(BigInteger entregadosTiempo) {
        this.entregadosTiempo = entregadosTiempo;
    }

    public BigInteger getEntregadosAtras() {
        return entregadosAtras;
    }

    public void setEntregadosAtras(BigInteger entregadosAtras) {
        this.entregadosAtras = entregadosAtras;
    }

    public BigInteger getPendientes() {
        return pendientes;
    }

    public void setPendientes(BigInteger pendientes) {
        this.pendientes = pendientes;
    }

    public BigDecimal getPorcentRendimiento() {
        return porcentRendimiento;
    }

    public void setPorcentRendimiento(BigDecimal porcentRendimiento) {
        this.porcentRendimiento = porcentRendimiento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario);
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

        IndiceCertificacion other = (IndiceCertificacion) obj;
        return Objects.equals(this.usuario, other.usuario);
    }

    @Override
    public String toString() {
        return "IndiceCertificacion{" +
                "usuario='" + usuario + '\'' +
                ", nombreFuncionario='" + nombreFuncionario + '\'' +
                ", asignados=" + asignados +
                ", reasignadosQuitar=" + reasignadosQuitar +
                ", reasignadosMas=" + reasignadosMas +
                ", entregadosTiempo=" + entregadosTiempo +
                ", entregadosAtras=" + entregadosAtras +
                ", pendientes=" + pendientes +
                ", porcentRendimiento=" + porcentRendimiento +
                '}';
    }

}
