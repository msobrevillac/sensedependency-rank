package pe.edu.dependencyparser.parser;

import java.util.List;

import pe.edu.dependencyparser.domain.Word;


public class DependencyTree {
	
	public class Node {
		
		Word word;
		List<Node> children;
		
	}
	
	Node root;
	
	/**
	 * Constructor
	 */
	public DependencyTree() {
		root = null;
	}
	
	/**
	 * 
	 * @param word
	 */
	public void insertNode(Word word) {
		Node n;
        n = new Node();
        n.word = word;
        
        if (root == null)
        	root = n;
        else {
        	
        }
	}
	

}
