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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

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

    @GetMapping("/post/details")
    public String getSelectedPost(@RequestParam long id, Model model) {
        Post post = postRepository.findById(id).get();
        List<Comment> comments = commentRepository.findByPost(post);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        return "Posts/Details";
    }

    @PostMapping("/post/create")
    public String blogPostCreate(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String text,
                                 @RequestParam Long userId) {
        User user = userRepository.findById(userId).get();
        Post post = new Post(title, description, text, 0, new Date(), user);
        postRepository.save(post);
        return "redirect:/post/index";
    }

    @PostMapping("/post/edit")
    public String postEdit(@RequestParam long id, Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);
        return "Posts/Edit";
    }

    @PostMapping("/post/editPost")
    public String blogPostEdit(@RequestParam long id,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String text,
                               @RequestParam Long userId) {
        Post post = postRepository.findById(id).get();
        User user = userRepository.findById(userId).get();
        post.setTitle(title);
        post.setDescription(description);
        post.setText(text);
        post.setUser(user);
        postRepository.save(post);
        return "redirect:/post/index";
    }

    @PostMapping("/post/delete")
    public String postDelete(@RequestParam long id) {
        Post post = postRepository.findById(id).get();
        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
        postRepository.delete(post);
        return "redirect:/post/index";
    }

    @GetMapping("/post/index")
    public String postFilter(@RequestParam(required = false) String description,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<Post> posts;
        if (description != null && !description.equals("")) {
            if (exactSearch != null && exactSearch) posts = postRepository.findByDescription(description);
            else posts = postRepository.findByDescriptionContains(description);
        } else posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "Posts/Index";
    }
}