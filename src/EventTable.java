import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class EventTable extends JTable {
  DefaultTableModel model;
  Mediator med;
  ArrayList<JProgressBar> progressBars;
  HashMap<String, Integer> allFiles;
  ArrayList<Object[]> allData;
  
  public EventTable(Mediator med, DefaultTableModel model) {
    super(model);
    
    this.model = model;
    this.med = med;
    
    med.registerEventTable(this);
    
    progressBars = new ArrayList<>();
    allFiles = new HashMap<>();
    allData = new ArrayList<>();
    
    getColumnModel().getColumn(3).setCellRenderer(new ProgressBarRenderer());
  }
  
  private class ProgressBarRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
      return progressBars.get(row);
    }
  }
  
  public void addEntry(String user, String file, boolean isDownloading) {
    JProgressBar progressBar = new JProgressBar(0, 100);
    progressBar.setStringPainted(true);
    
    String status = null, to = null, from = null;
    
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
    
    Object[] rowData = {"_me_", user, file, progressBar, status};
    model.addRow(rowData);
    
    allData.add(rowData);
    allFiles.put(file, progressBars.size() - 1);
  }
  
  public void updateProgressBar(String file, int part) {
    int index = allFiles.get(file);
    JProgressBar progressBar = progressBars.get(index);
    
    System.out.println(index);
    
    int oldValue = progressBar.getValue();
    
    progressBar.setValue(oldValue + part);
    progressBar.setString(oldValue + part + "%");
    if (oldValue + part == 100) {
      model.setValueAt("Completed.", index, 4);
    }
    
    model.fireTableChanged(new TableModelEvent(model, index));
  }
}
