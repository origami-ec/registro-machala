package org.bcbg.services.ejbs;

import org.bcbg.services.interfaces.DatabaseLocal;
import org.bcbg.util.DataBaseConfigs;
import org.bcbg.util.Utils;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.sql.DataSource;

/**
 * @author AndySanchez
 */
@Singleton(name = "dataSource")
public class DataSourceEjb implements DatabaseLocal {

    private DataBaseConfigs dbc = null;
    private DataSource ds = null;
    private DataSource mds = null;
    private DataSource docs = null;

    @PostConstruct
    protected void init() {
        dbc = new DataBaseConfigs();
        ds = dbc.getDataSource(1);
        mds = dbc.getDataSource(2);
        docs = dbc.getDataSource(3);
    }

    @Override
    public DataSource getDataSource() {
        return ds;
    }

    @Override
    public DataSource getMsqlDataSource() {
        return mds;
    }

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
