/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.text.WordUtils;
import org.bcbg.config.SisVars;
import org.bcbg.session.UserSession;
import org.bcbg.util.Utils;

/**
 *
 * @author ORIGAMI
 */
@Named()
@ViewScoped
public class BienvenidaMB implements Serializable {

    @Inject
    protected UserSession us;
    private String mision, vision, nombre;

    @PostConstruct
    public void init() {
        mision = SisVars.MISION;
        vision = SisVars.VISION;
    }

    public String getSaludo() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String bienvenido = "Bienvenid@ ";
        if (timeOfDay >= 0 && timeOfDay < 12) {
            bienvenido = "Buenos días ";
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            bienvenido = "Buenas tardes ";
        } else if (timeOfDay >= 18 && timeOfDay < 21) {
            bienvenido = "Buenas noches ";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            bienvenido = "Buenas noches ";
        }
        return bienvenido + us.getNombres();
    }

    public String getFrase() {
        return us.getFrase();
    }

    public String getMision() {
        return mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

}
