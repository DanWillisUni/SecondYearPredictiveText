package DSA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        String prev = "";
        int count = 0;
        for (int i = 0; i<in.size();i++){
            if(prev.equals(in.get(i))){
                count+=1;
                //in.remove(i);
            } else {
                if (count !=0){
                    map.put(in.get(i-1),count);
                }
                count = 1;
                prev=in.get(i);
            }
        }
        map.put(in.get(in.size()-1),count);
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
            //System.out.println(key+ "=" + map.get(key));
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
     * timing experiments
     * @throws IOException
     */
    private static void timingExperiments() throws IOException, InterruptedException {
        DictionaryFinder df=new DictionaryFinder();
        ArrayList<Integer> time = new ArrayList<>();
        TimeUnit.SECONDS.sleep(2);
        for (int i = 1000;i<=100000;i+=1000){
            long duration = 0;
            for (int n = 0;n<5;n++){
                ArrayList<String> in = Main.generateDoc(i,5);
                long startTime = System.nanoTime();
                df.formDictionary(in);
                long endTime = System.nanoTime();
                duration = duration + (endTime - startTime);
            }
            time.add((int)duration/5);
        }
        saveCollectionToFile(time,"ChangingNumberOfWords.csv");
        ArrayList<Integer> timeTwo = new ArrayList<>();
        for (int i = 1;i<=1000;i++){
            long duration = 0;
            for (int n = 0;n<5;n++){
                ArrayList<String> in = Main.generateDoc(100,i);
                long startTime = System.nanoTime();
                df.formDictionary(in);
                long endTime = System.nanoTime();
                duration = duration + (endTime - startTime);
            }
            timeTwo.add((int)duration/5);
        }
        saveCollectionToFile(timeTwo,"ChangingWordLength.csv");
        System.out.println("Done");
    }
    /**
     * Test harness
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        DictionaryFinder df=new DictionaryFinder();
//        //ArrayList<String> in=readWordsFromCSV("C:\\Teaching\\2017-2018\\Data Structures and Algorithms\\Coursework 2\\test.txt");
//        ArrayList<String> in=readWordsFromCSV("C:\\Users\\danny\\OneDrive\\Documents\\~Work\\DSA\\TextFiles\\testDocument.csv");
//        LinkedHashMap<String,Integer> map = df.formDictionary(in);
//        df.saveToFile(map,"C:\\Users\\danny\\OneDrive\\Documents\\~Work\\DSA\\TextFiles\\mytestDictionary.csv");
        timingExperiments();
    }
}
