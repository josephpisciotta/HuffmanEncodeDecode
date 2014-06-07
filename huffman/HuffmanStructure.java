/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffman;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josephpisciotta
 * 
 * This file creates the general Huffman Tree structure.
 * 
 * There are Nodes and Leafs as classes so it is easy to differentiate
 * when traversing the tree later on.
 */
class HuffmanTree{
	
    public final HuffmanNode root; 
	public List<String> codes;
	private int length = 0;
	
    public HuffmanTree(HuffmanNode root, int symbolLimit) {
		this.root = root;

		codes = new ArrayList<String>(); 
		for (int i = 0; i < symbolLimit; i++)
			codes.add(null);
		buildCodeList(root, new StringBuffer());  
	}
	
	private void buildCodeList(Node node, StringBuffer code) {
		if (node instanceof HuffmanNode) {
			HuffmanNode huffmanNode = (HuffmanNode)node;

			// traverse left
            code.append('0');
            buildCodeList(huffmanNode.left, code);
            code.deleteCharAt(code.length()-1);
 
            // traverse right
            code.append('1');
            buildCodeList(huffmanNode.right, code);
            code.deleteCharAt(code.length()-1);

		} else if (node instanceof HuffmanLeaf) {
			HuffmanLeaf leaf = (HuffmanLeaf)node;
			codes.set(leaf.symbol, code.toString());
			length++;

		} else {
			throw new AssertionError("Something aint right");
		}
	}
	public String getCode(char symbol) {
		if (codes.get(symbol) == null)
			throw new IllegalArgumentException("NO CODE");
		else
			return codes.get(symbol);
	}
	public int size(){
		return length;
	}
}

abstract class Node{
	Node(){} 
}

class FrequencyNode implements Comparable<FrequencyNode> {

	public final Node node;
	public final int lowestSymbol;
	public final int frequency; 

	public FrequencyNode(Node node, int lowestSymbol, int freq) {
		this.node = node;
		this.lowestSymbol = lowestSymbol;
		this.frequency = freq;
	}


	public int compareTo(FrequencyNode other) {
		if (frequency < other.frequency)
			return -1;
		else if (frequency > other.frequency)
			return 1;
		else if (lowestSymbol < other.lowestSymbol)
			return -1;
		else if (lowestSymbol > other.lowestSymbol)
			return 1;
		else
			return 0;
	}

}

class HuffmanNode extends Node{
	public final Node left, right;
	public HuffmanNode(Node l, Node r) {
		this.left = l;
		this.right = r;
	}
}
 
class HuffmanLeaf extends Node {

	public final char symbol;



	public HuffmanLeaf(char symbol) {
		if (symbol < 0)
			throw new IllegalArgumentException("Illegal symbol value");
		this.symbol = symbol;
	}

}