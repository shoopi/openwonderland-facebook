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
        <link href="Stylesheet/FacebookStylesheet.css" rel="stylesheet" type="text/css" />
        <title>Essex - E-Learning 3D Application</title>
    </head>
    <body>
        <% FacebookTestApp fb = new FacebookTestApp(); %>
        <% String accessToken = request.getParameter("accessToken"); %> 
        <div id="createPost">
            <fieldset>
                <legend style="font-weight: bold"> Create a new Discussion </legend>
                <h1> Create a New Post </h1>
                <form name="newPostForm" action="successfull.jsp?trans=cmnt&accessToken=<%=accessToken%>" method="POST">
                    <p>
                        <input type="text" name="inputPost" style="height:30px; width:300px;"/>
                        <% String newPost = request.getParameter("inputPost");%>
                    </p>
                    <p> 
                    <label for="accessToken"> accessToken 
                    <input type="text" name="accessToken" />
                    <% String accessToken1 = request.getParameter("accessToken"); %>
                </p>
                    <p>
                        <input type="submit" name="submitNewPost" style="margin: 0 auto;" value="Submit Your Discussion" formaction="<% if(newPost != null && newPost.trim().length() > 0) {fb.sendPost(newPost, accessToken1); response.sendRedirect("successfull.jsp?trans=post&postID=1&accessToken=" + accessToken1);} %>" class="fbtab"/>
                        <!-- TODO: EXCEPTION HANDELING FOR EMPT FIELD -->
                    </p>
                </form> 
            </fieldset>
        </div>
    </body>
</html>