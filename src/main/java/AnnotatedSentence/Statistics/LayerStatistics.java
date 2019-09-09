package AnnotatedSentence.Statistics;

import DataStructure.CounterHashMap;
import WordNet.WordNet;

import java.util.Map;

public class LayerStatistics {
    protected CounterHashMap<String> counts;

    /**
     * Prints top N statistics
     * @param N Parameter N
     */
    public void printTopN(int N){
        for (Map.Entry<String, Integer> entry: counts.topN(N)){
            System.out.println(String.format("%20s\t%10d", entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Print all occurrence statistics
     */
    public void printStatistics(){
        double sum = counts.sumOfCounts();
        for (String key : counts.keySet()){
            System.out.println(String.format("%20s\t%10d\t%3.2f", key, counts.get(key), 100 * counts.get(key) / sum));
        }
        System.out.println(String.format("%20s\t%10d\t%3.2f", "TOTAL", counts.sumOfCounts(), 100.0));
    }

    public void printStatistics(WordNet wordNet){
        double sum = counts.sumOfCounts();
        for (String key : counts.keySet()){
            if (wordNet.getSynSetWithId(key) != null){
                System.out.println(String.format("%20s\t%s\t%10d\t%3.2f", key, wordNet.getSynSetWithId(key).getSynonym(), counts.get(key), 100 * counts.get(key) / sum));
            } else {
                System.out.println(String.format("%20s\t%10d\t%3.2f", key, counts.get(key), 100 * counts.get(key) / sum));
            }
        }
        System.out.println(String.format("%20s\t%10d\t%3.2f", "TOTAL", counts.sumOfCounts(), 100.0));
    }

}
