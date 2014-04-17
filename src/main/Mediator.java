package main;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JTextArea;


public class Mediator {
  UserList userList;
  FileList fileList;
  Network network;
  WebServiceClient webServiceClient;
  EventTable eventTable;
  JTextArea statusText;

  public static final String GETING_FILES_FROM = "Getting files from ";
  public static final String DOWNLOADING_FILE = "Downloading file ";

  private String status = "Idle";

  public Mediator() {
  }

  // Register the components which interact with the mediator
  public void registerUserList(UserList userList) {
    this.userList = userList;
  }

  public void registerFileList(FileList fileList) {
    this.fileList = fileList;
  }

  public void registerNetwork(Network network) {
    this.network = network;
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

  // Methods of communicating with the mediator
  public void addUser(String userName) {
    this.userList.addElement(userName);
  }

  public void delUser(String userName) {
    this.userList.removeElement(userName);
  }

  public void addCurrentFile(File file) {
    network.publishFile(file);
    webServiceClient.publishFile(file);
  }

  public void showFiles(String userName) {
    ArrayList<String> files = this.webServiceClient.getFilesFromUser(userName);

    fileList.removeElements();

    for (String file : files) {
      fileList.addElement(file);
    }
  }

  public String getStatus() {
    return "some status";
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

    network.startDownload(webServiceClient.getIP(this.userList.selectedUser),
        webServiceClient.getPort(this.userList.selectedUser), this.fileList.selectedFile);
  }

  public void addFilePart(String name, float quantity) {
    if (eventTable != null) {
      eventTable.updateProgressBar(name, quantity);
    }
  }
}
