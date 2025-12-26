/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Anyelo
 */
@Entity
@Table(name = "acl_user", schema = "app")
public class AclUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 50)
    @Column(name = "pass")
    private String pass;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sis_enabled")
    private boolean sisEnabled;
    @Column(name = "user_is_director")
    private Boolean userIsDirector;
    @Size(max = 250)
    @Column(name = "ruta_imagen")
    private String rutaImagen;
    @Size(max = 500)
    @Column(name = "imagen_perfil")
    private String imagenPerfil;
    @Basic(optional = false)
    @NotNull
    @Column(name = "es_super_user")
    private boolean esSuperUser;
    @JoinTable(name = "acl_user_has_rol", joinColumns = {
        @JoinColumn(name = "acl_user", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "acl_rol", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<AclRol> aclRolCollection;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatEnte ente;
    @OneToMany(mappedBy = "userCreador", fetch = FetchType.LAZY)
    private Collection<RegMovimiento> regMovimientoCollection;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Collection<RenCajero> renCajeroCollection;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Collection<AclRegistroUser> aclRegistroUserCollection;
    @Column(name = "code")
    private String code;
    @Column(name = "dashboard")
    private boolean dashboard = true;
    @Column(name = "clave")
    private String clave;
    @Column(name = "user_documental")
    private String userDocumental;
    @Column(name = "pass_documental")
    private String passDocumental;
    @Column(name = "fecha_act_pass")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActPass = new Date();
    @Column(name = "caducada_pass")
    private boolean caducadaPass = false;

    @JoinColumn(name = "firma", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FirmaElectronica firma;
    @Column(name = "firma_nombre")
    private String firmaNombre;
    @Column(name = "firma_cargo")
    private String firmaCargo;

    public AclUser() {
    }

    public AclUser(Long id) {
        this.id = id;
    }

    public AclUser(Long id, String usuario, boolean sisEnabled, boolean esSuperUser) {
        this.id = id;
        this.usuario = usuario;
        this.sisEnabled = sisEnabled;
        this.esSuperUser = esSuperUser;
    }

    public String getNombreReportes() {
        return this.firmaNombre + "\n" + this.firmaCargo;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean getSisEnabled() {
        return sisEnabled;
    }

    public void setSisEnabled(boolean sisEnabled) {
        this.sisEnabled = sisEnabled;
    }

    public Boolean getUserIsDirector() {
        return userIsDirector;
    }

    public void setUserIsDirector(Boolean userIsDirector) {
        this.userIsDirector = userIsDirector;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    public Collection<AclRol> getAclRolCollection() {
        return aclRolCollection;
    }

    public void setAclRolCollection(Collection<AclRol> aclRolCollection) {
        this.aclRolCollection = aclRolCollection;
    }

    public CatEnte getEnte() {
        return ente;
    }

    public void setEnte(CatEnte ente) {
        this.ente = ente;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection<RegMovimiento> getRegMovimientoCollection() {
        return regMovimientoCollection;
    }

    public void setRegMovimientoCollection(Collection<RegMovimiento> regMovimientoCollection) {
        this.regMovimientoCollection = regMovimientoCollection;
    }

    public Collection<RenCajero> getRenCajeroCollection() {
        return renCajeroCollection;
    }

    public void setRenCajeroCollection(Collection<RenCajero> renCajeroCollection) {
        this.renCajeroCollection = renCajeroCollection;
    }

    public boolean getDashboard() {
        return dashboard;
    }

    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Collection<AclRegistroUser> getAclRegistroUserCollection() {
        return aclRegistroUserCollection;
    }

    public void setAclRegistroUserCollection(Collection<AclRegistroUser> aclRegistroUserCollection) {
        this.aclRegistroUserCollection = aclRegistroUserCollection;
    }

    public String getUserDocumental() {
        return userDocumental;
    }

    public void setUserDocumental(String userDocumental) {
        this.userDocumental = userDocumental;
    }

    public String getPassDocumental() {
        return passDocumental;
    }

    public void setPassDocumental(String passDocumental) {
        this.passDocumental = passDocumental;
    }

    public Date getFechaActPass() {
        return fechaActPass;
    }

    public void setFechaActPass(Date fechaActPass) {
        this.fechaActPass = fechaActPass;
    }

    public boolean isCaducadaPass() {
        return caducadaPass;
    }

    public void setCaducadaPass(boolean caducadaPass) {
        this.caducadaPass = caducadaPass;
    }

    public FirmaElectronica getFirma() {
        return firma;
    }

    public void setFirma(FirmaElectronica firma) {
        this.firma = firma;
    }

    public String getFirmaNombre() {
        return firmaNombre;
    }

    public void setFirmaNombre(String firmaNombre) {
        this.firmaNombre = firmaNombre;
    }

    public String getFirmaCargo() {
        return firmaCargo;
    }

    public void setFirmaCargo(String firmaCargo) {
        this.firmaCargo = firmaCargo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AclUser other = (AclUser) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AclUser{");
        sb.append("id=").append(id);
        sb.append(", usuario=").append(usuario);
        sb.append(", pass=").append(pass);
        sb.append(", sisEnabled=").append(sisEnabled);
        sb.append(", userIsDirector=").append(userIsDirector);
        sb.append(", rutaImagen=").append(rutaImagen);
        sb.append(", imagenPerfil=").append(imagenPerfil);
        sb.append(", esSuperUser=").append(esSuperUser);
        sb.append(", code=").append(code);
        sb.append(", dashboard=").append(dashboard);
        sb.append(", clave=").append(clave);
        sb.append(", userDocumental=").append(userDocumental);
        sb.append(", passDocumental=").append(passDocumental);
        sb.append(", fechaActPass=").append(fechaActPass);
        sb.append(", caducadaPass=").append(caducadaPass);
        sb.append('}');
        return sb.toString();
    }

}
