package AnnotatedSentence;

import AnnotatedSentence.AutoProcessor.AutoDisambiguation.TurkishSentenceAutoDisambiguator;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalDisambiguation.RootWordStatistics;

import java.io.File;

public class TestAnnotatedCorpus {

    public static void testDisambiguation(AnnotatedCorpus annotatedCorpus){
        int total = 0, totalAnnotated = 0, correct = 0;
        MorphologicalParse[] parses;
        TurkishSentenceAutoDisambiguator turkishSentenceAutoDisambiguator = new TurkishSentenceAutoDisambiguator(new RootWordStatistics("Model/rootwordstatistics.bin"));
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


    public static void main(String[] args){
        AnnotatedCorpus annotatedCorpus = new AnnotatedCorpus(new File("../../../Turkish-Phrase/"));
        testDisambiguation(annotatedCorpus);
    }
}
