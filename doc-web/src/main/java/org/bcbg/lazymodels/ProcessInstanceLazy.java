/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.lazymodels;

import org.bcbg.bpm.models.DetalleProceso;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.services.interfaces.BpmBaseEngine;
import org.bcbg.util.EjbsCaller;
import org.bcbg.ws.AppServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.bcbg.util.Utils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

/**
 *
 * @author AndySanchez
 */
public class ProcessInstanceLazy extends LazyDataModel<DetalleProceso> {

    @Inject
    private AppServices appServices;
    protected BpmBaseEngine engine;
    private DetalleProceso det;
    private List<DetalleProceso> ldet;
    private List<HistoricProcessInstance> lhpi;
    private boolean state = false;
    private ProcessDefinition p;
    private ProcessInstance pi;
    private HistoricoTramites ht;
    private int maxRows = 0;
    private HistoryService hs;
    private Map map;
    private String htCodigo;

    public ProcessInstanceLazy() {
        ldet = new ArrayList<>();
        engine = EjbsCaller.getEngine();
        maxRows = engine.getProcessInstanceHistoric(false).size();
    }

    public ProcessInstanceLazy(boolean state) {
        ldet = new ArrayList<>();
        engine = EjbsCaller.getEngine();

        this.state = state;
        maxRows = engine.getProcessInstanceHistoric(state).size();
    }

    public ProcessInstanceLazy(boolean state, AppServices appServices) {
        ldet = new ArrayList<>();
        engine = EjbsCaller.getEngine();
        this.state = state;
        maxRows = engine.getProcessInstanceHistoric(state).size();
        this.appServices = appServices;
    }

    private List getDefaultData(int first, int pageSize, Map<String, FilterMeta> filters) {
        try {
            ldet = new ArrayList<>();
            hs = engine.getProcessEngine().getHistoryService();
            if (filters.isEmpty()) {
                this.setRowCount(maxRows);
                lhpi = engine.getProcessInstanceHistoric(first, pageSize, state);
            } else {
                if (filters.containsKey("idProceso")) {
                    lhpi = hs.createHistoricProcessInstanceQuery().processInstanceId(getProceso(Long.parseLong(filters.get("idProceso").getFilterValue().toString())).getIdProceso()).unfinished().orderByProcessInstanceStartTime().desc().listPage(first, pageSize);
                }
                if (filters.containsKey("instancia")) {
                    lhpi = hs.createHistoricProcessInstanceQuery().processInstanceId(filters.get("instancia").getFilterValue().toString()).listPage(first, pageSize);
                }
                if (filters.containsKey("codigo")) {
                    htCodigo = (String) appServices.getIdProcessActiviti(filters.get("codigo").getFilterValue().toString());
                    lhpi = hs.createHistoricProcessInstanceQuery().processInstanceId(htCodigo).unfinished().orderByProcessInstanceStartTime().desc().listPage(first, pageSize);
                }
                this.setRowCount(lhpi.size());
            }
            for (HistoricProcessInstance hpi : lhpi) {
                p = engine.getProcessDataByDefID(hpi.getProcessDefinitionId());
                pi = engine.getProcessInstanceById(hpi.getId());
                det = new DetalleProceso();
                ht = appServices.historicoTramiteProcessId(hpi.getId());
                if (ht == null) {
                    ht = appServices.historicoTramiteProcessTemp(hpi.getId());
                }
                if (ht != null) {
                    det.setId(ht.getId());
                    if (p.getName() != null) {
                        det.setNombreProceso(p.getName() + " (" + ht.getId() + ")");
                    } else {
                        det.setNombreProceso(p.getKey() + " (" + ht.getId() + ")");
                    }
                    if (pi != null) {
                        det.setInstancia(pi.getId());
                    }
                    if (ht.getId() != null) {
                        det.setIdProceso(ht.getNumTramite().toString());
                        det.setCodigo(ht.getCodigo());
                    }

                    det.setFechaInicio(hpi.getStartTime());
                    det.setFechaFin(hpi.getEndTime());
                    det.setTasks(engine.getTaskByProcessInstanceId(hpi.getId()));
                    ldet.add(det);
                }
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(ProcessInstanceLazy.class.getName()).log(Level.SEVERE, null, e);
        }
        return ldet;
    }

    public HistoricoTramites getProceso(Long id) {
        ht = appServices.buscarHistoricoTramite(new HistoricoTramites(null, id));
        return ht;
    }

    @Override
    public List<DetalleProceso> load(int i, int i1, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
        return getDefaultData(i, i1, map1);
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        if (ldet == null) {
            return 0;
        }
        return (int) ldet.stream()
                .filter(o -> Utils.filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
    }
}
