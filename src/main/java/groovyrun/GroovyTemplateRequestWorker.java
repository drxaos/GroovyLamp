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
            String filename = request.env.get("SCRIPT_FILENAME");
            if (filename == null) {
                filename = request.env.get("DOCUMENT_URI");
                if (filename == null || "/".equals(filename)) {
                    filename = "/index.gsp";
                }
                filename = request.env.get("DOCUMENT_ROOT") + filename;
            }
            File file = new File(filename);
            PrintWriter writer = response.getOutputWriter();

            if (!file.exists()) {
                writer.println("The file \'" + file.getAbsolutePath() + "\' does not exist.");
                writer.flush();
                response.setStatus(404);
                writer.close();
            }

            FileReader reader = new FileReader(file);

            if (filename.endsWith(".gsp")) {
                HashMap<String, Object> binding = new HashMap<String, Object>();
                binding.put("request", request);
                binding.put("response", response);

                eng.createTemplate(reader).make(binding).writeTo(writer);
            } else {
                char[] buffer = new char[1024];
                int count = 0;
                int n = 0;
                while (-1 != (n = reader.read(buffer))) {
                    writer.write(buffer, 0, n);
                    count += n;
                }
            }
            response.setStatus(200);
            writer.flush();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }

    }

}
