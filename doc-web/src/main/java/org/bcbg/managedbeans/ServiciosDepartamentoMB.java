/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.ServiciosDepartamentoRequisitos;
import org.bcbg.entities.TipoTramite;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author ORIGAMI1
 */
@Named
@ViewScoped
public class ServiciosDepartamentoMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;
    @Inject
    private UserSession session;
//    private ServiciosDepartamento serviciosDepartamento;
    private ServiciosDepartamento item;
//    private List<ServiciosDepartamento> serviciosDepartamentos;
    //private LazyModelWS<ServiciosDepartamentoItem> items;
    private List<ServiciosDepartamento> itemList;
    private LazyModelWS<ServiciosDepartamento> lazyServicios;
    private Boolean cargarItems;
    private List<Departamento> departamentos;
    private Departamento departamento;
    private String imagen;
    private String imagenItem;
    //VALIDAR SI CAMBIA DE ABREVIATURA AL SERVICIO
    private String abreviatura;
    private List<ServiciosDepartamentoRequisitos> serviciosRequisitoList;
    private TipoTramite tipoTramite;

    @PostConstruct
    public void init() {
        loadModel();
    }

    private void loadModel() {
        cargarItems = Boolean.FALSE;
        departamento = new Departamento();
        tipoTramite = appServices.findTipoTramite(new TipoTramite(Boolean.TRUE, "VU"));
        if (tipoTramite == null || tipoTramite.getId() == null) {
            JsfUti.messageError("", "", "No existe un trámite de ventanilla única");
        }
        initDepartamentos();
        initServiciosDepartamentos();
        initItem();
    }

    private void initDepartamentos() {
        departamentos = new ArrayList<>();
        if (session.getIsUserAdmin()) {
            departamentos = appServices.getListDepartamentosHijos();
        } else {
            departamento = appServices.buscarDepartamento(new Departamento(session.getDepts().get(0)));
            departamentos.add(departamento);
        }
    }

    private void initServiciosDepartamentos() {
        try {
            if (session.getIsUserAdmin()) {
                lazyServicios = new LazyModelWS<>(SisVars.ws + "serviciosDepartamentoItems/find?sort=departamento.id,asc", ServiciosDepartamento[].class, session.getToken());
            } else {
                lazyServicios = new LazyModelWS<>(SisVars.ws + "serviciosDepartamentoItems/find?departamento.id=" + departamento.getId() + "&sort=departamento.id,asc", ServiciosDepartamento[].class, session.getToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initItem() {
        item = new ServiciosDepartamento();
        item.setTipoTramite(tipoTramite);
        item.setDepartamento(null);
        item.setOnline(Boolean.FALSE);
        item.setValidar(Boolean.FALSE);
        item.setInterno(Boolean.TRUE);
        item.setExterno(Boolean.TRUE);
        item.setEstado(Boolean.TRUE);
        imagenItem = "";
        abreviatura = "";
    }

    public void loadItem(ServiciosDepartamento data) {
        item = data;
        abreviatura = item.getAbreviatura();
        JsfUti.executeJS("PF('dlgNuevoItem').show();");
        JsfUti.update("formNuevoItem");
    }

    public void cargarRequisitos(ServiciosDepartamento sd) {
        item = sd;
        serviciosRequisitoList = new ArrayList<>();
        ServiciosDepartamentoRequisitos req = new ServiciosDepartamentoRequisitos(new ServiciosDepartamento(item.getId()));
        serviciosRequisitoList = appServices.getDeptsItemsRequisito(req);
        JsfUti.executeJS("PF('dlgRequisitoItem').show();");
        JsfUti.executeJS("PF('dItems').clearFilters()");
        JsfUti.update("formRequisitos");
    }

    public void guardarItem() {
        if (item.getHora() != null) {
            if (item.getHora() < 1 && item.getHora() > 24) {
                return;
            }
        }
        if (item.getMinutos() != null) {
            if (item.getMinutos() < 1 && item.getMinutos() > 60) {
                return;
            }
        }
        if (!item.getInterno() && !item.getExterno()) {
            JsfUti.messageError(null, "Debe especificar si es un trámite interno o externo.", "");
            return;
        }

        if (Utils.isNotEmptyString(item.getNombre()) && Utils.isNotEmptyString(item.getAbreviatura())) {
            try {
                ServiciosDepartamento itemREST;
                if (existeAbreviatura()) {
                    return;
                }
                item.setNombre(item.getNombre().toUpperCase());
                item.setAbreviatura(item.getAbreviatura().toUpperCase());
                if (item.getId() == null) {
                    item.setUsuario(session.getName_user());
                    item.setFecha(new Date());
                    itemREST = (ServiciosDepartamento) service.methodPOST(item, SisVars.ws + "serviciosDepartamentoItems/guardar", ServiciosDepartamento.class);
                } else {
                    itemREST = (ServiciosDepartamento) service.methodPUT(item, SisVars.ws + "serviciosDepartamentoItems/actualizar", ServiciosDepartamento.class);
                }
                if (itemREST != null) {
                    JsfUti.messageInfo(null, "Datos actualizados", "");
                    JsfUti.executeJS("PF('dlgNuevoItem').hide();");
                    JsfUti.update("mainForm");
                    initServiciosDepartamentos();
                    initItem();
                } else {
                    JsfUti.messageError(null, "Intente nuevamente", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            JsfUti.messageError(null, "Debe ingresar una abreviatura y una descripción del item", "");
        }
    }

    public Boolean existeAbreviatura() {
        if (abreviatura != null && !abreviatura.isEmpty()) {
            if (!abreviatura.equals(item.getAbreviatura())) {
                return validarExistente();
            }
        } else {
            return validarExistente();
        }
        return Boolean.FALSE;
    }

    private Boolean validarExistente() {
        List<ServiciosDepartamento> listRest = (List<ServiciosDepartamento>) service.methodListGET(SisVars.ws + "serviciosDepartamentoItems/find?abreviatura=" + item.getAbreviatura(), ServiciosDepartamento[].class);
        if (!Utils.isEmpty(listRest)) {
            JsfUti.messageError(null, "Error", "Ya existe " + item.getAbreviatura() + ", debe ingresar otra abreviatura");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
//            serviciosDepartamento.setUrlImagen(SisVars.wsMedia + "resource/image/" + f.getName());
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUploadItem(FileUploadEvent event) {
        try {
            imagenItem = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
            item.setUrlImagen(SisVars.wsMedia + "resource/image/" + f.getName());
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void habilitarItem(ServiciosDepartamento sd, Boolean habilitar) {
        sd.setEstado(habilitar);
        ServiciosDepartamento sdDB = (ServiciosDepartamento) service.methodPUT(sd, SisVars.ws + "serviciosDepartamentoItems/actualizar", ServiciosDepartamento.class);
        if (sdDB != null && sdDB.getId() != null) {
            JsfUti.messageInfo(null, "Servicio: " + sdDB.getNombre() + (habilitar ? " habilitado" : " deshabilitado"), "");
        }
        initServiciosDepartamentos();
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public List<ServiciosDepartamentoRequisitos> getServiciosRequisitoList() {
        return serviciosRequisitoList;
    }

    public void setServiciosRequisitoList(List<ServiciosDepartamentoRequisitos> serviciosRequisitoList) {
        this.serviciosRequisitoList = serviciosRequisitoList;
    }

    public LazyModelWS<ServiciosDepartamento> getLazyServicios() {
        return lazyServicios;
    }

    public void setLazyServicios(LazyModelWS<ServiciosDepartamento> lazyServicios) {
        this.lazyServicios = lazyServicios;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagenItem() {
        return imagenItem;
    }

    public void setImagenItem(String imagenItem) {
        this.imagenItem = imagenItem;
    }

    public ServiciosDepartamento getItem() {
        return item;
    }

    public void setItem(ServiciosDepartamento item) {
        this.item = item;
    }

    public Boolean getCargarItems() {
        return cargarItems;
    }

    public void setCargarItems(Boolean cargarItems) {
        this.cargarItems = cargarItems;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<ServiciosDepartamento> getItemList() {
        return itemList;
    }

    public void setItemList(List<ServiciosDepartamento> itemList) {
        this.itemList = itemList;
    }
//</editor-fold>

}
