package main;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;


public class FileList extends DefaultList {

  private static final long serialVersionUID = -5364537998629640357L;

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

      // A file has been selected - start download
      med.startDownload();
    }
  }
}
