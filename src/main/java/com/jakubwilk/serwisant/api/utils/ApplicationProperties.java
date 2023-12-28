package com.jakubwilk.serwisant.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "api.paths")
@ConfigurationPropertiesScan
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProperties {
    private String frontUrl;

    private String fileStorageDirectory;

    private String resources;

    private Path protocols;

    private String font;
}
