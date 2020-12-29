package AnnotatedSentence;

import Corpus.Corpus;
import DataStructure.CounterHashMap;
import Dictionary.*;
import MorphologicalAnalysis.*;
import MorphologicalDisambiguation.RootWordStatistics;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AnnotatedCorpus extends Corpus{

    /**
     * Empty constructor for {@link AnnotatedCorpus}.
     */
    public AnnotatedCorpus(){
    }

    /**
     * A constructor of {@link AnnotatedCorpus} class which reads all {@link AnnotatedSentence} files with the file
     * name satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates an AnnotatedSentence and puts in inside the list parseTrees.
     * @param folder Folder where all sentences reside.
     * @param pattern File pattern such as "." ".train" ".test".
     */
    public AnnotatedCorpus(File folder, String pattern){
        sentences = new ArrayList();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            String fileName = file.getName();
            if (!fileName.contains(pattern))
                continue;
            AnnotatedSentence sentence = new AnnotatedSentence(file);
            sentences.add(sentence);
        }
    }

    /**
     * A constructor of {@link AnnotatedCorpus} class which reads all {@link AnnotatedSentence} files inside the given
     * folder. For each file inside that folder, the constructor creates an AnnotatedSentence and puts in inside the
     * list sentences.
     * @param folder Folder to load annotated courpus
     */
    public AnnotatedCorpus(File folder){
        sentences = new ArrayList();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            if (!file.isHidden()){
                AnnotatedSentence sentence = new AnnotatedSentence(file);
                sentences.add(sentence);
            }
        }
    }

    /**
     * An obsolete constructor of the {@link AnnotatedSentence} class. If the contents of all the sentences are inside
     * a single file, this constructor can be called. Each line in this file corresponds to a single AnnotatedSentence.
     * @param fileName File containing the sentences.
     */
    public AnnotatedCorpus(String fileName){
        this();
        this.fileName = fileName;
        int i = 0;
        String line;
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null){
                addSentence(new AnnotatedSentence(line));
                line = br.readLine();
                i++;
                if (i % 10000 == 0){
                    System.out.println("Read " + i + " sentences");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportUniversalDependencyFormat(String path, String outputFileName){
        try {
            PrintWriter output = new PrintWriter(outputFileName);
            for (int i = 0; i < sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
                output.write(sentence.getUniversalDependencyFormat(path));
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportUniversalDependencyFormat(String outputFileName){
        try {
            PrintWriter output = new PrintWriter(outputFileName);
            for (int i = 0; i < sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
                output.write(sentence.getUniversalDependencyFormat());
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method removes all empty words from the sentences.
     */
    public void clearNullWords(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            boolean changed = false;
            for (int j = 0; j < sentence.wordCount(); j++){
                if (sentence.getWord(j).getName() == null || sentence.getWord(j).getName().length() == 0){
                    sentence.removeWord(j);
                    j--;
                    changed = true;
                }
            }
            if (changed){
                sentence.save();
            }
        }
    }

    /**
     * The method traverses all words in all sentences and prints the words which do not have a morphological analysis.
     */
    public void checkMorphologicalAnalysis(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getParse() == null){
                    System.out.println("Morphological Analysis does not exist for sentence " + sentence.getFileName());
                    break;
                }
            }
        }
    }

    /**
     * The method traverses all words in all sentences and prints the words which do not have named entity annotation.
     */
    public void checkNer(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getNamedEntityType() == null){
                    System.out.println("NER annotation does not exist for sentence " + sentence.getFileName());
                    break;
                }
            }
        }
    }

    /**
     * The method traverses all words in all sentences and prints the words which do not have shallow parse annotation.
     */
    public void checkShallowParse(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getShallowParse() == null){
                    System.out.println("Shallow Parse annotation does not exist for sentence " + sentence.getFileName());
                    break;
                }
            }
        }
    }

    /**
     * The method traverses all words in all sentences and prints the words which do not have sense annotation.
     */
    public void checkSemantic(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getSemantic() == null){
                    System.out.println("Semantic annotation does not exist for sentence " + sentence.getFileName());
                    break;
                }
            }
        }
    }

    /**
     * Creates a dictionary from the morphologically annotated words.
     * @return A dictionary containing root forms of the morphological annotations of words.
     */
    public TxtDictionary createDictionary() {
        TxtDictionary dictionary = new TxtDictionary(new TurkishWordComparator());
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getParse() != null){
                    MorphologicalParse morphologicalParse = word.getParse();
                    String pos = morphologicalParse.getRootPos();
                    String name = morphologicalParse.getWord().getName();
                    switch (pos){
                        case "NOUN":
                            if (morphologicalParse.isProperNoun()){
                                dictionary.addProperNoun(name);
                            } else {
                                dictionary.addNoun(name);
                            }
                            break;
                        case "VERB":
                            dictionary.addVerb(name);
                            break;
                        case "ADJ":
                            dictionary.addAdjective(name);
                            break;
                        case "ADV":
                            dictionary.addAdverb(name);
                            break;
                    }
                }
            }
        }
        return dictionary;
    }

    public RootWordStatistics extractRootWordStatistics(FsmMorphologicalAnalyzer fsm){
        RootWordStatistics statistics = new RootWordStatistics();
        HashMap<String, ArrayList<String>> rootWordFiles = new HashMap<>();
        CounterHashMap<String> rootWordStatistics;
        ArrayList<String> fileNames;
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getName() != null){
                    FsmParseList parseList = fsm.morphologicalAnalysis(word.getName());
                    MorphologicalParse parse = word.getParse();
                    if (parseList.size() > 0 && parse != null){
                        String rootWords = parseList.rootWords();
                        if (rootWords.contains("$")){
                            if (!statistics.containsKey(rootWords)){
                                rootWordStatistics = new CounterHashMap<>();
                                fileNames = new ArrayList<>();
                            } else {
                                rootWordStatistics = statistics.get(rootWords);
                                fileNames = rootWordFiles.get(rootWords);
                            }
                            fileNames.add(sentence.getFileName());
                            rootWordFiles.put(rootWords, fileNames);
                            rootWordStatistics.put(parse.getWord().getName());
                            statistics.put(rootWords, rootWordStatistics);
                        }
                    }
                }
            }
        }
        return statistics;
    }

}
