package com.emaflores.file.repository;

import com.emaflores.file.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {

    @Query("SELECT f FROM FileModel f WHERE f.hashSha256 = :hash OR f.hashSha512 = :hash")
    FileModel findByHash(@Param("hash") String hash);

    @Query("SELECT f FROM FileModel f WHERE (f.hashSha256 = :hash AND 'SHA-256' = :hashType) OR (f.hashSha512 = :hash AND 'SHA-512' = :hashType)")
    FileModel findByHashTypeAndHash(@Param("hashType") String hashType, @Param("hash") String hash);

}


