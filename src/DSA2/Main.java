package DSA2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //1. form a dictionary file of words and counts from the file lotr.csv
        DictionaryFinder df=new DictionaryFinder();
        ArrayList<String> in= new ArrayList<>();
        try {
            in = DictionaryFinder.readWordsFromCSV("TextFiles\\lotr.csv");//reads from lotr into the arraylist in
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String,Number> wordsAndFreq = df.formDictionary(in); //form a dictionary with frequencies from the text file
        df.saveToFile(wordsAndFreq,"TextFiles\\Results\\myGollem.csv");//save the dictionary with frequencies
        //2. construct a trie from the dictionary using your solution from part 2
        Trie t = new Trie();
        for (String word:in){
            t.add(word);
        }
        //3. load the prefixs from lotrQueries.csv
        ArrayList<String> prefixLine = new ArrayList<>();
        try {
            prefixLine = DictionaryFinder.readWordsFromCSV("TextFiles\\lotrQueries.csv");//read in the prefixes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String prefixes[] = prefixLine.get(0).split("\\r?\\n");
        //4. for each prefix query
        for (String pre:prefixes){//for each prefix
            System.out.println("For: " + pre);
            Trie subT = t.getSubTrie(pre);
            subT.getAllWords();//4.1. Recover all words matching the prefix from the trie.
            AutoCompletionTrie ACT = new AutoCompletionTrie(wordsAndFreq);//create new object
            HashMap<String,Number> pro = ACT.getTopThreeProbability(pre);//4.2. Choose the three most frequent words and display to standard output.
            DictionaryFinder.saveToFile(pro,"TextFiles\\Results\\mylotrMatches.csv");//4.3. Write the results to lotrMatches.csv
        }
    }
    /**
     * Generates a number of words of a set length
     *
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
     * Generate a word of a given length by randomly generating letters
     *
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
