/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffman.binary;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author josephpisciotta
 */


public final class BinaryIn {
    private BufferedInputStream in;
    private static final int EOF = -1;    

    private int buffer;           
    private int N;                

    public BinaryIn(String s) throws FileNotFoundException {
        in = new BufferedInputStream(new FileInputStream(s));
        fillBuffer();  
    }

    private void fillBuffer() {
        try { 
            buffer = in.read(); 
            N = 8; 
        }
        catch (IOException e) { 
            buffer = EOF; 
            N = -1; 
        }
    }

    public void close() {
        try {
            in.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Could not close BinaryStdIn");
        }
    }


    public boolean isEmpty() {
        return buffer == EOF;
    }


    public boolean readBoolean() {
        if (isEmpty()) throw new RuntimeException("Reading from empty input stream");
        N--;
        boolean bit = ((buffer >> N) & 1) == 1;
        if (N == 0) fillBuffer();
        return bit;
    }


    public char readChar() {
        if (isEmpty()) throw new RuntimeException();

        if (N == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        int x = buffer;
        x <<= (8-N);
        int oldN = N;
        fillBuffer();
        if (isEmpty()) throw new RuntimeException();
        N = oldN;
        x |= (buffer >>> N);
        return (char) (x & 0xff);
    }


    public byte readByte() {
        char c = readChar();
        byte x = (byte) (c & 0xff);
        return x;
    }
    
    public int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }
    
    public String toString(){
        return in.toString();
    }
    
}