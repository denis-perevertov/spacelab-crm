package com.example.spacelab.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public interface FilenameUtils {

    static String getExtension(File file) {
        if(file == null) return "";
        else {
            String filename = file.getName();
            if(!filename.isEmpty() && filename.lastIndexOf(".") != -1) {
                return filename.substring(filename.lastIndexOf(".")+1);
            }
            else return "";
        }
    }

    // todo redo
    static String getExtension(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()) return "";
        else {
            String filename = multipartFile.getOriginalFilename();
            if(filename != null && !filename.isEmpty()) {
                if(filename.split("\\.").length > 0) {
                    return filename.split("\\.")[1];
                }
                else return "";
            }
            else return "";

        }
    }

    static String generateFileName(MultipartFile multipartFile) {
        return String.join(
                "-",
                UUID.randomUUID().toString(),
                Objects.requireNonNull(multipartFile.getOriginalFilename()).replaceAll("[-,.!$]", "_")
        );
    }

    static String trimNameString(String string) {
        return string.trim().replaceAll("[- ,.]", "_");
    }



}
