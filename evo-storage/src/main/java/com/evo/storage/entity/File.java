package com.evo.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String extensionType;

    @Column(nullable = false, unique = true)
    private String storageFileName;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String accessType;

    @Column
    private String fileSize;

    @Column
    private String MIMEType;

}
