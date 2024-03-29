package main;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JTextArea;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class Mediator {
  UserList userList;
  FileList fileList;
  Network network;
  WebServiceClient webServiceClient;
  EventTable eventTable;
  JTextArea statusText;
  String curUser;

  public static final String GETING_FILES_FROM = "Getting files from ";
  public static final String DOWNLOADING_FILE = "Downloading file ";
  public static final String UPLOADING_FILE = "Uploading file ";

  private String status = "Idle";

  static Logger logger = Logger.getLogger(Network.class);
  static {
    logger.addAppender(new ConsoleAppender(new PatternLayout()));
  }

  public Mediator(String user) {
    curUser = user;

    if (MainWindow.appender != null) {
      logger.addAppender(MainWindow.appender);
    }
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

  public void setUsers(ArrayList<String> userNames) {
    // Add the logged-in users to the GUI
    this.userList.removeElements();

    for (String userName : userNames) {
      this.userList.addElement(userName);
    }
  }

  public void addCurrentFile(File file) {
    // Add file to upload list
    network.publishFile(file);
    webServiceClient.publishFile(file);
  }

  public void addUser(String user, String ip, int port) {
    // Add user to the logged-in list
    this.webServiceClient.addUser(user, ip, port);
  }

  public void getUsers() {
    this.webServiceClient.getUsers();
  }

  public void removeUser(String user) {
    this.webServiceClient.removeUser(user);
  }

  public void refreshUser() {
    // Get the files of the current selected user
    if (this.userList.selectedUser != null) {
      showFiles(this.userList.selectedUser);
    }
  }

  public void showFiles(String userName) {
    ArrayList<String> files = this.webServiceClient.getFilesFromUser(userName);

    fileList.removeElements();

    for (String file : files) {
      fileList.addElement(file);
    }
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
    File f = new File(getDownloadPath() + "/" + this.fileList.selectedFile);
    if (f.exists()) {
      logger.info("File " + this.fileList.selectedFile + " already exists.");
      setStatus("File already in folder. Please delete.", "");

      return;
    }

    setStatus(DOWNLOADING_FILE, "\"" + this.fileList.selectedFile + "\"" 
        + " from " + this.userList.selectedUser);
    eventTable.addEntry(this.userList.selectedUser, this.fileList.selectedFile,
        true);

    network.startDownload(webServiceClient.getIP(this.userList.selectedUser),
        webServiceClient.getPort(this.userList.selectedUser), this.fileList.selectedFile);
  }

  public void startUpload(String ip, int port, String filename) {
    String user = webServiceClient.getUser(ip, port);

    setStatus(UPLOADING_FILE, "\"" + filename + "\"" + " to " + user);

    eventTable.addEntry(user, filename, false);
  }

  public void addFilePart(String filename, float quantity) {
    // Add file part to the progress bar
    if (eventTable != null) {
      eventTable.updateProgressBar(filename, quantity);
    }
  }

  public int getProgress(String filename) {
    return eventTable.getProgress(filename);
  }

  public EventTable getTable() {
    return eventTable;
  }

  public String getDownloadPath() {
    return curUser;
  }
}
