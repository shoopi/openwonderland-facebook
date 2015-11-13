/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.common;

import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;

/**
 *
 * @author Shaya
 */
public class FacebookAccessTokenMessage extends CellMessage{
    
    private String accessToken = null;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public FacebookAccessTokenMessage(CellID cellID, String accessToken) {
        super(cellID);
        this.accessToken = accessToken;
    }
    
    
}
