package ru.nsu.fit.trafficProjectServer.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<Resource> getMap(@RequestParam(required = false) Integer id) {
    String fileName = service.getDocumentName(id == null ? -1 : id);
    Resource resource = null;
    if(fileName !=null && !fileName.isEmpty()) {
      try {
        resource = service.loadFileAsResource(fileName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String contentType = "application/octet-stream";
      return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("saveMap")
  public ResponseEntity<Resource> saveMap(@RequestParam("file") MultipartFile file,
                                             @RequestParam(required = false) Integer id) {
    service.storeFile(file, id == null ? -1 : id);
    return ResponseEntity.ok().build();
  }

}
