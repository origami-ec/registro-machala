/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.bcbg.entities.PubGuiMenu;
import org.bcbg.ws.BcbgService;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ANGEL NAVARRO
 */
@RewriteConfiguration
public class UrlConfigurationProvider extends HttpConfigurationProvider {

    @Inject
    protected BcbgService service;
    private static final Logger LOG = Logger.getLogger(UrlConfigurationProvider.class.getName());

    @Override
    public Configuration getConfiguration(ServletContext context) {
//        if (CONFIG.URL_APP == null) {
//            CONFIG.URL_APP = context.getContextPath();
//        }
        ConfigurationBuilder begin = ConfigurationBuilder.begin();
        try {
//            List<Menu> menus = menuEjb.findByNamedQuery("Menu.findByPrettyIsNotNull", (Object[]) null);
//            PubGuiMenubar menubar = consultarMenus();
            //List<PubGuiMenubar> modulos = menuService.getMenuBarList(userSession.getName_user());
            List<PubGuiMenu> menus = consultarMenus();
            if (menus != null) {
                menus.forEach((menu) -> {
                    if (menu.getPrettyPattern() != null && menu.getPrettyPattern().trim().length() > 0) {
                        if (!menu.getPrettyPattern().startsWith("/")) {
                            menu.setPrettyPattern("/" + menu.getPrettyPattern());
                        }
                        if (!menu.getHrefUrl().startsWith("/")) {
                            menu.setHrefUrl("/" + menu.getHrefUrl());
                        }

                        //menu.setPrettyPattern(context.getContextPath() + menu.getPrettyPattern());
                        //  menu.setPrettyPattern("/ventanillaInteligente/" + menu.getPrettyPattern());
                        if (menu.getHrefUrl() != null && menu.getHrefUrl().trim().length() > 0) {
                            begin.addRule(Join.path(menu.getPrettyPattern()).to(menu.getHrefUrl().trim()).withInboundCorrection()).withId(menu.getId().toString());
                        }
                    }
                });
            }
            begin.addRule(Join.path("/404").to("404.xhtml"));
            begin.addRule(Join.path("/expired-page").to("expired.xhtml"));
            begin.addRule(Join.path("/page").to("error.xhtml"));
            begin.addRule(Join.path("/general").to("empty.xhtml"));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return begin;
    }

    @Override
    public int priority() {
        return 100;
    }

    public List<PubGuiMenu> consultarMenus() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            //  System.out.println("//MENU FINDALL " + SisVars.ws + "menu/findAll");
            URI uri = new URI(SisVars.ws + "menu/findAll");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<PubGuiMenu[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, PubGuiMenu[].class);
            //Object[] obj = (Object[]) restTemplate.getForObject(new URI(url), resultClazz);
            if (response != null) {
                // System.out.println("consultarMenus");
                return Arrays.asList(response.getBody());
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            System.out.println(e);
            //e.printStackTrace();
            //Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
