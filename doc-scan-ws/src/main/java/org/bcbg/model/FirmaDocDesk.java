package org.bcbg.model;

public class FirmaDocDesk {

    private String archivo;
    private String token; //ES EL ALIAS Y EN EL CASO DE P12 SERA EL ARCHIVO
    private String clave; //ES EL ALIAS
    private String pagina;

    private String ubicacion;
    private String posicionX;
    private String posicionY;
    private String tipo; //ARCHIVO - TOKEN

    private String usuario;
    private String usuarioNombre;
    private Long usuarioId;

    public FirmaDocDesk() {
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(String posicionX) {
        this.posicionX = posicionX;
    }

    public String getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(String posicionY) {
        this.posicionY = posicionY;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return "FirmaDocDesk{" +
                "archivo='" + archivo + '\'' +
                ", token='" + token + '\'' +
                ", clave='" + clave + '\'' +
                ", pagina='" + pagina + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", posicionX='" + posicionX + '\'' +
                ", posicionY='" + posicionY + '\'' +
                ", tipo='" + tipo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", usuarioId=" + usuarioId +
                '}';

    }
}
