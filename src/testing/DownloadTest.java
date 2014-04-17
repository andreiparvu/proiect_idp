package testing;

import java.io.File;

import junit.framework.TestCase;
import main.Mediator;
import main.Network;

public class DownloadTest extends TestCase {
  Network net1;
  Network net2;

  protected void setUp() {
    net1 = new Network(new Mediator(), "127.0.0.1", 8000);
    net2 = new Network(new Mediator(), "127.0.0.1", 9000);
  }

  public void doTest(String file, Network net1, Network net2, int port, long timeout) {
    File f = new File("testing/" + file);

    assertTrue(f.exists());

    net1.publishFile(f);

    net2.startDownload("127.0.0.1", port, file);

    try {
      Thread.sleep(timeout);
    } catch (Exception ex) {}

    File newFile = new File(file);
    assertTrue(newFile.exists());

    newFile.delete();
  }

  public void testDownloadSmall() {
    doTest("some_file.txt", net1, net2, 8000, 3000);
    net1.close();
    net2.close();
  }

  public void testDownloadMedium() {
    doTest("medium.txt", net1, net2, 8000, 3000);
    net1.close();
    net2.close();
  }

  public void testDownloadBig() {
    doTest("kids.jpg", net2, net1, 9000, 3000);
    net1.close();
    net2.close();
  }
}
