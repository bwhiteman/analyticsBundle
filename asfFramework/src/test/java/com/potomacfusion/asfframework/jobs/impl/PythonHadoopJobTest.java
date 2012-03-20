/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs.impl;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.jobs.Workflow;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class PythonHadoopJobTest {
    
    public PythonHadoopJobTest() {
    }

    
    @Test
    public void testValidPythonHadoopJobJSONParse(){
        try{
            Configurations.init("src/main/resources/remote.properties");
            ObjectMapper om = new ObjectMapper();
            Workflow w = om.readValue(ClassLoader.getSystemResourceAsStream("validpythonhadoop.json"), Workflow.class);
            PythonHadoopJob myJob = (PythonHadoopJob) w.getWorkflow().get(0);
            String task = myJob.toCommandLine();
            assertEquals(task, Configurations.getProperty("HADOOP") + " jar " + Configurations.getProperty("HADOOP_STREAMING_JAR") + " -file mapper.py "
                    + "-mapper mapper.py -file reducer.py -reducer reducer.py -input /tmp/txt/* -output /tmp/out/");
        }
        catch(Exception e){
            fail();
        }
    }
    
    @Test(expected=Exception.class)
    public void testInvalidPythonHadoopJobJSONParse() throws Exception{
        Configurations.init("src/main/resources/remote.properties");
        ObjectMapper om = new ObjectMapper();
        Workflow w = om.readValue(ClassLoader.getSystemResourceAsStream("invalidpythonhadoop.json"), Workflow.class);
        PythonHadoopJob myJob = (PythonHadoopJob) w.getWorkflow().get(0);
        String task = myJob.toCommandLine();
    }
}
