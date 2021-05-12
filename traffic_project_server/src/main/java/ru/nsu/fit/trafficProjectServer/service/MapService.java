package ru.nsu.fit.trafficProjectServer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MapService {
  String getDocumentName(int id);
  Resource loadFileAsResource(String fileName);
  String storeFile(MultipartFile file, int id);
}
