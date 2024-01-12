package com.example.spacelab.controller.image;

import com.amazonaws.util.IOUtils;
import com.example.spacelab.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images/")
public class ImageController {

    private final FileService fileService;

    @GetMapping("{*filePath}")
    public ResponseEntity<?> getImage(@PathVariable String filePath) throws IOException {
        String path = filePath.substring(1, filePath.lastIndexOf("/"));
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        log.info("path: {}", path);
        log.info("file name: {}", fileName);

        File file = fileService.getFile(fileName, path);
        if(file != null) {
            return ResponseEntity.ok(IOUtils.toByteArray(new FileInputStream(file)));
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
