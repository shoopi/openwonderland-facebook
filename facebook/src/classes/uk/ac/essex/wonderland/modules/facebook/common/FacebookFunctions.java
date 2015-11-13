/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.common;

import uk.ac.essex.wonderland.modules.facebook.server.*;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.*;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Shaya
 */
public class FacebookFunctions {

    private String accessToken = "";
    FacebookClient facebookClient; 
    //Group group; 

    public FacebookFunctions(String accessToken) {
        this.accessToken = accessToken;
        facebookClient = new DefaultFacebookClient(accessToken);
        System.out.println("facebook Client Object is created with access token: " + this.accessToken);
        //group = facebookClient.fetchObject("246428652139549", Group.class);
    }
        
    public void sendPost(String message)
    {
        facebookClient.publish("246428652139549/feed" , FacebookType.class, Parameter.with("message", message));
    }

    public void sendComment(String postID, String message)
    {
        facebookClient.publish(postID + "/comments", FacebookType.class, Parameter.with("message" , message));
    }

    public User loadUser(String userID)
    {
        User user = facebookClient.fetchObject( userID, User.class);
        return user;
    }

    public void likePost (String postID, String postType)
    {
        //TODO: CREATE THIS METHOD  
        FacebookType publishMsgResponse = facebookClient.publish( postID + "/" + postType, FacebookType.class, Parameter.with("message", "This msg has auto comment feature #2 :::JAVA program - 3D E-Learning App:::"));
        facebookClient.publish(publishMsgResponse.getId() + "/comments", FacebookType.class, Parameter.with("message" , "This is auto comment"));
    }

    public Vector<Comment> loadComments (String postID)
    {
        Vector<Comment> comments = new Vector<Comment>();
        Post post = facebookClient.fetchObject(postID, Post.class);
        comments.addAll(post.getComments().getData());
        return comments;
    }
    
    public Vector<Post> loadAllPosts()
    {
        Vector<Post> posts = new Vector<Post>();
        Connection<Post> groupFeed = facebookClient.fetchConnection("246428652139549/feed", Post.class);
        for(List<Post> groupFeedConnection : groupFeed)
            for(Post post : groupFeedConnection)
                posts.add(post);
        return posts;
    }
    
    public String groupOwner(){
        System.out.println("IIIIIINNNNNNNN METHODDDDDDD CALLLLL SHOD");
        
        return facebookClient.fetchObject("246428652139549", Group.class).getOwner().getName();
        //return "dasdas";
    }
    
    public String testMethod(){
       return "This is the test Method";
    }
}
