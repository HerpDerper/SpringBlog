package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    public UserController(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String userIndex(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Users/Index";
    }

    @GetMapping("/user/create")
    public String userCreate(@ModelAttribute("user") User user) {
        return "Users/Create";
    }

    @PostMapping("/user/create")
    public String blogUserCreate(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "Users/Create";
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
        model.addAttribute("user", userRepository.findById(id).get());
        return "Users/Details";
    }

    @PostMapping("/user/delete")
    public String userDelete(@RequestParam long id) {
        User user = userRepository.findById(id).get();
        List<Comment> comments = commentRepository.findByUser(user);
        List<Post> posts = postRepository.findByUser(user);
        commentRepository.deleteAll(comments);
        postRepository.deleteAll(posts);
        userRepository.delete(user);
        return "redirect:/user/index";
    }

    @PostMapping("/user/editUser")
    public String blogUserEdit(@ModelAttribute("user") @Valid User userValid, BindingResult bindingResult) {
        User user = userRepository.findById(userValid.getId()).get();
        if (bindingResult.hasErrors()) return "/Users/Edit";
        user.setName(userValid.getName());
        user.setSurname(userValid.getSurname());
        user.setLastName(userValid.getLastName());
        user.setEmail(userValid.getEmail());
        user.setPassword(userValid.getPassword());
        user.setDateBirth(userValid.getDateBirth());
        userRepository.save(user);
        return "redirect:/user/index";
    }

    @PostMapping("/user/edit")
    public String userEdit(@RequestParam long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "Users/Edit";
    }
}