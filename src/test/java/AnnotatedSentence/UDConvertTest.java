package AnnotatedSentence;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class UDConvertTest {

    public void testConvertAtisEnglish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("en_atis-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("en_atis-ud-test.conllu");
        corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("en_atis-ud-dev.conllu");
    }

    public void testConvertAtisTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_atis-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_atis-ud-test.conllu");
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_atis-ud-dev.conllu");
    }

    public void testConvertUdPenn(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-train.conllu", "15-");
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-test.conllu", "15-");
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-dev.conllu", "15-");
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-train.conllu", "20-");
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-test.conllu", "20-");
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_penn-ud-dev.conllu", "20-");
    }

    public void testConvertUd(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".train");
        corpus.exportAmr();
    }

    public void testConvertUdTourism(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".train", 0, 7749);
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".test", 0, 7749);
        corpus.combine(corpus2);
        corpus.exportUniversalDependencyFormat("tr_tourism-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".train", 7750, 9999);
        corpus.exportUniversalDependencyFormat("tr_tourism-ud-dev.conllu");
        corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".test", 7750, 9999);
        corpus.exportUniversalDependencyFormat("tr_tourism-ud-test.conllu");
    }

    public void testConvertUdKeNet(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".train", 1, 7699);
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".test", 1, 7699);
        corpus.combine(corpus2);
        corpus.exportUniversalDependencyFormat("tr_kenet-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".train", 7700, 9345);
        corpus.exportUniversalDependencyFormat("tr_kenet-ud-dev.conllu");
        corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".test", 7700, 9342);
        corpus.exportUniversalDependencyFormat("tr_kenet-ud-test.conllu");
    }

    public void testConvertUdFramenet(){
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        for (File file:listOfFiles) {
            if (file.isDirectory()){
                String fileName = file.getName();
                AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".train");
                corpus.exportUniversalDependencyFormat("tr_framenet-ud-train.conllu", fileName + "-");
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".test");
                corpus.exportUniversalDependencyFormat("tr_framenet-ud-test.conllu", fileName + "-");
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".dev");
                corpus.exportUniversalDependencyFormat("tr_framenet-ud-dev.conllu", fileName + "-");
            }
        }
    }

}
