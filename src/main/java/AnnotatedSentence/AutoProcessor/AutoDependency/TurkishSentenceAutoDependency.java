package AnnotatedSentence.AutoProcessor.AutoDependency;

import AnnotatedSentence.*;

public class TurkishSentenceAutoDependency extends SentenceAutoDependency{

    private int lastNounIndex(AnnotatedSentence sentence, int startIndex){
        for (int i = startIndex; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getParse() == null){
                return -1;
            } else {
                if (!word.getParse().getUniversalDependencyPos().equals("DET") && !word.getParse().getUniversalDependencyPos().equals("ADJ") && !word.getParse().getUniversalDependencyPos().equals("NOUN") && !word.getParse().getUniversalDependencyPos().equals("PROPN")){
                    return i - 1;
                }
            }
        }
        return -1;
    }

    @Override
    public void autoDependency(AnnotatedSentence sentence) {
        AnnotatedWord word, nextWord, candidate;
        int j;
        /* Compound determination from semantic annotation */
        for (int i = 0; i < sentence.wordCount() - 1; i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            nextWord = (AnnotatedWord) sentence.getWord(i + 1);
            if (word.getSemantic() != null && nextWord.getSemantic() != null && word.getSemantic().equals(nextWord.getSemantic())){
                word.setUniversalDependency(i + 2, "COMPOUND");
            }
        }
        /* Root determination from propbank or framenet annotation*/
        for (int i = sentence.wordCount() - 1; i >= 0; i--){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if ((word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")) ||
                    (word.getFrameElement() != null && word.getFrameElement().getFrameElementType().equals("PREDICATE"))){
                word.setUniversalDependency(0, "ROOT");
                break;
            }
        }
        /* Aux determination from morphology*/
        for (int i = 0; i < sentence.wordCount(); i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if (word.getParse() != null && word.getParse().getUniversalDependencyPos().equals("AUX")){
                word.setUniversalDependency(i, "AUX");
            }
        }
        /* . determination from morphology*/
        word = (AnnotatedWord) sentence.getWord(sentence.wordCount() - 1);
        if (word.getUniversalDependency() == null && word.getName().equals(".")){
            word.setUniversalDependency(sentence.wordCount() - 1, "PUNCT");
        }
        /* Nummod determination from morphology*/
        for (int i = 0; i < sentence.wordCount() - 1; i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if (word.getParse() != null && word.getParse().getUniversalDependencyPos().equals("NUM")){
                word.setUniversalDependency(i + 2, "NUMMOD");
            }
        }
        /* Amod determination from morphology*/
        for (int i = 0; i < sentence.wordCount() - 1; i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if (word.getParse() != null && word.getParse().getUniversalDependencyPos().equals("ADJ")){
                int nounIndex = lastNounIndex(sentence, i + 1);
                if (nounIndex != -1){
                    word.setUniversalDependency(nounIndex + 1, "AMOD");
                }
            }
        }
        /* Det determination from morphology*/
        for (int i = 0; i < sentence.wordCount() - 1; i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if (word.getParse() != null && word.getParse().getUniversalDependencyPos().equals("DET")){
                word.setUniversalDependency(i + 2, "DET");
            }
        }
        /* Case determination from morphology*/
        for (int i = 0; i < sentence.wordCount(); i++){
            word = (AnnotatedWord) sentence.getWord(i);
            if (word.getUniversalDependency() != null){
                continue;
            }
            if (word.getParse() != null && word.getParse().getUniversalDependencyPos().equals("ADP")){
                word.setUniversalDependency(i, "CASE");
            }
        }
    }
}
