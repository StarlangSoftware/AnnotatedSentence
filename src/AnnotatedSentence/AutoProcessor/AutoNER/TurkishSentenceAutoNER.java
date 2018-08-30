package AnnotatedSentence.AutoProcessor.AutoNER;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import Dictionary.Word;
import MorphologicalAnalysis.MorphologicalTag;

import java.util.Locale;

public class TurkishSentenceAutoNER extends SentenceAutoNER{

    protected void autoDetectPerson(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getNamedEntityType() == null && word.getParse() != null){
                if (Word.isHonorific(word.getName())){
                    word.setNamedEntityType("PERSON");
                }
                word.checkGazetteer(personGazetteer);
            }
        }
    }

    protected void autoDetectLocation(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getNamedEntityType() == null && word.getParse() != null){
                word.checkGazetteer(locationGazetteer);
            }
        }
    }

    protected void autoDetectOrganization(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getNamedEntityType() == null && word.getParse() != null){
                if (Word.isOrganization(word.getName())){
                    word.setNamedEntityType("ORGANIZATION");
                }
                word.checkGazetteer(organizationGazetteer);
            }
        }
    }

    protected void autoDetectTime(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            String wordLowercase = word.getName().toLowerCase(new Locale("tr"));
            if (word.getNamedEntityType() == null && word.getParse() != null){
                if (Word.isTime(wordLowercase)){
                    word.setNamedEntityType("TIME");
                    if (i > 0){
                        AnnotatedWord previous = (AnnotatedWord) sentence.getWord(i - 1);
                        if (previous.getParse().containsTag(MorphologicalTag.CARDINAL)){
                            previous.setNamedEntityType("TIME");
                        }
                    }
                }
            }
        }
    }

    protected void autoDetectMoney(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            String wordLowercase = word.getName().toLowerCase(new Locale("tr"));
            if (word.getNamedEntityType() == null && word.getParse() != null){
                if (Word.isMoney(wordLowercase)) {
                    word.setNamedEntityType("MONEY");
                    int j = i - 1;
                    while (j >= 0){
                        AnnotatedWord previous = (AnnotatedWord) sentence.getWord(j);
                        if (previous.getParse() != null && previous.getParse().containsTag(MorphologicalTag.CARDINAL)){
                            previous.setNamedEntityType("MONEY");
                        } else {
                            break;
                        }
                        j--;
                    }
                }
            }
        }
    }

}
