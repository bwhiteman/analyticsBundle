package com.potomacfusion.asfframework.jobs;

/**
 * User: bwhiteman
 * Date: 3/15/12
 * Time: 2:09 PM
 */
public class OozieStreamingJob implements Job {

    public String getTask() {
        return "oozie job -oozie http://localhost:11000/oozie -config wordcount/job.properties -run";

    }
}
