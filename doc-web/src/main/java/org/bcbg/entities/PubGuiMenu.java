/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Anyelo
 */
public class PubGuiMenu implements Serializable {

    private Integer id;
    private String nombre;
    private int numPosicion;
    private String hrefUrl;
    private String icono;
    private String prettyPattern;
    private String prettyId;
    private Collection<PubGuiMenuRol> pubGuiMenuRolCollection;
    private PubGuiMenubar menubar;
    private PubGuiMenuTipoAcceso tipoAcceso;
    private Collection<PubGuiMenu> pubGuiMenuCollection;
    private PubGuiMenu menuPadre;
    private Long idMenuPadre;
    private List<PubGuiMenu> menusHijos_byNumPosicion;

    public PubGuiMenu() {
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public PubGuiMenu(Integer id) {
        this.id = id;
    }

    public PubGuiMenu(Integer id, String nombre, int numPosicion) {
        this.id = id;
        this.nombre = nombre;
        this.numPosicion = numPosicion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumPosicion() {
        return numPosicion;
    }

    public void setNumPosicion(int numPosicion) {
        this.numPosicion = numPosicion;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public Collection<PubGuiMenuRol> getPubGuiMenuRolCollection() {
        return pubGuiMenuRolCollection;
    }

    public void setPubGuiMenuRolCollection(Collection<PubGuiMenuRol> pubGuiMenuRolCollection) {
        this.pubGuiMenuRolCollection = pubGuiMenuRolCollection;
    }

    public PubGuiMenubar getMenubar() {
        return menubar;
    }

    public void setMenubar(PubGuiMenubar menubar) {
        this.menubar = menubar;
    }

    public PubGuiMenuTipoAcceso getTipoAcceso() {
        return tipoAcceso;
    }

    public void setTipoAcceso(PubGuiMenuTipoAcceso tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }

    public Collection<PubGuiMenu> getPubGuiMenuCollection() {
        return pubGuiMenuCollection;
    }

    public void setPubGuiMenuCollection(Collection<PubGuiMenu> pubGuiMenuCollection) {
        this.pubGuiMenuCollection = pubGuiMenuCollection;
    }

    public PubGuiMenu getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(PubGuiMenu menuPadre) {
        this.menuPadre = menuPadre;
    }

    public List<PubGuiMenu> getMenusHijos_byNumPosicion() {
        return menusHijos_byNumPosicion;
    }

    public void setMenusHijos_byNumPosicion(List<PubGuiMenu> menusHijos_byNumPosicion) {
        this.menusHijos_byNumPosicion = menusHijos_byNumPosicion;
    }

    public Long getIdMenuPadre() {
        return idMenuPadre;
    }

    public void setIdMenuPadre(Long idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
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
        if (!(object instanceof PubGuiMenu)) {
            return false;
        }
        PubGuiMenu other = (PubGuiMenu) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "PubGuiMenu{" + "id=" + id + ", nombre=" + nombre + ", numPosicion=" + numPosicion + ", hrefUrl=" + hrefUrl + ", icono=" + icono + ", pubGuiMenuRolCollection=" + pubGuiMenuRolCollection + ", menubar=" + menubar + ", tipoAcceso=" + tipoAcceso + ", pubGuiMenuCollection=" + pubGuiMenuCollection + ", menuPadre=" + menuPadre + ", idMenuPadre=" + idMenuPadre + ", menusHijos_byNumPosicion=" + menusHijos_byNumPosicion + '}';
    }

    public String getPrettyPattern() {
        return prettyPattern;
    }

    public void setPrettyPattern(String prettyPattern) {
        this.prettyPattern = prettyPattern;
    }

    public String getPrettyId() {
        return prettyId;
    }

    public void setPrettyId(String prettyId) {
        this.prettyId = prettyId;
    }

}
