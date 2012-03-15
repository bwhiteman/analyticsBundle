/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
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
    
    @Test
    public void testValidPythonHadoopJobXMLParse(){
        try{
            PythonHadoopJob myJob = new PythonHadoopJob(validPythonHadoop);
            String task = myJob.getTask();
            assertEquals(task, "/srv/hadoop/bin/hadoop jar /srv/hadoop/contrib/streaming/hadoop-0.20.2-streaming.jar -file /analytics/tmp/mapper.py "
                    + "-mapper mapper.py -file /analytics/tmp/reducer.py -reducer reducer.py -input /tmp/txt/* -output /tmp/out");
        }
        catch(Exception e){
            fail();
        }
    }
    
    @Test(expected=Exception.class)
    public void testInvalidPythonHadoopJobXMLParse() throws Exception{
        PythonHadoopJob myJob = new PythonHadoopJob(invalidPythonHadoop);     
    }
}
