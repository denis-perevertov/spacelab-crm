package com.example.spacelab.service.impl;

import com.example.spacelab.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    static final String FILE_UPLOAD_DIRECTORY = "uploads";

    @Override
    public void saveFile(MultipartFile file, String... directories) throws IOException {
        if(file != null && file.getSize() > 0) {
            StringBuilder sb = new StringBuilder(FILE_UPLOAD_DIRECTORY).append('/');
            for(String directory : directories) {
                sb.append(directory).append('/');
            }
            Path directoryPath = Paths.get(FILE_UPLOAD_DIRECTORY, directories);
            log.info(directoryPath.toString());
            File f = new File(directoryPath.toString());
            if(!f.exists()) {
                if(f.mkdirs()) {
                    log.info("created directories");
                    Path pathToSave = directoryPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                    file.transferTo(pathToSave);
                }
                else {
                    log.error("error creating directories");
                }
            }
            else {
                log.info("directory path already exists");
                Path pathToSave = directoryPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                file.transferTo(pathToSave);
            }
        }
    }

    @Override
    public File getFile(String fileName, String... directories) throws IOException {
        StringBuilder sb = new StringBuilder(FILE_UPLOAD_DIRECTORY).append('/');
        for(String directory : directories) {
            sb.append(directory).append('/');
        }
        Path directoryPath = Paths.get(FILE_UPLOAD_DIRECTORY, directories);
        Path filePath = directoryPath.resolve(fileName);
        log.info(directoryPath.toString());
        log.info(filePath.toString());

        File f = new File(filePath.toString());
        if(f.exists()) {
            return f;
        }
        else {
            throw new FileNotFoundException("File not found!");
        }
    }

    @Override
    public boolean fileExists(String... directories) {
        return false;
    }

}
