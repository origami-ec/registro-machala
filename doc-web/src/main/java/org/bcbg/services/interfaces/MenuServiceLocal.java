/*
 *  Origami Solutions
 */
package org.bcbg.services.interfaces;

import org.bcbg.session.WfUserSession;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenuRol;
import org.bcbg.entities.PubGuiMenuTipoAcceso;
import org.bcbg.entities.PubGuiMenubar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author fernando
 */
@Local
public interface MenuServiceLocal {

    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenu menuPadre);

    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenubar menuBar);

    public PubGuiMenubar getMenuBarTree(String menubarIdent);

    public PubGuiMenubar getMenuBar(String menubarIdent);

    public PubGuiMenu getMenu(Integer idMenu);

    public PubGuiMenubar genMenuStruct(String menubarIdent, WfUserSession userSession);

    //***************** Metodos para mantenimiento de menu *******************//
    public PubGuiMenu guardar(PubGuiMenu menu);

    public List<PubGuiMenubar> getMenuBarList(String user);

    public List<PubGuiMenuTipoAcceso> getMenuAccesoList();

    public List<PubGuiMenuRol> getAccesosMenuRol(PubGuiMenu menu);

    public PubGuiMenu borrarMenu(PubGuiMenu menu);

    public PubGuiMenuRol borrarMenu(PubGuiMenuRol menuRol);

}
