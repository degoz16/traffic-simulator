package ru.nsu.fit.trafficProjectServer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.model.Map;

public interface MapService {
  String getDocumentName(Long id, Long roomId);
  Resource loadFileAsResource(String fileName, Long roomId);
  String storeFile(MultipartFile file, Long id, Long roomId);
  Long getRooms() ;
  Long createRoom(MultipartFile file, String name);
  String getGlobalMap(Long roomId);
  Map getMap(Long id, Long roomId, Boolean block);
  Map getGlobalMapNew(Long roomId);
}
