/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs.impl;

import org.codehaus.jackson.map.ObjectMapper;
import com.potomacfusion.asfframework.jobs.Workflow;
import com.potomacfusion.asfframework.Configurations;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class PythonJobTest {
    
    public PythonJobTest() {
    }
    
    @Test
    public void testValidPythonJobJSONParse(){
        try{
            Configurations.init("src/main/resources/remote.properties");
            ObjectMapper om = new ObjectMapper();
            Workflow w = om.readValue(ClassLoader.getSystemResourceAsStream("shipSorter.json"), Workflow.class);
            PythonJob myJob = (PythonJob) w.getWorkflow().get(0);
            String task = myJob.toCommandLine();
            assertEquals(task, Configurations.getProperty("PYTHON_PATH") + " " + "shipSorter.py aisShipData.csv ADEONA");
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
