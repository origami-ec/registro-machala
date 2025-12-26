/*
 *  Origami Solutions
 */
package org.bcbg.services.ejbs;

import org.bcbg.session.UserSession;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenubar;
import org.bcbg.services.interfaces.MenuServiceLocal;
import org.bcbg.util.EntityBeanCopy;
import org.bcbg.util.Utils;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author fernando
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MenuCache {

    @Inject
    private MenuServiceLocal menuService;
    @Inject
    private UserSession session;
    private final String cacheName = "workflow.main";
    private ConcurrentMap<String, Object> cache;
    protected ConcurrentMap<String, Object> menubarsMap;
    protected ConcurrentMap<String, String> lockerMap = new ConcurrentHashMap<>();

    /**
     * Inicializa el singleton ejb
     *
     * @param init
     */
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init(null);
    }

    public void init(String key) {
        if (cache == null) {
            cache = new ConcurrentHashMap<>();
        }
        if (key != null) {
            List<PubGuiMenubar> modulos = menuService.getMenuBarList(key);
            if (Utils.isNotEmpty(modulos)) {
//                PubGuiMenubar menubar = new PubGuiMenubar();
//                for (PubGuiMenubar mod : modulos) {
//                    if (Utils.isNotEmpty(mod.getMenuListSoyMenubar_byOrden())) {
//                        menubar = mod;
//                        for (PubGuiMenu menu : mod.getMenuListSoyMenubar_byOrden()) {
//                            loadRolMenu(menu);
//                        }
//                    }
//                }
                cache.put(key, EntityBeanCopy.clone(modulos.get(0).getMenuListSoyMenubar_byOrden()));
            }
        } else {

        }
    }

    private void loadRolMenu(PubGuiMenu menu) {
        if (menu == null) {
            return;
        }
        if (Utils.isEmpty(menu.getPubGuiMenuRolCollection())) {
            menu.setPubGuiMenuRolCollection(menuService.getAccesosMenuRol(menu));
        }
    }

    /**
     * Devuelve los datos que estan en cache por nombre de la misma.
     *
     * @param <T> Tipo de dato
     * @param nameCache Nombre de la cache a buscar.
     * @return Objecto en cache si existe, caso contrario null.
     */
    public <T> T getDataCache(String nameCache) {
        if (nameCache == null) {
            return (T) cache.get(this.cacheName);
        } else {
            return (T) cache.get(nameCache);
        }
    }

    public void clearCache() {
        cache.remove(cacheName);
    }

    public void clearCache(String key) {
        cache.remove(key);
    }

    //@Override
    public void putCache(String identificador, PubGuiMenubar menubar) {
        if (menubarsMap == null) {
            initMenubarsMap();
        }
        menubarsMap.putIfAbsent(identificador, menubar);
    }

    /**
     * devuelve el menuBar correspondiente, en caso de no estar cacheado lo
     * genera.
     *
     * @param identificador Object
     * @return Object
     */
    //@Override
    public PubGuiMenubar getMenuBar(String identificador) {
        if (identificador == null) {
            return null;
        }

        PubGuiMenubar menubar1 = (PubGuiMenubar) menubarsMap.get(identificador);
        // si no ha sido inicializado, hacerlo:
        if (menubar1 == null) {
            generarMenubar(identificador);
            return getMenuBar(identificador);
        }
        PubGuiMenubar menu = (PubGuiMenubar) EntityBeanCopy.clone(menubar1);
        return menu;
    }

    protected void generarMenubar(String identificador) {

        synchronized (getLockerObject(identificador)) {

            // comprobar si no se entró en espera y ya existe el identificador mapeado:
            PubGuiMenubar menubar1 = (PubGuiMenubar) menubarsMap.get(identificador);
            if (menubar1 != null) {

            } else {
                this.loadMenuBar(identificador);
            }

        }
    }

    private void loadMenuBar(String identificador) {

        PubGuiMenubar menuBar1 = menuService.getMenuBarTree(identificador);

        // si se encontro menubar, realizar la clonacion
        if (menuBar1 != null) {
            PubGuiMenubar menubar2 = (PubGuiMenubar) EntityBeanCopy.clone(menuBar1);
            menubarsMap.putIfAbsent(identificador, menubar2);
        }
    }

    protected String getLockerObject(String identificador) {
        lockerMap.putIfAbsent(identificador, identificador);

        return lockerMap.get(identificador);
    }

    /**
     * Inicializa el map de menubars en vacio
     */
    protected void initMenubarsMap() {
        this.menubarsMap = new ConcurrentHashMap<>();
    }

}
