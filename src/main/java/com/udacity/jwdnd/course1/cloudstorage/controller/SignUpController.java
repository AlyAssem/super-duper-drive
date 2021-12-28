package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("signup")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupErr = null;

        if (!userService.isUserNameAvailable(user.getUsername())) {
            signupErr = "The username already exists";
        }

        if (signupErr == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupErr = "There was an error signing you up, Please try again.";
            }
        }

        if (signupErr == null) {
            model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("registeredSuccessfullyMessage", "Signed up successfully");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupErr);
            return "signup";
        }
    }
}
