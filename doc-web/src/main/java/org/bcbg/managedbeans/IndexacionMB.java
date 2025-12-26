/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.TipoDato;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class IndexacionMB extends BpmManageBeanBaseRoot implements Serializable {

    private TipoDato tipoDato;
    private List<TipoDato> tiposDatos;
    private Indexacion indexacion;

    private Boolean activaGenerar;
    private List<IndexacionCampo> indicesPredeterminados;
    private List<IndexacionCampo> indicesFormulario;
    private String campo, formulario, categoria, id;
    private LazyModelWS<Indexacion> lazy;

    private List<String> categorias;
    private Boolean obligatorio;

    @PostConstruct
    public void init() {
        cargarVariables();
        indicesPredeterminados = documentalService.getIndicesPredeterminados();
    }

    public void cargarVariables() {
        id = null;
        activaGenerar = Boolean.TRUE;
        obligatorio = Boolean.FALSE;
        campo = "";
        formulario = "";
        categoria = "";
        indicesFormulario = new ArrayList<>();
        categorias = new ArrayList<>();
        tiposDatos = documentalService.getTipoDatos();
        lazy = new LazyModelWS<>(SisVars.wsDocs + "indexaciones?estado=true&sort=fecha,DESC", Indexacion[].class, session.getToken());
    }

    public void generarIndicesPredeterminados() {
        for (IndexacionCampo i : indicesPredeterminados) {
            indicesFormulario.add(i);
        }
        activaGenerar = Boolean.FALSE;
    }

    public void agregarCategoria() {
        if (Utils.isEmptyString(categoria)) {
            JsfUti.messageError(null, "Debe ingresar la categoria del campo para continuar", "");
            return;
        }
        if (Utils.isNotEmpty(categorias)) {
            for (String cat : categorias) {
                if (cat.equals(categoria)) {
                    JsfUti.messageError(null, "Ya existe una categoría con la misma descripción", "");
                    return;
                }
            }
        }
        categorias.add(categoria);
    }

    public void agregarCampoFormulario() {
        if (tipoDato == null) {
            JsfUti.messageError(null, "Debe ingresar el tipo de dato para continuar", "");
            return;
        }
        if (Utils.isEmptyString(campo)) {
            JsfUti.messageError(null, "Debe ingresar la descripción del campo para continuar", "");
            return;
        }
        if (tipoDato.getDescripcion().equals(Variables.tipoDatoCategorico)) {
            if (Utils.isEmpty(categorias)) {
                JsfUti.messageError(null, "Debe ingresar los datos de las categorías", "");
                return;
            }
            if (categorias.size() <= 1) {
                JsfUti.messageError(null, "Debe agregar más de una categoría", "");
                return;
            }
        }
        if (Utils.isNotEmpty(indicesFormulario)) {
            for (IndexacionCampo i : indicesFormulario) {
                if (i.getDescripcion().equals(campo)) {
                    JsfUti.messageError(null, "Ya existe un campo con la misma descripción", "");
                    return;
                }
            }
        }
        indicesFormulario.add(new IndexacionCampo(tipoDato.getDescripcion(), campo, categorias, obligatorio));
        categorias = new ArrayList<>();
        categoria = "";
        campo = "";
        obligatorio = Boolean.FALSE;
        tipoDato = null;
    }

    public void guardarIndice() {
        if (Utils.isEmptyString(formulario)) {
            JsfUti.messageError(null, "Debe ingresar la descripción del formulario para continuar", "");
            return;
        }
        if (Utils.isEmpty(indicesFormulario)) {
            JsfUti.messageError(null, "Debe ingresar los campos para la indexación", "");
            return;
        }
        indexacion = new Indexacion();
        indexacion.set_id(id);
        indexacion.setEstado(Boolean.TRUE);
        indexacion.setFecha(Utils.getDate(new Date()));
        indexacion.setDescripcion(formulario);
        indexacion.setCampos(indicesFormulario);
        Boolean guardar = Boolean.FALSE;
        if (Utils.isEmptyString(id)) {
            indexacion.setUsuario(session.getUsuarioDocs());
            Indexacion rest = documentalService.validarIndexacionFormulario(new Indexacion(formulario));
            if (rest == null) {
                guardar = Boolean.TRUE;
            }
        } else {
            indexacion.setUsuarioEdita(session.getUsuarioDocs());
            guardar = Boolean.TRUE;
        }

        if (guardar) {
            Indexacion rest = documentalService.guardarIndexacion(indexacion);
            if (rest != null) {
                cargarVariables();
                JsfUti.messageInfo(null, Messages.transaccionOK, "");
            } else {
                JsfUti.messageError(null, Messages.intenteNuevamente, "");
            }
        } else {
            JsfUti.messageError(null, Messages.existeIndexacionFormulario, "");
        }
    }

    public void eliminarIndiceFormulario(int index) {
        if (Utils.isNotEmpty(indicesFormulario)) {
            indicesFormulario.remove(index);
        }
    }

    public void editarIndexacion(Indexacion index) {
        formulario = index.getDescripcion();
        indicesFormulario = index.getCampos();
        id = index.get_id();
    }

    public void eliminarIndexacion(Indexacion index) {
        index.setEstado(Boolean.FALSE);
        index.setUsuarioEdita(session.getUsuarioDocs());
        Indexacion rest = documentalService.guardarIndexacion(index);
        if (rest != null) {
            cargarVariables();
            JsfUti.messageInfo(null, Messages.transaccionOK, "");
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public List<TipoDato> getTiposDatos() {
        return tiposDatos;
    }

    public void setTiposDatos(List<TipoDato> tiposDatos) {
        this.tiposDatos = tiposDatos;
    }

    public Indexacion getIndexacion() {
        return indexacion;
    }

    public void setIndexacion(Indexacion indexacion) {
        this.indexacion = indexacion;
    }

    public List<IndexacionCampo> getIndicesPredeterminados() {
        return indicesPredeterminados;
    }

    public void setIndicesPredeterminados(List<IndexacionCampo> indicesPredeterminados) {
        this.indicesPredeterminados = indicesPredeterminados;
    }

    public List<IndexacionCampo> getIndicesFormulario() {
        return indicesFormulario;
    }

    public void setIndicesFormulario(List<IndexacionCampo> indicesFormulario) {
        this.indicesFormulario = indicesFormulario;
    }

    public LazyModelWS<Indexacion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<Indexacion> lazy) {
        this.lazy = lazy;
    }

    public Boolean getActivaGenerar() {
        return activaGenerar;
    }

    public void setActivaGenerar(Boolean activaGenerar) {
        this.activaGenerar = activaGenerar;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getFormulario() {
        return formulario;
    }

    public void setFormulario(String formulario) {
        this.formulario = formulario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

}
