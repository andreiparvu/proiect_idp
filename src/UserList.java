import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;


public class UserList extends DefaultList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 379567853482462427L;
	
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
        // get files from webservice/mediator
        med.showFiles(selectedUser);
        med.setStatus(Mediator.GETING_FILES_FROM, selectedUser);
      }
    }
  }
}
