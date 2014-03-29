/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
  static MainWindow frame;
  
	Mediator mediator;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4202146695554199981L;

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private JList<String> fileList;
	
	private JList<String> userList;

	private WebService webService;
	
	class MyTableModel extends DefaultTableModel {
	  
	  public MyTableModel(String[] columnNames) {
	    super(columnNames, 0);
	  }
	  
	  public boolean isCellEditable(int row, int column) {
	    return false;
	  }
	}
	
	public MainWindow() {
	  mediator = new Mediator();
	  webService = new WebService(mediator);
	  
	  fileList = new FileList(mediator, new DefaultListModel<String>());
	  
		JScrollPane fileListScrollPane = new JScrollPane(fileList);
		
		userList = new UserList(mediator, new DefaultListModel<String>()); //new JList<String>(users);
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

		// Add the split pane to this frame
		getContentPane().add(horizontalSplitPane);
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
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		
		while (true) {
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      String command = null;

      //  read the username from the command-line; need to use try/catch with the
      //  readLine() method
      try {
         command = br.readLine();
         StringTokenizer st = new StringTokenizer(command, " \n");
         
         String cmd = st.nextToken();
         
         switch(cmd) {
           case "add_user":
             frame.mediator.addUser(st.nextToken());
             break;
           case "rm_user":
             frame.mediator.delUser(st.nextToken());
           case "all":
             frame.mediator.addUser("andrei");
             frame.mediator.addUser("daniel");
             break;
         }
         
      } catch (IOException ioe) {
         System.out.println("IO error trying to read your name!");
//         System.exit(1);
      }
		}
	}
}