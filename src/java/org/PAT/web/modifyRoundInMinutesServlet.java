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
public class modifyRoundInMinutesServlet extends ControllerServlet {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public void pageAction(HttpServletRequest request, HttpServletResponse response) {
        rounds roundObj = new rounds();

        String minutesToExtend = request.getParameter("minutes");
        String password = request.getParameter("password");

        if (!password.equals("adminpass")) {
            outputObj.put("value", "");
            outputObj.put("error", "ERROR_NORIGHTS");
            return;
        }

        int minutes;
        try {
            minutes = Integer.parseInt(minutesToExtend);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            outputObj.put("value", minutesToExtend);
            outputObj.put("error", "ERROR_NUMBERFORMATEXCEPTION");
            return;
        }

        long millisecs = minutes * 60 * 1000;
        ArrayList<HashMap<String, String>> rows = roundObj.getCurrentRound();
        if (rows.size() > 0) {
            String currentRoundID = rows.get(0).get("round_id");
            long currentRoundStartTime=Long.parseLong(rows.get(0).get("start_time"));
            long currentRoundEndTime=Long.parseLong(rows.get(0).get("end_time"));

            if ((currentRoundID != null)) {
                rows = roundObj.getFutureRounds(currentRoundID);
                if (rows.size() > 0) {
                    if (millisecs > 0) {
                        for (int i = 0; i < rows.size(); i++) {
                            //the list of rows is returned in referese, appropriate for when we want to add minutes (so we add starting with the last round
                            String roundid = rows.get(i).get("round_id");
                            long oldStartTime = Long.parseLong(rows.get(i).get("start_time"));
                            long oldEndTime = Long.parseLong(rows.get(i).get("end_time"));
                            roundObj.update(roundid, roundid, String.valueOf(oldStartTime + millisecs), String.valueOf(oldEndTime + millisecs), "");                        
                        }
                        //finally, extend the current round (just it's end time)
                        roundObj.update(currentRoundID, currentRoundID, String.valueOf(currentRoundStartTime), String.valueOf(currentRoundEndTime + millisecs), "");
                    } else {
                        //at this point we want to substract , so we will do it in the order of the rounds, which is the opposit of what is returned
                        //start with the current round;
                                                
                        roundObj.update(currentRoundID, currentRoundID, String.valueOf(currentRoundStartTime), String.valueOf(currentRoundEndTime + millisecs), "");
                        for (int i = rows.size()-1; i >=0; i--) {
                            //the list of rows is returned in referese, appropriate for when we want to add minutes (so we add starting with the last round
                            String roundid = rows.get(i).get("round_id");
                            long oldStartTime = Long.parseLong(rows.get(i).get("start_time"));
                            long oldEndTime = Long.parseLong(rows.get(i).get("end_time"));
                            roundObj.update(roundid, roundid, String.valueOf(oldStartTime + millisecs), String.valueOf(oldEndTime + millisecs), "");
                        }

                    }
                    outputObj.put("value", "ok");
                    outputObj.put("error", "");
                } else {
                    outputObj.put("value", "");
                    outputObj.put("error", "ERROR_NOROUND");
                }
            } else {
                outputObj.put("value", "");
                outputObj.put("error", "ERROR_NOROUND");
            }

        }
    }
}
