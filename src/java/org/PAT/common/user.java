package org.PAT.common;

import java.util.ArrayList;
import java.util.HashMap;
import org.PAT.inc.CryptMD5;
public class user extends MySQL{
	
	public String username;
	public String user_role;
	public String fullname;
	public String error;
	public user(){
		super();
	}
	public Boolean userExists(String username){
		this.error = "";
		this.fullname = "";
		Boolean result = true;
		String[] fields = {"username","fullname","password","user_role"};
		
		if (!this.checkConnect()){
			this.error = "ERROR_AUTHBACKEND_UNAVAILABLE";
			result = false;
		}else{
			CryptMD5 md5Obj = new CryptMD5();
			ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE username='"+username+"'", fields);
			if (rows.size()>0){
				this.username = username;
			}else{
				this.error = "ERROR_WRONG_CREDENTIALS";
				result = false;
			}
		}
		return result;
	}
	
	public Boolean login(String username, String password){
		this.error = "";
		this.fullname = "";
		Boolean result = true;
		String[] fields = {"username","fullname","password","user_role"};
		
		if (!this.checkConnect()){
			this.error = "ERROR_AUTHBACKEND_UNAVAILABLE";
			result = false;
		}else{
			CryptMD5 md5Obj = new CryptMD5();
			ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE username='"+username+"' AND password='"+md5Obj.md5(password)+"'", fields);
			if (rows.size()>0){
				this.username = username;
				this.fullname = rows.get(0).get("fullname");
				this.user_role = rows.get(0).get("user_role");	
			}else{
				this.error = "ERROR_WRONG_CREDENTIALS";
				result = false;
			}
		}
		return result;
	}
	
	public Boolean loginAdmin(String username, String password){
		this.error = "";
		this.fullname = "";
		Boolean result = true;
		String[] fields = {"username","fullname","password"};
		
		if (!this.checkConnect()){
			this.error = "ERROR_AUTHBACKEND_UNAVAILABLE";
			result = false;
		}else{
			CryptMD5 md5Obj = new CryptMD5();
			ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM admin WHERE username='"+username+"' AND password='"+md5Obj.md5(password)+"'", fields);
			if (rows.size()>0){
				this.username = username;
				this.fullname = rows.get(0).get("fullname");
			}else{
				this.error = "ERROR_WRONG_CREDENTIALS";
				result = false;
			}
		}
		return result;
	}
	
	public String getFullname(String user_id) {
		String[] fields = {"fullname"};
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE username='"+user_id+"'", fields);
		String result = null;
		
		if (rows.size()>0){
			result = rows.get(0).get("fullname");
		}
		return result;
	}
	
	
	public Boolean isLogin(String username){
		String[] fields = {"username"};
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM users WHERE username='"+username+"' AND isLogin=1", fields);
		
		if (result.size()>0){
			updateLoginTime(username);
			return true;
		}else{
			return false;
		}
		
	}
	
	private void updateLoginTime(String username){
		//SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		//Date d = new Date();
		//this.Update("UPDATE users SET logintime='"+timeFormat.format(d.getTime())+"',islogin=1 WHERE username='"+username+"'");
	}
	
	public void logout(String username){
		//this.Update("Update users SET islogin=0 WHERE username='"+username+"'");
	}
	
	@SuppressWarnings("unchecked")
	public Boolean adminLogin(String username, String password){
		String[] fields = {"username","password"};
		ArrayList result = this.query("SELECT * FROM admin WHERE username='"+username+"' AND password='"+password+"'", fields);
		
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public void updateSession(String username,String sessionID){
		if (sessionID.compareTo("")==0){
			this.Update("UPDATE users SET sessionID='"+sessionID+"' WHERE username='"+username+"'");
		}else{
			this.Update("UPDATE users SET sessionID='' WHERE sessionID='"+sessionID+"'");
			this.Update("UPDATE users SET sessionID='"+sessionID+"' WHERE username='"+username+"'");
		}
		
	}
	
	public String getRoleBySession(String sessionID){
		String[] fields = {"username","fullname","password","user_role"};
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE sessionID='"+sessionID+"'", fields);
		String result = "";
		if (rows.size()>0){
			result = rows.get(0).get("user_role");	
		}
		return result;
	}
	
	public String getRoleByUser(String userID){
		String[] fields = {"username","fullname","password","user_role"};
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE username='"+userID+"'", fields);
		String result = "";
		if (rows.size()>0){
			result = rows.get(0).get("user_role");	
		}
		return result;
	}
	
	public String getUserBySession(String sessionID){
		String[] fields = {"username","fullname","password","user_role"};
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM users WHERE sessionID='"+sessionID+"'", fields);
		String result = "";
		if (rows.size()>0){
			result = rows.get(0).get("username");	
		}
		return result;
	}
	
	public Boolean add(String id,String pass,String name,String role){
		int result = this.Update("INSERT INTO users (username,fullname,password,user_role) VALUES ('"+id+"','"+name+"','"+pass+"','"+role+"')");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean update(String id, String pass,String name ){
		int result ;
		if (pass != ""){
			result = this.Update("UPDATE users SET password='"+pass+"',fullname='"+name+"' WHERE username = '"+id+"'");
		}else{
			result = this.Update("UPDATE users SET fullname='"+name+"' WHERE username = '"+id+"'");
		}
		
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean delete(String id){
		int result = this.Update("DELETE FROM users WHERE username ='"+id+"'");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public ArrayList<HashMap<String, String>> getListTeam(){
		String[] fields = {"username","fullname"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM users WHERE user_role='TEAM'", fields);
		
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getListExpert(){
		String[] fields = {"username","fullname"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM users WHERE user_role='REFEREE'", fields);
		
		return result;
	}
}