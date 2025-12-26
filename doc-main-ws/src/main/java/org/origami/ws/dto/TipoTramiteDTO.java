package org.origami.ws.dto;

public class TipoTramiteDTO {

    private Long id;
    private String descripcion;
    private String activitykey;
    private String carpeta;
    private String userDireccion;
    private Boolean estado;
    private String archivoBpmn;
    private String abreviatura;
    private DepartamentoDTO departamento;
    private RolDTO rol;
    private Boolean interno;
    private Boolean asignacionManual;
    private Boolean tieneDigitalizacion;
    private String definicionTramite;
    private String color;
    // private Boolean tramiteVentanillaUnica;

    public TipoTramiteDTO() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDefinicionTramite() {
        return definicionTramite;
    }

    public void setDefinicionTramite(String definicionTramite) {
        this.definicionTramite = definicionTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(String userDireccion) {
        this.userDireccion = userDireccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getTieneDigitalizacion() {
        return tieneDigitalizacion;
    }

    public void setTieneDigitalizacion(Boolean tieneDigitalizacion) {
        this.tieneDigitalizacion = tieneDigitalizacion;
    }

    public Boolean getAsignacionManual() {
        return asignacionManual;
    }

    public void setAsignacionManual(Boolean asignacionManual) {
        this.asignacionManual = asignacionManual;
    }

    public DepartamentoDTO getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoDTO departamento) {
        this.departamento = departamento;
    }

    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }

    /**
     * @return Boolean return the interno
     */
    public Boolean getInterno() {
        return interno;
    }

    /**
     * @param interno the interno to set
     */
    public void setInterno(Boolean interno) {
        this.interno = interno;
    }
    /*
     * public Boolean getTramiteVentanillaUnica() { return tramiteVentanillaUnica; }
     *
     * public void setTramiteVentanillaUnica(Boolean tramiteVentanillaUnica) {
     * this.tramiteVentanillaUnica = tramiteVentanillaUnica; }
     */
}
