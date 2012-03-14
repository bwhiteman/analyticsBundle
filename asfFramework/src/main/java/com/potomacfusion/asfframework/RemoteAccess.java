package com.potomacfusion.asfframework;

import com.potomacfusion.asfframework.exceptions.InvalidXMLException;
import com.potomacfusion.asfframework.jobs.Job;
import com.potomacfusion.asfframework.jobs.PythonHadoopJob;
import com.potomacfusion.asfframework.jobs.PythonJob;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.w3c.dom.Element;

public class RemoteAccess {

    // ##START BLOCK FOR ADDING ADDITIONAL JOB TYPES ##
    private static enum JOBS {PYTHON, PYTHON_HADOOP};
    
    private static String getCallToServer(Document doc) throws InvalidXMLException{
        
        // Determine which Job subclass to invoke
        String analyticLanguage = ((Element) doc.getElementsByTagName("analytic").item(0)).getAttribute("language");
        
        Job myJob = null;
        
        // For each job in the enum above, link the text to a specific Job subclass
        switch(JOBS.valueOf(analyticLanguage.toUpperCase())){
            case PYTHON:
                myJob = new PythonJob(doc);
                break;
            case PYTHON_HADOOP:
                myJob = new PythonHadoopJob(doc);
                break;
        }
        
        if (myJob == null){
            throw new InvalidXMLException();
        }
        return myJob.getTask();
    }
    
    // ##END BLOCK FOR ADDING ADDITIONAL JOB TYPES ##

    public static void main(String[] args) throws IOException {
        
        // -d local <source> <target>
        if (args.length == 4 && args[0].equalsIgnoreCase("-d") && args[1].equalsIgnoreCase("local")) {
            deployLocalResource(args[2], args[3]);
        } 
        
        // -d hdfs <filename> <target>
        else if (args.length == 4 && args[0].equalsIgnoreCase("-d") && args[1].equalsIgnoreCase("hdfs")){
            deployResourceToHDFS(args[2], args[3]);
        }
        
        // -x <context>
        else if (args.length == 2 && args[0].equalsIgnoreCase("-x")) {
            execute(args[1]);
        }

    }
    
    private static void deployLocalResource(String source, String target) throws IOException{
        SSHClient ssh = new SSHClient();
        try{
            // overhead connection stuff for ssh
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);
            
            // upload data            
            ssh.newSCPFileTransfer().upload(source, Configurations.ANALYTIC_ROOT + target);
                       
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
            String dest = Configurations.ANALYTIC_ROOT + "tmp/" + source;
            ssh.newSCPFileTransfer().upload(source, dest);
            String task = Configurations.HADOOP + " dfs -copyFromLocal " + dest + " " + hdfsPath; 

            
            // TODO: clean up the temp file
            final Session session = ssh.startSession();
            try{                
                final Command cmd = session.exec(task);
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
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
    
    private static void execute(String contextPath) throws IOException {
        SSHClient ssh = new SSHClient();

        try {
            // overhead conection stuff for xml
            File fXmlFile = new File(contextPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Connect, ignore host key verification
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.HOST_NAME, 22);
            ssh.authPassword(Configurations.USER_NAME, Configurations.PASSWORD);

            String task = getCallToServer(doc);
            final Session session = ssh.startSession();
            try{                
                System.out.println("Invoking: " + task);
                final Command cmd = session.exec(task);
                System.out.println("\nOutput\n------");
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
            }
            finally{
                session.close();
            }

            // TODO: handle success/failure, assuming success for now


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    }
    

}