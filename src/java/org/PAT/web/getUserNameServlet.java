package org.PAT.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getUserNameServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		String userID = request.getParameter("user_id");
		String sessionUser = pageSession.getAttribute("userID").toString();
    	if ((userID!= null)&&(sessionUser!= null)&&(sessionUser.compareTo(userID)==0)){
			outputObj.put("value", pageSession.getAttribute("fullname").toString());
			outputObj.put("error", "");
		}else{
			outputObj.put("value", "");
			outputObj.put("error", "ERROR_INVALIDUSER");
		}
    	
	}

}
