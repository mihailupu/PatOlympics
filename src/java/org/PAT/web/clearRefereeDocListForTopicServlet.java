package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.docid;
import org.PAT.common.scoreboard;
import org.PAT.common.topic;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class clearRefereeDocListForTopicServlet extends ControllerServlet {
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
    		//if (pageSession.getAttribute("user_role").toString().compareTo("REFEREE")==0){
    			if (userObj.getRoleBySession(sessionID).compareTo("REFEREE")==0){
    			if (topic_id!=null){
    				String userID = userObj.getUserBySession(sessionID);
	    			if (topicObj.topicIsAssignedToUser(userID,topic_id)){

		        			docid docObj = new docid();
		        			int numOfDel = docObj.clearREFDoc(userID,topic_id);
	        				outputObj.put("value", numOfDel);
	        				outputObj.put("error", null);
                                                scoreboardObj.refreshScorePerSport(sport_id, "");

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
