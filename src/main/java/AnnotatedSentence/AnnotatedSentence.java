package AnnotatedSentence;

import AnnotatedSentence.DependencyError.DependencyError;
import AnnotatedSentence.DependencyError.DependencyErrorType;
import Corpus.Sentence;
import Dictionary.Word;
import FrameNet.FrameNet;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.MetamorphicParse;
import MorphologicalAnalysis.MorphologicalParse;
import PropBank.FramesetList;
import WordNet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AnnotatedSentence extends Sentence{
    private File file;

    /**
     * Empty constructor for the {@link AnnotatedSentence} class.
     */
    public AnnotatedSentence(){
    }

    /**
     * Reads an annotated sentence from a text file.
     * @param file File containing the annotated sentence.
     */
    public AnnotatedSentence(File file){
        this.file = file;
        words = new ArrayList<Word>();
        try {
            Scanner sc = new Scanner(file, "UTF8");
            while (sc.hasNext()){
                words.add(new AnnotatedWord(sc.next()));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a simple sentence to an annotated sentence
     * @param sentence Simple sentence
     */
    public AnnotatedSentence(String sentence){
        String[] wordArray;
        words = new ArrayList<>();
        wordArray = sentence.split(" ");
        for (String word : wordArray){
            if (!word.isEmpty()){
                words.add(new AnnotatedWord(word));
            }
        }
    }

    /**
     * The method constructs all possible shallow parse groups of a sentence.
     * @return Shallow parse groups of a sentence.
     */
    public ArrayList<AnnotatedPhrase> getShallowParseGroups(){
        ArrayList<AnnotatedPhrase> shallowParseGroups = new ArrayList<>();
        AnnotatedWord previousWord = null;
        AnnotatedPhrase current = null;
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord annotatedWord = (AnnotatedWord) getWord(i);
            if (previousWord == null){
                current = new AnnotatedPhrase(i, annotatedWord.getShallowParse());
            } else {
                if (previousWord.getShallowParse() != null && !previousWord.getShallowParse().equals(annotatedWord.getShallowParse())){
                    shallowParseGroups.add(current);
                    current = new AnnotatedPhrase(i, annotatedWord.getShallowParse());
                }
            }
            current.addWord(annotatedWord);
            previousWord = annotatedWord;
        }
        shallowParseGroups.add(current);
        return shallowParseGroups;
    }

    /**
     * The method checks all words in the sentence and returns true if at least one of the words is annotated with
     * PREDICATE tag.
     * @return True if at least one of the words is annotated with PREDICATE tag; false otherwise.
     */
    public boolean containsPredicate(){
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgument() != null && annotatedWord.getArgument().getArgumentType().equals("PREDICATE")){
                return true;
            }
        }
        return false;
    }

    /**
     * The method checks all words in the sentence and returns true if at least one of the words is annotated with
     * PREDICATE tag.
     * @return True if at least one of the words is annotated with PREDICATE tag; false otherwise.
     */
    public boolean containsFramePredicate(){
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getFrameElement() != null && annotatedWord.getFrameElement().getFrameElementType().equals("PREDICATE")){
                return true;
            }
        }
        return false;
    }

    public boolean updateConnectedPredicate(String previousId, String currentId){
        boolean modified = false;
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgument() != null && annotatedWord.getArgument().getId() != null && annotatedWord.getArgument().getId().equals(previousId)){
                annotatedWord.setArgument(annotatedWord.getArgument().getArgumentType() + "$" + currentId);
                modified = true;
            }
            if (annotatedWord.getFrameElement() != null && annotatedWord.getFrameElement().getId() != null && annotatedWord.getFrameElement().getId().equals(previousId)){
                annotatedWord.setFrameElement(annotatedWord.getFrameElement().getFrameElementType() + "$" + annotatedWord.getFrameElement().getFrame() + "$" + currentId);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * The method returns all possible words, which is
     * 1. Verb
     * 2. Its semantic tag is assigned
     * 3. A frameset exists for that semantic tag
     * @param framesetList Frameset list that contains all frames for Turkish
     * @return An array of words, which are verbs, semantic tags assigned, and framesetlist assigned for that tag.
     */
    public ArrayList<AnnotatedWord> predicateCandidates(FramesetList framesetList){
        ArrayList<AnnotatedWord> candidateList = new ArrayList<>();
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getParse() != null && annotatedWord.getParse().isVerb() && annotatedWord.getSemantic() != null && framesetList.frameExists(annotatedWord.getSemantic())){
                candidateList.add(annotatedWord);
            }
        }
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < words.size() - i - 1; j++){
                AnnotatedWord annotatedWord = (AnnotatedWord) words.get(j);
                AnnotatedWord nextAnnotatedWord = (AnnotatedWord) words.get(j + 1);
                if (!candidateList.contains(annotatedWord) && candidateList.contains(nextAnnotatedWord) && annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals(nextAnnotatedWord.getSemantic())){
                    candidateList.add(annotatedWord);
                }
            }
        }
        return candidateList;
    }

    /**
     * The method returns all possible words, which is
     * 1. Verb
     * 2. Its semantic tag is assigned
     * 3. A frameset exists for that semantic tag
     * @param frameNet FrameNet list that contains all frames for Turkish
     * @return An array of words, which are verbs, semantic tags assigned, and framenet assigned for that tag.
     */
    public ArrayList<AnnotatedWord> predicateFrameCandidates(FrameNet frameNet){
        ArrayList<AnnotatedWord> candidateList = new ArrayList<>();
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getParse() != null && annotatedWord.getParse().isVerb() && annotatedWord.getSemantic() != null && frameNet.lexicalUnitExists(annotatedWord.getSemantic())){
                candidateList.add(annotatedWord);
            }
        }
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < words.size() - i - 1; j++){
                AnnotatedWord annotatedWord = (AnnotatedWord) words.get(j);
                AnnotatedWord nextAnnotatedWord = (AnnotatedWord) words.get(j + 1);
                if (!candidateList.contains(annotatedWord) && candidateList.contains(nextAnnotatedWord) && annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals(nextAnnotatedWord.getSemantic())){
                    candidateList.add(annotatedWord);
                }
            }
        }
        return candidateList;
    }

    /**
     * Returns the i'th predicate in the sentence.
     * @param index Predicate index
     * @return The predicate with index index in the sentence.
     */
    public String getPredicate(int index){
        int count1  = 0, count2 = 0;
        String data = "";
        ArrayList<AnnotatedWord> word = new ArrayList<>();
        ArrayList<MorphologicalParse> parse = new ArrayList<>();
        if (index < wordCount()){
            for (int i = 0; i < wordCount(); i++) {
                word.add((AnnotatedWord) getWord(i));
                parse.add(word.get(i).getParse());
            }
            for (int i = index; i >= 0; i--) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")){
                    count1 = index - i;
                    break;
                }
            }
            for (int i = index; i < wordCount() - index; i++) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")){
                    count2 = i - index;
                    break;
                }
            }
            if (count1 > count2){
                data = word.get(count1).getName();
            }
            else{
                data = word.get(count2).getName();
            }
        }
        return data;
    }

    /**
     * Returns file name of the sentence
     * @return File name of the sentence
     */
    public String getFileName(){
        return file.getName();
    }

    /**
     * Removes the i'th word from the sentence
     * @param index Word index
     */
    public void removeWord(int index){
        for (Word value : words) {
            AnnotatedWord word = (AnnotatedWord) value;
            if (word.getUniversalDependency() != null) {
                if (word.getUniversalDependency().to() == index + 1) {
                    word.setUniversalDependency(-1, "ROOT");
                } else {
                    if (word.getUniversalDependency().to() > index + 1) {
                        word.setUniversalDependency(word.getUniversalDependency().to() - 1, word.getUniversalDependency().toString());
                    }
                }
            }
        }
        words.remove(index);
    }

    /**
     * The toStems method returns an accumulated string of each word's stems in words {@link ArrayList}.
     * If the parse of the word does not exist, the method adds the surfaceform to the resulting string.
     *
     * @return String result which has all the stems of each item in words {@link ArrayList}.
     */
    public String toStems() {
        String result;
        AnnotatedWord annotatedWord;
        if (words.size() > 0) {
            annotatedWord = (AnnotatedWord) words.get(0);
            if (annotatedWord.getParse() != null){
                result = annotatedWord.getParse().getWord().getName();
            } else {
                result = annotatedWord.getName();
            }
            for (int i = 1; i < words.size(); i++) {
                annotatedWord = (AnnotatedWord) words.get(i);
                if (annotatedWord.getParse() != null){
                    result = result + " " + annotatedWord.getParse().getWord().getName();
                } else {
                    result = result + " " + annotatedWord.getName();
                }
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * Saves the current sentence.
     */
    public void save(){
        writeToFile(file);
    }

    /**
     * Saves the current sentence.
     * @param fileName Name of the output file.
     */
    public void save(String fileName){
        writeToFile(new File(fileName));
    }

    public static boolean checkDependencyWithUniversalPosTag(String dependency, String universalPos){
        if (dependency.equals("ADVMOD")){
            if (!universalPos.equals("ADV") && !universalPos.equals("ADJ") && !universalPos.equals("CCONJ") &&
                    !universalPos.equals("DET") && !universalPos.equals("PART") && !universalPos.equals("SYM")){
                return false;
            }
        }
        if (dependency.equals("AUX") && !universalPos.equals("AUX")){
            return false;
        }
        if (dependency.equals("CASE")){
            if (universalPos.equals("PROPN") || universalPos.equals("ADJ") || universalPos.equals("PRON") ||
                    universalPos.equals("DET") || universalPos.equals("NUM") || universalPos.equals("AUX")){
                return false;
            }
        }
        if (dependency.equals("MARK") || dependency.equals("CC")){
            if (universalPos.equals("NOUN") || universalPos.equals("PROPN") || universalPos.equals("ADJ") ||
                    universalPos.equals("PRON") || universalPos.equals("DET") || universalPos.equals("NUM") ||
                    universalPos.equals("VERB") || universalPos.equals("AUX") || universalPos.equals("INTJ")){
                return false;
            }
        }
        if (dependency.equals("COP")){
            if (!universalPos.equals("AUX") && !universalPos.equals("PRON") &&
                    !universalPos.equals("DET") && !universalPos.equals("SYM")){
                return false;
            }
        }
        if (dependency.equals("DET")){
            if (!universalPos.equals("DET") && !universalPos.equals("PRON")){
                return false;
            }
        }
        if (dependency.equals("NUMMOD")){
            if (!universalPos.equals("NUM") && !universalPos.equals("NOUN") && !universalPos.equals("SYM")){
                return false;
            }
        }
        if (!dependency.equals("PUNCT") && universalPos.equals("PUNCT")){
            return false;
        }
        if (dependency.equals("COMPOUND") && universalPos.equals("AUX")){
            return false;
        }
        return true;
    }

    public boolean checkForNonProjectivityOfPunctuation(int from, int to){
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null) {
                int currentTo = word.getUniversalDependency().to();
                int currentFrom = i + 1;
                if (currentFrom > min && currentFrom < max && (currentTo < min || currentTo > max)){
                    return false;
                }
                if (currentTo > min && currentTo < max && (currentFrom < min || currentFrom > max)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkMultipleRoots(){
        int rootCount = 0;
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("ROOT")) {
                rootCount++;
            }
        }
        return rootCount <= 1;
    }

    public boolean checkMultipleSubjects(){
        int subjectCount = 0;
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("NSUBJ") && word.getUniversalDependency().to() - 1 >= 0 && word.getUniversalDependency().to() - 1 < wordCount()) {
                AnnotatedWord toWord = (AnnotatedWord) getWord(word.getUniversalDependency().to() - 1);
                if (toWord.getUniversalDependency() != null && toWord.getUniversalDependency().toString().equals("ROOT")){
                    subjectCount++;
                }
            }
        }
        return subjectCount <= 1;
    }

    public ArrayList<DependencyError> getDependencyErrors(){
        ArrayList<DependencyError> errorList = new ArrayList<>();
        if (!checkMultipleRoots()){
            errorList.add(new DependencyError(DependencyErrorType.MULTIPLE_ROOT, 0, "", ""));
        }
        if (!checkMultipleSubjects()){
            errorList.add(new DependencyError(DependencyErrorType.MULTIPLE_SUBJECTS, 0, "", ""));
        }
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getParse() == null){
                errorList.add(new DependencyError(DependencyErrorType.NO_MORPHOLOGICAL_ANALYSIS, i + 1, "", ""));
            }
            if (word.getUniversalDependency() != null){
                int to = word.getUniversalDependency().to();
                int from = i + 1;
                String dependency = word.getUniversalDependency().toString();
                if (from == to){
                    errorList.add(new DependencyError(DependencyErrorType.HEAD_EQUALS_ID, from, "", ""));
                }
                if (dependency.equals("PUNCT") && !checkForNonProjectivityOfPunctuation(from, to)){
                    errorList.add(new DependencyError(DependencyErrorType.PUNCTUATION_NOT_PROJECTIVE, from, "", ""));
                }
                if (to > from && (dependency.equals("CONJ") || dependency.equals("GOESWITH") ||
                        dependency.equals("FIXED") || dependency.equals("FLAT") || dependency.equals("APPOS"))){
                    errorList.add(new DependencyError(DependencyErrorType.GO_LEFT_TO_RIGHT, from, dependency, ""));
                }
                if (from > to && from > to + 1 && dependency.equals("GOESWITH")){
                    errorList.add(new DependencyError(DependencyErrorType.GAPS_IN_GOESWITH, from, "", ""));
                }
                if (word.getParse() != null){
                    String universalPos = word.getParse().getUniversalDependencyPos();
                    if (!checkDependencyWithUniversalPosTag(dependency, universalPos)){
                        errorList.add(new DependencyError(DependencyErrorType.SHOULDNT_BE_OF_POS, from, dependency, universalPos));
                    }
                }
                if (to > 0 && to <= wordCount()){
                    AnnotatedWord toWord = (AnnotatedWord) getWord(to - 1);
                    if (toWord.getUniversalDependency() != null){
                        String toDependency = toWord.getUniversalDependency().toString();
                        if (toDependency.equals("AUX") || toDependency.equals("COP") || toDependency.equals("CC") ||
                                toDependency.equals("FIXED") || toDependency.equals("GOESTWITH") || toDependency.equals("CASE") ||
                                toDependency.equals("MARK") || toDependency.equals("PUNCT")){
                            errorList.add(new DependencyError(DependencyErrorType.NOT_EXPECTED_TO_HAVE_CHILDREN, from, toDependency, ""));
                        }
                        if (dependency.equals("ORPHAN") && !toDependency.equals("CONJ")){
                            errorList.add(new DependencyError(DependencyErrorType.PARENT_ORPHAN_SHOULD_BE_CONJ, from, toDependency, ""));
                        }
                    }
                }
            } else {
                errorList.add(new DependencyError(DependencyErrorType.NO_DEPENDENCY, i + 1, "", ""));
            }
        }
        return errorList;
    }

    public String getUniversalDependencyFormat(String path){
        String result = "# sent_id = " + path + getFileName() + "\n" + "# text = " + toWords() + "\n";
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            result += (i + 1) + "\t" + word.getUniversalDependencyFormat(wordCount()) + "\n";
        }
        result += "\n";
        return result;
    }

    public String getUniversalDependencyFormat(){
        String result = "# sent_id = " + getFileName() + "\n" + "# text = " + toWords() + "\n";
        for (int i = 0; i < wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            result += (i + 1) + "\t" + word.getUniversalDependencyFormat(wordCount()) + "\n";
        }
        result += "\n";
        return result;
    }

    /**
     * Creates a list of literal candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible root forms of the i'th word in the sentence
     * 2. All possible 2-word expressions containing the i'th word in the sentence
     * 3. All possible 3-word expressions containing the i'th word in the sentence
     * @param wordNet Turkish wordnet
     * @param fsm Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of literal candidates containing all possible root forms and multiword expressions.
     */
    public ArrayList<Literal> constructLiterals(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        ArrayList<Literal> possibleLiterals = new ArrayList<>();
        MorphologicalParse morphologicalParse = word.getParse();
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        possibleLiterals.addAll(wordNet.constructLiterals(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
        AnnotatedWord firstSucceedingWord = null;
        AnnotatedWord secondSucceedingWord = null;
        if (wordCount() > wordIndex + 1) {
            firstSucceedingWord = (AnnotatedWord) getWord(wordIndex + 1);
            if (wordCount() > wordIndex + 2) {
                secondSucceedingWord = (AnnotatedWord) getWord(wordIndex + 2);
            }
        }
        if (firstSucceedingWord != null) {
            if (secondSucceedingWord != null) {
                possibleLiterals.addAll(wordNet.constructIdiomLiterals(word.getParse(), firstSucceedingWord.getParse(), secondSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), secondSucceedingWord.getMetamorphicParse(), fsm));
            }
            possibleLiterals.addAll(wordNet.constructIdiomLiterals(word.getParse(), firstSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        Collections.sort(possibleLiterals, new LiteralWithSenseComparator());
        return possibleLiterals;
    }

    /**
     * Creates a list of synset candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible synsets containing the i'th word in the sentence
     * 2. All possible synsets containing 2-word expressions, which contains the i'th word in the sentence
     * 3. All possible synsets containing 3-word expressions, which contains the i'th word in the sentence
     * @param wordNet Turkish wordnet
     * @param fsm Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of synset candidates containing all possible root forms and multiword expressions.
     * @throws ParseRequiredException When parse does not exists
     */
    public ArrayList<SynSet> constructSynSets(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex) throws ParseRequiredException {
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        ArrayList<SynSet> possibleSynSets = new ArrayList<>();
        MorphologicalParse morphologicalParse = word.getParse();
        if (morphologicalParse == null){
            throw new ParseRequiredException(word.getName());
        }
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        possibleSynSets.addAll(wordNet.constructSynSets(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
        AnnotatedWord firstPrecedingWord = null;
        AnnotatedWord secondPrecedingWord = null;
        AnnotatedWord firstSucceedingWord = null;
        AnnotatedWord secondSucceedingWord = null;
        if (wordIndex > 0) {
            firstPrecedingWord = (AnnotatedWord) getWord(wordIndex - 1);
            if (firstPrecedingWord.getParse() == null){
                throw new ParseRequiredException(word.getName());
            }
            if (wordIndex > 1) {
                secondPrecedingWord = (AnnotatedWord) getWord(wordIndex - 2);
                if (secondPrecedingWord.getParse() == null){
                    throw new ParseRequiredException(word.getName());
                }
            }
        }
        if (wordCount() > wordIndex + 1) {
            firstSucceedingWord = (AnnotatedWord) getWord(wordIndex + 1);
            if (firstSucceedingWord.getParse() == null){
                throw new ParseRequiredException(word.getName());
            }
            if (wordCount() > wordIndex + 2) {
                secondSucceedingWord = (AnnotatedWord) getWord(wordIndex + 2);
                if (secondSucceedingWord.getParse() == null){
                    throw new ParseRequiredException(word.getName());
                }
            }
        }
        if (firstPrecedingWord != null) {
            if (secondPrecedingWord != null) {
                possibleSynSets.addAll(wordNet.constructIdiomSynSets(secondPrecedingWord.getParse(), firstPrecedingWord.getParse(), word.getParse(), secondPrecedingWord.getMetamorphicParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), fsm));
            }
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(firstPrecedingWord.getParse(), word.getParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), fsm));
        }
        if (firstPrecedingWord != null && firstSucceedingWord != null) {
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(firstPrecedingWord.getParse(), word.getParse(), firstSucceedingWord.getParse(), firstPrecedingWord.getMetamorphicParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        if (firstSucceedingWord != null) {
            if (secondSucceedingWord != null) {
                possibleSynSets.addAll(wordNet.constructIdiomSynSets(word.getParse(), firstSucceedingWord.getParse(), secondSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), secondSucceedingWord.getMetamorphicParse(), fsm));
            }
            possibleSynSets.addAll(wordNet.constructIdiomSynSets(word.getParse(), firstSucceedingWord.getParse(), word.getMetamorphicParse(), firstSucceedingWord.getMetamorphicParse(), fsm));
        }
        Collections.sort(possibleSynSets, new SynSetComparator());
        return possibleSynSets;
    }


}
