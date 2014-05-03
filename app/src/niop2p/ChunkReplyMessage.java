package niop2p;

public class ChunkReplyMessage implements IMessage {

	public String filename;
	public int chunkIndex;
	public byte[] data;
	
	public ChunkReplyMessage(String filename, int chunkIndex, byte[] data) {
		this.filename = filename;
		this.chunkIndex = chunkIndex;
		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
	}

	public ChunkReplyMessage() {
		// intentionally left blank
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	} 
}
