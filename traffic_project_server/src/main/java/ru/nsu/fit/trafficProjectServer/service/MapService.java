package ru.nsu.fit.trafficProjectServer.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.model.Map;

public interface MapService {
  String storeFile(MultipartFile file, Long id, Long roomId);
  List<Long> getRooms() ;
  Long createRoom(MultipartFile file, String name);
  Map getMap(Long id, Long roomId, Boolean block);
  Map getGlobalMapNew(Long roomId);
  void dropBlock(Long roomId, Long mapId);
  List<Long> getBlocks(Long roomId);
  boolean admin(Long roomId);
}
