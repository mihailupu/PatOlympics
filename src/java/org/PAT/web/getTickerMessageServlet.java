package org.PAT.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.PAT.common.ticker_message;
import org.PAT.inc.ControllerServlet;

/**
 * Servlet implementation class excelSevlet
 */
public class getTickerMessageServlet extends ControllerServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void pageAction(HttpServletRequest request, HttpServletResponse response){
            ticker_message ticker_messageObj=new ticker_message();
            String message = ticker_messageObj.getMessage();
		outputObj.put("value", message);
		outputObj.put("error", "");
	}

}
