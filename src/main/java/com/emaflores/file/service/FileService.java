package com.emaflores.file.service;

import com.emaflores.file.model.FileModel;
import com.emaflores.file.repository.FileRepository;
import com.emaflores.file.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final String SHA256 = "SHA-256";
    private final String SHA512 = "SHA-512";

    public FileModel uploadFile(MultipartFile file, String hash) throws Exception {
        if (!hash.equals(SHA256) && !hash.equals(SHA512)) {
            throw new IllegalArgumentException("El parámetro 'hash' solo puede ser 'SHA-256' o 'SHA-512'");
        }

        byte[] bytes = file.getBytes();
        String hashValue = HashUtils.hash(bytes, hash);

        FileModel fileModel = fileRepository.findByHash(hashValue);

        if (fileModel == null) {
            fileModel = new FileModel();
            fileModel.setFileName(file.getOriginalFilename());
            fileModel.setHashSha256(hash.equals(SHA256) ? hashValue : null);
            fileModel.setHashSha512(hash.equals(SHA512) ? hashValue : null);
            fileModel.setLastUpload(new Date());
            fileRepository.save(fileModel);
        } else {
            fileModel.setLastUpload(new Date());
            fileRepository.save(fileModel);
        }
        return fileModel;
    }

    public List<FileModel> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileModel getFileByHash(String hashType, String hash) {
        if (!hashType.equals(SHA256) && !hashType.equals(SHA512)) {
            throw new IllegalArgumentException("El parámetro 'hashType' solo puede ser 'SHA-256' o 'SHA-512'");
        }

        FileModel fileModel = fileRepository.findByHashTypeAndHash(hashType, hash);

        if (fileModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ningún archivo con el hash especificado");
        }

        return fileModel;
    }

    @Transactional
    public FileModel updateFileByHash(String hashType, String hash, MultipartFile file) throws Exception {
        if (!hashType.equals(SHA256) && !hashType.equals(SHA512)) {
            throw new IllegalArgumentException("El parámetro 'hashType' solo puede ser 'SHA-256' o 'SHA-512'");
        }

        byte[] bytes = file.getBytes();
        String hashValue = HashUtils.hash(bytes, hashType);

        FileModel fileModel = fileRepository.findByHashTypeAndHash(hashType, hash);

        if (fileModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ningún archivo con el hash especificado");
        }

        fileModel.setFileName(file.getOriginalFilename());
        if (hashType.equals(SHA256)) {
            fileModel.setHashSha256(hashValue);
        } else {
            fileModel.setHashSha512(hashValue);
        }
        fileModel.setLastUpload(new Date());
        fileRepository.save(fileModel);

        return fileModel;
    }

    public void deleteFileByHash(String hashType, String hash) {
        if (!hashType.equals(SHA256) && !hashType.equals(SHA512)) {
            throw new IllegalArgumentException("El parámetro 'hashType' solo puede ser 'SHA-256' o 'SHA-512'");
        }

        FileModel fileModel = fileRepository.findByHashTypeAndHash(hashType, hash);

        if (fileModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró ningún archivo con el hash especificado");
        }

        fileRepository.delete(fileModel);
    }
}


