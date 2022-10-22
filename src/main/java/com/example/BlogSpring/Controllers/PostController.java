package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("post/")
    public String postIndex(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "Posts/Index";
    }

    @GetMapping("/post/create")
    public String postCreate(@ModelAttribute("post") Post post, Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "Posts/Create";
    }

    @PostMapping("/post/details")
    public String getSelectedPost(@ModelAttribute("comment") Comment comment, @RequestParam long id, Model model) {
        Post post = postRepository.findById(id).get();
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("post", post);
        return "Posts/Details";
    }

    @PostMapping("/post/create")
    public String blogPostCreate(@ModelAttribute("post") @Valid Post postValid, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Iterable<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "Posts/Create";
        }
        Post post = new Post(postValid.getTitle(), postValid.getDescription(), postValid.getText(), 0, new Date(), postValid.getUser());
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
    public String blogPostEdit(@ModelAttribute("post") @Valid Post postValid, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Iterable<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "Posts/Edit";
        }
        Post post = postRepository.findById(postValid.getId()).get();
        post.setTitle(postValid.getTitle());
        post.setDescription(postValid.getDescription());
        post.setText(postValid.getText());
        post.setUser(postValid.getUser());
        postRepository.save(post);
        return "redirect:/post/index";
    }

    @PostMapping("/post/delete")
    public String postDelete(@RequestParam long id) {
        Post post = postRepository.findById(id).get();
        List<Comment> comments = commentRepository.findByPost(post);
        commentRepository.deleteAll(comments);
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