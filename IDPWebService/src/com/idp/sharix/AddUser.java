package com.idp.sharix;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddUser", urlPatterns = {"/addUser"})
public class AddUser extends HttpServlet {
  private static final long serialVersionUID = 1L;

  Mediator mediator = Mediator.getInstance();

  public AddUser() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String user = request.getParameter("user"),
        ip = request.getParameter("ip"),
        port = request.getParameter("port");

    mediator.addUser(user, ip, Integer.parseInt(port));
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // nothing to do here
  }
}
