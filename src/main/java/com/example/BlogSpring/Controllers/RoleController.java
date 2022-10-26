package com.example.BlogSpring.Controllers;

import com.example.BlogSpring.Models.Role;
import com.example.BlogSpring.Models.User;
import com.example.BlogSpring.Repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class RoleController {

    private final UserRepository userRepository;

    public RoleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/role/editRole")
    public String postIndex(@RequestParam long userId, @RequestParam long roleId, Model model) {
        model.addAttribute("adminAccess", true);
        User user = userRepository.findById(userId).get();
        user.getRoles().clear();
        if (roleId == 0) user.getRoles().add(Role.ADMIN);
        else user.getRoles().add(Role.USER);
        userRepository.save(user);
        return "redirect:/user/index";
    }

    @PostMapping("/role/edit")
    public String userEdit(@RequestParam long userId, Model model) {
        User user = userRepository.findById(userId).get();
        model.addAttribute("adminAccess", true);
        model.addAttribute("user", user);
        return "Roles/Edit";
    }
}