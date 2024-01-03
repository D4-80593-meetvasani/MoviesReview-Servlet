package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	
		// to destroy the cookie
		Cookie c = new Cookie("username", "");
		c.setMaxAge(-1);
		resp.addCookie(c);
		
		// destroy the current user session
		HttpSession session = req.getSession();
		session.invalidate();
    	
    	
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Logout</title>");

        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; }");
        out.println("a { color: #007bff; text-decoration: none; }");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Logout</h1>");
        out.println("<p>Thank you.</p>");
        out.println("<p><a href='index.html'>Login Again</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
