/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.util;

import org.bcbg.config.SisVars;
import org.bcbg.models.ProcessDef;
import org.bcbg.services.interfaces.BpmBaseEngine;
import org.bcbg.services.interfaces.DatabaseLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.activiti.engine.repository.ProcessDefinition;

/**
 *
 * @author AndySanchez
 */
public class EjbsCaller {

    private static BpmBaseEngine engine = null;
    private static DatabaseLocal dataSocurce = null;

    
    public static BpmBaseEngine getEngine() {
        try {
            engine = (BpmBaseEngine) new InitialContext().lookup(SisVars.bpmBaseEngine);
        } catch (Exception e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        }
        return engine;
    }

    public static ProcessDef getProcessDef(String key) {
        ProcessDef pd = null;
        try {
            ProcessDefinition p = getEngine().getProcessDefinitionByKey(key);
            if (p != null) {
                pd = new ProcessDef();
                pd.setId(p.getId());
                pd.setKey(p.getKey());
                pd.setName(p.getName());
                pd.setDescription(p.getDescription());
                pd.setDeploymentId(p.getDeploymentId());
                pd.setVersion(p.getVersion());
                pd.setDiagramResourceName(p.getDiagramResourceName());
                pd.setResourceName(p.getResourceName());
                pd.setSuspended(p.isSuspended());
                pd.setHasStartFormKey(p.hasStartFormKey());
            }
        } catch (Exception e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        }
        return pd;
    }

    public static DataSource getDataSource() {
        try {
            dataSocurce = (DatabaseLocal) new InitialContext().lookup(SisVars.datasource);
        } catch (Exception e) {
            Logger.getLogger(EjbsCaller.class.getName()).log(Level.SEVERE, null, e);
        }
        return dataSocurce.getDataSource();
    }

}
