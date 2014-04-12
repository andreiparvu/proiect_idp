package main;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import niop2p.Client;

public class Network {
	Mediator med;
	private Client client;
	private ExecutorService pool = Executors.newCachedThreadPool();
	
	public Network(Mediator med, String myAddress, int myPort) {
		this.med = med;
		
		try {
			client = new Client(myAddress, myPort, med);
			pool.execute(client);
			System.out.println(myAddress + " " + myPort);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void publishFile(File file) {
		try {
			client.publishFile(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void startDownload(final String ip, final int port, final String file) {
		pool.execute(new Runnable() {
			public void run() {
				try {
					client.retrieveFile(InetAddress.getByName(ip), port, file);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
