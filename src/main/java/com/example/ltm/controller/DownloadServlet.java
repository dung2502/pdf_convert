package com.example.ltm.controller;

import com.example.ltm.bean.Source;
import com.example.ltm.bo.SourceBo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownloadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Thiết lập thông tin trả về
		SourceBo sourcebo = new SourceBo();
		int id = Integer.parseInt(request.getParameter("id"));

		Source source = sourcebo.getSource(id);
		byte[] data = source.getDocument_text();

		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-disposition", "attachment; filename=\"" + source.getFilename() + ".docx\"");
		response.setContentLength(data.length);

		try (InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
			 OutputStream outStream = response.getOutputStream()) {

			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
