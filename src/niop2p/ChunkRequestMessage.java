package niop2p;

public class ChunkRequestMessage implements IMessage {

	public String filename;
	public int chunkIndex;
	public int port;
	
	public ChunkRequestMessage(String filename, int chunkIndex, int port) {
		this.filename = filename;
		this.chunkIndex = chunkIndex;
		this.port = port;
	}
	
	public ChunkRequestMessage() {
		// intentionally left blank
	}

	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
