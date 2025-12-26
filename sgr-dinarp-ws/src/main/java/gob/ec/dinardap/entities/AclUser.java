package gob.ec.dinardap.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Xndy
 */
@Entity
@Table(name = "usuarios_app", schema = "catastro")
public class AclUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private boolean sisEnabled;
    private String clave;
    private String registrador;

    public AclUser() {
    }

    public AclUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRegistrador() {
        return registrador;
    }

    public void setRegistrador(String registrador) {
        this.registrador = registrador;
    }
}
