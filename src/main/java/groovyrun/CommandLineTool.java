package groovyrun;

import java.util.HashMap;
import java.util.Map;

public class CommandLineTool {


    public static Map<String, String> parseArguments(String[] args) {

        Map<String, String> arguments = new HashMap<String, String>();

        for (String arg1 : args) {

            String arg = arg1;

            if (arg.startsWith("-")) {

                // Trim '-' symbol
                arg = arg.substring(1);

                // Split on '='
                String[] parts = arg.split("=");

                String key = parts[0];

                String value = (parts.length == 2) ? parts[1] : "1";

                arguments.put(key, value);

            }

        }

        return arguments;
    }


    public static boolean checkRequiredArguments(String[] required, Map arguments) {

        boolean ret = true;

        for (String aRequired : required) {

            ret = arguments.containsKey(aRequired);

        }

        return ret;
    }

    public static void printUsageAndExit() {

        System.err.println("Required command-line arguments not given.");

        System.exit(1);

    }

}
