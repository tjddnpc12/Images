package com.example.images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)

public class ImagesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagesApplication.class, args);
    }

}
