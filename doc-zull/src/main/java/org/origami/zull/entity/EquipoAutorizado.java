package org.origami.zull.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "equipo_autorizado", schema = "documental")
public class EquipoAutorizado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreEquipo;
    private String ip;
    private String mac;
    private int rango;
    private int excluir;

    public EquipoAutorizado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public int getExcluir() {
        return excluir;
    }

    public void setExcluir(int excluir) {
        this.excluir = excluir;
    }

}
