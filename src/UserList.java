import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;


public class UserList extends DefaultList {
	Mediator med;
	public String selectedUser;
	
	public UserList(Mediator med, DefaultListModel<String> model) {
	  super(model);
	  
		med.registerUserList(this);
		
		this.med = med;
		
		addMouseListener(new UserMouseAdapter());
	}
	
	private class UserMouseAdapter extends MouseAdapter {
    int lastClicked = -1;
   
    public void mouseClicked(MouseEvent e) {
      int index = UserList.this.locationToIndex(e.getPoint());
      
      if (index != lastClicked) {
        selectedUser = model.elementAt(index);
        med.showFiles(model.elementAt(index));
      }
    }
  }
  
}
