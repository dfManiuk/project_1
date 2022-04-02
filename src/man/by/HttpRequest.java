package man.by;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private List<String> headers = new ArrayList<String>();
    Map<String, String> headerFields = new HashMap<String,String>();
    private Method method;
    private Socket socket;

    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        InputStream is = socket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        System.out.println(reader.toString());

        String str = reader.readLine();

        parseRequestLine(str);

        while (!str.equals("")) {
            str = reader.readLine();
            parseRequestHeader(str);
        }



    }

    private void parseRequestLine(String str) {

        String[] split = str.split("\\s+");
        try {
            method = Method.valueOf(split[0]);
            headers.add(method.toString());
        } catch (Exception e) {
            method = Method.UNRECOGNIZED;
        }
        headers.add(split[1]);
        headers.add(split[2]);
    }

    private void parseRequestHeader(String str) {
        headers.add(str);

        String[] tokens = str.split(":", 2);
        if (tokens.length == 2) {
            headerFields.put(tokens[0].trim(), tokens[1].trim());
        }
    }

    public Method getMethod() {
        return method;
    }

    public List<String> getHeaders(){
        return headers;
    }

    public Socket getSocket(){
        return socket;
    }

    public Map<String, String> getHeaderFields(){
        return headerFields;
    }


    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("");
        for (String header : headers) {
            string.append(header);
        }
        return string.toString();
    }
}
