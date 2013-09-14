package groovyrun.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

public class HTTPResponse {

    private int status = -1;
    private String content_type = "text/html";

    private HashMap<String, String> cookies;

    private StringWriter buffer;
    private PrintWriter buffer_writer;
    private String buffer_str;


    public HTTPResponse() {
        this.buffer = new StringWriter();
        this.buffer_writer = new PrintWriter(this.buffer);

        this.cookies = new HashMap<String, String>();
    }

    public PrintWriter getOutputWriter() {
        return this.buffer_writer;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContentType(String content_type) {
        this.content_type = content_type;
    }

    public void setCookie(String key, String value) {
        this.cookies.put(key, value);
    }

    private void writeHeaders(PrintWriter out) {
        out.println("Status: " + this.status);
        out.println("Content-type: " + this.content_type);
        out.println("Content-length: " + this.buffer_str.getBytes().length);

        // Deal with cookies
        if (!this.cookies.isEmpty()) {

            for (String key : this.cookies.keySet()) {
                String value = this.cookies.get(key);
                out.println("Set-Cookie: " + key + "=" + value);
            }

        }

        out.println();
        out.println();
    }

    public void writeTo(PrintWriter out) {
        this.buffer_str = this.buffer.toString();

        this.writeHeaders(out);

        out.println(this.buffer_str);
    }

    public void clearBuffer() {
        this.buffer = new StringWriter();
        this.buffer_writer = new PrintWriter(this.buffer);
    }

}
