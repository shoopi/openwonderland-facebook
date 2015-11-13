/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.client.jme.renderer;

/**
 *
 * @author Shaya
 */

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.*;
import com.jme.scene.state.RenderState.StateType;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import org.jdesktop.mtgame.Entity;
import org.jdesktop.mtgame.RenderManager;
import org.jdesktop.mtgame.processor.WorkProcessor.WorkCommit;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.client.jme.SceneWorker;
import org.jdesktop.wonderland.client.jme.cellrenderer.BasicRenderer;
import uk.ac.essex.wonderland.modules.facebook.client.FacebookAccessToken;
import uk.ac.essex.wonderland.modules.facebook.client.FacebookCell;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookAccessTokenMessage;

public class FacebookCellRenderer extends BasicRenderer{
    
    private Node node = null;

    public FacebookCellRenderer(Cell cell) {
        super(cell);
    }
    
    private TriMesh getShapeMesh(String name, String shapeType){
        TriMesh mesh = null;
        if(shapeType != null && shapeType.equals("BOX") == true)
            mesh = new Box(name, new Vector3f(), 1, 1, 1);
        if (shapeType != null && shapeType.equals("SPHERE") == true)
            mesh = new Sphere(name, new Vector3f(), 12, 12, 1);
        else
            logger.warning("Unsupported Shape type " + cell.getLocalBounds().getClass().getName());
        return mesh;
    }
    
    protected Node createSceneGraph (Entity entity){
        String name = cell.getCellID().toString();
        String accessToken = ((FacebookCell)cell).getAccessToken();
        String shapeType = ((FacebookCell)cell).getShapeType();
        String textureURI = ((FacebookCell)cell).getTextureURI();
        
        if(accessToken == null)
        {
            final FacebookAccessToken accessTokenObj = new FacebookAccessToken();            
            accessTokenObj.addObserver(new Observer() {
                final String name = cell.getCellID().toString();
                public void update(Observable o, Object arg) {
                    FacebookAccessTokenMessage msg = new FacebookAccessTokenMessage(cell.getCellID(), accessTokenObj.getAccessToken());
                    cell.sendCellMessage(msg);
                }
            });
        }
        //if(accessToken.)
        {
            TriMesh mesh = this.getShapeMesh(name, shapeType);
            if(mesh == null){
                node = new Node();
                return node;
            }

            node = new Node();
            node.attachChild(mesh);
            node.setModelBound(new BoundingBox());
            node.updateModelBound();
            node.setName("Cell&#95;" + cell.getCellID() + ":" + cell.getName());

            if(textureURI != null){
                RenderManager rm = ClientContextJME.getWorldManager().getRenderManager();
                TextureState ts = (TextureState)rm.createRendererState(StateType.Texture);
                Texture t = null;
                try{
                    URL url = getAssetURL(textureURI);
                    t = TextureManager.loadTexture(url);
                    t.setWrap(Texture.WrapMode.MirroredRepeat);
                    t.setTranslation(new Vector3f());
                    ts.setTexture(t);
                    ts.setEnabled(true);
                } catch (MalformedURLException ex){
                    logger.warning("Could not found the proper texture in this URI");
                }
                node.setRenderState(ts);
            }
        }
        return node;
    }
    
    public void updateShape(){
        final String name = cell.getCellID().toString();
        final String shapeType = ((FacebookCell)cell).getShapeType();
        
        SceneWorker.addWorker(new WorkCommit() {
            public void commit(){
                node.detachAllChildren();
                node.attachChild(getShapeMesh(name, shapeType));
                node.setModelBound(new BoundingBox());
                node.updateModelBound();
                
                ClientContextJME.getWorldManager().addToUpdateList(node);
            }
        });
    }
}
