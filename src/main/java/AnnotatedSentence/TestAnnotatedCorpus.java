package AnnotatedSentence;

import AnnotatedSentence.AutoProcessor.AutoArgument.TurkishSentenceAutoArgument;
import AnnotatedSentence.AutoProcessor.AutoDisambiguation.TurkishSentenceAutoDisambiguator;
import AnnotatedSentence.AutoProcessor.AutoNER.TurkishSentenceAutoNER;
import AnnotatedSentence.AutoProcessor.AutoPredicate.TurkishSentenceAutoPredicate;
import AnnotatedSentence.AutoProcessor.AutoSemantic.TurkishSentenceAutoSemantic;
import Corpus.Sentence;
import Dictionary.TxtDictionary;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalDisambiguation.DisambiguatedWord;
import MorphologicalDisambiguation.DisambiguationCorpus;
import MorphologicalDisambiguation.RootWordStatistics;
import NamedEntityRecognition.NamedEntityType;
import PropBank.Argument;
import PropBank.FramesetList;
import WordNet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TestAnnotatedCorpus {

    public static void synSetConversion(){
        AnnotatedCorpus annotatedCorpus;
        WordNet turkish = new WordNet();
        IdMapping mapping = new IdMapping("Data/Wordnet/mapping.txt");
        annotatedCorpus = new AnnotatedCorpus(new File("../../../Turkish-Phrase/"));
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            boolean changed = false;
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            for (int j = 0; j <  sentence.wordCount(); j++){
                AnnotatedWord annotatedWord = (AnnotatedWord) sentence.getWord(j);
                if (annotatedWord.getSemantic() != null){
                    String id = annotatedWord.getSemantic();
                    if (turkish.getSynSetWithId(id) == null){
                        String mappedId = mapping.map(id);
                        if (mappedId != null){
                            annotatedWord.setSemantic(mappedId);
                            changed = true;
                        } else {
                            System.out.println(id + " does not exist");
                        }
                    }
                }
            }
            if (changed){
                sentence.save();
            }
        }
    }

    public static void testDisambiguation(AnnotatedCorpus annotatedCorpus){
        int total = 0, totalAnnotated = 0, correct = 0;
        MorphologicalParse[] parses;
        TurkishSentenceAutoDisambiguator turkishSentenceAutoDisambiguator = new TurkishSentenceAutoDisambiguator(new RootWordStatistics("rootwordstatistics.bin"));
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            parses = new MorphologicalParse[sentence.wordCount()];
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                parses[j] = word.getParse();
                word.setParse(null);
            }
            turkishSentenceAutoDisambiguator.autoDisambiguate(sentence);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (parses[j] != null){
                    total++;
                    if (word.getParse() != null){
                        totalAnnotated++;
                        if (parses[j].toString().equals(word.getParse().toString())){
                            correct++;
                        } else {
                            System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getParse() + "\t" + parses[j]);
                        }
                    }
                }
            }
        }
        System.out.println("Correct: " + correct + " Annotated: " + totalAnnotated + " Total: " + total);
    }

    public static void testNER(AnnotatedCorpus annotatedCorpus){
        int total = 0, totalAnnotated = 0, correct = 0;
        NamedEntityType[] ners;
        TurkishSentenceAutoNER turkishSentenceAutoNER = new TurkishSentenceAutoNER();
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            ners = new NamedEntityType[sentence.wordCount()];
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                ners[j] = word.getNamedEntityType();
                word.setNamedEntityType(null);
            }
            turkishSentenceAutoNER.autoNER(sentence);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (ners[j] != null){
                    total++;
                    if (word.getParse() != null){
                        totalAnnotated++;
                        if (ners[j].equals(word.getNamedEntityType())){
                            correct++;
                        } else {
                            System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getNamedEntityType() + "\t" + ners[j]);
                        }
                    }
                }
            }
        }
        System.out.println("Correct: " + correct + " Annotated: " + totalAnnotated + " Total: " + total);
    }

    public static void testSemantic(AnnotatedCorpus annotatedCorpus){
        int total = 0, totalAnnotated = 0, correct = 0;
        String[] semantics;
        TurkishSentenceAutoSemantic turkishSentenceAutoSemantic = new TurkishSentenceAutoSemantic(new WordNet(), new FsmMorphologicalAnalyzer());
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            semantics = new String[sentence.wordCount()];
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                semantics[j] = word.getSemantic();
                word.setSemantic(null);
            }
            turkishSentenceAutoSemantic.autoSemantic(sentence);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (semantics[j] != null){
                    total++;
                    if (word.getSemantic() != null){
                        totalAnnotated++;
                        if (semantics[j].equals(word.getSemantic())){
                            correct++;
                        } else {
                            System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getSemantic() + "\t" + semantics[j]);
                        }
                    }
                }
            }
        }
        System.out.println("Correct: " + correct + " Annotated: " + totalAnnotated + " Total: " + total);
    }

    public static void multiplePredicates(AnnotatedCorpus annotatedCorpus){
        HashSet<String> arguments;
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            arguments = new HashSet<>();
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                    arguments.add(word.getArgument().getId());
                }
            }
            if (arguments.size() > 1){
                System.out.println(sentence.getFileName() + "\t" + arguments.size());
            }
        }
    }

    public static void testPredicateSelection(AnnotatedCorpus annotatedCorpus){
        int wrongAnnotated = 0, totalAnnotated = 0, correct = 0, unannotated = 0;
        Argument[] arguments;
        TurkishSentenceAutoPredicate turkishSentenceAutoPredicate = new TurkishSentenceAutoPredicate(new FramesetList());
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            arguments = new Argument[sentence.wordCount()];
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                    arguments[j] = word.getArgument();
                    word.setArgument(null);
                } else {
                    arguments[j] = null;
                }
            }
            turkishSentenceAutoPredicate.autoPredicate(sentence);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (arguments[j] != null){
                    if (word.getArgument() != null){
                        totalAnnotated++;
                        if (arguments[j].toString().equalsIgnoreCase(word.getArgument().toString())){
                            correct++;
                        } else {
                            wrongAnnotated++;
                            System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getArgument() + "\t" + arguments[j]);
                        }
                    }
                } else {
                    if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                        totalAnnotated++;
                        unannotated++;
                        System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getArgument() + "\t" + arguments[j]);
                    }
                }
            }
        }
        System.out.println("Correct: " + correct + " Annotated: " + totalAnnotated + " Wrong: " + wrongAnnotated + " Unannotated: " + unannotated);
    }

    public static void testArgument(AnnotatedCorpus annotatedCorpus){
        int total = 0, totalAnnotated = 0, correct = 0;
        Argument[] arguments;
        TurkishSentenceAutoArgument turkishSentenceAutoArgument = new TurkishSentenceAutoArgument();
        for (int i = 0; i < annotatedCorpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) annotatedCorpus.getSentence(i);
            arguments = new Argument[sentence.wordCount()];
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getArgument() != null){
                    if (!word.getArgument().getArgumentType().equals("PREDICATE")){
                        arguments[j] = word.getArgument();
                        word.setArgument(null);
                    } else {
                        arguments[j] = null;
                    }
                } else {
                    arguments[j] = null;
                }
            }
            turkishSentenceAutoArgument.autoArgument(sentence);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (arguments[j] != null){
                    total++;
                    if (word.getArgument() != null){
                        totalAnnotated++;
                        if (arguments[j].toString().equalsIgnoreCase(word.getArgument().toString())){
                            correct++;
                        } else {
                            System.out.println(sentence.getFileName() + "\t" + word.getName() + "\t" + word.getArgument() + "\t" + arguments[j]);
                        }
                    }
                }
            }
        }
        System.out.println("Correct: " + correct + " Annotated: " + totalAnnotated + " Total: " + total);
    }

    public static void extractTourismRootWordStatistics(){
        AnnotatedCorpus annotatedCorpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        RootWordStatistics rootWordStatistics = annotatedCorpus.extractRootWordStatistics(new FsmMorphologicalAnalyzer("../../Etstur/Program/tourism_dictionary.txt"));
        rootWordStatistics.saveStatistics("tourism_statistics.bin");
    }

    public static void extractDictionary(String pathName, String outputFileName){
        AnnotatedCorpus annotatedCorpus = new AnnotatedCorpus(new File(pathName));
        TxtDictionary dictionary = annotatedCorpus.createDictionary();
        dictionary.saveAsTxt(outputFileName);
    }

    public static void compareAnnotations(){
        DisambiguationCorpus corpus = new DisambiguationCorpus("milliyet.txt");
        AnnotatedCorpus annotatedCorpus = new AnnotatedCorpus(new File("../../Milliyet-5/Turkish-Phrase/"));
        int oldj = 0;
        for (int i = 0; i < corpus.sentenceCount(); i++){
            Sentence sentence1 = corpus.getSentence(i);
            for (int j = oldj; j < annotatedCorpus.sentenceCount(); j++){
                Sentence sentence2 = annotatedCorpus.getSentence(j);
                if (sentence1.toWords().equalsIgnoreCase(sentence2.toWords())){
                    for (int k = 0; k < sentence1.wordCount(); k++){
                        if (((DisambiguatedWord) sentence1.getWord(k)).getParse() != null && ((AnnotatedWord)sentence2.getWord(k)).getParse() != null){
                            if (!((DisambiguatedWord) sentence1.getWord(k)).getParse().toString().equalsIgnoreCase(((AnnotatedWord) sentence2.getWord(k)).getParse().toString())){
                                System.out.println(((AnnotatedSentence)sentence2).getFileName() + "\t" + sentence1.toWords() + "\t" + ((DisambiguatedWord) sentence1.getWord(k)).getName() + "\t" + ((DisambiguatedWord) sentence1.getWord(k)).getParse() + "\t" + ((AnnotatedWord)sentence2.getWord(k)).getParse());
                            }
                        }
                    }
                    oldj = j + 1;
                    break;
                }
            }
        }
    }

    public static void main(String[] args){
        //compareAnnotations();
        //extractTourismRootWordStatistics();
        AnnotatedCorpus annotatedCorpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"));
        /*try {
            annotatedCorpus.propBankAnnotationControl("output.txt", new WordNet());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        //multiplePredicates(annotatedCorpus);
        //testArgument(annotatedCorpus);
        //testPredicateSelection(annotatedCorpus);
        //testSemantic(annotatedCorpus);
        //testNER(annotatedCorpus);
        //testDisambiguation(annotatedCorpus);
        //extractDictionary("../../Penn-Treebank/Turkish-Phrase/", "deneme3.txt");
    }
}
