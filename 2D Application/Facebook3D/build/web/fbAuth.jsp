<%-- 
    Document   : fbAuth
    Created on : 06-Jun-2012, 20:02:49
    Author     : Shaya
--%>

<%@page import="Facebook_Java.AuthenticationProperties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Stylesheet/FacebookStylesheet.css" rel="stylesheet" type="text/css" />
        <title>Essex 3D Collaborative Learning - Facebook Login</title>
    </head>
    <body>
        <div style="text-align:center; margin: 0 auto; padding:10px; margin-top:50px; width:300px; border:2px #3b5998 dashed; -moz-border-radius: 10px; -webkit-border-radius: 10px;">
        <h1>Authentication Servlet</h1>
        <lable> You need to login to Facebook, please </label>
        <span>
            <a href="<%= AuthenticationProperties.getLoginRedirectURL() %>">Click me!</a>
        </span>
        </div>
    </body>
</html>
