package sit.int221.sas.sit_announcement_system_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.sas.sit_announcement_system_backend.DTO.files.FileDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.FileException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.properties.FileStorageProperties;
import sit.int221.sas.sit_announcement_system_backend.repository.AnnouncementRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileService {
    @Autowired
    private AnnouncementRepository announcementRepository ;
    private Path fileStorageLocation ;

    private Path directoryMainLocation ;
    @Autowired
    FileStorageProperties fileStorageProperties ;


//save link file
    public List<FileDTO> loadAllFilesAsResource(String id )throws FileException {

        setFileStorageLocation(id);
        if(Files.exists(this.fileStorageLocation.toAbsolutePath())) {

            File directoryTarget = new File(String.valueOf(this.fileStorageLocation));
//            pull list of file
            List<File> fileFromDirectoryTarget = List.of(Objects.requireNonNull(directoryTarget.listFiles()));

            List<Resource> fileResource = new ArrayList<>();
            List<FileDTO> fileDTOList = new ArrayList<>();
//            loop of list in file
            fileFromDirectoryTarget.forEach(x -> {
                try {
//                    make it to uri can remotely access
                    fileResource.add(new UrlResource(this.fileStorageLocation.resolve(x.getName()).normalize().toUri()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            });
//            loop again for attach id folder and file name
            fileResource.forEach(x -> {
                String url = "https://intproj22.sit.kmutt.ac.th/kw1/api/files/" + id + "/" + x.getFilename();
                try {
//          take file to fileDTO and
                    fileDTOList.add(new FileDTO(x.getFilename(), url, detectFileType(Objects.requireNonNull(x.getFilename())), x.contentLength()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return fileDTOList;
        }else {
            throw new NotfoundByfield("this directory not existing.","file");
        }

    }

    private String detectFileType(String fileName) {
//        substring start at front of target index
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        switch (fileExtension) {
            case "png":
            case "jpg":
            case "jpeg":
                return "image";
            case "mp4":
                return "video";
            case "txt":
                return "text/plain";
            case "pdf":
                return "application/pdf";
            case "mp3":
                return "audio";
            case "apk":
                return "application/vnd.android.package-archive";
            case "zip":
                return "application/zip";
            case "sql":
                return "application/sql";
            default:
                return "application/octet-stream"; // Default to binary if the type is unknown
        }
    }


    public List<String> store(String id,MultipartFile [] files) throws FileException {
        createDirectory(id);
        File directoryTarget = new File(String.valueOf(this.fileStorageLocation));
        int fileAdded = 0 ;
        List<String> filesName = new ArrayList<>() ;
        try {
// Normalize file name

                for(MultipartFile file : files) {

                    String fileName =StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    filesName.add( fileName);
                    if (filesName.get(filesName.size()-1).contains("..")) {
                        throw new FileException("Sorry! Filename contains invalid path sequence " + filesName.get(filesName.size()-1), "file");
                    }
                    if(Objects.requireNonNull(directoryTarget.listFiles()).length<5) {
                        Path targetLocation = this.fileStorageLocation.resolve(fileName);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                        fileAdded++ ;
                    }
                    else {
                        throw new FileException(" Sorry, You can't store more than 5 files. So now You have added already "+ fileAdded +" files from before.","file");
                    }

                }

            return filesName;
        } catch (IOException ex) {
            throw new FileException("Could not store file " + String.join(" , ", filesName) + " at announcement folder id is "+ id +". Please try again! "+ex.getMessage(),"file");
        }
    }
    public void updateFiles(String id, MultipartFile[] files, String[] oldFile) throws FileException {
        setFileStorageLocation(id);
        File directoryTarget = new File(String.valueOf(this.fileStorageLocation));

        if(directoryTarget.listFiles()==null){
            store(id,files);
            return;
        }
        //        pull lists file from directory
        List<File> fileFromDirectoryTarget = List.of(Objects.requireNonNull(directoryTarget.listFiles()));
//        make list to array
        List<String> oldFileList = Arrays.asList(oldFile);
        List<String> filesName = new ArrayList<>();

        try {
//   loop for check file have same name or not ?
            for (File file : fileFromDirectoryTarget) {
                if (oldFileList.contains(file.getName())) {
//                    delete if file is duplicate
                    Files.deleteIfExists(file.toPath());
                }
            }if(files[0].isEmpty()){
                return;
            }

            if (files.length + fileFromDirectoryTarget.size() > 5) {
//                check file size not over 5
                throw new FileException("Sorry, You can't store more than 5 files. So now You have added already " + fileFromDirectoryTarget.size() + " files from before.", "file");
            }


//           add file from request
            for (MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                filesName.add(fileName);
            }

            // เพิ่มการประมวลผลเพิ่มเติมตามความต้องการของคุณ
        } catch (IOException e) {
            throw new FileException("Could not update files: " + e.getMessage(), "file");
        }
    }



    public Resource loadFileAsResource(String id,String fileName) throws FileException {
        try {
            setFileStorageLocation(id);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex) {
            throw new FileException("File operation error: " + fileName, "file");
        }
    }

    public void  deleteFolderById(String id) throws FileException {
        try {
            setFileStorageLocation(id);
            if(Files.exists(this.fileStorageLocation.toAbsolutePath())) {
                FileSystemUtils.deleteRecursively(this.fileStorageLocation.toFile());
            }else {
                throw new FileException("Could not delete directory because there not existing.","file");
            }
        }catch (FileException e) {
            throw new NotfoundByfield(e.getMessage(),"Folder");
        }

    }

    public void  deleteFileById(String id,String fileName) {
        try {
            setFileStorageLocation(id);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if(Files.exists(filePath)){
                FileSystemUtils.deleteRecursively(filePath.toFile());
            }
            else {
                throw new FileException("Could not delete file  because there not existing.","file");
            }

        }  catch (FileException e) {
            throw new NotfoundByfield(e.getMessage(),"File");
        }
    }






    public void setFileStorageLocation(String id) throws FileException {
        try {
            this.directoryMainLocation =  Paths.get(fileStorageProperties.getUploadDir()) ;
            this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()+"/announcement_"+id)
                    .toAbsolutePath().normalize();
        }catch (Exception ex){
            throw new FileException(
                    "Could not set the fileStorageLocation variable", "file");
        }

    }

    public void createDirectory(String id) throws FileException {
        try {
            setFileStorageLocation(id);
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileException(
                    "Could not create the directory where the uploaded files will be stored.", "file");
        }
    }


}
