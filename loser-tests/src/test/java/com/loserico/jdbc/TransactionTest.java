package com.loserico.jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Copyright: (C), 2022-09-02 15:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TransactionTest {
	
	private static Connection connection;
	private static Connection connection2;
	
	@BeforeClass
	public static void initconnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection =
				DriverManager.getConnection("jdbc:mysql://192.168.100.111:3306/test?characterEncoding=utf8&" +
						"connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false", "root", "qianyu14");
		connection2 =
				DriverManager.getConnection("jdbc:mysql://192.168.100.111:3306/db_order?characterEncoding=utf8&" +
						"connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false", "root", "qianyu14");
	}
	
	@AfterClass
	public static void closeConnection() throws SQLException {
		connection.close();
	}
	
	@Test
	public void testTransaction() throws SQLException {
		try {
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("update t set name='yuxh'");
			connection.commit();
		} catch (Exception e) {
			connection.rollback();
		}
	}
	
	@Test
	public void testXaTransaction() throws SQLException, XAException {
		//打印XA语句, 用于调试
		boolean logXaCommand = true;
		XAConnection xaConn1 = new MysqlXAConnection((com.mysql.jdbc.Connection) connection, logXaCommand);
		//拿到XA的资源管理器
		XAResource rm1 = xaConn1.getXAResource();
		
		XAConnection xaConn2 = new MysqlXAConnection((com.mysql.jdbc.Connection) connection2, logXaCommand);
		XAResource rm2 = xaConn2.getXAResource();
		
		//AP请求TM执行一个分布式事务, TM生成全局事务id
		byte[] gtrid = "g12345".getBytes(UTF_8);
		int formatId = 1;
		try {
			// ============ 分别执行RM1和RM2上的分支事务 ============ 
			// TM生成RM1上的事务分支id
			byte[] bqual1 = "b00001".getBytes(UTF_8);
			Xid xid1 = new MysqlXid(gtrid, bqual1, formatId);
			//执行RM1上的分支事务
			rm1.start(xid1, XAResource.TMNOFLAGS);
			PreparedStatement ps1 = connection.prepareStatement("insert into t(id, name) values (4, '俞雪华')");
			ps1.execute();
			rm1.end(xid1, XAResource.TMSUCCESS);
			
			// TM生成rm2上的事务分支id
			byte[] bqual2 = "b00002".getBytes(UTF_8);
			Xid xid2 = new MysqlXid(gtrid, bqual2, formatId);
			
			// 执行rm2上的事务分支
			rm2.start(xid2, XAResource.TMNOFLAGS);
			PreparedStatement ps2 = connection2.prepareStatement("update tbl_order set count = count-50 where id=1");
			ps2.execute();
			rm2.end(xid2, XAResource.TMSUCCESS);
			
			// ===================两阶段提交================================
			// phase1：询问所有的RM 准备提交事务分支
			int rm1_prepare = rm1.prepare(xid1);
			int rm2_prepare = rm2.prepare(xid2);
			// phase2：提交所有事务分支
			boolean onePhase = false;
			//TM判断有2个事务分支，所以不能优化为一阶段提交
			if (rm1_prepare == XAResource.XA_OK && rm2_prepare == XAResource.XA_OK) {
				//所有事务分支都prepare成功，提交所有事务分支
				rm1.commit(xid1, onePhase);
				rm2.commit(xid2, onePhase);
			} else {
				//如果有事务分支没有成功，则回滚
				rm1.rollback(xid1);
				rm2.rollback(xid2);
			}
		} catch (XAException e) {
			e.printStackTrace();
		}
	}
}
