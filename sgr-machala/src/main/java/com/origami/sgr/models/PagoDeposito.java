/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import com.origami.sgr.entities.RenEntidadBancaria;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author Usuario
 */
public class PagoDeposito implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private RenEntidadBancaria banco;
    private String referencia;
    private BigDecimal valor= new BigDecimal("0.00");
    private Date fecha;
    private Integer tipoPago;
    
    public PagoDeposito(){
    }

    public RenEntidadBancaria getBanco() {
        return banco;
    }

    public void setBanco(RenEntidadBancaria banco) {
        this.banco = banco;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Integer tipoPago) {
        this.tipoPago = tipoPago;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PagoDeposito)){
            return false;
        }
        
        PagoDeposito other = (PagoDeposito) object;
        return !((this.banco == null && other.banco != null) || (this.banco != null && !this.banco.equals(other.banco))
                || (this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))
                || (this.referencia == null && other.referencia != null) || (this.referencia != null && !this.referencia.equals(other.referencia))
                || (this.valor == null && other.valor != null) || (this.valor != null && !this.valor.equals(other.valor)));
    }
    
    
}
