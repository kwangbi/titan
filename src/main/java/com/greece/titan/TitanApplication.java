package com.greece.titan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class TitanApplication {

    public static void main(String[] args) {
        SpringApplication.run(TitanApplication.class, args);
    }

}
