/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffman;

import huffman.binary.BinaryIn;
import huffman.binary.BinaryOut;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;




/**
 *
 * @author josephpisciotta
 */
 
public class Encode {
    // input is an array of frequencies, indexed by character code
    
//    private static HashMap<Character, Integer> canonicalCodes = new HashMap<>();
//	private static HashMap<String, Character> codes = new HashMap<>();
	private static HuffmanTree tree;
	private static int[] frequencyArray = new int[256];
	private static int[] codeLengths = new int[256];
    private static final int EOF = 0;
    
    public static void main(String[] args) throws IOException {
        String outFile = args[1];
        String inFile = args[0];
        
        new BinaryOut(outFile);
        
        BinaryIn bin = new BinaryIn(inFile);
 
        // contains count of all letters in used alphabet        
        
       	// calc frequencies of all letters
        char temp;
        boolean empty = bin.isEmpty();
        while(!empty){
            temp = bin.readChar();
            frequencyArray[temp]++;
            empty = bin.isEmpty();
        }
        frequencyArray[0]++;
        
 
        // build tree
        tree = buildTreeFromFrequencies(frequencyArray);
        
		buildCodeLengths(tree.root, 0);
		
		
		// Canonize Tree
		tree = generateCanonTree();

		
		int l = tree.size();
		BinaryOut.write((byte) (l));
        
		// write the character code and the frequency
		for(int i = 0; i < 256; i++){
			if(codeLengths[i] > 0){
				BinaryOut.write((byte)i);
				BinaryOut.write((byte)codeLengths[i]);
				//System.out.println(i + " " + codeLengths[i]);
			}
		}


        bin = null;
        BinaryIn bin2 = new BinaryIn(inFile);


        // write the encoded bits of the message
        empty = bin2.isEmpty();
        while(!empty){
            char temp2 = (char) bin2.readByte();
            String code = tree.getCode(temp2);
            
            // write the bits
            writeCode(code);

            empty = bin2.isEmpty();
        }
            
        // write the bits
        writeCode(tree.getCode((char)0));

        BinaryOut.close();
    }

	private static void writeCode(String code){
		for(char u : code.toCharArray()){
          	if (u == '0') {
            	BinaryOut.write(false);
            }
          	else if (u == '1') {
            	BinaryOut.write(true);
        	}
        }
	}
    
    private static HuffmanTree buildTreeFromFrequencies(int[] charFreqs) {
        // Minimum Priority queue
        PriorityQueue<FrequencyNode> priorityQueue = new PriorityQueue<FrequencyNode>();
        
        // add all the leafs to the priority queue
        for (int i = 0; i < charFreqs.length; i++){
            if (charFreqs[i] > 0){
                priorityQueue.offer(new FrequencyNode( new HuffmanLeaf((char) i), i,charFreqs[i]));
            }
        }
 
        assert priorityQueue.size() > 0;
        
        // build tree with the top trees on PQ
        // then insert the new tree to the PQ
        // do this until there is only 1 left
        while (priorityQueue.size() > 1) {
            FrequencyNode a = priorityQueue.poll();
            FrequencyNode b = priorityQueue.poll();

			HuffmanNode temp = new HuffmanNode(a.node, b.node);
 
            priorityQueue.offer(new FrequencyNode(temp, Math.min(a.lowestSymbol, b.lowestSymbol), a.frequency + b.frequency));
        }
        // last tree gets returned as it is the full tree
        HuffmanTree temp = new HuffmanTree((HuffmanNode)priorityQueue.poll().node, charFreqs.length);
        
        return temp;
    }
	
	private static void buildCodeLengths(Node node, int depth) {
		if (node instanceof HuffmanNode) {
			HuffmanNode huffmanNode = (HuffmanNode)node;
			buildCodeLengths(huffmanNode.left , depth + 1);
			buildCodeLengths(huffmanNode.right, depth + 1);
		} else if (node instanceof HuffmanLeaf) {
			char symbol = ((HuffmanLeaf)node).symbol;
			codeLengths[symbol] = depth;
		} else {
			throw new AssertionError("Something aint right");
		}
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
  
 
