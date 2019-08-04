package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dao.DbService;
import ru.otus.userspace.User;

import java.util.List;

@Controller
public class UserController {

    private DbService dbService;

    UserController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping({"/"})
    public String index(Model model) {
        return "index.html";
    }

    @GetMapping({"/user/list"})
    public String userList(Model model) {
        List<User> users = dbService.listAll(User.class);
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/user/create")
    public String userCreate(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @PostMapping("/user/save")
    public RedirectView userCreate(@ModelAttribute User user) {
        dbService.saveOrUpdate(user);
        return new RedirectView("/user/list", true);
    }
}
