package ru.nsu.fit.trafficProjectServer.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MapService {
  String getDocumentName(int id, int roomId);
  Resource loadFileAsResource(String fileName, int roomId);
  String storeFile(MultipartFile file, int id, int roomId);
  List<Integer> getRooms() ;
  Long createRoom(MultipartFile file);
  String getGlobalMap(int roomId);
}
