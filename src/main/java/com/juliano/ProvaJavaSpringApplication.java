package com.juliano;

import org.joinfaces.autoconfigure.javaxfaces.JsfBeansAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = {JsfBeansAutoConfiguration.class})
public class ProvaJavaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvaJavaSpringApplication.class, args);
    }

}
