package com.musiy.servlets;

import com.musiy.dao.UserDto;
import com.musiy.dao.UsersDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class UsersListServlet extends HttpServlet {

    private final UsersDao usersDao;

    public UsersListServlet(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<UserDto> users = usersDao.getUsers();
        prepareResponse(resp, users);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name != null) {
            UserDto userDto = new UserDto();
            userDto.setName(name);
            usersDao.saveUser(userDto);
        }
        resp.sendRedirect(req.getContextPath() + req.getServletPath());
    }

    private void prepareResponse(HttpServletResponse resp, Collection<UserDto> users) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.print("<html><body>");
        writer.print("<b>Список пользователей:</b><br>");
        for (UserDto userDto : users) {
            writer.print(userDto.getName() + "<br>");
        }
        writer.print("==========================="
                + "<form method='POST'>"
                + "<input type='text' name='name'/>"
                + "<input type='submit' value='Добавить'/></form>");
        writer.print("</body></html>");
        writer.flush();
    }
}
