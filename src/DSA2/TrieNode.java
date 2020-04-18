package DSA2;

public class TrieNode {
//    1. Define a TrieNode data structure and class that contains a list of offspring and a flag
//    to indicate whether the node represents a complete word or not. Offspring should be
//    stored in an array of fixed size 26 and the char values of the characters in the trie used as
//    the index. So, for example, the letter ‘a’ is represented by the position 0 in the offspring
//    array. Hence, the root node for the trie shown in Figure 1 would contain a TrieNode array
//    of size 26 with all null values except in positions 1 (‘b’) and 2 (‘c’).
    private TrieNode[] children;
    private char letter;//value of the node
    private boolean isWordEnd;//boolean to tell if it is the end of the word
    /**
     * Constructor
     * @param letter value of the node
     */
    public TrieNode(char letter) {
        this.children = new TrieNode[26];
        this.letter=letter;
        this.isWordEnd=false;
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
    public boolean getIsWordEnd(){
        return isWordEnd;
    }
    //setters
    /**
     * Set the is word End
     * @param isWordEnd what to set the isWordEnd to
     */
    public void setWordEnd(boolean isWordEnd){
        this.isWordEnd=isWordEnd;
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
