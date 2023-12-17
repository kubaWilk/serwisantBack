package com.jakubwilk.serwisant.api.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class ApplicationPropertiesUtility {
    @Value("${api.front.url}")
    private String frontUrl;

    @Value("${api.file-storage.directory}")
    private String fileStorageDirectory;
}
