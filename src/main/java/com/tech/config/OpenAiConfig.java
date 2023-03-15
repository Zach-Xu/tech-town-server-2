package com.tech.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "openai")
@Component
@Getter
@Setter
public class OpenAiConfig {

    private String secretKey;
    private String model;
    private String prompt;
    private Integer temperature;
    private Integer max_tokens;
    private Boolean stream;
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

}
