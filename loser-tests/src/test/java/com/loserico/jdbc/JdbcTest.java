package com.loserico.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * <p>
 * Copyright: (C), 2020/3/9 10:49
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class JdbcTest {
	
	private static Connection connection;
	
	@BeforeClass
	public static void initConnection() throws ClassNotFoundException, SQLException {
		//This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		//Setup the connection with the DB
		//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=10000&socketTimeout=3000&autoReconnect=true", "rico", "123456");
		//connection = DriverManager.getConnection("jdbc:mysql://192.168.100.101:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true", "rico", "123456");
		connection = DriverManager.getConnection("jdbc:mysql://192.168.2.176:3306/micromall?characterEncoding=utf8&connectTimeout=100000&socketTimeout=30000&autoReconnect=true&useSSL=false", "root", "qianyu14");
		//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuling?useSSL=false&&characterEncoding=UTF-8", "rico", "123456");
	}
	
	@AfterClass
	public static void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
	
	@Test
	public void testSelectOrder() throws SQLException {
		PreparedStatement preparedStatement =
				connection.prepareStatement("select * from oms_order");
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			String orderSn = rs.getString(4);
			System.out.println(orderSn);
		}
	}
	
	@Test
	public void testInsert() throws SQLException {
		PreparedStatement preparedStatement =
				connection.prepareStatement("INSERT INTO pub_codes ( shopcode, branchcode, codeprefix, codemax, codename ) VALUES  ( '125827291', '', '', -1, 'weighorcountbarcode' )");
		preparedStatement.execute();
	}
	
	@Test
	public void testReadDataBase() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from read_book_pd");
		ResultSet resultSet = preparedStatement.executeQuery();
		writeMetaData(resultSet);
		writeResultSet(resultSet);
	}
	
	@Test
	public void testUpdate() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("update read_book_pd set en_name =? where id=?");
		preparedStatement.setString(1, "三少爷的剑");
		preparedStatement.setInt(2, 18);
		preparedStatement.execute();
		int rows = preparedStatement.getUpdateCount();
		System.out.println("Effect rows: " + rows);
	}
	
	
	@Test
	public void testHandleResults() throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("select * from read_book_pd");
		ResultSet rs = stmt.executeQuery();
		if (rs == null) {
			if (stmt.getMoreResults()) {
				rs = stmt.getResultSet();
			}
		}
	}
	
	private void writeMetaData(ResultSet resultSet) throws SQLException {
		System.out.println("The columns in the table are: ");
		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}
	
	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			/*
			 * It is possible to get the columns via name also possible to get the columns via the column number which starts at 1
			 * e.g. resultSet.getSTring(2);
			 */
			Integer id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String enName = resultSet.getString("en_name");
			String author = resultSet.getString("author");
			String imgUrl = resultSet.getString("imgurl");
			String description = resultSet.getString("description");
			LocalDateTime createTime = resultSet.getObject("create_time", LocalDateTime.class);
			int status = resultSet.getInt("status");
			int commentNum = resultSet.getInt("comment_num");
			int price = resultSet.getInt("price");
			String category = resultSet.getString("category");
			
			System.out.println("id: " + id);
			System.out.println("name: " + name);
			System.out.println("enName: " + enName);
			System.out.println("author: " + author);
			System.out.println("imgUrl: " + imgUrl);
			System.out.println("description: " + description);
			System.out.println("createTime: " + createTime);
			System.out.println("status: " + status);
			System.out.println("commentNum: " + commentNum);
			System.out.println("price: " + price);
			System.out.println("category: " + category);
		}
	}
}
