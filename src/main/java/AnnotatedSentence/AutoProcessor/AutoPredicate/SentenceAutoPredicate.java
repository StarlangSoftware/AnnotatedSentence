package AnnotatedSentence.AutoProcessor.AutoPredicate;

import AnnotatedSentence.AnnotatedSentence;

public abstract class SentenceAutoPredicate {
    /**
     * The method should set determine all predicates in the sentence.
     * @param sentence The sentence for which predicates will be determined automatically.
     * @return True if the auto predicate is successful.
     */
    public abstract boolean autoPredicate(AnnotatedSentence sentence);
}
