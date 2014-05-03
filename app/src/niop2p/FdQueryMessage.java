package niop2p;

public class FdQueryMessage implements IMessage {

	public String filename;
	
	public FdQueryMessage(String filename) {
		this.filename = filename;
	}
	
	public FdQueryMessage() {
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}

}
