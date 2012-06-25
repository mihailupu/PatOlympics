package org.PAT.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.*;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class submitRefereeDocListForTopicServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		String sessionID = request.getParameter("session_id");
		String topic_id = request.getParameter("topic_id");
		topic topicObj = new topic();
                ArrayList<HashMap<String,String>> topicParams = topicObj.getTopic(topic_id);
                String sport_id=topicParams.get(0).get("sport_id");
		user userObj = new user();
                scoreboard scoreboardObj = new scoreboard();
    	if ((sessionID!= null)){
    		if (userObj.getRoleBySession(sessionID).compareTo("REFEREE")==0){
    			if (topic_id!=null){
    				String userID = userObj.getUserBySession(sessionID);
	    			if (topicObj.topicIsAssignedToUser(userID,topic_id)){
		    			
		    			rounds roundObj = new rounds();
		    			ArrayList<HashMap<String, String>> rObj = roundObj.getCurrentRound();
		    			Date date = new Date();
		    			
		    				String doclist = request.getParameter("doclist");
		    				
		    				String submitTime = String.valueOf(date.getTime());
		        			String docid = "";
		        			docid docObj = new docid();
		        			int numOfSubmit = 0;
		        			if (doclist != null){
		        				if (doclist.startsWith("||")){
		        					doclist = doclist.substring(2);
		        				}
		        				if (doclist.endsWith("||")){
		        					doclist = doclist.substring(0,doclist.length()-2);
		        				}
		        				        				
		        				while (doclist.indexOf("||")>=0){
		        					docid = doclist.substring(0,doclist.indexOf("||"));
		        					doclist = doclist.substring(doclist.indexOf("||")+2);
		        					if ((docObj.checkDoc(docid))&&(!docObj.checkSubmitList(topic_id,docid))){
		        						docObj.insertDoc(userID,rObj.get(0).get("round_id"),topic_id,docid,submitTime,"REFEREE");
		        						numOfSubmit++;
		        					}
		        				}
		        				if (((doclist!= null)||(doclist.trim()!= ""))&&(!docObj.checkSubmitList(topic_id,doclist))){
		        					if (docObj.checkDoc(doclist)){
		        						docObj.insertDoc(userID,rObj.get(0).get("round_id"),topic_id,doclist,submitTime,"REFEREE");
		        						numOfSubmit++;
		        					}
		        				}
		        				outputObj.put("value", numOfSubmit);
		        				outputObj.put("error", null);
                                                        scoreboardObj.refreshScorePerSport(sport_id, "");
		        			}else{
		        				outputObj.put("value", null);
		        				outputObj.put("error", "ERROR_NOTADDED");
		        			}

	    			}else{
	    				outputObj.put("value", null);
						outputObj.put("error", "ERROR_NOTASSIGNED");
	    			}
    			}else{
					outputObj.put("value", null);
    				outputObj.put("error", "ERROR_NOTOPIC");
    			}
    		}else{
        		outputObj.put("value", null);
    			outputObj.put("error", "ERROR_NOREFEREE");
        	}
    	}else{
    		outputObj.put("value", null);
			outputObj.put("error", "ERROR_INVALIDSESSION");
    	}
	}

}
