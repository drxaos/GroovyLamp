package groovyrun;

import groovyrun.http.HTTPRequest;
import groovyrun.http.HTTPResponse;
import groovyrun.scgi.SCGIParser;

import java.io.*;
import java.util.HashMap;

public class RequestWorker implements Runnable {

    Thread thread;
    BufferedInputStream in;
    OutputStream out;
    long time_started;
    SCGIApplicationServer server;

    /**
     * Creates a new instance of RequestWorker
     */
    public RequestWorker(SCGIApplicationServer server, InputStream in, OutputStream out) {

        this.server = server;
        this.thread = new Thread(this);
        this.in = new BufferedInputStream(in, 4096);
        this.out = out;

    }

    public void start() {
        thread.start();
        time_started = System.currentTimeMillis();
    }

    public void run() {

        try {

            HashMap<String, String> env = SCGIParser.parse(in);
            HTTPRequest request = new HTTPRequest(env, in);
            HTTPResponse response = new HTTPResponse();

            // General catch block to catch all errors in user code
            try {

                work(request, response);

            } catch (Exception e) {

                this.server.getLog().error("ERROR: Exception in work method");
                e.printStackTrace();
                response.setStatus(500);
                response.clearBuffer();

            }

            PrintWriter pout = new PrintWriter(this.out);
            response.writeTo(pout);
            pout.close();

            long time = (System.currentTimeMillis() - time_started);
            float ftime = time / 1000.0f;
            this.server.getLog().notice("Time taken " + ftime + " seconds");

            in.close();
            out.close();
            this.server.getLog().notice("Worker done!");


        } catch (IOException e) {

            this.server.getLog().error("ERROR: IO Exception in worker class");
            e.printStackTrace();

        }

    }

    public void work(HTTPRequest request, HTTPResponse response) throws Exception {

    }

}
