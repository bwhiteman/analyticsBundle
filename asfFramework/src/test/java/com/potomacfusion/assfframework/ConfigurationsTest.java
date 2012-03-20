/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.assfframework;

import com.potomacfusion.asfframework.Configurations;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class ConfigurationsTest {
    
    public ConfigurationsTest() {
    }
    
    public static final String HOST_NAME = "gmdev";
    public static final String USER_NAME = "root";
    public static final String CERT_LOCATION = "/users/bwhiteman/.ssh/hduser.priv";
    public static final String PASSWORD = "ghostmachine20";
    
    // System Paths
    public static final String HADOOP = "/srv/hadoop/bin/hadoop";
    public static final String HADOOP_STREAMING_JAR = "/srv/hadoop/contrib/streaming/hadoop-0.20.2-streaming.jar";
    public static final String PYTHON_PATH = "python";
    public static final String USER_HOME = "/home/"+USER_NAME+"/";
    public static final String USER_TMP = USER_HOME + "tmp/";
    
    @Test
    public void testLoadProperties() throws IOException{
        Configurations.init("src/main/resources/remote.properties");
        assertEquals(Configurations.getProperty("HOST_NAME"), HOST_NAME);
        assertEquals(Configurations.getProperty("USER_NAME"), USER_NAME);
        assertEquals(Configurations.getProperty("CERT_LOCATION"), CERT_LOCATION);
        assertEquals(Configurations.getProperty("PASSWORD"), PASSWORD);
        assertEquals(Configurations.getProperty("HADOOP"), HADOOP);
        assertEquals(Configurations.getProperty("HADOOP_STREAMING_JAR"), HADOOP_STREAMING_JAR);
        assertEquals(Configurations.getProperty("PYTHON_PATH"), PYTHON_PATH);
        assertEquals(Configurations.getProperty("USER_HOME"), USER_HOME);
        assertEquals(Configurations.getProperty("USER_TMP"), USER_TMP);
    }
}
