 /*
 * CommandLineTool.java
 *
 * Created on February 18, 2007, 10:10 PM
 *
 * Base class for java tools that can be run from command line. 
 * Provides basic command line argument parsing.
 *
 */

package groovyrun;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author alastairjames
 */
public class CommandLineTool {

    
    public static Map parseArguments(String[] args){
        
        Map<String, String> arguments = new HashMap();
        
        for (int i=0; i < args.length ; i++){
            
            String arg = args[i];
            
            if (arg.startsWith("-")){
                
                // Trim '-' symbol
                arg = arg.substring(1);
                
                // Split on '='
                String[] parts = arg.split("=");
                
                String key = parts[0];
                
                String value = (parts.length ==2) ? parts[1] : "1";
                
                arguments.put(key, value);
                
            }
            
        }
        
        return arguments;
    }
    
    
    public static boolean checkRequiredArguments(String[] required, Map arguments){
        
        boolean ret = true;
        
        for (int i=0; i < required.length; i++){
            
            ret = arguments.containsKey(required[i]) & ret;
            
        }
        
        return ret;
        
    }
    
    public static void printUseageAndExit(){
        
        System.err.println("Required command-line arguments not given.");
        
        System.exit(1);
    
    }
    
}
