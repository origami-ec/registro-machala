package org.origami.ws.dto;

import java.util.Date;

public class UsuarioDTO {

    private Long id;
    private String user;
    private String pass;
    private Boolean sisEnabled;
    private Boolean userIsDirector;
    private String rutaImagen;
    private String imagenPerfil;
    private Boolean esSuperUser;
    private CatEnteDTO ente;
    private Date fechaActPass;
    private Boolean caducadaPass;
    private String usuarioEncriptado;

    public UsuarioDTO() {
    }

    public String getUsuarioEncriptado() {
        return usuarioEncriptado;
    }

    public void setUsuarioEncriptado(String usuarioEncriptado) {
        this.usuarioEncriptado = usuarioEncriptado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(Boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public Boolean getUserIsDirector() {
        return userIsDirector;
    }

    public void setUserIsDirector(Boolean userIsDirector) {
        this.userIsDirector = userIsDirector;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public Boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(Boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    public CatEnteDTO getEnte() {
        return ente;
    }

    public void setEnte(CatEnteDTO ente) {
        this.ente = ente;
    }

    public Date getFechaActPass() {
        return fechaActPass;
    }

    public void setFechaActPass(Date fechaActPass) {
        this.fechaActPass = fechaActPass;
    }

    public Boolean getCaducadaPass() {
        return caducadaPass;
    }

    public void setCaducadaPass(Boolean caducadaPass) {
        this.caducadaPass = caducadaPass;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", sisEnabled=" + sisEnabled +
                ", userIsDirector=" + userIsDirector +
                ", rutaImagen='" + rutaImagen + '\'' +
                ", imagenPerfil='" + imagenPerfil + '\'' +
                ", esSuperUser=" + esSuperUser +
                ", ente=" + ente +
                ", fechaActPass=" + fechaActPass +
                ", caducadaPass=" + caducadaPass +
                '}';
    }
}
