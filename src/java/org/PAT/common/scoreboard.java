package org.PAT.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;



public class scoreboard extends MySQL {

    public scoreboard() {
        super();
    }

    /**
     * This needs to do more...
     * every time there is this request, we must check what time it is, in which round we are, so we can respond with the full answer
     * @param sport_id
     * @return
     */
    public ArrayList<HashMap<String, String>> getScoreboard(String sport_id) {


        String[] fields2 = {"username", "fullname"};
        String Query = "SELECT * FROM users WHERE user_role='TEAM' ";

        ArrayList<HashMap<String, String>> userset = this.query(Query, fields2);
        int usersetSize = userset.size();
        ArrayList<HashMap<String, String>> result;
        for (int i = 0; i < usersetSize; i++) {

            String expert;
            String nextExpert;
            String user_id = userset.get(i).get("username");
            rounds roundObj = new rounds();
            if (roundObj.getCurrentRound().size() > 0) {
                String currentroundid = roundObj.getCurrentRound().get(0).get("round_id");



                String[] fields = {"expert_title"};
                result = this.query("SELECT * FROM playinggame WHERE team_id='" + user_id + "' and sport_id=" + sport_id + " AND round_id=" + currentroundid + " ORDER BY round_id, topic_id", fields);

                if (result.isEmpty()) {
                    expert = "---";
                } else {
                    expert = result.get(0).get("expert_title");
                }




            } else {
                //there is no current round
                expert = "---";
            }
            if (roundObj.getNextRound().size() > 0) {
                String nextroundid = roundObj.getNextRound().get(0).get("round_id");
                String[] fields3 = {"expert_title"};
                result = this.query("SELECT * FROM playinggame WHERE team_id='" + user_id + "' and sport_id=" + sport_id + " AND round_id=" + nextroundid + " ORDER BY round_id, topic_id", fields3);

                if (result.isEmpty()) {
                    nextExpert = "---";
                } else {
                    nextExpert = result.get(0).get("expert_title");
                }
            } else {
                nextExpert = "---";
            }

            this.Update("update scoreboard set referee_name_current_round='" + expert + "', referee_name_next_round='" + nextExpert + "' " +
                    "where sport_id='" + sport_id + "' and user_id='" + user_id + "';");
        }

        String[] fields1 = {"user_id", "referee_name_current_round", "referee_name_next_round", "score", "score_relevant", "score_happiness"};

        result = this.query("SELECT * FROM scoreboard WHERE sport_id='" + sport_id + "' ORDER BY user_id", fields1);

        return result;
    }

    /**
     * @TODO
     *
     * This method calculates the precision/recall values whenever new docs are submitted/withdrawn by a team or referee
     * for a specific topic
     *
     * called from:
     * submitRefereeDocListForTopicServlet,
     * clearRefereeEntryForTopicServlet,
     * clearRefereeDocListForTopicServlet
     * clearTeamEntryForCurrentRoundServlet
     * clearTeamDocListForCurrentRoundServlet
     *
     * @param topic_id
     * @return
     */
    public boolean refreshScorePerSport(String sport_id, java.lang.String user_id) {
        if (user_id.equals("")) {
            //means it was a referee doing the update, update all teams' scores
            String[] fields2 = {"username", "fullname"};
            String Query = "SELECT * FROM users WHERE user_role='TEAM' ";

            ArrayList<HashMap<String, String>> userset = this.query(Query, fields2);
            int usersetSize = userset.size();
            ArrayList<HashMap<String, String>> result;
            for (int i = 0; i < usersetSize; i++) {
                String team_id = userset.get(i).get("username");
                updateTeamScore(team_id,sport_id);
            }
        } else {
            //update only this team's scores
            updateTeamScore(user_id,sport_id);
        }
        return true;
    }

    private void updateTeamScore(String team_id,String sport_id) {
        String[] fields = {"topic_id"};
        ArrayList<HashMap<String, String>> teamTopics = this.query("select distinct topic_id from playinggame game where team_id='" + team_id + "' and sport_id='"+sport_id+"';", fields);
        int teamTopicsSize = teamTopics.size();
        int retrievedDocuments = 0;
        for (int i = 0; i < teamTopicsSize; i++) {
            String topic_id = teamTopics.get(i).get("topic_id");

            String[] fields1 = {"doc_id"};
            ArrayList<HashMap<String, String>> result = this.query("select * from submitdoc where user_id='" + team_id + "' and topic_id='" + topic_id + "' and doc_id in (select doc_id from submitdoc where topic_id='" + topic_id + "' and submit_role='REFEREE');", fields1);
            retrievedDocuments += result.size();
        }

        scoreboard scoreboardObj = new scoreboard();

        String[] fields2={"score"};
        ArrayList<HashMap<String,String>> userHappiness = this.query("select * from userHappiness where team_id='"+team_id+"';",fields2);
        float averageScore=0;

        if (!userHappiness.isEmpty()){

        int userHappinessSize=userHappiness.size();
        for (int i = 0 ; i < userHappinessSize;i++){
            float score=Float.parseFloat(userHappiness.get(i).get("score"));
            averageScore=averageScore+score;
        }
        averageScore = averageScore/userHappinessSize;
        }
        //the final score is the number of retrieved documents increased with a percentage based on the average user happiness:
        //if the average user happines =0 then no increase, if the average user happines = 5 (max value) then it is doubled
        float finalScore = retrievedDocuments*(100+averageScore*20)/100;

        DecimalFormat myFormatter = new DecimalFormat("###.##");
        String finalScoreString = myFormatter.format(finalScore);
        String averageScoreString= myFormatter.format(averageScore) ;
        String retrievedDocumentsString=myFormatter.format((float)retrievedDocuments) ;
        
        scoreboardObj.Update("update scoreboard " +
                " set score_relevant="+retrievedDocumentsString+", " +
                "score="+finalScoreString+", " +
                "score_happiness="+averageScoreString+" " +
                "where user_id='"+team_id+"' and sport_id='"+sport_id+"'"+
                ";");

    }
}
