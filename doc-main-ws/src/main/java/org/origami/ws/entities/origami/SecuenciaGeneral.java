package org.origami.ws.entities.origami;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(schema = "public", name = "secuencia_general")
public class SecuenciaGeneral implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigInteger secuencia;
    private Integer anio;
    private Long departamento;

    public SecuenciaGeneral() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "SecuenciaGeneral{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", secuencia=" + secuencia +
                ", anio=" + anio +
                '}';
    }
}
