/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework.jobs;

/**
 *
 * @author SPines
 */
public class Resource {
    private String source;
    private String target;
    private String location;
    
    public Resource(){}

    public Resource(String source, String target, String location) {
        this.source = source;
        this.target = target;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }  
}
