package AnnotatedSentence.AutoProcessor.AutoSemantic;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import WordNet.*;

import java.util.ArrayList;

public class TurkishSentenceAutoSemantic extends SentenceAutoSemantic{

    private WordNet turkishWordNet;
    private FsmMorphologicalAnalyzer fsm;

    public TurkishSentenceAutoSemantic(WordNet turkishWordNet, FsmMorphologicalAnalyzer fsm){
        this.turkishWordNet = turkishWordNet;
        this.fsm = fsm;
    }

    protected void autoLabelSingleSemantics(AnnotatedSentence sentence) {
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getSemantic() == null){
                ArrayList<SynSet> meanings = turkishWordNet.constructSynSets(word.getParse().getWord().getName(), word.getParse(), word.getMetamorphicParse(), fsm);
                if (meanings.size() == 1){
                    word.setSemantic(meanings.get(0).getId());
                }
            }
        }
    }
}
