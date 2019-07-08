package me.alwaysawake.eventservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean // DTO -> 도메인 객체로 값 복사
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
