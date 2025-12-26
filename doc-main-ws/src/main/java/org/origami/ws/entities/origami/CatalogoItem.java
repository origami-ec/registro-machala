package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "public", name = "item")
public class CatalogoItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String valor;
    private String codename;
    private Long referencia;
    private String estado = "A";
    @JoinColumn(name = "catalogo_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Catalogo catalogo;

    public CatalogoItem() {
    }

    public CatalogoItem(Long id) {
        this.id = id;
    }

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
        return "CatalogoItem{" +
                "id=" + id +
                ", valor='" + valor + '\'' +
                ", codename='" + codename + '\'' +
                ", referencia=" + referencia +
                ", estado='" + estado + '\'' +
                ", catalogo=" + catalogo +
                '}';
    }
}
