package org.PAT.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getUseridBySessionidServlet extends ControllerServlet {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public void pageAction(HttpServletRequest request, HttpServletResponse response) {
        String sessionID = request.getParameter("session_id");
        user userObj = new user();
        if ((sessionID != null)) {
            String userID = userObj.getUserBySession(sessionID);
            if (userID != null && !userID.isEmpty()) {
                outputObj.put("value", userID);
                outputObj.put("error", null);
            } else {
                outputObj.put("value", null);
                outputObj.put("error", "ERROR_INVALIDSESSION");
            }
        } else {
            outputObj.put("value", null);
            outputObj.put("error", "ERROR_INVALIDSESSION");
        }
    }
}
