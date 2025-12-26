/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bcbg.session.UserSession;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.entities.PubGuiMenuRol;
import org.bcbg.models.Document;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandlink.CommandLink;

/**
 *
 * @author jesus
 */
public class AppViewHandler extends ViewHandlerWrapper {

    private UserSession userSession;
    private final ViewHandler wrapped;
    private PubGuiMenu pubGuiMenu;
    private static Logger LOGGER = Logger.getLogger(AppViewHandler.class.getName());

    public AppViewHandler(ViewHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return super.restoreView(context, viewId); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) {
        try {
            if (context.isPostback()) {
                if (userSession == null) {
                    super.renderView(context, viewToRender);
                } else {
                    if (userSession.getRoles().contains(1L)) {
                        super.renderView(context, viewToRender);
                    } else {
                        processView(context, viewToRender);
                    }
                }
            } else if (context.getPartialViewContext().isAjaxRequest()) {
                super.renderView(context, viewToRender);
            } else {
                if (userSession == null) {
                    userSession = (UserSession) JsfUti.getSessionBean("userSession");
                }
                if (userSession == null) {
                    super.renderView(context, viewToRender);
                } else {
                    //id 1 rol ADMIN 
                    /*
                        DESCOMENTAR SI SE NECESITA QUE EL ADMIN SEA SUPERADMIN xD
                     */
//                    if (userSession.getRoles().contains(1L)) {
//                        super.renderView(context, viewToRender);
//                    } else {
//                        processView(context, viewToRender);
//                    }
                    processView(context, viewToRender);
                }
            }
        } catch (IOException | FacesException ex) {
            Logger.getLogger(AppViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }

    public void processView(FacesContext context, UIViewRoot view) throws IOException {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String requestURI = request.getRequestURI().replace(request.getContextPath() + "/", "");
        pubGuiMenu = userSession.getMenuRol(requestURI);
        processViewTree(view, requestURI);
        super.renderView(context, view);
    }

    private List<String> processViewTree(UIComponent component, String url) {
        try {
            if (pubGuiMenu != null && Utils.isNotEmpty(component.getChildren())) {
                for (UIComponent child : component.getChildren()) {
                    addLogAction(child, url);
                    String get = (String) child.getAttributes().get("styleClass");
                    if (get != null) {
                        for (PubGuiMenuRol menuRol : pubGuiMenu.getPubGuiMenuRolCollection()) {
                            fromJsonDocuments(menuRol);
                            if (menuRol.getDocuments() != null && userSession.getRoles().contains(menuRol.getRol())) {
                                for (Document d : menuRol.getDocuments()) {
                                    if (!get.isEmpty() && d.getName().trim().contains(get)) {
                                        renderedComponent(child, d.getEnable(), null);
                                    }
                                }
                            }
                        }
                    }
                    if (child.getChildCount() > 0) {
                        processViewTree(child, url);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            Logger.getLogger(AppViewHandler.class.getName()).log(Level.SEVERE, "Load url" + url, e);
            return null;
        }
    }

    private void renderedComponent(UIComponent child, boolean rendered, List<String> componentIds) {
        child.setRendered(rendered);
    }

    private void fromJsonDocuments(PubGuiMenuRol menuRol) {
        Gson gson = new Gson();
        if (menuRol.getAcciones() != null && !menuRol.getAcciones().isEmpty()) {
            menuRol.setDocuments(gson.fromJson(menuRol.getAcciones(), new TypeToken<List<Document>>() {
            }.getType()));
        }
    }

    private void addLogAction(UIComponent child, String url) {
        if (child.getClientId() != null) {
            try {
                String value;
                if (CommandButton.class.isAssignableFrom(child.getClass())) {
                    CommandButton button = (CommandButton) child.findComponent(child.getClientId());
                    if (button != null) {
                        value = button.getValue() != null ? button.getValue().toString() : button.getTitle() != null ? button.getTitle() : "";
                        button.setOnclick("btnLogFunction([{name:'name_', value:'" + "Menú: " + pubGuiMenu.getNombre() + " click: " + value + "'}, {name:'pagina_', value:'" + url + "'}]);");
                    }
                }
                if (CommandLink.class.isAssignableFrom(child.getClass())) {
                    CommandLink button = (CommandLink) child.findComponent(child.getClientId());
                    if (button != null) {
                        value = button.getValue() != null ? button.getValue().toString() : button.getTitle() != null ? button.getTitle() : "";
                        button.setOnclick("btnLogFunction([{name:'name_', value:'" + "Menú: " + pubGuiMenu.getNombre() + " click: " + value + "'}, {name:'pagina_', value:'" + url + "'}]);");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
