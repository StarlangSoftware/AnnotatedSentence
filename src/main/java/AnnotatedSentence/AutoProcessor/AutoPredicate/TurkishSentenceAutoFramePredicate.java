package AnnotatedSentence.AutoProcessor.AutoPredicate;

import AnnotatedSentence.*;
import FrameNet.FrameNet;
import PropBank.FramesetList;

import java.util.ArrayList;

public class TurkishSentenceAutoFramePredicate extends SentenceAutoFramePredicate{
    private FrameNet frameNet;

    /**
     * Constructor for {@link TurkishSentenceAutoFramePredicate}. Gets the FrameSets as input from the user, and sets
     * the corresponding attribute.
     * @param frameNet FrameNet containing the Turkish frames.
     */
    public TurkishSentenceAutoFramePredicate(FrameNet frameNet){
        this.frameNet = frameNet;
    }

    @Override
    public boolean autoPredicate(AnnotatedSentence sentence) {
        ArrayList<AnnotatedWord> candidateList = sentence.predicateFrameCandidates(frameNet);
        for (AnnotatedWord word : candidateList){
            word.setFrameElement("PREDICATE$NONE$" + word.getSemantic());
        }
        if (candidateList.size() > 0){
            return true;
        }
        return false;
    }
}
