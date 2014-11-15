package groovyrun;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovyrun.http.HTTPRequest;
import groovyrun.http.HTTPResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GroovyScriptRequestWorker extends RequestWorker {

    GroovyScriptEngine gse;

    public GroovyScriptRequestWorker(SCGIApplicationServer server, InputStream in, OutputStream out) {
        super(server, in, out);

        try {
            String[] roots = new String[]{"/"};
            gse = new GroovyScriptEngine(roots);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void work(HTTPRequest request, HTTPResponse response) throws Exception {
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
