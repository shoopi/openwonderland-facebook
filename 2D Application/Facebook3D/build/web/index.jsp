<%-- 
    Document   : index
    Created on : 04-Jun-2012, 13:45:13
    Author     : Shaya
--%>

<%@page import="Facebook_Java.TempServlet"%>
<%@page import="Facebook_Java.AuthenticationProperties"%>
<%@page import="com.restfb.types.Group"%>
<%@page import="sun.awt.SunHints.Value"%>
<%@page import="java.util.Vector"%>
<%@page import="com.restfb.types.Comment"%>
<%@page import="com.restfb.types.Post"%>
<%@page import="com.restfb.types.User"%>
<%@page import="Facebook_Java.FacebookTestApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Stylesheet/FacebookStylesheet.css" rel="stylesheet" type="text/css" />
        <title>Essex 3D E-learning Application</title>
    </head>
    <body>
        <%
        FacebookTestApp fb = new FacebookTestApp();
        Group group = fb.getGroup(); 
        %>
        <br />
        <a href="http://facebook.com/groups/<%= group.getId() %>" style="text-align: center;">
            <h1><%= group.getName() %></h1>
        </a>
        
        <div id="groupInformation">
            <h5>Group Owner:</h5> <%= group.getOwner().getName() %>
            <br />
            <h5>Last Update Time:</h5> <%= group.getUpdatedTime() %>
            <br />
            <h5>Privacy: </h5><%= group.getPrivacy() %>
        </div>
        
        <div id="groupMemebrs">
            <% 
            Vector<User> users = new Vector<User>();
            users = fb.loadGroupMember();
            %><div id="memberLists"><%
            for(int i = 0; i < users.size() ; i++) {%>
                <div id="member">
                    <a href="http://facebook.com/<%= users.elementAt(i).getId() %>" >
                        <p id="memberPic">
                            <img alt="<%= users.elementAt(i).getName() %> " src="http://graph.facebook.com/<%= users.elementAt(i).getId() %>/picture" />
                        </p>
                        <p id="memberName">
                            <% out.print(users.elementAt(i).getName()); %>
                        </p>
                    </a>
                </div>
            <%}%>
            </div>
        </div>
        <div id="documents"> <!-- DOCUMENT SECTION --- FACEBOOK BUG REPORTED
            <%
            //Vector<Post> docs = new Vector<Post>();
            //docs = fb.findDocuments();
            Post doc = new Post();
            %><ul><%
            /*
            for(int i = 0; i < docs.size() ; i++) {
                Post doc = docs.elementAt(i);
                */%><li>
                    <p>
                    Post ID: <%=doc.getId() %>
                    </p>
                    <p>
                        Link: <%=doc.getSource()%>
                        <br />
                        Photo URL: <%=doc.getPicture() %>
                        <br />
                        Attribution: <%=doc.getAttribution() %>
                        <br />
                        Application <%=doc.getApplication() %>
                        <br />
                        Message <%=doc.getMessage() %>
                    </p>
                </li>
            <%//}%>
            </ul>
            -->
        </div>
        <div id="createPost">
            <fieldset>
                <legend style="font-weight: bold"> Create a new Discussion </legend>
                <form name="newPostForm" action="" method="POST">
                    <p>
                        <input type="text" name="inputPost" style="height:30px; width:300px;"/>
                        <% String newPost = request.getParameter("inputPost");%>
                    </p>
                    <p>
                        <input type="submit" name="submitNewPost" style="margin: 0 auto;" value="Submit Your Discussion" formaction="<% if(newPost != null && newPost.trim().length() > 0) {fb.sendPost(newPost);} %>" class="fbtab"/>
                        <!-- TODO: EXCEPTION HANDELING FOR EMPT FIELD -->
                    </p>
                </form> 
            </fieldset>
        </div>
        <div id="oldPosts">
        <% Vector<Post> posts = fb.loadAllPosts();
        for(int i=0; i<posts.size(); i++) {
            Post post = posts.elementAt(i);
            User user = fb.loadUser(posts.elementAt(i).getFrom().getId());
            String userID = user.getId();
            String userName = user.getName();
            String postID = post.getId(); %>
            <div class="post">
                <div id="member">
                    <a href="http://facebook.com/<%= userID %>" >
                        <p id="memberPic">
                            <img alt="<%= user.getName() %> " src="http://graph.facebook.com/<%= userID %>/picture" />
                        </p>
                        <p id="memberName">
                            <% out.print(userName); %>
                        </p>
                    </a>
                </div>
                <div id="postMsg">
                    <p style="font-weight: bold;">
                        <%=post.getMessage()%>
                    </p>
                    <p>
                        <span style="font-weight: bold;">Date: </span><%= post.getCreatedTime()%>
                    </p>
                    <p>
                    <% if (post.getSource() != null) { %>
                        <a href="<%=post.getSource()%>"> Click here </a> <%}%>
                    </p>
                    <p>
                    <% if (post.getPicture() != null) {%>
                        <img src ="<%= post.getPicture() %>" alt="No picture was found" /> <%}%>
                    </p>
                </div>
                <div id="postInfo">
                    <span style="font-weight: bold;">Like Counter: </span> <% if(post.getLikesCount() != null ) { out.print(post.getLikesCount());} else { out.print("0"); } %>
                    <% Vector<Comment> comments = new Vector<Comment>();
                    comments = fb.loadComments(postID); %>
                    <span style="font-weight: bold; margin-left: 10px;"> Comment Counter: </span> <%=comments.size() %>
                </div>
                <div id="commentList">
                <% for(int cmnt=0; cmnt< comments.size(); cmnt++){ %>
                    <div id="comment">
                        <span id="member">
                            <a href="http://facebook.com/<%= comments.elementAt(cmnt).getFrom().getId() %>" >
                                <span id="memberName">
                                    <%= comments.elementAt(cmnt).getFrom().getName() %>
                                </span>
                            </a>
                        </span>
                        <span style="margin-left: 5px;">
                            <%= comments.elementAt(cmnt).getMessage() %>     
                        </span>
                        <p>
                            <span style="margin: 0 auto; font-weight: bold;">Date: </span> <%= comments.elementAt(cmnt).getCreatedTime() %>
                            <span style="margin-left: 5px; font-weight: bold;"> Like: </span> <% if( comments.elementAt(cmnt).getLikes() != null ) { out.print(comments.elementAt(cmnt).getLikes()); } else { out.print("0");} %>    
                        </p>
                    </div>
                <%}%>
                </div>
                <div id="newCmnt">
                    <form action="" method="POST" name="form<%=postID%>">
                        <input type="text" name="inputComment<%=postID %>"/> 
                        <% String newComment = request.getParameter("inputComment" + postID); %>
                        <input type="submit" name="cmntSubmitBtn<%=postID %>" value="New Comment" formaction="<% if (newComment != null && newComment.trim().length() > 0){ fb.sendComment(postID, newComment); response.sendRedirect("index.jsp");
                        } %>" class="fbtab">
                    </form>
                </div>
            </div>
        <%}%>
        </div>
    </body>
</html>