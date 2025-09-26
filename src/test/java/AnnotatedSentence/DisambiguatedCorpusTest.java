package AnnotatedSentence;

import Corpus.DisambiguatedWord;
import Corpus.DisambiguationCorpus;
import Corpus.Sentence;
import org.junit.Test;

import java.io.File;

public class DisambiguatedCorpusTest {

    @Test
    public void export(){
        String dataSet = "penn-treebank-20";
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../" + dataSet + "/Turkish-Phrase"));
        DisambiguationCorpus disambiguationCorpus = new DisambiguationCorpus();
        for (int i = 0; i < 100; i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            Sentence disambiguationSentence = new Sentence();
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord annotatedWord = (AnnotatedWord) sentence.getWord(j);
                DisambiguatedWord word = new DisambiguatedWord(annotatedWord.getName(), annotatedWord.getParse());
                disambiguationSentence.addWord(word);
            }
            disambiguationCorpus.addSentence(disambiguationSentence);
        }
        disambiguationCorpus.writeToFile(dataSet + ".txt");
    }
}
