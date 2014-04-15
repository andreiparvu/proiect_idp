package main;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class MainWindow extends JFrame {
  static MainWindow frame;
  static String curUser;
  static String curIP;
  static int curPort;

  Mediator mediator;

  private static final long serialVersionUID = -4202146695554199981L;

  private static final int WIDTH = 640;
  private static final int HEIGHT = 480;
  private static final int STATUS_HEIGHT = 60;

  private static final String LOGS_DIR = "logs";
  
  private JList<String> fileList;

  private JList<String> userList;

  static Logger logger = Logger.getLogger(MainWindow.class);
  public static FileAppender appender;

  class MyTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 3316004335920963650L;

    public MyTableModel(String[] columnNames) {
      super(columnNames, 0);
    }

    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }

  public MainWindow() {
    mediator = new Mediator();

    // Create and register web service, user list, file list and event table
    WebServiceClient webServiceClient = new WebServiceClient(mediator);
    mediator.registerWebServiceClient(webServiceClient);

    Network network = new Network(mediator, curIP, curPort);
    mediator.registerNetwork(network);

    fileList = new FileList(mediator, new DefaultListModel<String>());

    JScrollPane fileListScrollPane = new JScrollPane(fileList);

    userList = new UserList(mediator, new DefaultListModel<String>()); 
    JScrollPane userScrollPane = new JScrollPane(userList);

    // Create a split pane and put "top" (a split pane)
    // and JLabel instance in it.
    String[] columnNames = {"Source", "Destination", "File Name", "Progress", "Status"};

    EventTable t = new EventTable(mediator, new MyTableModel(columnNames));
    JScrollPane p = new JScrollPane(t);

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fileListScrollPane, p);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(180);
    splitPane.setBorder(null);

    // Create a split pane with the two scroll panes in it.
    JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane,
        userScrollPane);
    horizontalSplitPane.setOneTouchExpandable(true);
    horizontalSplitPane.setDividerLocation(WIDTH * 3/4);


    JTextArea statusText = new JTextArea();
    mediator.registerStatusArea(statusText);

    JSplitPane superSplitPane =
      new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, statusText);
    superSplitPane.setDividerLocation(HEIGHT - STATUS_HEIGHT);
    superSplitPane.setDividerSize(0);

    // Add the split pane to this frame
    getContentPane().add(superSplitPane);

    logger.info("Created the GUI.");

    try {
      Scanner s = new Scanner(new File(curUser + ".txt"));
      while (s.hasNextLine()) {
        String line = s.nextLine();

        StringTokenizer st = new StringTokenizer(line, "=");

        String prop = st.nextToken(), value = st.nextToken();

        System.out.println(prop + value);
        if (prop.compareTo("user") == 0) {
          webServiceClient.newUser(value, st.nextToken(), Integer.parseInt(st.nextToken()));
        }
      }

      s.close();
      File dir = new File(curUser + "/");
      for (File curFile : dir.listFiles()) {
        if (!curFile.isDirectory()) {
          mediator.addCurrentFile(curFile);
        }
      }
      
      mediator.statusText.setText("Current user: " + curUser);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private static void createAndShowGUI() {
    // Create and set up the window.
    frame = new MainWindow();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Display the window.
    // @Daniel: pack BEFORE setSize!
    frame.pack();
    frame.setSize(WIDTH, HEIGHT);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
//    curUser = args[0];
//    curIP = args[1];
//    curPort = Integer.parseInt(args[2]);
  	
  	curUser = "daniel";
  	curIP = "127.0.0.1";
  	curPort = 9000;
  	
//  	curUser = "andrei";
//  	curIP = "127.0.0.1";
//  	curPort = 8000;
  	
    try {
    	File logFile = new File(LOGS_DIR + "/" + curUser + ".log");
    	if (!logFile.exists()) {
    		logFile.createNewFile();
    	}
    	
    	appender = new FileAppender(new PatternLayout(), logFile.getAbsolutePath() , false);
  	} catch (IOException ex) {
  		ex.printStackTrace();
  	}
    logger.addAppender(appender);

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
        System.out.println("Current user: " + curUser);
      }
    });
  }
}
