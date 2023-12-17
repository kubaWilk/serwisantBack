package com.jakubwilk.serwisant.api.entity.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="files")
public class InDbFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_type")
    private String fileType;

    @Lob
    @Column(name="data")
    private byte[] data;

    @Column(name="repair_id")
    private int repairId;

    public InDbFile(String filename, String contentType, int repairId, byte[] bytes) {
        this.fileName = filename;
        this.fileType = contentType;
        this.repairId = repairId;
        this.data = bytes;
    }
}



