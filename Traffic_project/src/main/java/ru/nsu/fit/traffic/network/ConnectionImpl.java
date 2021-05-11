package ru.nsu.fit.traffic.network;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.utils.ParameterStringBuilder;

public class ConnectionImpl implements Connection {

  private static final String URL = "http://localhost:8080/";
  private static final String GET_URL = URL + "api/getMap";
  private static final String SAVE_URL = URL + "api/saveMap";


  @Override
  public String getMapFromServer(int num) {
    URL url = null;

    try {
      url = new URL(GET_URL + "?id=" + num);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");


      ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
      FileOutputStream fos;

      Path fullPath;

      if (num != -1) {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_" + num + ".json");
        fos = new FileOutputStream(fullPath.toString());
      } else {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_global.json");
        fos = new FileOutputStream(fullPath.toString());
      }
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      return fullPath.toString();
    } catch (IOException e) {
      throw new RuntimeException("Can't get map: '" + num + "' id.", e);
    }
  }

  @Override
  public String getGlobalMapFromServer() {
    return getMapFromServer(-1);
  }

  @Override
  public void pushMap(int num, String filepath) {
    String url = null;
    try {
      url = SAVE_URL + "?id=" + num;
      HttpEntity entity = MultipartEntityBuilder.create()
        .addPart("file", new FileBody(new File(filepath)))
        .build();

      HttpPost request = new HttpPost(url);
      request.setEntity(entity);

      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(request);
      response.getEntity();
    } catch (IOException e) {
      throw new RuntimeException("Can't push map: '" + num + "' id.", e);
    }
  }

  @Override
  public void pushGlobalMap(String filepath) {
    pushMap(-1, filepath);
  }


}
