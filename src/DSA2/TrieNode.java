package DSA2;

public class TrieNode {
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
        for(TrieNode child:this.children){
            if (child!=null){
                if (child.getLetter()==letter){
                    return true;
                }
            }
        }
        return false;
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
            if(isChild((char)(((int)'a')+i))){
                return true;
            }
        }
        return false;
    }
}
