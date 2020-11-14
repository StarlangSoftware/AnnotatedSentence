package AnnotatedSentence.DependencyError;

public class DependencyError {

    private DependencyErrorType dependencyErrorType;
    private int node;
    private String relation;
    private String morphologicalTag;

    public DependencyError(DependencyErrorType dependencyErrorType, int node, String relation, String morphologicalTag){
        this.dependencyErrorType = dependencyErrorType;
        this.node = node;
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
            return "Node " + node + ":" + dependencyErrorType.toString();
        } else {
            if (morphologicalTag.isEmpty()){
                return "Node " + node + ":" + dependencyErrorType.toString() + " <---> " + relation;
            } else {
                return "Node " + node + ":" + dependencyErrorType.toString() + " <---> " + relation + " should not be " + morphologicalTag;
            }
        }
    }

}
