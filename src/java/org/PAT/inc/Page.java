package org.PAT.inc;

import java.util.HashMap;
import java.util.Map;

public class Page {
    private String template;
    private String forward;
    @SuppressWarnings("unchecked")
	private Map root = new HashMap();

    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(String template) {
        forward = null;
        this.template = template;
    }

    @SuppressWarnings("unchecked")
	public void put(String name, Object value) {
        root.put(name, value);
    }
    
    @SuppressWarnings("unchecked")
	public void put(String name, int value) {
        root.put(name, new Integer(value));
    }
    
    @SuppressWarnings("unchecked")
	public void put(String name, double value) {
        root.put(name, new Double(value));
    }

    @SuppressWarnings("unchecked")
	public void put(String name, boolean value) {
        root.put(name, new Boolean(value));
    }
    
    @SuppressWarnings("unchecked")
	public Map getRoot() {
        return root;
    }
    
    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        template = null;
        this.forward = forward;
    }

}
