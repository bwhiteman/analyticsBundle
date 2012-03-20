package com.potomacfusion.asfframework.jobs;

import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * User: bwhiteman
 * Date: 3/14/12
 * Time: 2:35 PM
 */
public class OozieJob extends Job {

    private String oozieServer;
    private String ooziePort;
    private String oozieURL;
    private String jobPropertiesLocation;

    private enum ATTRIBUTES {SERVER, OOZIE_PORT, OOZIE_URL, JOB_PROPERTIES};
    
    public OozieJob(){}

    public OozieJob(String name, String language, List<Input> inputs, List<Output> outputs, List<Resource> resources) throws InvalidXMLException{
        super(name, language, inputs, outputs, resources);
    }

    public String toCommandLine() {
        oozieURL = "oozie";
        for (Input e : this.getInputs()){
            switch(ATTRIBUTES.valueOf(e.getName().toUpperCase())){
                case SERVER:
                    this.oozieServer = e.getValue();
                    //LOGGER.debug("Server: {}", this.oozieServer);
                    break;
                case OOZIE_PORT:
                    this.ooziePort = e.getValue();
                    //LOGGER.debug("Port: {}", this.ooziePort);
                    break;
                case JOB_PROPERTIES:
                    this.jobPropertiesLocation = e.getValue();
                    //LOGGER.debug("Properties: {}", this.jobPropertiesLocation);
                    break;
                case OOZIE_URL:
                    this.oozieURL = e.getValue();
                    //LOGGER.debug("URL: {}", this.oozieURL);
                    break;
            }
        }
        return "oozie job -oozie http://" + oozieServer + ":" + ooziePort + "/" + oozieURL +" -config " + jobPropertiesLocation + "  -run";
    }
}
