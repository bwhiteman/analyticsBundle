/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author SPines
 */
public class PythonHadoopJob implements Job{

    //./hadoop jar ../contrib/streaming/hadoop-streaming-0.20.2-cdh3u3.jar -file 
    // /home/hduser/mapper.py -mapper /home/hduser/mapper.py -file /home/hduser/reducer.py 
    // -reducer /home/hduser/reducer.py -input /user/hduser/gutenberg/* -output /user/hduser/gutenberg-output
    private String mapper;
    private String reducer;
    private String input;
    private String output;
    
    private enum ATTRIBUTES {MAPPER, REDUCER, INPUT, OUTPUT};
    
    public PythonHadoopJob(Document doc) throws InvalidXMLException{

        NodeList n = doc.getElementsByTagName("input");        
        
        for (int i = 0; i < n.getLength(); i++){
            Element e = (Element) n.item(i);
            switch(ATTRIBUTES.valueOf(e.getAttribute("name").toUpperCase())){
                case MAPPER:
                    this.mapper = e.getAttribute("value");
                    break;
                case REDUCER:
                    this.reducer = e.getAttribute("value");
                    break;
                case INPUT:
                    this.input = e.getAttribute("value");
                    break;
                case OUTPUT:
                    this.output = e.getAttribute("value");
                    break;
            }            
        }
        
        if (mapper == null || reducer == null || input == null || output == null){
            throw new InvalidXMLException();
        }        
    }
    
    public String getTask() {
        return Configurations.HADOOP + " jar " +
               Configurations.HADOOP_STREAMING_JAR + " " +
               "-file " + mapper + " -mapper " + mapper + " " +
               "-file " + reducer + " -reducer " + reducer + " " +
               "-input " + input + " -output " + output;
    }
}
