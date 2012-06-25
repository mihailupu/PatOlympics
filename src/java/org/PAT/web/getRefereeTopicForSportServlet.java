package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.topic;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getRefereeTopicForSportServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		topic topicObj = new topic();
    	String userID = request.getParameter("user_id");
    	String sportID = request.getParameter("sport_id");
	user userObj = new user();
    	if ((userID!= null)){
    		if (userObj.getRoleByUser(userID).compareTo("REFEREE")==0){
    			ArrayList<HashMap<String, String>> rows = topicObj.getTopicByUserandSport(userID,sportID);
    			if (rows.size()>0){
	    	    	LinkedList<String> itemTopic = new LinkedList<String>();;
	    	    	itemTopic.add(rows.get(0).get("topic_id"));
	    	    	itemTopic.add(rows.get(0).get("topic_title"));
	    	    	outputObj.put("value", itemTopic);
        			outputObj.put("error", "");
    			}else{
    				outputObj.put("value", "");
        			outputObj.put("error", "ERROR_NOTOPIC");
    			}
    		}else{
    			outputObj.put("value", "");
    			outputObj.put("error", "ERROR_TREFEREE");
    		}
    	}else{
    		outputObj.put("value", "");
			outputObj.put("error", "ERROR_INVALIDUSER");
    	}
    	
	}

}
