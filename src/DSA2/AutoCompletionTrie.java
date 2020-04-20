package DSA2;

import java.util.*;

public class AutoCompletionTrie{
    private AutoCompletionTrieNode root;//root of the trie

    /**
     * Constructor
     *
     * Sets the dictionary, prefix and trie
     *
     * @param dict dictionary with frequencies
     */
    public AutoCompletionTrie(HashMap<String,Number> dict){
        root = new AutoCompletionTrieNode(' ',null);
        for (Map.Entry<String, Number> entry : dict.entrySet()) {//descend through the treemap
            this.add(entry.getKey(), (Integer) entry.getValue());
        }
    }

    //Trie Clone
    /**
     * Constructor
     *
     * For a trie that doesnt have any nodes on it already
     * Creates a TrieNode that is the root of the trie
     */
    public AutoCompletionTrie(){
        root = new AutoCompletionTrieNode(' ',null);
    }
    /**
     * Constructor
     * For a sub trie
     * @param root the node that needs to be the root of the trie
     */
    public AutoCompletionTrie(AutoCompletionTrieNode root){
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
    public boolean add(String key,int quantity) {
        if (!contains(key)){
            AutoCompletionTrieNode current = root;
            key = key.toLowerCase();
            for (int i = 0; i < key.length(); i++) {
                if (!current.isChild(key.charAt(i))){
                    current.addChild(key.charAt(i));
                }
                current = current.getChildNode(key.charAt(i));
            }
            current.alterWordsEnding(quantity);
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
        AutoCompletionTrieNode current = root;//start at the root
        key=key.toLowerCase();
        for (char c:key.toCharArray()) {//goes through each character
            current = current.getChildNode(c);//attempt to ge the child node
            if (current == null) {//is the child null
                return false;
            }
        }
        return current.getQuantityOfWordsEnding()>0;//if all the characters are there
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
        Queue q = new LinkedList<AutoCompletionTrieNode>();//make a queue with a linked list
        addChildNodeToQueue(root,q);//adds the child nodes of the root to the queue
        while(q.size()>0){//while there is still nodes in the queue
            AutoCompletionTrieNode current = (AutoCompletionTrieNode) q.peek();//get first element
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
    private Queue addChildNodeToQueue(AutoCompletionTrieNode node, Queue q){
        AutoCompletionTrieNode[] children = node.getChildren();//get all the children of the node
        for (AutoCompletionTrieNode child:children) {//for each child
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
    private String depthRecursive(AutoCompletionTrieNode node){
        StringBuilder str = new StringBuilder();//create a new string builder
        str.append(node.getLetter());//add the node value first
        AutoCompletionTrieNode[] children = node.getChildren();//get the children
        for (AutoCompletionTrieNode child:children) {//for each child node
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
    public AutoCompletionTrie getSubTrie(String prefix){
        AutoCompletionTrieNode current = root;//start at the root of the trie
        for (int i = 0; i < prefix.length(); i++) {//for each letter in the prefix
            char c = prefix.charAt(i);//get the character
            AutoCompletionTrieNode node = current.getChildNode(c);//get the child node of the character
            if (node == null) {//if at any point the trie doesnt contain the prefix it returns null
                return null;
            }
            current = node;//set the current node to the child node
        }
        return new AutoCompletionTrie(current);//creates a new trie with the root being the current node
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
    public LinkedHashMap<String,Integer> getAllWords(){
        LinkedHashMap<String,Integer> words = new LinkedHashMap<>();//create new list
        if (root.getQuantityOfWordsEnding()>0){//This is so that when a prefix of a sub-trie is a whole word in the trie that word isnt missed out
            words.put("",root.getQuantityOfWordsEnding());
        }
        Queue q = new LinkedList<AutoCompletionTrieNode>();//make a queue with a linked list
        addChildNodeToQueue(root,q);//adds the child nodes of the root to the queue
        while(q.size()>0){//while there is still nodes in the queue
            AutoCompletionTrieNode current = (AutoCompletionTrieNode) q.peek();//get first element
            q.remove();//remove the first item in the queue
            q = addChildNodeToQueue(current,q);//add all the child nodes of the current element to the queue
            if (current.getQuantityOfWordsEnding()>0){
                StringBuilder sb = new StringBuilder(getWordFromNode(current));
                words.put(sb.reverse().toString(),current.getQuantityOfWordsEnding());
            }
        }
        return words;
    }
    String getWordFromNode(AutoCompletionTrieNode node){
        String str = "";
        if (node.getLetter()!=' '){
            str += node.getLetter() + getWordFromNode(node.getParentNode());
        }
        return str;
    }

    //Additional
    /**
     * Gets all probabilities
     *
     * Gets a list of words from the subtrie
     * Iterates through that list of words
     * Adding the prefix onto them all
     * And generating the total number of frequency of all the words
     * Iterate again calculating the probability and adding it to the hashmap
     *
     * @return the hashmap of the probability and word
     */
    private LinkedHashMap<String,Number> getAllProbabilites(String prefix){
        AutoCompletionTrie subTrie = this.getSubTrie(prefix);//gets the list of words after the prefix that are in the subtrie
        int total = subTrie.root.getQuantityOfWordsEnding();
        Queue q = new LinkedList<AutoCompletionTrieNode>();//make a queue with a linked list
        addChildNodeToQueue(subTrie.root,q);//adds the child nodes of the root to the queue
        while(q.size()>0){//while there is still nodes in the queue
            AutoCompletionTrieNode current = (AutoCompletionTrieNode) q.peek();//get first element
            q.remove();//remove the first item in the queue
            q = addChildNodeToQueue(current,q);//add all the child nodes of the current element to the queue
            total += current.getQuantityOfWordsEnding();
        }
        LinkedHashMap<String,Number> r = new LinkedHashMap<>();//create new hashmap to return
        LinkedHashMap<String,Integer> allWords = subTrie.getAllWords();
        for (Map.Entry<String,Integer> entry : allWords.entrySet()) {//through the map
            if (entry.getKey()==""){
                r.put(prefix,(double) entry.getValue()/total);
            } else {
                r.put(entry.getKey(),(double) entry.getValue()/total);
            }
        }
        return r;
    }
    /**
     * Get all the probabilities for the prefix
     * Put all the entries into a TreeMap which sorts by value
     * Descend through the tree map adding the entry to the hashmap
     * Only put a max if 3 entries into the hashmap
     *
     * @return linked hash map of the top three probabilities
     */
    public LinkedHashMap<String,Number> getTopThreeProbability(String prefix){
        LinkedHashMap<String,Number> r = new LinkedHashMap<>();//create new hashmap to return
        LinkedHashMap<String,Number> all = getAllProbabilites(prefix);//get all the probabilies
        int i = 0;
        while (all.size()>0&&i++<3){
            double highestProbability = 0.0;
            String highestKey = "";
            for (String key : all.keySet()) {//goes through the map
                if ((double) all.get(key)>highestProbability){
                    highestProbability=(double) all.get(key);
                    highestKey = key;
                }
            }
            r.put(highestKey,highestProbability);
            System.out.println(highestKey + "=" + highestProbability);
            all.remove(highestKey);
        }
        return r;
    }
}

class AutoCompletionTrieNode {
    private AutoCompletionTrieNode[] children;//the children
    private char letter;//value of the node
    private int quantityOfWordsEnding;
    private AutoCompletionTrieNode parent;

    /**
     * Constructor
     * @param letter value of the node
     */
    public AutoCompletionTrieNode(char letter,AutoCompletionTrieNode parent) {
        this.children = new AutoCompletionTrieNode[26];//constructs a new array of trie 26 trie nodes where all are set to null
        this.letter=letter;
        this.quantityOfWordsEnding =0;
        this.parent = parent;
    }
    //Accessors
    /**
     * Gets the children array
     *
     * @return children array
     */
    public AutoCompletionTrieNode[] getChildren(){
        return children;
    }
    /**
     * Gets value of the node
     *
     * @return the letter value
     */
    public char getLetter() {
        return letter;
    }
    /**
     * Get quantity
     *
     * @return number of words that end at this node
     */
    public int getQuantityOfWordsEnding(){
        return quantityOfWordsEnding;
    }
    /**
     * Null if no child node of that letter
     *
     * @param letter the letter value of the child node
     * @return TrieNode of the child node of letter
     */
    public AutoCompletionTrieNode getChildNode(char letter){
        return children[(int)letter-'a'];
    }
    public AutoCompletionTrieNode getParentNode(){
        return parent;
    }
    //Modifiers
    /**
     * Add a child
     *
     * Create a new node
     * @param letter child letter
     */
    public void addChild(char letter){
        this.children[(int)letter-'a'] = new AutoCompletionTrieNode(letter,this);
    }
    /**
     * Change the number of words finishing at this node
     *
     * @param i number of additional words ending at this node
     */
    public void alterWordsEnding(int i){
        quantityOfWordsEnding += i;
    }
    //Methods
    /**
     * Checks if a letter is a child
     *
     * @param letter letter to test
     * @return true if letter is a child
     */
    public boolean isChild(char letter){
        return (getChildNode(letter)!=null);
    }
    /**
     * Determines if this node has any children
     *
     * For each letter in the alphabet
     * If the children array is not null at that letter
     * Return True
     *
     * @return true if there is any children
     */
    public Boolean hasChildren(){
        for(int i = 0;i<26;i++){
            if (this.children[i] != null){
                return true;
            }
        }
        return false;
    }
}