package app;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import worker.ExportTask;
import buttons.CircleButton;
import buttons.ClearButton;
import buttons.ExportButton;
import buttons.RectangleButton;
import buttons.UndoButton;
import drawings.JCanvas;

public class Main extends JFrame implements ActionListener {
	private static final long serialVersionUID = -1650911041239669953L;

	JToolBar toolbar;
	JLabel   progressLabel;
	Mediator med;
	ExportTask task;
	final JProgressBar progressBar;
	
	public Main() {
		super("Drawing");
		this.setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		task = new ExportTask();
		
		JPanel jp = new JPanel();
		jp.setSize(400, 100);
		getContentPane().add(jp);
		med = new Mediator();

		GridBagLayout gbl = new GridBagLayout();
		gbl.layoutContainer(this);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill   = GridBagConstraints.BOTH;
		constraints.weightx = 500;
		constraints.weighty = 400;
			
		gbl.setConstraints(jp, constraints);
		
		jp.setLayout(gbl);
		
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		jp.add(toolbar, constraints);
		
		RectangleButton rect = new RectangleButton(this, med);
		toolbar.add(rect);
		CircleButton circ = new CircleButton(this, med);
		toolbar.add(circ);
		toolbar.addSeparator();
		UndoButton undo = new UndoButton(this, med);
		toolbar.add(undo);
		ClearButton clr = new ClearButton(this, med);
		toolbar.add(clr);
		toolbar.addSeparator();
		ExportButton export = new ExportButton(this, med);
		toolbar.add(export);
		
		JCanvas canvas = new JCanvas(med);
		constraints.fill = GridBagConstraints.HORIZONTAL;
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    constraints.ipady = 300;
	    canvas.setSize(300, 300);
		jp.add(canvas, constraints);

	    progressBar = new JProgressBar(0, 10);
		progressBar.setSize(10, 10);
		export.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  //TODO 3.2 - execute export task
		    	  
		    	  Main.this.task.execute();
		      }
		});
		
	    //TODO 3.5  - add property change listener to export task
		task.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().compareTo("progress") == 0) {
					progressBar.setValue((Integer)e.getNewValue());
				}
			}
		});
		
	    progressLabel = new JLabel("Export image ...");
	    
		JPanel cp = new JPanel();
	    LayoutManager layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
	    cp.setLayout(layout);
	    cp.add(progressLabel);
	    cp.add(progressBar);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    constraints.ipady = 10;
	    jp.add(cp, constraints);
	    
		MouseApp map = new MouseApp(med);
		canvas.addMouseListener(map);
		MouseMoveApp mvap = new MouseMoveApp(med);
		canvas.addMouseMotionListener(mvap);
		setSize(new Dimension(500, 400));
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		//TODO 1.2 - obtain the command from the source of the event and execute it
		
		((Command)e.getSource()).execute();
	}

	static public void main(String[] argv) {
		new Main();
	}
}

