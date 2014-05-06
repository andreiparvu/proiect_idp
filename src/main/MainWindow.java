package main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
  public static String curUser;
  public static String curIP;
  public static int curPort;

  Mediator mediator;

  private static final long serialVersionUID = -4202146695554199981L;

  private static final int WIDTH = 640;
  private static final int HEIGHT = 480;
  private static final int STATUS_HEIGHT = 60;
  private static final int REF_BTN_WIDTH = 160;

  private static final String LOGS_DIR = "logs";

  private JList<String> fileList;

  private JList<String> userList;

  static Logger logger = Logger.getLogger(MainWindow.class);
  public static FileAppender appender;

  public static class MyTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 3316004335920963650L;

    public MyTableModel(String[] columnNames) {
      super(columnNames, 0);
    }

    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }

  public MainWindow() {
    super(curUser);
    mediator = new Mediator(curUser);

    // Create and register web service, user list, file list and event table
    Network network = new Network(mediator, curIP, curPort);
    mediator.registerNetwork(network);

    fileList = new FileList(mediator, new DefaultListModel<String>());

    JScrollPane fileListScrollPane = new JScrollPane(fileList);

    userList = new UserList(mediator, new DefaultListModel<String>()); 
    JScrollPane userScrollPane = new JScrollPane(userList);

    WebServiceClient webServiceClient = new WebServiceClient(mediator, curUser, curIP, curPort);
    mediator.registerWebServiceClient(webServiceClient);
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
    horizontalSplitPane.setDividerLocation(WIDTH - REF_BTN_WIDTH);

    JTextArea statusText = new JTextArea();
    mediator.registerStatusArea(statusText);

    JButton refreshBtn = new JButton();
    refreshBtn.setSize(REF_BTN_WIDTH, STATUS_HEIGHT);
    refreshBtn.setText("Refresh");
    refreshBtn.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent arg0) {
        mediator.removeUser(curUser);
        mediator.addUser(curUser, curIP, curPort);
        mediator.getUsers();

        addFiles();

        mediator.refreshUser();
      }

      public void mouseEntered(MouseEvent arg0) {}
      public void mouseExited(MouseEvent arg0) {}
      public void mousePressed(MouseEvent arg0) {}
      public void mouseReleased(MouseEvent arg0) {}

    });

    JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
        statusText, refreshBtn);
    bottomSplitPane.setDividerLocation(WIDTH - REF_BTN_WIDTH);
    bottomSplitPane.setDividerSize(0);

    JSplitPane superSplitPane =
      new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomSplitPane);
    superSplitPane.setDividerLocation(HEIGHT - STATUS_HEIGHT);
    superSplitPane.setDividerSize(0);

    // Add the split pane to this frame
    getContentPane().add(superSplitPane);

    logger.info("Created the GUI.");
    addFiles();
  }

  private void addFiles() {
    File uploadFolder = new File(curUser);
    for (File file : uploadFolder.listFiles()) {
      mediator.addCurrentFile(file);
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

    frame.addWindowListener(new WindowListener() {
      public void windowDeactivated(WindowEvent e) {}
      public void windowClosing(WindowEvent e) {
        frame.mediator.removeUser(curUser);
      }
      public void windowClosed(WindowEvent e) {}
      public void windowOpened(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowActivated(WindowEvent e) {}
    });
  }

  public static void main(String[] args) {
    curUser = args[0];
    curIP = args[1];
    curPort = Integer.parseInt(args[2]);

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
        logger.info("Current user: " + curUser);
      }
    });
  }
}
