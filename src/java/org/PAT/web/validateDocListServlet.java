package org.PAT.web;

import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.PAT.common.docid;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class validateDocListServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
    	
    	String doclist = request.getParameter("doclist");
    	
		String docid ;
		docid docObj = new docid();
		if (doclist != null){
			if (doclist.startsWith("||")){
				doclist = doclist.substring(2);
			}
			if (doclist.endsWith("||")){
				doclist = doclist.substring(0,doclist.length()-2);
			}
			LinkedList<Object> listDocs = new LinkedList<Object>();
			LinkedList<Object> itemDoc;
			
			while (doclist.indexOf("||")>=0){
				docid = doclist.substring(0,doclist.indexOf("||"));
				doclist = doclist.substring(doclist.indexOf("||")+2);
				itemDoc = new LinkedList<Object>();
				itemDoc.add(docid);
				itemDoc.add(docObj.checkDoc(docid));
				itemDoc.add(docObj.getTitle());
		    	listDocs.add(itemDoc);
		    	
			}
			if (!doclist.isEmpty()){
				itemDoc = new LinkedList<Object>();
				itemDoc.add(doclist);
				itemDoc.add(docObj.checkDoc(doclist));
				itemDoc.add(docObj.getTitle());
		    	listDocs.add(itemDoc);
			}
			outputObj.put("value", listDocs);
			outputObj.put("error", "");
		}else{
			outputObj.put("value", "");
			outputObj.put("error", "ERROR_INVALIDDOCLIST");
		}
		
	}

}
