package main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;


// Mock class for the moment, will implement later
public class WebServiceClient {
  Mediator med;
  HashMap<String, String> ips = new HashMap<>();
  HashMap<String, Integer> ports = new HashMap<>();

  static Logger logger = Logger.getLogger(WebServiceClient.class);

  public WebServiceClient(Mediator med) {
    this.med = med;

    logger.info("WebServiceClient created.");
  }

  public void newUser(String user, String ip, int port) {
    med.addUser(user);
    ips.put(user, ip);
    ports.put(user, port);

    logger.info("Added user " + user + " with " + ip + ":" + port);
  }

  public ArrayList<String> getFilesFromUser(String userName) {
    // mock
    ArrayList<String> files = new ArrayList<>();

    try {
      Scanner s = new Scanner(new File("users/files/" + userName + ".txt"));

      while (s.hasNextLine()) {
        files.add(s.nextLine());
      }

      s.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    logger.info("Retireved files from " + userName);

    return files;
  }

  public String getIP(String user) {
    return ips.get(user);
  }

  public int getPort(String user) {
    return ports.get(user);
  }

  public void publishFile(File f) {
    // inca nimic \:D/
  }
}
