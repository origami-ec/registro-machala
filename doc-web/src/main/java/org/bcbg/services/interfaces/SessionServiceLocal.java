/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.services.interfaces;

import org.bcbg.session.UserSession;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface SessionServiceLocal {

    public Object getExternalContextObject(String key);

    //public UserSession getSessionContext();

}
