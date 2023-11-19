package sit.int221.sas.sit_announcement_system_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.FileException;
import sit.int221.sas.sit_announcement_system_backend.properties.FileStorageProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {
    private Path fileStorageLocation ;

    private Path directoryMainLocation ;
    @Autowired
    FileStorageProperties fileStorageProperties ;

    public List<Resource> loadAllFilesAsResource(String id) throws FileException {
        setFileStorageLocation(id);
        List<File> fileFromDirectoryTarget = List.of(Objects.requireNonNull(new File(String.valueOf(this.fileStorageLocation)).listFiles()));

        return  fileFromDirectoryTarget.stream().map(x->{
            try {
              return (Resource) new UrlResource(  this.fileStorageLocation.resolve(x.getName()).normalize().toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toList() ;
//        new UrlResource(filePath.toUri());
//        return Arrays.stream(directoryTarget.listFiles()).toList();

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

    public Resource loadFileAsResource(String id,String fileName) throws FileException {
        try {
            setFileStorageLocation(id);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex) {
            throw new FileException("File operation error: " + fileName, "file");
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
