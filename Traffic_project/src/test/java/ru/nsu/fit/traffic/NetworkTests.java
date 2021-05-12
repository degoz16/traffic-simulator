package ru.nsu.fit.traffic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.traffic.interfaces.network.Connection;
import ru.nsu.fit.traffic.network.ConnectionImpl;

public class NetworkTests {
  @Test
  public void testPutAndGet() throws FileNotFoundException, URISyntaxException {
    Connection connection = new ConnectionImpl();

    Path path = Path.of(NetworkTests.class.getResource("/connection/map_1.json").toURI());
    String val = new Scanner(path.toFile()).next();

    connection.pushMap(999, path.toString());
    Assert.assertEquals(val, new Scanner(Path.of(connection.getMapFromServer(999)).toFile())
      .next());
  }

  @Test
  public void testPutAndGetGlobal() throws FileNotFoundException, URISyntaxException {
    Connection connection = new ConnectionImpl();

    Path path = Path.of(NetworkTests.class.getResource("/connection/global.json").toURI());
    String val = new Scanner(path.toFile()).next();

    connection.pushGlobalMap(path.toString());
    Assert.assertEquals(val, new Scanner(Path.of(connection.getGlobalMapFromServer()).toFile())
      .next());
  }


}
