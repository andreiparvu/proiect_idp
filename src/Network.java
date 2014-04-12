import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {
	Mediator med;
	private ExecutorService pool = Executors.newCachedThreadPool();
	
	public Network(Mediator med) {
		this.med = med;
	}
	
	private class DownloadThread extends Thread {
		MappedByteBuffer fileBuffer;
		String ip;
		int port;
		
		public DownloadThread(String ip, int port, String file) {
			try {
				this.ip = ip;
				this.port = port;
				
				RandomAccessFile raf = new RandomAccessFile(file, "w");
				
				FileChannel fc = raf.getChannel();
				fileBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1000);
				// Change limit ^
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			try {
				SocketChannel s = SocketChannel.open(new InetSocketAddress(ip, port));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void startDownload(String ip, int port, String file) {
		System.out.println(ip + " " + port + " " + file);
		
		pool.execute(new DownloadThread(ip, port, file));
	}
}
