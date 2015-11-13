/*-
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.client;

/**
 *
 * @author Shaya
 */

import java.util.Observable;
import java.util.Observer;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.Cell.RendererType;
import org.jdesktop.wonderland.client.cell.CellCache;
import org.jdesktop.wonderland.client.cell.CellRenderer;
import org.jdesktop.wonderland.client.cell.ChannelComponent;
import org.jdesktop.wonderland.client.cell.ChannelComponent.ComponentMessageReceiver;
import org.jdesktop.wonderland.client.input.Event;
import org.jdesktop.wonderland.client.input.EventClassListener;
import org.jdesktop.wonderland.client.jme.input.MouseButtonEvent3D;
import org.jdesktop.wonderland.client.jme.input.MouseEvent3D.ButtonId;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import uk.ac.essex.wonderland.modules.facebook.client.jme.renderer.FacebookCellRenderer;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookAccessTokenMessage;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookCellClientState;


public class FacebookCell extends Cell {
    
    private String shapeType = null;
    private String textureURI = null;
    private FacebookCellRenderer renderer = null;
    private MouseEventListener listener = null;
    private String accessToken = null;

    public FacebookCell(CellID cellID, CellCache cellCache) {
        super(cellID, cellCache);
    }
    
    @Override
    public void setClientState(CellClientState state){
        super.setClientState(state);
        this.shapeType = ((FacebookCellClientState)state).getShapeType();
        this.textureURI = ((FacebookCellClientState)state).getTextureURI();
    }
    
    public String getShapeType(){
        return this.shapeType;
    }
    
    public String getTextureURI() {
        return textureURI;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    @Override
    protected CellRenderer createCellRenderer(RendererType rendererType) {
        
        if (rendererType == RendererType.RENDERER_JME) 
        {
            this.renderer = new FacebookCellRenderer(this);
            return this.renderer;
        }
        else
        {
            return super.createCellRenderer(rendererType);
        }
    }
    
    @Override
    public void setStatus(CellStatus status, boolean increasing){
        super.setStatus(status, increasing);
        
        /* Looping back from the server to the client */
        ChannelComponent channel = getComponent(ChannelComponent.class);
        
        if(status == CellStatus.INACTIVE && increasing == false){
            if(listener != null){
                listener.removeFromEntity(renderer.getEntity());
                listener = null;
                
                // it must be placed whitin the if clause
                channel.addMessageReceiver(FacebookAccessTokenMessage.class, new FacebookAccessTokenMessageReceiver());
            }
        }
        else if (status == CellStatus.RENDERING && increasing == true){
            if(listener == null){
                listener = new MouseEventListener();
                listener.addToEntity(renderer.getEntity());
                
                // in the if clause of DISK
                channel.removeMessageReceiver(FacebookAccessTokenMessage.class);
        
            }
        }
    }

    class MouseEventListener extends EventClassListener{
        @Override
        public Class[] eventClassesToConsume(){
            return new Class[] { MouseButtonEvent3D.class};
        }

        @Override
        public void commitEvent(Event event){
            MouseButtonEvent3D mbe = (MouseButtonEvent3D)event;
            if(mbe.isClicked() == false || mbe.getButton() != ButtonId.BUTTON1){
                return;
            }         
            shapeType = ((shapeType).equals("BOX") == true )? "SPHERE" : "BOX";
            renderer.updateShape();
        }
    }
    
    
    class FacebookAccessTokenMessageReceiver implements ComponentMessageReceiver {
        public void messageReceived(CellMessage message) {
            
            FacebookAccessTokenMessage accessTokenMsg = (FacebookAccessTokenMessage)message;
            accessToken = accessTokenMsg.getAccessToken();
                System.out.println("in return accesstoken is: " + accessToken);
                
            if (!accessTokenMsg.getSenderID().equals(getCellCache().getSession().getID())) {
                
                //It needs to converted to the string
                accessToken = accessTokenMsg.getAccessToken();
                System.out.println("in return accesstoken is: " + accessToken);
                //renderer.updateShape();
                
                //renderer.updateAccessToken()?
            }
        }
    }
}