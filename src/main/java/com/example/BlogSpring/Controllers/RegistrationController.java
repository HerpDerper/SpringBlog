package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Role;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public String blogUserCreate(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        User userDB = userRepository.findUserByUsername(user.getUsername());
        if (userDB != null) {
            return "Users/Create";
        }
        if (bindingResult.hasErrors()) return "Users/Create";
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String userCreate(@ModelAttribute("user") User user) {
        return "Users/Create";
    }
}
