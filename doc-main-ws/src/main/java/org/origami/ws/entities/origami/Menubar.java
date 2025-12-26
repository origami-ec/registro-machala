package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "public", name = "menu_bar")
public class Menubar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "identificador")
    private String identificador;

    @Transient
    private List<Menu> menuListSoyMenubar_byOrden;

    public Menubar() {
    }

    public Menubar(Integer id) {
        this.id = id;
    }

    public Menubar(Integer id, String identificador) {
        this.id = id;
        this.identificador = identificador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public List<Menu> getMenuListSoyMenubar_byOrden() {
        return menuListSoyMenubar_byOrden;
    }

    public void setMenuListSoyMenubar_byOrden(List<Menu> menuListSoyMenubar_byOrden) {
        this.menuListSoyMenubar_byOrden = menuListSoyMenubar_byOrden;
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
        if (!(object instanceof Menubar)) {
            return false;
        }
        Menubar other = (Menubar) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenubar[ id=" + id + " ]";
    }

}
