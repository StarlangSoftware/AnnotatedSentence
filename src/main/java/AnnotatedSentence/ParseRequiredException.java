package AnnotatedSentence;

public class ParseRequiredException extends Exception{
    private String word;

    public ParseRequiredException(String word){
        this.word = word;
    }

    public String toString(){
        return "Morphological analysis required for word " + word;
    }

    public String getWord(){
        return word;
    }
}
