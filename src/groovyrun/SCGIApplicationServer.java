/*
 * ApplicationServer.java
 *
 * Created on September 6, 2007, 8:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package groovyrun;

import java.net.ServerSocket;
import java.net.Socket;

import groovyrun.logging.Log;

/**
 *
 * @author alastairjames
 */
public class SCGIApplicationServer implements Runnable {
    
    private boolean running;
    private ServerSocket server_socket;
    private int port;
    private Thread thread;
    private boolean template_mode;
    
    private Log log;
    
    /** Creates a new instance of ApplicationServer */
    public SCGIApplicationServer(int port, boolean template_mode) {
        
        this.port = port;
        this.template_mode = template_mode;
        this.thread = new Thread(this);
        
        this.log = new Log();
        
    }
    
    public void run()
    {
        this.log.notice("Starting server");
        
        try{
        
            openServerSocket();
        
        } catch (Exception e) {
            
            
            this.log.error("Unable to open server socket.");
            running = false;
            //throw e;
            return;
            
        }
        
        this.log.notice("SCGIApplicationServer running on port: "+server_socket.getLocalPort());
        
        while(running)
        {
            try 
            {
            
                Socket client_socket = server_socket.accept();

                this.log.notice("Accepted new connection");
                
                if (this.template_mode)
                    (new GroovyTemplateRequestWorker(this, client_socket.getInputStream(), client_socket.getOutputStream())).start();
                else
                    (new GroovyScriptRequestWorker(this, client_socket.getInputStream(), client_socket.getOutputStream())).start();
                    
                this.log.notice("Dispatched");
                
            } catch (Exception e) {
                
                this.log.error("ERROR: in accept loop.");
                
            }
        }
        
        try{
        
            closeServerSocket();
        
        } catch (Exception e) {
            
            this.log.error("Unable to close server socket");
            //throw e;
            return;
            
        }
           
    }
    
    public void start(){
        
        this.running = true;
        
        this.thread.start();
        
    }
    
    public void stop(){
        
        this.running = false;
        
    }
    
    public Log getLog()
    {
        return this.log;
    }
    
    private void openServerSocket() throws Exception
    {
    
        server_socket = new ServerSocket(port);
    
    }
    
    private void closeServerSocket() throws Exception
    {
    
        server_socket.close();
    
    }
    
}
