package org.PAT.common;

import java.util.ArrayList;
import java.util.HashMap;
public class docid extends MySQL{
	
	private String title;
	public docid(){
		super();
	}
	
	public ArrayList<String> getDocIDs(){
		
		String[] fields = {"docid"};
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM doclist", fields);
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0;i<rows.size();i++){
			result.add(rows.get(i).get("docid"));
		}
		return result;
		
	}

	public Boolean checkDoc(String docID){
		String[] fields = {"doc_id","doc_title"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM doclist WHERE doc_id='"+docID+"'", fields);
		
		if (result.size()>0){
			this.title = result.get(0).get("doc_title");
			return true;
		}else{
			this.title = "";
			return false;
		}
		
	}
	
	/* check list submit by part*/
	public Boolean checkSubmitList(String userID, String roundID, String topicID, String docID){
		String[] fields = {"doc_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM submitdoc WHERE user_id='"+userID+"' AND round_id='"+roundID+"' AND topic_id='"+topicID+"' AND doc_id='"+docID+"'", fields);
		
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	/* check list submit by referee*/
	public Boolean checkSubmitList(String topicID, String docID){
		String[] fields = {"doc_id"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM submitdoc   LEFT JOIN (users) ON (submitdoc.user_id = users.username) WHERE  topic_id='"+topicID+"' AND doc_id='"+docID+"' AND users.user_role = 'REFEREE'", fields);
		
		if (result.size()>0){
			return true;
		}else{
			return false;
		}
		
	}

        	/* check list submit by referee*/
        //returns how many documents were submitted by this user for this topic.
	public int countSubmitList(String topicID, String userID){
		String[] fields = {"doc_id"};

		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM submitdoc WHERE  topic_id='"+topicID+"' AND user_id='"+userID+"'", fields);

		return result.size();

	}
	
	public String getTitle() {
		return this.title;
	}
	
	
	public void insertDoc(String Username, String roundID, String topicID, String DocID, String SubmitTime){
		this.Update("INSERT  INTO submitdoc (user_id,round_id,topic_id,doc_id,submittime) VALUES('"+Username+"',"+roundID+",'"+topicID+"','"+DocID+"','"+SubmitTime+"')");
	}
	
	public void insertDoc(String Username, String roundID, String topicID, String DocID, String SubmitTime, String SubmitRole){
		this.Update("INSERT  INTO submitdoc (user_id,round_id,topic_id,doc_id,submittime,submit_role) VALUES('"+Username+"',"+roundID+",'"+topicID+"','"+DocID+"','"+SubmitTime+"','"+SubmitRole+"')");
	}
	
	public int clearDoc(String Username, String roundID, String topicID, String DocID){
		return this.Update("DELETE FROM submitdoc WHERE user_id='"+Username+"' AND round_id="+roundID+" AND topic_id='"+topicID+"' AND doc_id='"+DocID+"'");
	}
	
	
	public int clearDoc(String Username, String roundID, String topicID){
		return this.Update("DELETE FROM submitdoc WHERE user_id='"+Username+"' AND round_id="+roundID+" AND topic_id='"+topicID+"'");
	}
	
	public int clearREFDoc(String Username, String topicID, String DocID){
		return this.Update("DELETE FROM submitdoc WHERE user_id='"+Username+"' AND topic_id='"+topicID+"' AND doc_id='"+DocID+"'");
	}
	
	public int clearREFDoc(String Username, String topicID){
		
		//return this.Update("DELETE FROM submitdoc WHERE user_id='"+Username+"' AND topic_id='"+topicID+"'");
		return this.Update("DELETE FROM submitdoc WHERE topic_id='"+topicID+"' AND submit_role = 'REFEREE'");
	}
	
	
}