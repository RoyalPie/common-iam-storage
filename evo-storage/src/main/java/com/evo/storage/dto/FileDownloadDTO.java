package com.evo.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDownloadDTO {
    private String fileName;
    private String fileType;
    private byte[] fileData;
}
