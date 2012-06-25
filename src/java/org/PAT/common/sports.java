package org.PAT.common;

import java.util.ArrayList;
import java.util.HashMap;
public class sports extends MySQL{
	
	public sports(){
		super();
	}
	
	public Boolean checkSport(String sport_id){
		String[] fields = {"sport_id", "sport_title"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM sports WHERE sport_id= "+sport_id+" AND active=1", fields);
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<HashMap<String, String>> getListAll(){
		String[] fields = {"sport_id", "sport_title","active"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM sports", fields);
		
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getList(){
		String[] fields = {"sport_id", "sport_title"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM sports WHERE active=1", fields);
		
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getTopicsByUser(String user_id){
		String[] fields = {"sport_id","topic_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT DISTINCT sport_id,topic_id FROM playinggame WHERE expert_id='"+user_id + "' ORDER BY sport_id", fields);
		
		return result;
	}
	
	public Boolean add(String name,String Active){
		int result = this.Update("INSERT INTO sports (sport_title,active) VALUES ('"+name+"',"+Active+")");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean update(String id, String name, String active ){
		int result = this.Update("UPDATE sports SET sport_title = '"+name+"' ,active= "+active+" WHERE sport_id = "+id);
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean delete(String id){
		int result = this.Update("DELETE FROM sports WHERE sport_id = "+id);
		if (result >0)
			return true;
		else
			return false;
	}
}