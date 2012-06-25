package org.PAT.web;


import java.util.Date;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

public class authenticateUserServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		user userObj = new user();
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (userObj.login(username, password)){
			Date d = new Date();
			pageSession.setAttribute("loginTime", d);
			pageSession.setAttribute("userID", username);
			pageSession.setAttribute("isLogin", true);
			pageSession.setAttribute("fullname", userObj.fullname);
			pageSession.setAttribute("user_role", userObj.user_role);
			
			
			LinkedList<String> value = new LinkedList<String>();
			value.add(pageSession.getId());
			value.add(username);
			value.add(userObj.user_role);
			userObj.updateSession(username,pageSession.getId());
			outputObj.put("value", value);
			outputObj.put("error", null);
    	}else{
    		pageSession.removeAttribute("userID");
    		pageSession.removeAttribute("loginTime");
    		pageSession.removeAttribute("isLogin");
    		pageSession.removeAttribute("fullname");
    		pageSession.removeAttribute("user_role");
    		outputObj.put("value", null);
			outputObj.put("error", userObj.error);
    	}
        
	}

}
