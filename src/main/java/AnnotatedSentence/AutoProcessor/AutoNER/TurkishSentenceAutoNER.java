package AnnotatedSentence.AutoProcessor.AutoNER;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import Dictionary.Word;
import MorphologicalAnalysis.MorphologicalTag;

import java.util.Locale;

public class TurkishSentenceAutoNER extends SentenceAutoNER{

    /**
     * The method assigns the words "bay" and "bayan" PERSON tag. The method also checks the PERSON gazetteer, and if
     * the word exists in the gazetteer, it assigns PERSON tag.
     * @param sentence The sentence for which PERSON named entities checked.
     */
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

    /**
     * The method checks the LOCATION gazettteer, and if the word exists in the gazetteer, it assigns the LOCATION tag.
     * @param sentence The sentence for which LOCATION named entities checked.
     */
    protected void autoDetectLocation(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getNamedEntityType() == null && word.getParse() != null){
                word.checkGazetteer(locationGazetteer);
            }
        }
    }

    /**
     * The method assigns the words "corp.", "inc.", and "co" ORGANIZATION tag. The method also checks the
     * ORGANIZATION gazetteer, and if the word exists in the gazetteer, it assigns ORGANIZATION tag.
     * @param sentence The sentence for which ORGANIZATION named entities checked.
     */
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

    /**
     * The method checks for the TIME entities using regular expressions. After that, if the expression is a TIME
     * expression, it also assigns the previous texts, which are numbers, TIME tag.
     * @param sentence The sentence for which TIME named entities checked.
     */
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


    /**
     * The method checks for the MONEY entities using regular expressions. After that, if the expression is a MONEY
     * expression, it also assigns the previous text, which may included numbers or some monetarial texts, MONEY tag.
     * @param sentence The sentence for which MONEY named entities checked.
     */
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
                        if (previous.getParse() != null && (previous.getName().equalsIgnoreCase("amerikan") || previous.getParse().containsTag(MorphologicalTag.REAL) || previous.getParse().containsTag(MorphologicalTag.CARDINAL) || previous.getParse().containsTag(MorphologicalTag.NUMBER))){
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
