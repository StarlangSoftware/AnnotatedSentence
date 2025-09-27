package AnnotatedSentence;

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

    public void testConvertUdBoun(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Boun/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_boun-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Boun/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_boun-ud-test.conllu");
        corpus = new AnnotatedCorpus(new File("../../Boun/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_boun-ud-dev.conllu");
    }

    public void testConvertUdGb(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Gb/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_gb-ud-test.conllu");
    }

    public void testConvertUdImst(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Imst/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_imst-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Imst/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_imst-ud-test.conllu");
        corpus = new AnnotatedCorpus(new File("../../Imst/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_imst-ud-dev.conllu");
    }

    public void testConvertUdImst2(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Imst2/Turkish-Phrase/"), ".train");
        corpus.exportUniversalDependencyFormat("tr_imst2-ud-train.conllu");
        corpus = new AnnotatedCorpus(new File("../../Imst2/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_imst2-ud-test.conllu");
        corpus = new AnnotatedCorpus(new File("../../Imst2/Turkish-Phrase/"), ".dev");
        corpus.exportUniversalDependencyFormat("tr_imst2-ud-dev.conllu");
    }

    public void testConvertUdPud(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Pud/Turkish-Phrase/"), ".test");
        corpus.exportUniversalDependencyFormat("tr_pud-ud-test.conllu");
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
