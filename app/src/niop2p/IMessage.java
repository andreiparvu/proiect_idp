package niop2p;

public interface IMessage {

	String delimitator = "iamasupermessageyoushallnotpass";
	
	/**
	 * Allows the given worker to process this message. This should be the first call
	 * in a double-dispatch mechanism, in which the <code>process()</code> method callback from
	 * the worker is the second.
	 * 
	 * @param worker	the IWorker instance that will process this message
	 * @param event		the data correlated with the processing of this message
	 */
	void allowProcessing (IWorker worker, ServerDataEvent event);
}
