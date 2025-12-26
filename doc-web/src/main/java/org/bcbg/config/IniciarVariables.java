/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.config;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.bcbg.util.Variables;
import org.bcbg.ws.AppServices;

/**
 *
 * @author Xndy
 */
@Singleton(name = "iniciarVariables")
@Startup
@ApplicationScoped
public class IniciarVariables {

    private static final Logger LOG = Logger.getLogger(IniciarVariables.class.getName());
    @Inject
    protected AppServices appServices;

    public void appinit(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOG.info(" @@@ Holi variables CDI way...");
        SisVars.MISION = appServices.consultarValorTexto(Variables.mision);
        SisVars.VISION = appServices.consultarValorTexto(Variables.vision);
        SisVars.urlCorreoZimbra = appServices.consultarValorTexto(Variables.urlCorreoZimbra);
        SisVars.JITSI = appServices.consultarValorTexto(Variables.jitsi);
        SisVars.JENKINS = appServices.consultarValorTexto(Variables.jenkins);
    }
}
