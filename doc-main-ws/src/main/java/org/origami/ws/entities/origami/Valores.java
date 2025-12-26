package org.origami.ws.entities.origami;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "valor")
public class Valores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String valorString;
    private Long valorNumeric;

    public Valores() {
    }

    public Valores(Long id) {
        this.id = id;
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

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public Long getValorNumeric() {
        return valorNumeric;
    }

    public void setValorNumeric(Long valorNumeric) {
        this.valorNumeric = valorNumeric;
    }

    @Override
    public String toString() {
        return "Valores{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", valorString='" + valorString + '\'' +
                ", valorNumeric=" + valorNumeric +
                '}';
    }
}