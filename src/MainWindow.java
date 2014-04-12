import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
  static MainWindow frame;
  static String curUser;
  
  Mediator mediator;

  private static final long serialVersionUID = -4202146695554199981L;

  private static final int WIDTH = 640;
  private static final int HEIGHT = 480;
  private static final int STATUS_HEIGHT = 60;

  private JList<String> fileList;

  private JList<String> userList;

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

    Network network = new Network(mediator);
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
  	curUser = args[0];
  	
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    
    
    // Mock testing routine
//    while (true) {
//      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//      String command = null;
//
//      //  read the username from the command-line; need to use try/catch with the
//      //  readLine() method
//      try {
//        command = br.readLine();
//        StringTokenizer st = new StringTokenizer(command, " \n");
//
//        String cmd = st.nextToken();
//        cmd = "all";
//
//        switch(cmd) {
//          case "add_user":
//            frame.mediator.addUser(st.nextToken());
//            break;
//          case "rm_user":
//            frame.mediator.delUser(st.nextToken());
//          case "all":
//            frame.mediator.addUser("andrei");
//            frame.mediator.addUser("daniel");
//            break;
//          case "add_part":
//            frame.mediator.addFilePart(st.nextToken(), 10);
//            break;
//        }
//
//      } catch (Exception ioe) {
//        System.out.println("IO error trying to read your name!");
//      }
//    }
  }
}
