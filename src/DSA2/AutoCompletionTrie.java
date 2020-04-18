package DSA2;

import java.io.*;
import java.util.*;

public class AutoCompletionTrie {
    private TrieNode root;//root of the trie

    /**
     * Constructor
     * For a trie that doesnt have any nodes on it already
     */
    public AutoCompletionTrie(){
        root = new TrieNode(' ');
    }
    /**
     * Constructor
     * For a sub trie
     * @param root new root
     */
    public AutoCompletionTrie(TrieNode root){
        this.root = root;
    }
    /**
     * 1. boolean add(String key): adds a key to the trie, creating any nodes required and
     * returns true if add was successful (i.e. returns false if key is already in the trie, true otherwise).
     * Adding a word to the trie
     * Checks if the word is already in the trie
     * For each character in the word
     * Create child character if needed
     * then look at the child node
     * @param wordToAdd the word to add to the trie
     * @return true if it was already in the trie
     */
    public boolean add(String wordToAdd) {
        if (!contains(wordToAdd)){
            TrieNode current = root;
            wordToAdd = wordToAdd.toLowerCase();
            for (int i = 0; i < wordToAdd.length(); i++) {
                if (!current.isChild(wordToAdd.charAt(i))){
                    current.addChild(wordToAdd.charAt(i));
                }
                current = current.getChildNode(wordToAdd.charAt(i));
            }
            current.setWordEnd(true);
            return true;
        }
        return false;
    }
    /**
     * 2. boolean contains(String key): returns true if the word passed is in the trie as a
     * whole word, not just as a prefix
     * See if the word is in the trie
     * @param wordToSearch The word being looked for
     * @return true if the word is in the trie, false if not
     */
    public boolean contains(String wordToSearch){
        TrieNode current = root;
        wordToSearch=wordToSearch.toLowerCase();
        for (char c:wordToSearch.toCharArray()) {
            current = current.getChildNode(c);
            if (current == null) {
                return false;
            }
        }
        return current.getIsWordEnd();
    }
    /**
     * 3. String outputBreadthFirstSearch(): returns a string representing a breadth first
     * traversal.
     * breadth first traversal of the trie
     * look at the root and get the children nodes and add them all to the queue
     * take off the queue then look at the children and add them to the queue
     * keep going until the queue is empty
     * @return returns the letters in breath first order
     */
    public String outputBreadthFirstSearch(){
        StringBuilder str = new StringBuilder();
        Queue q = new LinkedList<TrieNode>();
        TrieNode[] rootChildren = root.getChildren();
        for (TrieNode rootChild:rootChildren) {
            if (rootChild!=null){
                q.add(rootChild);
            }
        }
        while(q.size()>0){
            TrieNode current = (TrieNode) q.peek();
            q.remove();
            TrieNode[] currentChildren = current.getChildren();
            for (TrieNode currentChild:currentChildren) {
                if (currentChild!=null){
                    q.add(currentChild);
                }
            }
            str.append(current.getLetter());
        }
        return str.toString();
    }
    /**
     * 4. String outputDepthFirstSearch(): returns a string represenng a pre order depth
     * first traversal.
     * Appends all the strings of the results of all the children together
     * @return output of depth first traversal
     */
    public String outputDepthFirstSearch(){
        StringBuilder str = new StringBuilder();
        TrieNode[] rootChildren = root.getChildren();
        for (TrieNode rootChild:rootChildren) {
            if (rootChild!=null){
                str.append(depthRecursive(rootChild));
            }
        }
        return str.toString();
    }
    /**
     * recursive function that is called to do depth first
     * for each node it will add its letter first, then call the function on each of the child nodes
     * and add all those results together
     * @param node Current node
     * @return
     */
    private String depthRecursive(TrieNode node){
        StringBuilder str = new StringBuilder();
        str.append(node.getLetter());
        TrieNode[] children = node.getChildren();
        for (TrieNode nodeChild:children) {
            if (nodeChild!=null){
                str.append(depthRecursive(nodeChild));
            }
        }
        return str.toString();
    }
    /**
     * 5. Trie getSubTrie(String prefix): returns a new trie rooted at the prefix, or null if
     * the prefix is not present in this trie
     * Traverse down the trie until the end of the prefix
     * create a new trie with the root being the current node
     * @param prefix prefix of the new sub trie
     * @return sub trie of the prefix
     */
    public DSA2.Trie getSubTrie(String prefix){
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            TrieNode node = current.getChildNode(c);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return new DSA2.Trie(current);
    }
    /**
     * 6. List getAllWords(): returns a list containing all words in the trie.
     * Do depth first traversal
     * keeps track of which nodes to do next with a stack
     * keep track of which nodes are done
     * keep a stack of which nodes children have been done (the current word)
     * @return List containing all the words in the trie
     */
    public List getAllWords(){
        List words = new ArrayList();
        if (root.getIsWordEnd()){//for if it is a sub trie, and the prefix was a whole word
            words.add("");
        }
        LinkedList<TrieNode> todo = new LinkedList<>();
        TrieNode[] rootChildren = root.getChildren();
        for (TrieNode rootChild:rootChildren){
            if (rootChild!=null){
                todo.push(rootChild);
            }
        }
        ArrayList<TrieNode> done = new ArrayList<>();
        LinkedList<TrieNode> currentWord = new LinkedList<>();
        while(todo.size()>0){
            TrieNode current = todo.peek();
            todo.remove();
            TrieNode[] children = current.getChildren();
            for (TrieNode tn:children){
                if (tn!=null){
                    todo.push(tn);
                }
            }
            while (currentWord.size()>0){
                TrieNode top = currentWord.peek();
                if (!top.hasChildren()){
                    done.add(top);
                    currentWord.pop();
                } else {
                    TrieNode[] topChildren = top.getChildren();
                    Boolean doneWith = true;
                    for (TrieNode topChild:topChildren){
                        if (topChild!=null){
                            if (!done.contains(topChild)){
                                doneWith = false;
                            }
                        }
                    }
                    if (doneWith){
                        done.add(top);
                        currentWord.pop();
                    } else {
                        break;
                    }
                }
            }
            currentWord.push(current);
            if (current.getIsWordEnd()){
                StringBuilder newWord = new StringBuilder();
                for (TrieNode t : currentWord) {
                    newWord.append(t.getLetter());
                }
                words.add(newWord.reverse().toString());
            }
        }
        return words;
    }
}

class AutoCompletionTrieNode {
    private TrieNode[] children;
    private char letter;//value of the node
    private int isWordEnd;//boolean to tell if it is the end of the word
    /**
     * Constructor
     * @param letter value of the node
     */
    public AutoCompletionTrieNode(char letter) {
        this.children = new TrieNode[26];
        this.letter=letter;
        this.isWordEnd=0;
    }
    //accessors
    /**
     * Gets the children
     * @return children array
     */
    public TrieNode[] getChildren(){
        return children;
    }
    /**
     * Gets value of the node
     * @return the letter value
     */
    public char getLetter() {
        return letter;
    }
    /**
     * Get end of word boolean
     * @return return if the node is an end of a word
     */
    public int getIsWordEnd(){
        return isWordEnd;
    }
    //setters
    /**
     * Set the is word End
     */
    public void increaseWordEnd(){
        this.isWordEnd=+1;
    }
    /**
     * Add a child
     * Create a new node
     * @param letter child letter
     */
    public void addChild(char letter){
        this.children[(int)letter-'a'] = new TrieNode(letter);
    }
    /**
     * Checks if a letter is a child
     * @param letter letter to test
     * @return true if letter is a child
     */
    public boolean isChild(char letter){
        return getChildNode(letter)!=null;
    }
    /**
     * Null if no childnode of that letter
     * @param letter the letter value of the child node
     * @return TrieNode of the childnode of letter
     */
    public TrieNode getChildNode(char letter){
        for(TrieNode tn:this.children){
            if (tn!=null){
                if (tn.getLetter()==letter){
                    return tn;
                }
            }
        }
        return null;
    }
    /**
     * Determines if this node has any children
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






//    LinkedHashMap<String,Integer> dict;//dictionary, frequency
//    String prefix;//the prefix that is being searched for
//    Trie currentTrie;//trie that the prefix is on
//    /**
//     * Simple test harness for the predictive text
//     * @param args
//     */
//    public static void main(String[] args) {
//        LinkedHashMap<String,Integer> freq = new LinkedHashMap<>();
//        freq.put("bat",6);
//        freq.put("cheese",5);
//        freq.put("cheers",4);
//        freq.put("chat",3);
//        freq.put("cat",2);
//        Trie test = new Trie();
//        test.add("bat");
//        test.add("cheese");
//        test.add("cheers");
//        test.add("chat");
//        test.add("cat");
//        AutoCompletionTrie pch = new AutoCompletionTrie(freq,"ch");
//        LinkedHashMap<String,Double> allProbabilitespch = pch.getAllProbabilites();
//        for (String key : allProbabilitespch.keySet()) {
//            System.out.println(key+ "=" + allProbabilitespch.get(key));
//        }
//        AutoCompletionTrie pche = new AutoCompletionTrie(freq,"che");
//        LinkedHashMap<String,Double> allProbabilitespche = pche.getAllProbabilites();
//        for (String key : allProbabilitespche.keySet()) {
//            System.out.println(key+ "=" + allProbabilitespche.get(key));
//        }
//    }
//    /**
//     * Constructor
//     * Sets the dictionary, prefix and trie
//     * @param dict dictionary with frequencies
//     * @param prefix prefix applied
//     */
//    public AutoCompletionTrie(LinkedHashMap<String,Integer> dict, String prefix){
//        this.dict = dict;
//        this.prefix = prefix;
//        currentTrie = new Trie();
//        for (String key : dict.keySet()) {
//            this.currentTrie.add(key);
//        }
//    }
//    /**
//     * Gets all probabilities
//     * get the subtrie of the trie with the applied prefix
//     * get all the words of the sub trie
//     * add the prefix on the start of the start of the word
//     * add all the frequiencies together of all the words in the sub trie to get a total
//     * put the word, word frequiencies/total into a hashmap
//     * @return the hashmap of the probability and word
//     */
//    private LinkedHashMap<String,Double> getAllProbabilites(){
//        List words = currentTrie.getSubTrie(prefix).getAllWords();
//        ListIterator<String> iterator = words.listIterator();
//        int total = 0;
//        while (iterator.hasNext()) {
//            String next = iterator.next();
//            //iterator.set(prefix+next);
//            total += dict.get(prefix+next);
//        }
//        LinkedHashMap<String,Double> r = new LinkedHashMap<>();
//        iterator = words.listIterator();
//        while (iterator.hasNext()) {
//            String next = iterator.next();
//            r.put(prefix+next,(dict.get(prefix+next)/(double)total));
//        }
//        return r;
//    }
//    /**
//     * get all the probabilities for the words
//     * iterate over the probabilities
//     * If a probability is greater it sorts it into the top 3
//     * and keep going until all the words are sorted
//     * @return linked hash map of the top three probabilities
//     */
//    public LinkedHashMap<String,Double> getTopThreeProbability(){
//        LinkedHashMap<String,Double> r = new LinkedHashMap<>();
//        LinkedHashMap<String,Double> all = getAllProbabilites();
//        double topThreeA = 0;
//        double topThreeB = 0;
//        double topThreeC = 0;
//        String topThreeKeyA = null;
//        String topThreeKeyB = null;
//        String topThreeKeyC = null;
//        for(String key:all.keySet()){
//            double v = all.get(key);
//            if (v>topThreeC){
//                if (v>topThreeB){
//                    topThreeC=topThreeB;
//                    topThreeKeyC = topThreeKeyB;
//                    if (v>topThreeA){
//                        topThreeB=topThreeA;
//                        topThreeKeyB = topThreeKeyA;
//                        topThreeA=v;
//                        topThreeKeyA=key;
//                    } else {
//                        topThreeB=v;
//                        topThreeKeyB=key;
//                    }
//                } else {
//                    topThreeC = v;
//                    topThreeKeyC = key;
//                }
//            }
//        }
//        if (topThreeA>0){
//            r.put(topThreeKeyA,topThreeA);
//            if (topThreeB>0){
//                r.put(topThreeKeyB,topThreeB);
//                if (topThreeC>0){
//                    r.put(topThreeKeyC,topThreeC);
//                }
//            }
//        }
//        return r;
//    }
//    /**
//     * Saves the top three probabilities to a file
//     * @param map map to write to the file
//     * @param fileToWriteTo file name to write to
//     */
//    public static void saveToFile(LinkedHashMap<String, Double> map, String fileToWriteTo){
//        StringBuilder str = new StringBuilder();
//        for (String key : map.keySet()) {
//            str.append(key).append(",").append(map.get(key)).append("\n");
//            System.out.println(key+ "=" + map.get(key));
//        }
//        try {
//            FileWriter fileWriter = new FileWriter(fileToWriteTo,true);
//            fileWriter.write(str.toString());
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * read the prefixes from csv file
//     * @param file file to read from
//     * @return arraylist of all the prefixes
//     */
//    public static ArrayList<String> readWordsFromCSV(String file){
//        BufferedReader br = null;
//        ArrayList<String> prefixes = new ArrayList<>();
//        String line;
//        try {
//            br = new BufferedReader(new FileReader(file));
//            while ((line = br.readLine()) != null) {
//                String[] splitLines = line.split(",");
//                for (String word:splitLines){
//                    prefixes.add(word.toLowerCase());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return prefixes;
//    }

