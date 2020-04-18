package DSA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class DictionaryFinder {
    
    public DictionaryFinder(){
    }
    /**
     * 1. read text document into a list of strings;
     *
     * Given
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
    /**
     * Given
     * @param c
     * @param file
     * @throws IOException
     */
    public static void saveCollectionToFile(Collection<?> c,String file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(Object w: c){
            printWriter.println(w.toString());
        }
        printWriter.close();
    }

    /**
     * 2. form a set of words that exist in the document and count the number of times each word
     * occurs in a method called FormDictionary;
     * 3. sort the words alphabetically;
     *
     * If there is a repeat add it to the current frequency count
     * @param in all the words in total
     * @return each word, frequencies of each word
     */
    public HashMap<String,Integer> formDictionary(ArrayList<String> in){
        in.sort(String::compareTo);//sorts all the words into alphabetical order
        HashMap<String, Integer> map = new HashMap<>();
        String prev = "";//keeps track of the previous word
        int count = 0;
        for (String word: in){
            if (word == prev){
                count+=1;
            } else {
                map.put(prev,count);
                count = 1;
            }
            prev = word;
        }
        map.put(prev,count);
        return map;
    }
    /**
     * 4. write the words and associated frequency to file.
     *
     * Saves the dictionary and frequencies to a file
     * @param map the dictionary with frequencies
     * @param fileToWriteTo the file to save to
     */
    public void saveToFile(HashMap<String,Integer> map,String fileToWriteTo){
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

    public static void main(String[] args) throws Exception {
        DictionaryFinder df=new DictionaryFinder();
        //ArrayList<String> in=readWordsFromCSV("C:\\Teaching\\2017-2018\\Data Structures and Algorithms\\Coursework 2\\test.txt");
        ArrayList<String> in=readWordsFromCSV("TextFiles\\testDocument.csv");//added to read in correct words
        HashMap<String,Integer> map = df.formDictionary(in);
        df.saveToFile(map,"TextFiles\\Results\\mytestDictionary.csv");
    }
}