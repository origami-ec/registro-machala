/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.ws;

import com.ibm.icu.text.SimpleDateFormat;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bcbg.config.SisVars;
import org.bcbg.entities.LogsTransaccionales;
import org.bcbg.session.UserSession;
import org.bcbg.util.Utils;

/**
 *
 * @author ORIGAMI
 */
@Singleton(name = "asyncEjb")
@ApplicationScoped
public class AsyncEjb implements AsyncServices {

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void guardarLog(String descripcion, String accion, String response, String request, String usuario, String usuarioApp, String ip, String mac, String osClient) {
        try {
            LogsTransaccionales logs = new LogsTransaccionales();
            logs.setApp(SisVars.app);
            logs.setAccion(accion);
            logs.setDescripcion(Utils.quitarTildes(descripcion));
            logs.setResponse(response);
            logs.setUsuario(usuario);
            logs.setUsuarioApp(usuarioApp);
            logs.setIp(ip);
            logs.setMac(mac);
            logs.setRequest(request);
            logs.setOsClient(osClient);
            //new BcbgEjb().methodPOSTwithouAuth(logs, SisVars.wsLogs + "logs/guardar", LogsTransaccionales.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
