package org.PAT.inc;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class Config {
	
	public static ServletContext SERV_CONTEXT = null;
	private static String FILE_NAME = "WEB-INF/config.xml";

	
	private static Element ROOT     = null;
	
	//SAXReader reader  = null;
	static Document contents = null;
	  

	@SuppressWarnings("unchecked")
	private HashMap configValues = new HashMap();
	  
	public static void init(ServletContext context) {
		SERV_CONTEXT = context;
	}
	
	@SuppressWarnings("unchecked")
	public Config() {		
		SAXReader reader = new SAXReader();
		
		try {
			File xmlFile = new File(SERV_CONTEXT.getRealPath("/") + FILE_NAME);
			contents = reader.read(xmlFile);
		} catch (DocumentException e) {
			throw new IllegalAccessError("Can not read:"+SERV_CONTEXT.getRealPath("/") + FILE_NAME);
		}
		ROOT = contents.getRootElement();		
		for (Iterator i = ROOT.elementIterator(); i.hasNext(); ) {
	      Element element = (Element) i.next();	      	      
	      for (Iterator ii = element.elementIterator(); ii.hasNext(); ) {
	          Element cElement = (Element) ii.next();
	          configValues.put(cElement.getName(), cElement.getStringValue());
	        }
	    }		
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap getConfig(String ConfName){
		HashMap returnVal = new HashMap();
		SAXReader reader = new SAXReader();
		
		try {
			File xmlFile = new File(SERV_CONTEXT.getRealPath("/") + FILE_NAME);
			contents = reader.read(xmlFile);
		} catch (DocumentException e) {
			throw new IllegalAccessError("Can not read:"+SERV_CONTEXT.getRealPath("/") + FILE_NAME);
		}
		ROOT = contents.getRootElement();		
		for (Iterator i = ROOT.elementIterator(); i.hasNext(); ) {
	      Element element = (Element) i.next();	
		  if (element.getName().compareTo(ConfName)==0){
			for (Iterator ii = element.elementIterator(); ii.hasNext(); ) {
			  Element cElement = (Element) ii.next();
			  returnVal.put(cElement.getName(), cElement.getStringValue());
			}
			break;	
		  }
	    }	
		return returnVal;
	}
	
	public String getWebUser() {
		return configValues.get("sql_user").toString();
	}

	public String getWebPass() {
		return configValues.get("sql_pass").toString();
	}
	
	public String getParam(String name){
		return configValues.get(name).toString();
	}
	
	public String getWebHost() {
		return configValues.get("sql_host").toString();
	}
	
	public String getWebDB() {
		return configValues.get("sql_dbname").toString();
	}
	
	public String getWebPort() {
		return configValues.get("sql_port").toString();
	}
	
}
