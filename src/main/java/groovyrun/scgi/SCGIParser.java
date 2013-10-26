package groovyrun.scgi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;

public class SCGIParser {

    public static class SCGIException extends IOException {

        private static final long serialVersionUID = 1L;

        public SCGIException(String message) {
            super(message);
        }
    }

    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static HashMap<String, String> parse(InputStream input) throws IOException {

        StringBuilder lengthString = new StringBuilder(12);

        String headers = "";

        for (; ; ) {

            char ch = (char) input.read();

            if (ch >= '0' && ch <= '9') {

                lengthString.append(ch);

            } else if (ch == ':') {

                int length = Integer.parseInt(lengthString.toString());
                byte[] headersBuf = new byte[length];
                int read = input.read(headersBuf);

                if (read != headersBuf.length)
                    throw new SCGIException("Couldn't read all the headers (" + length + ").");

                headers = ISO_8859_1.decode(ByteBuffer.wrap(headersBuf)).toString();

                if (input.read() != ',') throw new SCGIException("Wrong SCGI header length: " + lengthString);

                break;

            } else {

                lengthString.append(ch);
                throw new SCGIException("Wrong SCGI header length: " + lengthString);

            }
        }

        HashMap<String, String> env = new HashMap<String, String>();

        while (headers.length() != 0) {

            int sep1 = headers.indexOf(0);
            int sep2 = headers.indexOf(0, sep1 + 1);
            env.put(headers.substring(0, sep1), headers.substring(sep1 + 1, sep2));
            headers = headers.substring(sep2 + 1);

        }

        return env;
    }

}
