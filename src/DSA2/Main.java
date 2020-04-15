package DSA2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
//      1. read in a document of words, find the set of unique words to form a dictionary, count
//      the occurrence of each word, then save the words and counts to file;
        DictionaryFinder df=new DictionaryFinder();
        ArrayList<String> in= new ArrayList<>();
        try {
            in = DictionaryFinder.readWordsFromCSV("TextFiles\\lotr.csv");//reads from lotr into the arraylist in
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LinkedHashMap<String,Integer> map = df.formDictionary(in); //form a dictionary with frequencies from the text file
        df.saveToFile(map,"TextFiles\\Results\\myGollem.csv");//save the dictionary with frequencies
//      3. make a word completion system using the dictionary from part 1) and the data structure
//      from part 2).
        ArrayList<String> prefixes;
        prefixes = AutoCompletionTrie.readWordsFromCSV("TextFiles\\lotrQueries.csv");//read in the prefixes
        for (String pre:prefixes){//for each prefix
//          2. define a trie data structure for storing a prefix tree and design and implement algorithms
//          to manipulate the trie; and
            AutoCompletionTrie ACT = new AutoCompletionTrie(map,pre);//create new object
            LinkedHashMap<String,Double> pro = ACT.getTopThreeProbability();//get top three probabilities
            AutoCompletionTrie.saveToFile(pro,"TextFiles\\Results\\mylotrMatches.csv");//save the probabilities to the file
        }
    }
    /**
     * Generates a number of words of a set length
     * @param numOfWords the number of words
     * @param wordLength the length of all the words
     * @return all the words
     */
    public static ArrayList<String> generateDoc(int numOfWords,int wordLength) {
        ArrayList<String> words = new ArrayList<>();
        for (int i = 0; i < numOfWords; i++) {
            words.add(generateWord(wordLength));
        }
        return words;
    }
    /**
     * generate a word of a given length by randomly generating letters
     * @param wordLength length of the words
     * @return A word
     */
    private static String generateWord(int wordLength){
        Random rand = new Random();
        StringBuilder st = new StringBuilder();
        for(int i =0; i < wordLength; i++){
            st.append((char)(rand.nextInt(26)+'a'));
        }
        return st.toString();
    }
}
