package DSA2;

import java.io.FileNotFoundException;
import java.util.*;

public class AutoCompletionTrie{
    private AutoCompletionTrieNode root;//root of the trie

    /**
     * Test harness
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Testing Part 3");
        //1. Load all the queries file called queries.csv from the project directory.
        ArrayList<String> prefixes = new ArrayList<>();
        try {
            prefixes = DictionaryFinder.readWordsFromCSV("TextFiles\\testQueries.csv");//read in the prefixes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> wordsAndFrequencies = new ArrayList<>();
        try {
            wordsAndFrequencies = DictionaryFinder.readWordsFromCSV("TextFiles\\Results\\mytestDictionary.csv");//read in the prefixes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String,Number> dict = new LinkedHashMap<>();
        dict.put((wordsAndFrequencies.get(0)),Integer.parseInt((wordsAndFrequencies.get(1).split("\n"))[0]));
        for (int i = 1;i < wordsAndFrequencies.size() - 1;i++){
            dict.put((wordsAndFrequencies.get(i).split("\n"))[1],Integer.parseInt((wordsAndFrequencies.get(i+1).split("\n"))[0]));
        }
        AutoCompletionTrie t = new AutoCompletionTrie(dict);
        LinkedHashMap<String,Number> pro = new LinkedHashMap<>();
        for (String pre:prefixes){//for each prefix
            System.out.println("For: " + pre);
            //2. For each query, find the best three matches (at most) with the most likely first, and with
            //associated estimated probability of correctness. If words have equal probability, choose
            //the first occurring word as determined by a breadth first search. Probabilities should be
            //calculated from the frequencies (see example below).
            pro.putAll(t.getTopThreeProbability(pre));
            DictionaryFinder.saveToFile(pro,"TextFiles\\Results\\myTestMatches.csv");//3. Write the results into a file called matches.csv in exactly the specified format.
        }
    }

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
     * ALtered to take a quantity of word aswell as a word
     *
     * Checks if the word is already in the trie
     * For each character in the word
     * Create child character if needed
     * Then look at the child node
     * When it gets to the end of the word set the node at the end to have the quantity of words finishing there
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
     * Modified to return the fequency of the word too
     * Also is a linked hashmap os the order matters
     * This is a breadth first search to determine the order for any words that occurs equal number of times
     *
     * Check if the root is a end of word
     * Add all the root children to the queue (actually a Linked List)
     * While there is still nodes to process
     * Get the next node to process out of the queue
     * Add all the children of the current node into the queue
     * If it is an end of a word
     * Work out what the word is by using the parents of it
     * Add the word and the frequencies to the hashmap
     *
     * @return Linked hashmap containing all the words in the trie with their frequencies
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
                StringBuilder sb = new StringBuilder(getWordFromNode(current));//builds the word from end to start
                words.put(sb.reverse().toString(),current.getQuantityOfWordsEnding());//reverses the string builder and adds it to the map
            }
        }
        return words;
    }
    /**
     * Helper function to get all words
     *
     * If the node is not the root (the character space)
     * Add the node letter value to the start of the string
     * Call recursivly on the current nodes parent
     *
     * @param node current node
     * @return the word as a string in reverse backtracking up through the parents
     */
    String getWordFromNode(AutoCompletionTrieNode node){
        String str = "";//new string to return
        if (node.getLetter()!=' '){//if node isnt the root node
            str += node.getLetter() + getWordFromNode(node.getParentNode());//add letter to string plus recursive call on the parant node
        }
        return str;
    }

    //Additional
    /**
     * Gets all probabilities of all the words from the prefix
     *
     * Get the sub trie of the prefix
     * Set the total number of words equal to the root of the sub trie quantity (for words that are the prefix)
     * Add all the root nodes to the queue to process
     * While there is still nodes to process
     * Get the first node in the queue
     * Add all of its children to the queue
     * Add the number of words finishing on that node to the total
     * Get a linked hashmap of all the words and frequencies with getAllWords function
     * Go through the map
     * If the entry is "" add the prefix and the frequency divided by the total to the Linked hashmap to return
     * else just add the key entry and the frequency divided by the total for it to the linked hashmap
     *
     * @param prefix the prefix to get all the probabilities from
     * @return the linked hashmap of the probability and word
     */
    private LinkedHashMap<String,Number> getAllProbabilites(String prefix){
        LinkedHashMap<String,Number> r = new LinkedHashMap<>();//create new hashmap to return
        //getting subtrie
        AutoCompletionTrie subTrie = this.getSubTrie(prefix);//gets the list of words after the prefix that are in the subtrie
        if (subTrie!=null){
            //getting total number of words in sub trie
            int total = subTrie.root.getQuantityOfWordsEnding();//set the total to the sub trie root node quantity of words ending
            Queue q = new LinkedList<AutoCompletionTrieNode>();//make a queue with a linked list
            addChildNodeToQueue(subTrie.root,q);//adds the child nodes of the root to the queue
            while(q.size()>0){//while there is still nodes in the queue
                AutoCompletionTrieNode current = (AutoCompletionTrieNode) q.peek();//get first element
                q.remove();//remove the first item in the queue
                q = addChildNodeToQueue(current,q);//add all the child nodes of the current element to the queue
                total += current.getQuantityOfWordsEnding();//add the number of words ending there to the total
            }
            //calculating the probabilities
            LinkedHashMap<String,Integer> allWords = subTrie.getAllWords();//gets all the words and frequencies
            for (Map.Entry<String,Integer> entry : allWords.entrySet()) {//through the map
                if (entry.getKey()==""){//if the prefix was a word
                    r.put(prefix,(double) entry.getValue()/total);//put the prefix and frequencies divided by total in map
                } else {
                    r.put(entry.getKey(),(double) entry.getValue()/total);//put the word and the frequencies difvided by the total in the map
                }
            }
        }
        return r;
    }
    /**
     * Get the three most likely words from a given prefix
     *
     * Get all the probabilities for the prefix
     * While the map of all the words still has entrys
     * and 3 havent yet been put in
     * Check for the first highest one
     * Only change the highest one if the next one is greater than any previous (not greater than or equal to)
     * Print out the highest one that round
     * Add it to the map to return
     * Remove that element from the all words map
     *
     * @param prefix prefix of the word
     * @return linked hash map of the top three probabilities
     */
    public LinkedHashMap<String,Number> getTopThreeProbability(String prefix){
        LinkedHashMap<String,Number> r = new LinkedHashMap<>();//create new hashmap to return
        LinkedHashMap<String,Number> all = getAllProbabilites(prefix);//get all the probabilies
        int i = 0;
        while (all.size()>0&&i++<3){//while there is still entrys left to go through and less than three entries have been put in
            double highestProbability = 0.0;
            String highestKey = "";
            for (String key : all.keySet()) {//goes through the map of words and probabilities
                if ((double) all.get(key)>highestProbability){//if the probability of the current word is greater than the current highest probability
                    highestProbability=(double) all.get(key);//set the highest probability to the current map
                    highestKey = key;//set the highest key too
                }
            }
            r.put(highestKey,highestProbability);//put the highest one in the map to return
            System.out.println(highestKey + "=" + highestProbability);//print out the highest word and the highest probability
            all.remove(highestKey);//remove the highest one from the map of all words
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
     *
     * @param parent the parent node
     * @param letter value of the node
     */
    public AutoCompletionTrieNode(char letter,AutoCompletionTrieNode parent) {
        this.children = new AutoCompletionTrieNode[26];//constructs a new array of trie 26 trie nodes where all are set to null
        this.letter=letter;//sets the letter value
        this.quantityOfWordsEnding =0;//sets the number of words ending on this node to 0
        this.parent = parent;//sets the parent to the parent passed in
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
    /**
     * Gets the parent node
     *
     * @return the parent node
     */
    public AutoCompletionTrieNode getParentNode(){
        return parent;
    }
    //Modifiers
    /**
     * Add a child
     *
     * Create a new node in the appropreate place in the children array
     *
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
}