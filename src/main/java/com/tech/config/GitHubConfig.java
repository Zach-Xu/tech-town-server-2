package com.tech.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "github")
@Component
@Getter
@Setter
public class GitHubConfig {

    private String client_id;

    private String client_secret;

}
