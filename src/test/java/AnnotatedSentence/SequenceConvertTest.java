package AnnotatedSentence;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class SequenceConvertTest {

    public void testAtisEnglish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".train");
        corpus.exportSequenceDataSet("slot-atis-en-train.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("postag-atis-en-train.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".test");
        corpus.exportSequenceDataSet("slot-atis-en-test.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("postag-atis-en-test.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"), ".dev");
        corpus.exportSequenceDataSet("slot-atis-en-dev.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("postag-atis-en-dev.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/English-Phrase/"));
        corpus.exportSequenceDataSet("slot-atis-en.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("postag-atis-en.txt", ViewLayerType.POS_TAG);
    }

    public void testAtisTurkish() {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".train");
        corpus.exportSequenceDataSet("slot-atis-tr-train.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("disambiguation-atis-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-atis-train.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-atis-tr-train.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".test");
        corpus.exportSequenceDataSet("slot-atis-tr-test.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("disambiguation-atis-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-atis-test.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-atis-tr-test.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"), ".dev");
        corpus.exportSequenceDataSet("slot-atis-tr-dev.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("disambiguation-atis-dev.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-atis-dev.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-atis-tr-dev.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"));
        corpus.exportSequenceDataSet("slot-atis-tr.txt", ViewLayerType.SLOT);
        corpus.exportSequenceDataSet("disambiguation-atis.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-atis.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-atis-tr.txt", ViewLayerType.POS_TAG);
    }

    public void testPenn(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".train");
        corpus.exportSequenceDataSet("disambiguation-penn-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-train.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-train.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("ner-penn-train.txt", ViewLayerType.NER);
        corpus.exportSequenceDataSet("semantics-penn-train.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("propbank-penn-train.txt", ViewLayerType.PROPBANK);
        corpus.exportSequenceDataSet("shallowparse-penn-train.txt", ViewLayerType.SHALLOW_PARSE);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".train");
        corpus.exportSequenceDataSet("disambiguation-penn-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-train.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-train.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".test");
        corpus.exportSequenceDataSet("disambiguation-penn-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-test.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-test.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("ner-penn-test.txt", ViewLayerType.NER);
        corpus.exportSequenceDataSet("semantics-penn-test.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("propbank-penn-test.txt", ViewLayerType.PROPBANK);
        corpus.exportSequenceDataSet("shallowparse-penn-test.txt", ViewLayerType.SHALLOW_PARSE);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".test");
        corpus.exportSequenceDataSet("disambiguation-penn-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-test.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-test.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"), ".dev");
        corpus.exportSequenceDataSet("disambiguation-penn-dev.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-dev.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-dev.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("ner-penn-dev.txt", ViewLayerType.NER);
        corpus.exportSequenceDataSet("semantics-penn-dev.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("propbank-penn-dev.txt", ViewLayerType.PROPBANK);
        corpus.exportSequenceDataSet("shallowparse-penn-dev.txt", ViewLayerType.SHALLOW_PARSE);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"), ".dev");
        corpus.exportSequenceDataSet("disambiguation-penn-dev.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn-dev.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn-dev.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"));
        corpus.exportSequenceDataSet("disambiguation-penn.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("ner-penn.txt", ViewLayerType.NER);
        corpus.exportSequenceDataSet("semantics-penn.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("propbank-penn.txt", ViewLayerType.PROPBANK);
        corpus.exportSequenceDataSet("shallowparse-penn.txt", ViewLayerType.SHALLOW_PARSE);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"));
        corpus.exportSequenceDataSet("disambiguation-penn.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn.txt", ViewLayerType.POS_TAG);
    }

    public void testTourism(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".train");
        corpus.exportSequenceDataSet("disambiguation-tourism-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-tourism-train.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-tourism-train.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("semantics-tourism-train.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("shallowparse-tourism-train.txt", ViewLayerType.SHALLOW_PARSE);
        corpus.exportSequenceDataSet("sentiment-tourism-train.txt", ViewLayerType.POLARITY);
        corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"), ".test");
        corpus.exportSequenceDataSet("disambiguation-tourism-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-tourism-test.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-tourism-test.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("semantics-tourism-test.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("shallowparse-tourism-test.txt", ViewLayerType.SHALLOW_PARSE);
        corpus.exportSequenceDataSet("sentiment-tourism-test.txt", ViewLayerType.POLARITY);
        corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        corpus.exportSequenceDataSet("disambiguation-tourism.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-tourism.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-tourism.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("semantics-tourism.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("shallowparse-tourism.txt", ViewLayerType.SHALLOW_PARSE);
        corpus.exportSequenceDataSet("sentiment-tourism.txt", ViewLayerType.POLARITY);
    }

    public void testKeNet(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".train");
        corpus.exportSequenceDataSet("disambiguation-kenet-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-kenet-train.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-kenet-train.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".test");
        corpus.exportSequenceDataSet("disambiguation-kenet-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-kenet-test.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-kenet-test.txt", ViewLayerType.POS_TAG);
        corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"));
        corpus.exportSequenceDataSet("disambiguation-kenet.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-kenet.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-kenet.txt", ViewLayerType.POS_TAG);
    }

    public void testFramenet(){
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        for (File file:listOfFiles) {
            if (file.isDirectory()){
                String fileName = file.getName();
                AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".train");
                corpus.exportSequenceDataSet("disambiguation-framenet-train.txt", ViewLayerType.INFLECTIONAL_GROUP);
                corpus.exportSequenceDataSet("metamorpheme-framenet-train.txt", ViewLayerType.META_MORPHEME);
                corpus.exportSequenceDataSet("postag-framenet-train.txt", ViewLayerType.POS_TAG);
                corpus.exportSequenceDataSet("semanticrolelabeling-framenet-train.txt", ViewLayerType.FRAMENET);
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".test");
                corpus.exportSequenceDataSet("disambiguation-framenet-test.txt", ViewLayerType.INFLECTIONAL_GROUP);
                corpus.exportSequenceDataSet("metamorpheme-framenet-test.txt", ViewLayerType.META_MORPHEME);
                corpus.exportSequenceDataSet("postag-framenet-test.txt", ViewLayerType.POS_TAG);
                corpus.exportSequenceDataSet("semanticrolelabeling-framenet-test.txt", ViewLayerType.FRAMENET);
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName), ".dev");
                corpus.exportSequenceDataSet("disambiguation-framenet-dev.txt", ViewLayerType.INFLECTIONAL_GROUP);
                corpus.exportSequenceDataSet("metamorpheme-framenet-dev.txt", ViewLayerType.META_MORPHEME);
                corpus.exportSequenceDataSet("postag-framenet-dev.txt", ViewLayerType.POS_TAG);
                corpus.exportSequenceDataSet("semanticrolelabeling-framenet-dev.txt", ViewLayerType.FRAMENET);
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName));
                corpus.exportSequenceDataSet("disambiguation-framenet.txt", ViewLayerType.INFLECTIONAL_GROUP);
                corpus.exportSequenceDataSet("metamorpheme-framenet.txt", ViewLayerType.META_MORPHEME);
                corpus.exportSequenceDataSet("postag-framenet.txt", ViewLayerType.POS_TAG);
                corpus.exportSequenceDataSet("semanticrolelabeling-framenet.txt", ViewLayerType.FRAMENET);
            }
        }
    }

}
