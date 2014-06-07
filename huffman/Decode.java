/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffman;

import huffman.binary.BinaryIn;
import huffman.binary.BinaryOut;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;




/**
 *
 * @author josephpisciotta
 */


public class Decode {
    // input is an array of frequencies, indexed by character code
    private static HuffmanTree tree;
	private static int[] codeLengths = new int[256];
    private static final char EOF = (char) 0x00;
 
    public static void main(String[] args) throws IOException {
        
        // file names - can replace with args
        String outFile = args[1];
        String inFile = args[0];
        
        // set the BinaryOut stream file
        new BinaryOut(outFile);
        
        // Set up Binary input reader
        BinaryIn bin = new BinaryIn(inFile);
        
        // get the length of the alphabet - this is first byte
        int alphLength = (int) bin.readByte();        
        
        // Code Lengths contains the length of code for each letter
        for(int i = 0; i < alphLength; i++){
            // the character code
            int x = (int) bin.readByte();
            
            // canon code
            int y = (int) bin.readByte();
            
            // set count of letter
            codeLengths[x] = y;

			

        }
        
        // Build the huffman tree 
        tree = generateCanonTree();

  
        HuffmanNode currentNode = tree.root;
		System.out.println(tree.toString());

        // loop control
        boolean notEnd = true;
        while(notEnd){
            // Get the next bit
            boolean curBit = bin.readBoolean();
			Node next;
            // Add the character 1 or 0 to the code string 
            if (curBit) {
                next = currentNode.right;
            }
            else{
                next = currentNode.left;
            }
            
			if(next instanceof HuffmanLeaf){
				char found = ((HuffmanLeaf) next).symbol;
				if(found == EOF){
					break;
				}
				BinaryOut.write((byte)found);
				currentNode = tree.root;
			}
			else if (next instanceof HuffmanNode) {
				currentNode = (HuffmanNode) next;
			}
               
        }
 
        BinaryOut.close();
    }
    
    private static HuffmanTree generateCanonTree() {
		List<Node> nodes = new ArrayList<Node>();
		
		for (int i = max(codeLengths); i >= 1; i--) {
			List<Node> newNodes = new ArrayList<Node>();

			// Add leaves for symbols with code length i
			for (int j = 0; j < codeLengths.length; j++) {
				if (codeLengths[j] == i)
					newNodes.add(new HuffmanLeaf((char)j));
			}

			// Merge nodes from the previous deeper layer
			for (int j = 0; j < nodes.size(); j += 2)
				newNodes.add(new HuffmanNode(nodes.get(j), nodes.get(j + 1)));

			nodes = newNodes;
		}

		return new HuffmanTree(new HuffmanNode(nodes.get(0), nodes.get(1)), codeLengths.length);
	}
	
	private static int max(int[] array) {
		int result = array[0];
		for (int x : array)
			result = Math.max(x, result);
		return result;
	}
}
   
