package ru.nsu.fit.traffic.network;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.nsu.fit.traffic.interfaces.network.Connection;

public class ConnectionImpl implements Connection {

  private final String URL;
  private final String GET_URL;
  private final String SAVE_URL;
  private final String ROOMS_URL;
  private final String CREATE_ROOM;
  private final String GLOBAL;


  public ConnectionImpl(String url) {
    if (!url.endsWith("/")) url += "/";
    if (!url.startsWith("http://")) url = "http://" + url;
    this.URL = url;
    GET_URL = URL + "api/getMap";
    SAVE_URL = URL + "api/saveMap";
    ROOMS_URL = URL + "api/rooms";
    CREATE_ROOM = URL + "api/createRoom";
    GLOBAL = URL + "api/global";
  }

  @Override
  public Integer createRoom(String filePath) {
    HttpEntity entity = pushAnyMap(filePath, CREATE_ROOM);
    try {
      Scanner scanner = new Scanner(entity.getContent());
      return scanner.nextInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Double> getRooms() {
    HttpGet request = new HttpGet(ROOMS_URL);
    HttpClient client = HttpClientBuilder.create().build();
    try {
      HttpResponse response = client.execute(request);
      Gson gson = new Gson();
      return gson.fromJson(
        new BufferedReader(new InputStreamReader(response.getEntity().getContent())),
        ArrayList.class
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getMapFromServer(int num, int roomId) {
    try {
      return getMap(num, new URL(GET_URL + "?id=" + num + "&roomId=" + roomId));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getGlobalMapFromServer(int roomId) {
    try {
      return getMap(null, new URL(GLOBAL + "?roomId="+roomId));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void pushMap(int num, String filepath, int roomId) {
    pushAnyMap(filepath, SAVE_URL + "?id=" + num + "&roomId=" + roomId);
  }

  private HttpEntity pushAnyMap(String filepath, String url) {
    try {
      HttpEntity entity = MultipartEntityBuilder.create()
        .addPart("file", new FileBody(new File(filepath)))
        .build();

      HttpPost request = new HttpPost(url);
      request.setEntity(entity);

      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(request);
      return response.getEntity();
    } catch (IOException e) {
      throw new RuntimeException("Can't push map: '" + url, e);
    }
  }

  private String getMap(Integer id, URL url) {
    try {
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
      FileOutputStream fos;

      Path fullPath;

      if (id != null) {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_" + id + ".json");
      } else {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_global.json");
      }
      fos = new FileOutputStream(fullPath.toString());
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      return fullPath.toString();
    } catch (IOException e) {
      throw new RuntimeException("Can't get map: '" + id + "' id.", e);
    }
  }
}
