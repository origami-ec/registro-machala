/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.AclLogin;
import org.bcbg.entities.Apps;
import org.bcbg.entities.LogsTransaccionales;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.models.Data;
import org.bcbg.models.DatosReporte;
import org.bcbg.session.ServletSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class AsgardLogsMB implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    protected UserSession us;
    @Inject
    private BcbgService service;

    private LazyModelWS<LogsTransaccionales> asgardLogs;
    private LogsTransaccionales logs;

    private LazyModelWS<AclLogin> aclLogins;
    private AclLogin aclLogin;

    private List<Apps> apps;
    private List<String> acciones;
    private Date desde, hasta;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        desde = new Date();
        hasta = new Date();
        //asgardLogs = new LazyModelWS<>(SisVars.wsLogs + "logs/find?sort=id,DESC", LogsTransaccionales[].class, us.getToken());
        //aclLogins = new LazyModelWS<>(SisVars.wsLogs + "aclLogin/find?sort=id,DESC", AclLogin[].class, us.getToken());
        apps = service.methodListGET(SisVars.ws + "apps/find", Apps[].class);
        acciones = new ArrayList<>();
        acciones.add("Acceso al menú");
        acciones.add("Events");
        acciones.add("GET");
        acciones.add("HEAD");
        acciones.add("POST");
        acciones.add("PUT");
    }

    public void generarReporte() {
        System.out.println("generarReporte");

        try {
            if (desde != null && hasta != null) {
                String nombreReporte = "Reporte_Logs_" + Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", new Date());
                DatosReporte data = new DatosReporte();
                data.setFechadesde(desde.getTime());
                data.setFechahasta(hasta.getTime());
                data.setIncluirFechas(Boolean.TRUE);
                Map map = new HashMap();
                map.put("DESDE", Utils.dateFormatPattern("yyyy-MM-dd", desde));
                map.put("HASTA", Utils.dateFormatPattern("yyyy-MM-dd", hasta));
                map.put("NOMBRE_REPORTE", nombreReporte);
                data.setNombreArchivoPDF(nombreReporte);
                data.setParametros(map);
                //ss.setUrlWebService(SisVars.wsLogs + "reporte/transacional");
                ss.setDatos(data);
                JsfUti.redirectFacesNewTab("/DocumentoWs");
            } else {
                JsfUti.messageError(null, "Debe seleccionar una hora y una fecha", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public LazyModelWS<LogsTransaccionales> getAsgardLogs() {
        return asgardLogs;
    }

    public void setAsgardLogs(LazyModelWS<LogsTransaccionales> asgardLogs) {
        this.asgardLogs = asgardLogs;
    }

    public LogsTransaccionales getLogs() {
        return logs;
    }

    public void setLogs(LogsTransaccionales logs) {
        this.logs = logs;
    }

    public LazyModelWS<AclLogin> getAclLogins() {
        return aclLogins;
    }

    public void setAclLogins(LazyModelWS<AclLogin> aclLogins) {
        this.aclLogins = aclLogins;
    }

    public AclLogin getAclLogin() {
        return aclLogin;
    }

    public void setAclLogin(AclLogin aclLogin) {
        this.aclLogin = aclLogin;
    }

    public List<Apps> getApps() {
        return apps;
    }

    public void setApps(List<Apps> apps) {
        this.apps = apps;
    }

    public List<String> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<String> acciones) {
        this.acciones = acciones;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

}
