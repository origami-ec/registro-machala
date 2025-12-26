/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import jodd.props.Props;

/**
 *
 * @author fernando
 */
public class PropertiesLoader {

    protected ServletContext sc;

    public void load() {

        Props props1 = new Props();
        try {
            props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props"));
            //props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props.desarrollo"));
            //props1.load(sc.getResourceAsStream("/WEB-INF/sistema.props.produccion"));

            SisVars.ejbRuta = props1.getValue("sistema.ejbRuta");
            SisVars.rutaAnexos = props1.getValue("sistema.rutaAnexos");
            SisVars.rutaTemporales = props1.getValue("sistema.rutaTemporales");
            SisVars.rutaFirmados = props1.getValue("sistema.rutaFirmados");
            SisVars.rutaFirmaEC = props1.getValue("sistema.rutaFirmaEC");
            SisVars.rutaDocumentos = props1.getValue("sistema.rutaDocumentos");
            SisVars.formatoArchivos = props1.getValue("sistema.formatoArchivos");

            // VARIABLES CONFIGURACION CORREO
            SisVars.correo = props1.getValue("mail.correo");
            SisVars.pass = props1.getValue("mail.pass");
            SisVars.smtp_Host = props1.getValue("mail.smtp_Host");
            SisVars.smtp_Port = props1.getValue("mail.smtp_Port");
            SisVars.ssl = Boolean.parseBoolean(props1.getValue("mail.ssl"));

            //variables condb
            SisVars.driverClass = props1.getValue("condb.driverClass");
            SisVars.url = props1.getValue("condb.url");
            SisVars.userName = props1.getValue("condb.username");
            SisVars.password = props1.getValue("condb.password");
            SisVars.maxIdleTime = Integer.parseInt(props1.getValue("condb.maxIdleTime"));
            SisVars.maxPoolSize = Integer.parseInt(props1.getValue("condb.maxPoolConsize"));
            SisVars.minPoolSize = Integer.parseInt(props1.getValue("condb.minPoolConSize"));

            //variables docdb
            SisVars.docDriverClass = props1.getValue("docdb.driverClass");
            SisVars.docUrl = props1.getValue("docdb.url");
            SisVars.docUserName = props1.getValue("docdb.username");
            SisVars.docPassword = props1.getValue("docdb.password");
            SisVars.docMaxIdleTime = Integer.parseInt(props1.getValue("docdb.maxIdleTime"));
            SisVars.docMaxPoolSize = Integer.parseInt(props1.getValue("docdb.maxPoolConsize"));
            SisVars.docMinPoolSize = Integer.parseInt(props1.getValue("docdb.minPoolConSize"));

            //variables archivodb
            SisVars.archivoDriverClass = props1.getValue("archivodb.driverClass");
            SisVars.archivoUrl = props1.getValue("archivodb.url");
            SisVars.archivoUserName = props1.getValue("archivodb.username");
            SisVars.archivoPassword = props1.getValue("archivodb.password");
            SisVars.archivoMaxIdleTime = Integer.parseInt(props1.getValue("archivodb.maxIdleTime"));
            SisVars.archivoMaxPoolSize = Integer.parseInt(props1.getValue("archivodb.maxPoolConsize"));
            SisVars.archivoMinPoolSize = Integer.parseInt(props1.getValue("archivodb.minPoolConSize"));

            //variables activiti
            SisVars.actdriverClass = props1.getValue("activiti.driverClass");
            SisVars.acturl = props1.getValue("activiti.url");
            SisVars.actuserName = props1.getValue("activiti.username");
            SisVars.actpassword = props1.getValue("activiti.password");
            SisVars.actmaxIdleTime = Integer.parseInt(props1.getValue("activiti.maxIdleTime"));
            SisVars.actmaxPoolSize = Integer.parseInt(props1.getValue("activiti.maxPoolConsize"));
            SisVars.actminPoolSize = Integer.parseInt(props1.getValue("activiti.minPoolConSize"));

            //ejbs
            SisVars.entityManager = props1.getBaseValue("ejbs.entityManager");
            SisVars.bpmBaseEngine = props1.getBaseValue("ejbs.bpmBaseEngine");
            SisVars.bpmProcessEngine = props1.getBaseValue("ejbs.bpmProcessEngine");
            SisVars.datasource = props1.getBaseValue("ejbs.datasource");
            //zoning
            SisVars.region = props1.getBaseValue("zoning.region");

            //WEBSERVICE
            SisVars.dominioVentanilla = props1.getValue("webservices.dominioVentanilla");
            SisVars.urlWSVentanilla = props1.getValue("webservices.urlWSVentanilla");
            SisVars.wsdlMunicipio = props1.getValue("webservices.wsdlMunicipio");
            SisVars.urlWsApiClientes = props1.getValue("webservices.urlWsApiClientes");
            SisVars.urlWsApiFacturas = props1.getValue("webservices.urlWsApiFacturas");
            SisVars.accesstoken = props1.getValue("webservices.accesstoken");
            SisVars.idUsuarioEgob = Integer.parseInt(props1.getValue("webservices.idUsuarioEgob"));
            SisVars.urlFirmaEC = props1.getValue("webservices.urlFirmaEC");
            SisVars.urlWsDinardap = props1.getValue("webservices.urlWsDinardap");
            SisVars.urlWsEmail = props1.getValue("webservices.urlWsEmail");
            SisVars.urlOrigamiZuul = props1.getValue("webservices.urlOrigamiZuul");
            SisVars.urlOrigamiDocs = props1.getValue("webservices.urlOrigamiDocs");
            SisVars.urlOrigamiMedia = props1.getValue("webservices.urlOrigamiMedia");
            SisVars.urlYuraDocs = props1.getValue("webservices.urlYuraDocs");
            SisVars.ambienteFacturacion = props1.getValue("webservices.ambienteFacturacion");
            SisVars.isOnline = Boolean.parseBoolean(props1.getValue("webservices.isOnline"));
            SisVars.enviar = Boolean.parseBoolean(props1.getValue("automatic.enviar"));

        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public PropertiesLoader(ServletContext sc) {
        this.sc = sc;
    }
}
