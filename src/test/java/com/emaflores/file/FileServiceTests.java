package com.emaflores.file;

import com.emaflores.file.model.FileModel;
import com.emaflores.file.repository.FileRepository;
import com.emaflores.file.service.FileService;
import com.emaflores.file.utils.HashUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    private final String SHA256 = "SHA-256";
    private final String SHA512 = "SHA-512";

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        FileModel fileModel = fileService.uploadFile(file, SHA256);
        assertNotNull(fileModel.getId());
        assertEquals("test.txt", fileModel.getFileName());
        assertNotNull(fileModel.getHashSha256());
        assertNull(fileModel.getHashSha512());
    }

    @Test
    public void testGetAllFiles() throws Exception {
        List<FileModel> files = fileService.getAllFiles();
        assertTrue(files.size() >= 1);
    }

    @Test
    public void testGetFileByHash() throws Exception {
        String hash = HashUtils.hash("Hello, World!".getBytes(), SHA256);
        FileModel fileModel = fileService.getFileByHash(SHA256, hash);
        assertEquals("test.txt", fileModel.getFileName());
        assertNotNull(fileModel.getLastUpload());
    }

    @Test
    public void testGetFileByHashNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            fileService.getFileByHash(SHA256, "invalid-hash");
        });
    }

    @Test
    public void testUpdateFileByHash() throws Exception {
        String hash = HashUtils.hash("Hello, World!".getBytes(), SHA256);
        MockMultipartFile file = new MockMultipartFile("file", "test-updated.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World! Updated".getBytes());
        FileModel fileModel = fileService.updateFileByHash(SHA256, hash, file);
        assertEquals("test-updated.txt", fileModel.getFileName());
        assertNotNull(fileModel.getLastUpload());
    }

    @Test
    public void testUpdateFileByHashNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        assertThrows(ResponseStatusException.class, () -> {
            fileService.updateFileByHash(SHA256, "invalid-hash", file);
        });
    }

    @Test
    public void testDeleteFileByHash() throws Exception {
        String hash = HashUtils.hash("Hello, World! Updated".getBytes(), SHA256);
        fileService.deleteFileByHash(SHA256, hash);
        assertNull(fileRepository.findByHash(hash));
    }

    @Test
    public void testDeleteFileByHashNotFound() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            fileService.deleteFileByHash(SHA256, "invalid-hash");
        });
    }

}

