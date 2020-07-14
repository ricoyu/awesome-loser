package com.loserico;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JDKCommonTest {

	@Test
	public void testName() {
		AccountHolder ach = new AccountHolder();
		ach.setName("Shamik Mitra");
		Account acc = new Account();
		acc.setAccNumber("100sm");
		acc.setAccType("Savings");
		acc.setHolder(ach);
		ach.addAccount(acc);
		System.out.println(ach);
	}
	
	@Test
	public void testSystemProperties() {
		String s = System.getProperty("java.io.tmpdir");
		System.out.println(s);
	}

	private class AccountHolder {
		private String name;
		private List<Account> accList = new ArrayList<Account>();

		public void addAccount(Account acc) {
			accList.add(acc);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "AccountHolder [name=" + name + ", accList=" + accList + "]";
		}
	}

	public class Account {
		private String accNumber;
		private String accType;
		private AccountHolder holder;

		public String getAccNumber() {
			return accNumber;
		}

		public void setAccNumber(String accNumber) {
			this.accNumber = accNumber;
		}

		public String getAccType() {
			return accType;
		}

		public void setAccType(String accType) {
			this.accType = accType;
		}

		public AccountHolder getHolder() {
			return holder;
		}

		public void setHolder(AccountHolder holder) {
			this.holder = holder;
		}

		@Override
		public String toString() {
			return "Account [accNumber=" + accNumber + ", accType=" + accType
					+ ", holder=" + holder + "]";
		}
	}
}
