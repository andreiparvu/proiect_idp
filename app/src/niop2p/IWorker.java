package niop2p;
import java.nio.channels.SocketChannel;


public interface IWorker extends Runnable {
	
	/**
	 * Processes the given data (as a byte []) that has just been received at the NioServer from
	 * the given SocketChannel.
	 * 
	 * @param owner	the NioServer that has just received this data
	 * @param sc	the SocketChannel through which the owner has received the data
	 * @param array	the actual data
	 * @param nBytes	the number of bytes contained
	 */
	void processData (NioServer owner, SocketChannel sc, byte [] array, int nBytes);
	
	/**
	 * Wakes up this worker and processes the remaining data.
	 */
	void processRemainingData();
	
	/**
	 * Handled by the <b>central node</b>. Receives file description requests from a client
	 * and replies with the appropriate message. Sends back an FdQueryReply message, containing
	 * the requested file description and the list of all peers that contain the file chunks.
	 * 
	 * @param message	the message to be processed
	 * @param event		the ServerDataEvent that recorded the sender and receiver of this message
	 */
	void process(FdQueryMessage message, ServerDataEvent event);
	
	/**
	 * Handled by a <b>client/peer</b>. Receives a file description message from the 
	 * central node and, after the information has been processed, creates and sends
	 * data request messages from the other peers that have the data chunks required.
	 * 
	 * @param message	the message to be processed
	 * @param event		the ServerDataEvent that recorded the sender and receiver of this message
	 */
	void process(FdQueryReply message, ServerDataEvent event);
	
	/**
	 * Handled by <b>clients/peers</b>. Receives an processes a message that requests data 
	 * corresponding to a particular file and chunk index, then responds with a ChunkReplyMessage.
	 * 
	 * @param message	the message to be processed
	 * @param event		the ServerDataEvent that recorded the sender and receiver of this message 
	 */
	void process(ChunkRequestMessage message, ServerDataEvent event);
	
	/**
	 * Handled by <b>peers/clients</b>. Receives actual data for a file chunk, stores it in the client's own
	 * data storage and notifies the central node that this chunk is available for download.
	 * 
	 * @param message	the message to be processed
	 * @param event		the ServerDataEvent that recorded the sender and receiver of this message
	 */
	void process(ChunkReplyMessage message, ServerDataEvent event);
	
}
