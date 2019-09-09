package AnnotatedSentence.AutoProcessor.AutoArgument;

import AnnotatedSentence.*;
import MorphologicalAnalysis.MorphologicalTag;

public class TurkishSentenceAutoArgument extends SentenceAutoArgument{

    /**
     * Given the sentence for which the predicate(s) were determined before, this method automatically assigns
     * semantic role labels to some/all words in the sentence. The method first finds the first predicate, then assuming
     * that the shallow parse tags were preassigned, assigns ÖZNE tagged words ARG0; NESNE tagged words ARG1. If the
     * verb is in passive form, ÖZNE tagged words are assigned as ARG1.
     * @param sentence The sentence for which semantic roles will be determined automatically.
     * @return If the method assigned at least one word a semantic role label, the method returns true; false otherwise.
     */
    public boolean autoArgument(AnnotatedSentence sentence) {
        boolean modified = false;
        String predicateId = null;
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                predicateId = word.getArgument().getId();
                break;
            }
        }
        if (predicateId != null){
            for (int i = 0; i < sentence.wordCount(); i++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
                if (word.getArgument() == null){
                    if (word.getShallowParse() != null && word.getShallowParse().equalsIgnoreCase("ÖZNE")){
                        if (word.getParse() != null && word.getParse().containsTag(MorphologicalTag.PASSIVE)){
                            word.setArgument("ARG1$" + predicateId);
                        } else {
                            word.setArgument("ARG0$" + predicateId);
                        }
                        modified = true;
                    } else {
                        if (word.getShallowParse() != null && word.getShallowParse().equalsIgnoreCase("NESNE")){
                            word.setArgument("ARG1$" + predicateId);
                            modified = true;
                        }
                    }
                }
            }
        }
        return modified;
    }
}
