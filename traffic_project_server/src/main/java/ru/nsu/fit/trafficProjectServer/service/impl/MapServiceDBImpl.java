package ru.nsu.fit.trafficProjectServer.service.impl;

import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.trafficProjectServer.configuration.MapConfiguration;
import ru.nsu.fit.trafficProjectServer.model.Map;
import ru.nsu.fit.trafficProjectServer.model.Room;
import ru.nsu.fit.trafficProjectServer.repository.MapRepository;
import ru.nsu.fit.trafficProjectServer.repository.RoomRepository;
import ru.nsu.fit.trafficProjectServer.service.MapService;

@Service
public class MapServiceDBImpl implements MapService {

  @Autowired
  private MapConfiguration configuration;

  @Autowired
  private MapRepository mapRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Override
  @Deprecated
  public String getDocumentName(Long id, Long roomId) {
    return null;
  }

  @Override
  public Resource loadFileAsResource(String fileName, Long roomId) {
    return null;
  }

  @Override
  public String storeFile(MultipartFile file, Long id, Long roomId) {
    Room room = roomRepository.getOne(roomId);
    Map map = mapRepository.findByRoomIdAndFollowUpNumber(roomId, id)
      .orElse(new Map());
    enrichMap(map, file, room);
    mapRepository.save(map);
    if (map.getFollowUpNumber() == null) {
      room.addMap(map);
      roomRepository.save(room);
    }
    return file.getOriginalFilename();
  }

  private void enrichMap(Map map, MultipartFile file, Room room) {
    try {
      map.setFile(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    map.setRoom(room);
    map.setFilename(file.getOriginalFilename());
  }

  @Override
  public Long getRooms() {
    return roomRepository.count();
  }

  @Override
  public Long createRoom(MultipartFile file, String name) {
    Room room = new Room();
    room.setName(name);
    roomRepository.save(room);
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    Map map = new Map();
    map.setFilename(fileName);
    try {
      map.setFile(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    map.setRoom(room);
    mapRepository.save(map);
    room.setGlobalMap(map);
    return roomRepository.save(room).getId();
  }

  @Override
  @Deprecated
  public String getGlobalMap(Long roomId) {
    return roomRepository.findById(roomId)
      .map(Room::getGlobalMap)
      .map(Map::getFilename)
      .orElse(null);
  }

  @Override
  public Map getMap(Long id, Long roomId) {
    return mapRepository.findByRoomIdAndFollowUpNumber(roomId, id).orElse(null);
  }

  @Override
  public Map getGlobalMapNew(Long roomId) {
    return roomRepository.findById(roomId)
      .map(Room::getGlobalMap)
      .orElse(null);
  }
}
