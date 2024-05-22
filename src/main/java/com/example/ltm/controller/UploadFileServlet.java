package com.example.ltm.controller;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.example.ltm.bean.Account;
import com.example.ltm.bean.Source;
import com.example.ltm.bo.SourceBo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

@WebServlet("/UploadFileServlet")
@MultipartConfig
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SourceBo sourcebo = new SourceBo();

	public UploadFileServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Code không cần thiết cho phương thức GET
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isCheck = true;
		Account account = (Account) request.getSession().getAttribute("account");
		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());

		for (Part filePart : fileParts) {
			String fileName = new String(Paths.get(filePart.getSubmittedFileName()).getFileName().toString().getBytes(), "UTF-8");
			if (fileName.length() == 0) {
				isCheck = false;
				break;
			}

			fileName = fileName.substring(0, fileName.length() - 4);
			InputStream fileContent = filePart.getInputStream();
			String outputFilePath = "E:\\CATALINA_BASE\\" + fileName + ".docx"; // Thay đổi đường dẫn thư mục và đuôi tệp tùy theo yêu cầu của bạn
			try {
				convertToWord(fileContent, outputFilePath);
				Source newSource = new Source(fileName, false, account.getUsername());
				sourcebo.save(fileContent, newSource);
			} catch (Exception e) {
				isCheck = false;
				e.printStackTrace(); // In lỗi ra console để debug
			} finally {
				fileContent.close();
			}
		}

		if (isCheck) {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/OptionalServlet?index=2"));
		} else {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/OptionalServlet?index=3"));
		}
	}

	private void convertToWord(InputStream pdfInputStream, String outputFilePath) throws IOException {
		try (PDDocument document = PDDocument.load(pdfInputStream)) {
			XWPFDocument doc = new XWPFDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			XWPFParagraph paragraph = doc.createParagraph();
			XWPFRun run = paragraph.createRun();
			run.setText(text);
			try (FileOutputStream out = new FileOutputStream(new File(outputFilePath))) {
				doc.write(out);
			}
		}
	}
}
