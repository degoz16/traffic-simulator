package ru.nsu.fit.trafficProjectServer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.service.MapService;

@RestController
@RequestMapping("api")
public class MapController {

  @Autowired
  private MapService service;

  @GetMapping("getMap")
  public ResponseEntity<Resource> getMap(@RequestParam Integer id,
                                         @RequestParam Integer roomId) {
    String fileName = service.getDocumentName(id, roomId);
    Resource resource = null;
    if (fileName != null && !fileName.isEmpty()) {
      try {
        resource = service.loadFileAsResource(fileName, roomId);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String contentType = "application/octet-stream";
      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("saveMap")
  public ResponseEntity<Resource> saveMap(@RequestParam("file") MultipartFile file,
                                          @RequestParam Integer id,
                                          @RequestParam Integer roomId) {
    service.storeFile(file, id, roomId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("rooms")
  public List<Integer> roomsNumber() {
    return service.getRooms();
  }

  @PostMapping("createRoom")
  public Long createRoom(@RequestParam("file") MultipartFile file) {
    return service.createRoom(file);
  }

  @GetMapping("global")
  public ResponseEntity<Resource> getGlobalMap(@RequestParam Integer roomId){
    String fileName = service.getGlobalMap(roomId);
    Resource resource = null;
    if (fileName != null && !fileName.isEmpty()) {
      try {
        resource = service.loadFileAsResource(fileName, roomId);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String contentType = "application/octet-stream";
      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

}
