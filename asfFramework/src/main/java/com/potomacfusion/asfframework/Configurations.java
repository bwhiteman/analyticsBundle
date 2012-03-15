/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework;

/**
 *
 * @author SPines
 */
public class Configurations {
    
    // System Login    
    public static final String HOST_NAME = "gmdev";
    public static final String USER_NAME = "hduser";
    public static final String CERT_LOCATION = "/users/bwhiteman/.ssh/hduser.priv";
    public static final String PASSWORD = "hadoop";
    
    // System Paths
    public static final String HADOOP = "/opt/hadoop/bin/hadoop";
    public static final String HADOOP_STREAMING_JAR = "/opt/hadoop/contrib/streaming/hadoop-0.20.2-streaming.jar";
    public static final String PYTHON_PATH = "python";
    public static final String ANALYTIC_ROOT = "/home/"+USER_NAME+"/";
}
