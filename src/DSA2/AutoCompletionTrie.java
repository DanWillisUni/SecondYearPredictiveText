package DSA2;

import java.io.*;
import java.util.*;

class AutoCompletionTrie{
    LinkedHashMap<String,Integer> dict;//dictionary, frequency
    String prefix;//the prefix that is being searched for
    Trie currentTrie;//trie that the prefix is on

    /**
     * Simple test harness for the predictive text
     * @param args
     */
    public static void main(String[] args) {
        LinkedHashMap<String,Integer> freq = new LinkedHashMap<>();
        freq.put("bat",6);
        freq.put("cheese",5);
        freq.put("cheers",4);
        freq.put("chat",3);
        freq.put("cat",2);
        Trie test = new Trie();
        test.add("bat");
        test.add("cheese");
        test.add("cheers");
        test.add("chat");
        test.add("cat");
        AutoCompletionTrie pch = new AutoCompletionTrie(freq,"ch");
        HashMap<String,Number> allProbabilitespch = pch.getAllProbabilites();
        for (String key : allProbabilitespch.keySet()) {
            System.out.println(key+ "=" + allProbabilitespch.get(key));
        }
        pch.getTopThreeProbability();
        AutoCompletionTrie pche = new AutoCompletionTrie(freq,"che");
        HashMap<String,Number> allProbabilitespche = pche.getAllProbabilites();
        for (String key : allProbabilitespche.keySet()) {
            System.out.println(key+ "=" + allProbabilitespche.get(key));
        }
    }
    
    /**
     * Constructor
     *
     * Sets the dictionary, prefix and trie
     *
     * @param dict dictionary with frequencies
     * @param prefix prefix applied
     */
    public AutoCompletionTrie(LinkedHashMap<String,Integer> dict, String prefix){
        this.dict = dict;
        this.prefix = prefix;
        currentTrie = new Trie();
        for (String key : dict.keySet()) {
            this.currentTrie.add(key);
        }
    }
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
    private HashMap<String,Number> getAllProbabilites(){
        List words = currentTrie.getSubTrie(prefix).getAllWords();//gets the list of words after the prefix that are in the subtrie
        ListIterator<String> iterator = words.listIterator();//creates a list iterator through all the words
        int total = 0;
        while (iterator.hasNext()) {//for each word in the hashmap
            String next = iterator.next();
            total += dict.get(prefix+next);//adds the total frequencies up
        }
        HashMap<String,Number> r = new HashMap<>();//create new hashmap to return
        iterator = words.listIterator();
        while (iterator.hasNext()) {//iterate again
            String next = iterator.next();
            r.put(prefix+next,(dict.get(prefix+next)/(double)total));//put the full word and probability into the hashmap
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
    public HashMap<String,Number> getTopThreeProbability(){
        HashMap<String,Number> r = new HashMap<>();//create new hashmap to return
        HashMap<String,Number> all = getAllProbabilites();//get all the probabilies
        TreeMap<String,Number> sorted = new TreeMap<>();//create a new treemap
        sorted.putAll(all);//put all the probabilities into the treemap
        int i = 0;
        for (Map.Entry<String,Number> entry : sorted.descendingMap().entrySet()) {//descend through the treemap
            if (i++ < 3) {//max of 3 times
                r.put(entry.getKey(),entry.getValue());//put the entry into the hashmap to return
            }
        }
        return r;
    }
}