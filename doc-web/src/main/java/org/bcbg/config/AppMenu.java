/*
 *  Origami Solutions
 */
package org.bcbg.config;

import org.bcbg.session.UserSession;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenubar;
import org.bcbg.services.ejbs.MenuCache;
import org.bcbg.services.interfaces.MenuServiceLocal;
import org.bcbg.util.JsfUti;
import org.bcbg.ws.AppServices;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.AsyncServices;
import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author fernando
 */
@Named
@RequestScoped
public class AppMenu {

    @Inject
    protected MenuServiceLocal menuService;
    @Inject
    private MenuCache menuCache;
    @Inject
    protected UserSession userSession;
    @Inject
    private AsyncServices asyncServices;

    private MenuModel model;

    @PostConstruct
    public void init() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            if (model == null) {
                model = new DefaultMenuModel();
            }
            List<PubGuiMenu> menus = menuCache.getDataCache(userSession.getName_user());
            if (menus == null) {
                menuCache.init(userSession.getName_user());
                menus = menuCache.getDataCache(userSession.getName_user());
            }
            llenarMenu(menus);
        }

    }

    public void llenarMenu(List<PubGuiMenu> menus) {
        int count = 0;
        model = new DefaultMenuModel();
        //System.out.println("Menus encontrados: " + menus);
        if (menus != null) {
            for (PubGuiMenu menu : menus) {
                DefaultSubMenu subMenu = DefaultSubMenu.builder().label(menu.getNombre()).icon(menu.getIcono()).build();
                //DefaultSubMenu subMenu = new DefaultSubMenu(menu.getNombre());
                subMenu.setId("menu_" + count);
                subMenu.setStyleClass("layout-menubar-container");
                if (tieneSubmenus(menu)) {
                    for (PubGuiMenu itemMenu : menu.getMenusHijos_byNumPosicion()) {
                        String url = getUrl(itemMenu.getPrettyPattern());

                        DefaultMenuItem item
                                = DefaultMenuItem.builder()
                                        .value(itemMenu.getNombre())
                                        .icon(itemMenu.getIcono())
                                        .url(url)
                                        .build();
                        item.setIncludeViewParams(false);
                        item.setOnclick("customfunction([{name:'name_', value:'" + itemMenu.getNombre() + "'}, {name:'url_', value:'" + menuUrl(itemMenu) + "'}]); customMenu(event);");
                        subMenu.getElements().add(item);
                    }
                    model.getElements().add(subMenu);
                }
                count++;
            }
        }
    }

    public void onClic(ActionEvent x) {

        try {
            String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name_");
            String url = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("url_");
            userSession.setTopPage(value);
            userSession.setTitlePage(value);
            asyncServices.guardarLog(value, SisVars.eventMenu, x.toString(), url, userSession.getName_user(), userSession.getNombrePersonaLogeada(), userSession.getIpClient(), userSession.getMacClient(), userSession.getOsClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClicBtn(ActionEvent x) {
        try {
            String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name_");
            String pagina = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pagina_");
            userSession.setTopPage(value);
            userSession.setTitlePage(value);
            asyncServices.guardarLog(value, SisVars.eventClick, x.toString(), pagina, userSession.getName_user(), userSession.getNombrePersonaLogeada(), userSession.getIpClient(), userSession.getMacClient(), userSession.getOsClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PubGuiMenu> getMenuWorkflow() {
        PubGuiMenubar menubar = menuService.genMenuStruct("workflow.main", userSession);
        return menubar.getMenuListSoyMenubar_byOrden();
    }

    public Boolean tieneSubmenus(PubGuiMenu menu) {
        if (menu.getMenusHijos_byNumPosicion() != null
                && menu.getMenusHijos_byNumPosicion().size() > 0) {
            return true;
        }
        return false;
    }

    public void clearMenuWorkflow() {
        menuCache.clearCache();
    }

    public String menuUrl(PubGuiMenu menu) {

        if (menu.getPrettyPattern() != null && !menu.getPrettyPattern().equals("") && !menu.getPrettyPattern().equals("#")) {
            if (menu.getPrettyPattern().startsWith("http://")) {
                return menu.getPrettyPattern();
            }
            if (menu.getPrettyPattern().startsWith("/")) {
                return JsfUti.getHostContextUrl() + menu.getPrettyPattern().substring(1);
            } else {
                return JsfUti.getHostContextUrl() + menu.getPrettyPattern();
            }

        }
        return "#";
    }

    private String getUrl(String url) {
        if (url == null) {
            return "#";
        }
        if (url.startsWith("http")) {
            return url;
        } else {
            return SisVars.urlbase + (url.startsWith("/") ? url.substring(1).trim() : url.trim());
        }
    }

    /**
     * Creates a new instance of AppMenu
     */
    public AppMenu() {
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
