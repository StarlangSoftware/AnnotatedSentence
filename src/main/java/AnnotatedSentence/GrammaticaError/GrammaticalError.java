package AnnotatedSentence.GrammaticaError;

public class GrammaticalError {

    private String error;
    private String operation;
    private String editedWord;
    private String wordIndex;

    public GrammaticalError(String layerValue){
        String[] probs = layerValue.split("-");
        if (probs.length >= 1) {
            error = probs[0];
        } else {
            error = "";
        }
        if (probs.length >= 2) {
            operation = probs[1];
        } else {
            operation = "";
        }
        if (probs.length >= 3) {
            editedWord = probs[2];
        } else {
            editedWord = "";
        }
        if (probs.length >= 4) {
            wordIndex = probs[3];
        } else {
            wordIndex = "";
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEditedWord(){
        return editedWord;
    }

    public void setEditedWord(String editedWord){
        this.editedWord = editedWord;
    }

    public String getOperation(){
        return operation;
    }

    public void setOperation(String operation){
        this.operation = operation;
    }

    public String getWordIndex(){
        return wordIndex;
    }

    public void setWordIndex(String wordIndex){
        this.wordIndex = wordIndex;
    }

    public String toString(){
        String result = "";
        if (error != null){
            result = result +  error + "-";
        }
        if (operation != null){
            result = result + operation + "-";
        }
        if (editedWord != null){
            result = result + editedWord + "-";
        }
        if (wordIndex != null) {
            result = result + wordIndex;
        }
        return result;
    }

}
