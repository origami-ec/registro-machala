package com.origami.sgr.services.ejbs;

import com.origami.sgr.services.interfaces.DatabaseLocal;
import com.origami.sgr.util.DataBaseConfigs;
import com.origami.sgr.util.Utils;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.sql.DataSource;

/**
 * @author origami
 */
@Singleton(name = "dataSource")
public class DataSourceEjb implements DatabaseLocal {

    private DataBaseConfigs dbc = null;
    private DataSource ds = null;
    //private DataSource mds = null;
    private DataSource docs = null;

    @PostConstruct
    protected void init() {
        dbc = new DataBaseConfigs();
        ds = dbc.getDataSource(1);
        //mds = dbc.getDataSource(2);
        docs = dbc.getDataSource(3);
    }

    @Override
    public DataSource getDataSource() {
        return ds;
    }

    /*@Override
    public DataSource getMsqlDataSource() {
        return mds;
    }*/

    /**
     *
     * @return
     */
    @Override
    public DataSource getDocs() {
        return docs;
    }

    @Override
    public DataSource getDocs(Date fechaInscripcion) {
        return dbc.getDataSource(Utils.getAnio(fechaInscripcion));
    }

}
