package AnnotatedSentence.AutoProcessor.AutoDependency;

import AnnotatedSentence.AnnotatedSentence;

public abstract class SentenceAutoDependency {
    /**
     * The method should set all the dependency labels in the sentence. The method assumes that the morphological
     * analysis is done previously.
     * @param sentence The sentence for which dependency labels will be determined automatically.
     */
    public abstract void autoDependency(AnnotatedSentence sentence);
}
