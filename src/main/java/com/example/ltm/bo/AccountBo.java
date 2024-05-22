package com.example.ltm.bo;

import java.util.List;

import com.example.ltm.bean.Account;
import com.example.ltm.dao.AccountDao;


public class AccountBo {
	private AccountDao dao;
	public AccountBo() {
		dao = new AccountDao();
	}
	
	public List<Account> getAll(){
		return dao.getAll();
	}
	
	public boolean isExist(String username,String password){
		return dao.isExist(username, password);
	}
	
	public boolean save(Account account) {
		return dao.save(account);
	}
}
