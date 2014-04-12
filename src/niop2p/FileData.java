package niop2p;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileData {

	FileDescription fd;
	byte[][] data;
	int chunksLeft;
	
	public FileData (File file) {
		this(new FileDescription(file));
		populateData(file);
		chunksLeft = 0;
	}
	
	public FileData (FileDescription fileDescription) {
		fd = fileDescription;
		chunksLeft = (int) fd.getNChunks();
		data = new byte[chunksLeft][fd.chunkSize];
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
			System.err.println("Exception caught in populateData for file " + file.getName() + "!");
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
	
	public File newFile() throws IOException {
		File file = new File(fd.filename);
		
		if (!isComplete() || !file.createNewFile())
			return null;
		
		FileOutputStream fos = new FileOutputStream(file);
		for (int i = 0; i < fd.getNChunks(); i++) {
			int chunkSize;
			if (i < fd.getNChunks() - 1)
				chunkSize = fd.chunkSize;
			else
				chunkSize = (int) (fd.totalSize - (fd.getNChunks() - 1)  * fd.chunkSize);
			
			fos.write(data[i], 0, chunkSize);
		}
		fos.close();
		return file;
	}
}
