package com.example.spacelab.service;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Hidden
public interface FileService {

    void saveFile(MultipartFile file, String filename, String... directories) throws IOException;
    File getFile(String filename, String... directiories) throws IOException;
    void deleteFile(String filename, String... directiories) throws IOException;
    boolean fileExists(String filename);

}
