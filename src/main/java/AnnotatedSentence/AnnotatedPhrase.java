package AnnotatedSentence;

import Corpus.Sentence;

public class AnnotatedPhrase extends Sentence {
    private int wordIndex;
    private String tag;

    /**
     * Constructor for AnnotatedPhrase. AnnotatedPhrase stores information about phrases such as
     * Shallow Parse phrases or named entity phrases.
     * @param wordIndex Starting index of the first word in the phrase w.r.t. original sentence the phrase occurs.
     * @param tag Tag of the phrase. Corresponds to the shallow parse or named entity tag.
     */
    public AnnotatedPhrase(int wordIndex, String tag){
        this.wordIndex = wordIndex;
        this.tag = tag;
    }

    /**
     * Accessor for the wordIndex attribute.
     * @return Starting index of the first word in the phrase w.r.t. original sentence the phrase occurs.
     */
    public int getWordIndex() {
        return wordIndex;
    }

    /**
     * Accessor for the tag attribute.
     * @return Tag of the phrase. Corresponds to the shallow parse or named entity tag.
     */
    public String getTag() {
        return tag;
    }
}
