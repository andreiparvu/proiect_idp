package niop2p;

public class ChunkPublishMessage implements IMessage {

	public String filename;
	public int chunkIndex;
	
	public ChunkPublishMessage(String filename, int chunkIndex) {
		this.filename = filename;
		this.chunkIndex = chunkIndex;
	}

	public ChunkPublishMessage() {
		// intentionally left blank
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
