package worker;

import java.util.List;

import javax.swing.SwingWorker;

public class ExportTask extends SwingWorker<Integer, Integer> {
	  private static final int DELAY = 1000;

	  public ExportTask() {
		  System.out.println(Thread.currentThread());
	  }

	  @Override
	  protected Integer doInBackground() throws Exception {
		  // TODO 3.2
		
		  System.out.println(Thread.currentThread());
		  System.out.println("da");
		  int DELAY = 1000;
		  int count = 10;
		  int i     = 0;
		  try {
			  while (i < count) {
				  i++;
				  Thread.sleep(DELAY);
				  publish(i);
			  }
		  } catch (InterruptedException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		
		  return 0;
	  }
	   
	  protected void process(List<Integer> chunks) {
		  System.out.println(chunks);
		  setProgress(chunks.get(0));
//		  System.out.println(Thread.currentThread());
	  }

	  @Override
	  protected void done() {
	    if (isCancelled())
	      System.out.println("Cancelled !");
	    else
	      System.out.println("Done !");
	  }
	}