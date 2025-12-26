/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

//import com.origami.sgr.services.interfaces.CatastroLocal;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
//import com.origami.ws.catastro.PredioModel;
import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ConsultaCatastro implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultaCatastro.class.getName());

    /*@EJB(beanName = "catastro")
    private CatastroLocal cat;*/

    protected Integer tipo = 1;
    protected Integer render = 0;
    protected String ingreso;
    /*protected PredioModel pm = new PredioModel();
    protected List<PredioModel> list = new ArrayList<>();*/

    @PostConstruct
    protected void iniView() {
        try {

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultar() {
        switch (tipo) {
            case 1:
                this.consultarCedula();
                break;
            case 2:
                this.consultarPredio();
                break;
            default:
                JsfUti.messageWarning(null, "Seleccione el tipo de consulta.", "");
                break;
        }
    }

    public void consultarPredio() {
        try {
            /*if (ingreso != null && ingreso.length() == 17) {
                pm = cat.consultarPredio(ingreso);
                if (pm == null) {
                    pm = new PredioModel();
                    JsfUti.messageWarning(null, "No hay datos en la consulta.", "");
                } else {
                    render = 2;
                    JsfUti.update("mainForm");
                }
            } else {
                JsfUti.messageWarning(null, "Error en la clave catastral.", "");
            }*/
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void consultarCedula() {
        try {
            /*if (ingreso != null && (ingreso.length() == 10 || ingreso.length() == 13)) {
                list = cat.consultarPropiedades(ingreso);
                if (list == null) {
                    list = new ArrayList<>();
                    JsfUti.messageWarning(null, "No hay datos en la consulta.", "");
                } else {
                    render = 1;
                    JsfUti.update("mainForm");
                }
            } else {
                JsfUti.messageWarning(null, "Error en el numero de cedula/ruc.", "");
            }*/
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    /*public PredioModel getPm() {
        return pm;
    }

    public void setPm(PredioModel pm) {
        this.pm = pm;
    }

    public List<PredioModel> getList() {
        return list;
    }

    public void setList(List<PredioModel> list) {
        this.list = list;
    }*/

    public Integer getRender() {
        return render;
    }

    public void setRender(Integer render) {
        this.render = render;
    }

}
