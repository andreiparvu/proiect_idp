import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class ListDecorator extends DefaultListModel {
	DefaultListModel decoratedObject;
	
	public ListDecorator(DefaultListModel decoratedObject) {
		this.decoratedObject = decoratedObject;
		
		decoratedObject.addListDataListener(new ListDataListener() {
			public void intervalAdded(ListDataEvent e) {
				ListDecorator.this.add(0,
						ListDecorator.this.decoratedObject.getElementAt(e.getIndex0()));
			}
			public void contentsChanged(ListDataEvent e) {
		    }
		    public void intervalRemoved(ListDataEvent e) {
		    	ListDecorator.this.remove(ListDecorator.this.decoratedObject.getSize() - e.getIndex0());
		    }
		});
	}
	
	public int getSize() {
		return decoratedObject.getSize();
	}
	
	public Object getElementAt(int pos) {
		return decoratedObject.getElementAt(decoratedObject.getSize() - pos - 1);
	}
	
	public void addElement(Object o) {
		decoratedObject.add(0, o);
	}
}
