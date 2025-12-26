package org.origami.ws.entities.databcbg;

import javax.persistence.*;

@Entity
@Table(schema = "dbo", name = "AF_COMPANIA")
public class CompaniaBCBG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COM_ID")
    private Long id;
    @Column(name = "COM_CODIGO")
    private String codigo;
    @Column(name = "COM_NOMBRE")
    private String nombre;
    @Column(name = "COM_ELIMINADO")
    private Boolean estado;

    public CompaniaBCBG() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
