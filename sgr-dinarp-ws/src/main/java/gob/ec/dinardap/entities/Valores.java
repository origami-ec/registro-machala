package gob.ec.dinardap.entities;

import javax.persistence.*;

@Entity
@Table(name = "valores", schema = "conf")
public class Valores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String valorString;
    private Double valorNumeric;

    public Valores() {
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

    public Double getValorNumeric() {
        return valorNumeric;
    }

    public void setValorNumeric(Double valorNumeric) {
        this.valorNumeric = valorNumeric;
    }
}
