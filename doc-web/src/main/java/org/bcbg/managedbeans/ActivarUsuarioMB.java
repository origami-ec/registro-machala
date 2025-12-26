/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.math.BigInteger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.User;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author jesus
 */
@ViewScoped
@Named
public class ActivarUsuarioMB implements Serializable {

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;
    @Inject
    private UserSession sess;

    private Integer code;  //Id de PubUsuario
    private User user;
    private String pass;
    private String passConfirm;
    private String mensaje = "";

    public void doPreRenderView() {
        if (!Utils.isAjaxRequest()) {
            initView();
        }
    }

    public void initView() {
        if (code != null) {
            user = (User) service.methodGETwithouAuth(SisVars.ws + "usuario/find" + Utils.armarUrlCamposObj(new User(code.longValue()), User.class, null), User.class);
            if (user != null && user.getId() != null) {
            } else {
                user = new User();
                mensaje = "Error al Validar los Parametros ingresados!!!";
            }
        }
    }

    public void save() {
        if (validar()) {
            user.setPassword(pass);
            user.setEliminado(Boolean.FALSE);
            user = (User) service.methodPUT(user, SisVars.ws + "update/password/user", User.class);
            AclLogin aclLogin = new AclLogin();
            aclLogin.setEvento("Cambio de clave");
            aclLogin.setIpUserSession(sess.getDatosEquipo());
            aclLogin.setUserSessionId(BigInteger.valueOf(user.getId()));
            aclLogin.setUserSessionName(sess.getName_user());
            if (user != null && user.getId() != null) {
                JsfUti.executeJS("PF('dlgConfirm').show()");
            } else {
                JsfUti.messageInfo(null, "Ocurrió un error al activar el usuario, intenten nuevamente", "");
            }
        }
    }

    public Boolean validar() {
        if (Utils.isEmpty(pass).isEmpty()) {
            JsfUti.messageError(null, "Debe Ingresar la contraseña", "");
            return false;
        }
        if (Utils.isEmpty(passConfirm).isEmpty()) {
            JsfUti.messageError(null, "Debe confirmar la contraseña", "");
            return false;
        }
        if (pass.length() < 8) {
            JsfUti.messageError(null, "Su contraseña debe ser mayor o igual a 8 caracteres", "");
            return false;
        }
        if (!pass.equals(passConfirm)) {
            JsfUti.messageError(null, "Las contraseñas no coinciden por favor ingrese una nueva.", "");
            return false;
        }
        return true;
    }

    public void confirmDialog() {
        JsfUti.redirectFaces("/login");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
//</editor-fold>
}
