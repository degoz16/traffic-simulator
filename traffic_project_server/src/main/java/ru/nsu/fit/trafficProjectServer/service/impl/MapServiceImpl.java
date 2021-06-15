package ru.nsu.fit.trafficProjectServer.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.configuration.MapConfiguration;
import ru.nsu.fit.trafficProjectServer.model.Map;
import ru.nsu.fit.trafficProjectServer.service.MapService;

@Service
public class MapServiceImpl implements MapService {

  private Path fileStorageLocation;
  private Path blockingFile;
  private Set<String> blockedFiles;
  private long roomsCount;

  @Autowired
  private MapConfiguration configuration;

  @PostConstruct
  public void init() {
    this.fileStorageLocation = Paths.get(configuration.getFilePath()).toAbsolutePath().normalize();
    this.blockingFile = fileStorageLocation
      .resolve(Paths.get(configuration.getBlockPath()))
      .toAbsolutePath()
      .normalize();
    blockedFiles = new HashSet<>();
    roomsCount = Arrays.stream(fileStorageLocation.toFile().listFiles())
      .filter(File::isDirectory).count();
    try {
      Files.createDirectories(this.fileStorageLocation);
      if (!blockingFile.toFile().exists()) {
        Files.createFile(this.blockingFile);
      } else {
        Scanner scanner = new Scanner(blockingFile.toFile());
        while (scanner.hasNext()) {
          blockedFiles.add(scanner.nextLine().trim());
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(
        "Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  @Override
  public String getDocumentName(Long id, Long roomId) {
    String fileName = toFileName(id);
    Path filePath = getRoomPath(roomId).resolve(fileName).toAbsolutePath();
    if (blockedFiles.contains(filePath.toString())) {
      return null;
    }
    return fileName;
  }

  @Override
  public Resource loadFileAsResource(String fileName, Long roomId) {
    try {
      Path filePath = getRoomPath(roomId).resolve(fileName).normalize();
      File file = filePath.toFile();
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter writer = new FileWriter(blockingFile.toFile(), true);
      writer.write(filePath.toString() + "\n");
      writer.flush();
      blockedFiles.add(filePath.toString());
      return new UrlResource(filePath.toUri());
    } catch (IOException ex) {
      return null;
    }
  }

  @Override
  public String storeFile(MultipartFile file, Long id, Long roomId) {
    try {
      Path filePath = getRoomPath(roomId).resolve(toFileName(id)).normalize();
      FileWriter writer = new FileWriter(blockingFile.toFile());

      blockedFiles.remove(filePath.toAbsolutePath().toString());
      blockedFiles.forEach(blockedFile -> {
        try {
          writer.write(blockedFile + "\n");
        } catch (IOException e) {
          throw new RuntimeException("Could not store file. Please try again!", e);
        }
      });
      writer.flush();
      return saveFileMap(file, id, getRoomPath(roomId));
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file. Please try again!", ex);
    }
  }

  @Override
  public Long getRooms() {
    return roomsCount;
  }

  @Override
  public Long createRoom(MultipartFile file, String name) {
    Path newRoom = fileStorageLocation.resolve(String.valueOf(roomsCount));
    try {
      Files.createDirectories(newRoom);
      saveFileMap(file, null, newRoom);
    } catch (IOException e) {
      throw new RuntimeException("Could create room. Please try again!", e);
    }

    return roomsCount++;
  }

  private String saveFileMap(MultipartFile file, Long id, Path roomPath) throws IOException {
    String fileName = toFileName(id);
    // Copy file to the target location (Replacing existing file with the same name)
    Path targetLocation = roomPath.resolve(fileName);
    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    return fileName;
  }

  @Override
  public Map getGlobalMapNew(Long roomId) {
    return null;
  }

  @Override
  public void dropBlock(Long roomId, Long mapId) {

  }

  @Override
  public List<Long> getBlocks(Long roomId) {
    return null;
  }

  @Override
  public String getGlobalMap(Long roomId) {
    String fileName = toFileName(null);
    Path filePath = getRoomPath(roomId).resolve(fileName).toAbsolutePath();
    if (!filePath.toFile().exists()) {
      return null;
    }
    return fileName;
  }

  @Override
  public Map getMap(Long id, Long roomId, Boolean block) {
    return null;
  }

  private String toFileName(Long id) {
    return "map_"+(id == null ? "global" : id)+".json";
  }
  
  private Path getRoomPath(long roomId) {
    Path path = fileStorageLocation.resolve(String.valueOf(roomId));
    if (path.toFile().exists()) {
      return path;
    } else {
      throw new IllegalArgumentException("Room:" + roomId + "doesn't exists");
    }
  }


}
