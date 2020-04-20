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
     *
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
     *
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
     * Sorts all the words into alphabetical order
     * Keeps track of the previous word
     * When the current word is different to the previous word put the previous word into the map with the frequency
     *
     * @param in all the words in
     * @return each word, frequencies of each word in alphabetical order
     */
    public HashMap<String,Integer> formDictionary(ArrayList<String> in){
        in.sort(String::compareTo);//sorts all the words into alphabetical order
        HashMap<String, Integer> map = new HashMap<>();
        String prev = "";//keeps track of the previous word
        int frequency = 0;//sets the frequency to 0 to start
        for (String word: in){//for each word in the array
            if (word == prev){//if it is the same as the last, and therefore will already be added to the dictionary
                frequency+=1;//add one to the frequency that will be added with it
            } else {//the word has changed and being as the words are sorted alphabetically the previous word wont appear again in the list
                if (frequency!=0){//this excludes the first time when the frequency is 0 and the prev is null
                    map.put(prev,frequency);//puts the previous word in the map because it wont appear again
                }
                frequency = 1;//sets the frequency back to 1 being as it is a new word
            }
            prev = word;//set the previous word to the current word for the next word
        }
        map.put(prev,frequency);//after all the words are done the last word still isnt added as it only adds when the word changes therefore this line adds the last word alphabetically to the map
        return map;
    }
    /**
     * 4. write the words and associated frequency to file.
     *
     * String builds the string to write to the file with a new line for each word
     * Writes the string to the file specified
     * @param map the dictionary with frequencies
     * @param fileToWriteTo the file to save to
     */
    public void saveToFile(HashMap<String,Integer> map,String fileToWriteTo){
        StringBuilder str = new StringBuilder();
        for (String key : map.keySet()) {//goes through the map
            str.append(key).append(",").append(map.get(key)).append("\n");//builds the string
        }
        try {//tries to write the string to the file
            FileWriter fileWriter = new FileWriter(fileToWriteTo);
            fileWriter.write(str.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test Harness
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DictionaryFinder df=new DictionaryFinder();
        //ArrayList<String> in=readWordsFromCSV("C:\\Teaching\\2017-2018\\Data Structures and Algorithms\\Coursework 2\\test.txt");
        ArrayList<String> in=readWordsFromCSV("TextFiles\\testDocument.csv");//added to read in correct words
        HashMap<String,Integer> map = df.formDictionary(in);
        df.saveToFile(map,"TextFiles\\Results\\mytestDictionary.csv");
    }
}