package niop2p;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import main.MainWindow;

import org.apache.log4j.Logger;


public class FileData {

	FileDescription fd;
	byte[][] data;
	int chunksLeft;
	
	static Logger logger = Logger.getLogger(FileData.class);
	
	public FileData (File file) {
		this(new FileDescription(file));
		populateData(file);
		chunksLeft = 0;
		
		logger.addAppender(MainWindow.appender);
	}
	
	public FileData (FileDescription fileDescription) {
		fd = fileDescription;
		chunksLeft = (int) fd.getNChunks();
		data = new byte[chunksLeft][fd.chunkSize];
		
		logger.addAppender(MainWindow.appender);
	}
	
	public byte[] getChunkData(int index) {
		int chunkSize;
		if (index < fd.getNChunks() - 1)
			chunkSize = fd.chunkSize;
		else
			chunkSize = (int) (fd.totalSize - (fd.getNChunks() - 1)  * fd.chunkSize);
		
		byte[] copy = new byte[chunkSize];
		System.arraycopy(data[index], 0, copy, 0, chunkSize);
		
		return copy;
	}
	
	private void populateData(File file) {
		try {
			FileInputStream fis = new FileInputStream (file);
			for (int index = 0; index < data.length; index++) {
				fis.read(data[index]);
			}
			fis.close();
		} catch (Exception e) {
			logger.error("Exception caught in populateData for file " + file.getName() + "!");
			e.printStackTrace();
		}
	}
	
	public void storeData (int chunkIndex, byte [] data) {
		System.arraycopy(data, 0, this.data[chunkIndex], 0, data.length);
		chunksLeft --;
	}
	
	public boolean isComplete() {
		return chunksLeft == 0;
	}
	
	public File newFile() {
		try {
			buildFile();
			return new File(fd.filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void buildFile() throws IOException {
		RandomAccessFile raf = new RandomAccessFile(fd.filename, "rw");
		FileChannel fc = raf.getChannel();
		MappedByteBuffer mb;
		
		for (int i = 0; i < fd.getNChunks(); i++){
			int chunkSize;
			if (i < fd.getNChunks() - 1)
				chunkSize = fd.chunkSize;
			else
				chunkSize = (int) (fd.totalSize - (fd.getNChunks() - 1)  * fd.chunkSize);
			mb = fc.map(FileChannel.MapMode.READ_WRITE, i * fd.chunkSize, i * fd.chunkSize + chunkSize);
			mb.put(data[i]);
		}
		
		fc.close();
		raf.close();
	}
}
