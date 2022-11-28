package com.example.images;

import com.example.images.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StorageService {

    private String uploadPath;

    @Autowired
    public StorageService(FileStorageProperties fileStorageProperties){
        this.uploadPath = fileStorageProperties.getUploadDir();
    }

    private String getRandomStr(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println("random : " + generatedString);
        return generatedString;
    }

    public List<String> saveFiles(MultipartFile[] files, String postName) throws IOException {
        String randomStr = getRandomStr();
        List<String> fileNames = new ArrayList<>();
        for(MultipartFile file : files) {
            fileNames.add(randomStr + StringUtils.cleanPath(file.getOriginalFilename()));
        }
        Path uploadPath = Paths.get(this.uploadPath+"/"+postName);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("make dir : " + uploadPath.toString());
        }
        for(int i =0; i< files.length; i++) {
            try (InputStream inputStream = files[i].getInputStream()) {
                Path filePath = uploadPath.resolve(fileNames.get(i));
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileNames.get(i), ioe);
            }
        }
        return fileNames;
    }

    public String saveFile(MultipartFile file, String userName) throws IOException {

        String randomStr = getRandomStr();
        String fileName = randomStr + StringUtils.cleanPath(file.getOriginalFilename());

        Path uploadPath = Paths.get(this.uploadPath+"/"+userName);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public Resource loadFileAsResource(String userName, String fileName) {
        Path uploadPath = Paths.get(this.uploadPath+"/"+userName);
        try {
            Path filePath = uploadPath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                System.out.println("not");
                return null;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public void getImageUrl(String imageUrl) throws IOException {
        URL url = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            url = new URL(imageUrl);
            in = url.openStream();
            out = new FileOutputStream("/Users/joseong-u/Downloads/images/images/sungwoo/bstgrzblge테이블구성도.png");

            while (true) {
                int data = in.read();
                if (data == -1) {
                    break;
                }
                out.write(data);
            }
            in.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}