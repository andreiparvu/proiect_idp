import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JTextArea;


public class Mediator {
	UserList userList;
	FileList fileList;
	WebServiceClient webServiceClient;
	EventTable eventTable;
	JTextArea statusText;
	
	public static final String GETING_FILES_FROM = "Getting files from ";
	public static final String DOWNLOADING_FILE = "Downloading file ";
	
	private String status = "morti";
	
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
	
	public void registerStatusArea(JTextArea textArea) {
		statusText = textArea;
		statusText.setFocusable(false);
		updateStatus();
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
	
	public String getStatus() {
		return "morti";
	}
	
	public void setStatus(String genericMessage, String subject) {
		status = genericMessage + subject;
		updateStatus();
	}
	
	private void updateStatus() {
		if (statusText != null)
			statusText.setText(status);
	}
	
	public void startDownload() {
		setStatus(DOWNLOADING_FILE, "\"" + this.fileList.selectedFile + "\"" 
				+ " from " + this.userList.selectedUser);
	  eventTable.addEntry(this.userList.selectedUser, this.fileList.selectedFile,
	      true);
	}
	
	public void addFilePart(String name, int quantity) {
	  eventTable.updateProgressBar(name, quantity);
	}
}
