package com.emaflores.file.controller;

import com.emaflores.file.model.FileModel;
import com.emaflores.file.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    private final String SHA256 = "SHA-256";
    private final String SHA512 = "SHA-512";

    // Endpoint para subir uno o mas archivos
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload one or more files")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Files uploaded successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("files") MultipartFile[] files,
                                                          @RequestParam(value = "hashType", defaultValue = SHA256) String hashType) throws Exception {
            List<FileModel> uploadedFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                FileModel uploadedFile = fileService.uploadFile(file, hashType);
                uploadedFiles.add(uploadedFile);
            }

            List<Map<String, Object>> documents = new ArrayList<>();
            for (FileModel fileModel : uploadedFiles) {
                Map<String, Object> document = new HashMap<>();
                document.put("fileName", fileModel.getFileName());
                document.put("hashType", hashType.equals(SHA256) ? fileModel.getHashSha256() : fileModel.getHashSha512());
                document.put("lastUpload", fileModel.getLastUpload());
                documents.add(document);
            }

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("algorithm", hashType);
            responseBody.put("documents", documents);

            return ResponseEntity.created(new URI("/api/documents")).body(responseBody);
    }

    // Endpoint para traer todos los archivos subidos
    @GetMapping("/documents")
    @ApiOperation(value = "Get all uploaded files")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Files retrieved successfully"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public List<FileModel> getAllFiles() {
        return fileService.getAllFiles();
    }

    // Endpoint para buscar un archivo por su hash
    @GetMapping("/document")
    @ApiOperation(value = "Get file by hash")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File retrieved successfully"),
            @ApiResponse(code = 404, message = "File not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getFileByHash(@RequestParam(value = "hashType", defaultValue = SHA256) String hashType,
                                                             @RequestParam("hash") String hash) throws Exception {
            FileModel fileModel = fileService.getFileByHash(hashType, hash);
            if (fileModel == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(buildResponse(fileModel, hashType));
    }

    // Endpoint para actualizar un archivo por su hash
    @PutMapping("/document")
    @ApiOperation(value = "Update file by hash")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File updated successfully"),
            @ApiResponse(code = 404, message = "File not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Object> updateFileByHash (@RequestParam(value = "hashType", defaultValue = SHA256) String hashType,
                                                   @RequestParam("hash") String hash,
                                                   @RequestParam("file") MultipartFile file) throws Exception {
            FileModel fileModel = fileService.updateFileByHash(hashType, hash, file);
            return ResponseEntity.ok(buildResponse(fileModel, hashType));
    }

    // Endpoint para deletear un archivo por su hash
    @DeleteMapping("/document")
    @ApiOperation(value = "Delete file by hash")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File deleted successfully"),
            @ApiResponse(code = 404, message = "File not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Object> deleteFileByHash(@RequestParam(value = "hashType", defaultValue = SHA256) String hashType,
                                                   @RequestParam("hash") String hash) throws Exception {
        fileService.deleteFileByHash(hashType, hash);
        return ResponseEntity.ok("El archivo fue eliminado exitosamente.");

    }

    private Map<String, Object> buildResponse(FileModel fileModel, String hashType){
        Map<String, Object> document = new HashMap<>();
        document.put("fileName", fileModel.getFileName());
        document.put("hash", hashType.equals(SHA256) ? fileModel.getHashSha256() : fileModel.getHashSha512());
        document.put("lastUpload", fileModel.getLastUpload());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("algorithm", hashType);
        responseBody.put("document", document);
        return responseBody;
    }
}
