/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.lazymodels;

import org.bcbg.bpm.models.TareaWF;
import org.bcbg.util.EjbsCaller;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.math.NumberUtils;
import org.bcbg.util.Utils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author Anyelo
 */
public class TareasWFLazy extends LazyDataModel<TareaWF> {

    private String taskDefKey;
    private String usuario = "admin";
    private List<TareaWF> tareas;

    public TareasWFLazy() {

    }

    public TareasWFLazy(String user) {
        this.usuario = user;
    }

    public TareasWFLazy(String user, String taskDefKey) {
        this.usuario = user;
        this.taskDefKey = taskDefKey;

    }

    private List getDefaultDataView(int first, int pageSize, Map<String, FilterMeta> filters) {
        try {
            Object[] parametros = new Object[]{usuario, usuario};
            String order = "ORDER BY fecha_entrega ASC ";
            StringBuilder queryCount = new StringBuilder("SELECT CAST(COUNT(*) AS INTEGER) FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
//            StringBuilder query = new StringBuilder("SELECT id, num_tramite_sd \"numTramiteSd\",  num_tramite \"numTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
//            query.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_propietario \"nombrePropietario\", ");
//            query.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate ");
//            query.append("FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
            StringBuilder query = new StringBuilder("SELECT id, num_tramite \"numTramite\",codigo \"codigoTramite\", id_proceso_temp \"idProcesoTemp\", entregado, ");
            query.append("fecha_ingreso \"fechaIngreso\", fecha_entrega \"fechaEntrega\", nombre_solicitante \"nombreSolicitante\", ");
            query.append("blocked, task_id \"taskId\", form_key \"formKey\", priority, name_ \"name\", candidate");
            query.append("FROM flow.tareas_activas WHERE (assignee = ? OR candidate ~ ?) ");
            tareas = new ArrayList<>();
            if (filters.isEmpty()) {
                //this.setRowCount(maxRows);
                if (taskDefKey == null) {
                    parametros = new Object[]{usuario, usuario};
                } else {
                    query.append("AND task_def_key = ? ");
                    queryCount.append("AND task_def_key = ? ");
                    parametros = new Object[]{usuario, usuario, taskDefKey};
                }
                //this.setRowCount((Integer) manager.getNativeQuery(queryCount.toString(), parametros));
            } else {
                if (filters.containsKey("idTramite") && NumberUtils.isDigits(filters.get("idTramite").toString().trim())) {
                    query.append("AND num_tramite = ? ");
                    parametros = new Object[]{usuario, usuario, new BigInteger(filters.get("idTramite").toString().trim())};
                }
                if (filters.containsKey("idTramiteSd")
                        && NumberUtils.isDigits(filters.get("idTramiteSd").toString().trim())) {
                    query.append("AND num_tramite_sd = ? ");
                    parametros = new Object[]{usuario, usuario,
                        new BigInteger(filters.get("idTramiteSd").toString().trim())};
                }
            }
            query.append(order);
            //tareas = manager.findNativeQueryFirstAndMaxResult(TareaWF.class, query.toString(), parametros, first, pageSize);
            if (!filters.isEmpty()) {
                this.setRowCount(tareas == null ? 0 : tareas.size());
            }
        } catch (Exception e) {
            Logger.getLogger(TareasWFLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return tareas;
    }

    @Override
    public List<TareaWF> load(int i, int i1, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
        return getDefaultDataView(i, i1, map1);
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        if (tareas == null) {
            return 0;
        }
        return (int) tareas.stream()
                .filter(o -> Utils.filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
    }
}
