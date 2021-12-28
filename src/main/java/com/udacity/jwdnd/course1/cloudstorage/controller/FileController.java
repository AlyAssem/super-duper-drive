package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home/files")
public class FileController {
    private UserMapper userMapper;
    private FileService fileService;

    public FileController(UserMapper userMapper, FileService fileService) {
        this.userMapper = userMapper;
        this.fileService = fileService;
    }

    @PostMapping
    public String handleFileUpload(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, RedirectAttributes redirectAttributes) throws IOException {
        String error = null;
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        int userId = user.getUserId();


        if (fileService.hasFileBeenUploadedByUser(fileUpload.getOriginalFilename(), userId)){
            error = "file has already been uploaded before";
        }

        if (fileUpload.isEmpty()) {
            error = "Please do not upload an empty file.";
        }

        if (error != null) {
            redirectAttributes.addFlashAttribute("errorMessage", error);
            return "redirect:/result?error";
        }

        fileService.uploadFile(fileUpload,userId);
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(Authentication authentication,@RequestParam("id") int fileId,  RedirectAttributes redirectAttributes) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        int userId = user.getUserId();

        if (fileId > 0) {
            fileService.deleteFile(fileId);
            return "redirect:/result?success";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "was not able to delete the file.");
        return "redirect:/result?error";
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> getFile(@RequestParam("id") int fileId) {
        File file = fileService.getFile(fileId);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }
}
