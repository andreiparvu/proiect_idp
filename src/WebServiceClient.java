import java.util.ArrayList;
import java.util.Arrays;


public class WebServiceClient {
  Mediator med;
  
  public WebServiceClient(Mediator med) {
    this.med = med;
  }
  
  public ArrayList<String> getFilesFromUser(String userName) {
    // mock
    String[] files = null;
    
    switch (userName) {
      case "andrei":
        String[] filesAux1 = {"ceva", "atlceva"};
        files = filesAux1;
        
        break;
      case "daniel":
        String[] filesAux2 = {"unu", "doi", "trei"};
        files = filesAux2;
        
        break;
    }
    
    return new ArrayList<String>(Arrays.asList(files));
  }
  
  public void receivedPartOfFile(String file) {
    med.addFilePart(file, 10);
  }
}
