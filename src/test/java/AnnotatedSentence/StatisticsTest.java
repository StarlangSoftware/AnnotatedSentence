package AnnotatedSentence;
import org.junit.Before;
import org.junit.Test;

import Dictionary.Pos;
import WordNet.*;

import java.io.File;
import java.util.HashSet;
import java.util.Map;

public class StatisticsTest {

    public void verbsMultiFolder(){
        WordNet turkish = new WordNet();
        String dataset = "FrameNet-Examples";
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        for (File file:listOfFiles) {
            if (file.isDirectory()){
                String fileName = file.getName();
                AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../" + dataset + "/Turkish-Phrase/" + fileName));
                for (int i = 0; i < corpus.sentenceCount(); i++){
                    AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
                    HashSet<String> verbs = new HashSet<>();
                    for (int j = 0; j < sentence.wordCount(); j++){
                        AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                        if (word.getSemantic() != null){
                            SynSet synSet = turkish.getSynSetWithId(word.getSemantic());
                            if (synSet != null && synSet.getPos().equals(Pos.ADVERB)){
                                verbs.add(word.getSemantic());
                            }
                        }
                    }
                    for (String id: verbs){
                        System.out.print(id + "\t" + turkish.getSynSetWithId(id).getDefinition() + "\t" + dataset + "-" + fileName + "\t" + sentence.getFileName() + "\t" +  sentence.toWords() + "\t");
                        for (int j = 0; j < sentence.wordCount(); j++){
                            AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                            if (word.getSemantic() != null && word.getSemantic().equals(id)){
                                System.out.print(" " + word.getName());
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    public void nouns(){
        WordNet turkish = new WordNet();
        String dataset = "Penn-Treebank-20";
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../" + dataset + "/Turkish-Phrase/"));
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            HashSet<String> verbs = new HashSet<>();
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getSemantic() != null){
                    SynSet synSet = turkish.getSynSetWithId(word.getSemantic());
                    if (synSet != null && synSet.getPos().equals(Pos.ADVERB)){
                        verbs.add(word.getSemantic());
                    }
                }
            }
            for (String id: verbs){
                System.out.print(id + "\t" + turkish.getSynSetWithId(id).getDefinition() + "\t" + dataset + "\t" + sentence.getFileName() + "\t" +  sentence.toWords() + "\t");
                for (int j = 0; j < sentence.wordCount(); j++){
                    AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                    if (word.getSemantic() != null && word.getSemantic().equals(id)){
                        System.out.print(" " + word.getName());
                    }
                }
                System.out.println();
            }
        }
    }

    public void statistics(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"));
        System.out.println("Unique Surface Forms");
        System.out.println(corpus.uniqueSurfaceForms());
        System.out.println("Token Count Except Punctuations");
        System.out.println(corpus.tokenCountExceptPunctuations());
        System.out.println("Top Dependency Annotations");
        for (Map.Entry<String, Integer> entry : corpus.topDependencyAnnotations()){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
        System.out.println("Top 20 Surface Forms");
        for (Map.Entry<String, Integer> entry : corpus.topSurfaceForms(20)){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
        System.out.println("Top 20 Root Forms");
        for (Map.Entry<String, Integer> entry : corpus.topRootForms(20)){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
        System.out.println("Root Pos Tags");
        for (Map.Entry<String, Integer> entry : corpus.rootPosTags()){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }

}
