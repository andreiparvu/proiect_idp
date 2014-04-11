package niop2p;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.yaml.snakeyaml.Yaml;

public class EchoWorker implements IWorker {
	
	private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();
	private Assoc assoc = new Assoc();
	private Map <SocketChannel, StringBuffer> buffers = new HashMap<SocketChannel, StringBuffer>();
	private NioServer master;
	private final Logger logger = Logger.getLogger(EchoWorker.class);
	
	// at construction phase
	public EchoWorker(String logFilename) {
		try {
			FileAppender fileappender = new FileAppender(new PatternLayout(), "logs/" + logFilename);
			logger.addAppender(fileappender);
		} catch (IOException e) {
			System.err.println("IOException at FileAppender creation within " + this.getClass());
		}
	}
	
	public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
		master = server;
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized (queue) {
			queue.add(new ServerDataEvent(server, socket, dataCopy));
			queue.notify();
		}
	}

	public void run() {
		ServerDataEvent dataEvent;
		Yaml yaml = new Yaml();
		
		while (true) {
			processBuffers();
			// Wait for data to become available
			synchronized (queue) {
				while (queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (ServerDataEvent) queue.remove(0);
				String [] msgStrings = new String(dataEvent.data).split(IMessage.delimitator);
				
				for (String string : msgStrings) {
					try {
						// unwrap message
						IMessage message = (IMessage) yaml.load(string);
						message.allowProcessing(this, dataEvent);
					} catch (Exception e) {
						StringBuffer sb = buffers.get(dataEvent.socket);
						if (sb == null) {
							sb = new StringBuffer ("");
							buffers.put(dataEvent.socket, sb);
						}
						
						sb.append(string);
					}
				}
			}
		}
	}
	
	public void processRemainingData() {
		processLastBuffers();
	}
	
	private void processLastBuffers() {
		Yaml yaml = new Yaml();
		
		for (SocketChannel key : buffers.keySet()) {
			StringBuffer sb = buffers.get(key);
			if (sb.length() != 0) {
				IMessage message = (IMessage) yaml.load(sb.toString());
				message.allowProcessing(this, new ServerDataEvent(master, key, null));
				sb.delete(0, sb.length());
			}
		}
	}
	
	private void processBuffers() {
		Yaml yaml = new Yaml();
		
		for (SocketChannel key : buffers.keySet()) {
			StringBuffer sb = buffers.get(key);
			if (sb.length() == 0)
				continue;
			
			try {
				int pos = sb.indexOf(IMessage.delimitator) + IMessage.delimitator.length();
				if (pos < 0)
					throw new Exception();

				IMessage message = (IMessage) yaml.load(sb.substring(0, pos));
				message.allowProcessing(this, new ServerDataEvent(master, key, null));
				sb.delete(0, pos);
			} catch (Exception newe) {
				
			}
		}
	}

	@Override
	public void process(PublishFileMessage m, ServerDataEvent event) {
		assoc.addFileDescription(m.address, m.port, m.description);
		try {
			logger.info("Received publish notification of file " + m.description.filename 
					+ " from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
	}
	
	@Override
	public void process(AnnounceMessage message, ServerDataEvent event) {
		try {
			logger.info("Received announce message from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
	}
	
	@Override
	public void process(FdQueryMessage message, ServerDataEvent event) {
		try {
			logger.info("Received file description request message from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
		
		FileDescription fd = assoc.getFileDescription(message.filename);
		Map<Integer, List<PeerAddress>> list = assoc.getPeerList(message.filename);
		FdQueryReply reply = new FdQueryReply(fd, list);
		
		event.server.sendMessage(event.socket, reply);
	}
	
	@Override
	public void process(FdQueryReply message, ServerDataEvent event) {
		try {
			logger.info("Received file description message for file " + message.fd.filename + 
					" from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
		
		Client owner = (Client)event.server;
		
		synchronized (owner.fileContents) {
			owner.fileContents.put(message.fd.filename, new FileData(message.fd));
		}
		
		for (int i = 0; i < message.fd.getNChunks(); i++)
			try {
				byte[] address = message.peerList.get(i).get(0).address;
				int port = message.peerList.get(i).get(0).port;
				InetAddress peerAddress = InetAddress.getByAddress(address);
				String filename = message.fd.filename;
				
				((Client)event.server).sendMessage(peerAddress, port, new ChunkRequestMessage(filename,i));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void process(ChunkRequestMessage message, ServerDataEvent event) {
		try {
			logger.info("Received chunk request message for chunk " + message.filename + ":" + message.chunkIndex
					+ " from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
		
		Client owner = (Client)event.server;
		
		synchronized (owner.fileContents) {
			byte[] data = owner.fileContents.get(message.filename).getChunkData(message.chunkIndex);
			byte[] copy = new byte[data.length];
			System.arraycopy(data, 0, copy, 0, data.length);
			ChunkReplyMessage reply = new ChunkReplyMessage(message.filename, message.chunkIndex, copy);
			owner.sendMessage(event.socket, reply);
		}
	}
	
	@Override
	public void process(ChunkReplyMessage message, ServerDataEvent event) {
		try {
			logger.info("Received data for chunk " + message.filename + ":" + message.chunkIndex
					+ " from " + event.socket.getRemoteAddress());
		} catch (IOException e) {
			// eat it up
		}
		
		Client owner = (Client)event.server;
		
		synchronized (owner.fileContents) {
			owner.fileContents.get(message.filename).storeData(message.chunkIndex, message.data);
		}
		
		try {
			ChunkPublishMessage notification = new ChunkPublishMessage(message.filename, message.chunkIndex);
			owner.sendMessage(owner.serverAddress, owner.serverPort, notification);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(ChunkPublishMessage message, ServerDataEvent event) {
		try {
			try {
				logger.info("Received chunk publish message for " + message.filename + ":" + message.chunkIndex
						+ " from " + event.socket.getRemoteAddress());
			} catch (IOException e) {
				// eat it up
			}
			
			InetSocketAddress socketAddress = (InetSocketAddress) event.socket.getRemoteAddress();
			InetAddress address = socketAddress.getAddress();
			int port = socketAddress.getPort();
			String filename = message.filename;
			int chunkIndex = message.chunkIndex;
			
			assoc.addPeerForChunk(address, port, filename, chunkIndex);
		} catch (IOException e) {
			System.err.println("IOException at process chunkPublishMessage");
			e.printStackTrace();
		}
	}
}