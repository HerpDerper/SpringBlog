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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("comment/")
    public String commentIndex(Model model) {
        Iterable<Comment> comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "Comments/Index";
    }

    @GetMapping("/comment/create")
    public String commentCreate(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "Comments/Create";
    }

    @PostMapping("/comment/create")
    public String blogCommentCreate(@RequestParam String text,
                                 @RequestParam Long userId,
                                 @RequestParam Long postId, Model model) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        Comment comment = new Comment(text, 0, new Date(), user, post);
        commentRepository.save(comment);
        return "redirect:/comment/index";
    }

    @GetMapping("/comment/index")
    public String commentFilter(@RequestParam(required = false) String text,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {
        Iterable<Comment> comments = new ArrayList<Comment>();
        if (text != null && !text.equals("")) {
            if (exactSearch != null && exactSearch)
                comments = commentRepository.findByText(text);
            else
                comments = commentRepository.findByTextContains(text);
        } else
            comments = commentRepository.findAll();
        model.addAttribute("comments", comments);
        return "Comments/Index";
    }
}
