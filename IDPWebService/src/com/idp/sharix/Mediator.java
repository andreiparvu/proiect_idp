package com.idp.sharix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mediator {

	private static Mediator instance = new Mediator();

	// 4-dotted IPv4 or names e.g. localhost
	private Map<String, String> addresses;
	private Map<String, Integer> ports;
	private Map<String, List<String>> fileLists;
	
	private Mediator() {
        addresses = new HashMap<String, String>();
        ports = new HashMap<String, Integer>();
        fileLists = new HashMap<String, List<String>>();
        
        addUser("daniel", "127.0.0.1", 8000);
        addUser("andrei", "127.0.0.1", 9000);
	}
	
	public static Mediator getInstance() {
		return instance;
	}
	
	public Set<String> keySet() {
		return addresses.keySet();
	}
	
	public String getAddress(String username) {
		return addresses.get(username);
	}
	
	public int getPort(String username) {
		return ports.get(username);
	}
	
	public void addUser(String username, String address, int port) {
		addresses.put(username, address);
		ports.put(username, port);
		fileLists.put(username, new ArrayList<String>());
	}
	
	public void addToFilelist(String username, String filename) {
		fileLists.get(username).add(filename);
	}
	
	public List<String> getFileList(String username) {
		return fileLists.get(username);
	}
}
