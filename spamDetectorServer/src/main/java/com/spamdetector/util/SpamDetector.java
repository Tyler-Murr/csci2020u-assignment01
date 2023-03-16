package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
//        TODO: main method of loading the directories and files, training and testing the model
        //training
        Map<String, Integer> trainHamFreq = new TreeMap<>();
        Map<String, Integer> trainSpamFreq = new TreeMap<>();
        Map<String, Double> probInHam = new TreeMap<>();
        Map<String, Double> probInSpam = new TreeMap<>();
        Map<String, Double> probOfSpam = new TreeMap<>();

        File trainHamFolder = new File(mainDirectory,"/train/ham");
        File trainSpamFolder = new File(mainDirectory,"/train/spam");
        trainHamFreq = wordFrequencyDir(trainHamFolder);
        trainSpamFreq = wordFrequencyDir(trainSpamFolder);

        ArrayList<TestFile> testFiles = new ArrayList<>();

        //Pr(W|S)
        for(String key : trainSpamFreq.keySet()){
            probInSpam.put(key, (double) (trainSpamFreq.get(key)/trainSpamFolder.listFiles().length));
        }
        //Pr(W|H)
        for(String key : trainHamFreq.keySet()){
            probInHam.put(key, (double) (trainHamFreq.get(key)/trainHamFolder.listFiles().length));
        }

        //P(S|W)
        for(String key : probInSpam.keySet()){

            double value;
            if( !probInHam.containsKey(key)){
                value = probInSpam.get(key)/(probInSpam.get(key));
            }
            else {
                value = probInSpam.get(key)/(probInSpam.get(key)+probInHam.get(key));
            }

            probOfSpam.put(key,value);
        }


        //iterate over each file in spam and count their words
        File testSpamFolder = new File(mainDirectory,"/test/spam");
        File testHamFolder = new File(mainDirectory,"/test/ham");
        //Pr(S|F) and create test file to add to list
        for(File file : testSpamFolder.listFiles()){
            Map<String,Integer> wordsInFile = wordFrequencyDir(file);
            double constant = 0;
            for (String key: wordsInFile.keySet()){
                constant += Math.log(1-probOfSpam.get(key))  - Math.log(probOfSpam.get(key));
            }
            double fileSpamProb = 1/(1+Math.exp(constant));
            testFiles.add(new TestFile(file.getName(), fileSpamProb,"spam"));
        }
        for(File file : testHamFolder.listFiles()){
            Map<String,Integer> wordsInFile = wordFrequencyDir(file);
            double constant = 0;
            for (String key: wordsInFile.keySet()){
                constant += Math.log(1-probOfSpam.get(key))  - Math.log(probOfSpam.get(key));
            }
            double fileSpamProb = 1/(1+Math.exp(constant));
            testFiles.add(new TestFile(file.getName(), fileSpamProb,"Ham"));
        }




        return testFiles;
    }


    // calculate word frequency of directory's
    public Map<String, Integer> wordFrequencyDir(File dir) throws IOException {
        Map<String, Integer> frequencies = new TreeMap<>();

        File[] filesInDir = dir.listFiles();
        int numFiles = filesInDir.length;

        // iterate over each file in the dir and count their words
        for (int i = 0; i<numFiles; i++){
            Map<String, Integer> wordMap = countWordFile(filesInDir[i]);

            // merge the file wordMap into the global frequencies
            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();
            while (keyIterator.hasNext()){
                String word  = keyIterator.next();
                int count = wordMap.get(word);

                if(frequencies.containsKey(word)){
                    // increment
                    int oldCount = frequencies.get(word);
                    frequencies.put(word, count + oldCount);
                }
                else{
                    frequencies.put(word, count);
                }
            }

        }

        return frequencies;
    }
    private Map<String, Integer> countWordFile(File file) throws IOException {
        Map<String, Integer> wordMap = new TreeMap<>();
        if(file.exists()){
            // load all the data and process it into words
            Scanner scanner  = new Scanner(file);
            while(scanner.hasNext()){
                // ignore the casing for words
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)){
                    // add the word if not exists yet
                    if(!wordMap.containsKey(word)){
                        wordMap.put(word, 1);
                    }

                }
            }
        }
        return wordMap;
    }


    private Boolean isWord(String word){
        if (word == null){
            return false;
        }

        String pattern = "^[a-zA-Z]*$";
        if(word.matches(pattern)){
            return true;
        }

        return false;

    }

}

