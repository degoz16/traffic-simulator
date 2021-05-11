package ru.nsu.fit.trafficProjectServer.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.configuration.MapConfiguration;
import ru.nsu.fit.trafficProjectServer.service.MapService;

@Service
public class MapServiceImpl implements MapService {

  private Path fileStorageLocation;

  @Autowired
  private MapConfiguration configuration;

  @PostConstruct
  public void init() {
    this.fileStorageLocation = Paths.get(configuration.getFilePath()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException(
        "Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  @Override
  public String getDocumentName(int id) {
    String fileName = "map_" + id + ".json";
    Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
    if (filePath.toFile().exists()) {
      return fileName;
    }
    return null;
  }

  @Override
  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        return resource;
      } else {
        return null;
      }
    } catch (MalformedURLException ex) {
      return null;
    }
  }

  @Override
  public String storeFile(MultipartFile file, int id) {
    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = "";
    try {
      if(!originalFileName.endsWith(".json")) {
        throw new RuntimeException("Sorry! File isn't json map " + originalFileName);
      }
      String fileExtension = "";
      try {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch (Exception e) {
        fileExtension = "";
      }
      fileName = "map_" + id + fileExtension;
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return fileName;

    } catch (IOException ex) {

      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);

    }

  }
}
