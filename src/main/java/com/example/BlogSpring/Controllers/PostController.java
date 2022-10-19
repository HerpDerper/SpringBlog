package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("post/")
    public String postIndex(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "Posts/Index";
    }

    @GetMapping("/post/create")
    public String postCreate(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Posts/Create";
    }

    @PostMapping("/post/create")
    public String blogPostCreate(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String text,
                                 @RequestParam Long userId, Model model) {
        User user = userRepository.findById(userId).get();
        Post post = new Post(title, description, text, 0,  new Date(), user);
        postRepository.save(post);
        return "redirect:/post/index";
    }

    @GetMapping("/post/index")
    public String postFilter(@RequestParam(required = false) String title,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<Post> posts = new ArrayList<Post>();
        if (title != null && !title.equals("")) {
            if (exactSearch != null && exactSearch)
                posts = postRepository.findByTitle(title);
            else
                posts = postRepository.findByTitleContains(title);
        } else
            posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "Posts/Index";
    }
}