/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author SPines
 */
public class ResourceManager {

    public static void deployResources(Document doc) throws IOException{
        NodeList resources = doc.getElementsByTagName("resource");
        for (int i = 0; i < resources.getLength(); i++){
            Element e = (Element)resources.item(i);
            String source = e.getAttribute("source");
            String target = e.getAttribute("target");
            if (e.getAttribute("location").equalsIgnoreCase("local")){
                deployLocalResource(source, target);
            }
            else if (e.getAttribute("location").equalsIgnoreCase("hdfs")){
                deployResourceToHDFS(source, target);
            }
        }
    }
    
        
    public static void getOutputs(Document doc) throws Exception{
        NodeList outputs = doc.getElementsByTagName("output");
        for (int i = 0; i < outputs.getLength(); i++){
            Element e = (Element)outputs.item(i);
            if (e.getAttribute("location").equalsIgnoreCase("local")){
                getRemoteResource(e.getAttribute("name"), e.getAttribute("name"));
            }
            else if (e.getAttribute("location").equalsIgnoreCase("hdfs")){
                getRemoteResourceFromHDFS(e.getAttribute("source"), e.getAttribute("target"));
            }
        }
    }
    
    // TEST ONLY - testing branching logic
    protected static Map<String, Integer> deployResourcesTest(Document doc) throws IOException{
        NodeList resources = doc.getElementsByTagName("resource");
        Map<String, Integer> ret = new HashMap<String, Integer>();
        ret.put("local", 0);
        ret.put("hdfs", 0);
        for (int i = 0; i < resources.getLength(); i++){
            Element e = (Element)resources.item(i);
            String source = e.getAttribute("source");
            String target = e.getAttribute("target");
            if (e.getAttribute("location").equalsIgnoreCase("local")){
                // We would be calling deployLocalResource
                ret.put("local", ret.get("local") + 1);
            }
            else if (e.getAttribute("location").equalsIgnoreCase("hdfs")){
               // We would be calling deployResourceToHDFS
                ret.put("hdfs", ret.get("hdfs") + 1);
            }
        }
        return ret;
    }

    private static void deployLocalResource(String source, String target) throws IOException{
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);

            // upload data            
            System.out.println("Deploying " + source);
            ssh.newSCPFileTransfer().newSCPUploadClient().copy(new FileSystemFile(source), Configurations.USER_HOME + target);           
            System.out.println(source + " successfully deployed to " + target);

        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
    
    private static void deployResourceToHDFS(String source, String hdfsPath) throws IOException{
       
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);
            
            // deploly to hdfs
            String dest = Configurations.USER_TMP + source;
            System.out.println("Deploying " + source);
            ssh.newSCPFileTransfer().upload(source, dest);
            System.out.println(source + " successfully deployed to " + dest);
            String task = Configurations.HADOOP + " dfs -copyFromLocal " + dest + " " + hdfsPath; 
            
            // TODO: clean up the temp file
            final Session session = ssh.startSession();
            try{                
                System.out.println("Copying " + source + " to HDFS.");
                final Command cmd = session.exec(task);
                System.out.println(source + " copied to HDFS at " + hdfsPath);
            }
            finally{
                session.close();
            }
                    }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
    
    private static void getRemoteResource(String remotePath, String localPath) throws Exception{
        SSHClient ssh = new SSHClient();
        try {
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);

            ssh.newSCPFileTransfer().download(Configurations.USER_HOME + remotePath, localPath);
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
    
    private static void cleanRemoteTmp() throws IOException{
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);
            
            String task = "rm -rf " + Configurations.USER_TMP + "*"; 
            
            // TODO: clean up the temp file
            final Session session = ssh.startSession();
            try{                
                session.exec(task);
                System.out.println("Temp directory cleaned");
            }
            finally{
                session.close();
            }     
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
    
    private static void getRemoteResourceFromHDFS(String hdfsPath, String localPath) throws Exception{
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);
            
            // Copy the remote file from HDFS to remote local
            String task = "cd " + Configurations.USER_HOME+ " ; " + Configurations.HADOOP + " dfs -copyToLocal " + hdfsPath + " tmp/"; 
            System.out.println(task);
            
            final Session session = ssh.startSession();
            try{          
                cleanRemoteTmp();
                final Command cmd = session.exec(task);
            }
            finally{
                session.close();
            }
                      
            // Copy from remote system to our local system
            ssh.newSCPFileTransfer().download(Configurations.USER_TMP, localPath);            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
}
 