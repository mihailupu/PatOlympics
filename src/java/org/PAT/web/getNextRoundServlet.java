package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.rounds;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getNextRoundServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		rounds roundObj = new rounds();
		ArrayList<HashMap<String, String>> rows = roundObj.getNextRound();
		if (rows.size()>0){
	    	outputObj.put("value", rows.get(0).get("round_id"));
			outputObj.put("error", "");
		}else{
			outputObj.put("value", "");
			outputObj.put("error", "ERROR_NOROUND");
		}
    	
	}

}
