package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.sports;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getSportsServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
    	
    	sports spObj  = new sports();
    	ArrayList<HashMap<String, String>> result = spObj.getList();
    	
    	LinkedList<Object> listSport = new LinkedList<Object>();
    	LinkedList<String> itemSport = new LinkedList<String>();
    	
    	for (int i=0;i<result.size();i++){
    		itemSport  = new LinkedList<String>();
    		itemSport.add(result.get(i).get("sport_id"));
    		itemSport.add(result.get(i).get("sport_title"));
    		listSport.add(itemSport);
    	}
		
		outputObj.put("value", listSport);
		outputObj.put("error", "");
	}

}
