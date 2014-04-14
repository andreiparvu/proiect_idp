package niop2p;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.MainWindow;
import main.Mediator;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

public class NioServer implements Runnable {
	// The host:port combination to listen on
	InetAddress myAddress;
	int myPort;

	// The channel on which we'll accept connections
	ServerSocketChannel serverChannel;

	// The selector we'll be monitoring
	Selector selector;

	// The buffer into which we'll read data when it's available
	ByteBuffer readBuffer = ByteBuffer.allocate(81920);

	// the worker
	IWorker worker;

	// A list of ChangeRequest instances
	List<ChangeRequest> changeRequests = new LinkedList<ChangeRequest>();

	// Maps a SocketChannel to a list of ByteBuffer instances
	Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();

	static Logger logger = Logger.getLogger(NioServer.class);
	
	public NioServer(String address, int port, Mediator med) throws IOException {
		this (InetAddress.getByName(address), port, new EchoWorker(med));
	}
	
	public NioServer(InetAddress hostAddress, int port, IWorker worker) throws IOException {
		logger.addAppender(MainWindow.appender);
		
		this.myAddress = hostAddress;
		this.myPort = port;
		this.selector = this.initSelector();
		this.worker = worker;
		new Thread(worker).start();
	}

	public NioServer() {
	}

	Selector initSelector() throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.myAddress, this.myPort);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in
		// accepting new connections
		serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		return socketSelector;
	}

	public void run() {
		while (true) {
			try {

				// Process any pending changes
				synchronized (this.changeRequests) {
					Iterator<ChangeRequest> changes = this.changeRequests.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = (ChangeRequest) changes.next();
						switch (change.type) {
						case ChangeRequest.CHANGEOPS:
							SelectionKey key = change.socket.keyFor(this.selector);
							key.interestOps(change.ops);
						}
					}
					this.changeRequests.clear();
				}

				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						read(key);
					} else if (key.isWritable()) {
						write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void accept(SelectionKey key) throws IOException {
		// For an accept to be pending the channel must be a server socket
		// channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		// Socket socket = socketChannel.socket();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = socketChannel.read(this.readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			logger.info("Someone force-closed the server instance!");
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			logger.info("Someone closed cleanlythe server instance.");
			key.channel().close();
			key.cancel();
			return;
		}

		// Hand the data off to our worker thread
		this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
	}

	/**
	 * Called from another thread!
	 * 
	 * @param socket
	 * @param data
	 */
	public void send(SocketChannel socket, byte[] data) {
		synchronized (this.changeRequests) {
			// Indicate we want the interest ops set changed
			this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}
				queue.add(ByteBuffer.wrap(data));
			}
		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}

	public void sendMessage(SocketChannel socket, IMessage message) {
		Yaml yaml = new Yaml();
		String string = yaml.dump(message) + IMessage.delimitator;
		send(socket, string.getBytes());
	}
	
	void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			List<ByteBuffer> queue = this.pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				ByteBuffer buf = (ByteBuffer) queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}
			
			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}
}