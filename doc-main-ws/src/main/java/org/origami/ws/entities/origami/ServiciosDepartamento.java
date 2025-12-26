package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public", name = "servicio_departamento")
public class ServiciosDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "padre_item")
    private Long padreItem;
    private String nombre;
    private String abreviatura;
    private String usuario;
    private Date fecha;
    private Boolean online;
    private Integer diasRespuesta;
    private Boolean validar;
    @JoinColumn(name = "tipo_tramite_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramite tipoTramite;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento departamento;
    @Column(name = "url_imagen")
    private String urlImagen;
    private Integer hora;
    private Integer minutos;
    private Integer segundos;
    private Boolean interno;
    private Boolean estado;
    private Boolean externo;

    public ServiciosDepartamento() {
    }

    public ServiciosDepartamento(Long id) {
        this.id = id;
    }

    public ServiciosDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Boolean getExterno() {
        return externo;
    }

    public void setExterno(Boolean externo) {
        this.externo = externo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPadreItem() {
        return padreItem;
    }

    public void setPadreItem(Long padreItem) {
        this.padreItem = padreItem;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
/*  public ServiciosDepartamento getServiciosDepartamento() {
        return serviciosDepartamento;
    }

    public void setServiciosDepartamento(ServiciosDepartamento serviciosDepartamento) {
        this.serviciosDepartamento = serviciosDepartamento;
    }*/

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getDiasRespuesta() {
        return diasRespuesta;
    }

    public void setDiasRespuesta(Integer diasRespuesta) {
        this.diasRespuesta = diasRespuesta;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
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
}
