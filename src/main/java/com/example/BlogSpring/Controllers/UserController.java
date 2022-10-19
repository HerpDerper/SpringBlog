package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String userIndex(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Users/Index";
    }

    @GetMapping("/user/create")
    public String userCreate(Model model) {
        return "Users/Create";
    }

    @PostMapping("/user/create")
    public String blogUserCreate(@RequestParam String name,
                                 @RequestParam String surname,
                                 @RequestParam String lastName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateBirth, Model model) {
        User user = new User(name, surname, lastName, email, password, dateBirth);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/user/index")
    public String userFilter(@RequestParam(required = false) String email,
                           @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<User> users = new ArrayList<User>();
        if (email != null && !email.equals("")) {
            if (exactSearch != null && exactSearch)
                users = userRepository.findByEmail(email);
            else
                users = userRepository.findByEmailContains(email);
        } else
            users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Users/Index";
    }
}