package AnnotatedSentence.AutoProcessor.AutoArgument;

import AnnotatedSentence.*;

public class TurkishSentenceAutoArgument extends SentenceAutoArgument{

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
                    if (word.getShallowParse().equalsIgnoreCase("ÖZNE")){
                        word.setArgument("ARG0$" + predicateId);
                        modified = true;
                    } else {
                        if (word.getShallowParse().equalsIgnoreCase("NESNE")){
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
