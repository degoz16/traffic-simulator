package ru.nsu.fit.trafficProjectServer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/application.properties")
@Configuration
public class MapConfiguration {
  @Value("${file.upload-dir}")
  String filePath;

  public String getFilePath() {
    return filePath;
  }
}
