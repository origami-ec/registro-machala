package org.origami.ws.service;

import com.github.javaparser.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.origami.ws.entities.origami.*;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.repository.origami.PubGuiMenuRepository;
import org.origami.ws.repository.origami.PubGuiMenuRolRepository;
import org.origami.ws.repository.origami.PubGuiMenuTipoAccesoRepository;
import org.origami.ws.repository.origami.PubGuiMenubarRepository;
import org.origami.ws.repository.origami.RolUsuarioRepository;
import org.origami.ws.repository.security.UsuarioRepository;
import org.origami.ws.util.Constantes;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PubGuiMenuService {

    private static final Logger logger = Logger.getLogger(PubGuiMenuService.class.getName());

    @Autowired
    private PubGuiMenuRepository menuRepository;
    @Autowired
    private PubGuiMenubarRepository menubarRepository;
    @Autowired
    private PubGuiMenuTipoAccesoRepository menuTipoAccesoRepository;
    @Autowired
    private PubGuiMenuRolRepository menuRolRepository;
    @Autowired
    private PubGuiMenuRolService pubGuiMenuRolService;
    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    public Menu findMenu(Menu data) {
        return menuRepository.findOne(Example.of(data)).get();
    }

    public List<Menu> findMenuAll() {
        return menuRepository.findAll();
    }

    public List<Menu> findAllMenu(Menu data, Pageable pageable) {
        if (data.getIdMenuPadre() != null) {
            if (data.getIdMenuPadre() > 0) {
                data.setMenuPadre(new Menu(data.getIdMenuPadre().intValue()));
                data.setIdMenuPadre(null);
            } else {
                return loadMenu(menuRepository.findAllByMenuPadreIsNullOrderByNumPosicion());
            }
        }
        return loadMenu(menuRepository.findAll(Example.of(data), pageable).getContent());
    }

    public List<Menubar> findAllMenuBar(Menubar data) {
        List<Menubar> menus = menubarRepository.findAll(Example.of(data));
        for (Menubar menubar : menus) {
            menubar.setMenuListSoyMenubar_byOrden(loadMenu(menuRepository.findAll(Example.of(new Menu(menubar)), Sort.by(Sort.Direction.ASC, "numPosicion"))));
        }
        return menus;
    }

    public List<Menubar> findAllMenuBar(String user) {
        try {
            List<Menubar> menus = menubarRepository.findAll();
            List<Menubar> menuUser = new ArrayList<>(menus.size());
            Menubar menubar = new Menubar(1);
            List<Menu> list = loadMenu(menuRepository.findAll(Example.of(new Menu(new Menubar(1))), Sort.by(Sort.Direction.ASC, "numPosicion")), user);
            if (!Utils.isNullOrEmpty(list)) {
                menubar.setMenuListSoyMenubar_byOrden(list);
                menuUser.add(menubar);
            }
            return menuUser;
        } catch (Exception e) {
            logger.log(Level.INFO, "Exception findAllMenuBar {0} ", e.getMessage());
            return null;
        }
    }

    public Menubar findMenuBar(Menubar data) {
        Menubar menubar = menubarRepository.findOne(Example.of(data)).get();
        menubar.setMenuListSoyMenubar_byOrden(loadMenu(menuRepository.findAll(Example.of(new Menu(menubar)), Sort.by(Sort.Direction.ASC, "numPosicion"))));
        return menubar;
    }

    public List<MenuTipoAcceso> findAllMenuTipoAcceso(MenuTipoAcceso data) {
        return menuTipoAccesoRepository.findAll(Example.of(data));
    }

    public List<MenuRol> findAllMenuRol(MenuRol data) {
        return pubGuiMenuRolService.findAll(data);
    }

    public Menu registrarActualizarMenu(Menu data) {
        System.out.println(data.getIcono());
        if (data.getIdMenuPadre() != null) {
            data.setMenuPadre(menuRepository.findById(data.getIdMenuPadre().intValue()).get());
        }
        if (data.getId() != null && Utility.isNotEmpty(data.getPubGuiMenuRolCollection())) {
            for (MenuRol menuRol : data.getPubGuiMenuRolCollection()) {
                menuRol.setMenu(data);
            }
        }
        Menu guiMenu = menuRepository.save(data);
        return guiMenu;
    }

    public void eliminarMenu(Menu data) {
        deleteAllMenuRolByMenu(findAllByMenuRolByMenu(data.getId()));
        menuRepository.delete(data);
    }

    public MenuRol guardarRol(MenuRol data) {
        if (!data.getDocuments().isEmpty() && data.getDocuments().get(0).getEnable() != null) {
            data.setAcciones(new Gson().toJson(data.getDocuments()));
        }
        return menuRolRepository.save(data);
    }

    public void deleteMenuRol(MenuRol data) {
        menuRolRepository.delete(data);
    }

    //SIRVE PARA SETEAR EL MENU PADRE
    private List<Menu> loadMenu(List<Menu> menus) {
        Menu menuPadre;
        for (Menu menu : menus) {
            if (menu.getMenuPadre() == null) {//ES UN MENU PADRE DEL MENU BAR =O
                menuPadre = new Menu();
                menuPadre.setMenuPadre(new Menu(menu.getId()));
                menu.setMenusHijos_byNumPosicion(loadMenu(menuRepository.findAll(Example.of(menuPadre), Sort.by(Sort.Direction.ASC, "numPosicion"))));
            } else {//TIENE MENU PADRE =V
                menu.setIdMenuPadre(menu.getMenuPadre().getId().longValue());
            }
        }
        return menus;
    }

    private List<Menu> loadMenu(List<Menu> menus, String user) {
        try {
            Usuario u = userRepository.findByUsuarioNombre(user);
            List<Menu> temp = new ArrayList<>();
            if (u != null && u.getId() != null && !Utils.isNullOrEmpty(menus)) {
                List<RolUsuario> rolsUser = rolUsuarioRepository.findAllByUsuario(u.getId());
                RolUsuario admin = rolUsuarioRepository.findFirstByRolNombreAndUsuarioOrderByIdDesc(Constantes.rolAdmin, u.getId());
                List<Menu> tempH;
                Menu menuPadre;
                for (Menu menu : menus) {
                    if (menu.getMenuPadre() == null) {//ES UN MENU PADRE DEL MENU BAR =O
                        menuPadre = new Menu();
                        menuPadre.setMenuPadre(new Menu(menu.getId()));
                        List<Menu> mhijos = loadMenu(menuRepository.findAll(Example.of(menuPadre), Sort.by(Sort.Direction.ASC, "numPosicion")), user);
                        tempH = new ArrayList<>();
                        if (mhijos != null) {
                            for (Menu mh : mhijos) {
                                if (mh.getTipoAcceso().getIdentificador().equalsIgnoreCase("publico")) {
                                    tempH.add(mh);
                                } else {
                                    if (admin == null) {
                                        for (RolUsuario ru : rolsUser) {
                                            for (MenuRol pgmr : mh.getPubGuiMenuRolCollection()) {
                                                if (pgmr.getRol().equals(ru.getRol().getId())) {
                                                    tempH.add(mh);
                                                }
                                            }
                                        }
                                    } else {
                                        tempH.add(mh);
                                    }
                                }
                            }
                        }
                        if (tempH.size() > 0) {
                            menu.setMenusHijos_byNumPosicion(tempH);
                            temp.add(menu);
                        }
                    } else {//TIENE MENU PADRE =V
                        menu.setIdMenuPadre(menu.getMenuPadre().getId().longValue());
                        temp.add(menu);
                    }
                }
            }
            return temp;
        } catch (Exception e) {
            logger.log(Level.INFO, "loadMenu Exception {0} ", e.getMessage());
            return null;
        }
    }

    public List<MenuRol> findAllByMenuRolByMenu(Integer idMenu) {
        return menuRolRepository.findAllByMenu_Id(idMenu);
    }

    public void deleteAllMenuRolByMenu(List<MenuRol> data) {
        menuRolRepository.deleteAll(data);
    }

}
