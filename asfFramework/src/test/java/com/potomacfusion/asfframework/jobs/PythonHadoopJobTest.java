/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author SPines
 */
public class PythonHadoopJobTest {
    
    private Document validPythonHadoop = null;
    private Document invalidPythonHadoop = null;
    
    public PythonHadoopJobTest() {
    }
    
    @Before
    public void setUp() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        validPythonHadoop = dBuilder.parse(ClassLoader.getSystemResourceAsStream("validpythonhadoop.xml"));
        validPythonHadoop.getDocumentElement().normalize();
        
        invalidPythonHadoop = dBuilder.parse(ClassLoader.getSystemResourceAsStream("invalidpythonhadoop.xml"));
        invalidPythonHadoop.getDocumentElement().normalize();
    }
    
//    @Test
//    public void testValidPythonHadoopJobXMLParse(){
//        try{
//            Element e = (Element)validPythonHadoop.getChildNodes().item(0);
//            PythonHadoopJob myJob = new PythonHadoopJob(e);
//            String task = myJob.getTask();
//            System.out.println(task);
//            assertEquals(task, Configurations.getProperty("HADOOP") + " jar " + Configurations.getProperty("HADOOP_STREAMING_JAR") + " -file /analytics/tmp/mapper.py "
//                    + "-mapper mapper.py -file /analytics/tmp/reducer.py -reducer reducer.py -input /tmp/txt/* -output /tmp/out");
//        }
//        catch(Exception e){
//            fail();
//        }
//    }
//    
//    @Test(expected=Exception.class)
//    public void testInvalidPythonHadoopJobXMLParse() throws Exception{
//        Element e = (Element)invalidPythonHadoop.getChildNodes().item(0);
//        PythonHadoopJob myJob = new PythonHadoopJob(e);     
//    }
}
