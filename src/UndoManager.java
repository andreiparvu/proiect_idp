import java.util.ArrayList;


public class UndoManager {
	private ArrayList<String> elements = new ArrayList<String>();
	public int pos;
	
	public int getPos() {
		return pos;
	}
	
	public int size() {
		return elements.size();
	}
	
	public String getCur() {
		return elements.get(pos);
	}
	
	public void add(String el) {
		elements.add(el);
		pos = elements.size() - 1;
	}
}
