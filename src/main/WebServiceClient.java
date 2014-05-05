package main;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


// Mock class for the moment, will implement later
public class WebServiceClient {
  Mediator med;

  private final static String BASE_URL = "http://localhost:8080/IDPWebService/";

  HashMap<String, String> ips = new HashMap<>();
  HashMap<String, Integer> ports = new HashMap<>();

  private String curUser;

  static Logger logger = Logger.getLogger(WebServiceClient.class);

  public WebServiceClient(Mediator med, String user, String ip, int port) {
    this.med = med;
    this.curUser = user;

    connect("addUser?user=" + user + "&ip=" + ip + "&port=" + port);
    getUsers();

    logger.addAppender(MainWindow.appender);
    logger.info("WebServiceClient created and registered.");
  }

  public void newUser(String user, String ip, int port) {
    ips.put(user, ip);
    ports.put(user, port);

    logger.info("Added user " + user + " with " + ip + ":" + port);
  }

  private String connect(String uri) {
    try {
      URL url = new URL(BASE_URL + uri);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");

      connection.connect();

      InputStreamReader reader = new InputStreamReader(connection.getInputStream());

      String rez = "";
      while (true) {
        char[] buf = new char[100];

        int n = reader.read(buf, 0, 100);
        if (n < 0) {
          break;
        }
        rez += new String(buf, 0, n);
      }

      return rez;
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  public void removeUser(String user) {
    connect("removeUser?user=" + user);
  }

  public void getUsers() {
    ArrayList<String> allUsers = new ArrayList<>();

    String rez = connect("/getUserList");

    StringTokenizer st = new StringTokenizer(rez, "\n");

    for (; st.hasMoreTokens(); ) {
      String curUser = st.nextToken();

      StringTokenizer userSt = new StringTokenizer(curUser, "=");
      String user = userSt.nextToken();
      allUsers.add(user);

      ips.put(user, userSt.nextToken());
      ports.put(user, Integer.parseInt(userSt.nextToken()));
    }

    this.med.setUsers(allUsers);
  }
  public ArrayList<String> getFilesFromUser(String userName) {
    ArrayList<String> files = new ArrayList<>();

    String rez = connect("getFileList?user=" + userName);

    StringTokenizer st = new StringTokenizer(rez, "|");
    for (; st.hasMoreTokens(); ) {
      files.add(st.nextToken());
    }

    logger.info("Retrieved files from " + userName);

    return files;
  }

  public String getIP(String user) {
    return ips.get(user);
  }

  public int getPort(String user) {
    return ports.get(user);
  }

  public String getUser(String ip, int port) {
  	int count = 0;
  	String correctUser = null;
  	
  	for (String user : ips.keySet())
  		if (ips.get(user).equals(ip)) {
  			count ++;
  			correctUser = user;
  		}
  	
  	if (count == 1)
  		return correctUser;
  	
  	for (String user : ports.keySet())
  		if (ports.get(user) == port)
  			return user;
  	
  	return null;
  }
  
  public void publishFile(File f) {
    connect("publishFile?user=" + curUser + "&file=" + f.getName());
  }
}
