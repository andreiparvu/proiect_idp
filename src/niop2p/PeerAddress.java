package niop2p;

public class PeerAddress {
	
	public byte[] address;
	public int port;
	
	public PeerAddress(byte[] address, int port) {
		this.address = address;
		this.port = port;
	}

	public PeerAddress() {
		
	}
}
