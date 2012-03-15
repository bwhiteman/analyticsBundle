package com.potomacfusion.asfframework.jobs;

import org.w3c.dom.Document;

/**
 * User: bwhiteman
 * Date: 3/15/12
 * Time: 2:09 PM
 */
public class OozieStreamingJob implements Job {

    public OozieStreamingJob(Document doc) {
    }

    public String getTask() {
        return "oozie job -oozie http://localhost:11000/oozie -config wordcount/job.properties -run";

    }
}
