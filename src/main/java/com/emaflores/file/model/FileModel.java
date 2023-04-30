package com.emaflores.file.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files")
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "hash_sha256")
    private String hashSha256;

    @Column(name = "hash_sha512")
    private String hashSha512;

    @Column(name = "last_upload")
    private Date lastUpload;

    // getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }

    public String getHashSha512() {
        return hashSha512;
    }

    public void setHashSha512(String hashSha512) {
        this.hashSha512 = hashSha512;
    }

    public Date getLastUpload() {
        return lastUpload;
    }

    public void setLastUpload(Date lastUpload) {
        this.lastUpload = lastUpload;
    }
}

