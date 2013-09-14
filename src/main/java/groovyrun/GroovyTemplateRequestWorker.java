package groovyrun;

import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import groovyrun.http.HTTPRequest;
import groovyrun.http.HTTPResponse;

import java.io.*;
import java.util.HashMap;

public class GroovyTemplateRequestWorker extends RequestWorker {

    TemplateEngine eng;

    public GroovyTemplateRequestWorker(SCGIApplicationServer server, InputStream in, OutputStream out) {

        super(server, in, out);

        eng = new SimpleTemplateEngine();

    }


    public void work(HTTPRequest request, HTTPResponse response) throws Exception {

        try {

            File file = new File(request.env.get("SCRIPT_FILENAME"));
            PrintWriter writer = response.getOutputWriter();

            if (!file.exists()) {
                writer.println("The file \'" + file.getAbsolutePath() + "\' does not exist.");
                writer.flush();
                response.setStatus(404);
                writer.close();
            }

            FileReader reader = new FileReader(file);

            HashMap<String, Object> binding = new HashMap<String, Object>();
            binding.put("request", request);
            binding.put("response", response);

            eng.createTemplate(reader).make(binding).writeTo(writer);

            response.setStatus(200);

        } catch (FileNotFoundException ex) {

            ex.printStackTrace();

            response.setStatus(404);

        }

    }

}
