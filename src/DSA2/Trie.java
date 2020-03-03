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
     */
    public Trie(){
        root = new TrieNode(' ');
    }
    /**
     * Constructor
     * For a sub trie
     * @param root new root
     */
    public Trie(TrieNode root){
        this.root = root;
    }
    /**
     * Adding a word to the trie
     * Checks if the word is already in the trie
     * Converts to lower case
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
            return false;
        }
        return true;
    }
    /**
     * See if the word is in the trie
     * @param wordToSearch The word being looked for
     * @return true if the word is in the trie, false if not
     */
    public boolean contains(String wordToSearch){
        TrieNode current = root;
        wordToSearch=wordToSearch.toLowerCase();
        for (char c:wordToSearch.toCharArray()) {
            TrieNode node = current.getChildNode(c);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.getIsWordEnd();
    }
    /**
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
     * Traverse down the trie until the end of the prefix
     * create a new trie with the root being the current node
     * @param prefix prefix of the new sub trie
     * @return sub trie of the prefix
     */
    public Trie getSubTrie(String prefix){
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            TrieNode node = current.getChildNode(c);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return new Trie(current);
    }
    /**
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
