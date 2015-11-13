package Facebook_Java;


import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.*;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class FacebookTestApp 
{   
    String MY_ACCESS_KEY = TempServlet.accessToken;
    FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_KEY);
    Group group = facebookClient.fetchObject("246428652139549", Group.class);

    public Group getGroup() {
        return group;
    }
    
    public Vector<Post> loadAllPosts() {
        Vector<Post> posts = new Vector<Post>();
        Connection<Post> groupFeed = facebookClient.fetchConnection("246428652139549/feed", Post.class);
        for(List<Post> groupFeedConnection : groupFeed)
            for(Post post : groupFeedConnection)
                posts.add(post);
        return posts;
    }
    
    public void sendPost(String message) {
        facebookClient.publish("246428652139549/feed" , FacebookType.class, Parameter.with("message", message));
    }

    public void sendComment(String postID, String message) {
        facebookClient.publish(postID + "/comments", FacebookType.class, Parameter.with("message" , message));
    }

    public User loadUser(String userID) {
        User user = facebookClient.fetchObject( userID, User.class);
        return user;
    }

    public void likePost (String postID, String postType) {
        //TODO: CREATE THIS METHOD  
        FacebookType publishMsgResponse = facebookClient.publish( postID + "/" + postType, FacebookType.class, Parameter.with("message", "This msg has auto comment feature #2 :::JAVA program - 3D E-Learning App:::"));
        facebookClient.publish(publishMsgResponse.getId() + "/comments", FacebookType.class, Parameter.with("message" , "This is auto comment"));
    }

    public Vector<Comment> loadComments (String postID) {
        Vector<Comment> comments = new Vector<Comment>();
        Post post = facebookClient.fetchObject(postID, Post.class);
        comments.addAll(post.getComments().getData());
        return comments;
    }
    
    public boolean checkUpdate(Date oldDate) {
        boolean isUpdate = false;
        if(oldDate.equals(group.getUpdatedTime()))
            isUpdate = true;
        return isUpdate;
    }
    
    public Vector<User> loadGroupMember() {
        Vector<User> users = new Vector<User>();
        Connection<User> members = facebookClient.fetchConnection("246428652139549/members", User.class);
        for(List<User> member : members)
            for(User user : member)
                users.add(user);
        return users;
    }
    
    public Vector<Post> findDocuments() {
        Vector<Post> users = new Vector<Post>();
        Connection<Post> members = facebookClient.fetchConnection("246428652139549/docs", Post.class);
        for(List<Post> member : members)
            for(Post user : member)
                users.add(user);
        return users;
    }
    
    public void sendNewComment(String postID, String message, String accessToken) {
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        newFacebookClient.publish(postID + "/comments", FacebookType.class, Parameter.with("message" , message));
    }
    
    public void sendPost(String message, String accessToken ) {
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        newFacebookClient.publish("246428652139549/feed" , FacebookType.class, Parameter.with("message", message));
    }
    
    public Post loadPost (String postID, String accessToken) {
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        Post post = newFacebookClient.fetchObject(postID, Post.class);
        return post;
    }
    
    public User loadUser(String userID, String accessToken) {
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        User user = newFacebookClient.fetchObject( userID, User.class);
        return user;
    }
    
    public Vector<Post> loadAllPosts(String accessToken) {
        Vector<Post> posts = new Vector<Post>();
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        Connection<Post> groupFeed = newFacebookClient.fetchConnection("246428652139549/feed", Post.class);
        for(List<Post> groupFeedConnection : groupFeed)
            for(Post post : groupFeedConnection)
                posts.add(post);
        return posts;
    }
    
    public Vector<Comment> loadComments (String postID, String accessToken) {
        Vector<Comment> comments = new Vector<Comment>();
        FacebookClient newFacebookClient = new DefaultFacebookClient(accessToken);
        Post post = newFacebookClient.fetchObject(postID, Post.class);
        comments.addAll(post.getComments().getData());
        return comments;
    }
}
