package AnnotatedSentence.Statistics;

import AnnotatedSentence.AnnotatedCorpus;

import java.io.File;

public class TestSentenceLayerStatistics {

    public static void main(String[] args){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../Penn-Treebank/Turkish-Phrase"), ".");
        SentenceLayerStatistics sentenceLayerStatistics = new SentenceLayerStatistics(corpus);
        sentenceLayerStatistics.calculateRootWithPosStatistics();
        sentenceLayerStatistics.printTopN(40);
    }
}
