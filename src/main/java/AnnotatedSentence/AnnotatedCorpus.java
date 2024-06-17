package AnnotatedSentence;

import Corpus.Corpus;
import DataStructure.CounterHashMap;
import DependencyParser.ParserEvaluationScore;
import Dictionary.*;
import MorphologicalAnalysis.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        sentences = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                String fileName = file.getName();
                if (!fileName.contains(pattern))
                    continue;
                AnnotatedSentence sentence = new AnnotatedSentence(file);
                sentences.add(sentence);
            }
        }
    }

    /**
     * A constructor of {@link AnnotatedCorpus} class which reads all {@link AnnotatedSentence} files with the file
     * name satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates an AnnotatedSentence and puts in inside the list parseTrees.
     * @param folder Folder where all sentences reside.
     * @param pattern File pattern such as "." ".train" ".test".
     * @param from Index (such as 23 or 100) from which the files are read.
     * @param to Index (such as 23 or 100) to which the files are read.
     */
    public AnnotatedCorpus(File folder, String pattern, int from, int to){
        sentences = new ArrayList<>();
        for (int i = from; i <= to; i++){
            sentences.add(new AnnotatedSentence(new File(folder.getAbsolutePath() + "/" + String.format("%04d", i) + pattern)));
        }
    }

    /**
     * A constructor of {@link AnnotatedCorpus} class which reads all {@link AnnotatedSentence} files inside the given
     * folder. For each file inside that folder, the constructor creates an AnnotatedSentence and puts in inside the
     * list sentences.
     * @param folder Folder to load annotated courpus
     */
    public AnnotatedCorpus(File folder){
        sentences = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                if (!file.isHidden()){
                    AnnotatedSentence sentence = new AnnotatedSentence(file);
                    sentences.add(sentence);
                }
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
        } catch (IOException ignored) {
        }
    }

    /**
     * Compares the corpus with the given corpus and returns a parser evaluation score for this comparison. The result
     * is calculated by summing up the parser evaluation scores of sentence by sentence dependency relation comparisons.
     * @param corpus Corpus to be compared.
     * @return A parser evaluation score object.
     */
    public ParserEvaluationScore compareParses(AnnotatedCorpus corpus){
        ParserEvaluationScore result = new ParserEvaluationScore();
        for (int i = 0; i < sentences.size(); i++){
            AnnotatedSentence sentence1 = (AnnotatedSentence) sentences.get(i);
            AnnotatedSentence sentence2 = (AnnotatedSentence) corpus.getSentence(i);
            result.add(sentence1.compareParses(sentence2));
        }
        return result;
    }

    /**
     * Calculates inter-annotator agreement between two sets of annotations in two distinct corpora. For each common
     * file in the corpora, the annotation of words in the parallel sentences are compared, and the number of agreements
     * and the total number of words are calculated. The ratio gives you the percent agreement.
     * @param corpus Corpus with which annotator agreement will be calculated.
     * @param layerType Annotation layer for which inter-annotator agreement will be calculated.
     * @return Inter-annotator agreement in percent.
     */
    public double percentAgreement(AnnotatedCorpus corpus, ViewLayerType layerType){
        int i = 0, j = 0, agreeCount = 0, total = 0;
        while (i < sentences.size() && j < corpus.sentences.size()){
            AnnotatedSentence sentence1 = (AnnotatedSentence) sentences.get(i);
            AnnotatedSentence sentence2 = (AnnotatedSentence) corpus.getSentence(j);
            if (sentence1.getFileName().compareTo(sentence2.getFileName()) < 0){
                i++;
            } else {
                if (sentence1.getFileName().compareTo(sentence2.getFileName()) > 0){
                    j++;
                } else {
                    for (int k = 0; k < sentence1.wordCount(); k++){
                        AnnotatedWord word1 = (AnnotatedWord) sentence1.getWord(k);
                        AnnotatedWord word2 = (AnnotatedWord) sentence2.getWord(k);
                        String layer1 = word1.getLayerInfo(layerType);
                        String layer2 = word2.getLayerInfo(layerType);
                        if (layer1 != null && layer2 != null){
                            if (layer1.contains(layer2) || layer2.contains(layer1)){
                                agreeCount++;
                            }
                            total++;
                        }
                    }
                    i++;
                    j++;
                }
            }
        }
        return agreeCount / (total + 0.0);
    }

    /**
     * Exports the annotated corpus as a sequence labeling dataset. The label type and labels are determined according
     * to the viewLayerType in each word. Every sentence is processed and each word in the sentence is converted to a
     * labeled instance.
     * @param outputFileName Output file name for the sequence labeling dataset.
     * @param layerType Annotation layer for which sequence labels are determined.
     */
    public void exportSequenceDataSet(String outputFileName, ViewLayerType layerType){
        try {
            PrintWriter output = new PrintWriter(new FileWriter(outputFileName, true));
            for (int i = 0; i < sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
                if (sentence.wordCount() > 0){
                    output.append(sentence.exportSequenceDataSet(layerType));
                }
            }
            output.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Exports the annotated corpus as a UD file in connlu format. Every sentence is converted into connlu format and
     * appended to the output file. Multiple paths are possible in the annotated corpus. This method outputs the
     * sentences in the given path.
     * @param outputFileName Output file name in connlu format.
     * @param path Current path for the part of the annotated corpus.
     */
    public void exportUniversalDependencyFormat(String outputFileName, String path){
        try {
            PrintWriter output = new PrintWriter(new FileWriter(outputFileName, true));
            for (int i = 0; i < sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
                if (sentence.wordCount() > 0){
                    output.append(sentence.getUniversalDependencyFormat(path));
                }
            }
            output.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Exports the annotated corpus as a UD file in connlu format. Every sentence is converted into connlu format and
     * appended to the output file.
     * @param outputFileName Output file name in connlu format.
     */
    public void exportUniversalDependencyFormat(String outputFileName){
        try {
            PrintWriter output = new PrintWriter(outputFileName);
            for (int i = 0; i < sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
                if (sentence.wordCount() > 0){
                    output.write(sentence.getUniversalDependencyFormat());
                }
            }
            output.close();
        } catch (FileNotFoundException ignored) {
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
                if (sentence.getWord(j).getName() == null || sentence.getWord(j).getName().isEmpty()){
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

    /**
     * Creates a list of distinct dependencies annotated in the corpus, then returns them with number of annotations
     * per each.
     * @return List of map entries, where each entry is a dependency and its number of annotations in the corpus.
     */
    public List<Map.Entry<String, Integer>> topDependencyAnnotations(){
        CounterHashMap<String> dependencies = new CounterHashMap<>();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (((AnnotatedWord)sentence.getWord(j)).getUniversalDependency() != null){
                    dependencies.put(((AnnotatedWord)sentence.getWord(j)).getUniversalDependency().toString());
                }
            }
        }
        return dependencies.topN(dependencies.size());
    }

    /**
     * Returns number of distinct surface forms in the corpus.
     * @return Number of distinct surface forms
     */
    public int uniqueSurfaceForms(){
        CounterHashMap<String> surfaceForms = new CounterHashMap<>();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (sentence.getWord(j).getName() != null){
                    surfaceForms.put(sentence.getWord(j).getName());
                }
            }
        }
        return surfaceForms.keySet().size();
    }

    /**
     * Returns number of words with morphological analysis except punctuations.
     * @return Number of words with morphological analysis except punctuations.
     */
    public int tokenCountExceptPunctuations(){
        int count = 0;
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (((AnnotatedWord)sentence.getWord(j)).getParse() != null && !((AnnotatedWord)sentence.getWord(j)).getParse().isPunctuation()){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns a list of N most frequent distinct surface forms with their counts.
     * @return A list of N most frequent distinct surface forms with their counts.
     */
    public List<Map.Entry<String, Integer>> topSurfaceForms(int N){
        CounterHashMap<String> surfaceForms = new CounterHashMap<>();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (sentence.getWord(j).getName() != null){
                    surfaceForms.put(sentence.getWord(j).getName());
                }
            }
        }
        return surfaceForms.topN(N);
    }

    /**
     * Returns a list of N most frequent distinct root forms with their counts.
     * @return A list of N most frequent distinct root forms with their counts.
     */
    public List<Map.Entry<String, Integer>> topRootForms(int N){
        CounterHashMap<String> rootForms = new CounterHashMap<>();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (((AnnotatedWord)sentence.getWord(j)).getParse() != null){
                    rootForms.put(((AnnotatedWord)sentence.getWord(j)).getParse().getWord().getName());
                }
            }
        }
        return rootForms.topN(N);
    }

    /**
     * Creates a list of distinct pos tags annotated in the corpus, then returns them with number of annotations per
     * each.
     * @return List of map entries, where each entry is a pos tag and its number of annotations in the corpus.
     */
    public List<Map.Entry<String, Integer>> rootPosTags(){
        CounterHashMap<String> rootPos = new CounterHashMap<>();
        for (int i = 0; i < sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (((AnnotatedWord)sentence.getWord(j)).getParse() != null && ((AnnotatedWord)sentence.getWord(j)).getUniversalDependency() != null && ((AnnotatedWord)sentence.getWord(j)).getUniversalDependency().toString().equals("ROOT")){
                    rootPos.put(((AnnotatedWord)sentence.getWord(j)).getParse().getRootPos());
                }
            }
        }
        return rootPos.topN(rootPos.size());
    }

}
