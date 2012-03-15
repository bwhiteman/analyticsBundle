package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * User: bwhiteman
 * Date: 3/14/12
 * Time: 2:35 PM
 */
public class OozieJob implements Job {

    private String oozieServer;
    private String ooziePort;
    private String oozieURL = "oozie";
    private String jobPropertiesLocation;

    private enum ATTRIBUTES {SERVER, OOZIE_PORT, OOZIE_URL, JOB_PROPERTIES};

    public OozieJob(Document doc) throws InvalidXMLException {

            NodeList n = doc.getElementsByTagName("input");

            for (int i = 0; i < n.getLength(); i++){
                Element e = (Element) n.item(i);
                System.out.println("Attribute: " + e.getAttribute("name"));
                //LOGGER.debug("Attribute: {}", e.getAttribute("name"));
                switch(ATTRIBUTES.valueOf(e.getAttribute("name").toUpperCase())){
                    case SERVER:
                        this.oozieServer = e.getAttribute("value");
                        //LOGGER.debug("Server: {}", this.oozieServer);
                        break;
                    case OOZIE_PORT:
                        this.ooziePort = e.getAttribute("value");
                        //LOGGER.debug("Port: {}", this.ooziePort);
                        break;
                    case JOB_PROPERTIES:
                        this.jobPropertiesLocation = e.getAttribute("value");
                        //LOGGER.debug("Properties: {}", this.jobPropertiesLocation);
                        break;
                    case OOZIE_URL:
                        this.oozieURL = e.getAttribute("value");
                        //LOGGER.debug("URL: {}", this.oozieURL);
                        break;
                }
            }

            if (oozieServer == null || ooziePort == null || jobPropertiesLocation == null){
                throw new InvalidXMLException();
            }
        }

    public String getTask() {
        return "oozie job -oozie http://" + oozieServer + ":" + ooziePort + "/" + oozieURL +" -config " + jobPropertiesLocation + "  -run";
    }
}
