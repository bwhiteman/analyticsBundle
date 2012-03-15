/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.Configurations;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class PythonJobTest {
    
    private Document validPython = null;;
    
    public PythonJobTest() {
    }
    
    @Before
    public void setUp() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        validPython = dBuilder.parse(ClassLoader.getSystemResourceAsStream("shipSorter.xml"));
        validPython.getDocumentElement().normalize();
    }
    
    @Test
    public void testValidPythonJobXMLParse(){
        try{
            PythonJob myJob = new PythonJob(validPython);
            String task = myJob.getTask();
            assertEquals(task, Configurations.PYTHON_PATH + " " + Configurations.ANALYTIC_ROOT + "shipSorter.py aisShipData.csv ADEONA");
        }
        catch(Exception e){
            fail();
        }
    }
}
