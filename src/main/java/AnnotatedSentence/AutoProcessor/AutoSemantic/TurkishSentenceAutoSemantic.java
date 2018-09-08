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
        AnnotatedWord twoPrevious = null, previous = null, current, twoNext = null, next = null;
        for (int i = 0; i < sentence.wordCount(); i++){
            current = (AnnotatedWord) sentence.getWord(i);
            if (i > 1){
                twoPrevious = (AnnotatedWord) sentence.getWord(i - 2);
            }
            if (i > 0){
                previous = (AnnotatedWord) sentence.getWord(i - 1);
            }
            if (i != sentence.wordCount() - 1){
                next = (AnnotatedWord) sentence.getWord(i + 1);
            }
            if (i < sentence.wordCount() - 2){
                twoNext = (AnnotatedWord) sentence.getWord(i + 2);
            }
            if (current.getSemantic() == null && current.getParse() != null){
                if (twoPrevious != null && twoPrevious.getParse() != null && previous.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(twoPrevious.getParse(), previous.getParse(), current.getParse(), twoPrevious.getMetamorphicParse(), previous.getMetamorphicParse(), current.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                        continue;
                    }
                }
                if (previous != null && previous.getParse() != null && next != null && next.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(previous.getParse(), current.getParse(), next.getParse(), previous.getMetamorphicParse(), current.getMetamorphicParse(), next.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                        continue;
                    }
                }
                if (next != null && next.getParse() != null && twoNext != null && twoNext.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(current.getParse(), next.getParse(), twoNext.getParse(), current.getMetamorphicParse(), next.getMetamorphicParse(), twoNext.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                        continue;
                    }
                }
                if (previous != null && previous.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(previous.getParse(), current.getParse(), previous.getMetamorphicParse(), current.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                        continue;
                    }
                }
                if (current.getSemantic() == null && next != null && next.getParse() != null){
                    ArrayList<SynSet> idioms = turkishWordNet.constructIdiomSynSets(current.getParse(), next.getParse(), current.getMetamorphicParse(), next.getMetamorphicParse(), fsm);
                    if (idioms.size() == 1){
                        current.setSemantic(idioms.get(0).getId());
                        continue;
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
