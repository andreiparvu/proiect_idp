package niop2p;
import java.net.InetAddress;


public class AnnounceMessage implements IMessage {

	public InetAddress address;
	public int port;
	
	public AnnounceMessage(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public AnnounceMessage() {
		// deliberately empty
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
