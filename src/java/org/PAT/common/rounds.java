package org.PAT.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class rounds extends MySQL{
	
	public rounds(){
		super();
	}

        public ArrayList<HashMap<String,String>> getFutureRounds(String round_id){
            	String[] fields = {"round_id","start_time","end_time"};

		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE round_id>"+round_id + " ORDER BY start_time DESC", fields);

		return result;
        }
		
	public ArrayList<HashMap<String, String>> getRound(String round_id){
		String[] fields = {"start_time","end_time"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE round_id='"+round_id + "'", fields);
		
		return result;
	}

	public ArrayList<HashMap<String, String>> getCurrentRound(){
		String[] fields = {"round_id","round_title","start_time","end_time"};
		Date date = new Date();
		String submitTime = String.valueOf(date.getTime());
		//ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE status='CURRENT'", fields);
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE "+submitTime+" BETWEEN start_time AND end_time", fields);
		
			
		return result;
	}
	
	public Boolean currentRoundinTime(long numOftime){
		String[] fields = {"round_id","round_title","start_time","end_time"};
		
		Date date = new Date();
		String submitTime = String.valueOf(date.getTime());
		//ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE status='CURRENT'", fields);
		ArrayList<HashMap<String, String>> rows = this.query("SELECT * FROM rounds WHERE "+submitTime+" BETWEEN start_time AND end_time", fields);
		Boolean result = false;
		if (rows.size()>0){
			result = true;
		}
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getNextRound(){
		String[] fields = {"round_id","round_title","start_time","end_time"};
		Date date = new Date();
		String submitTime = String.valueOf(date.getTime());
		//ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE status='NEXT'", fields);
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE start_time>"+submitTime+" ORDER BY start_time", fields);
		return result;
	}
	
	public Boolean userInCurrentRound(String user_id){
		String[] fields = {"team_id"};

		ArrayList<HashMap<String, String>> currentRound = this.getCurrentRound();
		if (currentRound.size()<=0){
			return false;
		}else{
			ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM playinggame WHERE team_id='"+user_id+"' AND round_id="+currentRound.get(0).get("round_id"), fields);
			if (result.size()>0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public ArrayList<HashMap<String, String>> getDoclistSubmitinCurrentRound(String user_id, String Order){
		ArrayList<HashMap<String, String>> currentRound = this.getCurrentRound();
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String[] fields = {"doc_id","doc_title","submittime"};
		
		if (currentRound.size()>0){
			String query = "SELECT submitdoc.doc_id AS doc_id,doclist.doc_title AS doc_title,submitdoc.submittime AS submittime FROM submitdoc LEFT JOIN (doclist) ON (submitdoc.doc_id=doclist.doc_id) WHERE submitdoc.user_id='"+user_id+"' AND submitdoc.round_id="+currentRound.get(0).get("round_id");
			if (Order!=null){
				if (Order.compareTo("DOCID")==0){
					query += " ORDER BY submitdoc.doc_id";
				}else if (Order.compareTo("TITLE")==0){
					query += " ORDER BY doclist.doc_title";
				}else if (Order.compareTo("TIMESTAMP")==0){
					query += " ORDER BY submitdoc.submittime";
				}else if (Order.compareTo("SUBMISSION_TIMESTAMP")==0){
					query += " ORDER BY submitdoc.submittime";
				}
				
			}
			result = this.query(query, fields);
		}
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getDoclistSubmitedByTopic(String topic_id, String Order){
		ArrayList<HashMap<String, String>> currentRound = this.getCurrentRound();
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String[] fields = {"doc_id","doc_title","submittime"};
		
		if (currentRound.size()>0){
			//String query = "SELECT submitdoc.doc_id AS doc_id,doclist.doc_title AS doc_title,submitdoc.submittime AS submittime FROM submitdoc LEFT JOIN (doclist) ON (submitdoc.doc_id=doclist.doc_id)  LEFT JOIN (users) ON (submitdoc.user_id = users.username) WHERE submitdoc.user_id = '"+user_id+"' AND submitdoc.topic_id='"+topic_id+"' AND users.user_role = 'REFEREE'";
			String query = "SELECT submitdoc.doc_id AS doc_id,doclist.doc_title AS doc_title,submitdoc.submittime AS submittime FROM submitdoc LEFT JOIN (doclist) ON (submitdoc.doc_id=doclist.doc_id)  LEFT JOIN (users) ON (submitdoc.user_id = users.username) WHERE submitdoc.topic_id='"+topic_id+"' AND users.user_role = 'REFEREE'";
			if (Order!=null){
				if (Order.compareTo("DOCID")==0){
					query += " ORDER BY submitdoc.doc_id";
				}else if (Order.compareTo("TITLE")==0){
					query += " ORDER BY doclist.doc_title";
				}else if (Order.compareTo("TIMESTAMP")==0){
					query += " ORDER BY submitdoc.submittime";
				}else if (Order.compareTo("SUBMISSION_TIMESTAMP")==0){
					query += " ORDER BY submitdoc.submittime";
				}
			}
			result = this.query(query, fields);
		}
		return result;
	}
	
	public Boolean add(String name,String starttime, String endtime){
		int result = this.Update("INSERT INTO rounds (round_title,start_time,end_time) VALUES ('"+name+"','"+starttime+"','"+endtime+"')");
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean checkTimeInRound(String timestring){
		String[] fields = {"round_id","start_time","end_time"};
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE "+timestring+" BETWEEN start_time AND end_time", fields);
		
		if (result.size() >0)
			return true;
		else
			return false;
	}
	
	public Boolean checkTimeInRound(String id,String timestring){
		String[] fields = {"round_id","start_time","end_time"};
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds WHERE round_id !="+id+" AND "+timestring+" BETWEEN start_time AND end_time", fields);
		
		if (result.size() >0)
			return true;
		else
			return false;
	}
	
	public Boolean update(String id, String name,String starttime, String endtime, String status){

		int result = this.Update("UPDATE rounds SET round_title = '"+name+"', start_time= '"+starttime+"', end_time= '"+endtime+"' WHERE round_id ='"+id+"'");
	/*	if (true)
			try {
				throw new ServletException("UPDATE rounds SET round_title = '"+name+"', start_time= '"+starttime+"', end_time= '"+endtime+"' WHERE round_id = "+id);
			} catch (ServletException e) {
				e.printStackTrace();
			}*/
		if (result >0)
			return true;
		else
			return false;
	}
	
	public Boolean delete(String id){
		int result = this.Update("DELETE FROM rounds WHERE round_id = "+id);
		if (result >0)
			return true;
		else
			return false;
	}
	
	public ArrayList<HashMap<String, String>> getListRound(){
		String[] fields = {"round_id","round_title","start_time","end_time"};
		
		ArrayList<HashMap<String, String>> result = this.query("SELECT * FROM rounds", fields);
		
		return result;
	}
}