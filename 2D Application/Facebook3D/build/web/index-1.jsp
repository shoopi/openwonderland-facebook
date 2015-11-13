<%-- 
    Document   : index
    Created on : 04-Jun-2012, 13:45:13
    Author     : Shaya
--%>

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
        <title>Essex</title>
    </head>
    <body>
        <%
        FacebookTestApp fb = new FacebookTestApp();
        Group group = fb.getGroup(); %>
        
        <h2><a href="http://facebook.com/groups/<%= group.getId() %>" >
            <%= group.getName() %></a></h2>
        <h5>
            Group Owner: <%= group.getOwner().getName() %>
            <br />
            Last Update Time: <%= group.getUpdatedTime() %>
            <br />
            Privacy: <%= group.getPrivacy() %>
        </h5>
        <div id="createPost">
            <fieldset>
                <legend> New Post </legend>
                <form name="newPostForm" action="" method="POST">
                    <input type="text" name="inputPost" />
                    <% String newPost = request.getParameter("inputPost");%>
                    <input type="submit" name="submitNewPost" value="Submit Post" formaction="<% if(newPost != null && newPost.trim().length() > 0) {fb.sendPost(newPost);} %>"/>
                    <!-- TODO: EXCEPTION HANDLING FOR EMPTY FIELDS -->
                </form> 
            </fieldset>
        </div>
        <div id="oldPosts">
        <ul>
            <%
            for(int i=0; i<fb.loadAllPosts().size(); i++)
            {%>
                <li>
                    <p>
                        <%
                            User user = fb.loadUser(fb.loadAllPosts().elementAt(i).getFrom().getId());
                            Post post = fb.loadAllPosts().elementAt(i);
                        %>
                        <!-- Sender -->
                        <a href="http://facebook.com/<%= user.getId() %>" >
                            <img alt="<%= user.getName() %> TEST" src="http://graph.facebook.com/<%= user.getId() %>/picture" />
                            <% out.print(user.getName()); %>
                        </a>
                        
                        <!-- Post -->
                    </p>
                    <p>
                        Post: <% out.print(post.getMessage());%>
                        <br />
                        Date: <% out.print(post.getCreatedTime());%>
                    </p>
                    <p>
                        Like( <%= post.getLikesCount() %> )
                        <br />
                        <%Vector<Comment> comments = new Vector<Comment>();
                        comments = fb.loadComments(post.getId());
                        %>
                        Comments( <%=comments.size() %> )
                        <ul>
                            <%
                            for(int cmnt=0; cmnt< comments.size(); cmnt++){
                            %>
                            <li>
                                <a href="http://facebook.com/<%= comments.elementAt(cmnt).getFrom().getId() %>" >
                                    <%= comments.elementAt(cmnt).getFrom().getName() %>" </a> 
                                : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <%= comments.elementAt(cmnt).getMessage() %>
                                
                                <br />
                                
                                Date: <%= comments.elementAt(cmnt).getCreatedTime() %>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                Like( <%= comments.elementAt(cmnt).getLikes() %> )
                            </li><br />
                            <%}%>
                        </ul>
                    
                    <form action="" method="POST" name="form<%= i %>">
                        <input type="text" name="inputComment<%=i %>"/>
                        <%
                        String newComment = request.getParameter("inputComment" + i);
                         %>
                         <input type="submit" name="cmntSubmitBtn<%=i %>" value="submit" formaction="<% if (newComment != null && newComment.trim().length() > 0){ fb.sendComment(post.getId(), newComment);} %>">
                    </form>
                    </p>
                </li><hr /><br /> 
            <%}%>
        </ul>
        </div>
    </body>
</html>
