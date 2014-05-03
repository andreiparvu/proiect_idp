package main;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import niop2p.Client;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Network {
  Mediator med;
  private Client client;
  private ExecutorService pool = Executors.newCachedThreadPool();

  static Logger logger = Logger.getLogger(Network.class);
  static {
    logger.addAppender(new ConsoleAppender(new PatternLayout()));
  }

  public Network(Mediator med, String myAddress, int myPort) {
    this.med = med;
    if (MainWindow.appender != null) {
    	logger.addAppender(MainWindow.appender);
    }

    try {
      client = new Client(myAddress, myPort, med);
      pool.execute(client);
    } catch (IOException ex) {
      logger.error("Error in creating client");
      ex.printStackTrace();
    }
  }

  public void close() {
  	client.close();
  }

  public void publishFile(File file) {
    logger.info("Publishing " + file.getName());
    try {
      client.publishFile(file);
    } catch (IOException ex) {
      logger.error("Error in publishing " + file.getName());
      ex.printStackTrace();
    }
  }

  public void startDownload(final String ip, final int port, final String file) {
    logger.info("Starting download for " + file);
    pool.execute(new Runnable() {
      public void run() {
        try {
          client.retrieveFile(InetAddress.getByName(ip), port, file);
          logger.info(file + " download successful");

        } catch (Exception ex) {
          logger.error("Error in downloading file " + file);
          ex.printStackTrace();
        }
      }
    });
  }
  
  public Client getClient() {
  	return client;
  }
  
  public Mediator getMediator() {
  	return med;
  }
}
