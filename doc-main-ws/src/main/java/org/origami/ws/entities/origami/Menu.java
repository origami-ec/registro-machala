package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(schema = "public", name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "pretty_pattern")
    private String prettyPattern;
    @Column(name = "pretty_id")
    private String prettyId;
    @Basic(optional = false)
    @Column(name = "num_posicion")
    private Integer numPosicion;
    @Column(name = "href_url")
    private String hrefUrl;
    @Column(name = "icono")
    private String icono;
    @JoinColumn(name = "menubar_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Menubar menubar;
    @JoinColumn(name = "tipo_acceso_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private MenuTipoAcceso tipoAcceso;
    @JoinColumn(name = "menu_padre_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Menu menuPadre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu", fetch = FetchType.EAGER)
    private Collection<MenuRol> menuRolCollection;
    @Transient
    private Long idMenuPadre;
    @Transient
    private List<Menu> menusHijos_byNumPosicion;

    public Menu() {
    }

    public Menu(Menubar menubar) {
        this.menubar = menubar;
    }


    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }


    public Menu(Integer id) {
        this.id = id;
    }

    public Menu(Integer id, String nombre, Integer numPosicion) {
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

    public Integer getNumPosicion() {
        return numPosicion;
    }

    public void setNumPosicion(Integer numPosicion) {
        this.numPosicion = numPosicion;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public Menubar getMenubar() {
        return menubar;
    }

    public void setMenubar(Menubar menubar) {
        this.menubar = menubar;
    }

    public MenuTipoAcceso getTipoAcceso() {
        return tipoAcceso;
    }

    public void setTipoAcceso(MenuTipoAcceso tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }


    public Menu getMenuPadre() {
        return menuPadre;
    }

    public void setMenuPadre(Menu menuPadre) {
        this.menuPadre = menuPadre;
    }

    public List<Menu> getMenusHijos_byNumPosicion() {
        return menusHijos_byNumPosicion;
    }

    public void setMenusHijos_byNumPosicion(List<Menu> menusHijos_byNumPosicion) {
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
        Integer hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public Collection<MenuRol> getPubGuiMenuRolCollection() {
        return menuRolCollection;
    }

    public void setPubGuiMenuRolCollection(Collection<MenuRol> menuRolCollection) {
        this.menuRolCollection = menuRolCollection;
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

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", prettyPattern='" + prettyPattern + '\'' +
                ", prettyId='" + prettyId + '\'' +
                ", numPosicion=" + numPosicion +
                ", hrefUrl='" + hrefUrl + '\'' +
                ", icono='" + icono + '\'' +
                ", menubar=" + menubar +
                ", tipoAcceso=" + tipoAcceso +
                ", menuPadre=" + menuPadre +
                ", menuRolCollection=" + menuRolCollection +
                ", idMenuPadre=" + idMenuPadre +
                ", menusHijos_byNumPosicion=" + menusHijos_byNumPosicion +
                '}';
    }
}
