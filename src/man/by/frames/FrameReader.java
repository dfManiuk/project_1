package man.by.frames;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FrameReader {



    public void read(Socket socket) throws IOException {
        byte[] data = null;
        int length = -1;
        InputStream is = socket.getInputStream();

        DataInputStream reader = new DataInputStream(is);

        if ((length = reader.available()) > 0) {
            data = new byte[length];
            reader.readFully(data, 0, length);

            String da = bytesToHex(data);
            System.out.println(da);

            new Frame().parse(data).toString();

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

    private class Frame {

       private boolean fin;
       private boolean rsv1;
       private boolean rsv2;
       private boolean rsv3;
       private byte opcode;
       private boolean masked;
       private int payloadLength;
       private int byteCount = 0;

        private Frame parse(byte[] data){
            Frame  frame = new Frame();
            frame.fin =  ((data[0] & 0x80) != 0);
            frame.rsv1 = ((data[0] & 0x40) != 0);
            frame.rsv2 = ((data[0] & 0x20) != 0);
            frame.rsv3 = ((data[0] & 0x10) != 0);
            frame.opcode = (byte)(data[0] & 0xF);
            frame.masked = ((data[0] & 0x80) != 0);
            frame.payloadLength = (byte) (0x0F & data[1]);
            if (payloadLength == 0x7F){
                frame.byteCount = 8;
            } else if (payloadLength == 0x7E){
                frame.payloadLength = 2;
            }

            return frame;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fin).append(' ').append(this.rsv1).append(' ').append(this.rsv2).append(' ').append(this.rsv3)
                    .append(' ').append(this.opcode) .append(' ').append(masked).append(' ').append(payloadLength);

            System.out.println(stringBuilder.toString());

            return stringBuilder.toString();
        }
    }
}
