package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class CommentController {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public CommentController(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/comment/create")
    public String blogCommentCreate(@RequestParam Long postId, @ModelAttribute("comment") @Valid Comment commentValid, BindingResult bindingResult, Model model) {
        Iterable<User> users = userRepository.findAll();
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        model.addAttribute("users", users);
        if (bindingResult.hasErrors()) {
            model.addAttribute("comments", commentRepository.findByPost(post));
            return "Posts/Details";
        }
        Comment comment = new Comment(commentValid.getText(), 0, new Date(), commentValid.getUser(), postRepository.findById(postId).get());
        commentRepository.save(comment);
        model.addAttribute("comments", commentRepository.findByPost(post));
        return "Posts/Details";
    }

    @PostMapping("/comment/editComment")
    public String blogCommentEdit(@ModelAttribute("comment") @Valid Comment commentValid, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Iterable<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "Comments/Edit";
        }
        Comment comment = commentRepository.findById(commentValid.getId()).get();
        comment.setUser(commentValid.getUser());
        comment.setText(commentValid.getText());
        commentRepository.save(comment);
        return "redirect:/post/index";
    }

    @PostMapping("/comment/edit")
    public String commentEdit(@RequestParam long id, Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
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
    public String commentDelete(@RequestParam long id, Model model, @ModelAttribute("comment") Comment commentValid) {
        Comment comment = commentRepository.findById(id).get();
        commentRepository.delete(comment);
        Iterable<User> users = userRepository.findAll();
        Post post = postRepository.findById(comment.getPost().getId()).get();
        model.addAttribute("post", post);
        model.addAttribute("users", users);
        model.addAttribute("comments", commentRepository.findByPost(post));
        return "Posts/Details";
    }
}