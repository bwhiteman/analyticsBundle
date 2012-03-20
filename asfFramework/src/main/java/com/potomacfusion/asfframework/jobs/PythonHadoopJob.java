/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.util.List;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author SPines
 */
public class PythonHadoopJob extends Job{

    //./hadoop jar ../contrib/streaming/hadoop-streaming-0.20.2-cdh3u3.jar -file 
    // /home/hduser/mapper.py -mapper /home/hduser/mapper.py -file /home/hduser/reducer.py 
    // -reducer /home/hduser/reducer.py -input /user/hduser/gutenberg/* -output /user/hduser/gutenberg-output
    private String mapper = null;
    private String reducer = null;
    private String input = null;
    private String output = null;
    private String mapperFile = null;
    private String reducerFile = null;
     
    private enum ATTRIBUTES {MAPPER, MAPPERFILE, REDUCER, REDUCERFILE, INPUT, OUTPUT};
    
    public PythonHadoopJob(){}
    
    public PythonHadoopJob(String name, String language, List<Input> inputs, List<Output> outputs, List<Resource> resources) throws InvalidXMLException{
        super(name, language, inputs, outputs, resources);             
    }    
    public String toCommandLine(){          
        for (Input e : this.getInputs()){
            switch(ATTRIBUTES.valueOf(e.getName().toUpperCase())){
                case MAPPER:
                    this.mapper = e.getValue();
                    break;
                case MAPPERFILE:
                    this.mapperFile = e.getValue();
                    break;
                case REDUCER:
                    this.reducer = e.getValue();
                    break;
                case REDUCERFILE:
                    this.reducerFile = e.getValue();
                    break;
                case INPUT:
                    this.input = e.getValue();
                    break;
                case OUTPUT:
                    this.output = e.getValue();
                    break;
            }            
        }

        return Configurations.getProperty("HADOOP") + " jar " +
               Configurations.getProperty("HADOOP_STREAMING_JAR") + " " +
               "-file " + mapperFile + " -mapper " + mapper + " " +
               "-file " + reducerFile + " -reducer " + reducer + " " +
               "-input " + input + " -output " + output;
    }
}
