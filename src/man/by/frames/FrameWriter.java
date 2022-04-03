package man.by.frames;


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FrameWriter {

   private boolean close = true;

    public FrameWriter(FrameReader fr) {
            if (fr.getTextOpcode() == Opcode.CONNECTION_CLOSE_CONTROL_FRAME){
               this.close = false;

                try {
                    BufferedOutputStream bof = new BufferedOutputStream(new DataOutputStream(fr.getSocket().getOutputStream()));
                    bof.write(hexToByteArray("8880270AB957"));

                    bof.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public boolean isClose(){
        return close;
    }

    private static byte[] hexToByteArray(String hstr)
    {
        if ((hstr.length() < 0) || ((hstr.length() % 2) != 0))
        {
            throw new IllegalArgumentException(String.format("Invalid string length of <%d>",hstr.length()));
        }

        int size = hstr.length() / 2;
        byte buf[] = new byte[size];
        byte hex;
        int len = hstr.length();

        int idx = (int)Math.floor(((size * 2) - (double)len) / 2);
        for (int i = 0; i < len; i++)
        {
            hex = 0;
            if (i >= 0)
            {
                hex = (byte)(Character.digit(hstr.charAt(i),16) << 4);
            }
            i++;
            hex += (byte)(Character.digit(hstr.charAt(i),16));

            buf[idx] = hex;
            idx++;
        }

        return buf;
    }
}
