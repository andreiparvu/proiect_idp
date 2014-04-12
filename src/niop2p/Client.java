package niop2p;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Client extends NioServer implements IClient {
	// The host:port combination to listen on

	Map <String, FileData> fileContents = new HashMap <String, FileData> ();
	
	public Client(String clientHost, int clientPort) 
	throws IOException {
		this(InetAddress.getByName(clientHost), clientPort, new EchoWorker());
	}
	
	public Client(InetAddress myAddress, int myPort, IWorker worker) 
			throws IOException {
		// call super-constructor
		super(myAddress, myPort, worker);
	}

	private SocketChannel initiateConnection(InetAddress address, int port) throws IOException {
		SocketChannel socketChannel;

		// Create a non-blocking socket channel
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);

		// Kick off connection establishment
		socketChannel.connect(new InetSocketAddress(address, port));
			
		// Queue a channel registration since the caller is not the
		// selecting thread. As part of the registration we'll register
		// an interest in connection events. These are raised when a channel
		// is ready to complete connection establishment.
		synchronized (this.changeRequests) {
			this.changeRequests.add(new ChangeRequest(socketChannel, 
					ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
		}

		return socketChannel;
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
						case ChangeRequest.REGISTER:
							change.socket.register(this.selector, change.ops);
							break;
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
					if (key.isConnectable()) {
						finishConnection(key);
					} else if (key.isAcceptable()) {
						accept(key);
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

	private void finishConnection(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Finish the connection. If the connection operation failed
		// this will raise an IOException.
		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			// Cancel the channel's registration with our selector
			key.cancel();
			return;
		}

		// Register an interest in writing on this channel
		key.interestOps(SelectionKey.OP_WRITE);
	}

	public void send(InetAddress address, int port, byte[] data) throws IOException {
		// Start a new connection
		SocketChannel socket = this.initiateConnection(address, port);

		// Register the response handler

		// And queue the data we want written
		synchronized (this.pendingData) {
			List<ByteBuffer> queue = this.pendingData.get(socket);
			if (queue == null) {
				queue = new ArrayList<ByteBuffer>();
				this.pendingData.put(socket, queue);
			}
			queue.add(ByteBuffer.wrap(data));
		}

		// Finally, wake up our selecting thread so it can make the required
		// changes
		this.selector.wakeup();
	}
	
	void sendMessage(InetAddress address, int port, IMessage message) throws IOException {
		Yaml yaml = new Yaml();
		send(address,port, (yaml.dump(message) + IMessage.delimitator).getBytes());
	}
	
	@Override
	public void publishFile(File file) throws IOException {
		fileContents.put(file.getName(), new FileData(file));
	}
	
	@Override
	public File retrieveFile(InetAddress address, int port, String filename) throws IOException {
		File file = new File(filename);
		if (file.exists()) {
			System.out.println("File " + filename + " already exists at " + file.getAbsolutePath());
			return file;
		}
		
		FdQueryMessage message = new FdQueryMessage(filename);
		int nChunks = Integer.MAX_VALUE;
		
		// send my wishes to file's owner
		sendMessage(address, port, message);
		
		while (fileContents.get(filename) == null || !fileContents.get(filename).isComplete()) {
			try {
				// wait for some more data
				Thread.sleep(1000);
				
				if (fileContents.get(filename) != null) {
					System.out.println("waiting for " + fileContents.get(filename).chunksLeft);
					if (fileContents.get(filename).chunksLeft == nChunks) {
						// I'm done, my file is complete
						System.out.println("wakin up!");
						worker.processRemainingData();
					}
					
					// update my waiting count
					nChunks = fileContents.get(filename).chunksLeft;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// spit new file cuz I have all I need
		return fileContents.get(filename).newFile();
	}
	
	/**
	 * How to run locally:
	 * 	- create new Client on the listen port of your choice
	 * 	- publish one or more files
	 * 	- start
	 * 	- delete the file(s) you just published (they're all in memory)
	 * 
	 * Then:
	 * 	- create a Client on another port
	 * 	- call retrieveFile on the address and port of the first Client
	 * 	- start
	 * 	- watch a new file magically appearing 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client client = new Client(InetAddress.getByName("localhost"), 1265,
					new EchoWorker());
			Thread t = new Thread(client);
			t.start();
//			client.publishFile(new File("kids.jpg"));
//			Thread.sleep(10000);
			client.retrieveFile(InetAddress.getByName("localhost"), 1264, "kids.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}