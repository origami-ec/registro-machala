/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.session;

import org.bcbg.config.SisVars;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.services.ejbs.MenuCache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jodd.util.StringUtil;
import org.bcbg.documental.models.UsuarioDocs;
import org.bcbg.util.Utils;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Origami
 */
@Named
@SessionScoped
public class UserSession implements WfUserSession {

    private static final Logger LOG = Logger.getLogger(UserSession.class.getName());

    @Inject
    private BcbgService service;
    @Inject
    private MenuCache menuCache;

    protected String name_user;
    protected String token;
    protected String urlImagen;
    protected Boolean sexoUser;
    protected String actKey;
    protected String taskID;
    protected Long userId = (long) -1;
    protected String name = "invitado";
    protected String urlSolicitada;
    protected Boolean temp = false;
    protected String departamento;
    protected String nombrePersonaLogeada;
    protected Boolean esDirector;
    protected String varTemp;
    protected String ipClient;
    protected String macClient;
    protected String osClient;
    protected String topPage;
    private String titlePage = "OrigamiGT";
    protected String perfil = "/resources/image/avatar.jpg";
    protected Boolean esSuperUser;
    protected List<Long> roles = new ArrayList<>();
    protected List<Long> depts = new ArrayList<>();
    private AclLogin aclLogin = new AclLogin();
    private Long idAclLogin;
    private Pattern macpt = null;
    private Long numTramite;
    private FirmaElectronica firmaElectronica;
    private List<PubGuiMenu> menus = new ArrayList<>();
    private String taskDefKey;
    private Boolean isUserAdmin = Boolean.FALSE;
    private Boolean isUserResponsable = Boolean.FALSE;
    private Long idSolicitud;
    private Long idDepartamento;
    private Long tipoLugar;
    private UsuarioDocs usuarioDocs;
    private String codigoTramite;

    private String nombres;
    private String frase;

    public String getNombreBienvenida() {
        if (this.esLogueado()) {
            return this.getNombrePersonaLogeada();
        }
        return "invitado";
    }

    public Boolean esLogueado() {
        return userId != null && userId > 0L;
    }

    public void redirectUrlSolicitada() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (this.getUrlSolicitada() != null || (this.getUrlSolicitada() != null && this.getUrlSolicitada().equals(ec.getRequestContextPath() + "/login"))) {
            try {
                HttpSession session = (HttpSession) ec.getSession(false);
                session.setAttribute("usuario", this.getName_user());
                ec.redirect(this.getUrlSolicitada());
                this.setUrlSolicitada(null);
                this.getDatosEquipo();
            } catch (IOException ex) {
                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ec.redirect(ec.getRequestContextPath() + "/");
                this.getDatosEquipo();
            } catch (IOException ex) {
                Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void redirectInvitado() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String fullURI = servletRequest.getRequestURI();
        if (fullURI.equalsIgnoreCase(servletRequest.getContextPath() + "/faces/admin/users/recuperarClave.xhtml")
                || fullURI.equalsIgnoreCase(servletRequest.getContextPath() + "/faces/admin/users/cambioClave.xhtml")) {
            return;
        }
        if (temp) {
            //System.out.println(" done!!! ");
            return;
        }

        this.persistReqUrl();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persistReqUrl() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String fullURI = servletRequest.getRequestURI();
        if (StringUtil.isNotEmpty(servletRequest.getQueryString())) {
            fullURI = fullURI + "?" + servletRequest.getQueryString();
        }
        this.getDatosEquipo();
        this.setUrlSolicitada(fullURI);
    }

    public void logout2() {
        if (aclLogin != null && aclLogin.getId() != null) {
            aclLogin.setFechaDoLogout(Utils.getDate(new Date()));
            //service.methodPOST(aclLogin, SisVars.wsLogs + "logsInicioSesion/guardar", AclLogin.class);
        }
        menuCache.clearCache(this.getName_user());
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        this.setTemp(false);
        this.setUrlSolicitada(null);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void perfil() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/admin/manage/datosUsuario.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void correos() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/procesos/notificaciones/notificacionesCorreos.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void bandejaTarea() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/procesos/dashBoard.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ayuda() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/admin/manage/ayuda.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDatosEquipo() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletRequest sr = (HttpServletRequest) ctx.getExternalContext().getRequest();
            sr.getHeader("X-FORWARDED-FOR");
            this.setIpClient(sr.getRemoteAddr());
            return sr.getRemoteAddr();
        } catch (Exception e) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, e.getMessage());
            return null;
        }
    }

    public String getOsEquipo() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletRequest sr = (HttpServletRequest) ctx.getExternalContext().getRequest();

            sr.getHeader("User-Agent");
            this.setOsClient(sr.getHeader("User-Agent"));
            return sr.getHeader("User-Agent");
        } catch (Exception e) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, e.getMessage());
            return null;
        }
    }

    /**
     *
     * @return
     */
    public String getMACAddressEquipo() {
        // Find OS and set command according to OS
        String OS = System.getProperty("os.name").toLowerCase();
        String[] cmd;
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern.compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            String[] a = {"arp", "-a", this.getIpClient()};
            cmd = a;
        } else {
            // Mac OS X, Linux
            macpt = Pattern.compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
            String[] a = {"arp", this.getIpClient()};
            cmd = a;
        }
        try {
            System.out.println("arp -a " + ipClient);
            // Run command
            Process p = Runtime.getRuntime().exec("arp -a " + ipClient);
            p.waitFor();
            // read output with BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            // Loop trough lines
//            System.out.println("Line " + line);
            while (line != null) {
                Matcher m = macpt.matcher(line);
                // when Matcher finds a Line then return it as result
                if (m.find()) {
                    //System.out.println("MAC: " + m.group(0));
                    macClient = m.group(0);
                    return m.group(0);
                }
                line = reader.readLine();
            }
            if (line != null) {
                macClient = line.toUpperCase();
                return line.toUpperCase();
            }
            return "";
        } catch (IOException | InterruptedException e1) {
            LOG.log(Level.SEVERE, "", e1);
            macClient = "";
            return "";
        }
        // Return empty string if no MAC is found

    }

    public PubGuiMenu getMenuRol(String requestURI) {
        String ASCII = "\u001B[31m";
        if (this.getMenus() == null) {
            return null;
        }
        if (!this.getMenus().isEmpty() && requestURI != null) {
            for (PubGuiMenu pubGuiMenu : this.getMenus()) {
                if (pubGuiMenu.getIdMenuPadre() == null && pubGuiMenu.getMenusHijos_byNumPosicion() != null) {
                    for (PubGuiMenu menu : pubGuiMenu.getMenusHijos_byNumPosicion()) {
                        if (menu.getHrefUrl() != null && menu.getHrefUrl().contains(requestURI)) {
                            return menu;
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getNotificacionFirma() {
        if (firmaElectronica != null) {
            return firmaElectronica.getEstadofirmaCaducada();
        }
        return "";
    }

    public UsuarioDocs getUsuarioDocs() {
        return new UsuarioDocs(userId, nombrePersonaLogeada, name_user);
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Boolean getIsUserResponsable() {
        return isUserResponsable;
    }

    public void setIsUserResponsable(Boolean isUserResponsable) {
        this.isUserResponsable = isUserResponsable;
    }

    public Boolean getIsUserAdmin() {
        return isUserAdmin;
    }

    public void setIsUserAdmin(Boolean isUserAdmin) {
        this.isUserAdmin = isUserAdmin;
    }

    public List<PubGuiMenu> getMenus() {
        return menuCache.getDataCache(this.getName_user());
    }

    public void setMenus(List<PubGuiMenu> menus) {
        this.menus = menus;
    }

    @Override
    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getActKey() {
        return actKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrlSolicitada() {
        return urlSolicitada;
    }

    public void setUrlSolicitada(String urlSolicitada) {
        this.urlSolicitada = urlSolicitada;
    }

    @Override
    public Boolean getTemp() {
        return temp;
    }

    public void setTemp(Boolean temp) {
        this.temp = temp;
    }

    @Override
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String getNombrePersonaLogeada() {
        if (nombrePersonaLogeada == null || nombrePersonaLogeada.isEmpty()) {
            nombrePersonaLogeada = "Admin";
        }
        return nombrePersonaLogeada;
    }

    public void setNombrePersonaLogeada(String nombrePersonaLogeada) {
        this.nombrePersonaLogeada = nombrePersonaLogeada;
    }

    @Override
    public Boolean getEsDirector() {
        return esDirector;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
    }

    public String getVarTemp() {
        return varTemp;
    }

    public void setVarTemp(String varTemp) {
        this.varTemp = varTemp;
    }

    @Override
    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Boolean getEsSuperUser() {
        return esSuperUser;
    }

    public void setEsSuperUser(Boolean esSuperUser) {
        this.esSuperUser = esSuperUser;
    }

    @Override
    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    @Override
    public List<Long> getDepts() {
        return depts;
    }

    public void setDepts(List<Long> depts) {
        this.depts = depts;
    }

    public AclLogin getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(AclLogin aclLogin) {
        this.aclLogin = aclLogin;
    }

    public Long getIdAclLogin() {
        return idAclLogin;
    }

    public void setIdAclLogin(Long idAclLogin) {
        this.idAclLogin = idAclLogin;
    }

    public String getTopPage() {
        return topPage;
    }

    public void setTopPage(String topPage) {
        this.topPage = topPage;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Boolean getSexoUser() {
        return sexoUser;
    }

    public void setSexoUser(Boolean sexoUser) {
        this.sexoUser = sexoUser;
    }

    public String getMacClient() {
        return macClient;
    }

    public void setMacClient(String macClient) {
        this.macClient = macClient;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Long getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(Long tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    public String getOsClient() {
        return osClient;
    }

    public void setOsClient(String osClient) {
        this.osClient = osClient;
    }

    public String getCodigoTramite() {
        return codigoTramite;
    }

    public void setCodigoTramite(String codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

//</editor-fold>
}
