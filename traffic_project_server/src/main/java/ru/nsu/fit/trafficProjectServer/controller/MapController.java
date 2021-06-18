package ru.nsu.fit.trafficProjectServer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.nsu.fit.trafficProjectServer.model.Map;
import ru.nsu.fit.trafficProjectServer.service.MapService;

@RestController
@RequestMapping("api")
public class MapController {

  @Qualifier("mapServiceDBImpl")
  @Autowired
  private MapService service;

  @GetMapping("getMap")
  public ResponseEntity<byte[]> getMap(
    @RequestParam Long id,
    @RequestParam Long roomId,
    @RequestParam Boolean block
  ) {
    Map resource = service.getMap(id, roomId, block);
    if (resource == null) return ResponseEntity.badRequest().build();
    String contentType = "application/octet-stream";
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(contentType))
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + resource.getFilename() + "\""
      )
      .body(resource.getFile());
  }

  @PostMapping("saveMap")
  public ResponseEntity<Resource> saveMap(
    @RequestParam("file") MultipartFile file,
    @RequestParam Long id,
    @RequestParam Long roomId
  ) {
    service.storeFile(file, id, roomId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("rooms")
  public List<Long> roomsNumber() {
    return service.getRooms();
  }

  @PostMapping("createRoom")
  public Long createRoom(
    @RequestParam("file") MultipartFile file,
    @RequestParam String roomName
  ) {
    return service.createRoom(file, roomName);
  }

  @GetMapping("global")
  public ResponseEntity<byte[]> getGlobalMap(@RequestParam Long roomId) {
    try {
      Map resource = service.getGlobalMapNew(roomId);
      String contentType = "application/octet-stream";
      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(
          HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + resource.getFilename() + "\""
        ).body(resource.getFile());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("dropBlock")
  public ResponseEntity<String> dropBlock(@RequestParam Long roomId, @RequestParam Long mapId) {
    service.dropBlock(roomId, mapId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("blocks")
  public List<Long> blockedMaps(@RequestParam Long roomId) {
    return service.getBlocks(roomId);
  }

  @GetMapping("adminCheck")
  public ResponseEntity<String> admin(@RequestParam Long roomId) {
    if (service.admin(roomId)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }
}
