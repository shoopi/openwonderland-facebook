/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.common;

/**
 *
 * @author Shaya
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.cell.state.annotation.ServerState;
import org.jdesktop.wonderland.modules.poster.common.PosterCellServerState;

@XmlRootElement(name="shape-cell")
@ServerState
public class FacebookCellServerState extends CellServerState{
    
    @XmlElement(name="shape-type")
    private String shapeType = "BOX";
    
    @XmlElement(name="texture-uri")
    private String textureURI = null;
    
    private PosterCellServerState facebookPostsState;
    
    @XmlElement(name="facebookPostsState-cell")
    public PosterCellServerState getFacebookPostsState() {
        return facebookPostsState;
    }

    public void setFacebookPostsState(PosterCellServerState facebookPostsState) {
        this.facebookPostsState = facebookPostsState;
    }


    @XmlTransient
    public String getTextureURI() {
        return this.textureURI;
    }

    public void setTextureURI(String textureURI) {
        this.textureURI = textureURI;
    }
    
    public FacebookCellServerState(){
    }
    
    @XmlTransient
    public String getShapeType() {
        return this.shapeType;
    }
    
    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }
    
    @Override
    public String getServerClassName(){
        return "uk.ac.essex.wonderland.modules.facebook.server.FacebookCellMO";
    }
}

