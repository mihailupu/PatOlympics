package org.PAT.common;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.PAT.inc.Config;

public class MySQL extends Config{
	private Connection conn = null;
	
	public String connString = "jdbc:mysql://";
	private static String connClass = "com.mysql.jdbc.Driver";
	public MySQL(){
		try {
			Class.forName(connClass).newInstance();				
		} catch (Exception e) {
			e.printStackTrace();
		}
		connString +=  this.getWebHost() + ":"+ this.getWebPort() + "/"+  this.getWebDB();
		connString += "?user="+this.getWebUser()+"&password="+this.getWebPass();
	}
	
	public Boolean checkConnect(){
		Boolean result = true;
		connectDB();
		if (conn != null){
			try{
				conn.close();
			}catch(SQLException sqlEx){}
			conn = null;
		}else{
			result = false;
		}
		return result;
	}
	
	private void connectDB(){
		try {
			conn = DriverManager.getConnection(connString);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("SQLException:"+ex.getMessage());
			System.out.println("SQLState:"+ex.getSQLState());
			System.out.println("VendorError:"+ex.getErrorCode());
		}
	}
	
	public ArrayList<HashMap<String, String>> query(String Query, String[] listField){
				
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
	    HashMap<String, String> row = new HashMap<String, String>();
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
			connectDB();
			stmt = conn.createStatement();
			    
			rs = stmt.executeQuery(Query);
		    
		    while (rs.next()) {
		    	row = new HashMap<String, String>();
		    	for (int i=0; i<listField.length;i++){
		    		row.put(listField[i], rs.getString(listField[i]));
		    	}
		    	result.add(row);
		    }
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
	   
	    return result;
	}
	
	public int Update(String Query){
		Statement stmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    try {
			connectDB();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(Query);
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
		
		return result;
	}
	
}
