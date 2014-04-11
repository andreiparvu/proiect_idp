package niop2p;
import java.nio.channels.SocketChannel;


public class ServerDataEvent {

	NioServer server;
	SocketChannel socket;
	byte[] data;
	
	public ServerDataEvent(NioServer server, SocketChannel sc, byte[] data) {
		this.server = server;
		this.socket = sc;
		this.data = data;
	}
}
