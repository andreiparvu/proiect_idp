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
  
  public void removeUser(String username) {
    addresses.remove(username);
    ports.remove(username);
    fileLists.remove(username);
  }

  public void addToFilelist(String username, String filename) {
    fileLists.get(username).add(filename);
  }

  public List<String> getFileList(String username) {
    if (fileLists.get(username) == null) {
      return new ArrayList<String>();
    }
    return fileLists.get(username);
  }
}
