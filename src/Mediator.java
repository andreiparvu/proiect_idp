import java.nio.file.Files;
import java.util.ArrayList;


public class Mediator {
	UserList userList;
	FileList fileList;
	WebServiceClient webServiceClient;
	EventTable eventTable;
	
	public Mediator() {
	}
	
	public void registerUserList(UserList userList) {
		this.userList = userList;
	}
	
	public void registerFileList(FileList fileList) {
	  this.fileList = fileList;
	}
	
	public void registerWebServiceClient(WebServiceClient webServiceClient) {
	  this.webServiceClient = webServiceClient;
	}
	
	public void registerEventTable(EventTable eventTable) {
	  this.eventTable = eventTable;
	}
	
	public void addUser(String userName) {
	  this.userList.addElement(userName);
	}
	
	public void delUser(String userName) {
	  this.userList.removeElement(userName);
	}
	
	public void showFiles(String userName) {
	  ArrayList<String> files = this.webServiceClient.getFilesFromUser(userName);
	  
	  fileList.removeElements();
	  
	  for (String file : files) {
	    fileList.addElement(file);
	  }
	}
	
	public void startDownload() {
	  eventTable.addEntry(this.userList.selectedUser, this.fileList.selectedFile,
	      true);
	}
	
	public void addFilePart(String name, int quantity) {
	  eventTable.updateProgressBar(name, quantity);
	}
}
