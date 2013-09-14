/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package groovyrun.http;

import java.util.HashMap;
import java.net.*;
import java.io.*;

/**
 *
 * @author abj
 */
public class HTTPRequest {

    public HashMap<String, String> env;
    public HashMap<String, String> get;
    public HashMap<String, String> post;
    public HashMap<String, String> cookies;
    
    private BufferedInputStream in;
    
    public HTTPRequest(HashMap<String, String> env, BufferedInputStream in)
    {
        this.in = in;
        this.env = env;
        
        //
        //  Parse GET request. Note that the QUERY STRING is parsed even if the REQEST METHOD is not GET
        //      
        try {
            
            this.get = this.decodeQueryString(env.get("QUERY_STRING"));
            
        } catch(UnsupportedEncodingException ex) {
            
            System.err.println("Could not parse QUERY STRING");
            ex.printStackTrace();
            
        }
        
        //
        //  Parse POST data. Only if REQUEST_METHOD = POST
        //  Only: application/x-www-form-urlencoded supported at the moment
        //
        if (env.containsKey("REQUEST_METHOD") && (env.get("REQUEST_METHOD").equals("POST")))
        {
            
            try {
                
                String content = this.getContent();
                
                this.post = this.parsePostDataUrlEncoded(content);

                System.out.println(this.post);
                
            } catch(UnsupportedEncodingException ex) {

                System.err.println("Could not parse POST DATA");
                ex.printStackTrace();

            } catch(Exception ex) {

                System.err.println("Could not parse POST DATA");
                ex.printStackTrace();

            }
            
        }
       
        //
        //  Parse the cookies supplied by the browser
        //
        try {
            
            this.parseCookies();
            System.out.println(this.cookies);
            
        } catch(UnsupportedEncodingException ex) {
            
            System.err.println("Could not parse COOKIES");
            ex.printStackTrace();
            
        }
        
    }
    
    /*
     *  Returns the IP address of the client 
     */
    public String getRemoteAddr()
    {
        return this.env.get("REMOTE_ADDR");
    }
    
    public String getHTTPMethod()
    {
        return this.env.get("REQUEST_METHOD");
    }
    
    /*
     *  Cookies section
     * 
     */
    public boolean hasCookie(String key)
    {
        return this.cookies.containsKey(key);
    }
    
    public String getCookie(String key)
    {
        return this.cookies.get(key);
    }
    
    private void parseCookies() throws UnsupportedEncodingException
    {
        
        this.cookies = new HashMap<String, String>();
        
        String cookie_string = this.env.get("HTTP_COOKIE");
        
        if (cookie_string == null) return;
        
        cookie_string = cookie_string.trim();
        String[] pairs = cookie_string.split("; ");
        
        if (cookie_string.equals("")) return;
        
        for (int i=0; i<pairs.length; i++) {
            String[] fields = pairs[i].split("=");
            String name = URLDecoder.decode(fields[0], "UTF-8");
            String value = URLDecoder.decode(fields[1], "UTF-8");
            this.cookies.put(name, value);
        }
        

    }
    
    private String getContent() throws IOException
    {
        int length = in.available();
        
        byte[] buffer = new byte[length];
        in.read(buffer);
        
        String content = new String(buffer);
        
        return content;
    }
    
    private static HashMap<String, String> decodeQueryString(String qs) throws UnsupportedEncodingException
    {
        qs = qs.trim();
        String[] pairs = qs.split("&");
        HashMap vars = new HashMap<String, String>();
        
        if (qs.equals("")) return vars;
        
        for (int i=0; i<pairs.length; i++) {
            String[] fields = pairs[i].split("=");
            String name = URLDecoder.decode(fields[0], "UTF-8");
            String value = URLDecoder.decode(fields[1], "UTF-8");
            vars.put(name, value);
        }
        
        return vars;
    }
    
    private static HashMap<String, String> parsePostDataUrlEncoded(String data) throws UnsupportedEncodingException
    {
        return decodeQueryString(data);
    }
    
}
