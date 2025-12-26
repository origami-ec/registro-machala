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
 * @author Autor
 */
public class IndiceRevision implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String usuario;
    private String nombreRevisor;
    private BigInteger asignados;
    private BigInteger revisadosATiempo;
    private BigInteger revisadosFueraTiempo;
    private BigInteger reasignados;
    private BigInteger transferidos;
    private BigInteger devolutivas;
    private BigInteger negativas;
    private BigInteger habilitados;
    private BigInteger pendientes;
    
    
    public IndiceRevision() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreRevisor() {
        return nombreRevisor;
    }

    public void setNombreRevisor(String nombreRevisor) {
        this.nombreRevisor = nombreRevisor;
    }

    public BigInteger getAsignados() {
        return asignados;
    }

    public void setAsignados(BigInteger asignados) {
        this.asignados = asignados;
    }

    public BigInteger getRevisadosATiempo() {
        return revisadosATiempo;
    }

    public void setRevisadosATiempo(BigInteger revisadosATiempo) {
        this.revisadosATiempo = revisadosATiempo;
    }

    public BigInteger getRevisadosFueraTiempo() {
        return revisadosFueraTiempo;
    }

    public void setRevisadosFueraTiempo(BigInteger revisadosFueraTiempo) {
        this.revisadosFueraTiempo = revisadosFueraTiempo;
    }

    public BigInteger getReasignados() {
        return reasignados;
    }

    public void setReasignados(BigInteger reasignados) {
        this.reasignados = reasignados;
    }

    public BigInteger getTransferidos() {
        return transferidos;
    }

    public void setTransferidos(BigInteger transferidos) {
        this.transferidos = transferidos;
    }

    public BigInteger getDevolutivas() {
        return devolutivas;
    }

    public void setDevolutivas(BigInteger devolutivas) {
        this.devolutivas = devolutivas;
    }

    public BigInteger getNegativas() {
        return negativas;
    }

    public void setNegativas(BigInteger negativas) {
        this.negativas = negativas;
    }

    public BigInteger getHabilitados() {
        return habilitados;
    }

    public void setHabilitados(BigInteger habilitados) {
        this.habilitados = habilitados;
    }
    public BigInteger getPendientes() {
        return pendientes;
    }

    public void setPendientes(BigInteger pendientes) {
        this.pendientes = pendientes;
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

        IndiceRevision other = (IndiceRevision) obj;
        return Objects.equals(this.usuario, other.usuario);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndiceRevision{");
        sb.append("usuario=").append(usuario);
        sb.append(", nombreRevisor=").append(nombreRevisor);
        sb.append(", asignados=").append(asignados);
        sb.append(", revisadosATiempo=").append(revisadosATiempo);
        sb.append(", revisadosFueraTiempo=").append(revisadosFueraTiempo);
        sb.append(", reasignados=").append(reasignados);
        sb.append(", transferidos=").append(transferidos);
        sb.append(", devolutivas=").append(devolutivas);
        sb.append(", negativas=").append(negativas);
        sb.append(", habilitados=").append(habilitados);
        sb.append(", pendientes=").append(pendientes);
        sb.append('}');
        return sb.toString();
    }
}
