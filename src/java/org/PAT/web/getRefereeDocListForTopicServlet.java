package org.PAT.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.rounds;
import org.PAT.common.topic;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getRefereeDocListForTopicServlet extends ControllerServlet {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public void pageAction(HttpServletRequest request, HttpServletResponse response) {
        rounds roundObj = new rounds();
        topic topicObj = new topic();

        String Order = request.getParameter("sort_on");
        String topic_id = request.getParameter("topic_id");
        if (topic_id != null) {
            ArrayList<HashMap<String, String>> rows = roundObj.getDoclistSubmitedByTopic(topic_id, Order);
            if (rows.size() > 0) {
                LinkedList<Object> listTopic = new LinkedList<Object>();
                LinkedList<String> itemTopic;
                for (int i = 0; i < rows.size(); i++) {
                    itemTopic = new LinkedList<String>();
                    itemTopic.add(rows.get(i).get("doc_id"));
                    itemTopic.add(rows.get(i).get("doc_title"));
                    itemTopic.add(rows.get(i).get("submittime"));

                    listTopic.add(itemTopic);
                }
                outputObj.put("value", listTopic);
                outputObj.put("error", null);
            }
        } else {
            outputObj.put("value", null);
            outputObj.put("error", "ERROR_NOTOPIC");
        }

    }
}
