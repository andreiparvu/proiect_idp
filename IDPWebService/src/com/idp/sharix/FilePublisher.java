package com.idp.sharix;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "FilePublisher", urlPatterns={"/publishFile"})
public class FilePublisher extends HttpServlet {

  private static final long serialVersionUID = 3254722402700999098L;
  Mediator mediator = Mediator.getInstance();

  public FilePublisher() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String user = request.getParameter("user");
    String filename = request.getParameter("file");

    // do the thing
    mediator.addToFilelist(user,filename);

    // reply
    response.getWriter().write("OK\n");
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    // nothing here
  }

}
