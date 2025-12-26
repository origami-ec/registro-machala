package org.origami.ws.entities.security;

import org.origami.ws.entities.rrhh.RecursoHumano;

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

    @Transient
    private RecursoHumano recursoHumano;
    @Transient
    private String cargo;
    @Transient
    private Boolean disponible;
    @Transient
    private String estadoDisponible;
    @Transient
    private Long departamentoId;
    @Transient
    private String departamento;


    public Usuario(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, String usuarioNombre, String clave, Boolean eliminado) {
        this.id = id;
        this.usuarioNombre = usuarioNombre;
        this.clave = clave;
        this.eliminado = eliminado;
    }

    public Usuario(Long id, String usuarioNombre, String clave, Boolean eliminado, Long usuarioRRHH) {
        this.id = id;
        this.usuarioNombre = usuarioNombre;
        this.clave = clave;
        this.eliminado = eliminado;
        this.usuarioRRHH = usuarioRRHH;
    }

    public Usuario() {}

    public Usuario(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public RecursoHumano getRecursoHumano() {
        return recursoHumano;
    }

    public void setRecursoHumano(RecursoHumano recursoHumano) {
        this.recursoHumano = recursoHumano;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getEstadoDisponible() {
        return estadoDisponible;
    }

    public void setEstadoDisponible(String estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
