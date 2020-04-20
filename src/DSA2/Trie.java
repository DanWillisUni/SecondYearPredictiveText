package DSA2;

import java.util.*;

public class Trie {
    private TrieNode root;//root of the trie
    /**
     * Test harness
     * @param args
     */
    public static void main(String[] args) {
        Trie t = new Trie();
        t.add("bat");
        t.add("cheese");
        t.add("cheers");
        t.add("chat");
        t.add("cat");
        System.out.println("Added: bat,cheese,cheers,chat,cat");
        System.out.println("Going to try and add \"Cat\"");
        System.out.println(t.add("Cat"));
        System.out.println("Breath First Search: " + t.outputBreadthFirstSearch());
        System.out.println("Depth First Search: " + t.outputDepthFirstSearch());
        System.out.println("All words: " + t.getAllWords().toString());
        System.out.println("Creating sub trie of prefix \"ch\"");
        Trie sub = t.getSubTrie("ch");
        System.out.println("All words from sub trie: " + sub.getAllWords().toString());
    }
    /**
     * Constructor
     * For a trie that doesnt have any nodes on it already
     * Creates a TrieNode that is the root of the trie
     */
    public Trie(){
        root = new TrieNode(' ');
    }
    /**
     * Constructor
     * For a sub trie
     * @param root the node that needs to be the root of the trie
     */
    public Trie(TrieNode root){
        this.root = root;
    }
    /**
     * 1. boolean add(String key): adds a key to the trie, creating any nodes required and
     * returns true if add was successful (i.e. returns false if key is already in the trie, true otherwise).
     *
     * Checks if the word is already in the trie
     * For each character in the word
     * Create child character if needed
     * Then look at the child node
     *
     * @param key the word to add to the trie
     * @return true if it was already in the trie
     */
    public boolean add(String key) {
        if (!contains(key)){
            TrieNode current = root;
            key = key.toLowerCase();
            for (int i = 0; i < key.length(); i++) {
                if (!current.isChild(key.charAt(i))){
                    current.addChild(key.charAt(i));
                }
                current = current.getChildNode(key.charAt(i));
            }
            current.setWordEnd(true);
            return true;
        }
        return false;
    }
    /**
     * 2. boolean contains(String key): returns true if the word passed is in the trie as a
     * whole word, not just as a prefix
     *
     * For each characters in the key
     * Try to get the child of the next character
     * If it is null return false right away
     * If all the charaters are there return if the last charater is an end of the word
     *
     * @param key The word being looked for
     * @return true if the word is in the trie, false if not
     */
    public boolean contains(String key){
        TrieNode current = root;//start at the root
        key=key.toLowerCase();
        for (char c:key.toCharArray()) {//goes through each character
            current = current.getChildNode(c);//attempt to ge the child node
            if (current == null) {//is the child null
                return false;
            }
        }
        return current.getIsWordEnd();//if all the characters are there
    }
    /**
     * 3. String outputBreadthFirstSearch(): returns a string representing a breadth first
     * traversal.
     *
     * Get the children nodes of the root
     * Add the children to the queue
     * Take off the queue then look at the children of that node and add them to the queue
     * Keep going until the queue is empty
     *
     * @return returns the letters in breath first order
     */
    public String outputBreadthFirstSearch(){
        StringBuilder str = new StringBuilder();//make a string builder to return
        Queue q = new LinkedList<TrieNode>();//make a queue with a linked list
        addChildNodeToQueue(root,q);//adds the child nodes of the root to the queue
        while(q.size()>0){//while there is still nodes in the queue
            TrieNode current = (TrieNode) q.peek();//get first element
            q.remove();//remove the first item in the queue
            q = addChildNodeToQueue(current,q);//add all the child nodes of the current element to the queue
            str.append(current.getLetter());//add the current letter value to the output string
        }
        return str.toString();
    }
    /**
     * Helper function for breadth first search
     *
     * Gets the children of the node
     * For each child
     * If the value of the child is not null, add it to the queue
     *
     * @param node node whose children need adding
     * @param q queue to add them too
     * @return the queue with the child nodes added
     */
    private Queue addChildNodeToQueue(TrieNode node,Queue q){
        TrieNode[] children = node.getChildren();//get all the children of the node
        for (TrieNode child:children) {//for each child
            if (child!=null){//if the child isnt null
                q.add(child);//add the child to the queue
            }
        }
        return q;
    }
    /**
     * 4. String outputDepthFirstSearch(): returns a string representing a pre order depth
     * first traversal.
     *
     * Appends all the strings of the results of all the children together
     *
     * @return output of depth first traversal
     */
    public String outputDepthFirstSearch(){
        StringBuilder str = new StringBuilder();//string builder to append to
        str.append(depthRecursive(root));
        return str.toString();
    }
    /**
     * Helper function for depth first
     *
     * Adds the current letter to the string to return
     * Gets the children of the node
     * For each child of the node
     * Append onto the return string the recursive function call on each child
     *
     * @param node Current node
     * @return string of depth first search
     */
    private String depthRecursive(TrieNode node){
        StringBuilder str = new StringBuilder();//create a new string builder
        str.append(node.getLetter());//add the node value first
        TrieNode[] children = node.getChildren();//get the children
        for (TrieNode child:children) {//for each child node
            if (child!=null){//if the child exists
                str.append(depthRecursive(child));//recursivly call the function
            }
        }
        return str.toString();
    }
    /**
     * 5. Trie getSubTrie(String prefix): returns a new trie rooted at the prefix, or null if
     * the prefix is not present in this trie
     *
     * Traverse down the trie until the end of the prefix
     * Create a new trie with the root being the current node
     *
     * @param prefix prefix of the new sub trie
     * @return sub trie of the prefix
     */
    public Trie getSubTrie(String prefix){
        TrieNode current = root;//start at the root of the trie
        for (int i = 0; i < prefix.length(); i++) {//for each letter in the prefix
            char c = prefix.charAt(i);//get the character
            TrieNode node = current.getChildNode(c);//get the child node of the character
            if (node == null) {//if at any point the trie doesnt contain the prefix it returns null
                return null;
            }
            current = node;//set the current node to the child node
        }
        return new Trie(current);//creates a new trie with the root being the current node
    }
    /**
     * 6. List getAllWords(): returns a list containing all words in the trie.
     *
     * Check if the root is a end of word
     * Add all the root children to the stack (actually a Linked List)
     * While there is still nodes to process
     * Get the next node to process off the stack
     * Add all the children of the current node onto the stack
     * Check if the top node is done with, if it is pop it and check the new top one
     * repeat till it isnt done with
     * This will be the prefix of the next word
     * If it is the end of a word
     * Add the word to the list of all the words
     *
     * @return List containing all the words in the trie
     */
    public List getAllWords(){
        List words = new ArrayList();//create new list
        if (root.getIsWordEnd()){//This is so that when a prefix of a sub-trie is a whole word in the trie that word isnt missed out
            words.add("");
        }
        LinkedList<TrieNode> todo = new LinkedList<>();//create a new linked list which im using as a stack
        todo = addAllChildNodesToStack(root,todo);//add all the child nodes of the root to the stack
        ArrayList<TrieNode> fullyProcessedNodes = new ArrayList<>();//create an array list of all the done nodes
        LinkedList<TrieNode> currentWord = new LinkedList<>();//current word is also a stack
        while(todo.size()>0){//while there is still nodes to process
            TrieNode current = todo.peek();//peek at the top node on the stack to process
            todo.remove();//remove the top node
            todo = addAllChildNodesToStack(current,todo);//add all the children of the current node to the stack
            //calculates the word by removing the correct letters from the word stack
            while (currentWord.size()>0){//while the current word still has characters in
                TrieNode top = currentWord.peek();//peek at the top one
                if (isNodeDoneWith(top,fullyProcessedNodes)){//if all the children have been done with or it has no children
                    fullyProcessedNodes.add(top);//add to processed nodes
                    currentWord.pop();//remove letter from the word stack
                } else {
                    break;//ends the while if the top letter is still valid and needs to stay there
                }
            }
            //adds word to the all words list if it is the end of a word
            currentWord.push(current);//push the current letter onto the stack
            if (current.getIsWordEnd()){//if its an end of a word
                StringBuilder newWord = new StringBuilder();//creates a new string builder
                for (TrieNode node : currentWord) {//for all the nodes in the current word stack
                    newWord.append(node.getLetter());//append to the word string the node letter
                }
                words.add(newWord.reverse().toString());//add the new word string to the list
            }
        }
        return words;
    }
    /**
     * Helper function to get all words
     *
     * Get all the children
     * Adds all the children of the node to the stack
     *
     * @param node node to add all the children
     * @param stk stack to add all the children onto
     * @return the new stack
     */
    private LinkedList<TrieNode> addAllChildNodesToStack(TrieNode node,LinkedList<TrieNode> stk){
        TrieNode[] children = node.getChildren();//get the children
        for (TrieNode child:children){//for each child
            if (child!=null){//if the child isnt null
                stk.push(child);//push child onto the stack
            }
        }
        return stk;
    }
    /**
     * Helper function to get all words
     *
     * Check if the node has children
     * If it does, for each child
     * If there is a child that isnt in the fully processed nodes array,
     * then the node cannot be fully processed so return false
     *
     * @param node the node to work out if its done with
     * @param fullyProcessedNodes the arraylist of fully processed nodes
     * @return true if the node is done with in the word, false if not
     */
    private boolean isNodeDoneWith(TrieNode node,ArrayList<TrieNode> fullyProcessedNodes){
        if (node.hasChildren()){//the top in current word has children
            TrieNode[] topChildren = node.getChildren();//get the children
            for (TrieNode topChild : topChildren) {//for all of the top nodes children
                if (topChild != null) {
                    if (!fullyProcessedNodes.contains(topChild)) {//if fully processed nodes doesnt contain a child of top
                        return false;//we are not done with the top node
                    }
                }
            }
        }
        return true;
    }
}
