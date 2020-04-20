package DSA2;

import java.io.*;
import java.util.*;

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

