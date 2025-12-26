/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.Rol;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.TipoTramite;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author OrigamiEC
 */
@Named
@ViewScoped
public class ServiciosRequisitosMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;
    @Inject
    private UserSession session;

    private ServiciosDepartamentoRequisitos requisito;
    private List<ServiciosDepartamentoRequisitos> serviciosRequisitoList;
    private ServiciosDepartamento item;
    private Departamento departamento;
    private List<ServiciosDepartamento> itemList;
    private List<Departamento> departamentos;
    private List<TipoTramite> tipoTramites;
    private TipoTramite tipoTramite;

    @PostConstruct
    public void init() {
        iniciarTipo();
    }

    public void initRequisito() {
        loadModel();
        initRequisitos();
    }

    public void iniciarTipo() {
        loadModel();
        departamento = new Departamento();
        serviciosRequisitoList = new ArrayList<>();
        initDepartamentos();
    }

    private void initDepartamentos() { 
        departamentos = new ArrayList<>();
        if (session.getIsUserAdmin()) {
            departamentos = appServices.findAllDepartamentoByServiciosDepartamento();
        } else {
            departamento = appServices.buscarDepartamento(new Departamento(session.getDepts().get(0)));
            departamentos.add(departamento);
            initServiciosDepartamentos();
        }
    }

    public void initServiciotRequisito() {
        requisito = new ServiciosDepartamentoRequisitos();
    }

    public void loadModel() {
        item = new ServiciosDepartamento();
        tipoTramite = new TipoTramite();
        requisito = new ServiciosDepartamentoRequisitos();
    }

    public void initServiciosDepartamentos() {
        try {
            if (departamento != null && departamento.getId() != null) {
                itemList = new ArrayList<>();
                ServiciosDepartamento i = new ServiciosDepartamento(new Departamento(departamento.getId()), new TipoTramite(Boolean.TRUE, "VU"));
                itemList = appServices.getListItems(i);
            }
        } catch (Exception e) {
            System.out.println("//Exception initServiciosDepartamento: " + e.getMessage());
        }
    }
    
    public void openModalRequisito(){
        this.requisito.setOrden(serviciosRequisitoList.size()+1);
        JsfUti.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUti.update("formNuevoRequisito");
    }

    public void initRequisitos() {
        serviciosRequisitoList = new ArrayList<>();
        ServiciosDepartamentoRequisitos req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(item.getId()));
        serviciosRequisitoList = appServices.getDeptsItemsRequisito(req);
        JsfUti.executeJS("PF('dItems').clearFilters()");
        JsfUti.update("dItems");
    }

    public void editarRequisito(ServiciosDepartamentoRequisitos data) {
        requisito = data;
        JsfUti.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUti.update("formNuevoRequisito");
    }

    public void guardarRequisito() {
        if (validar()) {
            ServiciosDepartamentoRequisitos itemREST;
            requisito.setServiciosDepartamento(item);
            requisito.setNombre(requisito.getNombre().toUpperCase());
            requisito.setDescripcion(requisito.getDescripcion().toUpperCase());
            if (requisito.getId() == null) {
                itemREST = (ServiciosDepartamentoRequisitos) service.methodPOST(requisito, SisVars.ws + "serviciosDepartamentoItemsRequisitos/guardar", ServiciosDepartamentoRequisitos.class);
            } else {
                itemREST = (ServiciosDepartamentoRequisitos) service.methodPUT(requisito, SisVars.ws + "serviciosDepartamentoItemsRequisitos/actualizar", ServiciosDepartamentoRequisitos.class);
            }
            if (itemREST != null) {
                ServiciosDepartamentoRequisitos req;
                req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(item.getId()));
                serviciosRequisitoList = appServices.getDeptsItemsRequisito(req);
                JsfUti.messageInfo(null, "Datos actualizados", "");
                JsfUti.executeJS("PF('dlgNuevoRequisito').hide();");
                JsfUti.update("mainForm");
                requisito = new ServiciosDepartamentoRequisitos();
            } else {
                JsfUti.messageError(null, "Intente nuevamente", "");
            }
        }
    }

    public Boolean validar() {
        if (requisito.getNombre() == null
                || requisito.getNombre().isEmpty()) {
            JsfUti.messageError(null, "Debe ingresar un nombre", "");
            return false;
        }
        if (requisito.getTamanioArchivo() == null) {
            JsfUti.messageError(null, "Debe escoger el tamaño máximo del requisito", "");
            return false;
        }
        if (requisito.getFormato_archivo() == null || requisito.getFormato_archivo().isEmpty()) {
            JsfUti.messageError(null, "Debe escoger el formato que tendrá el requisito", "");
            return false;
        }
        if (requisito.getOrden() == null
                || requisito.getOrden() < 1 ) {
            JsfUti.messageError(null, "Debe ingresar un número de orden válido", "");
            return false;
        }
        return true;
    }

    public void eliminarRequisito(ServiciosDepartamentoRequisitos requisito) {
        requisito.setEstado(Boolean.FALSE);
        service.methodPUT(requisito, SisVars.ws + "serviciosDepartamentoItemsRequisitos/actualizar", ServiciosDepartamentoRequisitos.class);
        serviciosRequisitoList = new ArrayList<>();
        ServiciosDepartamentoRequisitos req;
        req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(item.getId()));
        serviciosRequisitoList = appServices.getDeptsItemsRequisito(req);
        JsfUti.messageInfo(null, "Datos actualizados", "");
        JsfUti.update("mainForm");
    }

    public void actualizarItem() {
        ServiciosDepartamento itemREST = (ServiciosDepartamento) service.methodPUT(item, SisVars.ws + "serviciosDepartamentoItems/guardar", ServiciosDepartamento.class);
        if (itemREST != null) {
            JsfUti.messageInfo(null, "Datos actualizados", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
    }

    public ServiciosDepartamento getItem() {
        return item;
    }

    public void setItem(ServiciosDepartamento item) {
        this.item = item;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<ServiciosDepartamento> getItemList() {
        return itemList;
    }

    public void setItemList(List<ServiciosDepartamento> itemList) {
        this.itemList = itemList;
    }

    public ServiciosDepartamentoRequisitos getRequisito() {
        return requisito;
    }

    public void setRequisito(ServiciosDepartamentoRequisitos requisito) {
        this.requisito = requisito;
    }

    public List<ServiciosDepartamentoRequisitos> getServiciosRequisitoList() {
        return serviciosRequisitoList;
    }

    public void setServiciosRequisitoList(List<ServiciosDepartamentoRequisitos> serviciosRequisitoList) {
        this.serviciosRequisitoList = serviciosRequisitoList;
    }

    public List<TipoTramite> getTipoTramites() {
        return tipoTramites;
    }

    public void setTipoTramites(List<TipoTramite> tipoTramites) {
        this.tipoTramites = tipoTramites;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

}
