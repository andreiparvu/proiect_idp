package niop2p;


public class FdQueryReply implements IMessage {

	public FileDescription fd;
	
	public FdQueryReply(FileDescription fd) {
		this.fd = fd;
	}

	public FdQueryReply() {
		// empty
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
