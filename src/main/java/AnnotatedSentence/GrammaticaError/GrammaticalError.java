package AnnotatedSentence.GrammaticaError;

public class GrammaticalError {

    private String error;
    private String operation;
    private String editedWord;
    private String wordIndex;

    /**
     * Constructor for the GrammaticalError class. Given a string of fields separated with '-', the method divides
     * the fields and set them.
     * @param layerValue Value of fields concatenated with '-'.
     */
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

    /**
     * Accessor for the error field.
     * @return Error field.
     */
    public String getError() {
        return error;
    }

    /**
     * Mutator for the error field.
     * @param error New error field.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Accessor for the editedWord
     * @return EditedWord field.
     */
    public String getEditedWord(){
        return editedWord;
    }

    /**
     * Mutator for the editedWord
     * @param editedWord New editedWord.
     */
    public void setEditedWord(String editedWord){
        this.editedWord = editedWord;
    }

    /**
     * Accessor for the operation field.
     * @return Operation field.
     */
    public String getOperation(){
        return operation;
    }

    /**
     * Mutator for the operation field.
     * @param operation New operation field.
     */
    public void setOperation(String operation){
        this.operation = operation;
    }

    /**
     * Accessor for the wordIndex field.
     * @return WordIndex field.
     */
    public String getWordIndex(){
        return wordIndex;
    }

    /**
     * Mutator for the wordIndex field.
     * @param wordIndex New wordIndex field.
     */
    public void setWordIndex(String wordIndex){
        this.wordIndex = wordIndex;
    }

    /**
     * Overridden toString method which converts a GrammaticalError object to a string.
     * @return String version of the grammatical error.
     */
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
