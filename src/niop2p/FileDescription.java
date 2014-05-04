package niop2p;
import java.io.File;


public class FileDescription {

  public String filename;
  public long totalSize;
  public int chunkSize;
  public static int MAX_CHUNK_SIZE = 40960;

  public FileDescription(String filename, long totalSize, int chunkSize) {
    this.filename = filename;
    this.totalSize = totalSize;
    this.chunkSize = chunkSize;
  }

  public FileDescription() {
    // empty constructor
  }

  public FileDescription(File file) {
    filename = file.getName();
    totalSize = file.length();
    chunkSize = MAX_CHUNK_SIZE;
  }

  public long getNChunks() {
    return totalSize / chunkSize + 1;
  }

  public String toString() {
    return "[" + filename + ":" + totalSize + " / " + chunkSize + " => " + getNChunks() + " chunks ]";
  }
}
