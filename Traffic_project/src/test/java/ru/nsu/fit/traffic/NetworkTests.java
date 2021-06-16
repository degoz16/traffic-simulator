package ru.nsu.fit.traffic;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.traffic.config.ConnectionConfig;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.network.ConnectionImpl;

public class NetworkTests {
  private static final String URL = "http://localhost:8080/";

  @Test
  public void testPutAndGet() throws Exception {
    Connection connection = new ConnectionImpl(URL);

    Path path = Path.of(NetworkTests.class.getResource("/connection/map_1.tsp").toURI());
    String val = new Scanner(path.toFile()).next();

    connection.pushMap(999, 0, path.toString());
    Assert.assertEquals(val, new Scanner(Path.of(connection.getMapFromServer(999, 0, true))
      .toFile())
      .next());
  }

  @Test
  public void testPutAndGetGlobal() throws FileNotFoundException, URISyntaxException {
    Connection connection = new ConnectionImpl(URL);

    Path path = Path.of(NetworkTests.class.getResource("/connection/global.tsp").toURI());
    String val = new Scanner(path.toFile()).next();

    Integer roomId = connection.createRoom(path.toString(), "NAME");
    Assert.assertEquals(val, new Scanner(Path.of(connection.getGlobalMapFromServer(roomId)).toFile())
      .next());
  }

  @Test
  public void getRooms() {
    Connection connection = new ConnectionImpl(URL);
    System.out.println(connection.getRooms());
  }

  @Test
  public void createRoom() {
    Connection connection = new ConnectionImpl(URL);
    try {
      Path path = Path.of(NetworkTests.class.getResource("/connection/map_1.tsp").toURI());
      System.out.println(connection.createRoom(path.toAbsolutePath().toString(), "NAME"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void getBlocks() {
    Connection connection = new ConnectionImpl(URL);
    List<Long> longList = connection.blockedMaps(2);
    connection.blockedMaps(2).forEach(System.out::println);
  }

  @Test
  public void dropBlock() {
    Connection connection = new ConnectionImpl(URL);
    Assert.assertTrue(connection.dropBlock(2,0));
  }


}
