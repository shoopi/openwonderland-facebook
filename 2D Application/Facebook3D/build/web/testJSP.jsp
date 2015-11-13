<%-- 
    Document   : testJSP
    Created on : 07-Jun-2012, 21:35:36
    Author     : Shaya
--%>

<%@page import="Facebook_Java.TempServlet"%>
<%@page import="Facebook_Java.NewClass"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <p>
        <% NewClass c = new NewClass();
         out.print("key is: " + c.getKey());
        %>
        <p>
        <p>
            <% out.print("the original key is: " + TempServlet.accessToken); %>
        </p>
    </body>
</html>
