/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.client;

/**
 *
 * @author Shaya
 */

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import org.jdesktop.wonderland.client.cell.registry.annotation.CellFactory;
import org.jdesktop.wonderland.client.cell.registry.spi.CellFactorySPI;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.modules.poster.common.PosterCellServerState;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookCellServerState;

@CellFactory
public class FacebookCellFactory implements CellFactorySPI {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("uk/ac/essex/wonderland/modules/facebook/client/resources/Bundle");

    public String[] getExtensions(){
        return new String[] {};
    }
    
    @SuppressWarnings("unchecked")
    public <T extends CellServerState> T getDefaultCellServerState(Properties props){
        FacebookCellServerState state = new FacebookCellServerState();
        state.setTextureURI("wla://facebook/fb.png");
        
        state.setName("state.setName test String");
        
        //Create poster to hold the simulation description and links
        PosterCellServerState facebookPostState = new PosterCellServerState();
        //facebookPostState.setName("facebook Cell");
        state.setFacebookPostsState(facebookPostState);
        
        return (T) state;
    }
    
    public String getDisplayName(){
        return bundle.getString("FACEBOOK");
        //return "Facebook";
    }
    
    public Image getPreviewImage()
    {
        try{
            URL url = FacebookCellFactory.class.getResource("resources/Facebook-logo.png");
            return Toolkit.getDefaultToolkit().createImage(url);
        }
        catch(NullPointerException ex)
        {
            System.out.println("Icon Not found!");
            return null;
        }
    }
}
