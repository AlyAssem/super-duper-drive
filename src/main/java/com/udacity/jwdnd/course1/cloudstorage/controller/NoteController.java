package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/notes")
public class NoteController {
    private final UserMapper userMapper;
    private final NoteService noteService;

    public NoteController(UserMapper userMapper, NoteService noteService) {
        this.userMapper = userMapper;
        this.noteService = noteService;
    }

    @PostMapping
    public String addOrEditNote(@ModelAttribute("note") Note note, Authentication authentication) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        int userId = user.getUserId();

        if (note.getNoteId() == null) {
            noteService.addNote(note, userId);
        } else {
            noteService.updateNote(note);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") int noteId, RedirectAttributes redirectAttributes) {
        if (noteId > 0) {
            noteService.deleteNote(noteId);
            return "redirect:/result?success";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "was not able to delete the note.");
        return "redirect:/result?error";
    }
}
