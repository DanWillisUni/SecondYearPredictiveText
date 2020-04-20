package DSA2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        DictionaryFinder df=new DictionaryFinder();
        ArrayList<String> in= new ArrayList<>();
        try {
            in = DictionaryFinder.readWordsFromCSV("TextFiles\\lotr.csv");//reads from lotr into the arraylist in
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String,Number> map = df.formDictionary(in); //form a dictionary with frequencies from the text file
        df.saveToFile(map,"TextFiles\\Results\\myGollem.csv");//save the dictionary with frequencies
        ArrayList<String> prefixes = new ArrayList<>();
        try {
            prefixes = DictionaryFinder.readWordsFromCSV("TextFiles\\lotrQueries.csv");//read in the prefixes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String pre:prefixes){//for each prefix
            System.out.println(pre);
//            AutoCompletionTrie ACT = new AutoCompletionTrie(map,pre);//create new object
//            HashMap<String,Number> pro = ACT.getTopThreeProbability();//get top three probabilities
            //DictionaryFinder.saveToFile(pro,"TextFiles\\Results\\mylotrMatches.csv");//save the probabilities to the file
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
