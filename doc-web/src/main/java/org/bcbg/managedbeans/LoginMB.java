/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.text.WordUtils;
import org.bcbg.config.SisVars;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.Motivaciones;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.User;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Ricardo Jesus
 */
@Named
@ViewScoped
public class LoginMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;

    private String user;
    private String pass;
    private List<Long> roles, depts;
    private AclLogin aclLogin;

    @Inject
    private UserSession sess;
    private String identificacion;
    private User usuario;
    private String passConfirm;
    private String codeUser;
    private String codePass;

    public void doPreRenderView() {
        if (!Utils.isAjaxRequest()) {
            init();
        }
    }

    public void init() {
        usuario = new User();
        identificacion = "";
        if (codeUser != null && codePass != null) {
            User userC = new User();
            userC.setUsuarioNombre(codeUser);
            userC.setClave(codePass);
            User userDB = (User) service.methodGETwithouAuth(SisVars.ws + "usuario/find"
                    + Utils.armarUrlCamposObj(userC, User.class, null), User.class);
            if (userDB != null && userDB.getId() != null) {
                sess.setUserId(userDB.getId());
            } else {
                sess.setUrlSolicitada(SisVars.urlbase + "login");
                sess.redirectUrlSolicitada();
            }
        }
    }

    public void doLogin() {
        //String ANSI_RED = "\u001B[31m";
        try {
            //if (user != null && pass != null) {
            if (!user.isEmpty() && !pass.isEmpty()) {
                pass = Utils.encriptaEnMD5(pass);
                String token = service.autenticate(user, pass);
                sess.setToken(token);
                usuario = new User(user, pass);
                User u = (User) service.methodPOST(usuario, SisVars.ws + "iniciarSesion", User.class);
                if (u != null) {
                    aclLogin = new AclLogin();
                    sess.setUserId(u.getId());
                    sess.setName_user(user);
                    sess.setTemp(true);

                    sess.setEsDirector(Boolean.FALSE);
                    if (u.getRecursoHumano().getPersona() != null) {
                        sess.setSexoUser(u.getRecursoHumano().getPersona().getGenero().toLowerCase().equals("m") ? Boolean.TRUE : Boolean.FALSE);
                        if (u.getRecursoHumano().getPersona().getDetalleNombre() != null) {
                            sess.setNombrePersonaLogeada(u.getRecursoHumano().getPersona().getDetalleNombre());
                        } else {
                            sess.setNombrePersonaLogeada("");
                        }
                        if (u.getRecursoHumano().getPersona().getNombres() != null) {
                            sess.setNombres(WordUtils.capitalize(u.getRecursoHumano().getPersona().getNombres().toLowerCase()));
                        } else {
                            sess.setNombres("");
                        }
                        sess.setIdDepartamento(u.getDepartamentoId());
                    } else {
                        sess.setSexoUser(Boolean.TRUE);
                        sess.setNombrePersonaLogeada("");
                        sess.setNombres("");
                    }
                    roles = new ArrayList<>();
                    depts = new ArrayList<>();
                    List<RolUsuario> rolU = appServices.getRolesUsuarios(new RolUsuario(null, u.getId(), null));
                    if (rolU != null && !rolU.isEmpty()) {
                        for (RolUsuario ru : rolU) {
                            if (ru.getRol().getNombre().equals(Variables.rolAdmin)) {
                                sess.setIsUserAdmin(Boolean.TRUE);
                            }
                            roles.add(ru.getRol().getId());
                        }
                    }
                    depts.add(u.getDepartamentoId());
                    sess.setRoles(roles);
                    sess.setDepts(depts);
                    sess.setEsSuperUser(Boolean.TRUE);
                    aclLogin.setIpUserSession(sess.getDatosEquipo());
                    aclLogin.setMacClient(sess.getMACAddressEquipo());
                    aclLogin.setOsClient(sess.getOsEquipo());
                    aclLogin.setUserSessionId(BigInteger.valueOf(u.getId()));
                    aclLogin.setUserSessionName(sess.getName_user());
                    aclLogin.setFechaDoLogin(Utils.getDate(new Date()));
                    aclLogin.setEvento("Iniciar sesion");
                    aclLogin.setNombres(sess.getNombrePersonaLogeada());
                    aclLogin.setApp(SisVars.app);
                    //aclLogin = (AclLogin) service.methodPOST(aclLogin, SisVars.wsLogs + "logsInicioSesion/guardar", AclLogin.class);
                    /*if (aclLogin != null) {
                        sess.setAclLogin(aclLogin);
                        sess.setIdAclLogin(aclLogin.getId());
                    }*/
                    /*
                        Obtiene la firma electronica del usuario
                     */
                    FirmaElectronica firmaElectronica = (FirmaElectronica) service.methodGET(SisVars.wsDocs + "firmaElectronica/find?usuario.referenceId=" + u.getId().toString(), FirmaElectronica.class);
                    sess.setFirmaElectronica(firmaElectronica);

                    Motivaciones motivaciones = (Motivaciones) appServices.consultarFraseMotivadora();
                    if (motivaciones != null) {
                        sess.setFrase(motivaciones.getDescripcion());
                    }

                    sess.setPerfil("/resources/image/avatar.jpg");
                    sess.setUrlSolicitada(SisVars.urlbase + "procesos/bienvenida");
                    sess.redirectUrlSolicitada();
                    JsfUti.setSessionBean("userSession", sess);
                } else {
                    JsfUti.messageError(null, Messages.credencialesInvalidas, "");
                }
            } else {
                JsfUti.messageError(null, Messages.credencialesInvalidas, "");
            }
        } catch (Exception e) {
            Logger.getLogger(LoginMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void olvidoContrasenia() {
        System.out.println("//forgot");
        usuario = new User();
//        forgotPass = Boolean.TRUE;
    }

    public void enviarRecuperarContrasenia() {
        service.methodPOSTwithouAuth(usuario, SisVars.ws + "recuperar/usuario", User.class);
        JsfUti.messageInfo(null, "", "Notificación enviada, por favor revisar en su bandeja de correo electrónico");
        JsfUti.redirectFaces("/login");
    }

    public void findUsuario() {
        if (!identificacion.isEmpty()) {
//            usuario = (User) service.methodGETwithouAuth(SisVars.ws + "usuario/find"
//                    + Utils.armarUrlCamposObj(new User(Boolean.TRUE, new Persona(identificacion)), User.class, null),
//                    User.class);
            if (usuario == null) {
                JsfUti.messageError(null, "", "Error, no existen datos registrados.");
            }
        }
    }

    public void cancelarRecuperarContrasenia() {
        System.out.println("//CancelForgot");
//        forgotPass = Boolean.FALSE;
    }

    public void actualizarPass() {
        try {
            if (validar()) {
                User userDB = (User) service.methodGETwithouAuth(SisVars.ws + "usuario/find" + Utils.armarUrlCamposObj(new User(sess.getUserId()), User.class, null), User.class);
                userDB.setClave(pass);
                service.methodPUTwithouAuth(userDB, SisVars.ws + "update/password/user", User.class);
                sess.setActKey(null);
                sess.setUserId(null);
                AclLogin acl = new AclLogin();
                acl.setEvento("Cambio de clave");
                acl.setIpUserSession(sess.getDatosEquipo());
                acl.setUserSessionId(BigInteger.valueOf(userDB.getId()));
                acl.setUserSessionName(userDB.getNombreUsuario());
                acl.setFechaDoLogin(Utils.getDate(new Date()));
                acl.setMacClient(sess.getMACAddressEquipo());
                service.methodPOSTwithouAuth(acl, SisVars.ws + "create/aclLogin", AclLogin.class);
//                updatePassword = Boolean.FALSE;
                JsfUti.messageInfo(null, "Contraseña actualizada", "");
                sess.setUrlSolicitada(SisVars.urlbase + "login");
                sess.redirectUrlSolicitada();
            }
        } catch (Exception e) {
            System.out.println("Exception update password " + e.getMessage());
        }
    }

    public Boolean validar() {
        if (sess.getUserId() == null) {
            JsfUti.redirect(SisVars.urlbase);
            return false;
        }
        if (Utils.isEmpty(pass).isEmpty()) {
            JsfUti.messageError(null, "Debe Ingresar la contraseña", "");
            return false;
        }
        if (Utils.isEmpty(passConfirm).isEmpty()) {
            JsfUti.messageError(null, "Debe confirmar la contraseña", "");
            return false;
        }
        if (!pass.equals(passConfirm)) {
            JsfUti.messageError(null, "La contraseña es igual a la anterior por favor ingrese una nueva.", "");
            return false;
        }
        if (pass.length() <= 7) {
            System.out.println("Test. 4");
            JsfUti.messageError(null, "Su contraseña debe ser mayor o igual a 8 caracteres", "");
            return false;
        }
        return true;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public String getCodePass() {
        return codePass;
    }

    public void setCodePass(String codePass) {
        this.codePass = codePass;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public UserSession getSess() {
        return sess;
    }

    public void setSess(UserSession sess) {
        this.sess = sess;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
//</editor-fold>
}
