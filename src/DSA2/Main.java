package DSA2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Task");
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
        AutoCompletionTrie t = new AutoCompletionTrie(wordsAndFreq);
        //3. load the prefixs from lotrQueries.csv
        ArrayList<String> prefixLine = new ArrayList<>();
        try {
            prefixLine = DictionaryFinder.readWordsFromCSV("TextFiles\\lotrQueries.csv");//read in the prefixes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String prefixes[] = prefixLine.get(0).split("\\r?\\n");
        //4. for each prefix query
        LinkedHashMap<String,Number> pro = new LinkedHashMap<>();
        for (String pre:prefixes){//for each prefix
            System.out.println("For: " + pre);
            AutoCompletionTrie subT = t.getSubTrie(pre);
            subT.getAllWords();//4.1. Recover all words matching the prefix from the trie.
            AutoCompletionTrie ACT = new AutoCompletionTrie(wordsAndFreq);//create new object
            pro.putAll(ACT.getTopThreeProbability(pre));//4.2. Choose the three most frequent words and display to standard output.
            DictionaryFinder.saveToFile(pro,"TextFiles\\Results\\mylotrMatches.csv");//4.3. Write the results to lotrMatches.csv
        }
    }
}
