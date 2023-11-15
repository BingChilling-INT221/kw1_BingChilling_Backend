package sit.int221.sas.sit_announcement_system_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.sas.sit_announcement_system_backend.service.FileService;

@CrossOrigin
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }



    @GetMapping("/{filename:.+}")
    @ResponseBody
// เวลาเรียกชื่อไฟล์ เรียกพร้อมนามสกุล และ เป็น inSensitiveCase
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);

        if (file.exists()) {
            // Set Content-Disposition header to trigger download prompt
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);

            // Set the content type as application/octet-stream for binary data
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Return the file content along with headers in a ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(file);
        } else {
            // If the file does not exist, you may return an appropriate HTTP status
            return ResponseEntity.notFound().build();
        }

    }



    @PostMapping("")
    //    parameter ที่ส่งใน body post man ต้องเป็น ชื่อเดียวกับใน code ที่เรารับมา
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        fileService.store(file);
        return "You successfully uploaded " + file.getOriginalFilename() + "!";
    }





}

