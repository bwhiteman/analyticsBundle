/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.jobs.impl.OozieStreamingJob;
import com.potomacfusion.asfframework.jobs.impl.OozieJob;
import com.potomacfusion.asfframework.jobs.impl.PythonJob;
import com.potomacfusion.asfframework.jobs.impl.PythonHadoopJob;
import java.util.List;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

/**
 *
 * @author SPines
 */

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="language")
@JsonSubTypes({
        @Type(name="python", value=PythonJob.class),
        @Type(name="python_hadoop", value=PythonHadoopJob.class),
        @Type(name="oozie", value=OozieJob.class),
        @Type(name="oozie_streaming", value=OozieStreamingJob.class)
})
public abstract class Job {
    private String name;
    private String language;
    private List<Input> inputs;
    private List<Output> outputs;
    private List<Resource> resources;     
    
    public Job(){}
    
    public Job(String name, String language, List<Input> inputs, List<Output> outputs, List<Resource> resources) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.resources = resources;
        this.name = name;
        this.language = language;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
    
    public abstract String toCommandLine();
}
