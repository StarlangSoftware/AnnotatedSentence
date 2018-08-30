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
        AnnotatedWord previous = null, current, next = null;
        for (int i = 0; i < sentence.wordCount(); i++){
            current = (AnnotatedWord) sentence.getWord(i);
            if (i > 0){
                previous = (AnnotatedWord) sentence.getWord(i - 1);
            }
            if (i != sentence.wordCount() - 1){
                next = (AnnotatedWord) sentence.getWord(i + 1);
            }
            if (current.getSemantic() == null && current.getParse() != null){
                if (previous != null && previous.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(previous.getParse(), current.getParse(), previous.getMetamorphicParse(), current.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                    }
                }
                if (current.getSemantic() == null && next != null && next.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(current.getParse(), next.getParse(), current.getMetamorphicParse(), next.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                    }
                }
                ArrayList<SynSet> meanings = turkishWordNet.constructSynSets(current.getParse().getWord().getName(), current.getParse(), current.getMetamorphicParse(), fsm);
                if (current.getSemantic() == null && meanings.size() == 1){
                    current.setSemantic(meanings.get(0).getId());
                }
            }
        }
    }
}
