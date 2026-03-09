package AnnotatedSentence;

import org.junit.Test;

import java.io.File;

public class JsonConvertTest {

    public void testAtisEnglish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"));
        corpus.exportJsonDataSet("slot-atis-en.txt", ViewLayerType.SLOT);
    }

    public void testAtisTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"));
        corpus.exportJsonDataSet("slot-atis-tr.txt", ViewLayerType.SLOT);
        corpus.exportJsonDataSet("disambiguation-atis-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-atis-tr.txt", ViewLayerType.POS_TAG);
        corpus.exportJsonDataSet("semantics-atis-tr.txt", ViewLayerType.SEMANTICS);
        corpus.exportJsonDataSet("propbank-atis-tr.txt", ViewLayerType.PROPBANK);
        corpus.exportJsonDataSet("framenet-atis-tr.txt", ViewLayerType.FRAMENET);
    }

    public void testPennTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-penn-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-penn-tr.txt", ViewLayerType.POS_TAG);
        corpus.exportJsonDataSet("semantics-penn-tr.txt", ViewLayerType.SEMANTICS);
        corpus.exportJsonDataSet("propbank-penn-tr.txt", ViewLayerType.PROPBANK);
        corpus.exportJsonDataSet("framenet-penn-tr.txt", ViewLayerType.FRAMENET);
        corpus.exportJsonDataSet("ner-penn-tr.txt", ViewLayerType.NER);
        corpus.exportJsonDataSet("shallowparse-penn-tr.txt", ViewLayerType.SHALLOW_PARSE);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-penn-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-penn-tr.txt", ViewLayerType.POS_TAG);
        corpus.exportJsonDataSet("semantics-penn-tr.txt", ViewLayerType.SEMANTICS);
        corpus.exportJsonDataSet("propbank-penn-tr.txt", ViewLayerType.PROPBANK);
        corpus.exportJsonDataSet("framenet-penn-tr.txt", ViewLayerType.FRAMENET);
        corpus.exportJsonDataSet("shallowparse-penn-tr.txt", ViewLayerType.SHALLOW_PARSE);
    }

    public void testTourismTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-etstur-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-etstur-tr.txt", ViewLayerType.POS_TAG);
        corpus.exportJsonDataSet("semantics-etstur-tr.txt", ViewLayerType.SEMANTICS);
        corpus.exportJsonDataSet("propbank-etstur-tr.txt", ViewLayerType.PROPBANK);
        corpus.exportJsonDataSet("framenet-etstur-tr.txt", ViewLayerType.FRAMENET);
        corpus.exportJsonDataSet("shallowparse-etstur-tr.txt", ViewLayerType.SHALLOW_PARSE);
        corpus.exportJsonDataSet("polarity-etstur-tr.txt", ViewLayerType.POLARITY);
    }

    public void testKenetTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-kenet-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-kenet-tr.txt", ViewLayerType.POS_TAG);
        corpus.exportJsonDataSet("semantics-kenet-tr.txt", ViewLayerType.SEMANTICS);
        corpus.exportJsonDataSet("propbank-kenet-tr.txt", ViewLayerType.PROPBANK);
        corpus.exportJsonDataSet("framenet-kenet-tr.txt", ViewLayerType.FRAMENET);
    }

    public void testBounTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Boun/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-boun-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-boun-tr.txt", ViewLayerType.POS_TAG);
    }

    public void testGbTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Gb/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-gb-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-gb-tr.txt", ViewLayerType.POS_TAG);
    }

    public void tesPudTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Pud/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-pud-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-pud-tr.txt", ViewLayerType.POS_TAG);
    }

    public void tesImstTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Imst/Turkish-Phrase/"));
        corpus.exportJsonDataSet("disambiguation-imst-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportJsonDataSet("postag-imst-tr.txt", ViewLayerType.POS_TAG);
    }

    public void testFramenetTurkish(){
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        for (File file:listOfFiles) {
            if (file.isDirectory()){
                String fileName = file.getName();
                AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName));
                corpus.exportJsonDataSet("disambiguation-framenet-tr.txt", ViewLayerType.INFLECTIONAL_GROUP);
                corpus.exportJsonDataSet("postag-framenet-tr.txt", ViewLayerType.POS_TAG);
                corpus.exportJsonDataSet("semantics-framenet-tr.txt", ViewLayerType.SEMANTICS);
                corpus.exportJsonDataSet("propbank-framenet-tr.txt", ViewLayerType.PROPBANK);
                corpus.exportJsonDataSet("framenet-framenet-tr.txt", ViewLayerType.FRAMENET);
            }
        }
    }

}
