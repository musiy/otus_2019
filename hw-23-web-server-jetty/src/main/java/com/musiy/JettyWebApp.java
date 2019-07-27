package com.musiy;

import com.musiy.dao.MongoDaoFactory;
import com.musiy.filters.SimpleFilter;
import com.musiy.loginservice.MyLoginService;
import com.musiy.loginservice.Role;
import com.musiy.servlets.LoginServlet;
import com.musiy.servlets.UsersListServlet;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.FormAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;

import java.net.URL;

/**
 * Чтобы запустить пример из jar файла, положите в каталог с jar-файлом файл  realm.properties
 */

public class JettyWebApp {

    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        new JettyWebApp().start();
    }

    private void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    public Server createServer(int port) {
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new UsersListServlet(MongoDaoFactory.getInstance().getUsersDao())),
                "/restricted/users");
        context.addFilter(new FilterHolder(new SimpleFilter()), "/*", null);
        context.setSecurityHandler(createSecurityHandler("/restricted/*"));
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{
                createResourceHandler(),
                context});
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = JettyWebApp.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        String fileDirPath = fileDir.toExternalForm();
        System.out.println(fileDirPath);
        resourceHandler.setResourceBase(fileDirPath);

        MimeTypes mimeTypes = new MimeTypes();
        mimeTypes.addMimeMapping("html", "text/html; charset=UTF-8");
        resourceHandler.setMimeTypes(mimeTypes);
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(String pathSpec) {
        Constraint constraint = new Constraint();
        constraint.setName("auth constraint");
        constraint.setRoles(new String[]{Role.USER.toString()});
        constraint.setAuthenticate(true);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setPathSpec(pathSpec);

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.addConstraintMapping(mapping);
        securityHandler.setLoginService(new MyLoginService());
        securityHandler.setAuthenticator(new FormAuthenticator("/login", "/login", false));

        return securityHandler;
    }
}
