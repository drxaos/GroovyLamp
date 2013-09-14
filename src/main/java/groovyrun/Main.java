package groovyrun;

import java.util.Map;

public class Main extends CommandLineTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Map<String, String> arguments = parseArguments(args);

        String[] required = {"port"};

        if (checkRequiredArguments(required, arguments)) {
            boolean template_mode = true;

            if (arguments.containsKey("mode") && (arguments.get("mode").equals("script"))) {
                template_mode = false;
            }

            SCGIApplicationServer app_server = new SCGIApplicationServer(Integer.parseInt(arguments.get("port")), template_mode);
            app_server.start();

        } else {

            printUsageAndExit();

        }

    }

    public static void printUsageAndExit() {

        System.err.println("Usage:");
        System.err.println("------");

        System.err.println("java -jar groovyscgi.jar -port=xxxx -mode=[template/script]");
        System.err.println();

        System.exit(1);

    }

}
