/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.essex.wonderland.modules.facebook.server;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.*;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Shaya
 */
public class FacebookFunctions {

    FacebookClient facebookClient; 
    
    protected FacebookFunctions(FacebookClient facebookClient) 
    {
        this.facebookClient = facebookClient;
    }
        
    protected void sendPost(String message)
    {
        facebookClient.publish("246428652139549/feed" , FacebookType.class, Parameter.with("message", message));
    }

    protected void sendComment(String postID, String message)
    {
        facebookClient.publish(postID + "/comments", FacebookType.class, Parameter.with("message" , message));
    }

    protected User loadUser(String userID)
    {
        User user = facebookClient.fetchObject( userID, User.class);
        return user;
    }

    protected void likePost (String postID, String postType)
    {
        //TODO: CREATE THIS METHOD  
        FacebookType publishMsgResponse = facebookClient.publish( postID + "/" + postType, FacebookType.class, Parameter.with("message", "This msg has auto comment feature #2 :::JAVA program - 3D E-Learning App:::"));
        facebookClient.publish(publishMsgResponse.getId() + "/comments", FacebookType.class, Parameter.with("message" , "This is auto comment"));
    }

    protected Vector<Comment> loadComments (String postID)
    {
        Vector<Comment> comments = new Vector<Comment>();
        Post post = facebookClient.fetchObject(postID, Post.class);
        comments.addAll(post.getComments().getData());
        return comments;
    }
    
    protected Vector<Post> loadAllPosts()
    {
        Vector<Post> posts = new Vector<Post>();
        Connection<Post> groupFeed = facebookClient.fetchConnection("246428652139549/feed", Post.class);
        for(List<Post> groupFeedConnection : groupFeed)
            for(Post post : groupFeedConnection)
                posts.add(post);
        return posts;
    }
    
    protected boolean checkUpdate(Date oldDate) {
        boolean isUpdate = false;
        Group group = facebookClient.fetchObject("246428652139549", Group.class);
        if(oldDate.equals(group.getUpdatedTime()))
            isUpdate = true;
        return isUpdate;
    }
    
    protected Vector<User> loadGroupMember() {
        Vector<User> users = new Vector<User>();
        Connection<User> members = facebookClient.fetchConnection("246428652139549/members", User.class);
        for(List<User> member : members)
            for(User user : member)
                users.add(user);
        return users;
    }
}
