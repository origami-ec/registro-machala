/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.AppMenu;
import org.bcbg.services.ejbs.MenuCache;
import org.bcbg.util.JsfUti;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class CPanelMain implements Serializable {

    @Inject
    private AppMenu menu;
    @Inject
    private MenuCache menuCache;

    @PostConstruct
    public void initView() {

    }

    public void iniciarMenu() {
        this.menuCache.clearCache();
        this.menuCache.init(null);
        JsfUti.redirectFaces("/admin/config/cpanelMain.xhtml");
    }

    /*public void firmarDocumentos() throws IOException {
        String queryZaso1 = "select ce from RegCertificado ce where ce.documento is null and ce.codVerificacion is not null and ce.secuencia = 1";
        //String queryZaso2 = "select ce from RegCertificado ce where ce.documento is not null and ce.codVerificacion is not null and ce.secuencia = 1";
        String getCertificados = queryZaso1;
        List<RegCertificado> certificados;
        certificados = manager.findAll(getCertificados);
        System.out.println("certificados.size(): " + certificados.size());
        Integer count = 0;
        for (RegCertificado rc : certificados) {
            count += 1;
            System.out.println("count " + count);
            fd.tareaFirmaCertificado(rc);
        }
    }*/
    public void sendNotificacion() {
        //as.sendNotificationFirebaseUserAndorid(null, Long.MIN_VALUE);
    }

}
