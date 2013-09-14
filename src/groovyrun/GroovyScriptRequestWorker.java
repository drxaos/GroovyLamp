/*
 * GroovyRequestWorker.java
 *
 * Created on September 6, 2007, 9:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package groovyrun;

import java.io.*;
import java.util.*;
import groovy.lang.Binding;
import groovy.util.*;
import groovy.text.*;

import groovyrun.http.*;


/**
 *
 * @author alastairjames
 */
public class GroovyScriptRequestWorker extends RequestWorker{
    
    GroovyScriptEngine gse;
    
    public GroovyScriptRequestWorker(SCGIApplicationServer server, InputStream in, OutputStream out) {
        
        super(server, in, out);
        
        try {
          
            String[] roots = new String[] { "/" };
            gse = new GroovyScriptEngine(roots);
        
        } catch (IOException e) {
        
            e.printStackTrace();
        
        }
        
    }

    
    public void work(HTTPRequest request, HTTPResponse response) throws Exception
    {
        try {

            Binding binding = new Binding();
            binding.setVariable("out", response.getOutputWriter());
            binding.setVariable("request", request);
            binding.setVariable("response", response);
            
            gse.run(request.env.get("SCRIPT_FILENAME"), binding);

            response.setStatus(200);
            
        } catch (ResourceException ex) {

            ex.printStackTrace();
            
            response.setStatus(404);
          
        } 
    }
    
}
