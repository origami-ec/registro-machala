/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.util;

import org.bcbg.config.PropertiesLoader;
import org.bcbg.config.SisVars;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import jodd.datetime.JDateTimeDefault;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.bcbg.entities.Valores;
import org.bcbg.ws.BcbgEjb;
import org.springframework.util.StringUtils;

/**
 * Web application lifecycle listener.
 *
 * @author Fernando
 */
@WebListener()
public class ConfigServletListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ConfigServletListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("ConfigServletListener");

        ServletContext sc = sce.getServletContext();

        String[] s = StringUtils.toStringArray(sc.getInitParameterNames());
        for (String s1 : s) {
            System.out.println("sc.getInitParameterNames(): " + s1);
        }

        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "ERROR");
        System.setProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE", "false");

        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
        JDateTimeDefault.timeZone = TimeZone.getTimeZone("America/Guayaquil");
        Locale.setDefault(new Locale("es", "EC"));

        SisVars.urlbase = sce.getServletContext().getContextPath() + "/";

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("dd MM yyyy HH mm ss");
        ConvertUtils.register(dateConverter, java.util.Date.class);
        PropertiesLoader props1 = new PropertiesLoader(sce.getServletContext());
        props1.load();

        /*String key = System.getenv("keyFiles");
        if (Utils.isEmptyString(key)) {//NO ES EL SERVIDOR
            System.out.println("applicationContext");
            sc.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContext.xml");
        } else {
            System.out.println("applicationContextProd");
            sc.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContextProd.xml");
        }*/
        
        sc.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContext.xml");
        
        Integer timeSesion = consultarValorTexto(Variables.timeSesion);
        if (timeSesion != null) {
            System.out.println("timeSesion: " + timeSesion);
            sc.setSessionTimeout(timeSesion);
        } 
    }

    public Integer consultarValorTexto(String codigo) {
        try {
            Valores valor = (Valores) new BcbgEjb().methodGETwithouAuth(SisVars.ws + "valor/code/" + codigo, Valores.class);
            if (valor != null) {
                return Integer.valueOf(valor.getValorNumeric().toString());
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver driver;
        // clear drivers
        while (drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Error remover drivers.", ex);
            }
        }
        System.out.println("close session factory!!!");
    }

}
