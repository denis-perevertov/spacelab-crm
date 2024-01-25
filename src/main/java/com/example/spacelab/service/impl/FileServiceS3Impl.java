package com.example.spacelab.service.impl;

import com.example.spacelab.service.FileService;
import com.example.spacelab.service.s3.S3Client;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service(value = "s3")
@Primary
@RequiredArgsConstructor
public class FileServiceS3Impl implements FileService {

    private final S3Client s3Client;

    @Override
    public void saveFile(MultipartFile multipartFile, String filename, String... directories) throws IOException {
        File file = new File(filename);
        log.info("trying to save file: {}", file.getName());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }

        String uploadDirectory = String.join("/", directories);
        String uploadPath = uploadDirectory + "/" + filename;

        log.info("path to file in bucket: {}", uploadPath);
        s3Client.uploadFile(file, uploadPath);
        log.info("file uploaded to s3 bucket");

        deleteFileLocal(filename);
    }

    @Override
    public File getFile(String filename, String... directories) throws IOException {
        log.info("getting file by name: {}", filename);

        File cachedFile = new File("tmp/" + filename);
        if(cachedFile.exists()) {
            log.info("file with this name is cached in tmp directory, returning");
            return cachedFile;
        }
        else {
            log.info("fetching file from s3 bucket");
            String downloadDirectory = String.join("/", directories);
            String downloadPath = downloadDirectory + "/" + filename;

            log.info("path to file in bucket: {}", downloadPath);

            File file = s3Client.downloadFile(downloadPath);
            if(file != null) log.info("received file");
            return file;
        }
    }

    @Override
    public boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    @Override
    public void deleteFile(String filename, String... directiories) throws IOException {
        log.info("deleting file: {}", filename);
        s3Client.deleteFile(filename);
    }

    private void deleteFileLocal(String filename) {
        File file = new File(filename);
        if(file.exists()) {
            if(file.delete()) {
                log.info("deleted file from local");
            }
            else {
                log.warn("could not delete file from local");
            }
        }
        else {
            log.warn("file not found");
        }
    }

    @Scheduled(fixedRate = 1000*60*60)   // every 1 hr
    public void clearTmpDirectory() {
        log.info(" --- clearing tmp directory --- ");
        File tmpDirectory = new File("tmp");
        File[] files = tmpDirectory.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                clearFolderContent(file);
            }
            else file.delete();
        }
    }

    public void clearFolderContent(File folder) {
        File[] files = folder.listFiles();
        for(File f: files) {
            if(f.isDirectory()) {
                clearFolderContent(f);
            } else {
                f.delete();
            }
        }
        folder.delete();
    }


    private String generateFileName(MultipartFile multiPart) {
        return LocalDateTime.now() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
