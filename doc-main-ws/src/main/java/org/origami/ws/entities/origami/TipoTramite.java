package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "tipo_tramite")
public class TipoTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private String activitykey;
    private Boolean estado;
    private String archivoBpmn;
    private String abreviatura;
    @JoinColumn(name = "departamento_id")
    @ManyToOne
    private Departamento departamento;
    @JoinColumn(name = "rol")
    @ManyToOne
    private Rol rol;
    private Integer dias;
    private Integer horas;
    private Integer minutos;
    private Integer segundos;
    private Integer orden;
    private Boolean interno;
    private String urlImagen;
    private String definicionTramite;
    private String color;

    public TipoTramite() {
    }

    public TipoTramite(Long id, String descripcion, String activitykey, Boolean estado,
                       String archivoBpmn, String abreviatura,
                       Integer dias, Integer horas, Integer minutos, Integer segundos,
                       Boolean interno, String urlImagen, Long idDep, String nombreDep,
                       Boolean estadoDep, String codigoDep, String definicion, String color) {
        this.id = id;
        this.descripcion = descripcion;
        this.activitykey = activitykey;
        this.estado = estado;
        this.archivoBpmn = archivoBpmn;
        this.abreviatura = abreviatura;
        this.departamento = new Departamento(idDep, nombreDep, estadoDep, codigoDep);
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.interno = interno;
        this.urlImagen = urlImagen;
        this.definicionTramite = definicion;
        this.color = color;
    }

    public TipoTramite(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public TipoTramite(Long id) {
        this.id = id;
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

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /*
     * public Boolean getTramiteVentanillaUnica() { return tramiteVentanillaUnica; }
     *
     * public void setTramiteVentanillaUnica(Boolean tramiteVentanillaUnica) {
     * this.tramiteVentanillaUnica = tramiteVentanillaUnica; }
     */

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }

    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "TipoTramite{" + "id=" + id + ", descripcion='" + descripcion + '\'' + ", activitykey='" + activitykey
                + '\'' + ", estado=" + estado + ", archivoBpmn='" + archivoBpmn + '\'' + ", abreviatura='" + abreviatura
                + '\'' + ", departamento=" + departamento + ", rol=" + rol + '}';
    }
}
