package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.scoreboard;
import org.PAT.common.sports;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getScoreboardForSportServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
		String sport_id = request.getParameter("sport_id");
		
		sports sportObj = new sports();
    	if (sportObj.checkSport(sport_id)){
    		scoreboard scoreObj = new scoreboard();
    		
    		ArrayList<HashMap<String, String>> rows = scoreObj.getScoreboard(sport_id);
    		
    		LinkedList<Object> listScore = new LinkedList<Object>();
	    	LinkedList<Object> itemScore;
	    	for (int i=0;i<rows.size();i++){
	    		itemScore  = new LinkedList<Object>();
	    		itemScore.add(rows.get(i).get("user_id"));
	    		itemScore.add(rows.get(i).get("referee_name_current_round"));
	    		itemScore.add(rows.get(i).get("referee_name_next_round"));
	    		itemScore.add(Float.parseFloat(rows.get(i).get("score")));
	    		itemScore.add(Float.parseFloat(rows.get(i).get("score_relevant")));
	    		itemScore.add(Float.parseFloat(rows.get(i).get("score_happiness")));
	    		
	    		listScore.add(itemScore);
	    	}
    		outputObj.put("value", listScore);
			outputObj.put("error", "");
    	}else{
    		outputObj.put("value", "");
			outputObj.put("error", "ERROR_NOSPORT");
    	}
    	
	}

}
