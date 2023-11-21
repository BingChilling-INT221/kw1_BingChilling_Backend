package sit.int221.sas.sit_announcement_system_backend.controller;


import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.sas.sit_announcement_system_backend.DTO.files.FileDTO;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.FileException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public AnnouncementService announcementService ;


    @GetMapping("/{id}")
    public List<FileDTO> serveFilesById(@PathVariable String id) throws IOException {
        return fileService.loadAllFilesAsResource(id);
    }


    @GetMapping("/{id}/{filename:.+}")
    @ResponseBody
// เวลาเรียกชื่อไฟล์ เรียกพร้อมนามสกุล และ เป็น inSensitiveCase
    public ResponseEntity<Resource> serveFile(@PathVariable String id,@PathVariable String filename) throws FileException {
        Resource file = fileService.loadFileAsResource(id,filename);


        if (file.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(file);
        } else {
            // If the file does not exist, you may return an appropriate HTTP status
           throw new  NotfoundByfield("Not found Path or filename as you want.","file");
        }

    }


    @PostMapping("/{id}")
    //    parameter ที่ส่งใน body post man ต้องเป็น ชื่อเดียวกับใน code ที่เรารับมา
    public String fileUpload(@RequestParam("file") MultipartFile [] file,@PathVariable String id) throws FileException {
        fileService.store(id,file);
        return "You successfully uploaded " + Arrays.stream(file).map(MultipartFile::getOriginalFilename).collect(Collectors.joining(" , "))+ " already !";
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFiles(@RequestParam("file") MultipartFile[] files, @PathVariable String id,@RequestParam("oldFile") String [] oldFile) throws FileException {
        fileService.updateFiles(id,files,oldFile);
        return ResponseEntity.status(HttpStatus.OK).body("Can update file successfully") ;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteFolderById(@PathVariable String id) throws FileException {
        fileService.deleteFolderById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Could delete all file of announcement successfully") ;
    }

    @DeleteMapping("/{id}/{filename:.+}")
    public ResponseEntity<String> DeleteFileById(@PathVariable String id, @PathVariable String filename) throws FileException {
        fileService.deleteFileById(id,filename);
        return ResponseEntity.status(HttpStatus.OK).body("Can delete file successfully") ;
    }


}

