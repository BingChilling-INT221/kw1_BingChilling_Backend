package sit.int221.sas.sit_announcement_system_backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.sas.sit_announcement_system_backend.DTO.AnnouncementsResponseDTO;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("api/announcer/announcements")
@CrossOrigin(origins = {
        "http://intproj22.sit.kmutt.ac.th",
        "https://intproj22.sit.kmutt.ac.th",
        "http://localhost:5173",
        "https://localhost:5173",
        "http://ip22kw1.sit.kmutt.ac.th",
        "https://ip22kw1.sit.kmutt.ac.th",
        "http://ip22kw1.sit.kmutt.ac.th:800",
        "https://ip22kw1.sit.kmutt.ac.th:800",
})
@Validated
public class AnnouncerAnnController {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AnnouncementService announcementService;
    @GetMapping("")
    public ResponseEntity<List<AnnouncementsResponseDTO>> getAnnouncements() {
        return ResponseEntity.ok(listMapper.mapList(announcementService.getAnnouncementsByOwnerName("admin"), AnnouncementsResponseDTO.class, modelMapper));
    }
}
