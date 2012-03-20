/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potomacfusion.asfframework;

import com.potomacfusion.asfframework.jobs.Output;
import com.potomacfusion.asfframework.jobs.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    public static void deployResources(List<Resource> resources) throws IOException{
        for (Resource r : resources){
            String source = r.getSource();
            String target = r.getTarget();
            if (r.getLocation().equalsIgnoreCase("local")){
                deployLocalResource(source, target);
            }
            else if (r.getLocation().equalsIgnoreCase("hdfs")){
                deployResourceToHDFS(source, target);
            }
        }
    }
    
        
    public static void getOutputs(List<Output> outputs) throws Exception{
        for (Output o : outputs){
            String source = o.getSource();
            String target = o.getTarget();
            if (o.getLocation().equalsIgnoreCase("local")){
                getRemoteResource(source, target);
            }
            else if (o.getLocation().equalsIgnoreCase("hdfs")){
                getRemoteResourceFromHDFS(source, target);
            }
        }
    }
    
    // TEST ONLY - testing branching logic
//    protected static Map<String, Integer> deployResourcesTest(Element doc) throws IOException{
//        NodeList resources = doc.getElementsByTagName("resource");
//        Map<String, Integer> ret = new HashMap<String, Integer>();
//        ret.put("local", 0);
//        ret.put("hdfs", 0);
//        for (int i = 0; i < resources.getLength(); i++){
//            Element e = (Element)resources.item(i);
//            String source = e.getAttribute("source");
//            String target = e.getAttribute("target");
//            if (e.getAttribute("location").equalsIgnoreCase("local")){
//                // We would be calling deployLocalResource
//                ret.put("local", ret.get("local") + 1);
//            }
//            else if (e.getAttribute("location").equalsIgnoreCase("hdfs")){
//               // We would be calling deployResourceToHDFS
//                ret.put("hdfs", ret.get("hdfs") + 1);
//            }
//        }
//        return ret;
//    }

    private static void deployLocalResource(String source, String target) throws IOException{
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.getProperty("HOST_NAME"));
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));

            // upload data            
            System.out.println("Deploying " + source);
            ssh.newSCPFileTransfer().newSCPUploadClient().copy(new FileSystemFile(source), Configurations.getProperty("USER_HOME") + target);           
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
            ssh.connect(Configurations.getProperty("HOST_NAME"));
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));
            
            // deploly to hdfs
            String dest = Configurations.getProperty("USER_TMP") + source;
            System.out.println("Deploying " + source);
            ssh.newSCPFileTransfer().upload(source, dest);
            System.out.println(source + " successfully deployed to " + dest);
            String task = Configurations.getProperty("HADOOP") + " dfs -copyFromLocal " + dest + " " + hdfsPath; 
            
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
            ssh.connect(Configurations.getProperty("HOST_NAME"));
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));

            ssh.newSCPFileTransfer().download(Configurations.getProperty("USER_HOME") + remotePath, localPath);
        
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
            ssh.connect(Configurations.getProperty("HOST_NAME"));
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));
            
            String task = "rm -rf " + Configurations.getProperty("USER_TMP") + "*"; 
            
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
            ssh.connect(Configurations.getProperty("HOST_NAME"));
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));
            
            // Copy the remote file from HDFS to remote local
            String task = "cd " + Configurations.getProperty("USER_HOME")+ " ; " + Configurations.getProperty("HADOOP") + " dfs -copyToLocal " + hdfsPath + " tmp/"; 
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
            ssh.newSCPFileTransfer().download(Configurations.getProperty("USER_TMP"), localPath);            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
}
 