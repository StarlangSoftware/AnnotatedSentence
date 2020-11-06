package AnnotatedSentence;

import DataStructure.CounterHashMap;
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

    @Test
    public void testConvert(){
        //AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        //corpus.exportUniversalDependencyFormat("uv4.txt");
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