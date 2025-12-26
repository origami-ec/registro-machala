/*
 *  Origami Solutions
 */
package org.bcbg.services.ejbs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.bcbg.config.RolesEspeciales;
import org.bcbg.config.SisVars;
import org.bcbg.config.TipoAccesoIdent;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenuRol;
import org.bcbg.entities.PubGuiMenuTipoAcceso;
import org.bcbg.entities.PubGuiMenubar;
import org.bcbg.services.interfaces.MenuServiceLocal;
import org.bcbg.session.WfUserSession;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author fernando
 */
@Stateless
public class MenuService implements MenuServiceLocal {

    @Inject
    private MenuCache menuCache;
    @Inject
    private BcbgService service;

    /**
     * genera estructura de menu correspondiente al nivel de acceso del usuario
     * web
     *
     * @param menubarIdent Object
     * @param userSession Object
     * @return Object
     */
    @Override
    public PubGuiMenubar genMenuStruct(String menubarIdent, WfUserSession userSession) {
        PubGuiMenubar menubar1;
        try {
            menubar1 = menuCache.getMenuBar(menubarIdent);
            genMenuStruct_process(menubar1.getMenuListSoyMenubar_byOrden(), userSession);
        } catch (Exception e) {
            Logger.getLogger(MenuService.class.getName()).log(Level.SEVERE, null, e);
            menubar1 = null;
        }
        return menubar1;
    }

    protected void genMenuStruct_process(Collection<PubGuiMenu> menuList, WfUserSession userSession) {
        LinkedList<PubGuiMenu> listaRemover = new LinkedList<>();
        for (PubGuiMenu cadaMenu : menuList) {
            // si no tiene acceso al menu, cortarlo:
            if (!genMenuStruct_checkAccess(cadaMenu, userSession)) {
                listaRemover.add(cadaMenu);
            } else {
                // recursive
                if (cadaMenu.getMenusHijos_byNumPosicion() != null) {
                    genMenuStruct_process(cadaMenu.getMenusHijos_byNumPosicion(), userSession);
                }
            }
        }
        menuList.removeAll(listaRemover);
    }

    protected Boolean genMenuStruct_checkAccess(PubGuiMenu menu, WfUserSession userSession) {
        for (Long r : userSession.getRoles()) {
            if (r.equals(RolesEspeciales.ADMINISTRADOR)) {
                return true;
            }
        }
        // chekear por tipo de acceso:
        if (menu.getTipoAcceso() == null) {
            return false;
        }

        if (menu.getTipoAcceso().getIdentificador().equals(TipoAccesoIdent.PUBLICO)) {
            return true;
        }
        if (menu.getTipoAcceso().getIdentificador().equals(TipoAccesoIdent.USUARIO)
                && userSession.getUserId() != null) {
            return true;
        }

        if (menu.getTipoAcceso().getIdentificador().equals(TipoAccesoIdent.DIRECTOR)
                && userSession.getEsDirector()) {
            return true;
        }

        // chekear por roles
        if (menu.getPubGuiMenuRolCollection() == null) {
            return false;
        }
        for (PubGuiMenuRol cadaRol : menu.getPubGuiMenuRolCollection()) {
            if (genMenuStruct_compare(cadaRol, userSession)) {
                return true;
            }
        }
        return false;
    }

    protected Boolean genMenuStruct_compare(PubGuiMenuRol rol, WfUserSession userSession) {
        for (Long r : userSession.getRoles()) {
            if ((rol.getRol().equals(r))
                    && (rol.getEsDirector() != null ? (rol.getEsDirector().equals(userSession.getEsDirector())) : true)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PubGuiMenubar getMenuBarTree(String menubarIdent) {

        PubGuiMenubar menubar1 = this.getMenuBar(menubarIdent);
        if (menubar1 == null) {
            return null; // si no existe el menubar,, null
        }
        menubar1.setMenuListSoyMenubar_byOrden(this.getMenusOrdenados(menubar1));

        this.getMenuBarTree_initMenuList(menubar1.getMenuListSoyMenubar_byOrden());

        return menubar1;
    }

    private void getMenuBarTree_initMenuList(List<PubGuiMenu> menus) {
        for (PubGuiMenu cadaMenu : menus) {
            this.getMenuBarTree_initmenu(cadaMenu);
        }
    }

    private void getMenuBarTree_initmenu(PubGuiMenu menu) {
        menu.setMenusHijos_byNumPosicion(this.getMenusOrdenados(menu));
        //menu.getRolesPermitidos().toArray();
        menu.getPubGuiMenuRolCollection().toArray();
        // generacion recursiva:
        this.getMenuBarTree_initMenuList(menu.getMenusHijos_byNumPosicion());
    }

    @Override
    public PubGuiMenu getMenu(Integer idMenu) {
        if (idMenu == null) {
            return null;
        }
        return (PubGuiMenu) service.methodGET(SisVars.ws + "menu/find?id=" + idMenu, PubGuiMenu.class);
    }

    @Override
    public PubGuiMenubar getMenuBar(String menubarIdent) {
        return (PubGuiMenubar) service.methodGET(SisVars.ws + "menuBar/find?identificador=" + menubarIdent, PubGuiMenubar.class);
    }

    @Override
    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenu menuPadre) {
        return service.methodListGET(SisVars.ws + "menus/find?idMenuPadre=" + menuPadre.getId() + "&page=0&size=100&sort=numPosicion,ASC", PubGuiMenu[].class);
    }

    @Override
    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenubar menuBar) {
        return service.methodListGET(SisVars.ws + "menus/find?menuBar.id=" + menuBar.getId() + "&idMenuPadre=-1&page=0&size=100&sort=numPosicion,ASC", PubGuiMenu[].class);
    }

    //***************** Metodos para mantenimiento de menu *******************//
    @Override
    public PubGuiMenu guardar(PubGuiMenu menu) {
        try {
            System.out.println("actualizarMenu guardar: " +menu.toString() );
            if (menu.getId() == null) {
                menu = (PubGuiMenu) service.methodPOST(menu, SisVars.ws + "menus/guardar", PubGuiMenu.class);
            } else {
                menu = (PubGuiMenu) service.methodPUT(menu, SisVars.ws + "menus/actualizar", PubGuiMenu.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return menu;
    }

    @Override
    public List<PubGuiMenubar> getMenuBarList(String user) {
        return service.methodListGET(SisVars.ws + "menusBar/find/" + user, PubGuiMenubar[].class);
    }

    @Override
    public List<PubGuiMenuTipoAcceso> getMenuAccesoList() {
        return service.methodListGET(SisVars.ws + "menusTipoAcceso/find", PubGuiMenuTipoAcceso[].class);
    }

    @Override
    public List<PubGuiMenuRol> getAccesosMenuRol(PubGuiMenu menu) {
        System.out.println("// URL ACCESOS MENU " + SisVars.ws + "menusRol/find?menu.id=" + menu.getId());
        return service.methodListGET(SisVars.ws + "menusRol/find?menu.id=" + menu.getId(), PubGuiMenuRol[].class);
    }

    @Override
    public PubGuiMenu borrarMenu(PubGuiMenu menu) {
        return (PubGuiMenu) service.methodPUT(menu, SisVars.ws + "menus/eliminar", PubGuiMenu.class);
    }

    @Override
    public PubGuiMenuRol borrarMenu(PubGuiMenuRol menuRol) {
        return (PubGuiMenuRol) service.methodPUT(menuRol, SisVars.ws + "menusRol/delete", PubGuiMenuRol.class);
    }

}
