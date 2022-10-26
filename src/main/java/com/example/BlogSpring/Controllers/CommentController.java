package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Checking;
import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Post post = postRepository.findById(postId).get();
        model.addAttribute("post", post);
        if (bindingResult.hasErrors()) {
            model.addAttribute("comments", commentRepository.findByPost(post));
            return "Posts/Details";
        }
        Comment comment = new Comment(commentValid.getText(), 0, new Date(), userRepository.findUserByUsername(auth.getName()), postRepository.findById(postId).get());
        commentRepository.save(comment);
        commentValid.setText("");
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("username", auth.getName());
        return "Posts/Details";
    }

    @PostMapping("/comment/editComment")
    public String blogCommentEdit(@ModelAttribute("comment") @Valid Comment commentValid, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        if (bindingResult.hasErrors()) {
            return "Comments/Edit";
        }
        Comment comment = commentRepository.findById(commentValid.getId()).get();
        comment.setText(commentValid.getText());
        commentRepository.save(comment);
        commentValid.setText("");
        Post post = postRepository.findById(comment.getPost().getId()).get();
        model.addAttribute("post", post);
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("username", auth.getName());
        return "Posts/Details";
    }

    @PostMapping("/comment/edit")
    public String commentEdit(@RequestParam long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        model.addAttribute("comment", comment);
        return "Comments/Edit";
    }

    @PostMapping("/comment/details")
    public String getSelectedComment(@RequestParam long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        model.addAttribute("username", auth.getName());
        model.addAttribute("comment", comment);
        model.addAttribute("post", comment.getPost());
        return "Comments/Details";
    }

    @PostMapping("/comment/delete")
    public String commentDelete(@RequestParam long id, Model model, @ModelAttribute("comment") Comment commentValid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Comment comment = commentRepository.findById(id).get();
        commentRepository.delete(comment);
        Post post = postRepository.findById(comment.getPost().getId()).get();
        model.addAttribute("post", post);
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("username", auth.getName());
        return "Posts/Details";
    }
}