package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE UserId = #{userId}")
    List<Note> getUserNotes(int userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " + "VALUES(#{noteTitle}, #{noteDescription}," +
            " #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void addNote(Note note);

    @Delete("DELETE FROM NOTES WHERE NoteId = #{noteId}")
    int deleteNote(int noteId);

    @Update("UPDATE NOTES" +
            " SET notetitle = #{noteTitle}," +
            " notedescription = #{noteDescription}" +
            " WHERE noteid = #{noteId};")
    void updateNote(Note note);
}
