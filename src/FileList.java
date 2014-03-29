import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;


public class FileList extends DefaultList {
  Mediator med;
  public String selectedFile;
  
  public FileList(Mediator med, DefaultListModel<String> model) {
    super(model);
    
    this.med = med;
    med.registerFileList(this);
    
    addMouseListener(new FileMouseAdapter());
  }
  
  private class FileMouseAdapter extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      int index = FileList.this.locationToIndex(e.getPoint());
      
      selectedFile = model.elementAt(index);
      med.startDownload();
    }
  }
}
