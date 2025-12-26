package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "menu_tipo_acceso")
public class MenuTipoAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "identificador")
    private String identificador;


    public MenuTipoAcceso() {
    }

    public MenuTipoAcceso(Integer id) {
        this.id = id;
    }

    public MenuTipoAcceso(Integer id, String identificador) {
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuTipoAcceso)) {
            return false;
        }
        MenuTipoAcceso other = (MenuTipoAcceso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenuTipoAcceso[ id=" + id + " ]";
    }

}
