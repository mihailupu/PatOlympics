package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.topic;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getTopicDataServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		topic topicObj = new topic();
    	String topicID = request.getParameter("topic_id");
    	if ((topicID!= null)){
			ArrayList<HashMap<String, String>> rows = topicObj.getTopic(topicID);
			if (rows.size()>0){
    	    	LinkedList<String> itemTopic = new LinkedList<String>();
    	    	itemTopic.add(rows.get(0).get("topic_title"));
    	    	itemTopic.add(rows.get(0).get("topic_description"));
    	    	outputObj.put("value", itemTopic);
    			outputObj.put("error", "");
			}else{
				outputObj.put("value", "");
    			outputObj.put("error", "ERROR_NOTOPIC");
			}
    	}else{
    		outputObj.put("value", "");
			outputObj.put("error", "ERROR_NOTOPIC");
    	}
    	
	}

}
