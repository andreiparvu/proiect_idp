package com.idp.sharix;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserListServlet
 */
@WebServlet(name = "UserListServlet", urlPatterns = {"/getUserList"})
public class UserListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Mediator mediator = Mediator.getInstance();
	
    public UserListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer sb = new StringBuffer("");
		
		for(String username:mediator.keySet())
			sb.append("user=" + username + "=" 
					+ mediator.getAddress(username) + "="
					+ mediator.getPort(username) + "\n");
		
		response.getWriter().print(sb.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// nothing to do here
	}

}
