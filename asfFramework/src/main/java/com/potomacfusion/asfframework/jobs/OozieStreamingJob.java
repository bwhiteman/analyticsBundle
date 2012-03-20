package com.potomacfusion.asfframework.jobs;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * User: bwhiteman
 * Date: 3/15/12
 * Time: 2:09 PM
 */
public class OozieStreamingJob extends Job {

    public OozieStreamingJob(){}
    
    public OozieStreamingJob(String name, String language, List<Input> inputs, List<Output> outputs, List<Resource> resources){
        super(name, language, inputs, outputs, resources);
    }

    public String toCommandLine() {
        return "oozie job -oozie http://localhost:11000/oozie -config wordcount/job.properties -run";

    }
}
