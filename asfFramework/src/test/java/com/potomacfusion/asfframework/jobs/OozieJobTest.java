package com.potomacfusion.asfframework.jobs;

import org.junit.Test;
import static org.junit.Assert.*;

public class OozieJobTest {

    @Test
    public void test_getTask() {
        OozieJob job = new OozieJob();
        assertEquals("oozie job -oozie http://localhost:11000/oozie -config wordcount/job.properties -run", job.getTask());


    }
}