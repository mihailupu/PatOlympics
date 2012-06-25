package org.PAT.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.rounds;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class userInCurrentRoundServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		rounds roundObj = new rounds();
    	
		String sessionID = request.getParameter("session_id");
		String userID = null;
		user userObj = new user();
		
    	if ((sessionID!= null)){
    		userID = userObj.getUserBySession(sessionID);
    	}
    	if ((sessionID!= null)&&(userID!=null)){
	    	outputObj.put("value", roundObj.userInCurrentRound(userID));
			outputObj.put("error", null);
    	}else{
    		outputObj.put("value", null);
			outputObj.put("error", "ERROR_INVALIDSESSION");
    	}
	}
}
