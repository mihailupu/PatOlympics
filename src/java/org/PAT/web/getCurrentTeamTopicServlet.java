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
public class getCurrentTeamTopicServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		topic topicObj = new topic();
    	String userID = request.getParameter("user_id");
		user userObj = new user();
    	if ((userID!= null)){
    		if (userObj.getRoleByUser(userID).compareTo("TEAM")==0){
    			ArrayList<HashMap<String, String>> rows = topicObj.getCurrentTopicByUser(userID);
    			if (rows.size()>0){
	    	    	LinkedList<String> itemTopic = new LinkedList<String>();;
	    	    	itemTopic.add(rows.get(0).get("topic_id"));
	    	    	itemTopic.add(rows.get(0).get("topic_title"));
	    	    	outputObj.put("value", itemTopic);
        			outputObj.put("error", null);
    			}else{
    				outputObj.put("value", null);
        			outputObj.put("error", "ERROR_NOTOPIC");
    			}
    		}else{
    			outputObj.put("value", null);
    			outputObj.put("error", "ERROR_NOTEAM");
    		}
    	}else{
    		outputObj.put("value", null);
			outputObj.put("error", "ERROR_INVALIDUSER");
    	}
    	
	}

}
