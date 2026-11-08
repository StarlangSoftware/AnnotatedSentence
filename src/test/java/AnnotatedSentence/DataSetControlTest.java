package AnnotatedSentence;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DataSetControlTest {

    private HashMap<String, ArrayList<AnnotatedPhrase>> constructDependencies(AnnotatedCorpus corpus){
        HashMap<String, ArrayList<AnnotatedPhrase>> result = new HashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            int rootWordIndex = -1;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equalsIgnoreCase("root")){
                    rootWordIndex = j;
                    break;
                }
            }
            if (rootWordIndex != -1){
                ArrayList<AnnotatedPhrase> groups = sentence.getDependencyGroups(rootWordIndex + 1);
                result.put(sentence.getFileName(), groups);
            }
        }
        return result;
    }

    private HashMap<String, ArrayList<AnnotatedPhrase>> constructFrameElements(AnnotatedCorpus corpus){
        HashMap<String, ArrayList<AnnotatedPhrase>> result = new HashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            String predicateId = null;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equalsIgnoreCase("root")){
                    predicateId = word.getSemantic();
                    break;
                }
            }
            if (predicateId != null){
                ArrayList<AnnotatedPhrase> groups = sentence.getFrameElementGroups(predicateId);
                if (!groups.isEmpty()){
                    result.put(sentence.getFileName(), groups);
                }
            }
        }
        return result;
    }

    private HashMap<String, ArrayList<AnnotatedPhrase>> constructPropbankArguments(AnnotatedCorpus corpus){
        HashMap<String, ArrayList<AnnotatedPhrase>> result = new HashMap<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            String predicateId = null;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equalsIgnoreCase("root")){
                    predicateId = word.getSemantic();
                    break;
                }
            }
            if (predicateId != null){
                ArrayList<AnnotatedPhrase> groups = sentence.getArgumentGroups(predicateId);
                if (!groups.isEmpty()){
                    result.put(sentence.getFileName(), groups);
                }
            }
        }
        return result;
    }

    private void matchGroups(String matchType, String prefix, HashMap<String, ArrayList<AnnotatedPhrase>> dependencyGroups, HashMap<String, ArrayList<AnnotatedPhrase>> srlgroups){
        for (String file : srlgroups.keySet()){
            ArrayList<AnnotatedPhrase> srlPhrases = srlgroups.get(file);
            ArrayList<AnnotatedPhrase> dependencyPhrases = dependencyGroups.get(file);
            boolean allFound = true;
            for (AnnotatedPhrase srlPhrase : srlPhrases){
                boolean found = false;
                for (AnnotatedPhrase dependencyPhrase : dependencyPhrases){
                    if (srlPhrase.toWords().equals(dependencyPhrase.toWords())){
                        found = true;
                        break;
                    }
                }
                if (!found){
                    allFound = false;
                    System.out.println(matchType + " Not found: " + prefix + file + " Phrase: " + srlPhrase.toWords());
                }
            }
            if (allFound){
                System.out.println(matchType + " Clean: " + prefix + file);
            }
        }
    }

    @Test
    public void testSemanticRoleLabeling(){
        AnnotatedCorpus corpus;
        HashMap<String, ArrayList<AnnotatedPhrase>> dependencyGroups, frameElements, propbankArguments;
        corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"));
        dependencyGroups = constructDependencies(corpus);
        frameElements = constructFrameElements(corpus);
        propbankArguments = constructPropbankArguments(corpus);
        matchGroups("ATIS-Framenet", "", dependencyGroups, frameElements);
        matchGroups("ATIS-Propbank", "", dependencyGroups, propbankArguments);
        corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        dependencyGroups = constructDependencies(corpus);
        frameElements = constructFrameElements(corpus);
        propbankArguments = constructPropbankArguments(corpus);
        matchGroups("TOURISM-Framenet", "", dependencyGroups, frameElements);
        matchGroups("TOURISM-Propbank", "", dependencyGroups, propbankArguments);
        corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"));
        dependencyGroups = constructDependencies(corpus);
        frameElements = constructFrameElements(corpus);
        propbankArguments = constructPropbankArguments(corpus);
        matchGroups("KENET-Framenet", "", dependencyGroups, frameElements);
        matchGroups("KENET-Propbank", "", dependencyGroups, propbankArguments);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"));
        dependencyGroups = constructDependencies(corpus);
        frameElements = constructFrameElements(corpus);
        propbankArguments = constructPropbankArguments(corpus);
        matchGroups("PENN15-Framenet", "", dependencyGroups, frameElements);
        matchGroups("PENN15-Propbank", "", dependencyGroups, propbankArguments);
        corpus = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"));
        dependencyGroups = constructDependencies(corpus);
        frameElements = constructFrameElements(corpus);
        propbankArguments = constructPropbankArguments(corpus);
        matchGroups("PENN20-Framenet", "", dependencyGroups, frameElements);
        matchGroups("PENN20-Propbank", "", dependencyGroups, propbankArguments);
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        for (File file:listOfFiles) {
            if (file.isDirectory()) {
                String fileName = file.getName();
                corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName));
                dependencyGroups = constructDependencies(corpus);
                frameElements = constructFrameElements(corpus);
                propbankArguments = constructPropbankArguments(corpus);
                matchGroups("FRAMENET-Framenet", fileName + "/", dependencyGroups, frameElements);
                matchGroups("FRAMENET-Propbank", fileName + "/", dependencyGroups, propbankArguments);
            }
        }
    }

}
