package man.by.frames;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FrameReader {

    private Socket socket;
    private byte[] payload;
    private Opcode textOpcode;

    public FrameReader(Socket socket) throws IOException {
        this.socket = socket;
        read();
    }

    private void read() throws IOException {
        byte[] data = null;
        int length = -1;
        InputStream is = socket.getInputStream();

        DataInputStream reader = new DataInputStream(is);

        if ((length = reader.available()) > 0) {
            data = new byte[length];
            reader.readFully(data, 0, length);

            String da = bytesToHex(data);
            System.out.println(da);

            Frame frame = new Frame().parse(data);

            opcodChecker( frame);

            this.payload = frame.demaskingPayload;

        }
    }


    private void opcodChecker(Frame frame){
        switch (frame.opcode) {
            case 8:
                System.out.println(Opcode.values()[frame.opcode]);
                textOpcode = Opcode.values()[frame.opcode];
        }
    }


    private static String bytesToHex(byte[] array)
    {
        char[] val = new char[2*array.length];
        String hex = "0123456789ABCDEF";
        for (int i = 0; i < array.length; i++)
        {
            int b = array[i] & 0xff;
            val[2*i] = hex.charAt(b >>> 4);
            val[2*i + 1] = hex.charAt(b & 15);
        }
        return String.valueOf(val);
    }

    public byte[] getPayload(){
        return payload;
    }

    public Opcode getTextOpcode(){
        return textOpcode;
    }

    public Socket getSocket(){
        return socket;
    }



    private class Frame {

       private boolean fin;
       private boolean rsv1;
       private boolean rsv2;
       private boolean rsv3;
       private byte opcode;
       private boolean masked;
       private byte[] maskingKey;
       private byte[] payload;
       private byte[] demaskingPayload;

        private Frame parse(byte[] data){
            Frame  frame = new Frame();
            frame.fin =  ((data[0] & 0x80) != 0);
            frame.rsv1 = ((data[0] & 0x40) != 0);
            frame.rsv2 = ((data[0] & 0x20) != 0);
            frame.rsv3 = ((data[0] & 0x10) != 0);
            frame.opcode = (byte)(data[0] & 0xF);
            frame.masked = ((data[0] & 0x80) != 0);
            int payloadLength = (byte) (0x7F & data[1]);

            int byteCount = 0;
            if (payloadLength == 0x7F){
                byteCount = 8;
            } else if (payloadLength == 0x7E){
                byteCount = 2;
            }

            int count = 2;

            while (--byteCount > 0){
                byte b = data[count++];
                payloadLength |= (b & 0xFF) << (8 * byteCount);
            }

            if(frame.masked){
                frame.maskingKey = new byte[4];
                for (int i = 0; i < 4; i++) {
                    frame.maskingKey[i] = data[count++];
                }
            }

            frame.payload = new byte[payloadLength];
            frame.demaskingPayload = new byte[payloadLength];
            System.out.println("");
            for (int i = 0; i < frame.payload.length; i++) {
                frame.payload[i] = data[count++];
            }

            if (frame.masked){
                for (int i = 0; i < frame.payload.length; i++){
                    frame.demaskingPayload[i] = (byte) (frame.payload[i] ^ frame.maskingKey[i % 4]);
                    System.out.print((char) (frame.demaskingPayload[i]));
                }
            }

            return frame;
        }
    }
}
