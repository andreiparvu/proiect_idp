package niop2p;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.Mediator;

import org.yaml.snakeyaml.Yaml;

public class EchoWorker implements IWorker {
	
	private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();
	private Map <SocketChannel, StringBuffer> buffers = new HashMap<SocketChannel, StringBuffer>();
	private NioServer master;
	private Mediator med;
	
	// at construction phase
	public EchoWorker(Mediator med) {
		this.med = med;
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
				String[] morti = sb.toString().split("!!niop2p.ChunkReplyMessage");
				for (String jeg : morti) if (jeg.length() > 0){
					IMessage message = (IMessage) yaml.load("!!niop2p.ChunkReplyMessage" + jeg);
					message.allowProcessing(this, new ServerDataEvent(master, key, null));
					sb.delete(0, sb.length());
				}
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
	public void process(FdQueryMessage message, ServerDataEvent event) {

		// TODO build up file description based on file
		FileData fileData = ((Client)master).fileContents.get(message.filename);
		FileDescription fd = fileData.fd;
		FdQueryReply reply = new FdQueryReply(fd);
		
		event.server.sendMessage(event.socket, reply);
	}
	
	@Override
	public void process(FdQueryReply message, ServerDataEvent event) {
		Client owner = (Client)event.server;
		
		synchronized (owner.fileContents) {
			owner.fileContents.put(message.fd.filename, new FileData(message.fd));
		}
		
		for (int i = 0; i < message.fd.getNChunks(); i++) {
			try {
				String filename = message.fd.filename;
				
				((Client)event.server).sendMessage(event.socket, new ChunkRequestMessage(filename,i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void process(ChunkRequestMessage message, ServerDataEvent event) {
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
		Client owner = (Client)event.server;
		
		synchronized (owner.fileContents) {
			owner.fileContents.get(message.filename).storeData(message.chunkIndex, message.data);
			
			FileData fd = owner.fileContents.get(message.filename);
			
			med.addFilePart(message.filename, (float)fd.fd.chunkSize * 100 / fd.fd.totalSize);
		}
	}
}