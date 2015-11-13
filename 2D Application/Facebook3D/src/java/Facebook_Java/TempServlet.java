/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Facebook_Java;


import com.visural.common.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TempServlet extends HttpServlet 
{
	
    public static String accessToken = "";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TempServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TempServlet at " + request.getContextPath() + "</h1>");
            String code = request.getParameter("code");
            if(StringUtil.isNotBlankStr(code))
            {
                out.print("<p> " + request.getParameter("code") + "</p>");
                String authURL = AuthenticationProperties.getAuthURL(code);
                URL url = new URL(authURL);
                out.print("<p>" + "link is: " +authURL + "</p>");
                
                try 
                {
                    String result = readURL(url);
                    out.print("<p>" + "Sring result is: " + result + "</p>");
                    
                    accessToken = null;
                    Integer expires = null;
                    String[] pairs = result.split("&");
                
                    for (String pair : pairs)
                    {
                        String[] temp = pair.split("=");
                        if (temp.length != 2) 
                        {
                            //out.print("<p> this string has more/less than 2 pairs, it has: " + temp.length + "</p>");
                            throw new RuntimeException("Unexpected Authentication response");
                        } 
                    else 
                    {
                        if (temp[0].equals("access_token")) 
                        {
                            accessToken = temp[1];
                            out.print("<p>" + "access token is: " + accessToken + "</p>");
                        }
                        if (temp[0].equals("expires")) 
                        {
                            expires = Integer.valueOf(temp[1]);
                            out.print("<p>" + "expires time is: " + expires + "</p>");
                        }
                    }
                }    
                if (accessToken != null && expires != null) 
                {   
                    //FacebookTestApp fb = new FacebookTestApp();
                    //fb.setMY_ACCESS_KEY(accessToken);
                    
                    //NewClass c = new NewClass();
                    //c.setKey(accessToken);   
                    //out.print("<p> access tokin in the program is: " + fb.getMY_ACCESS_KEY() + "</p>");
                    response.sendRedirect("http://shoopi.appspot.com/temp.jsp");
                } 
                else 
                {
                    throw new RuntimeException("Access token and expires not found");
                }
            } 
            catch (IOException e) 
            {
                throw new RuntimeException(e);
            }
            }
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    
    private String readURL(URL url) throws IOException 
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) 
        {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
