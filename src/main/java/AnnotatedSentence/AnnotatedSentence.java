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

    public boolean containsPredicate(){
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgument() != null && annotatedWord.getArgument().getArgumentType().equals("PREDICATE")){
                return true;
            }
        }
        return false;
    }

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

    public String getFileName(){
        return file.getName();
    }

    public void removeWord(int index){
        words.remove(index);
    }

    public void save(){
        writeToFile(file);
    }

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

    public ArrayList<SynSet> constructSynSets(WordNet wordNet,FsmMorphologicalAnalyzer fsm, int wordIndex) throws ParseRequiredException {
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
