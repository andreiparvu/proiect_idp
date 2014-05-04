package com.idp.sharix;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RemoveUser", urlPatterns = {"/removeUser"})
public class RemoveUser extends HttpServlet {
  private static final long serialVersionUID = 1L;

  Mediator mediator = Mediator.getInstance();

  public RemoveUser() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String user = request.getParameter("user");

    mediator.removeUser(user);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // nothing to do here
  }
}

