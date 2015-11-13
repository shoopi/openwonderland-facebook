<%-- 
    Document   : main
    Created on : 16-Aug-2012, 18:52:14
    Author     : Shaya
--%>

<%@page import="Facebook_Java.FacebookTestApp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Essex - E-Learning 3D Application</title>
    </head>
    <body>
        <% FacebookTestApp fb = new FacebookTestApp(); %>
        <% String postID = request.getParameter("postID"); 
           String accessToken = request.getParameter("accessToken"); %>
        <div id="newCmnt">
            <form action="" method="POST" name="form<%=postID%>">
                <input type="text" name="inputComment<%=postID %>"/> 
                <% String newComment = request.getParameter("inputComment" + postID); %>
                <input type="submit" name="cmntSubmitBtn<%=postID %>" value="New Comment" formaction="<% if (newComment != null && newComment.trim().length() > 0){ fb.sendNewComment(postID, newComment, accessToken);} %>" class="fbtab">
            </form>
        </div>
    </body>
</html>
