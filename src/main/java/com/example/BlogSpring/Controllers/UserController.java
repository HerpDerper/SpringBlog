package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.ContactData;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.ContactDataRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class UserController {
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final ContactDataRepository contactDataRepository;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository, ContactDataRepository contactDataRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.contactDataRepository = contactDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String userIndex(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("adminAccess", true);
        return "Users/Index";
    }

    @GetMapping("/user/index")
    public String userFilter(@RequestParam(required = false) String login,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<User> users;
        model.addAttribute("adminAccess", true);
        if (login != null && !login.equals("")) {
            if (exactSearch != null && exactSearch) users = userRepository.findByUsername(login);
            else users = userRepository.findByUsernameContains(login);
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
        if (user.getContactData() != null) {
            ContactData contactData = contactDataRepository.findById(user.getContactData().getId()).get();
            contactDataRepository.delete(contactData);
        }
        return "redirect:/user/index";
    }

    @PostMapping("/user/editUser")
    public String blogUserEdit(@ModelAttribute("user") @Valid User userValid, BindingResult bindingResult, Model model) {
        model.addAttribute("adminAccess", true);
        User user = userRepository.findById(userValid.getId()).get();
        if (bindingResult.hasErrors()) return "/Users/Edit";
        user.setName(userValid.getName());
        user.setSurname(userValid.getSurname());
        user.setLastName(userValid.getLastName());
        user.setUsername(userValid.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDateBirth(userValid.getDateBirth());
        userRepository.save(user);
        return "redirect:/user/index";
    }

    @PostMapping("/user/edit")
    public String userEdit(@RequestParam long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("adminAccess", true);
        model.addAttribute("user", user);
        return "Users/Edit";
    }
}