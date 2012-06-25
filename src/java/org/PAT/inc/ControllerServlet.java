package org.PAT.inc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

public class ControllerServlet extends HttpServlet {

    protected JSONObject outputObj;
    protected HttpSession pageSession;
    private static final long serialVersionUID = 1L;

    public ControllerServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        pageSession = request.getSession(true);

        Config.init(getServletContext());
        outputObj = new JSONObject();

        String action = request.getRequestURL().toString();

        if (action == null) {
            action = "pageAction";
        }

        if (action.endsWith(".html")) {
            if (action.indexOf("/") >= 0) {
                action = action.substring(action.lastIndexOf("/") + 1);
            }

            if (action.lastIndexOf(".") != -1) {
                action = action.substring(0, action.lastIndexOf("."));
            }
            action += "Action";
        } else {
            action = "pageAction";
        }


        Method actionMethod;
        try {
            actionMethod =
                    getClass().getMethod(action,
                    new Class[]{HttpServletRequest.class, HttpServletResponse.class});
        } catch (NoSuchMethodException e) {
            throw new ServletException("Unknown action: " + action);
        }

        try {
            actionMethod.invoke(this, new Object[]{request, response});
        } catch (IllegalAccessException e) {
            throw new ServletException(e);
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getTargetException());
        }

        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();

        out.println(outputObj);
        out.close();
    }

    public boolean isLogin(HttpServletRequest request) {
        boolean result;
        if ((pageSession.getAttribute("isLogin") == null) || (!(Boolean) pageSession.getAttribute("isLogin"))) {
            result = false;
        } else {
            Date d = new Date();
            pageSession.setAttribute("loginTime", d);
            pageSession.setAttribute("isLogin", true);
            result = true;
        }
        return result;

    }
}