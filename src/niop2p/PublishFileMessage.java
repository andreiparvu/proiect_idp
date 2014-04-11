package niop2p;

public class PublishFileMessage implements IMessage {
	
	public byte[] address;
	public int port;
	public FileDescription description;

	public PublishFileMessage(byte[] address, int port, FileDescription description) {
		this.address = address;
		this.port = port;
		this.description = description;
	}

	public PublishFileMessage() {
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}

}
