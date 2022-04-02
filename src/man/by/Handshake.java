package man.by;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

public class Handshake {

    private static final String ENCODING_TYPE = "UTF-8";
    private static final String HASHING_ALGORITHM = "SHA-1";

    private static final String WEBSOCKET_SUPPORTED_VERSIONS = "13";
    private static final String WEBSOCKET_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private static final String ORIGIN_HEADER = "ORIGIN";

    private static final String SEC_WEBSOCKET_KEY_HEADER = "Sec-WebSocket-Key";
    private static final String SEC_WEBSOCKET_ACCEPT_HEADER = "SEC-WEBSOCKET-ACCEPT";
    private static final String UPGRADE_HEADER = "UPGRADE";
    private static final String CONNECTION_HEADER = "CONNECTION";
    private static final String SEC_WEBSOCKET_VERSION_HEADER = "SEC-WEBSOCKET-VERSION";
    private static final String SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";


   public void handshakeRequest(HttpRequest httpRequest){

//       try {
//           httpRequest.getSocket().setSoTimeout(30000);
//       } catch (SocketException e) {
//           e.printStackTrace();
//       }

       String securityWebSocketKey = httpRequest.getHeaderFields().get(SEC_WEBSOCKET_KEY_HEADER);

       try {
           MessageDigest ms = MessageDigest.getInstance(HASHING_ALGORITHM);
           String keyArray = (securityWebSocketKey + WEBSOCKET_GUID);

           byte[] hashedKey = ms.digest (keyArray.getBytes(ENCODING_TYPE));
           String acceptKey = Base64.getEncoder().encodeToString(hashedKey);

           OutputStream outputStream = httpRequest.getSocket().getOutputStream();
           BufferedOutputStream bf = new BufferedOutputStream(new DataOutputStream(outputStream));

          String acceptKeyString = "Sec-WebSocket-Accept:" + acceptKey +"\r\n\r\n";


           byte[] response = (
                   "HTTP/1.1 101 Switching Protocols\r\n" +
                   "Upgrade: websocket\r\n" +
                   "Connection: Upgrade\r\n" +
                   "Sec-websocket-version: 13\r\n" +
             //      "Sec-WebSocket-Extensions: deflate-frame\r\n"+
             //       "Sec-WebSocket-Protocol: gzip, deflate, br\r\n" +
                    acceptKeyString
                   //+ "Sec-WebSocket-Protocol: chat\r\n"
                   ).getBytes(ENCODING_TYPE);

           bf.write(response,0,response.length);
           bf.flush();

       } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }
}
