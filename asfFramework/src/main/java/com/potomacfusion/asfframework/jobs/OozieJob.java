package com.potomacfusion.asfframework.jobs;

/**
 * User: bwhiteman
 * Date: 3/14/12
 * Time: 2:35 PM
 */
public class OozieJob implements Job {

    public String getTask() {
        return "oozie job -oozie http://localhost:11000/oozie -config wordcount/job.properties -run";
    }
}
