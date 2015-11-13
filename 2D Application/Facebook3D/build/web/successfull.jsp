<%-- 
    Document   : successfull
    Created on : 17-Aug-2012, 01:27:49
    Author     : Shaya
--%>

<%@page import="com.restfb.types.User"%>
<%@page import="com.restfb.types.Comment"%>
<%@page import="java.util.Vector"%>
<%@page import="com.restfb.types.Post"%>
<%@page import="Facebook_Java.FacebookTestApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Stylesheet/FacebookStylesheet.css" rel="stylesheet" type="text/css" />
        <title>Successful Transaction</title>
    </head>
    <body>
        <%  String transaction = request.getParameter("trans");
            String accessToken = request.getParameter("accessToken");
            FacebookTestApp fb = new FacebookTestApp();
        if(transaction.contains("cmnt")) { %>
        <%  String postID = request.getParameter("postID");
            Post post = fb.loadPost(postID, accessToken);
            String userID = post.getFrom().getId();
            String userName = post.getFrom().getName();
            %>
            <h1 style="border:2px #3b5991 dashed; -moz-border-radius: 10px; -webkit-border-radius: 10px;"> Your Comment has been successfully added. </h1>
        <div class="post">
                <div id="member">
                    <a href="http://facebook.com/<%= userID %>" >
                        <p id="memberPic">
                            <img alt="<%= userName %> " src="http://graph.facebook.com/<%= userID %>/picture" />
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
                comments = fb.loadComments(postID, accessToken); %>
                <span style="font-weight: bold; margin-left: 10px;"> Comment Counter: </span> <%=comments.size() %>
            </div>
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
        <%} if (transaction.contains("post")) { %>
        <h1 style="border:2px #3b5991 dashed; -moz-border-radius: 10px; -webkit-border-radius: 10px;"> Your new discussion has been successfully created. </h1>
        <h4> List of Discussions</h4>
        <div id="oldPosts">
        <% Vector<Post> posts = fb.loadAllPosts(accessToken);
        for(int i=0; i<posts.size(); i++) {
            Post post = posts.elementAt(i);
            User user = fb.loadUser(posts.elementAt(i).getFrom().getId(), accessToken);
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
            </div>
        <% } } %>
    </body>
</html>
