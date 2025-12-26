package org.origami.ws.entities.rrhh;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RH_PERSONA", schema = "recurso_humano")
public class PersonaRH implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PES_ID")
    private Long id;
    @Column(name = "PES_CEDULA")
    private String cedula;
    @Column(name = "PES_NOMBRES")
    private String nombres;
    @Column(name = "PES_APELLIDOS")
    private String apellidos;
    @Column(name = "PES_GENERO")
    private String genero;
    @Column(name = "PES_DIRECCION_CASA")
    private String direccion;
    @Column(name = "PES_TELEFONO_CASA")
    private String telefono;
    @Column(name = "PES_CELULAR")
    private String celular;
    @Column(name = "PES_CEDULAMILITAR")
    private String cedulaMilitar;
    @Column(name = "PES_CORREO")
    private String correo;
    @Column(name = "PES_TITULO")
    private String titulo;
    @Column(name = "PES_FEC_ELI")
    private Date fechaEliminado;
    @Column(name = "PES_DETALLE_NOMB")
    private String detalleNombre;
    @Column(name = "PES_ELIMINADO")
    private Boolean eliminado;

    public PersonaRH() {
    }

    public PersonaRH(String cedula) {
        this.cedula = cedula;
    }

    public PersonaRH(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCedulaMilitar() {
        return cedulaMilitar;
    }

    public void setCedulaMilitar(String cedulaMilitar) {
        this.cedulaMilitar = cedulaMilitar;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaEliminado() {
        return fechaEliminado;
    }

    public void setFechaEliminado(Date fechaEliminado) {
        this.fechaEliminado = fechaEliminado;
    }

    public String getDetalleNombre() {
        return detalleNombre;
    }

    public void setDetalleNombre(String detalleNombre) {
        this.detalleNombre = detalleNombre;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", cedula='" + cedula + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", genero='" + genero + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", celular='" + celular + '\'' +
                ", cedulaMilitar='" + cedulaMilitar + '\'' +
                ", correo='" + correo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", fechaEliminado=" + fechaEliminado +
                ", detalleNombre='" + detalleNombre + '\'' +
                ", eliminado=" + eliminado +
                '}';
    }
}
