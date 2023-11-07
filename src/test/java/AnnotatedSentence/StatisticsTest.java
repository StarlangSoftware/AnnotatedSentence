package AnnotatedSentence;

import java.io.File;
import java.util.Map;

public class StatisticsTest {

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
