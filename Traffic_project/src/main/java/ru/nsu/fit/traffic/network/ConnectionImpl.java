package ru.nsu.fit.traffic.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.interfaces.network.Connection;

public class ConnectionImpl implements Connection {

  private final String URL;
  private final String GET_URL;
  private final String SAVE_URL;
  private final String ROOMS_URL;
  private final String CREATE_ROOM;
  private final String GLOBAL;
  private final String DROP_BLOCK;
  private final String BLOCKS;
  private final ConnectionConfig connectionConfig;
  private final String LOGIN;
  private final String SIGN_UP;
  private final String ADMIN;

  private List<Long> blockedMaps;

  public ConnectionImpl(String url) {
    if (!url.endsWith("/")) url += "/";
    if (!url.startsWith("http://")) url = "http://" + url;
    this.URL = url;
    GET_URL = URL + "api/getMap";
    SAVE_URL = URL + "api/saveMap";
    ROOMS_URL = URL + "api/rooms";
    CREATE_ROOM = URL + "api/createRoom";
    GLOBAL = URL + "api/global";
    DROP_BLOCK = URL + "api/dropBlock";
    BLOCKS = URL + "api/blocks";
    LOGIN = URL + "login";
    SIGN_UP = URL + "registration";
    connectionConfig = ConnectionConfig.getConnectionConfig();
    ADMIN = URL + "api/adminCheck";
  }

  @Override
  public void pushGlobalMap(String filePath, Integer roomId) {
    pushAnyMap(filePath, SAVE_URL + "?roomId=" + roomId);
  }

  @Override
  public Integer createRoom(String filePath, String roomName) {
    HttpEntity entity = pushAnyMap(
      filePath,
      CREATE_ROOM + "?roomName=" + roomName.replace(' ', '+')
    ).getEntity();
    try {
      Scanner scanner = new Scanner(entity.getContent());
      return scanner.nextInt();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Long> getRooms() {
    HttpGet request = new HttpGet(ROOMS_URL);
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
      connectionConfig.getUsername(),
      connectionConfig.getPassword()
    );
    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
      .setDefaultCredentialsProvider(provider)
      .build();
    try {
      HttpResponse response = client.execute(request);
      Gson gson = new Gson();
      return gson.fromJson(
        new BufferedReader(new InputStreamReader(response.getEntity().getContent())),
        new TypeToken<List<Long>>(){}.getType()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getMapFromServer(int num, int roomId, boolean block) throws Exception {
    try {
      return getMap(num, new URL(GET_URL + "?id=" + num + "&roomId=" + roomId + "&block=" + block));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getGlobalMapFromServer(int roomId) {
    try {
      return getMap(null, new URL(GLOBAL + "?roomId="+roomId));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean pushMap(int num, int roomId, String filepath) {
    return pushAnyMap(filepath, SAVE_URL + "?id=" + num + "&roomId=" + roomId)
      .getStatusLine()
      .getStatusCode() == 200;
  }

  @Override
  public boolean dropBlock(int roomId, int mapId) {
    HttpPost request = new HttpPost(DROP_BLOCK + "?roomId="+roomId+"&mapId="+mapId);
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
      connectionConfig.getUsername(),
      connectionConfig.getPassword()
    );

    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
      .setDefaultCredentialsProvider(provider)
      .build();

    try {
      HttpResponse response = client.execute(request);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public List<Long> getLastBlockedMaps(int roomId){
    if (blockedMaps == null) {
      blockedMaps(roomId);
    }
    return blockedMaps;
  }

  @Override
  public List<Long> blockedMaps(int roomId) {
    HttpGet request = new HttpGet(BLOCKS+"?roomId="+roomId);
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
      connectionConfig.getUsername(),
      connectionConfig.getPassword()
    );
    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
      .setDefaultCredentialsProvider(provider)
      .build();
    try {
      HttpResponse response = client.execute(request);
      Gson gson = new Gson();
      blockedMaps = gson.fromJson(
              new BufferedReader(new InputStreamReader(response.getEntity().getContent())),
              new TypeToken<List<Long>>(){}.getType()
      );
      return blockedMaps;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean login() {
    return check200(LOGIN);
  }

  @Override
  public boolean registration(String username, String password, String passConfirm) {
    HttpPost request = new HttpPost(
      SIGN_UP+"?username="+username+"&password="+password+"&passConfirm="+passConfirm
    );
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
      connectionConfig.getUsername(),
      connectionConfig.getPassword()
    );

    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
      .setDefaultCredentialsProvider(provider)
      .build();

    try {
      HttpResponse response = client.execute(request);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean isAdmin(int roomId) {
    return check200(ADMIN + "?roomId="+roomId);
  }

  private boolean check200(String admin) {
    HttpGet request = new HttpGet(admin);
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
      connectionConfig.getUsername(),
      connectionConfig.getPassword()
    );
    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
      .setDefaultCredentialsProvider(provider)
      .build();
    try {
      HttpResponse response = client.execute(request);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private HttpResponse pushAnyMap(String filepath, String url) {
    try {
      HttpEntity entity = MultipartEntityBuilder.create()
        .addPart("file", new FileBody(new File(filepath)))
        .build();

      HttpPost request = new HttpPost(url);
      request.setEntity(entity);

      CredentialsProvider provider = new BasicCredentialsProvider();
      UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
        connectionConfig.getUsername(),
        connectionConfig.getPassword()
      );
      provider.setCredentials(AuthScope.ANY, credentials);
      HttpClient client = HttpClientBuilder.create()
        .setDefaultCredentialsProvider(provider)
        .build();
      return client.execute(request);
    } catch (IOException e) {
      throw new RuntimeException("Can't push map: '" + url, e);
    }
  }

  private String getMap(Integer id, URL url) throws Exception {
    try {
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setAuthenticator(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(
            connectionConfig.getUsername(),
            connectionConfig.getPassword().toCharArray()
          );
        }
      });

      ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
      FileOutputStream fos;

      Path fullPath;

      if (id != null) {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_" + id + ".tsp");
      } else {
        fullPath = Path.of(new File("").getAbsolutePath(), "map_global.tsp");
      }
      fos = new FileOutputStream(fullPath.toString());
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      return fullPath.toString();
    } catch (IOException e) {
      throw new Exception("Can't get map: '" + id + "' id.", e);
    }
  }
}
