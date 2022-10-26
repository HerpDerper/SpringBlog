package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Checking;
import com.example.BlogSpring.Models.Comment;
import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.CommentRepository;
import com.example.BlogSpring.Repo.PostRepository;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/post/index")
    public String postIndex(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        return "Posts/Index";
    }

    @GetMapping("/post/create")
    public String postCreate(@ModelAttribute("post") Post post, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        return "Posts/Create";
    }

    @PostMapping("/post/details")
    public String getSelectedPost(@ModelAttribute("comment") Comment comment, @RequestParam long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Post post = postRepository.findById(id).get();
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("post", post);
        model.addAttribute("username", auth.getName());
        User user = userRepository.findUserByUsername(auth.getName());
        model.addAttribute("user", user);
        return "Posts/Details";
    }

    @PostMapping("/post/create")
    public String blogPostCreate(@ModelAttribute("post") @Valid Post postValid, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        if (bindingResult.hasErrors()) {
            return "Posts/Create";
        }
        Post post = new Post(postValid.getTitle(), postValid.getDescription(), postValid.getText(), 0, new Date(), userRepository.findUserByUsername(auth.getName()));
        postRepository.save(post);
        return "redirect:/post/index";
    }

    @PostMapping("/post/edit")
    public String postEdit(@RequestParam long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);
        return "Posts/Edit";
    }

    @PostMapping("/post/editPost")
    public String blogPostEdit(@ModelAttribute("comment") Comment comment, @ModelAttribute("post") @Valid Post postValid, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Post post = postRepository.findById(postValid.getId()).get();
        if (bindingResult.hasErrors()) {
            return "Posts/Edit";
        }
        post.setTitle(postValid.getTitle());
        post.setDescription(postValid.getDescription());
        post.setText(postValid.getText());
        postRepository.save(post);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentRepository.findByPost(post));
        model.addAttribute("username", auth.getName());
        return "Posts/Details";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        Iterable<Post> posts;
        if (description != null && !description.equals("")) {
            if (exactSearch != null && exactSearch) posts = postRepository.findByDescription(description);
            else posts = postRepository.findByDescriptionContains(description);
        } else posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "Posts/Index";
    }

    @PostMapping("/post/likePost")
    public String blogPostLike(@ModelAttribute("comment") Comment comment, @RequestParam long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminAccess", new Checking().adminAccess(auth));
        model.addAttribute("username", auth.getName());
        User user = userRepository.findUserByUsername(auth.getName());
        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("comments", commentRepository.findByPost(post));
        if (post.getLikedUsers().contains(user)) {
            post.setLikeCount(post.getLikeCount() - 1);
            post.getLikedUsers().remove(user);
        } else {
            post.setLikeCount(post.getLikeCount() + 1);
            post.getLikedUsers().add(user);
        }
        postRepository.save(post);
        return "Posts/Details";
    }
}