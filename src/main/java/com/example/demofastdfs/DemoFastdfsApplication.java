package com.example.demofastdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoFastdfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoFastdfsApplication.class, args);
    }

}
