package niop2p;
import java.util.List;
import java.util.Map;


public class FdQueryReply implements IMessage {

	public FileDescription fd;
	public Map<Integer, List<PeerAddress>> peerList;
	
	public FdQueryReply(FileDescription fd, Map<Integer, List<PeerAddress>> peerList) {
		this.fd = fd;
		this.peerList = peerList; 
	}

	public FdQueryReply() {
		// empty
	}
	
	@Override
	public void allowProcessing(IWorker worker, ServerDataEvent event) {
		worker.process(this, event);
	}
}
