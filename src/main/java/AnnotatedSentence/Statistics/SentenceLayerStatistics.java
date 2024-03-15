package AnnotatedSentence.Statistics;

import AnnotatedSentence.*;
import AnnotatedSentence.AnnotatedSentence;
import DataStructure.CounterHashMap;

import java.util.Locale;
import java.util.function.Function;

public class SentenceLayerStatistics extends LayerStatistics{
    private final AnnotatedCorpus corpus;

    public SentenceLayerStatistics(AnnotatedCorpus corpus){
        this.corpus = corpus;
        counts = new CounterHashMap<>();
    }

    public void calculateStatistics(ViewLayerType viewLayerType){
        counts = new CounterHashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                String layerInfo = word.getLayerInfo(viewLayerType);
                if (layerInfo != null){
                    if (viewLayerType.equals(ViewLayerType.TURKISH_WORD)){
                        layerInfo = layerInfo.toLowerCase(new Locale("tr"));
                    }
                    counts.put(layerInfo);
                }
            }
        }
    }

    private void calculateStatisticsWithParse(Function<AnnotatedWord, String> wordProperty){
        counts = new CounterHashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getParse() != null){
                    counts.put(wordProperty.apply(word));
                }
            }
        }
    }

    public void calculatePosStatistics(){
        calculateStatisticsWithParse(annotatedWord -> annotatedWord.getParse().getPos());
    }

    public void calculateRootPosStatistics(){
        calculateStatisticsWithParse(annotatedWord -> annotatedWord.getParse().getRootPos());
    }

    public void calculateRootWithPosStatistics(){
        calculateStatisticsWithParse(annotatedWord -> annotatedWord.getParse().getWordWithPos().getName());
    }

    public void calculateRootWordStatistics(){
        calculateStatisticsWithParse(annotatedWord -> annotatedWord.getParse().getWord().getName());
    }

}
