/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.config;

/**
 *
 * @author Origami
 */
public class SisVars {

    public static String app = "ORIGAMIGT-WEB";
    public static String ubicacion = "Guayaquil, Ecuador"; //Campo valido para la firma
    public static String apiContext = "origami/api/"; //La concatenacion debe terminar en / para que coja bien porfa
    public static String eventMenu = "Acceso al menu";
    public static String eventClick = "Events";
    public static String tcSolicitud = "SOLICITUD";
    public static String tcVentanilla = "VENTANILLA";
    public static String urlbase;// = "/asdf/geoPortalSni/";
    public static String facesUrl = "/faces";
    public static String notaAgregar = "A";
    public static String notaEliminar = "E";
    public static String ejbRuta;// = "java:global/JavaServerFaces/";

    /**
     * Si va s a agregar una nueva ruta debes tambein agregarla en Utils ...
     * getFilterRuta en ese metodo para poder hacer el reemplazo al moneto d
     * obtener el archivo y agregar en asgar media la condicion todo esto es
     * para que no se vea la ruta del servidor en la url
     */
    public static String rutaRepositorioArchivo; //= "C:/servers_files/archivos/ "; 
    public static String rutaFirmasElectronicas; //= "C:/servers_files/firmasElectronicas/";  RECUERDA QUE SI CAMBIAS ESTA RUTA TAMBIEN DEBES CAMBIARLA EN EL SERVICIO DE FIRMA EC
    public static String rutaRepositorioFirmado; //= "C:/servers_files/firmasElectronicas/"; 
    public static String rutaRepositorioDocumental; //= "C:/servers_files/firmasElectronicas/"; 
    public static String rutaRepositorioNotas; //= "C:/servers_files/notas/"; 
    public static Integer keyFiles; //= "C:/servers_files/notas/"; 

    public static String ws;
    public static String wsDocs;
    public static String wsPublic;
    public static String wsZull;
    public static String wsFirmaEC;
    public static String wsFirmaECFile;
    public static String wsMail;
    public static String wsMedia;
    public static String wsLogs;
    public static String wsUser;
    public static String wsPass;
    public static String wsDinardap;
    public static String wsDinardapUser;
    public static String wsDinardapPass;
    public static String urlCorreoZimbra;

    //CONEXION BASE RPP_DOCUMENTAL
    public static String docDriverClass;
    public static String docUrl;
    public static String docUserName;
    public static String docPassword;
    public static int docMinPoolSize = 1;
    public static int docMaxPoolSize = 1;
    public static int docMaxIdleTime = 1;

    //JNDI EJB
    public static String entityManager;
    public static String bpmBaseEngine;
    public static String bpmProcessEngine;
    public static String datasource;

    //zoning
    public static String region;

    //VER - DESCARGAR DOC
    public static String VIEW_DOC = "VIEW";
    public static String DOWNLOAD_DOC = "DOWNLOAD";

    public static String rechazoSolicitud = "RECHAZO DE SOLICITUD";
    public static String reasignacionSolicitud = "REASIGNACION DE SOLICITUD";
    public static String finalizaSolicitud = "SOLICITUD FINALIZADA";

    public static String nombreManualVentanilla = "ma_MANUAL_DE_USUARIO_VENTANILLA_INTELIGENTE.pdf";
    public final static String nota = "NOTA";
    public final static String linea = "LINEA";
    public final static String remarcado = "REMARCADO";
    public final static String sello = "SELLOS";
    /**
     * VARIABLES SUGERIDAS PARA CONFIGURACION
     */
    public static Integer maxAnchoNota = 200;
    public static Integer maxAltoNota = 100;
    public static Integer maxAltoLinea = 10;
    public static Integer maxAltoRemarcado = 50;

    public static String MISION;
    public static String VISION;
    public static String TIEMPO_SESION;
    public static String JENKINS;
    public static String JITSI;
}
