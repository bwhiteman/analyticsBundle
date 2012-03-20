/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs.impl;

import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.jobs.Input;
import com.potomacfusion.asfframework.jobs.Job;
import com.potomacfusion.asfframework.jobs.Output;
import com.potomacfusion.asfframework.jobs.Resource;
import java.util.List;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author SPines
 */
public class PythonJob extends Job {

    public PythonJob(){}
    
    public PythonJob(String name, String language, List<Input> inputs, List<Output> outputs, List<Resource> resources){
        super(name, language, inputs, outputs, resources);
    }
    
    // python file.py param1 param2 param3
    public String toCommandLine() {
        String ret = Configurations.getProperty("PYTHON_PATH") + " " + this.getName() + ".py";
        for (Input s : this.getInputs()){
            ret += " " + s.getValue();
        }
        return ret;
    }
}
