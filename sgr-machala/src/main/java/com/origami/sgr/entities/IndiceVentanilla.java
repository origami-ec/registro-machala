/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 *
 * @author Usuario
 */
public class IndiceVentanilla implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String usuario;
    private String nombreFuncionario;
    private BigInteger totalIngresos;
    private BigInteger inscripcionSinValor;
    private BigInteger inscripcionConValor;
    private BigInteger certificadoSinValor;
    private BigInteger certificadoConValor;

    public IndiceVentanilla() {
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

    public BigInteger getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigInteger totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigInteger getInscripcionSinValor() {
        return inscripcionSinValor;
    }

    public void setInscripcionSinValor(BigInteger inscripcionSinValor) {
        this.inscripcionSinValor = inscripcionSinValor;
    }

    public BigInteger getInscripcionConValor() {
        return inscripcionConValor;
    }

    public void setInscripcionConValor(BigInteger inscripcionConValor) {
        this.inscripcionConValor = inscripcionConValor;
    }

    public BigInteger getCertificadoSinValor() {
        return certificadoSinValor;
    }

    public void setCertificadoSinValor(BigInteger certificadoSinValor) {
        this.certificadoSinValor = certificadoSinValor;
    }

    public BigInteger getCertificadoConValor() {
        return certificadoConValor;
    }

    public void setCertificadoConValor(BigInteger certificadoConValor) {
        this.certificadoConValor = certificadoConValor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        IndiceVentanilla other = (IndiceVentanilla) obj;
        return Objects.equals(this.usuario, other.usuario);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndiceVentanilla{");
        sb.append("usuario=").append(usuario);
        sb.append(", nombreFuncionario=").append(nombreFuncionario);
        sb.append(", totalIngresos=").append(totalIngresos);
        sb.append(", inscripcionSinValor=").append(inscripcionSinValor);
        sb.append(", inscripcionConValor=").append(inscripcionConValor);
        sb.append(", certificadoSinValor=").append(certificadoSinValor);
        sb.append(", certificadoConValor=").append(certificadoConValor);
        sb.append('}');
        return sb.toString();
    }
    
}
