package edu.hrbust.cheatreview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.hrbust.cheatreview.mapper")
public class CheatReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheatReviewApplication.class, args);
    }

}
