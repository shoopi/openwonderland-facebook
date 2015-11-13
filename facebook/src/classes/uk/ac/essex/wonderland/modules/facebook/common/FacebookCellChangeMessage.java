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
public class FacebookCellChangeMessage extends CellMessage{
    
    private String shapeType = null;

    public FacebookCellChangeMessage(CellID cellID, String shapeType){
        super(cellID);
        this.shapeType = shapeType;
    }
    
    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }
    
    
}
