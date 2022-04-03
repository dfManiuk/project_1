package man.by;


import java.io.*;

public class HttpResponse {

    private static final String WEB_SOCKET = "websocket";


    public boolean HttpResponseChecker(HttpRequest httpRequest) throws IOException {

        switch (httpRequest.getMethod()){
            case HEAD:
                System.out.println("Head");
                break;
            case GET:
                Handshake handshake = new Handshake();
                handshake.handshakeRequest(httpRequest );

                System.out.println("Handshake complete " + handshake);
                return true;

            case UNRECOGNIZED:
                System.out.println("UNRECOGNIZED");
                break;

        }
        return false;
    }
}
