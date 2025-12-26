package org.bcbg.util;

import org.bcbg.config.SisVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author AndySanchez
 */
public class DataBaseConfigs {

    private String path;
    private BasicDataSource ds = null;
    private DataBaseParameters dbp = null;

    public DataBaseConfigs(String path) {
        this.path = path;
    }

    public DataBaseConfigs() {

    }

    private DataBaseParameters getParameters(int tcon) {
        DataBaseParameters p;
        try {
            p = new DataBaseParameters();
            switch (tcon) {

                case 3:
                    p.setDriverClass(SisVars.docDriverClass);
                    p.setUserName(SisVars.docUserName);
                    p.setPassword(SisVars.docPassword);
                    p.setUrl(SisVars.docUrl);
                    break;
                default:
                   
                    break;
            }
        } catch (Exception e) {
            p = null;
            Logger.getLogger(DataBaseConfigs.class.getName()).log(Level.SEVERE, null, e);
        }
        return p;
    }

    public DataSource getDataSource(int p) {
        try {
            dbp = this.getParameters(p);
            if (dbp != null) {
                ds = new BasicDataSource();
                ds.setUrl(dbp.getUrl());
                ds.setDriverClassName(dbp.getDriverClass());
                ds.setUsername(dbp.getUserName());
                ds.setPassword(dbp.getPassword());
                ds.setMaxIdle(dbp.getMaxIdleTime());
                ds.setMaxActive(dbp.getMaxPoolSize());
                ds.setMinIdle(dbp.getMinPoolSize());
                ds.setDefaultAutoCommit(false);
            }
        } catch (Exception e) {
            Logger.getLogger(DataBaseConfigs.class.getName()).log(Level.SEVERE, null, e);
        }
        return ds;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
