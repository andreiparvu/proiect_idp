package testing;

import java.io.File;
import java.util.Random;

import junit.framework.TestCase;
import main.EventTable;
import main.MainWindow;
import main.Mediator;
import main.Network;

import org.junit.Before;
import org.junit.Test;

public class GUITest extends TestCase {

  Network net1, net2;
  Mediator med1, med2;
  EventTable e1, e2;
  static String ip = "127.0.0.1";
  
  @Before
  protected void setUp() {
  	String[] columnNames = {"Source", "Destination", "File Name", "Progress", "Status"};
  	
  	med1 = new Mediator("daniel");
  	med2 = new Mediator("andrei");
    net1 = new Network(med1, ip, 8000);
    
    e1 = new EventTable(med1, new MainWindow.MyTableModel(columnNames));
    net2 = new Network(med2, ip, 9000);
    e2 = new EventTable(med2, new MainWindow.MyTableModel(columnNames));
  }
  
  public void doTest(final String file, Network source, final Network dest, 
  		int port, final long timeout) {
    final File f = new File("testing/" + file);
    EventTable table = null;
    
    assertTrue(f.exists());
    source.publishFile(f);

    // table is the event table of the *destination*
    if (source == net1)
    	table = e2;
    else if (source == net2)
    	table = e1;
    else
    	fail("Weird network, neither source nor dest from setup()!");
    
    // mark the download
    table.addEntry("gigi", file, true);
    dest.startDownload(ip, port, file);

    Thread thread = new Thread(new Runnable() {
    	@Override
    	public void run() {
    		Random random = new Random();
    		long totalTime = timeout;

  			try {
  				Thread.sleep(1000);
  				
  				// repeated check at random moments
	    		while(totalTime > 10) {
	    				long time = Math.abs(random.nextLong() % totalTime);
	    				Thread.sleep(time);
	    				
	    				// test whether the progressBar shows the real progress
	    				int realProgress = (int)dest.getClient().getProgress(f.getName());
	    				int uiProgress = dest.getMediator().getProgress(f.getName());
	    				
	    				// a 0 progress in UI means that either the file is complete or not yet started
	    				if (realProgress == 100 && uiProgress == 0) {
	    			    File newFile = new File(file);
	    			    assertTrue(newFile.exists());
	    				} else 
	    					assertEquals(realProgress, uiProgress);
	    				
	    				totalTime -= time;
	    				System.out.println("still have to wait: " + totalTime);
	    		}
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  				fail();
  			}
    	}
    });
    
    thread.start();
    try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
    
    File newFile = new File(file);
    assertTrue(newFile.exists());

    newFile.delete();
  }
  
  @Test
  public void testDownloadSmall() {
    doTest("some_file.txt", net1, net2, 8000, 3000);
    net1.close();
    net2.close();
  }

  @Test
  public void testDownloadMedium() {
    doTest("medium.txt", net1, net2, 8000, 3000);
    net1.close();
    net2.close();
  }

  @Test
  public void testDownloadBig() {
    doTest("kids.jpg", net2, net1, 9000, 3000);
    net1.close();
    net2.close();
  }
}
