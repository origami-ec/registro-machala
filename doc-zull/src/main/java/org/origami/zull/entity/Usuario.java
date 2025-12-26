package org.origami.zull.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SG_USUARIO", schema = "seguridad")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USU_ID")
    private Long id;
    @Column(name = "USU_NOMBRE")
    private String usuarioNombre;
    @Column(name = "USU_PASSWORD")
    private String password;
    @Column(name = "USU_CLAVE")
    private String clave;
    @Column(name = "USU_NOMBRE_M")
    private String nombreUsuario;
    @Column(name = "USU_RRHH")
    private Long usuarioRRHH;
    @Column(name = "USU_ELIMINADO")
    private Boolean eliminado;

    public Usuario() {
    }

    public Usuario(String usuarioNombre, String clave, Boolean eliminado) {
        this.usuarioNombre = usuarioNombre;
        this.clave = clave;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Long getUsuarioRRHH() {
        return usuarioRRHH;
    }

    public void setUsuarioRRHH(Long usuarioRRHH) {
        this.usuarioRRHH = usuarioRRHH;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", password='" + password + '\'' +
                ", clave='" + clave + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", usuarioRRHH=" + usuarioRRHH +
                ", eliminado=" + eliminado +
                '}';
    }
}
