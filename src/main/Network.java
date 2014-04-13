package main;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import niop2p.Client;

public class Network {
  Mediator med;
  private Client client;
  private ExecutorService pool = Executors.newCachedThreadPool();

  static Logger logger = Logger.getLogger(Network.class);

  public Network(Mediator med, String myAddress, int myPort) {
    this.med = med;

    try {
      client = new Client(myAddress, myPort, med);
      pool.execute(client);
    } catch (IOException ex) {
      logger.error("Error in creating client");
      ex.printStackTrace();
    }
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
}
