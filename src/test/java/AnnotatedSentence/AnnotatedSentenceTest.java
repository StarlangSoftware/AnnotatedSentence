package AnnotatedSentence;

import PropBank.FramesetList;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AnnotatedSentenceTest {
    AnnotatedSentence sentence0, sentence1, sentence2, sentence3, sentence4;
    AnnotatedSentence sentence5, sentence6, sentence7, sentence8, sentence9;

    @Before
    public void setUp() throws Exception {
        sentence0 = new AnnotatedSentence(new File("sentences/0000.dev"));
        sentence1 = new AnnotatedSentence(new File("sentences/0001.dev"));
        sentence2 = new AnnotatedSentence(new File("sentences/0002.dev"));
        sentence3 = new AnnotatedSentence(new File("sentences/0003.dev"));
        sentence4 = new AnnotatedSentence(new File("sentences/0004.dev"));
        sentence5 = new AnnotatedSentence(new File("sentences/0005.dev"));
        sentence6 = new AnnotatedSentence(new File("sentences/0006.dev"));
        sentence7 = new AnnotatedSentence(new File("sentences/0007.dev"));
        sentence8 = new AnnotatedSentence(new File("sentences/0008.dev"));
        sentence9 = new AnnotatedSentence(new File("sentences/0009.dev"));
    }

    public void calculateGrade(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"), ".test", 5100, 5399);
        int totalWord = 0, totalAnnotated = 0;
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                if (((AnnotatedWord)sentence.getWord(j)).getSemantic() != null){
                    totalAnnotated++;
                }
            }
            totalWord += sentence.wordCount();
        }
        System.out.println(totalAnnotated / (totalWord + 0.0));
    }

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

    @Test
    public void testParserEvaluation() {
        assertEquals(1.0, sentence0.compareParses(sentence0).getUAS(), 0.0);
        assertEquals(1.0, sentence0.compareParses(sentence0).getLAS(), 0.0);
        assertEquals(1.0, sentence0.compareParses(sentence0).getLS(), 0.0);
    }

    @Test
    public void testGetShallowParseGroups() {
        assertEquals(4, sentence0.getShallowParseGroups().size());
        assertEquals(5, sentence1.getShallowParseGroups().size());
        assertEquals(3, sentence2.getShallowParseGroups().size());
        assertEquals(5, sentence3.getShallowParseGroups().size());
        assertEquals(5, sentence4.getShallowParseGroups().size());
        assertEquals(5, sentence5.getShallowParseGroups().size());
        assertEquals(6, sentence6.getShallowParseGroups().size());
        assertEquals(5, sentence7.getShallowParseGroups().size());
        assertEquals(5, sentence8.getShallowParseGroups().size());
        assertEquals(3, sentence9.getShallowParseGroups().size());
    }

    @Test
    public void testContainsPredicate() {
        assertTrue(sentence0.containsPredicate());
        assertTrue(sentence1.containsPredicate());
        assertFalse(sentence2.containsPredicate());
        assertTrue(sentence3.containsPredicate());
        assertTrue(sentence4.containsPredicate());
        assertFalse(sentence5.containsPredicate());
        assertFalse(sentence6.containsPredicate());
        assertTrue(sentence7.containsPredicate());
        assertTrue(sentence8.containsPredicate());
        assertTrue(sentence9.containsPredicate());
    }

    @Test
    public void testGetPredicate() {
        assertEquals("bulandırdı", sentence0.getPredicate(0));
        assertEquals("yapacak", sentence1.getPredicate(0));
        assertEquals("ediyorlar", sentence3.getPredicate(0));
        assertEquals("yazmıştı", sentence4.getPredicate(0));
        assertEquals("olunacaktı", sentence7.getPredicate(0));
        assertEquals("gerekiyordu", sentence8.getPredicate(0));
        assertEquals("ediyor", sentence9.getPredicate(0));
    }

    @Test
    public void testToStems() {
        assertEquals("devasa ölçek yeni kanun kullan karmaşık ve çetrefil dil kavga bulan .", sentence0.toStems());
        assertEquals("gelir art usul komite gel salı gün kanun tasarı hakkında bir duruşma yap .", sentence1.toStems());
        assertEquals("reklam ve tanıtım iş yara yara gör üzere .", sentence2.toStems());
        assertEquals("bu defa , daha da hız hareket et .", sentence3.toStems());
        assertEquals("shearson lehman hutton ınc. dün öğle sonra kadar yeni tv reklam yaz .", sentence4.toStems());
        assertEquals("bu kez , firma hazır .", sentence5.toStems());
        assertEquals("`` diyalog sür kesinlikle temel önem haiz .", sentence6.toStems());
        assertEquals("cuma gün bu üzerine düşün çok geç kal ol .", sentence7.toStems());
        assertEquals("bu hakkında önceden düşün gerek . ''", sentence8.toStems());
        assertEquals("isim göre çeşit göster birkaç kefaret fon reklam yap için devam et .", sentence9.toStems());
    }

    @Test
    public void testPredicateCandidates() {
        FramesetList framesetList = new FramesetList();
        assertEquals(1, sentence0.predicateCandidates(framesetList).size());
        assertEquals(1, sentence1.predicateCandidates(framesetList).size());
        assertEquals(0, sentence2.predicateCandidates(framesetList).size());
        assertEquals(2, sentence3.predicateCandidates(framesetList).size());
        assertEquals(1, sentence4.predicateCandidates(framesetList).size());
        assertEquals(0, sentence5.predicateCandidates(framesetList).size());
        assertEquals(0, sentence6.predicateCandidates(framesetList).size());
        assertEquals(1, sentence7.predicateCandidates(framesetList).size());
        assertEquals(1, sentence8.predicateCandidates(framesetList).size());
        assertEquals(2, sentence9.predicateCandidates(framesetList).size());
    }

}