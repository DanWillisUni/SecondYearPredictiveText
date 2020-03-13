package DSA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DictionaryFinder {
    /**
     * Reads all the words in a comma separated text document into an Array
     * @param file
     */
    public static ArrayList<String> readWordsFromCSV(String file) throws FileNotFoundException {
        Scanner sc=new Scanner(new File(file));
        sc.useDelimiter(" |,");
        ArrayList<String> words=new ArrayList<>();
        String str;
        while(sc.hasNext()){
            str=sc.next();
            str=str.trim();
            str=str.toLowerCase();
            words.add(str);
        }
        return words;
    }
    public static void saveCollectionToFile(Collection<?> c,String file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(Object w: c){
            printWriter.println(w.toString());
        }
        printWriter.close();
    }
    /**
     * Sorts the words into alphabetical order
     * if there is a repeat add it to the
     * @param in all the words in total
     * @return each word, frequencies of each word
     */
    public LinkedHashMap<String,Integer> formDictionary(ArrayList<String> in){
        in.sort(String::compareTo);
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (String word: in){
           if(map.containsKey(word)){
               map.put(word,map.get(word)+1);
           } else {
               map.put(word,1);
           }
        }
        return map;
    }
    /**
     * Saves the dictionary and frequencies to a file
     * @param map the dictionary with frequencies
     * @param fileToWriteTo the file to save to
     */
    public void saveToFile(LinkedHashMap<String,Integer> map,String fileToWriteTo){
        StringBuilder str = new StringBuilder();
        for (String key : map.keySet()) {
            str.append(key).append(",").append(map.get(key)).append("\n");
        }
        try {
            FileWriter fileWriter = new FileWriter(fileToWriteTo);
            fileWriter.write(str.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test harness
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DictionaryFinder df=new DictionaryFinder();
        //ArrayList<String> in=readWordsFromCSV("C:\\Teaching\\2017-2018\\Data Structures and Algorithms\\Coursework 2\\test.txt");
        ArrayList<String> in=readWordsFromCSV("TextFiles\\testDocument.csv");
        LinkedHashMap<String,Integer> map = df.formDictionary(in);
        df.saveToFile(map,"TextFiles\\Results\\mytestDictionary.csv");
    }
}
