package AnnotatedSentence;

public class LayerNotExistsException extends Exception{
    private String layerName;

    public LayerNotExistsException(String layerName){
        this.layerName = layerName;
    }

    public String toString(){
        return "Layer " + layerName + " does not exist";
    }
}
