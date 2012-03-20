package com.potomacfusion.asfframework;

import com.potomacfusion.asfframework.jobs.*;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import java.io.File;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

public class RemoteAccess {
    
    public static void main(String[] args) throws IOException {
        
        // java -jar framework.jar context.xml remote.properties
        if (args.length == 2) {
            Configurations.init(args[1]);
            execute(args[0]);
        }

    }
    
    private static void execute(String contextPath) throws IOException {
        SSHClient ssh = new SSHClient();

        try {
            
            // Connect, ignore host key verification
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(Configurations.getProperty("HOST_NAME"), 22);
            ssh.authPassword(Configurations.getProperty("USER_NAME"), Configurations.getProperty("PASSWORD"));
           
            // deserialize the data
            ObjectMapper om = new ObjectMapper();
            Workflow workflow = om.readValue(new File(contextPath), Workflow.class);        

            // Loop over the workflow      
            for (Job j : workflow.getWorkflow()){
                
                //Deploy resources if necessary
                ResourceManager.deployResources(j.getResources());

                // Execute the task
                String task = j.toCommandLine();
                final Session session = ssh.startSession();
                try{                
                    System.out.println("Invoking: " + task);
                    String dir = "cd " + Configurations.getProperty("USER_HOME");
                    final Command cmd = session.exec(dir + ";" + task);
                    System.out.println("\nOutput\n------");
                    System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
                }
                finally{
                    session.close();
                }

                // After the task has completed, pull the results back
                ResourceManager.getOutputs(j.getOutputs());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ssh != null) {
                ssh.disconnect();
            }
        }
    } 
}