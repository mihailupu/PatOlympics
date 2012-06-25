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
public class clearTeamEntryForCurrentRoundServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

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
    					Date date = new Date();
    	    			
    	    			if (roundObj.currentRoundinTime(date.getTime())){
    	    			
		    				String doc_id = request.getParameter("doc_id");
		        			docid docObj = new docid();
		        			if ((doc_id != null)&&(docObj.checkDoc(doc_id))){
                                                        String topic_id=topicObj.get(0).get("topic_id");
	        					if (docObj.clearDoc(userID,rObj.get(0).get("round_id"),topic_id,doc_id)>0){
	        						outputObj.put("value", true);
			        				outputObj.put("error", null);
                                                                scoreboardObj.refreshScorePerSport(topicObj.get(0).get("sport_id"), userID);

	        					}else{
	        						outputObj.put("value", "BACKEND ERROR: '"+userID+"' '"+rObj.get(0).get("round_id")+"' '"+
                                                                        topicObj.get(0).get("topic_id")+"' '"+doc_id+"'");
			        				outputObj.put("error", null);
	        					}
		        				
		        			}else{
		        				outputObj.put("value", "BACKEND ERROR: '"+userID+"' '"+rObj.get(0).get("round_id")+"' '"+
                                                                        topicObj.get(0).get("topic_id")+"' '"+doc_id+"'");
		        				outputObj.put("error", null);
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
