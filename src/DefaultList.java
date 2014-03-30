import javax.swing.DefaultListModel;
import javax.swing.JList;


// Base class for the FileList and UserList classes
public abstract class DefaultList extends JList<String> {
  private static final long serialVersionUID = 8488210035925008474L;

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
