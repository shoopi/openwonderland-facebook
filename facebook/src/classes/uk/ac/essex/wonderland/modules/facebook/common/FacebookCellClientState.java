/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.common;

import org.jdesktop.wonderland.common.cell.state.CellClientState;


/**
 *
 * @author Shaya
 */

public class FacebookCellClientState extends CellClientState {
    
    private String shapeType = null;
    private String textureURI = null;

    public String getTextureURI() {
        return textureURI;
    }

    public void setTextureURI(String textureURI) {
        this.textureURI = textureURI;
    }

    public FacebookCellClientState() {
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }
    
}
