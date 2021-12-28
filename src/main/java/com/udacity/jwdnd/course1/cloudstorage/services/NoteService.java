package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getUserNotes(int userId) {
        return noteMapper.getUserNotes(userId);
    }

    public void addNote(Note note, int userId) {
        Note newNote = new Note();
        newNote.setNoteTitle(note.getNoteTitle());
        newNote.setNoteDescription(note.getNoteDescription());
        newNote.setUserId(userId);

        noteMapper.addNote(newNote);
    }

    public int deleteNote (int noteId) {
        return noteMapper.deleteNote(noteId);
    }

    public void updateNote (Note note) {
        noteMapper.updateNote(note);
    }


}
