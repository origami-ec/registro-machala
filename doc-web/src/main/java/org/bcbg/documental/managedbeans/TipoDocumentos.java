/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.session.UserSession;
import org.bcbg.documental.models.TipoDato;
import org.bcbg.documental.models.TipoDocumento;
import org.bcbg.documental.models.TipoDocumentoDetalle;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class TipoDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TipoDocumentos.class.getName());

    @Inject
    private UserSession us;
    @Inject
    private BcbgService irs;

    protected TipoDocumento tipo;
    protected TipoDocumentoDetalle tdd;
    protected List<TipoDocumentoDetalle> list;
    protected LazyModelWS<TipoDocumento> lazy;
    protected List<TipoDato> datosSimple;
    protected List<TipoDato> datosLista;
    protected List<TipoDocumento> documentos;

    @PostConstruct
    public void initView() {
        tdd = new TipoDocumentoDetalle();
        lazy = new LazyModelWS<>(SisVars.ws + "tipoDocumento/lazy", TipoDocumento[].class, us.getToken());
        datosSimple = irs.methodListGET(SisVars.ws + "tipoDato/datoSimple", TipoDato[].class);
        datosLista = irs.methodListGET(SisVars.ws + "tipoDato/datoLista", TipoDato[].class);
        documentos = irs.methodListGET(SisVars.ws + "tipoDocumento/lista", TipoDocumento[].class);
    }

    public void showDlgNew() {
        tipo = new TipoDocumento();
        JsfUti.update("formNewTipo");
        JsfUti.executeJS("PF('dlgNewTipo').show();");
    }

    public void saveTipoDoc() {
        try {
            tipo.setUsuario(us.getName_user());
            tipo.setFecha(new Date());
            tipo = (TipoDocumento) irs.methodPOST(tipo, SisVars.ws + "tipoDocumento/guardar", TipoDocumento.class);
            lazy = new LazyModelWS<>(SisVars.ws + "/tipoDocumento/lazy", TipoDocumento[].class, us.getToken());
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgNewTipo').hide();");
            JsfUti.messageInfo(null, "Transacción exitosa.", "Tipo de documento agregado.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar nuevo tipo documento.", e);
        }
    }

    public void showDlgEdit(TipoDocumento td) {
        tipo = td;
        tdd = new TipoDocumentoDetalle();
        list = irs.methodListGET(SisVars.ws + "tipoDocumentoDetalle/tipoDocumento/" + tipo.getId(), TipoDocumentoDetalle[].class);
        JsfUti.update("formEditTipo");
        JsfUti.executeJS("PF('dlgEditTipo').show();");
    }

    public void agregarDetalle() {
        try {
            if (tipo.getId() != null) {
                tdd.setTipoDocumento(tipo);
            }
            tdd.setUsuario(us.getName_user());
            tdd.setFecha(new Date());
            irs.methodPOST(tdd, SisVars.ws + "tipoDocumentoDetalle/guardar", TipoDocumentoDetalle.class);
            tdd = new TipoDocumentoDetalle();
            //list = irs.methodListGET(SisVars.ws + "tipoDocumentoDetalle/documento/" + tipo.getId(), TipoDocumentoDetalle[].class);
            list = irs.methodListGET(SisVars.ws + "tipoDocumentoDetalle/tipoDocumento/" + tipo.getId(), TipoDocumentoDetalle[].class);
            JsfUti.update("formEditTipo");
            JsfUti.messageInfo(null, "Transacción exitosa.", "Detalle agregado al documento.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Agregar detalle a documento.", e);
        }
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumento tipo) {
        this.tipo = tipo;
    }

    public TipoDocumentoDetalle getTdd() {
        return tdd;
    }

    public void setTdd(TipoDocumentoDetalle tdd) {
        this.tdd = tdd;
    }

    public List<TipoDocumentoDetalle> getList() {
        return list;
    }

    public void setList(List<TipoDocumentoDetalle> list) {
        this.list = list;
    }

    public List<TipoDato> getDatosSimple() {
        return datosSimple;
    }

    public void setDatosSimple(List<TipoDato> datosSimple) {
        this.datosSimple = datosSimple;
    }

    public List<TipoDato> getDatosLista() {
        return datosLista;
    }

    public void setDatosLista(List<TipoDato> datosLista) {
        this.datosLista = datosLista;
    }

    public List<TipoDocumento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<TipoDocumento> documentos) {
        this.documentos = documentos;
    }

    public LazyModelWS<TipoDocumento> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<TipoDocumento> lazy) {
        this.lazy = lazy;
    }

}
