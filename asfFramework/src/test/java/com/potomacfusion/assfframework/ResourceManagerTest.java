/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.assfframework;

import org.w3c.dom.Element;
import com.potomacfusion.asfframework.ResourceManager;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

/**
 *
 * @author SPines
 */
public class ResourceManagerTest extends ResourceManager {

//    @Test
//    public void testResourceBranchingLogic() {
//        try {
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document validPythonHadoop = dBuilder.parse(ClassLoader.getSystemResourceAsStream("validpythonhadoop.xml"));
//            validPythonHadoop.getDocumentElement().normalize();
//            Element e = (Element)validPythonHadoop.getChildNodes().item(0);
//            Map<String, Integer> temp = ResourceManagerTest.deployResourcesTest(e);
//            assertEquals(new Integer(2), temp.get("local"));
//            assertEquals(new Integer(3), temp.get("hdfs"));
//        } catch (Exception e) {
//            fail();
//        }
//    }
}
