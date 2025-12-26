package org.origami.zull.service;

public class JwtDesk {

    private final String jwttoken;
    private final Long id;
    private final String usuario;
    private final String nombres;

    public JwtDesk(String jwttoken, Long id, String usuario, String nombres) {
        this.jwttoken = jwttoken;
        this.id = id;
        this.usuario = usuario;
        this.nombres = nombres;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombres() {
        return nombres;
    }
}
