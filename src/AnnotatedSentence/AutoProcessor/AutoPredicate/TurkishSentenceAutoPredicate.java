package AnnotatedSentence.AutoProcessor.AutoPredicate;

import AnnotatedSentence.*;
import PropBank.FramesetList;

import java.util.ArrayList;

public class TurkishSentenceAutoPredicate extends SentenceAutoPredicate{
    private FramesetList xmlParser;

    public TurkishSentenceAutoPredicate(FramesetList xmlParser){
        this.xmlParser = xmlParser;
    }

    public void autoPredicate(AnnotatedSentence sentence){
        ArrayList<AnnotatedWord> candidateList = sentence.predicateCandidates(xmlParser);
        for (AnnotatedWord word : candidateList){
            word.setArgument("PREDICATE$" + word.getSemantic());
        }
        if (candidateList.size() > 0){
            sentence.save();
        }
    }

}
