//Jake Hauser (3928675)
//CS 1501 Summer 2018

import java.util.*;

public class DLB implements DictInterface
{

	private static char null_term = '$';
	Node root;

	//constructor
	public DLB()
	{
		root = new Node(); 
	}	

	//adds strings to he DLB structure  
	public boolean add(String s)
	{
		s = s + null_term;       //null term added, '$'
		Node currentNode = root;

		for(int i = 0; i < s.length(); i++){

			char c = s.charAt(i);

			if(currentNode == root && root.value == 0){
				root.value = c;                 //if root, create value for it
				currentNode = newChild(root);   //go to next child down

			}else if(currentNode.value == '\0'){
				currentNode.value = c;
				currentNode = newChild(currentNode);

			}else if(currentNode.value != 0){

				if(currentNode.value == c){
					currentNode = currentNode.childNode; //if match, move down a level on this path
				}else{

					currentNode = checkSiblings(currentNode, c); //if not, check siblings for a match 

					if(currentNode.siblingNode == null && currentNode.value != c){
						currentNode.siblingNode = new Node(c);    //if not found, create a new sibling
						currentNode = currentNode.siblingNode;	  //then create a child for your new sibling 	
						currentNode = newChild(currentNode);
					}else{
						currentNode = currentNode.childNode;  //this would mean a match, so move down a level 
					}
				}	
			}			
		}

		return true; //it kind of always just returns true...
	}

	//adds a new child to the current node
	//I found it more conveninent to instantiate with a value of \0 
	private Node newChild(Node node){
		node.childNode = new Node('\0');
		node = node.childNode;
		return node;
	}


	//checks the current level for any matching sibling to the character
	//returned if found
	//if not, the last sibling on the level is returned so that a new sibling
	//can be created at the end of the node chain
	private Node checkSiblings(Node node, char letter)
	{
		if(node.siblingNode == null){
			return node;
		}else{
			while(hasSibling(node))
			{
				if(node.siblingNode.value == letter){
					node = node.siblingNode;
					return node;
				}else{
					node = node.siblingNode;
				}
			}
		}
		return node;
	}

	//simple boolean check to see if a the current node being checked has a sibling 
	private boolean hasSibling(Node n)
	{
		if(n.siblingNode != null){
			return true;
		}else{
			return false;
		}
	}
	
	//searches the DLB for a word or prefix 
	public int searchPrefix(StringBuilder s)
	{
	
		boolean prefix, word;
		prefix = false;
		word = false;	
		Node currentNode = root; //start at root 

		for(int i = 0; i <= s.length(); i++)
		{	

			if(currentNode.value == null_term && i == s.length()){

				if(currentNode.siblingNode == null){
					word = true;
					prefix = false;
				}else{
					word = true;
					prefix = true;			
				}
		
			}else if(currentNode.value != null_term && i == s.length()){
				prefix = true;
				word = false;

			}else{

				if(currentNode.value == s.charAt(i)){
					currentNode = currentNode.childNode;
				}else{

					currentNode = checkSiblings(currentNode, s.charAt(i));

					if(currentNode.value == s.charAt(i)){
						currentNode = currentNode.childNode;
						prefix = true;
						
					}else{
						prefix = false;
						word = false;
						return 0;
					}	
				}			
			}
		}

		if (prefix && word) return 3;
		else if (word) return 2;
		else if (prefix) return 1;
		else return 0;
	}	

	//same as the above method, but allows for a start and end point
	//note the only real difference in code is using 'end+1' instead of s.lengh()
	public int searchPrefix(StringBuilder s, int start, int end)
	{
		boolean prefix, word;
		prefix = false;
		word = false;	
		Node currentNode = root;

		for(int i = start; i <= end+1; i++)
		{	

			if(currentNode.value == null_term && i == end + 1){

				if(currentNode.siblingNode == null){
					word = true;
					prefix = false;
				}else{
					word = true;
					prefix = true;			
				}
		
			}else if(currentNode.value != null_term && i == end + 1){
				prefix = true;
				word = false;

			}else{

				if(currentNode.value == s.charAt(i)){
					currentNode = currentNode.childNode;
				}else{
					currentNode = checkSiblings(currentNode, s.charAt(i));

					if(currentNode.value == s.charAt(i)){
						currentNode = currentNode.childNode;
						prefix = true;
						
					}else{
						prefix = false;
						word = false;
						return 0;
					}	
				}		
			}
		}

		if (prefix && word) return 3;
		else if (word) return 2;
		else if (prefix) return 1;
		else return 0;
	}

	//private inner Node class
	private class Node
	{

		private Node siblingNode;
		private Node childNode;
		private char value;

		public Node()
		{
			siblingNode = null;
			childNode = null; 
		}

		public Node(char letter)
		{
			value = letter;
			siblingNode = null;
			childNode = null;
		}
	}
}
