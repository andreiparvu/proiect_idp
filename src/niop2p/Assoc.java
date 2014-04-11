package niop2p;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Assoc {
	
	private Map<String, Map<Integer, List<PeerAddress>>> map;
	private Map<String, FileDescription> fds;
	
	public Assoc() {
		map = new HashMap<String, Map<Integer, List<PeerAddress>>>();
		fds = new HashMap<String, FileDescription>();
	}

	public void addFileDescription(byte[] address, int port, FileDescription fd) {
		fds.put(fd.filename, fd);
		Map<Integer, List<PeerAddress>> m = map.get(fd.filename);
		
		if (m == null) {
			m = new HashMap<Integer, List<PeerAddress>>();
			map.put(fd.filename, m);
		}

		for (int i = 0; i < fd.getNChunks(); i++) {
			List<PeerAddress> list = m.get(i);
			if (list == null) {
				list = new LinkedList<PeerAddress>();
				m.put(i, list);
			}

			list.add(new PeerAddress(address, port));
		}
	}

	public void addChunk(byte[] address, int port, String filename, int chunkId) {
		Map<Integer, List<PeerAddress>> m = map.get(filename);
		List<PeerAddress> list = m.get(chunkId);
		list.add(new PeerAddress(address, port));
	}

	public List<PeerAddress> peersForChunk(String filename, int chunkId) {
		return map.get(filename).get(chunkId);
	}
	
	public FileDescription getFileDescription(String filename) {
		return fds.get(filename);
	}
	
	public Map<Integer, List<PeerAddress>> getPeerList(String filename) {
		return map.get(filename);
	}
	
	public void addPeerForChunk(InetAddress address, int port, String filename, int chunkIndex) {
		
	}
}