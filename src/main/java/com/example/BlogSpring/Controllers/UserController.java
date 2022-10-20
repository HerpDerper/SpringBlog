package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

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
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateBirth) {
        User user = new User(name, surname, lastName, email, password, dateBirth);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/user/index")
    public String userFilter(@RequestParam(required = false) String email,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<User> users;
        if (email != null && !email.equals("")) {
            if (exactSearch != null && exactSearch) users = userRepository.findByEmail(email);
            else users = userRepository.findByEmailContains(email);
        } else users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Users/Index";
    }

    @PostMapping("/user/details")
    public String getSelectedUser(@RequestParam long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "Users/Details";
    }

    @PostMapping("/user/delete")
    public String userDelete(@RequestParam long id) {
        User user = userRepository.findById(id).get();
        List<Comment> comments = commentRepository.findByUser(user);
        List<Post> posts = postRepository.findByUser(user);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
        for (Post post : posts) {
            postRepository.delete(post);
        }
        userRepository.delete(user);
        return "redirect:/user/index";
    }

    @PostMapping("/user/editUser")
    public String blogUserEdit(@RequestParam long id,
                               @RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateBirth) {
        User user = userRepository.findById(id).get();
        user.setName(name);
        user.setSurname(surname);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setDateBirth(dateBirth);
        userRepository.save(user);
        return "redirect:/user/index";
    }

    @PostMapping("/user/edit")
    public String postEdit(@RequestParam long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "Users/Edit";
    }
}