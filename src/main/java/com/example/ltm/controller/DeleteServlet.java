package com.example.ltm.controller;

import com.example.ltm.bean.Account;
import com.example.ltm.bo.SourceBo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public DeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SourceBo sourcebo = new SourceBo();
		int id= Integer.parseInt(request.getParameter("id"));
		sourcebo.delete(id);
		//reload mainform
		Account account = (Account)request.getSession().getAttribute("account");
		request.setAttribute("sources", sourcebo.get(account.getUsername()));
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/mainform.jsp");
		rd.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
