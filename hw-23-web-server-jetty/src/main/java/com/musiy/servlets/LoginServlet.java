package com.musiy.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.print(""
                + "<html>"
                + "<form method='POST' action='/j_security_check'>"
                + "<input type='text' name='j_username'/>"
                + "<input type='password' name='j_password'/>"
                + "<input type='submit' value='Login'/></form>"
                + "</html>"
        );

        writer.flush();
    }

}
