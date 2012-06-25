package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.rounds;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getTeamDocListForCurrentRoundServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		rounds roundObj = new rounds();
    	String Order = request.getParameter("sort_on");
    	String sessionID = request.getParameter("session_id");
    	user userObj = new user();
    	if ((sessionID!= null)){
    		if (userObj.getRoleBySession(sessionID).compareTo("TEAM")==0){
    			String userID =userObj.getUserBySession(sessionID) ;
	    		ArrayList<HashMap<String, String>> rows = roundObj.getDoclistSubmitinCurrentRound(userID, Order);
				if (rows.size()>0){
					LinkedList<Object> listTopic = new LinkedList<Object>();
					LinkedList<String> itemTopic;
					for (int i=0;i<rows.size();i++){
		    	    	itemTopic = new LinkedList<String>();
		    	    	itemTopic.add(rows.get(i).get("doc_id"));
		    	    	itemTopic.add(rows.get(i).get("doc_title"));
		    	    	itemTopic.add(rows.get(i).get("submittime"));
		    	    	
		    	    	listTopic.add(itemTopic);
					}
	    	    	outputObj.put("value", listTopic);
	    			outputObj.put("error", null);
				}else{
					outputObj.put("value", null);
					if (roundObj.userInCurrentRound(userID)){
						outputObj.put("error", "ERROR_NODOCUMENT");
					}else{
						outputObj.put("error", "ERROR_NOROUND");
					}
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
