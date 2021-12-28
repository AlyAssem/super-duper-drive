package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }


    public boolean hasFileBeenUploadedByUser(String fileName, int userId) {
        File file = fileMapper.getFileByNameAndUser(fileName, userId);
        return file != null;
    }

    public void uploadFile(MultipartFile uploadedFile, int userId) throws IOException {
        File file = new File();

        try {
            file.setContentType(uploadedFile.getContentType());
            file.setFileData(uploadedFile.getBytes());
            file.setFileName(uploadedFile.getOriginalFilename());
            file.setFileSize(Long.toString(uploadedFile.getSize()));
            file.setUserId(userId);
        } catch(IOException e) {
            throw e;
        }
        fileMapper.addFile(file);
    }

    public File getFile(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public List<File> getFiles() {
        return fileMapper.getAllFiles();
    }

    public List<File> getUserFiles(int userId) {
        return fileMapper.getUserFiles(userId);
    }

    public int deleteFile (int fileId) {
        return fileMapper.deleteFile(fileId);
    }

}
