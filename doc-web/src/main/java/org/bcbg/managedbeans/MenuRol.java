/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenuRol;
import org.bcbg.entities.PubGuiMenuTipoAcceso;
import org.bcbg.entities.PubGuiMenubar;
import org.bcbg.entities.Rol;
import org.bcbg.models.Document;
import org.bcbg.services.interfaces.MenuServiceLocal;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class MenuRol implements Serializable {

    private static final Logger LOG = Logger.getLogger(MenuRol.class.getName());

    @Inject
    private AppServices appServices;
    @Inject
    private BcbgService service;
    @Inject
    private MenuServiceLocal menuServ;

    private List<PubGuiMenu> padre;
    private List<PubGuiMenuRol> menuRoles;
    private PubGuiMenu menu;
    private PubGuiMenuRol menuRol;
    private Rol rol;
    private List<Rol> rols;
    private String accionesMenu;

    @PostConstruct
    public void initView() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            System.out.println("initView");
            loadModel();
        }
    }

    private void loadModel() {
        padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
        menu = new PubGuiMenu();
        menuRoles = new ArrayList<>();
        rol = new Rol();
        accionesMenu = "";
    }

    public void showDlgNuevoMenu(PubGuiMenu me) {
        rol = new Rol();
        rols = new ArrayList<>();
        menu = new PubGuiMenu();
        if (me != null) {
            menu.setMenuPadre(me);
            menu.setIdMenuPadre(me.getId().longValue());
        } else {
            menu.setMenubar(new PubGuiMenubar(1));
        }
        JsfUti.update("formNewMenu");
        JsfUti.executeJS("PF('dlgNewMenu').show()");
    }

    public void showDlgEditMenu(PubGuiMenu menu) {
        this.menu = menu;
        JsfUti.update("formEditMenu");
        JsfUti.executeJS("PF('dlgEditMenu').show()");
    }

    public void actualizarMenu() {
        try {
            System.out.println("actualizarMenu");
            menu.setMenubar(new PubGuiMenubar(1));
            menu = menuServ.guardar(menu);
            if (menu != null) {
                loadModel();
                JsfUti.executeJS("PF('dlgEditMenu').hide()");
                JsfUti.update("frmMenuRol");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditPermisos(PubGuiMenu menu) {
        rol = new Rol();
        this.menu = menu;
        this.menuRoles = menuServ.getAccesosMenuRol(menu);
        if (Utils.isEmpty(menuRoles)) {
            menuRoles = new ArrayList<>();
        }
        JsfUti.update("frmPermisos");
        JsfUti.executeJS("PF('dlgPermisos').show()");
    }

    public void eliminar(PubGuiMenu menu) {
        service.methodPUT(menu, SisVars.ws + "menus/eliminar", PubGuiMenuRol.class);
        padre = menuServ.getMenusOrdenados(new PubGuiMenubar(1));
        JsfUti.update("frmMenuRol");
    }

    public void updatePermisos() {
        try {
            if (!menuRoles.isEmpty()) {
                for (PubGuiMenuRol mr : menuRoles) {
                    service.methodPUT(mr, SisVars.ws + "menusRol/actualizar", PubGuiMenuRol.class);
                }
                loadModel();
                JsfUti.executeJS("PF('dlgPermisos').hide()");
                JsfUti.update("frmMenuRol");
            } else {
                JsfUti.messageWarning(null, "El menú no debe quedar sin roles.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getNombreRol(Long idRol) {
        if (idRol == null) {
            return "";
        }
        try {
            Rol rolDB = appServices.getRol(idRol);
            return rolDB != null ? rolDB.getNombre() : "";
        } catch (Exception e) {
            return "";
        }

    }

    public void agregarPermiso() {
        if (rol != null && rol.getId() != null) {
            menuRol = new PubGuiMenuRol();
            menuRol.setMenu(menu);
            menuRol.setRol(rol.getId());
            createObjectAcciones(rol.getAccionesMenu(), menuRol);
            service.methodPUT(menuRol, SisVars.ws + "menusRol/actualizar", PubGuiMenuRol.class);
            menuRoles = menuServ.getAccesosMenuRol(menu);
            rol = new Rol();
        }
    }

    public void eliminarPermiso(PubGuiMenuRol pubmenurol) {
        try {
            if (pubmenurol.getId() != null) {
                menuServ.borrarMenu(pubmenurol);
                menuRoles = menuServ.getAccesosMenuRol(menu);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void agregarRol() {
        if (rol != null) {
            rol.setAccionesMenu(accionesMenu);
            rols.add(rol);
            accionesMenu = "";
        }
    }

    public void eliminarRol(int indice) {
        rols.remove(indice);
    }

    public void saveNewMenu() {
        System.out.println("saveNewMenu");
        PubGuiMenuRol mr;
        try {
            menu = (PubGuiMenu) menuServ.guardar(menu);
            if (menu != null) {
                System.out.println("//DIFERENTE DE NULL");
                if (Utils.isNotEmpty(rols)) {
                    for (Rol r : rols) {
                        System.out.println("//ROLES DE NULL " + r.getId());
                        mr = new PubGuiMenuRol();
                        mr.setMenu(menu);
                        mr.setRol(r.getId());
                        createObjectAcciones(r.getAccionesMenu(), mr);
                        PubGuiMenuRol menurol = (PubGuiMenuRol) service.methodPOST(mr, SisVars.ws + "menusRol/guardar", PubGuiMenuRol.class);
                        if (menurol.getId() != null) {
                            System.out.println("//GUARDANDO DE NULL ID " + menurol.getId());
                        }
                    }
                }
                loadModel();
                JsfUti.executeJS("PF('dlgNewMenu').hide()");
                JsfUti.update("frmMenuRol");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void createObjectAcciones(String acciones, PubGuiMenuRol mr) {
        mr.setDocuments(new ArrayList<>());
        Document dview = new Document();
        Document dnew = new Document();
        Document ddelete = new Document();
        switch (acciones) {
            case "view":
                dview = new Document(acciones, "lectura", Boolean.TRUE);
                dnew = new Document("new,edit", "lectura_escritura", Boolean.FALSE);
                ddelete = new Document("delete", "lectura_escritura_eliminar", Boolean.FALSE);
                break;
            case "new,edit":
                dview = new Document("view", "lectura", Boolean.TRUE);
                dnew = new Document(acciones, "lectura_escritura", Boolean.TRUE);
                ddelete = new Document("delete", "lectura_escritura_eliminar", Boolean.FALSE);
                break;
            case "delete":
                dview = new Document("view", "lectura", Boolean.TRUE);
                dnew = new Document("new,edit", "lectura_escritura", Boolean.TRUE);
                ddelete = new Document(acciones, "lectura_escritura_eliminar", Boolean.TRUE);
                break;
        }
        mr.getDocuments().add(dview);
        mr.getDocuments().add(dnew);
        mr.getDocuments().add(ddelete);
    }

    public void nuevoMenu(Boolean esPadre, PubGuiMenu menu) {
        rols = new ArrayList<>();
        this.menu = new PubGuiMenu();
        this.menu.setMenuPadre(menu);
        JsfUti.executeJS("PF('dlgNewMenu').show()");
        JsfUti.update("formNewMenu");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<Rol> getListRols() {
        return appServices.getListRolsActivos();
    }

    public List<PubGuiMenuTipoAcceso> getAccesos() {
        return menuServ.getMenuAccesoList();
    }

    public List<PubGuiMenubar> getMenuBars() {
        return menuServ.getMenuBarList(null);
    }

    public List<PubGuiMenu> getHijos(PubGuiMenu padre) {
        return menuServ.getMenusOrdenados(padre);
    }

    public List<PubGuiMenu> getPadre() {
        return padre;
    }

    public void setPadre(List<PubGuiMenu> padre) {
        this.padre = padre;
    }

    public PubGuiMenu getMenu() {
        return menu;
    }

    public void setMenu(PubGuiMenu menu) {
        this.menu = menu;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public PubGuiMenuRol getMenuRol() {
        return menuRol;
    }

    public void setMenuRol(PubGuiMenuRol menuRol) {
        this.menuRol = menuRol;
    }

    public List<PubGuiMenuRol> getMenuRoles() {
        return menuRoles;
    }

    public void setMenuRoles(List<PubGuiMenuRol> menuRoles) {
        this.menuRoles = menuRoles;
    }

    public List<Rol> getRols() {
        return rols;
    }

    public void setRols(List<Rol> rols) {
        this.rols = rols;
    }

    public String getAccionesMenu() {
        return accionesMenu;
    }

    public void setAccionesMenu(String accionesMenu) {
        this.accionesMenu = accionesMenu;
    }
//</editor-fold>

}
