import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class Main extends JPanel {
	
	private DefaultListModel	model;										// list model
	private JList				list, mirror;								// lists
	private JTextField			tName			= new JTextField(10);		// name field
	private JButton				bAdd			= new JButton("Add");		// add button
	private JButton				bRemove			= new JButton("Remove");	// remove button
	private ListDecorator mirrorModel;
	
	private JButton				redoB			= new JButton("Redo");		// add button
	private JButton				undoB			= new JButton("Undo");	// remove button
	
	UndoManager m;
	UndoRedoAdd undoAdd;
	
	public Main() {
		init();
	}
	
	public void init() {
		// initialize model
		model = new DefaultListModel();
		
		// TODO 1: populate model
		
		model.addElement("CPL");
		model.addElement("SO");
		model.addElement("IA");
		
		mirrorModel = new ListDecorator(model);
		
		// initialize lists, based on the same model
		list	= new JList(model);
		mirror	= new JList(mirrorModel);
		
		// TODO 6: redefine mirror so as to use a ReverseListModel instance on top of 'model'
		
		// main panel: top panel, bottom panel
		JPanel top = new JPanel(new GridLayout(1, 0)); // 1 row, any number of columns
		JPanel bottom = new JPanel(new FlowLayout());
		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		// top panel: the two lists (scrollable)
		top.add(new JScrollPane(list));
		top.add(new JScrollPane(mirror));
		
		// bottom panel: name field, add button, remove button
		bottom.add(tName);
		bottom.add(bAdd);
		bottom.add(bRemove);
		bottom.add(redoB);
		bottom.add(undoB);
		
		m = new UndoManager();
		undoAdd = new UndoRedoAdd(model, m);
		
		undoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoAdd.undo();
			}
		});
		redoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoAdd.redo();
			}
		});
		
		bAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 2: call the method for obtaining the text field's content
				String text = tName.getText();
				
				System.out.println(text);
				if (text.isEmpty()) {
					JOptionPane.showMessageDialog(
							null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// TODO 3: add new element to model
				if (!model.contains(text)) {
					model.addElement(text);
					m.add(text);
				}
			}
		});
		
		bRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue() != null) {
					model.removeElement(list.getSelectedValue());
				}
			}
		});
		
		// TODO 4: add listener for Remove button
	}
	
	public static void buildGUI() {
		JFrame frame = new JFrame("Swing stuff"); // title
		frame.setLocation(500, 250);
		frame.setContentPane(new Main()); // content: the JPanel above
		frame.setSize(300, 300); // width / height
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}


	public static void main(String[] args) {
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});
	}

}
