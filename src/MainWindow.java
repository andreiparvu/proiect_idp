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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

public class MainWindow extends JFrame {

	Mediator mediator;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4202146695554199981L;

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	private JList<String> fileList;
	
	private String[] imageNames = { "Bird", "Cat", "Dog"};
	private String[] users = { "Jeg", "Dobitoc" };

	private JList<String> userList;

	private JLabel label;

	public MainWindow() {
		
		fileList = new JList<String>(imageNames);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setSelectedIndex(0);
//		fileList.addListSelectionListener(this); // ? nu chiar asa
		JScrollPane fileListScrollPane = new JScrollPane(fileList);
		
		userList = new JList<String>(users);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setSelectedIndex(0);
//		fileList.addListSelectionListener(this); // ? nu chiar asa
		JScrollPane userScrollPane = new JScrollPane(userList);

		// Create a regular old label
		label = new JLabel("Click on an image name in the list.", JLabel.CENTER);

		// Create a split pane and put "top" (a split pane)
		// and JLabel instance in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fileListScrollPane, label);
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
		JFrame frame = new MainWindow();
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
	}
}