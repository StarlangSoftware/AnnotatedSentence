package AnnotatedSentence.AutoProcessor.AutoSemantic;

import AnnotatedSentence.AnnotatedSentence;

public abstract class SentenceAutoSemantic {
    /**
     * The method should set the senses of all words, for which there is only one possible sense.
     * @param sentence The sentence for which word sense disambiguation will be determined automatically.
     */
    protected abstract void autoLabelSingleSemantics(AnnotatedSentence sentence);

    public void autoSemantic(AnnotatedSentence sentence){
        autoLabelSingleSemantics(sentence);
    }

}
