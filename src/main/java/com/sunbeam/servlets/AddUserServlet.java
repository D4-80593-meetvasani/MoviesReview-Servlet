package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunbeam.daos.UserDao;
import com.sunbeam.daos.UserDaoImple;
import com.sunbeam.pojos.User;


@WebServlet("/register")
public class AddUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String email = req.getParameter("email");
        String password = req.getParameter("pwd");
        String mobile = req.getParameter("mobile");
        String dob = req.getParameter("dob");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date uDate = null;
        try {
            uDate = sdf.parse(dob);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(uDate.getTime());

        try (UserDao dao = new UserDaoImple()) {
            Optional<User> userOpt = dao.findByEmail(email);
            if (userOpt.isPresent()) {
                throw new ServletException("User is already present");
            } else {
                User u = new User(0, fname, lname, email, password, mobile, dob);
                dao.save(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        // presentation logic
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Registered</title>");
        out.println("<style>");
        out.println("body {");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    background-color: #f4f4f4;");
        out.println("    text-align: center;");
        out.println("    margin-top: 50px;");
        out.println("}");

        out.println("h1 {");
        out.println("    color: #333333;");
        out.println("}");

        out.println("a {");
        out.println("    text-decoration: none;");
        out.println("    color: #007BFF;");
        out.println("    display: block;");
        out.println("    margin-top: 20px;");
        out.println("}");

        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
		ServletContext app = req.getServletContext();
		String title = app.getInitParameter("appTitle");
		out.printf("<h1>%s</h1>\n", title);
		
        out.println("<h1>Registered Successfully</h1><br/>");
        out.println("<a href=\"index.html\">Login</a>");
        out.println("</body>");
        out.println("</html>");
    }
}
