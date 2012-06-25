package org.PAT.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.PAT.inc.CryptMD5;
public class topic extends MySQL{
	
	public topic(){
		super();
	}
	
	public Boolean topicExists(String id){
		
		Boolean result = true;
		String[] fields = {"topic_id"};
		
		if (!this.checkConnect()){
			result = false;
		}else{
			CryptMD5 md5Obj = new CryptMD5();
			ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM topiclist WHERE topic_id='"+id+"'", fields);
			if (rows.size()<=0){
				result = false;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkTopic(String topicID){
		String[] fields = {"topic_id"};
		
		ArrayList result = this.query("SELECT * FROM topiclist WHERE topic_id='"+topicID+"'", fields);
		
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<HashMap<String, String>> getTopic(String topic_id){
		String[] fields = {"topic_id","topic_title","topic_description","sport_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM topiclist WHERE topic_id='"+topic_id + "'", fields);
		
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getCurrentTopicByUser(String user_id){
		String[] fields = {"topic_id","topic_title","sport_id"};
		Date date = new Date();
		String submitTime = String.valueOf(date.getTime());
		ArrayList<HashMap<String, String>> result = this.query("SELECT playinggame.topic_id AS topic_id, playinggame.topic_title as topic_title, playinggame.sport_id AS sport_id "
                        + "FROM playinggame LEFT JOIN rounds ON (playinggame.round_id = rounds.round_id) "
                        + "WHERE playinggame.team_id='"+user_id + "' AND  "+submitTime+" BETWEEN rounds.start_time AND rounds.end_time", fields);
		
		return result;
	}




	public ArrayList<HashMap<String, String>> getTopicByUserandSport(String user_id,String sport_id){
		String[] fields = {"topic_id","topic_title"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM playinggame WHERE expert_id='"+user_id + "' AND sport_id="+sport_id, fields);
		
		return result;
	}
	
	public Boolean topicIsAssignedToUser(String userID, String topicID){
		String[] fields = {"topic_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM playinggame WHERE expert_id='"+userID + "' AND topic_id='"+topicID+"'", fields);
		
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	
	public Boolean add(String expert_id, String id,String title,String desc,String sport_id){
		int result = this.Update("INSERT INTO topiclist (expert_id,topic_id,topic_title,topic_description,sport_id) VALUES ('"+expert_id+"','"+id+"','"+title+"','"+desc+"','"+sport_id+"')");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean update(String id,String title,String desc,String sport_id ){
		int result = this.Update("UPDATE topiclist SET topic_id = '"+id+"' ,topic_title= '"+title+"' ,topic_description= '"+desc+"', sport_id='"+sport_id+"' WHERE topic_id = '"+id+"'");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean delete(String id){
		int result = this.Update("DELETE FROM topiclist WHERE topic_id = '"+id+"'");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public ArrayList<HashMap<String, String>> getListTopic(){
		String[] fields = {"expert_id","topic_id","topic_title","topic_description","sport_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM topiclist", fields);
		
		return result;
	}
}