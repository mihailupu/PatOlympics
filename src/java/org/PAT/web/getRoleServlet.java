package org.PAT.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getRoleServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		String sessionID = request.getParameter("session_id");
		String userRole = "";
		if (sessionID!= null){
			user userObj = new user();
			userRole = userObj.getRoleBySession(sessionID);
		}
		outputObj.put("value", userRole);
		outputObj.put("error", null);
	}
}
