package main;
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

  public void addElement(String el) {
    model.addElement(el);
  }

  public boolean hasElement(String el) {
    return model.indexOf(el) != -1;
  }
  public void removeElement(String el) {
    model.removeElement(el);
  }

  public void removeElements() {
    model.removeAllElements();
  }
}
