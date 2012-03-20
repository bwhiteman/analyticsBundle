/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.assfframework;

import com.potomacfusion.asfframework.jobs.Resource;
import java.util.List;
import com.potomacfusion.asfframework.jobs.Workflow;
import org.codehaus.jackson.map.ObjectMapper;
import com.potomacfusion.asfframework.Configurations;
import com.potomacfusion.asfframework.ResourceManager;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class ResourceManagerTest extends ResourceManager {

    @Test
    public void testResourceBranchingLogic() {
        try {
            Configurations.init("src/main/resources/remote.properties");
            ObjectMapper om = new ObjectMapper();
            Workflow w = om.readValue(ClassLoader.getSystemResourceAsStream("validpythonhadoop.json"), Workflow.class);
            List<Resource> r = w.getWorkflow().get(0).getResources(); 
            Map<String, Integer> temp = ResourceManagerTest.deployResourcesTest(r);
            assertEquals(new Integer(2), temp.get("local"));
            assertEquals(new Integer(3), temp.get("hdfs"));
        } catch (Exception e) {
            fail();
        }
    }
}
