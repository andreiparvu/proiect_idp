package com.idp.sharix;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "FileListGetter", urlPatterns = {"/getFileList"})
public class FileListGetter extends HttpServlet {
       
	private static final long serialVersionUID = -8711382132302512516L;
	Mediator mediator = Mediator.getInstance();
	
	public FileListGetter() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("user");
		List<String> fileList = mediator.getFileList(user);
		StringBuffer sb = new StringBuffer("");
		
		// format is
		// file1|file2|file3...
		for (String filename : fileList) 
			sb.append(filename + "|");
		
		sb.setLength(sb.length() - 1);
		
		response.getWriter().write(sb.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// nothing
	}

}
