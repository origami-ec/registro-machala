/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import java.util.Date;
import javax.ejb.Local;
import javax.sql.DataSource;

/**
 *
 * @author origami
 */
@Local
public interface DatabaseLocal {

    public DataSource getDataSource();

    //public DataSource getMsqlDataSource();

    public DataSource getDocs();

    public DataSource getDocs(Date fechaInscripcion);

}
