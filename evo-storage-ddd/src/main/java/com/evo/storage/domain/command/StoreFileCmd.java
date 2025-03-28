package com.evo.storage.domain.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreFileCmd {
    private int UUID;
    private String originName;
    private String fileType;
    private Long fileSize;
    private String description;
    private String url;
    private Boolean isPublic;
}
