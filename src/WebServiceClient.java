import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


// Mock class for the moment, will implement later
public class WebServiceClient {
  Mediator med;
  HashMap<String, String> ips = new HashMap<>();
  HashMap<String, Integer> ports = new HashMap<>();
  
  public WebServiceClient(Mediator med) {
    this.med = med;
  }

  public void newUser(String user, String ip, int port) {
  	med.addUser(user);
  	ips.put(user, ip);
  	ports.put(user, port);
  }
  
  public ArrayList<String> getFilesFromUser(String userName) {
    // mock
//    String[] files = null;
//
//    switch (userName) {
//      case "andrei":
//        String[] filesAux1 = {"ceva", "atlceva"};
//        files = filesAux1;
//
//        break;
//      case "daniel":
//        String[] filesAux2 = {"unu", "doi", "trei"};
//        files = filesAux2;
//
//        break;
//    }

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
  	
    return files;
  }

  public String getIP(String user) {
  	return ips.get(user);
  }
  
  public int getPort(String user) {
  	return ports.get(user);
  }
  
  public void receivedPartOfFile(String file) {
    med.addFilePart(file, 10);
  }
}
