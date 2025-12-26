/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import jdk.jfr.Description;
import org.bcbg.util.Variables;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Anyelo
 */
public class Departamento implements Serializable {

    @Description("Clave primaria Departamento")
    private Long id;
    @Description("Nombre del departamento")
    private String nombre;
    @Description(Variables.omitirCampo)
    private Boolean direccion;
    @Description(Variables.omitirCampo)
    private Departamento padre;
    @Description(Variables.omitirCampo)
    private Boolean estado;
    @Description(Variables.omitirCampo)
    private String codigo;
    @Description(Variables.omitirCampo)
    private CtlgItem tipoUnidad;
    @Description(Variables.omitirCampo)
    private TreeNode nodeMenus;

    public Departamento() {
    }

    public Departamento(CtlgItem tipoUnidad) {
        this.estado = Boolean.TRUE;
        this.tipoUnidad = tipoUnidad;
    }

    public Departamento(Long id) {
        this.id = id;
    }

    public Departamento(Long id, Boolean estado) {
        this.id = id;
        this.estado = estado;
    }

    public Departamento(String nombre, Boolean estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public Departamento(String nombre, Boolean direccion, Boolean estado, String codigo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.estado = estado;
        this.codigo = codigo;

    }

    public TreeNode getNodeMenus() {
        return nodeMenus;
    }

    public void setNodeMenus(TreeNode nodeMenus) {
        this.nodeMenus = nodeMenus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getDireccion() {
        return direccion;
    }

    public void setDireccion(Boolean direccion) {
        this.direccion = direccion;
    }

    public Departamento getPadre() {
        return padre;
    }

    public void setPadre(Departamento padre) {
        this.padre = padre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CtlgItem getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(CtlgItem tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.GeDepartamento[ id=" + id + " ]";
    }

}
