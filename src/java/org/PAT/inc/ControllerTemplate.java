package org.PAT.inc;

import freemarker.template.*;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerTemplate extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Configuration cfg;
    protected HttpSession pageSession;

    @Override
    public void init() {
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(
                getServletContext(), "WEB-INF/tpl");
        cfg.setTemplateUpdateDelay(0);
        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        cfg.setLocale(Locale.US);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Config.init(getServletContext());
        pageSession = req.getSession(true);
        //if (true)	throw new ServletException("Unknown action: " + getMethod().getName().toString());

        String action = req.getRequestURL().toString();
        if (action == null) {
            action = "index";
        }
        if (action.indexOf("/") >= 0) {
            action = action.substring(action.lastIndexOf("/") + 1);
        }

        if (action.lastIndexOf(".") != -1) {
            action = action.substring(0, action.lastIndexOf("."));
        }

        if (!action.startsWith("login")) {
            if (!action.startsWith("logout")) {
                if ((pageSession.getAttribute("isLogin") == null) || (!(Boolean) pageSession.getAttribute("isLogin"))) {
                    resp.sendRedirect("../admin.html");
                }
            }
        }

        Method actionMethod;
        try {
            actionMethod =
                    getClass().getMethod(action + "Action",
                    new Class[]{HttpServletRequest.class, HttpServletResponse.class, Page.class});
        } catch (NoSuchMethodException e) {
            throw new ServletException("Unknown action: " + action);
        }

        req.setCharacterEncoding(cfg.getOutputEncoding());

        Page page = new Page();
        try {
            actionMethod.invoke(this, new Object[]{req, resp, page});
        } catch (IllegalAccessException e) {
            throw new ServletException(e);
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getTargetException());
        }

        if (page.getTemplate() != null) {
            HttpSession session = req.getSession(true);
            if (session.getAttribute("isNotAdmin") != null) {
                page.put("isNotAdmin", session.getAttribute("isNotAdmin").toString());
            } else {
                page.put("isNotAdmin", "true");
            }

            Template t = cfg.getTemplate(page.getTemplate());

            resp.setContentType("text/html; charset=" + cfg.getOutputEncoding());

            resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, "
                    + "post-check=0, pre-check=0");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "Thu, 01 Dec 2015 00:00:00 GMT");
            Writer out = resp.getWriter();

            try {
                t.process(page.getRoot(), out);
            } catch (TemplateException e) {
                throw new ServletException(
                        "Error while processing FreeMarker template", e);
            }
        } else if (page.getForward() != null) { // forward request
            RequestDispatcher rd = req.getRequestDispatcher(page.getForward());
            rd.forward(req, resp);
        } else {
            throw new ServletException("The action didn't specified a command.");
        }
    }
}