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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.entities.CtlgCatalogo;
import org.bcbg.entities.CtlgItem;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.User;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.Data;
import org.bcbg.session.UserSession;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class DepartamentoMB implements Serializable {

    @Inject
    protected UserSession us;
    @Inject
    private BcbgService service;
    @Inject
    private AppServices appServices;

    private LazyModelWS<Departamento> depts;
    private Departamento dept;
    private String imagen;
    protected TreeNode root;
    private List<CtlgItem> listaCatalogoItemTipos;
    private CtlgItem tipoCatalogo;
    private CtlgCatalogo catalogo;
    private List<Departamento> listDepartamentos;

    @PostConstruct
    protected void iniView() {
        inicializarVariables();
        listaCatalogoItemTipos = new ArrayList<>();
        catalogo = new CtlgCatalogo();
        listDepartamentos = new ArrayList<>();
        listDepartamentos = service.methodListGET(SisVars.ws + "departamento/padres", Departamento[].class);
    }

    public TreeNode initDepartamento(Departamento mod) {
        System.out.println("Ingresa al metodo");
        if (mod.getNodeMenus() == null) {
            System.out.println("Ingresa al metodoa a crear los nodos");
            return createNode(mod);
        } else {
            return mod.getNodeMenus();
        }
    }

    public TreeNode createNode(Departamento mod) {
        System.out.println("treenodo  Ingresa al metodo");
        TreeNode nodeMenus = new DefaultTreeNode("Departamento Hijo " + mod.getNombre(), mod, null);
        fillSons(nodeMenus, mod);
        mod.setNodeMenus(nodeMenus);
        return nodeMenus;
    }

    public void fillSons(TreeNode node, Departamento departamento) {
        TreeNode temp;
        List<Departamento> list = service.methodListGET(SisVars.ws + "departamento/hijos/" + departamento.getId(), Departamento[].class);
        if (!list.isEmpty()) {
            System.out.println("no esta vacio los hijos");
            for (Departamento arc : list) {
                temp = new DefaultTreeNode(arc, node);
                this.fillSons(temp, arc);
            }
        }
    }

    public void cargandoNodos(Departamento dept) {
        root = new DefaultTreeNode();
        root = appServices.getTreeNodesDeparamento(dept);
    }

    public void selectUsuario(User u) {
        PrimeFaces.current().dialog().closeDynamic(u);
    }

    public void inicializarVariables() {

        dept = new Departamento();
    }

    public void showDlgDept() {
        dept = new Departamento();
        //tipoUnidades = new ArrayList<>();
        //tipoUnidades = appServices.getTiposUnidades("tipo_unidad");
        JsfUti.update("formDepts");
        JsfUti.executeJS("PF('dlgDepts').show();");
    }

    public void showDlgEditDept(Departamento de) {
        dept = new Departamento();
        dept = de;
        //tipoUnidades = new ArrayList<>();
        //tipoUnidades = appServices.getTiposUnidades("tipo_unidad");
        JsfUti.update("formDepts");
        JsfUti.executeJS("PF('dlgDepts').show();");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
//            dept.setUrlImagen(SisVars.wsMedia + "resource/image/" + f.getName());
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirdlogoCatalogo() {
        tipoCatalogo = new CtlgItem();
        catalogo = new CtlgCatalogo();
        catalogo = appServices.finByCatalogo("tipo_unidad");
        listaCatalogoItemTipos = appServices.finByCatalogoItem("tipo_unidad");
        JsfUti.update("formTiposUnidades");
        JsfUti.executeJS("PF('dlgTipoUnidades').show();");
    }

    public void cargarEdicion(CtlgItem c) {
        tipoCatalogo = new CtlgItem();
        tipoCatalogo = c;
        JsfUti.update("formTiposUnidades");
    }

    public void aniadirDeptHijo(Departamento de) {
        dept = new Departamento();
        dept.setPadre(de);
        listDepartamentos = service.methodListGET(SisVars.ws + "departamento/padres", Departamento[].class);
        //tipoUnidades = new ArrayList<>();
        //tipoUnidades = appServices.getTiposUnidades("tipo_unidad");
        JsfUti.update("formDepts");
        JsfUti.executeJS("PF('dlgDepts').show();");
    }

    public void registrarTipoDepartamento() {

        if (tipoCatalogo.getCodename().isEmpty() || tipoCatalogo.getEstado().isEmpty() || tipoCatalogo.getValor().isEmpty()) {
            JsfUti.messageWarning("", "Los Campos no puede estar vacios", "");
            return;
        }
        tipoCatalogo.setCatalogo(catalogo);
        tipoCatalogo = appServices.guardarItme(tipoCatalogo);
        JsfUti.messageInfo(null, "Su registro se realizó correctamente ", "");
        tipoCatalogo = new CtlgItem();
        JsfUti.update("formTiposUnidades");
        listaCatalogoItemTipos = new ArrayList<>();
        listaCatalogoItemTipos = appServices.finByCatalogoItem("tipo_unidad");

    }

    public void redirectPage() {
        JsfUti.redirect(JsfUti.getHostContextUrl() + "/admin/manage/departamento.xhtml");
    }

    public void sincronizarDepartamentos() {
        Data rest = appServices.actualizarDepartamentos();
        if (rest != null) {
            JsfUti.messageInfo(null, Messages.correcto, "");
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public LazyModelWS<Departamento> getDepts() {
        return depts;
    }

    public void setDepts(LazyModelWS<Departamento> depts) {
        this.depts = depts;
    }

    public Departamento getDept() {
        return dept;
    }

    public void setDept(Departamento dept) {
        this.dept = dept;
    }

    public List<CtlgItem> getListaCatalogoItemTipos() {
        return listaCatalogoItemTipos;
    }

    public void setListaCatalogoItemTipos(List<CtlgItem> listaCatalogoItemTipos) {
        this.listaCatalogoItemTipos = listaCatalogoItemTipos;
    }

    public CtlgCatalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CtlgCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    public CtlgItem getTipoCatalogo() {
        return tipoCatalogo;
    }

    public void setTipoCatalogo(CtlgItem tipoCatalogo) {
        this.tipoCatalogo = tipoCatalogo;
    }

    public List<Departamento> getListDepartamentos() {
        return listDepartamentos;
    }

    public void setListDepartamentos(List<Departamento> listDepartamentos) {
        this.listDepartamentos = listDepartamentos;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

//</editor-fold>
}
