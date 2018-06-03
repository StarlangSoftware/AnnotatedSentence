package AnnotatedSentence.AutoProcessor.AutoSemantic;

import AnnotatedSentence.AnnotatedSentence;

public abstract class SentenceAutoSemantic {
    protected abstract void autoLabelSingleSemantics(AnnotatedSentence sentence);

    public void autoSemantic(AnnotatedSentence sentence){
        autoLabelSingleSemantics(sentence);
        sentence.save();
    }

}
