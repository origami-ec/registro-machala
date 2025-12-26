package org.origami.ws.entities.rrhh;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RH_ACCIONDEPERSONAL", schema = "recurso_humano")
public class AccionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACP_ID")
    private Long id;
    @Column(name = "ACP_NUMERO")
    private String numero;
    @Column(name = "ACP_RRHH_ORIGEN_ID")
    private Long orgigenId;
    @Column(name = "ACP_RRHH_DESTINO_ID")
    private Long destinoId;
    @Column(name = "ACP_CARGO_ORIGEN")
    private Long cargoOrigen;
    @Column(name = "ACP_CARGO_ORIGEN_DESC")
    private String cargoOrigenDesc;
    @Column(name = "ACP_CARGO_DESTINO")
    private Long cargoDestino;
    @Column(name = "ACP_CARGO_DESTINO_DESC")
    private String cargoDestinoDesc;
    @Column(name = "ACP_FECHAINI")
    private Date fechaDesde;
    @Column(name = "ACP_FECHAFIN")
    private Date fechaHasta;

    @Column(name = "ACP_TIPO")
    private Long tipo;
    @Column(name = "ACP_TIPO_DESC")
    private String tipoDesc;
    @Column(name = "ACP_ELIMINADO")
    private Boolean eliminado;

    public AccionPersonal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getOrgigenId() {
        return orgigenId;
    }

    public void setOrgigenId(Long orgigenId) {
        this.orgigenId = orgigenId;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }

    public Long getCargoOrigen() {
        return cargoOrigen;
    }

    public void setCargoOrigen(Long cargoOrigen) {
        this.cargoOrigen = cargoOrigen;
    }

    public String getCargoOrigenDesc() {
        return cargoOrigenDesc;
    }

    public void setCargoOrigenDesc(String cargoOrigenDesc) {
        this.cargoOrigenDesc = cargoOrigenDesc;
    }

    public Long getCargoDestino() {
        return cargoDestino;
    }

    public void setCargoDestino(Long cargoDestino) {
        this.cargoDestino = cargoDestino;
    }

    public String getCargoDestinoDesc() {
        return cargoDestinoDesc;
    }

    public void setCargoDestinoDesc(String cargoDestinoDesc) {
        this.cargoDestinoDesc = cargoDestinoDesc;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getTipoDesc() {
        return tipoDesc;
    }

    public void setTipoDesc(String tipoDesc) {
        this.tipoDesc = tipoDesc;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "AccionPersonal{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", orgigenId=" + orgigenId +
                ", destinoId=" + destinoId +
                ", cargoOrigen=" + cargoOrigen +
                ", cargoOrigenDesc='" + cargoOrigenDesc + '\'' +
                ", cargoDestino=" + cargoDestino +
                ", cargoDestinoDesc='" + cargoDestinoDesc + '\'' +
                ", fechaDesde=" + fechaDesde +
                ", fechaHasta=" + fechaHasta +
                ", tipo=" + tipo +
                ", tipoDesc='" + tipoDesc + '\'' +
                ", eliminado=" + eliminado +
                '}';
    }
}
