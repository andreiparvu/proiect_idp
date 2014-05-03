package niop2p;

public class ChunkRequestMessage implements IMessage {

	public String filename;
	public int chunkIndex;
	
	public ChunkRequestMessage(String filename, int chunkIndex) {
		this.filename = filename;
		this.chunkIndex = chunkIndex;
	}
	
	public ChunkRequestMessage() {
		// intentionally left blank
	}

	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
