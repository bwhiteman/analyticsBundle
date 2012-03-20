/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.jobs.impl.PythonJob;
import com.potomacfusion.asfframework.jobs.impl.PythonHadoopJob;
import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author SPines
 */
public class Workflow {
    
    private List<Job> workflow;

    public Workflow(){}
    
    public Workflow(List<Job> workflow) {
        this.workflow = workflow;
    }

    public List<Job> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<Job> workflow) {
        this.workflow = workflow;
    }
    
    public static void main(String args[]) throws IOException, InvalidXMLException{
        Workflow sample = new Workflow(new ArrayList<Job>());
        
        List<Input> inputs = new ArrayList<Input>();
        inputs.add(new Input("mapper", "mapper.py"));
        inputs.add(new Input("mapperFile", "mapper.py"));
        inputs.add(new Input("reducer", "reducer.py"));
        inputs.add(new Input("reducerFile", "reducer.py"));
        inputs.add(new Input("input", "/tmp/txt/*"));
        inputs.add(new Input("output", "/tmp/out/"));
        
        List<Output> outputs = new ArrayList<Output>();
        outputs.add(new Output("/tmp/out/*", ".", "hdfs"));
        
        List<Resource> resources = new ArrayList<Resource>();
        resources.add(new Resource("mapper.py", "mapper.py", "local"));
        resources.add(new Resource("reducer.py", "reducer.py", "local"));
        resources.add(new Resource("pg4300.txt", "/tmp/txt/pg4300.txt", "hdfs"));
        resources.add(new Resource("pg5000.txt", "/tmp/txt/pg5000.txt", "hdfs"));
        resources.add(new Resource("pg20417.txt", "/tmp/txt/pg20417.txt", "hdfs"));
        
        Job wordCount = new PythonHadoopJob("wordCount", "python_hadoop", inputs, outputs, resources);
        
        List<Input> inputs2 = new ArrayList<Input>();
        inputs2.add(new Input("fileName", "tmp/part-00000"));
        inputs2.add(new Input("threshold", "1000"));
        
        List<Output> outputs2 = new ArrayList<Output>();
        outputs2.add(new Output("results.out", ".", "local"));
        
        List<Resource> resources2 = new ArrayList<Resource>();
        resources2.add(new Resource("Filter.py", "Filter.py", "local"));
        
        Job filter = new PythonJob("Filter", "python", inputs2, outputs2, resources2);
        
        sample.getWorkflow().add(wordCount);
        sample.getWorkflow().add(filter);
        
        ObjectMapper om = new ObjectMapper();     
        String s = om.writeValueAsString(sample);
        
        Workflow tmp = om.readValue(s, Workflow.class);
        System.out.println(s);
    }

}
