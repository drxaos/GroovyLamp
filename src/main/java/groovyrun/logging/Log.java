package groovyrun.logging;

import java.util.Date;

public class Log {


    public void error(String message) {
        this.logMessage(message, "ERROR");
    }

    public void warning(String message) {
        this.logMessage(message, "WARN");
    }

    public void notice(String message) {
        this.logMessage(message, "NOTICE");
    }


    private void logMessage(String message, String type) {
        Date now = new Date();

        System.err.println(now + " : " + type + " : " + message);
    }


}
