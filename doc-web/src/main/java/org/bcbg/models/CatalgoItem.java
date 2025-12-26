/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.models;

/**
 *
 * @author Criss Intriago
 */
public class CatalgoItem {
    private Long id;
    private String valor;
    private String codename;
    private Long referencia;
    private String estado ;
    private Catalogo catalogo;
    
    public CatalgoItem(){}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    @Override
    public String toString() {
        return "CatalgoItem{" + "id=" + id + ", valor=" + valor + ", codename=" + codename + ", referencia=" + referencia + ", estado=" + estado + ", catalogo=" + catalogo + '}';
    }
    
}
