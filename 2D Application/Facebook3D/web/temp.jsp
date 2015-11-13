<%-- 
    Document   : temp
    Created on : 07-Jun-2012, 03:37:07
    Author     : Shaya
--%>

<%@page import="Facebook_Java.TempServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Stylesheet/FacebookStylesheet.css" rel="stylesheet" type="text/css" />
        <title>Essex Application - Access Token</title>
    </head>
    <body>
        <div style="text-align:center; margin: 0 auto; padding:10px; margin-top:50px; width:800px; border:2px #3b5991 dashed; -moz-border-radius: 10px; -webkit-border-radius: 10px;">
            <div style="margin: auto auto; border-bottom:2px #3a5977 dashed; -moz-border-radius: 10px; -webkit-border-radius: 10px;">
                <h2>Your Authentication code (Access Token) is: </h2>
                <p> <%= TempServlet.accessToken %> </p>
            </div>
            <div>
                <p> If you want to continue via the 2D Application Please, <a href="index.jsp"> Click Here! </a></p>
            </div>
        </div>
        
    </body>
</html>
