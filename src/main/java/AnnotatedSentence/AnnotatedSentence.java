package AnnotatedSentence;

import Corpus.Sentence;
import Dictionary.Word;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.MetamorphicParse;
import MorphologicalAnalysis.MorphologicalParse;
import PropBank.FramesetList;
import WordNet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AnnotatedSentence extends Sentence{
    private File file;

    /**
     * Empty constructor for the {@link AnnotatedSentence} class.
     */
    public AnnotatedSentence(){
    }

    /**
     * Reads an annotated sentence from a text file.
     * @param file File containing the annotated sentence.
     */
    public AnnotatedSentence(File file){
        this.file = file;
        words = new ArrayList<Word>();
        try {
            Scanner sc = new Scanner(file, "UTF8");
            while (sc.hasNext()){
                words.add(new AnnotatedWord(sc.next()));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a simple sentence to an annotated sentence
     * @param sentence Simple sentence
     */
    public AnnotatedSentence(String sentence){
        String[] wordArray;
        words = new ArrayList<>();
        wordArray = sentence.split(" ");
        for (String word : wordArray){
            if (!word.isEmpty()){
                words.add(new AnnotatedWord(word));
            }
        }
    }

    /**
     * The method constructs all possible shallow parse groups of a sentence.
     * @return Shallow parse groups of a sentence.
     */
    public ArrayList<AnnotatedPhrase> getShallowParseGroups(){
        ArrayList<AnnotatedPhrase> shallowParseGroups = new ArrayList<>();
        AnnotatedWord previousWord = null;
        AnnotatedPhrase current = null;
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord annotatedWord = (AnnotatedWord) getWord(i);
            if (previousWord == null){
                current = new AnnotatedPhrase(i, annotatedWord.getShallowParse());
            } else {
                if (previousWord.getShallowParse() != null && !previousWord.getShallowParse().equals(annotatedWord.getShallowParse())){
                    shallowParseGroups.add(current);
                    current = new AnnotatedPhrase(i, annotatedWord.getShallowParse());
                }
            }
            current.addWord(annotatedWord);
            previousWord = annotatedWord;
        }
        shallowParseGroups.add(current);
        return shallowParseGroups;
    }

    /**
     * The method checks all words in the sentence and returns true if at least one of the words is annotated with
     * PREDICATE tag.
     * @return True if at least one of the words is annotated with PREDICATE tag; false otherwise.
     */
    public boolean containsPredicate(){
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgument() != null && annotatedWord.getArgument().getArgumentType().equals("PREDICATE")){
                return true;
            }
        }
        return false;
    }

    public boolean updateConnectedPredicate(String previousId, String currentId){
        boolean modified = false;
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgument() != null && annotatedWord.getArgument().getId() != null && annotatedWord.getArgument().getId().equals(previousId)){
                annotatedWord.setArgument(annotatedWord.getArgument().getArgumentType() + "$" + currentId);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * The method returns all possible words, which is
     * 1. Verb
     * 2. Its semantic tag is assigned
     * 3. A frameset exists for that semantic tag
     * @param framesetList Frameset list that contains all frames for Turkish
     * @return An array of words, which are verbs, semantic tags assigned, and framesetlist assigned for that tag.
     */
    public ArrayList<AnnotatedWord> predicateCandidates(FramesetList framesetList){
        ArrayList<AnnotatedWord> candidateList = new ArrayList<>();
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getParse() != null && annotatedWord.getParse().isVerb() && annotatedWord.getSemantic() != null && framesetList.frameExists(annotatedWord.getSemantic())){
                candidateList.add(annotatedWord);
            }
        }
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < words.size() - i - 1; j++){
                AnnotatedWord annotatedWord = (AnnotatedWord) words.get(j);
                AnnotatedWord nextAnnotatedWord = (AnnotatedWord) words.get(j + 1);
                if (!candidateList.contains(annotatedWord) && candidateList.contains(nextAnnotatedWord) && annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals(nextAnnotatedWord.getSemantic())){
                    candidateList.add(annotatedWord);
                }
            }
        }
        return candidateList;
    }

    /**
     * Returns the i'th predicate in the sentence.
     * @param index Predicate index
     * @return The predicate with index index in the sentence.
     */
    public String getPredicate(int index){
        int count1  = 0, count2 = 0;
        String data = "";
        ArrayList<AnnotatedWord> word = new ArrayList<>();
        ArrayList<MorphologicalParse> parse = new ArrayList<>();
        if (index < wordCount()){
            for (int i = 0; i < wordCount(); i++) {
                word.add((AnnotatedWord) getWord(i));
                parse.add(word.get(i).getParse());
            }
            for (int i = index; i >= 0; i--) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")){
                    count1 = index - i;
                    break;
                }
            }
            for (int i = index; i < wordCount() - index; i++) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")){
                    count2 = i - index;
                    break;
                }
            }
            if (count1 > count2){
                data = word.get(count1).getName();
            }
            else{
                data = word.get(count2).getName();
            }
        }
        return data;
    }

    /**
     * Returns file name of the sentence
     * @return File name of the sentence
     */
    public String getFileName(){
        return file.getName();
    }

    /**
     * Removes the i'th word from the sentence
     * @param index Word index
     */
    public void removeWord(int index){
        words.remove(index);
    }

    /**
     * The toStems method returns an accumulated string of each word's stems in words {@link ArrayList}.
     * If the parse of the word does not exist, the method adds the surfaceform to the resulting string.
     *
     * @return String result which has all the stems of each item in words {@link ArrayList}.
     */
    public String toStems() {
        String result = "";
        AnnotatedWord annotatedWord;
        if (words.size() > 0) {
            annotatedWord = (AnnotatedWord) words.get(0);
            if (annotatedWord.getParse() != null){
                result = annotatedWord.getParse().getWord().getName();
            } else {
                result = words.get(0).getName();
            }
            for (int i = 1; i < words.size(); i++) {
                annotatedWord = (AnnotatedWord) words.get(i);
                if (annotatedWord.getParse() != null){
                    result = result + " " + annotatedWord.getParse().getWord().getName();
                } else {
                    result = result + " " + annotatedWord.getName();
                }
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * Saves the current sentence.
     */
    public void save(){
        writeToFile(file);
    }

    /**
     * Creates a list of literal candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible root forms of the i'th word in the sentence
     * 2. All possible 2-word expressions containing the i'th word in the sentence
     * 3. All possible 3-word expressions containing the i'th word in the sentence
     * @param wordNet Turkish wordnet
     * @param fsm Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of literal candidates containing all possible root forms and multiword expressions.
     */
    public ArrayList<Literal> constructLiterals(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        ArrayList<Literal> possibleLiterals = new ArrayList<>();
        MorphologicalParse morphologicalParse = word.getParse();
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        possibleLiterals.addAll(wordNet.constructLiterals(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
        AnnotatedWord firstSucceedingWord = null;
        AnnotatedWord secondSucceedingWord = null;
        if (wordCount() > wordIndex + 1) {
            firstSucceedingWord = (AnnotatedWord) getWord(wordIndex + 1);
            if (wordCount() > wordIndex + 2) {
                secondSucceedingWord = (AnnotatedWord) getWord(wordIndex + 2);
            }
        }
        if (firstSucceedingWord != null) {
            if (secondSucceedingWord != null) {
                possibleLiterals.addAll(wordNet.constructIdiomLiterals(word.getParse(), firstSucceedingWord.getParse(), secondSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), secondSucceedingWord.getMetamorphicParse(), fsm));
            }
            possibleLiterals.addAll(wordNet.constructIdiomLiterals(word.getParse(), firstSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        Collections.sort(possibleLiterals, new LiteralWithSenseComparator());
        return possibleLiterals;
    }

    /**
     * Creates a list of synset candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible synsets containing the i'th word in the sentence
     * 2. All possible synsets containing 2-word expressions, which contains the i'th word in the sentence
     * 3. All possible synsets containing 3-word expressions, which contains the i'th word in the sentence
     * @param wordNet Turkish wordnet
     * @param fsm Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of synset candidates containing all possible root forms and multiword expressions.
     */
    public ArrayList<SynSet> constructSynSets(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex) throws ParseRequiredException {
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        ArrayList<SynSet> possibleSynSets = new ArrayList<>();
        MorphologicalParse morphologicalParse = word.getParse();
        if (morphologicalParse == null){
            throw new ParseRequiredException(word.getName());
        }
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        possibleSynSets.addAll(wordNet.constructSynSets(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
        AnnotatedWord firstPrecedingWord = null;
        AnnotatedWord secondPrecedingWord = null;
        AnnotatedWord firstSucceedingWord = null;
        AnnotatedWord secondSucceedingWord = null;
        if (wordIndex > 0) {
            firstPrecedingWord = (AnnotatedWord) getWord(wordIndex - 1);
            if (firstPrecedingWord.getParse() == null){
                throw new ParseRequiredException(word.getName());
            }
            if (wordIndex > 1) {
                secondPrecedingWord = (AnnotatedWord) getWord(wordIndex - 2);
                if (secondPrecedingWord.getParse() == null){
                    throw new ParseRequiredException(word.getName());
                }
            }
        }
        if (wordCount() > wordIndex + 1) {
            firstSucceedingWord = (AnnotatedWord) getWord(wordIndex + 1);
            if (firstSucceedingWord.getParse() == null){
                throw new ParseRequiredException(word.getName());
            }
            if (wordCount() > wordIndex + 2) {
                secondSucceedingWord = (AnnotatedWord) getWord(wordIndex + 2);
                if (secondSucceedingWord.getParse() == null){
                    throw new ParseRequiredException(word.getName());
                }
            }
        }
        if (firstPrecedingWord != null) {
            if (secondPrecedingWord != null) {
                possibleSynSets.addAll(wordNet.constructIdiomSynSets(secondPrecedingWord.getParse(), firstPrecedingWord.getParse(), word.getParse(), secondPrecedingWord.getMetamorphicParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), fsm));
            }
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(firstPrecedingWord.getParse(), word.getParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), fsm));
        }
        if (firstPrecedingWord != null && firstSucceedingWord != null) {
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(firstPrecedingWord.getParse(), word.getParse(), firstSucceedingWord.getParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        if (firstSucceedingWord != null) {
            if (secondSucceedingWord != null) {
                possibleSynSets.addAll(wordNet.constructIdiomSynSets(word.getParse(), firstSucceedingWord.getParse(), secondSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), secondSucceedingWord.getMetamorphicParse(), fsm));
            }
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(word.getParse(), firstSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        Collections.sort(possibleSynSets, new SynSetComparator());
        return possibleSynSets;
    }


}
