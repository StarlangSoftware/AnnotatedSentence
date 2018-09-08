package AnnotatedSentence;

import Corpus.Corpus;
import DataStructure.CounterHashMap;
import Dictionary.*;
import MorphologicalAnalysis.*;
import MorphologicalDisambiguation.HmmDisambiguation;
import MorphologicalDisambiguation.RootWordStatistics;
import PropBank.Frameset;
import PropBank.FramesetList;
import WordNet.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AnnotatedCorpus extends Corpus{

    public AnnotatedCorpus(){
    }

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

    public AnnotatedCorpus(File folder){
        sentences = new ArrayList();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            AnnotatedSentence sentence = new AnnotatedSentence(file);
            sentences.add(sentence);
        }
    }

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

    public void shallowParseAnnotationControl(String fileName) throws FileNotFoundException {
        AnnotatedWord previous, word;
        String wordPhrase;
        int count;
        PrintWriter output = new PrintWriter(new File(fileName));
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            previous = (AnnotatedWord) sentence.getWord(0);
            wordPhrase = previous.getName();
            count = 0;
            for (int j = 1; j < sentence.wordCount(); j++){
                word = (AnnotatedWord) sentence.getWord(j);
                if (word.getShallowParse() != null && word.getShallowParse().equals(previous.getShallowParse())){
                    wordPhrase = wordPhrase + " " + word.getName();
                } else {
                    count++;
                    output.println(sentence.getFileName() + "\t" + count + "\t" + wordPhrase + "\t" + previous.getShallowParse() + "\t" + sentence.toWords());
                    previous = word;
                    wordPhrase = previous.getName();
                }
            }
            count++;
            output.println(sentence.getFileName() + "\t" + count + "\t" + wordPhrase + "\t" + previous.getShallowParse() + "\t" + sentence.toWords());
        }
        output.close();
    }

    public void nerAnnotationControl(String fileName) throws FileNotFoundException {
        AnnotatedWord previous, word;
        String wordPhrase;
        int count;
        PrintWriter output = new PrintWriter(new File(fileName));
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            previous = (AnnotatedWord) sentence.getWord(0);
            wordPhrase = previous.getName();
            count = 0;
            for (int j = 1; j < sentence.wordCount(); j++){
                word = (AnnotatedWord) sentence.getWord(j);
                if (word.getNamedEntityType() != null && word.getNamedEntityType().equals(previous.getNamedEntityType())){
                    wordPhrase = wordPhrase + " " + word.getName();
                } else {
                    count++;
                    output.println(sentence.getFileName() + "\t" + count + "\t" + wordPhrase + "\t" + previous.getNamedEntityType() + "\t" + sentence.toWords());
                    previous = word;
                    wordPhrase = previous.getName();
                }
            }
            count++;
            output.println(sentence.getFileName() + "\t" + count + "\t" + wordPhrase + "\t" + previous.getNamedEntityType() + "\t" + sentence.toWords());
        }
        output.close();
    }

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

    public void checkMorphologicalAnalysis(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            int count = 0;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getParse() == null){
                    count++;
                    if (count == 1){
                        System.out.println("Morphological Analysis does not exist for sentence " + sentence.getFileName());
                    }
                }
            }
        }
    }

    public void checkNer(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            int count = 0;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getNamedEntityType() == null){
                    count++;
                    if (count == 1){
                        System.out.println("NER annotation does not exist for sentence " + sentence.getFileName());
                    }
                }
            }
        }
    }

    public void checkShallowParse(){
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            int count = 0;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getShallowParse() == null){
                    count++;
                    if (count == 1){
                        System.out.println("Shallow Parse annotation does not exist for sentence " + sentence.getFileName());
                    }
                }
            }
        }
    }

    public void checkSemantic(){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        WordNet turkish = new WordNet();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getSemantic() == null){
                    System.out.println("File:" + sentence.getFileName() + " Semantic annotation does not exist for word " + word.getName());
                } else {
                    try {
                        ArrayList<SynSet> synSets = sentence.constructSynSets(turkish, fsm, j);
                        if (turkish.getSynSetWithId(word.getSemantic()) == null || !synSets.contains(turkish.getSynSetWithId(word.getSemantic()))){
                            System.out.println("File:" + sentence.getFileName() + " Semantic annotation is not correct for word " + word.getName());
                        }
                    } catch (ParseRequiredException parseRequired) {
                    }
                }
            }
        }
    }

    public void checkPredicate(){
        WordNet turkish = new WordNet();
        FramesetList framesetList = new FramesetList("frameset.xml");
        TxtDictionary dictionary = new TxtDictionary("Data/Dictionary/turkish_dictionary.txt", new TurkishWordComparator());
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                    if (word.getParse() == null){
                        System.out.println("Morphological Analysis for Predicate " + word.getName() + " does not exist in file " + sentence.getFileName());
                    } else {
                        if (word.getSemantic() == null){
                            System.out.println("Semantic Annotation for Predicate " + word.getName() + " does not exist in file " + sentence.getFileName());
                        } else {
                            SynSet synSet = turkish.getSynSetWithId(word.getSemantic());
                            if (!framesetList.frameExists(word.getSemantic()) && synSet != null && synSet.getPos() != null && synSet.getPos().equals(Pos.VERB)){
                                System.out.println("Frameset for Predicate " + word.getSemantic() + " does not exist in file " + sentence.getFileName());
                                Frameset frameset = new Frameset(word.getSemantic());
                                String infinitiveForm = synSet.representative();
                                String[] words = infinitiveForm.split(" ");
                                String verbForm = words[words.length - 1];
                                String prefix = "";
                                switch (words.length){
                                    case 2:
                                        prefix = words[0] + " ";
                                        break;
                                    case 3:
                                        prefix = words[0] + " " + words[1] + " ";
                                        break;
                                }
                                TxtWord verbRoot = (TxtWord) dictionary.getWord(verbForm.substring(0, verbForm.length() - 3));
                                if (verbRoot == null){
                                    verbRoot = new TxtWord(verbForm.substring(0, verbForm.length() - 3));
                                    verbRoot.addFlag("CL_FIIL");
                                }
                                switch (verbRoot.verbType()){
                                    case "F4PW":
                                    case "F4PW-NO-REF":
                                        frameset.addArgument("ARG1", prefix + new Transition("nAn").makeTransition(verbRoot, verbRoot.getName()) + " şey");
                                        break;
                                    default:
                                        frameset.addArgument("ARG0", prefix + new Transition("yAn").makeTransition(verbRoot, verbRoot.getName()));
                                        frameset.addArgument("ARG1", prefix + new Transition("nHlAn").makeTransition(verbRoot, verbRoot.getName()) + " şey");
                                        break;
                                }
                                framesetList.addFrameset(frameset);
                            }
                        }
                    }
                }
            }
        }
        framesetList.saveAsXml(null, null, null, -1);
    }

    public RootWordStatistics extractRootWordStatistics(FsmMorphologicalAnalyzer fsm){
        RootWordStatistics statistics = new RootWordStatistics();
        CounterHashMap<String> rootWordStatistics;
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
                            } else {
                                rootWordStatistics = statistics.get(rootWords);
                            }
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
