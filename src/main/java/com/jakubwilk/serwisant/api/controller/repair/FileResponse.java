package com.jakubwilk.serwisant.api.controller.repair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String downloadUrl;
    private String fileType;
    private long fileSize;
}
