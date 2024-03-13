package com.example.spacelab.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.spacelab.util.FilenameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Client {

    @Value("${application.aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3client;

    public void uploadFile(File file, String filename) throws IOException {

        PutObjectRequest request = new PutObjectRequest(bucketName, filename, file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/"+ FilenameUtils.getExtension(file));
        metadata.addUserMetadata("Title", "File Upload - " + filename);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        PutObjectResult result = s3client.putObject(request);
        log.info("Put result: {}", result.toString());
    }

    public File downloadFile(String filename) throws IOException {
        try {
            S3Object object = s3client.getObject(bucketName, filename);
            String tmp = Path.of("tmp").resolve(filename.substring(filename.lastIndexOf("/")+1)).toString();
            try (S3ObjectInputStream s3is = object.getObjectContent()) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(tmp)) {
                    byte[] read_buf = new byte[1024];
                    int read_len = 0;
                    while ((read_len = s3is.read(read_buf)) > 0) {
                        fileOutputStream.write(read_buf, 0, read_len);
                    }
                }
                File file = new File(tmp);
                if(file.exists()) {
                    return file;
                }
                else throw new FileNotFoundException("file not found!");
            }
        } catch (AmazonS3Exception ex) {
            log.error("Error during processing s3 objects: {}", ex.getMessage());
            return null;
        }
    }

    public void deleteFile(String filename) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, filename);
        s3client.deleteObject(request);
    }

    public void fileExists(String filename) {

    }

}
