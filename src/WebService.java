import java.util.ArrayList;
import java.util.Arrays;


public class WebService {
  Mediator med;
  
  public WebService(Mediator med) {
    this.med = med;
    
    med.registerWebService(this);
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
}
