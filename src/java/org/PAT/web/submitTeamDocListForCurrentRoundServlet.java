package org.PAT.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.PAT.common.*;
import org.PAT.common.rounds;
import org.PAT.common.topic;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class submitTeamDocListForCurrentRoundServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;
        private static final int MAXDOCS=200;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		
		String sessionID = request.getParameter("session_id");
		user userObj = new user();
                scoreboard scoreboardObj = new scoreboard();
    	if ((sessionID!= null)){
    		if (userObj.getRoleBySession(sessionID).compareTo("TEAM")==0){
    			
    			String userID = userObj.getUserBySession(sessionID);
    			topic topObj = new topic();
    			ArrayList<HashMap<String, String>> topicObj = topObj.getCurrentTopicByUser(userID);
    			
    			rounds roundObj = new rounds();
    			ArrayList<HashMap<String, String>> rObj = roundObj.getCurrentRound();
    			
    			if ((roundObj.userInCurrentRound(userID))&&(rObj.size()>0)){
    				if (topicObj.size()>0){
	    				String doclist = request.getParameter("doclist");
	    				Date date = new Date();
	    				String submitTime = String.valueOf(date.getTime());
	        			String docid = "";
	        			docid docObj = new docid();
	        			int numOfSubmit = 0;
	        			if (roundObj.currentRoundinTime(date.getTime())){
		        			if (doclist != null){
		        				if (doclist.startsWith("||")){
		        					doclist = doclist.substring(2);
		        				}
		        				if (doclist.endsWith("||")){
		        					doclist = doclist.substring(0,doclist.length()-2);
		        				}
		        				String topic_id=topicObj.get(0).get("topic_id");
		        				while (doclist.indexOf("||")>=0){
		        					docid = doclist.substring(0,doclist.indexOf("||"));
		        					doclist = doclist.substring(doclist.indexOf("||")+2);
		        					if ((docObj.checkDoc(docid))&&(!docObj.checkSubmitList(userID,rObj.get(0).get("round_id"),topic_id,docid))&&(docObj.countSubmitList(topic_id,userID)<MAXDOCS)){
		        						docObj.insertDoc(userID,rObj.get(0).get("round_id"),topic_id,docid,submitTime);
		        						numOfSubmit++;
		        					}
		        				}
		        				if ((doclist!= null)||(doclist.trim()!= "")){
		        					if ((docObj.checkDoc(doclist))&&(!docObj.checkSubmitList(userID,rObj.get(0).get("round_id"),topic_id,doclist))&&(docObj.countSubmitList(topic_id,userID)<MAXDOCS)){
		        						docObj.insertDoc(userID,rObj.get(0).get("round_id"),topic_id,doclist,submitTime);
		        						numOfSubmit++;
		        					}
		        				}
		        				outputObj.put("value", numOfSubmit);
		        				outputObj.put("error", null);
                                                        scoreboardObj.refreshScorePerSport(topicObj.get(0).get("sport_id"), userID);
		        			}else{
		        				outputObj.put("value", null);
		        				outputObj.put("error", "ERROR_NOTADDED");
		        			}
	        			}else{
	        				outputObj.put("value", null);
	        				outputObj.put("error", "ERROR_NOTINTIME");
	        			}
    				}else{
    					outputObj.put("value", null);
        				outputObj.put("error", "ERROR_NOTOPIC");
    				}
    			
    			}else{
    				outputObj.put("value", null);
    				outputObj.put("error", "ERROR_NOROUND");
    			}
    			
    		}else{
        		outputObj.put("value", null);
    			outputObj.put("error", "ERROR_NOTEAM");
        	}
    	}else{
    		outputObj.put("value", null);
			outputObj.put("error", "ERROR_INVALIDSESSION");
    	}
    	
	}

}
