package org.origami.docs.entity;

public class Usuario {
    private Long referenceId;
    private String nombres;
    private String usuario;

    public Usuario() {
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "referenceId=" + referenceId +
                ", nombres='" + nombres + '\'' +
                '}';
    }
}
