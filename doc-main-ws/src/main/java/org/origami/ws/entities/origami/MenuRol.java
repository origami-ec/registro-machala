package org.origami.ws.entities.origami;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.origami.ws.dto.Document;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "public", name = "menu_rol")
public class MenuRol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "es_director")
    private Boolean esDirector;
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Menu menu;
    private Long rol;
    private String acciones;
    @Transient
    private List<Document> documents;
    @Transient
    private String permiso;

    public MenuRol() {
    }

    public MenuRol(Long id) {
        this.id = id;
    }

    public MenuRol(Long id, Long rol) {
        this.id = id;
        this.rol = rol;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getEsDirector() {
        return esDirector;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getRol() {
        return rol;
    }

    public void setRol(Long rol) {
        this.rol = rol;
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
        if (!(object instanceof MenuRol)) {
            return false;
        }
        MenuRol other = (MenuRol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.origami.sgr.entities.PubGuiMenuRol[ id=" + id + " ]";
    }

}
