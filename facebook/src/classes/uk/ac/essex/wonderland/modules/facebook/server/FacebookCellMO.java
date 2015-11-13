/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.essex.wonderland.modules.facebook.server;

/**
 *
 * @author Shaya
 */

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Comment;
import com.restfb.types.Post;
import com.restfb.types.User;
import com.sun.sgs.app.*;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellTransform;
import org.jdesktop.wonderland.common.cell.ClientCapabilities;
import org.jdesktop.wonderland.common.cell.MultipleParentException;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.cell.state.PositionComponentServerState;
import org.jdesktop.wonderland.modules.poster.common.PosterCellServerState;
import org.jdesktop.wonderland.modules.poster.server.PosterCellMO;
import org.jdesktop.wonderland.server.cell.*;
import org.jdesktop.wonderland.server.comms.WonderlandClientID;
import org.jdesktop.wonderland.server.comms.WonderlandClientSender;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookAccessTokenMessage;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookCellClientState;
import uk.ac.essex.wonderland.modules.facebook.common.FacebookCellServerState;

public class FacebookCellMO extends CellMO{

    private String shapeType = null;
    private String textureURI = null;
    private CellID facebookPostCellID;
    private String accessToken = null;
    static boolean checked = false;
    private PeriodicTaskHandle queryTaskHandle = null;
    
    public FacebookCellMO() { }
    
    @Override
    public String getClientCellClassName(WonderlandClientID clientID, ClientCapabilities capabilities){
        return "uk.ac.essex.wonderland.modules.facebook.client.FacebookCell";
    }
    
    @Override
    public void setServerState(CellServerState state){
        super.setServerState(state);
        this.shapeType = ((FacebookCellServerState)state).getShapeType();
        this.textureURI = ((FacebookCellServerState)state).getTextureURI();
        /*
        FacebookCellServerState fbServerState = (FacebookCellServerState)state;
        if (fbServerState.getFacebookPostsState()!= null ){
            PosterCellMO posterCell = addFacebookPostCell(fbServerState.getFacebookPostsState());
            facebookPostCellID = posterCell.getCellID();
        }
        * 
        */
    }
    
    @Override
    public CellServerState getServerState(CellServerState state){
        if(state == null){
            state = new FacebookCellServerState();
        }
        ((FacebookCellServerState)state).setShapeType(shapeType);
        ((FacebookCellServerState)state).setTextureURI(textureURI);
        return super.getServerState(state);
    }
    
    @Override
    public CellClientState getClientState(CellClientState cellClientState, WonderlandClientID clientID, 
    ClientCapabilities capabilities){
        if(cellClientState == null)
            cellClientState = new FacebookCellClientState();
        ((FacebookCellClientState)cellClientState).setShapeType(shapeType);
        ((FacebookCellClientState)cellClientState).setTextureURI(textureURI);
        return super.getClientState(cellClientState, clientID, capabilities);
    }
    
    public void addPostIntoWorld(String str, int xAxis,int yAxis,int zAxis){
        PosterCellServerState setup = new PosterCellServerState();
        setup.setName("Facebook Posts");
        
        float angleDegrees = 180;
        float angleRadians = (float) Math.toRadians(angleDegrees);
        Quaternion quat = new Quaternion().fromAngleAxis(angleRadians, new Vector3f(0,1,0));
        CellTransform transform = new CellTransform(quat, new Vector3f(xAxis + 1, yAxis, zAxis+1));
        
        PosterCellMO posterCellMO = (PosterCellMO) addChild(setup, transform);//see below for addChild() method

        //CellManagerMO.getCellManager().insertCellInWorld(cellmo);
        posterCellMO.setPosterText(str);
    }

    private CellMO addChild(CellServerState setup, CellTransform transform) {
        
        //Set the position
        // Create a position component that will set the initial origin
        PositionComponentServerState position = (PositionComponentServerState)
                setup.getComponentServerState(PositionComponentServerState.class);
        if (position == null) {
            position = new PositionComponentServerState();
            setup.addComponentServerState(position);
        }
        position.setTranslation(transform.getTranslation(null));
        position.setRotation(transform.getRotation(null));
        position.setScaling(transform.getScaling(null));
        
        // fetch the server-side cell class name and create the cell
        String className = setup.getServerClassName();
        
        CellMO cellMO = CellMOFactory.loadCellMO(className);
        
        
        // call the cell's setup method
        try {
            cellMO.setServerState(setup);
            
            CellManagerMO.getCellManager().insertCellInWorld(cellMO);
            //addChild(cellMO);
            
        } catch (ClassCastException cce) {
            logger.log(Level.WARNING, "Error setting up new cell "
                    + cellMO.getName() + " of type "
                    + cellMO.getClass() + ", it does not implement "
                    + "BeanSetupMO.", cce);
            return null;
        } catch (MultipleParentException excp) {
            logger.log(Level.WARNING, "Error adding new cell " + cellMO.getName()
                    + " of type " + cellMO.getClass() + ", has multiple parents", excp);
        }
        return cellMO;
    }
        
    private String reshapeText(String userID, String userName,String post, String pictureLink, Vector<String> cmnts) {
        StringBuilder builder = new StringBuilder("<!DOCTYPE html> <html>"); 
        builder.append("<body style=\"width:400px; max-height:500px; font-size:10px; font-family:tahoma; color:#333333; margin:10px; margin-bottom:30px; border:2px dashed #334455; padding: 10 px;\" " + 
                "background=\"http://www.dewittindustries.com/cgibin/ibrowse/images/of_12/backgrounds/object-material/textile/7971.jpg\" >"); 
        builder.append("<div id=\"postSection\" style=\"font-weight:bold; margin:5px; padding: 5px; border-bottom:3px groove gray;\">");
        builder.append("<span id=\"user\" style=\" color: #3b5998; outline-style: none; text-decoration: none; font-weight: bold; \" >");
        builder.append("<a href=\"http://facebook.com/" + userID + "/" + "\" " + ">"); 
        builder.append("<img alt=\"" + userName + "\" src=\"http://graph.facebook.com/" + userID + "/picture" + "\" " + "/>" + userName +"</a></span>");
        builder.append("<span id=\"postText\" style=\"overflow:scroll\">" + post + "</span>");
        if(pictureLink != null) 
            builder.append("<p><img alt=\"picture\" src=\"" + pictureLink + "\" /></p>");
        builder.append("</div>");
        if(cmnts != null) {
            builder.append("<div id=\"comments\" style=\" color: #3b5998; margin-left:20px; margin-right:20px; border: 3px groove white;\">");
            for(int i = 0; i < cmnts.size(); i++) {
                builder.append("<div style=\"border-top:1px groove white;\"><span>");
                builder.append(cmnts.elementAt(i));
                builder.append("</span></div>");
            }
            builder.append("</div>");
        }
        builder.append("</body>");
        builder.append("</html>");
        String posterText = builder.toString();
        return posterText;
    }
    
    private String commentReshape(String userID,String userName, String cmnt) {
        StringBuilder builder = new StringBuilder(); 
        builder.append("<span id=\"user\" style=\" color: #3b5998; outline-style: none; text-decoration: none; font-weight: bold; \" >");
        builder.append("<a href=\"http://facebook.com/" + userID + "/" + "\" " + ">"); 
        builder.append("<img alt=\"" + userName + "\" src=\"http://graph.facebook.com/" + userID + "/picture" + "\" " + "/>" + userName +"</a></span>");
        builder.append("<span id=\"postText\" style=\"overflow:scroll\">" + cmnt + "</span>");
        String fullCmnt = builder.toString();
        return fullCmnt;
    }
    
    @Override
    protected void setLive(boolean live) {
        super.setLive(live);

        ChannelComponentMO channel = getComponent(ChannelComponentMO.class);
        if (live == true) {
            channel.addMessageReceiver(FacebookAccessTokenMessage.class,
                (ChannelComponentMO.ComponentMessageReceiver)new FacebookAccessTokenMessageReceiver(this));
            createPeriodicTask();
        }
        else {
            channel.removeMessageReceiver(FacebookAccessTokenMessage.class);
            if (queryTaskHandle != null) {
                queryTaskHandle.cancel();
            }
        }
    }

    private void createPeriodicTask() {
        /*
        TaskManager tm = AppContext.getTaskManager();
        FacebookManager fm = AppContext.getManager(FacebookManager.class);
        fm.
        //Task queryTask = new QueryTask(this);
        
        //FacebookManager manager = new AppContext.getManager(FacebookManager.class);
        
        queryTaskHandle = tm.schedulePeriodicTask(queryTask, 0, 30000); //run the task now and then every 30 seconds
*/
    }

    private static class FacebookAccessTokenMessageReceiver extends AbstractComponentMessageReceiver{
        public FacebookAccessTokenMessageReceiver(FacebookCellMO cellMO) {
            super(cellMO);
        }
        
        public void messageReceived(WonderlandClientSender sender, WonderlandClientID clientID, CellMessage message) {
            FacebookCellMO cellMO = (FacebookCellMO)getCell();            
            FacebookAccessTokenMessage accessTokenMsg = (FacebookAccessTokenMessage)message;
            cellMO.accessToken = accessTokenMsg.getAccessToken();
            logger.log(Level.WARNING, "in the CellMO accessToken is: {0}", cellMO.accessToken);
            
            FacebookClient facebookClient = new DefaultFacebookClient(cellMO.accessToken);
            FacebookManager manager = AppContext.getManager(FacebookManager.class);
            //manager.execute(new loadMembers(facebookClient), new GroupCallback(cellMO));
            
            manager.execute(new GroupPosts(facebookClient), new GroupCallback(cellMO));
            
            
            //cellMO.sendCellMessage(clientID, message); 
        }   
    }
   
    private static class GroupPosts extends BaseFacebookRunnable<Vector> {
        GroupPosts(FacebookClient fbClient) {
            super(fbClient);
        }

        @Override
        public Vector execute(FacebookClient fbClient) throws Exception {
            FacebookFunctions fb = new FacebookFunctions(fbClient);
            Vector<Post> v = new Vector<Post>();
            v.addAll(fb.loadAllPosts());
            return v;
        }
    }
    /*
    private static class PostComments extends BaseFacebookRunnable<Vector> {
        private String postID;
        public PostComments(FacebookClient facebookClient, String postID) {
            super(facebookClient);
            this.postID = postID;
        }

        @Override
        public Vector execute(FacebookClient fbClient) throws Exception {
            FacebookFunctions fb = new FacebookFunctions(fbClient);
            Vector<Comment> v = new Vector<Comment>();
            v.addAll(fb.loadComments(postID));
            return v;
        }
    }
    * 
    */
    private static class loadMembers extends BaseFacebookRunnable<Vector> {
        public loadMembers(FacebookClient facebookClient) {
            super(facebookClient);
        }

        @Override
        public Vector execute(FacebookClient fbClient) throws Exception {
            FacebookFunctions fb = new FacebookFunctions(fbClient);
            Vector<User> v = new Vector<User>();
            v.addAll(fb.loadGroupMember());
            return v;
        }
    }
    
    private static class GroupCallback implements FacebookCallback<Vector>, Serializable {
        private final ManagedReference<FacebookCellMO> cellMORef;
        private GroupCallback(FacebookCellMO cellMO) {
            cellMORef = AppContext.getDataManager().createReference(cellMO);
        }
        
        public void handleResult(Vector v1) {
            try {
                Vector<Post> vPosts = v1;
                for(int j = 0; j < vPosts.size(); j++) {
                    String userID = vPosts.elementAt(j).getFrom().getId();
                    String userName = vPosts.elementAt(j).getFrom().getName();
                    String post = vPosts.elementAt(j).getMessage();
                    String pictureLink = vPosts.elementAt(j).getPicture();
                    
                    List<Comment> cmnts = vPosts.elementAt(j).getComments().getData();
                    Vector<String> cmntsStr = new Vector<String>();
                    for(int k = 0; k < cmnts.size(); k++) {
                        String cmntUserID = cmnts.get(k).getFrom().getId();
                        String cmntUserName = cmnts.get(k).getFrom().getName();
                        String cmnt = cmnts.get(k).getMessage();
                        String fullCmnt = cellMORef.get().commentReshape(cmntUserID, cmntUserName, cmnt);
                        cmntsStr.add(fullCmnt);
                    }
                    
                    
                    String posterText = cellMORef.get().reshapeText(userID, userName, post, pictureLink, cmntsStr);
                    cellMORef.get().addPostIntoWorld(posterText, j + 5, 1, j + 5);
                }
            }
            catch (Exception e) {}
            /*
            try {
                Vector<User> vUsers = v1;
                for(int j = 0; j < vUsers.size(); j++) {
                    String userID = vUsers.elementAt(j).getId();
                    String userName = vUsers.elementAt(j).getName();
                    //String about = vUsers.elementAt(j).getEmail();
                    String posterText = cellMORef.get().reshapeText(userID, userName, "", null, null);
                    cellMORef.get().addPostIntoWorld(posterText, j, 3, j + 1);
                }
            }
            catch (Exception e) {}
            
            try {
                Vector<Comment> vCmnts = v1;
                for(int j = 0; j < vCmnts.size(); j++) {
                    String userID = vCmnts.elementAt(j).getFrom().getId();
                    String userName = vCmnts.elementAt(j).getFrom().getName();
                    String post = vCmnts.elementAt(j).getMessage();
                    String posterText = cellMORef.get().reshapeText(userID, userName, post, null, null);
                    cellMORef.get().addPostIntoWorld(posterText, j + 3, 1, j - 3);
                }
            }
            catch(Exception e) {}
            * 
            */
        }
        public void handleError(Throwable error) {
            logger.log(Level.WARNING, error.getLocalizedMessage());
        }
    }
}