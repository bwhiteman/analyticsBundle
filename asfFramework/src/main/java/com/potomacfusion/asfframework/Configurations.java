/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author SPines
 */
public class Configurations {
    
    private static final Properties props = new Properties();
    
    public static void init(String propertiesFile) throws IOException{
        FileInputStream input = new FileInputStream(propertiesFile);
        props.load(input);
        input.close();
    }
    
    public static String getProperty(String key){
        return props.getProperty(key);
    }
}
