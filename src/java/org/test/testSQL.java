package org.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.PAT.inc.CryptMD5;

public class testSQL {
	private static Connection conn = null;
	private static String connString = "jdbc:mysql://";
	private static String connClass = "com.mysql.jdbc.Driver";
	
	public static void main(String[] args) {
		try {
			Class.forName(connClass).newInstance();				
		} catch (Exception e) {
			e.printStackTrace();
		}
		connString +=  "localhost:3306/pat";
		connString += "?user=root&password=root";
		
		try {
			conn = DriverManager.getConnection(connString);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("SQLException:"+ex.getMessage());
			System.out.println("SQLState:"+ex.getSQLState());
			System.out.println("VendorError:"+ex.getErrorCode());
		}
		
		 Statement stmt = null;
		    ResultSet rs = null;
		    try {
				stmt = conn.createStatement();
				System.out.println("SELECT * FROM users WHERE username='user' AND password='user'");
				rs = stmt.executeQuery("SELECT * FROM users WHERE username='user' AND password='user'");
			    
			    while (rs.next()) {
			    	System.out.println("username:"+rs.getString("username"));
			    }
			    
			    stmt.executeUpdate("INSERT INTO submitdoc (username,roundid,topicid,docid,submittime) VALUES('user',1,'CHEM','EP-1000303-A4','12:29:17')");
			} catch (SQLException ex) {
				System.out.println("SQLException:"+ex.getMessage());
				System.out.println("SQLState:"+ex.getSQLState());
				System.out.println("VendorError:"+ex.getErrorCode());
			}finally{
				if(rs!=null){
					try{
						rs.close();
					}catch(SQLException sqlEx){}
					rs=null;
				}
				if(stmt!=null){
					try{
						stmt.close();
					}catch(SQLException sqlEx){}
					stmt=null;
				}
				if (conn != null){
					try{
						conn.close();
					}catch(SQLException sqlEx){}
					conn = null;
				}
			}
			
			CryptMD5 md5Obj = new CryptMD5();
			System.out.println(md5Obj.md5("hello"));
			
	}

}
