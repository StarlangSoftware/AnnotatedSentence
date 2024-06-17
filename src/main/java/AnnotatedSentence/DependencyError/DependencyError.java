package AnnotatedSentence.DependencyError;

import AnnotatedSentence.AnnotatedWord;

public class DependencyError {

    private final DependencyErrorType dependencyErrorType;
    private final int node;

    private final AnnotatedWord word;
    private final String relation;
    private final String morphologicalTag;

    /**
     * Constructor of the DependencyError class. Sets the fields of the object.
     * @param dependencyErrorType Dependency error type
     * @param node Node of the dependency tree in which this error occurs.
     * @param word Name of the word in which this error occurs.
     * @param relation Dependency relation possibly associated with the error. If it is empty, no association.
     * @param morphologicalTag UD tag possibly associated with the error. If it is empty, no association.
     */
    public DependencyError(DependencyErrorType dependencyErrorType, int node, AnnotatedWord word, String relation, String morphologicalTag){
        this.dependencyErrorType = dependencyErrorType;
        this.node = node;
        this.word = word;
        this.relation = relation;
        this.morphologicalTag = morphologicalTag;
    }

    /**
     * Accessor for the dependencyErrorType field.
     * @return DependencyErrorType field.
     */
    public DependencyErrorType getDependencyErrorType() {
        return dependencyErrorType;
    }

    /**
     * Accessor for the node field
     * @return Node field.
     */
    public int getNode() {
        return node;
    }

    /**
     * Accessor for the relation field.
     * @return Relation field.
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Accessor for the morphologicalTag field.
     * @return MorphologicalTag field.
     */
    public String getMorphologicalTag(){
        return morphologicalTag;
    }

    /**
     * Overridden toString method to convert a DependencyError to string. If the relation is empty, returns word
     * concatenated with dependency error type. Else if morphological tag is empty, returns word concatenated with
     * dependency error type and relation. For all other cases, returns word concatenated with dependency error type,
     * relation and morphological tags.
     * @return String version of a DependencyError
     */
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
