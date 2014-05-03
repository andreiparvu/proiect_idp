package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


// Mock class for the moment, will implement later
public class WebServiceClient {
  Mediator med;
  HashMap<String, String> ips = new HashMap<>();
  HashMap<String, Integer> ports = new HashMap<>();

  static Logger logger = Logger.getLogger(WebServiceClient.class);

  private final boolean mock = true;
  
  public WebServiceClient(Mediator med) {
    this.med = med;
    logger.addAppender(MainWindow.appender);
    logger.info("WebServiceClient created.");
  }

  public void newUser(String user, String ip, int port) {
    med.addUser(user);
    ips.put(user, ip);
    ports.put(user, port);

    logger.info("Added user " + user + " with " + ip + ":" + port);
  }

  public ArrayList<String> getFilesFromUser(String userName) {
    if (mock)
    	return mockGetFilesFromUser(userName);
    
    return null;
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
  
  public void refresh() throws IOException {
  	if(mock)
  		mockRefresh();
  }
  
  public String getContentsFrom(String source) {
  	URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		StringBuffer sb = new StringBuffer("");
		
		try {
	        url = new URL(source);
	        is = url.openStream();
	        br = new BufferedReader(new InputStreamReader(is));

	        while ((line = br.readLine()) != null) {
	        	sb.append(line);
	        }
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
		
		return sb.toString();
  }
  
  private void mockRefresh() throws IOException {
  	Scanner s = new Scanner(new File(med.getUser() + ".txt"));
    while (s.hasNextLine()) {
      String line = s.nextLine();

      StringTokenizer st = new StringTokenizer(line, "=");

      String prop = st.nextToken(), value = st.nextToken();

      System.out.println(prop + value);
      if (prop.compareTo("user") == 0) {
        newUser(value, st.nextToken(), Integer.parseInt(st.nextToken()));
      }
    }

    s.close();
  }
  
  private ArrayList<String> mockGetFilesFromUser(String userName) {
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

    logger.info("Retrieved files from " + userName);

    return files;
  }
}
