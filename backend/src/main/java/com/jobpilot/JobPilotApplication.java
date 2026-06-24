package com.jobpilot;

import com.jobpilot.config.properties.AiProperties;
import com.jobpilot.config.properties.EmbeddingProperties;
import com.jobpilot.config.properties.JwtProperties;
import com.jobpilot.config.properties.QdrantProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.jobpilot")
@EnableConfigurationProperties({
        AiProperties.class,
        EmbeddingProperties.class,
        JwtProperties.class,
        QdrantProperties.class
})
public class JobPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobPilotApplication.class, args);
    }
}
