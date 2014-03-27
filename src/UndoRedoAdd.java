import javax.swing.DefaultListModel;


public class UndoRedoAdd implements Undoable {
	private DefaultListModel model;
	private UndoManager manager;
	
	public UndoRedoAdd(DefaultListModel m, UndoManager manager) {
		model = m;
		this.manager = manager;
	}
	
	public void undo() {
		if (manager.pos >= 0) {
			model.removeElement(manager.getCur());
			manager.pos--;
		}
	}
	public void redo() {
		if (manager.pos < manager.size() - 1) {
			manager.pos++;
			model.addElement(manager.getCur());
		}
	}
}
