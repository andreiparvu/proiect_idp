import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class EventTable extends JTable {
  DefaultTableModel model;
  Mediator med;
  
  public EventTable(Mediator med, DefaultTableModel model) {
    super(model);
    
    this.model = model;
    this.med = med;
    
    med.registerEventTable(this);
  }
  
  public void addEntry(String user, String file, boolean isDownloading) {
    JProgressBar progressBar = new JProgressBar(0, 10);
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

    progressBar.setString("0%");
    
    Object[] rowData = {"_me_", user, file, progressBar, status};
    model.addRow(rowData);
  }
}
