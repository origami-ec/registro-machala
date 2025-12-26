/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import jodd.props.Props;
import org.bcbg.util.FilesUtil;
import org.bcbg.ws.AppServices;

/**
 *
 * @author AndySanchez
 */
public class PropertiesLoader {

    @Inject
    protected AppServices appServices;

    protected ServletContext sc;

    public void load() {

        Props props1 = new Props();
        try {
            /*String key = System.getenv("keyFiles");
            if (Utils.isEmptyString(key)) {//ES EL SERVIDOR
                props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props"));
            } else {
                props1.load(sc.getResourceAsStream("/WEB-INF/sistema.produccion.props"));
            }*/
            
            //props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props"));
            props1.load(sc.getResourceAsStream("/WEB-INF/sistema.produccion.props"));

            SisVars.ejbRuta = props1.getValue("sistema.ejbRuta");

            SisVars.ws = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios/" + SisVars.apiContext;
            SisVars.wsDocs = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-docs/origami/docs/";
            SisVars.wsPublic = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios/public/api/";
            SisVars.wsZull = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws"));
            SisVars.wsFirmaEC = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-firma-ec/" + SisVars.apiContext;
            SisVars.wsFirmaECFile = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-archivos/" + SisVars.apiContext;
            SisVars.wsMail = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-mail/" + SisVars.apiContext;
            SisVars.wsLogs = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-logs/" + SisVars.apiContext;
            SisVars.wsMedia = (System.getenv("servicios") != null ? System.getenv("servicios") : props1.getValue("sistema.ws")) + "/servicios-archivos/" + SisVars.apiContext;

            SisVars.wsUser = (System.getenv("wsUser") != null ? System.getenv("wsUser") : props1.getValue("sistema.wsUser"));
            SisVars.wsPass = (System.getenv("wsPass") != null ? System.getenv("wsPass") : props1.getValue("sistema.wsPass"));

            SisVars.rutaFirmasElectronicas = (System.getenv("rutaFirmasElectronicas") != null ? System.getenv("rutaFirmasElectronicas") : props1.getValue("sistema.rutaFirmasElectronicas"));
            SisVars.rutaRepositorioArchivo = (System.getenv("rutaRepositorioArchivo") != null ? System.getenv("rutaRepositorioArchivo") : props1.getValue("sistema.rutaRepositorioArchivo"));
            SisVars.rutaRepositorioFirmado = (System.getenv("rutaRepositorioFirmado") != null ? System.getenv("rutaRepositorioFirmado") : props1.getValue("sistema.rutaRepositorioFirmado"));
            SisVars.rutaRepositorioDocumental = (System.getenv("rutaRepositorioDocumental") != null ? System.getenv("rutaRepositorioDocumental") : props1.getValue("sistema.rutaRepositorioDocumental"));
            SisVars.rutaRepositorioNotas = (System.getenv("rutaRepositorioNotas") != null ? System.getenv("rutaRepositorioNotas") : props1.getValue("sistema.rutaRepositorioNotas"));
            SisVars.keyFiles = Integer.valueOf((System.getenv("keyFiles") != null ? System.getenv("keyFiles") : props1.getValue("sistema.keyFiles")));

            SisVars.wsDinardap = (System.getenv("wsDinardap") != null ? System.getenv("wsDinardap") : props1.getValue("sistema.wsDinardap"));
            SisVars.wsDinardapUser = (System.getenv("wsDinardapUser") != null ? System.getenv("wsDinardapUser") : props1.getValue("sistema.wsDinardapUser"));
            SisVars.wsDinardapPass = (System.getenv("wsDinardapPass") != null ? System.getenv("wsDinardapPass") : props1.getValue("sistema.wsDinardapPass"));

            //ejbs
            SisVars.entityManager = (System.getenv("entityManager") != null ? System.getenv("entityManager") : props1.getBaseValue("ejbs.entityManager"));
            SisVars.bpmBaseEngine = (System.getenv("bpmBaseEngine") != null ? System.getenv("bpmBaseEngine") : props1.getBaseValue("ejbs.bpmBaseEngine"));
            SisVars.bpmProcessEngine = (System.getenv("bpmProcessEngine") != null ? System.getenv("bpmProcessEngine") : props1.getBaseValue("ejbs.bpmProcessEngine"));
            SisVars.datasource = (System.getenv("datasource") != null ? System.getenv("datasource") : props1.getBaseValue("ejbs.datasource"));
            //zoning
            SisVars.region = (System.getenv("region") != null ? System.getenv("region") : props1.getBaseValue("zoning.region"));

            FilesUtil.createDirectory(SisVars.rutaFirmasElectronicas);
            FilesUtil.createDirectory(SisVars.rutaRepositorioArchivo);
            FilesUtil.createDirectory(SisVars.rutaRepositorioFirmado);
            FilesUtil.createDirectory(SisVars.rutaRepositorioDocumental);
            FilesUtil.createDirectory(SisVars.rutaRepositorioNotas);

        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public PropertiesLoader(ServletContext sc) {
        this.sc = sc;
    }
}
