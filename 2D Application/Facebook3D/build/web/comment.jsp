<%-- 
    Document   : main
    Created on : 16-Aug-2012, 18:52:14
    Author     : Shaya
--%>

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
        <title>Essex - E-Learning 3D Application</title>
    </head>
    <body>
        <h1> Add a new Comment </h1>
        <%  FacebookTestApp fb = new FacebookTestApp(); %>
        <%  String postID = request.getParameter("postID");
            String accessToken = request.getParameter("accessToken"); 
            Post post = fb.loadPost(postID, accessToken);
            String userID = post.getFrom().getId();
            String userName = post.getFrom().getName();
            %>
        
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
           
        <div id="newCmnt">
            <form action="successfull.jsp?trans=cmnt&postID=<%=postID%>&accessToken=<%=accessToken%>" method="POST" name="form<%=postID%>">
                <input type="text" name="inputComment<%=postID %>"/> 
                <% String newComment = request.getParameter("inputComment" + postID); %>
                <p> 
                    <label for="accessToken<%=postID %>"> accessToken 
                    <input type="text" name="accessToken<%=postID %>" />
                    <% String accessToken1 = request.getParameter("accessToken" + postID); %>
                </p>
                <input type="submit" name="cmntSubmitBtn<%=postID %>" value="New Comment" formaction="<% if (newComment != null && newComment.trim().length() > 0){ fb.sendNewComment(postID, newComment, accessToken1); response.sendRedirect("successfull.jsp?trans=cmnt&postID=" + postID + "&accessToken=" + accessToken1);} %>" class="fbtab">
            </form>
        </div>
    </body>
</html>
