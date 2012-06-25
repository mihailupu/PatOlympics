package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.rounds;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getRoundInfoServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		rounds roundObj = new rounds();
    	String roundID = request.getParameter("round_id");
    	if ((roundID!= null)){
			ArrayList<HashMap<String, String>> rows = roundObj.getRound(roundID);
			if (rows.size()>0){
    	    	LinkedList<String> itemRound = new LinkedList<String>();
    	    	itemRound.add(rows.get(0).get("start_time"));
    	    	itemRound.add(rows.get(0).get("end_time"));
    	    	outputObj.put("value", itemRound);
    			outputObj.put("error", "");
			}else{
				outputObj.put("value", "");
    			outputObj.put("error", "ERROR_NOROUND");
			}
    	}else{
    		outputObj.put("value", "");
			outputObj.put("error", "ERROR_NOROUND");
    	}
    	
	}

}
