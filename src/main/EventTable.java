package main;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


// Class for the event table
public class EventTable extends JTable {
  private static final long serialVersionUID = 9152820225895134313L;

  DefaultTableModel model;
  Mediator med;
  ArrayList<JProgressBar> progressBars;
  ArrayList<Float> progressBarValues;
  HashMap<String, Integer> allFiles;
  ArrayList<Object[]> allData;

  static Logger logger = Logger.getLogger(EventTable.class);

  public EventTable(Mediator med, DefaultTableModel model) {
    super(model);

    logger.addAppender(MainWindow.appender);

    this.model = model;
    this.med = med;

    med.registerEventTable(this);

    progressBars = new ArrayList<>();
    progressBarValues = new ArrayList<>();
    allFiles = new HashMap<>();
    allData = new ArrayList<>();

    // Set the renderer for the progress bar column
    getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRenderer());
  }

  private class ProgressBarRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      return progressBars.get(row);
    }
  }

  private boolean containsEntry(String user, String file, boolean isDownloading) {
    String from, to;

    if (isDownloading) {
      to = "_me_";
      from = user;
    } else {
      to = user;
      from = "_me_";
    }

    for (Object[] rowData: allData) {
      if (rowData[0].equals(from) &&
          rowData[1].equals(to) &&
          rowData[2].equals(file))
        return true;
    }

    return false;
  }

  public void addEntry(String user, String file, boolean isDownloading) {
    if (containsEntry(user,file, isDownloading))
      return;

    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setStringPainted(true);

    String status = null, to = null, from = null;

    // Depending on direction, set the source and destination
    if (isDownloading) {
      status = "Receiving...";
      to = "_me_";
      from = user;
    } else {
      status = "Sending...";
      to = user;
      from = "_me_";
    }

    progressBars.add(progressBar);
    progressBarValues.add(0f);

    Object[] rowData = {from, to, file, progressBar, status};
    model.addRow(rowData);

    allData.add(rowData);
    allFiles.put(file, progressBars.size() - 1);

    logger.info("Added entry " + status + " " + file + " from " + from +
    		" to " + to);
  }

  public void updateProgressBar(String file, float part) {
    int index = allFiles.get(file);
    JProgressBar progressBar = progressBars.get(index);

    // Increase the value of the progress bar
    float oldValue = progressBarValues.get(index);

    float newValue = oldValue + part;
    if (newValue > 100) {
    	newValue = 100;
    }

    progressBarValues.set(index, newValue);

    progressBar.setValue((int)newValue);
    progressBar.setString((int)newValue + "%");
    if (newValue == 100) {
      // Change state if completed
      model.setValueAt("Completed.", index, 4);
    }

    // Must update the table
    model.fireTableChanged(new TableModelEvent(model, index));

    logger.info("Received " + part + " of " + file);
  }

  public int getProgress(String file) {
    int index = allFiles.get(file);
    JProgressBar progressBar = progressBars.get(index);
    return progressBar.getValue();
  }
}
