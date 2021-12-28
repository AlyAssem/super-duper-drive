package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES")
    List<File> getAllFiles();

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getUserFiles(int userId);

    @Select("SELECT * FROM FILES WHERE FileId = #{fileId}")
    File getFileById(int fileId);

    @Select("SELECT * FROM FILES WHERE FileName = #{fileName} AND UserId = #{userId}")
    File getFileByNameAndUser(String fileName, int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " + "VALUES(#{fileName}," +
            "#{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void addFile(File file);

    @Delete("DELETE FROM FILES WHERE FileId = #{fileId}")
    int deleteFile(int fileId);
}



