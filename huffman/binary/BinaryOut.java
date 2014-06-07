

package huffman.binary;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class BinaryOut {
    private static BufferedOutputStream out;

    private static int buffer;     
    private static int N;          

    static{
        try {
            out = new BufferedOutputStream(System.out);
        } catch (Exception ex) {
            Logger.getLogger(BinaryOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BinaryOut(String s) {
        try{
        out = new BufferedOutputStream(new FileOutputStream(s));
        } catch (Exception ex) {
            Logger.getLogger(BinaryOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 

    private static void writeBit(boolean bit) {
        buffer <<= 1;
        if (bit) buffer |= 1;

        N++;
        if (N == 8) 
            clearBuffer();
    } 


    private static void writeByte(int x) {
        assert x >= 0 && x < 256;

        if (N == 0) {
            try { 
                out.write(x); 
            }
            catch (IOException e) { 
                e.printStackTrace(); 
            }
            return;
        }

        for (int i = 0; i < 8; i++) {
            boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

    private static void clearBuffer() {
        if (N == 0) return;
        if (N > 0) buffer <<= (8 - N);
        try { out.write(buffer); }
        catch (IOException e) { e.printStackTrace(); }
        N = 0;
        buffer = 0;
    }

   /**
     * Flush standard output
     */
    public static void flush() {
        clearBuffer();
        try { out.flush(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    public static void close() {
        flush();
        try { out.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    public static void write(boolean x) {
        writeBit(x);
    } 


    public static void write(byte x) {
        writeByte(x & 0xff);
    }
    
    public static void write(int x) {
        writeByte((x >>> 24) & 0xff);
        writeByte((x >>> 16) & 0xff);
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

}