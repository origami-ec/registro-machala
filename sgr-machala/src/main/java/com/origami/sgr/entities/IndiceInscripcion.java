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
 * @author autor
 */
public class IndiceInscripcion implements Serializable {
 private static final long serialVersionUID = 1L;
 
 private String usuario;
 private String nombreFuncionario;
 private BigInteger asignados;
 private BigInteger entregadosATiempo;
 private BigInteger entregadosConRetraso;
 private BigInteger reasignados;
 private BigInteger transferidos;
 private BigInteger pendientes;

  public IndiceInscripcion() {
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

    public BigInteger getEntregadosATiempo() {
        return entregadosATiempo;
    }

    public void setEntregadosATiempo(BigInteger entregadosATiempo) {
        this.entregadosATiempo = entregadosATiempo;
    }

    public BigInteger getEntregadosConRetraso() {
        return entregadosConRetraso;
    }

    public void setEntregadosConRetraso(BigInteger entregadosConRetraso) {
        this.entregadosConRetraso = entregadosConRetraso;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IndiceInscripcion other = (IndiceInscripcion) obj;
        return Objects.equals(this.usuario, other.usuario);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndiceInscripcion{");
        sb.append("usuario=").append(usuario);
        sb.append(", nombreFuncionario=").append(nombreFuncionario);
        sb.append(", asignados=").append(asignados);
        sb.append(", entregadosATiempo=").append(entregadosATiempo);
        sb.append(", entregadosConRetraso=").append(entregadosConRetraso);
        sb.append(", reasignados=").append(reasignados);
        sb.append(", transferidos=").append(transferidos);
        sb.append(", pendientes=").append(pendientes);
        sb.append('}');
        return sb.toString();
    }
}
