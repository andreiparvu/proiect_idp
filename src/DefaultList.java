import javax.swing.DefaultListModel;
import javax.swing.JList;


public abstract class DefaultList extends JList<String> {
  DefaultListModel<String> model;
  
  DefaultList(DefaultListModel<String> model) {
    super(model);
    
    this.model = model;
  }
  
  public void addElement(String user) {
    model.addElement(user);
  }
  
  public void removeElement(String user) {
    model.removeElement(user);
  }
  
  public void removeElements() {
    model.removeAllElements();
  }
}
