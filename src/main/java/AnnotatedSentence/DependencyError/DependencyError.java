package AnnotatedSentence.DependencyError;

import AnnotatedSentence.AnnotatedWord;

public class DependencyError {

    private final DependencyErrorType dependencyErrorType;
    private final int node;

    private final AnnotatedWord word;
    private final String relation;
    private final String morphologicalTag;

    public DependencyError(DependencyErrorType dependencyErrorType, int node, AnnotatedWord word, String relation, String morphologicalTag){
        this.dependencyErrorType = dependencyErrorType;
        this.node = node;
        this.word = word;
        this.relation = relation;
        this.morphologicalTag = morphologicalTag;
    }

    public DependencyErrorType getDependencyErrorType() {
        return dependencyErrorType;
    }

    public int getNode() {
        return node;
    }

    public String getRelation() {
        return relation;
    }

    public String getMorphologicalTag(){
        return morphologicalTag;
    }

    public String toString(){
        if (relation.isEmpty()){
            return "Node (" + word.getName() + "):" + dependencyErrorType.toString();
        } else {
            if (morphologicalTag.isEmpty()){
                return "Node (" + word.getName() + "):" + dependencyErrorType.toString() + " <---> " + relation;
            } else {
                return "Node (" + word.getName() + "):" + dependencyErrorType.toString() + " <---> " + relation + " should not be " + morphologicalTag;
            }
        }
    }

}
