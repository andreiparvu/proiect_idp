package com.idp.sharix;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SharixService
 */
@WebServlet(name = "SharixService", urlPatterns = {"/hello"})
public class SharixService extends HttpServlet {
	
	private static final long serialVersionUID = 3002939825209964265L;
	
	private Map<String, InetAddress> addresses;
	private Map<String, Integer> ports;
	private Map<String, List<String>> fileLists;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SharixService() {
        super();
        addresses = new HashMap<String, InetAddress>();
        ports = new HashMap<String, Integer>();
        fileLists = new HashMap<String, List<String>>();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println(request.getRemoteAddr() + " :: " + request.getRemotePort());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
