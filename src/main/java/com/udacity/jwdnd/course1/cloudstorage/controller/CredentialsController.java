package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/home/credentials")
public class CredentialsController {
    private final UserMapper userMapper;
    private final CredentialsService credentialsService;
    private final EncryptionService encryptionService;

    public CredentialsController(UserMapper userMapper, CredentialsService credentialsService, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String addOrEditCredentials(@ModelAttribute("credentials") Credentials credentials, Authentication authentication, Model model) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        int userId = user.getUserId();


        if (credentials.getCredentialId() == null) {
            credentialsService.addCredentials(credentials, userId);
        }
        else {
            credentialsService.updateCredentials(credentials);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredentials(@RequestParam("id") Integer credentialId, RedirectAttributes redirectAttributes) {
        if (credentialId > 0) {
            credentialsService.deleteCredentials(credentialId);
            return "redirect:/result?success";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "was not able to delete the credential.");
        return "redirect:/result?error";
    }

}
