package DSA2;

public class TrieNode {
    private TrieNode[] children;//the children
    private char letter;//value of the node
    private boolean isWordEnd;//boolean to tell if it is the end of the word

    /**
     * Constructor
     * @param letter value of the node
     */
    public TrieNode(char letter) {
        this.children = new TrieNode[26];//constructs a new array of trie 26 trie nodes where all are set to null
        this.letter=letter;
        this.isWordEnd=false;
    }
    //Accessors
    /**
     * Gets the children array
     *
     * @return children array
     */
    public TrieNode[] getChildren(){
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
     * Get end of word boolean
     *
     * @return return if the node is an end of a word
     */
    public boolean getIsWordEnd(){
        return isWordEnd;
    }
    /**
     * Null if no child node of that letter
     *
     * @param letter the letter value of the child node
     * @return TrieNode of the child node of letter
     */
    public TrieNode getChildNode(char letter){
        return children[(int)letter-'a'];
    }
    //Modifiers
    /**
     * Set the is word End
     *
     * @param isWordEnd what to set the isWordEnd to
     */
    public void setWordEnd(boolean isWordEnd){
        this.isWordEnd=isWordEnd;
    }
    /**
     * Add a child
     *
     * Create a new node
     * @param letter child letter
     */
    public void addChild(char letter){
        this.children[(int)letter-'a'] = new TrieNode(letter);
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
