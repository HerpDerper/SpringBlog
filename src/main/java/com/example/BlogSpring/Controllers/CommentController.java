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

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/comment/create")
    public String commentCreate(@RequestParam Long postId, Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        return "Comments/Create";
    }

    @PostMapping("/comment/createComment")
    public String blogCommentCreate(@RequestParam Long postId,
                                    @RequestParam String text,
                                    @RequestParam Long userId) {
        User user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        Comment comment = new Comment(text, 0, new Date(), user, post);
        commentRepository.save(comment);
        return "redirect:/post/details?id=" + postId;
    }

    @PostMapping("/comment/editComment")
    public String blogCommentEdit(@RequestParam Long id,
                                  @RequestParam Long postId,
                                  @RequestParam String text,
                                  @RequestParam Long userId) {
        User user = userRepository.findById(userId).get();
        Comment comment = commentRepository.findById(id).get();
        comment.setText(text);
        comment.setUser(user);
        commentRepository.save(comment);
        return "redirect:/post/details?id=" + postId;
    }

    @PostMapping("/comment/edit")
    public String commentEdit(@RequestParam Long postId,
                              @RequestParam Long id, Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        Comment comment = commentRepository.findById(id).get();
        model.addAttribute("comment", comment);
        return "Comments/Edit";
    }

    @PostMapping("/comment/details")
    public String getSelectedComment(@RequestParam long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        model.addAttribute("comment", comment);
        model.addAttribute("post", comment.getPost());
        return "Comments/Details";
    }

    @PostMapping("/comment/delete")
    public String commentDelete(@RequestParam long id,
                                @RequestParam long postId) {
        Comment comment = commentRepository.findById(id).get();
        commentRepository.delete(comment);
        return "redirect:/post/details?id=" + postId;
    }
}