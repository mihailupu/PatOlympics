package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.sports;
import org.PAT.common.user;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getRefereeTopicsServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
    	sports spObj = new sports();
    	String userID = request.getParameter("user_id");
	user userObj = new user();
        userObj.getRoleByUser(userID);

    	if ((userID!= null)){
    		if (userObj.getRoleByUser(userID).compareTo("REFEREE")==0){
    			ArrayList<HashMap<String, String>> rows = spObj.getTopicsByUser(userID);
    			if (rows.size()>0){
	    			String lastSportID = rows.get(0).get("sport_id");
	    			LinkedHashMap<String,Object> listSport = new LinkedHashMap<String,Object>();
	    	    	LinkedList<String> itemSport = new LinkedList<String>();;
	    	    	for (int i=0;i<rows.size();i++){
	    	    		if (lastSportID.compareTo(rows.get(i).get("sport_id"))!=0){
	    	    			if (itemSport != null){
	    	    				listSport.put(lastSportID,itemSport);
	    	    			}
	    	    			itemSport  = new LinkedList<String>();
	    	    		}
	    	    		
	    	    		itemSport.add(rows.get(i).get("topic_id"));
	    	    		lastSportID = rows.get(i).get("sport_id");	    	    		
	    	    	}
	    	    	
	    	    	listSport.put(lastSportID,itemSport);
	    	    	
	    	    	outputObj.put("value", listSport);
        			outputObj.put("error", "");
    			}else{
    				outputObj.put("value", "");
        			outputObj.put("error", "ERROR_NOSPORT");
    			}
    		}else{
    			outputObj.put("value", "");
    			outputObj.put("error", "ERROR_NOREFEREE");
    		}
    	}else{
    		outputObj.put("value", "");
			outputObj.put("error", "ERROR_INVALIDUSER");
    	}
    	
	}

}
