/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author SPines
 */
public class PythonJob implements Job {
    
    private String fileName;
    private List<String> params;

    public PythonJob(Document doc) throws InvalidXMLException{
        NodeList n = doc.getElementsByTagName("input");
        fileName = Configurations.ANALYTIC_ROOT + ((Element) doc.getElementsByTagName("analytic").item(0)).getAttribute("name");        
        params = new ArrayList<String>();
        
        if (n == null || fileName == null){
            throw new InvalidXMLException();
        }
        
        for (int i = 0; i < n.getLength(); i++){
            Element e = (Element) n.item(i);
            params.add(e.getAttribute("value"));
        }   
    }
    
    // python file.py param1 param2 param3
    public String getTask() {
        String ret = Configurations.PYTHON_PATH + " " + fileName + ".py";
        for (String s : params){
            ret += " " + s;
        }
        return ret;
    }
}
