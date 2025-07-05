package AnnotatedSentence;

import AnnotatedSentence.DependencyError.DependencyError;
import AnnotatedSentence.DependencyError.DependencyErrorType;
import Corpus.Sentence;
import DependencyParser.ParserEvaluationScore;
import DependencyParser.Universal.UniversalDependencyRelation;
import DependencyParser.Universal.UniversalDependencyTreeBankSentence;
import DependencyParser.Universal.UniversalDependencyTreeBankWord;
import Dictionary.Word;
import FrameNet.FrameNet;
import FrameNet.Frame;
import FrameNet.FrameElementList;
import FrameNet.DisplayedFrame;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.MetamorphicParse;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MorphologicalTag;
import NamedEntityRecognition.NamedEntityType;
import NamedEntityRecognition.Slot;
import PropBank.ArgumentList;
import PropBank.Frameset;
import PropBank.FramesetList;
import SentiNet.PolarityType;
import WordNet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AnnotatedSentence extends Sentence {
    private File file;

    /**
     * Empty constructor for the {@link AnnotatedSentence} class.
     */
    public AnnotatedSentence() {
    }

    /**
     * Reads an annotated sentence from a text file.
     *
     * @param file File containing the annotated sentence.
     */
    public AnnotatedSentence(File file) {
        this.file = file;
        words = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file, "UTF8");
            while (sc.hasNext()) {
                words.add(new AnnotatedWord(sc.next()));
            }
            sc.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    /**
     * Converts a simple sentence to an annotated sentence
     *
     * @param sentence Simple sentence
     */
    public AnnotatedSentence(String sentence) {
        String[] wordArray;
        words = new ArrayList<>();
        wordArray = sentence.split(" ");
        for (String word : wordArray) {
            if (!word.isEmpty()) {
                words.add(new AnnotatedWord(word));
            }
        }
    }

    public AnnotatedSentence(UniversalDependencyTreeBankSentence sentence, File file){
        this.file = file;
        HashMap<Integer, Integer> map = new HashMap<>();
        words = new ArrayList<>();
        int currentSplit = 0;
        int decrease = 0;
        for (int i = 1; i <= sentence.wordCount(); i++){
            if (decrease > 0){
                map.put(i, i - decrease);
            }
            UniversalDependencyTreeBankWord word = (UniversalDependencyTreeBankWord) sentence.getWord(i - 1);
            String surfaceForm = word.getName();
            if (currentSplit < sentence.splitSize() && sentence.getSplit(currentSplit).startsWith(i + "-")){
                for (int j = i + 1; j <= Integer.parseInt(sentence.getSplit(currentSplit).substring(sentence.getSplit(currentSplit).indexOf('-') + 1)); j++){
                    i++;
                    surfaceForm += sentence.getWord(i - 1).getName();
                    decrease++;
                }
                currentSplit++;
            }
            AnnotatedWord newWord = new AnnotatedWord(surfaceForm);
            newWord.setUniversalDependency(word.getRelation().to(), word.getRelation().toString());
            words.add(newWord);
        }
        for (Word word : words){
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getUniversalDependency() != null && map.containsKey(annotatedWord.getUniversalDependency().to())){
                annotatedWord.setUniversalDependency(map.get(annotatedWord.getUniversalDependency().to()), annotatedWord.getUniversalDependency().toString());
            }
        }
    }

    /**
     * Finds the subtrees (returned as phrases) directly connected to the word with the given word index.
     *
     * @param rootWordIndex Word index of the root word. The first word's index is 1.
     * @return The subtrees (returned as phrases) directly connected to the word with the given word index.
     */
    public ArrayList<AnnotatedPhrase> getDependencyGroups(int rootWordIndex) {
        HashMap<Integer, AnnotatedPhrase> groups = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            AnnotatedWord tmpWord = (AnnotatedWord) words.get(i);
            int index = i + 1;
            while (tmpWord.getUniversalDependency().to() != rootWordIndex && tmpWord.getUniversalDependency().to() != 0) {
                index = tmpWord.getUniversalDependency().to();
                tmpWord = (AnnotatedWord) words.get(tmpWord.getUniversalDependency().to() - 1);
            }
            if (tmpWord.getUniversalDependency().to() != 0) {
                AnnotatedPhrase phrase;
                if (groups.containsKey(index)) {
                    phrase = groups.get(index);
                } else {
                    phrase = new AnnotatedPhrase(i, tmpWord.getUniversalDependency().toString());
                    groups.put(index, phrase);
                }
                phrase.addWord(words.get(i));
            }
        }
        ArrayList<AnnotatedPhrase> dependencyGroups = new ArrayList<>();
        dependencyGroups.addAll(groups.values());
        return dependencyGroups;
    }

    /**
     * The method constructs all possible shallow parse groups of a sentence.
     *
     * @return Shallow parse groups of a sentence.
     */
    public ArrayList<AnnotatedPhrase> getShallowParseGroups() {
        ArrayList<AnnotatedPhrase> shallowParseGroups = new ArrayList<>();
        AnnotatedWord previousWord = null;
        AnnotatedPhrase current = null;
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord annotatedWord = (AnnotatedWord) getWord(i);
            if (previousWord == null) {
                current = new AnnotatedPhrase(i, annotatedWord.getShallowParse());
            } else {
                if (previousWord.getShallowParse() != null && !previousWord.getShallowParse().equals(annotatedWord.getShallowParse())) {
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
     *
     * @return True if at least one of the words is annotated with PREDICATE tag; false otherwise.
     */
    public boolean containsPredicate() {
        for (Word word : words) {
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getArgumentList() != null) {
                if (annotatedWord.getArgumentList().containsPredicate()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The method checks all words in the sentence and returns true if at least one of the words is annotated with
     * PREDICATE tag.
     *
     * @return True if at least one of the words is annotated with PREDICATE tag; false otherwise.
     */
    public boolean containsFramePredicate() {
        for (Word word : words) {
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getFrameElementList() != null) {
                if (annotatedWord.getFrameElementList().containsPredicate()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Replaces id's of predicates, which have previousId as synset id, with currentId. Replaces also predicate id's of
     * frame elements, which have predicate id's previousId, with currentId.
     *
     * @param previousId Previous id of the synset.
     * @param currentId  Replacement id.
     * @return Returns true, if any replacement has been done; false otherwise.
     */
    public boolean updateConnectedPredicate(String previousId, String currentId) {
        boolean modified = false;
        for (Word word : words) {
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            ArgumentList argumentList = annotatedWord.getArgumentList();
            if (argumentList != null) {
                if (argumentList.containsPredicateWithId(previousId)) {
                    argumentList.updateConnectedId(previousId, currentId);
                    modified = true;
                }
            }
            FrameElementList frameElementList = annotatedWord.getFrameElementList();
            if (frameElementList != null) {
                if (frameElementList.containsPredicateWithId(previousId)) {
                    frameElementList.updateConnectedId(previousId, currentId);
                    modified = true;
                }
            }
        }
        return modified;
    }

    /**
     * The method returns all possible words, which is
     * 1. Verb
     * 2. Its semantic tag is assigned
     * 3. A frameset exists for that semantic tag
     *
     * @param framesetList Frameset list that contains all frames for Turkish
     * @return An array of words, which are verbs, semantic tags assigned, and framesetlist assigned for that tag.
     */
    public ArrayList<AnnotatedWord> predicateCandidates(FramesetList framesetList) {
        ArrayList<AnnotatedWord> candidateList = new ArrayList<>();
        for (Word word : words) {
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getParse() != null && annotatedWord.getParse().isVerb() && annotatedWord.getSemantic() != null && framesetList.frameExists(annotatedWord.getSemantic())) {
                candidateList.add(annotatedWord);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < words.size() - i - 1; j++) {
                AnnotatedWord annotatedWord = (AnnotatedWord) words.get(j);
                AnnotatedWord nextAnnotatedWord = (AnnotatedWord) words.get(j + 1);
                if (!candidateList.contains(annotatedWord) && candidateList.contains(nextAnnotatedWord) && annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals(nextAnnotatedWord.getSemantic())) {
                    candidateList.add(annotatedWord);
                }
            }
        }
        return candidateList;
    }

    /**
     * Given the new name of the word as text, the method splits the text w.r.t space and inserts all tokens into
     * the position wordIndex in the sentence. For example, if the text is 'kaligrafi yaparak dinleniyorum', then the
     * word will be replaced with 'kaligrafi', and 'yaparak' and 'dinleniyorum' will be inserted after word 'kaligrafi'.
     *
     * @param text      New name of the word, possibly containing space.
     * @param word      Word whose name will be replaced.
     * @param wordIndex Position where the new word(s) will be inserted.
     */
    public void insertWord(String text, AnnotatedWord word, int wordIndex) {
        String[] words = text.split(" ");
        for (int i = words.length - 1; i >= 1; i--) {
            switch (word.getLanguage()) {
                case ENGLISH:
                    insertWord(wordIndex + 1, new AnnotatedWord("{english=" + words[i] + "}"));
                    break;
                case TURKISH:
                    insertWord(wordIndex + 1, new AnnotatedWord("{turkish=" + words[i] + "}"));
                    break;
                case PERSIAN:
                    insertWord(wordIndex + 1, new AnnotatedWord("{persian=" + words[i] + "}"));
                    break;
            }
        }
        word.setName(words[0]);
    }

    /**
     * The method returns all possible words, which is
     * 1. Verb
     * 2. Its semantic tag is assigned
     * 3. A frameset exists for that semantic tag
     *
     * @param frameNet FrameNet list that contains all frames for Turkish
     * @return An array of words, which are verbs, semantic tags assigned, and framenet assigned for that tag.
     */
    public ArrayList<AnnotatedWord> predicateFrameCandidates(FrameNet frameNet) {
        ArrayList<AnnotatedWord> candidateList = new ArrayList<>();
        for (Word word : words) {
            AnnotatedWord annotatedWord = (AnnotatedWord) word;
            if (annotatedWord.getParse() != null && annotatedWord.getParse().isVerb() && annotatedWord.getSemantic() != null && frameNet.lexicalUnitExists(annotatedWord.getSemantic())) {
                candidateList.add(annotatedWord);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < words.size() - i - 1; j++) {
                AnnotatedWord annotatedWord = (AnnotatedWord) words.get(j);
                AnnotatedWord nextAnnotatedWord = (AnnotatedWord) words.get(j + 1);
                if (!candidateList.contains(annotatedWord) && candidateList.contains(nextAnnotatedWord) && annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals(nextAnnotatedWord.getSemantic())) {
                    candidateList.add(annotatedWord);
                }
            }
        }
        return candidateList;
    }

    /**
     * Returns the framesets of all predicate verbs in the sentence.
     *
     * @param framesetList Used to get the framesets for the predicates
     * @return Framesets of all predicate verbs in the sentence.
     */
    public HashSet<Frameset> getPredicateSynSets(FramesetList framesetList) {
        HashSet<Frameset> synSets = new HashSet<>();
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getArgumentList() != null && word.getSemantic() != null) {
                if (word.getArgumentList().containsPredicate() && framesetList.frameExists(word.getSemantic())) {
                    synSets.add(framesetList.getFrameSet(word.getSemantic()));
                }
            }
        }
        return synSets;
    }

    /**
     * Returns the frames of all predicate verbs in the sentence.
     *
     * @param frameNet Used to get the frames for the predicates.
     * @return Frames of all predicate verbs in the sentence.
     */
    public ArrayList<DisplayedFrame> getFrames(FrameNet frameNet) {
        ArrayList<DisplayedFrame> currentFrames = new ArrayList<>();
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getFrameElementList() != null && word.getSemantic() != null) {
                if (word.getFrameElementList().containsPredicate() && frameNet.lexicalUnitExists(word.getSemantic())) {
                    for (Frame frame : frameNet.getFrames(word.getSemantic())) {
                        if (!currentFrames.contains(new DisplayedFrame(frame, word.getSemantic()))) {
                            currentFrames.add(new DisplayedFrame(frame, word.getSemantic()));
                        }
                    }
                }
            }
        }
        return currentFrames;
    }

    /**
     * Returns the i'th predicate in the sentence.
     *
     * @param index Predicate index
     * @return The predicate with index in the sentence.
     */
    public String getPredicate(int index) {
        int count1 = 0, count2 = 0;
        String data = "";
        ArrayList<AnnotatedWord> word = new ArrayList<>();
        ArrayList<MorphologicalParse> parse = new ArrayList<>();
        if (index < wordCount()) {
            for (int i = 0; i < wordCount(); i++) {
                word.add((AnnotatedWord) getWord(i));
                parse.add(word.get(i).getParse());
            }
            for (int i = index; i >= 0; i--) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")) {
                    count1 = index - i;
                    break;
                }
            }
            for (int i = index; i < wordCount() - index; i++) {
                if (parse.get(i) != null && parse.get(i).getRootPos() != null && parse.get(i).getPos() != null && parse.get(i).getRootPos().equals("VERB") && parse.get(i).getPos().equals("VERB")) {
                    count2 = i - index;
                    break;
                }
            }
            if (count1 > count2) {
                data = word.get(count1).getName();
            } else {
                data = word.get(count2).getName();
            }
        }
        return data;
    }

    /**
     * Returns file name of the sentence
     *
     * @return File name of the sentence
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Returns file of the sentence
     *
     * @return File of the sentence
     */
    public File getFile() {
        return file;
    }

    /**
     * Removes the i'th word from the sentence
     *
     * @param index Word index
     */
    public void removeWord(int index) {
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
        StringBuilder result;
        AnnotatedWord annotatedWord;
        if (!words.isEmpty()) {
            annotatedWord = (AnnotatedWord) words.get(0);
            if (annotatedWord.getParse() != null) {
                result = new StringBuilder(annotatedWord.getParse().getWord().getName());
            } else {
                result = new StringBuilder(annotatedWord.getName());
            }
            for (int i = 1; i < words.size(); i++) {
                annotatedWord = (AnnotatedWord) words.get(i);
                if (annotatedWord.getParse() != null) {
                    result.append(" ").append(annotatedWord.getParse().getWord().getName());
                } else {
                    result.append(" ").append(annotatedWord.getName());
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    /**
     * Creates a html string for the annotated sentence, where the dependency relation of the word with the give
     * wordIndex is specified with color codes. The parameter word is drawn in red color, the dependent word is
     * drawn in blue color.
     *
     * @param wordIndex The word for which the dependency relation will be displayed.
     * @return Html string.
     */
    public String toDependencyString(int wordIndex) {
        StringBuilder sentenceString = new StringBuilder();
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        for (int k = 0; k < words.size(); k++) {
            if (wordIndex == k) {
                sentenceString.append(" <b><font color=\"red\">").append(words.get(k).getName()).append("</font></b>");
            } else {
                if (k + 1 == word.getUniversalDependency().to()) {
                    sentenceString.append(" <b><font color=\"blue\">").append(words.get(k).getName()).append("</font></b>");
                } else {
                    sentenceString.append(" ").append(words.get(k).getName());
                }
            }
        }
        return sentenceString.toString();
    }

    /**
     * Creates a shallow parse string for the annotated sentence, where the shallow parse of the word with the give
     * wordIndex and the surrounding words with the same shallow parse tag are specified with blue color.
     *
     * @param wordIndex The word for which the shallow parse tag will be displayed.
     * @return Html string.
     */
    public String toShallowParseString(int wordIndex) {
        StringBuilder sentenceString = new StringBuilder();
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        int startIndex = wordIndex - 1;
        while (startIndex >= 0 && ((AnnotatedWord) words.get(startIndex)).getShallowParse() != null && ((AnnotatedWord) words.get(startIndex)).getShallowParse().equals(word.getShallowParse())) {
            startIndex--;
        }
        startIndex++;
        int endIndex = wordIndex + 1;
        while (endIndex < words.size() && ((AnnotatedWord) words.get(endIndex)).getShallowParse() != null && ((AnnotatedWord) words.get(endIndex)).getShallowParse().equals(word.getShallowParse())) {
            endIndex++;
        }
        endIndex--;
        for (int k = 0; k < words.size(); k++) {
            if (k >= startIndex && k <= endIndex) {
                sentenceString.append(" <b><font color=\"blue\">").append(words.get(k).getName()).append("</font></b>");
            } else {
                sentenceString.append(" ").append(words.get(k).getName());
            }
        }
        return sentenceString.toString();
    }

    /**
     * Creates a html string for the annotated sentence, where the named entity of the word with the give
     * wordIndex and the surrounding words with the same named entity tag are specified with blue color.
     *
     * @param wordIndex The word for which the named entity tag will be displayed.
     * @return Html string.
     */
    public String toNamedEntityString(int wordIndex) {
        StringBuilder sentenceString = new StringBuilder();
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        int startIndex = wordIndex - 1;
        while (startIndex >= 0 && ((AnnotatedWord) words.get(startIndex)).getNamedEntityType() != null && ((AnnotatedWord) words.get(startIndex)).getNamedEntityType().equals(word.getNamedEntityType())) {
            startIndex--;
        }
        startIndex++;
        int endIndex = wordIndex + 1;
        while (endIndex < words.size() && ((AnnotatedWord) words.get(endIndex)).getNamedEntityType() != null && ((AnnotatedWord) words.get(endIndex)).getNamedEntityType().equals(word.getNamedEntityType())) {
            endIndex++;
        }
        endIndex--;
        for (int k = 0; k < words.size(); k++) {
            if (k >= startIndex && k <= endIndex) {
                sentenceString.append(" <b><font color=\"blue\">").append(words.get(k).getName()).append("</font></b>");
            } else {
                sentenceString.append(" ").append(words.get(k).getName());
            }
        }
        return sentenceString.toString();
    }

    /**
     * Compares the sentence with the given sentence and returns a parser evaluation score for this comparison. The result
     * is calculated by summing up the parser evaluation scores of word by word dpendency relation comparisons.
     *
     * @param sentence Sentence to be compared.
     * @return A parser evaluation score object.
     */
    public ParserEvaluationScore compareParses(AnnotatedSentence sentence) {
        ParserEvaluationScore score = new ParserEvaluationScore();
        for (int i = 0; i < wordCount(); i++) {
            UniversalDependencyRelation relation1 = ((AnnotatedWord) words.get(i)).getUniversalDependency();
            UniversalDependencyRelation relation2 = ((AnnotatedWord) sentence.getWord(i)).getUniversalDependency();
            if (relation1 != null && relation2 != null) {
                score.add(relation1.compareRelations(relation2));
            }
        }
        return score;
    }

    /**
     * Saves the current sentence.
     */
    public void save() {
        writeToFile(file);
    }

    /**
     * Saves the current sentence.
     *
     * @param fileName Name of the output file.
     */
    public void save(String fileName) {
        writeToFile(new File(fileName));
    }

    /**
     * Checks if there is an error with the dependency relation and the universal pos tag of the word.
     *
     * @param dependency   Dependency tag of the dependency relation.
     * @param universalPos Universal pos tag of the dependent word.
     * @return True if there is no error, false if there is an error.
     */
    public static boolean checkDependencyWithUniversalPosTag(String dependency, String universalPos) {
        if (dependency.equals("ADVMOD")) {
            if (!universalPos.equals("ADV") && !universalPos.equals("ADJ") && !universalPos.equals("CCONJ") &&
                    !universalPos.equals("DET") && !universalPos.equals("PART") && !universalPos.equals("SYM")) {
                return false;
            }
        }
        if (dependency.equals("AUX") && !universalPos.equals("AUX")) {
            return false;
        }
        if (dependency.equals("CASE")) {
            if (universalPos.equals("PROPN") || universalPos.equals("ADJ") || universalPos.equals("PRON") ||
                    universalPos.equals("DET") || universalPos.equals("NUM") || universalPos.equals("AUX")) {
                return false;
            }
        }
        if (dependency.equals("MARK") || dependency.equals("CC")) {
            if (universalPos.equals("NOUN") || universalPos.equals("PROPN") || universalPos.equals("ADJ") ||
                    universalPos.equals("PRON") || universalPos.equals("DET") || universalPos.equals("NUM") ||
                    universalPos.equals("VERB") || universalPos.equals("AUX") || universalPos.equals("INTJ")) {
                return false;
            }
        }
        if (dependency.equals("COP")) {
            if (!universalPos.equals("AUX") && !universalPos.equals("PRON") &&
                    !universalPos.equals("DET") && !universalPos.equals("SYM")) {
                return false;
            }
        }
        if (dependency.equals("DET")) {
            if (!universalPos.equals("DET") && !universalPos.equals("PRON")) {
                return false;
            }
        }
        if (dependency.equals("NUMMOD")) {
            if (!universalPos.equals("NUM") && !universalPos.equals("NOUN") && !universalPos.equals("SYM")) {
                return false;
            }
        }
        if (!dependency.equals("PUNCT") && universalPos.equals("PUNCT")) {
            return false;
        }
        return !dependency.equals("COMPOUND") || !universalPos.equals("AUX");
    }

    /**
     * Checks if there is a non-projectivity case between two words.
     *
     * @param from Index of the first word
     * @param to   Index of the second word
     * @return True if there are no outgoing arcs to out of the group specified with indexes (from, to), false
     * otherwise.
     */
    public boolean checkForNonProjectivityOfPunctuation(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null) {
                int currentTo = word.getUniversalDependency().to();
                int currentFrom = i + 1;
                if (currentFrom > min && currentFrom < max && (currentTo < min || currentTo > max)) {
                    return false;
                }
                if (currentTo > min && currentTo < max && (currentFrom < min || currentFrom > max)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if there are multiple root words in the sentence or not.
     *
     * @return True if there are multiple roots, false otherwise.
     */
    public boolean checkMultipleRoots() {
        int rootCount = 0;
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("ROOT")) {
                rootCount++;
            }
        }
        return rootCount <= 1;
    }

    /**
     * Checks if there are multiple subjects dependent to the root node.
     *
     * @return True if there are multiple subjects dependent to the root node. False otherwise.
     */
    public boolean checkMultipleSubjects() {
        int subjectCount = 0;
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("NSUBJ") && word.getUniversalDependency().to() - 1 >= 0 && word.getUniversalDependency().to() - 1 < wordCount()) {
                AnnotatedWord toWord = (AnnotatedWord) getWord(word.getUniversalDependency().to() - 1);
                if (toWord.getUniversalDependency() != null && toWord.getUniversalDependency().toString().equals("ROOT")) {
                    subjectCount++;
                }
            }
        }
        return subjectCount <= 1;
    }

    /**
     * Checks the annotated sentence for dependency errors, and returns them as a list.
     *
     * @return An arraylist of dependency annotation errors.
     */
    public ArrayList<DependencyError> getDependencyErrors() {
        ArrayList<DependencyError> errorList = new ArrayList<>();
        if (!checkMultipleRoots()) {
            errorList.add(new DependencyError(DependencyErrorType.MULTIPLE_ROOT,
                    0,
                    (AnnotatedWord) getWord(0),
                    "",
                    ""));
        }
        if (!checkMultipleSubjects()) {
            errorList.add(new DependencyError(DependencyErrorType.MULTIPLE_SUBJECTS,
                    0,
                    (AnnotatedWord) getWord(0),
                    "",
                    ""));
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependencyPos() == null) {
                errorList.add(new DependencyError(DependencyErrorType.NO_MORPHOLOGICAL_ANALYSIS,
                        i + 1,
                        (AnnotatedWord) getWord(i),
                        "",
                        ""));
            }
            if (word.getUniversalDependency() != null) {
                int to = word.getUniversalDependency().to();
                int from = i + 1;
                String dependency = word.getUniversalDependency().toString();
                if (from == to) {
                    errorList.add(new DependencyError(DependencyErrorType.HEAD_EQUALS_ID,
                            from,
                            (AnnotatedWord) getWord(from - 1),
                            "",
                            ""));
                }
                if (dependency.equals("PUNCT") && !checkForNonProjectivityOfPunctuation(from, to)) {
                    errorList.add(new DependencyError(DependencyErrorType.PUNCTUATION_NOT_PROJECTIVE,
                            from,
                            (AnnotatedWord) getWord(from - 1),
                            "",
                            ""));
                }
                if (to > from && (dependency.equals("CONJ") || dependency.equals("GOESWITH") ||
                        dependency.equals("FIXED") || dependency.equals("FLAT") || dependency.equals("APPOS"))) {
                    errorList.add(new DependencyError(DependencyErrorType.GO_LEFT_TO_RIGHT,
                            from,
                            (AnnotatedWord) getWord(from - 1),
                            dependency,
                            ""));
                }
                if (from > to && from > to + 1 && dependency.equals("GOESWITH")) {
                    errorList.add(new DependencyError(DependencyErrorType.GAPS_IN_GOESWITH,
                            from,
                            (AnnotatedWord) getWord(from - 1),
                            "",
                            ""));
                }
                if (word.getUniversalDependencyPos() != null) {
                    String universalPos = word.getUniversalDependencyPos();
                    if (!checkDependencyWithUniversalPosTag(dependency, universalPos)) {
                        errorList.add(new DependencyError(DependencyErrorType.SHOULDNT_BE_OF_POS,
                                from,
                                (AnnotatedWord) getWord(from - 1),
                                dependency,
                                universalPos));
                    }
                }
                if (to > 0 && to <= wordCount()) {
                    AnnotatedWord toWord = (AnnotatedWord) getWord(to - 1);
                    if (toWord.getUniversalDependency() != null) {
                        String toDependency = toWord.getUniversalDependency().toString();
                        if (toDependency.equals("AUX") || toDependency.equals("COP") || toDependency.equals("CC") ||
                                toDependency.equals("FIXED") || toDependency.equals("GOESWITH") || toDependency.equals("CASE") ||
                                toDependency.equals("MARK") || toDependency.equals("PUNCT")) {
                            errorList.add(new DependencyError(DependencyErrorType.NOT_EXPECTED_TO_HAVE_CHILDREN,
                                    from,
                                    (AnnotatedWord) getWord(from - 1),
                                    toDependency,
                                    ""));
                        }
                        if (dependency.equals("ORPHAN") && !toDependency.equals("CONJ")) {
                            errorList.add(new DependencyError(DependencyErrorType.PARENT_ORPHAN_SHOULD_BE_CONJ,
                                    from,
                                    (AnnotatedWord) getWord(from - 1),
                                    toDependency,
                                    ""));
                        }
                    }
                }
            } else {
                errorList.add(new DependencyError(DependencyErrorType.NO_DEPENDENCY,
                        i + 1,
                        (AnnotatedWord) getWord(i),
                        "",
                        ""));
            }
        }
        return errorList;
    }

    /**
     * Appends the Connlu format output of the current sentence to the given result string.
     *
     * @param result String after which new output string will be appended
     * @return Result string appended with Connlu format output of the current sentence.
     */
    public String getUniversalDependencyFormatForSentence(String result) {
        StringBuilder resultBuilder = new StringBuilder(result);
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            boolean goesWithHead = false;
            if (i < wordCount() - 1) {
                goesWithHead = ((AnnotatedWord) getWord(i + 1)).goesWithCase();
            }
            resultBuilder.append((i + 1)).append("\t").append(word.getUniversalDependencyFormat(wordCount(), goesWithHead)).append("\n");
        }
        result = resultBuilder.toString();
        result += "\n";
        return result;
    }

    /**
     * Constructs sequence string consisting of word annotations between start sentence and end sentence tags. Word
     * annotations are selected according to the layerType. If layertype is POLARITY, polarity of the word is appended
     * to its name. IF layerType is PART_OF_SPEECH, morphological parse of the word is appended to its name. If
     * layerType is NER, named entity tag of the word is appended to its name. If layerType is SEMANTICS, sense id
     * of the word (taken from WordNet) is appended to its name. If layerType is SOT, slot tag of the word is appended
     * to its name. If layerType is FRAMENET, frame element and possibly predicate of the word is appended to
     * its name. If layerType is PROPBANK, propbank argument and possibly predicate of the word is appended to its name.
     * If layerType is SHALLOW_PARSE, shallow parse tag of the word is appended to its name. If layerType is
     * META_MORPHEME, metamorpheme of the word is appended to its name. If layerType is POS_TAG, pos tag of the word is
     * appended to its name.
     *
     * @param layerType LayerType for the output.
     * @return A sequence string consisting of word annotations between start sentence and end sentence tags.
     */
    public String exportSequenceDataSet(ViewLayerType layerType) {
        StringBuilder result = new StringBuilder("<S>");
        if (layerType == ViewLayerType.POLARITY) {
            boolean positive = false, negative = false;
            for (int i = 0; i < wordCount(); i++) {
                AnnotatedWord word = (AnnotatedWord) getWord(i);
                if (word.getPolarity() != null) {
                    if (word.getPolarity().equals(PolarityType.POSITIVE)) {
                        positive = true;
                    } else {
                        if (word.getPolarity().equals(PolarityType.NEGATIVE)) {
                            negative = true;
                        }
                    }
                }
            }
            if (positive) {
                result.append(" POSITIVE\n");
            } else {
                if (negative) {
                    result.append(" NEGATIVE\n");
                } else {
                    result.append(" NEUTRAL\n");
                }
            }
        } else {
            result.append("\n");
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            result.append(word.getName()).append(" ");
            switch (layerType) {
                case INFLECTIONAL_GROUP:
                case PART_OF_SPEECH:
                    MorphologicalParse parse = word.getParse();
                    if (parse != null) {
                        result.append(parse);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case NER:
                    NamedEntityType namedEntityType = word.getNamedEntityType();
                    if (namedEntityType != null) {
                        result.append(namedEntityType);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case SEMANTICS:
                    String semantic = word.getSemantic();
                    if (semantic != null) {
                        result.append(semantic);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case SLOT:
                    Slot slot = word.getSlot();
                    if (slot != null) {
                        result.append(slot);
                    } else {
                        result.append("O");
                    }
                    break;
                case FRAMENET:
                    FrameElementList frameElement = word.getFrameElementList();
                    if (frameElement != null) {
                        result.append(frameElement);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case PROPBANK:
                    ArgumentList argument = word.getArgumentList();
                    if (argument != null) {
                        result.append(argument);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case SHALLOW_PARSE:
                    String shallowParse = word.getShallowParse();
                    if (shallowParse != null) {
                        result.append(shallowParse);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case META_MORPHEME:
                    MetamorphicParse metamorphicParse = word.getMetamorphicParse();
                    if (metamorphicParse != null) {
                        result.append(metamorphicParse);
                    } else {
                        result.append("NONE");
                    }
                    break;
                case POS_TAG:
                    String posTag = word.getUniversalDependencyPos();
                    if (posTag != null) {
                        result.append(posTag);
                    } else {
                        result.append("NONE");
                    }
                    break;
            }
            result.append("\n");
        }
        return result + "</S>\n";
    }

    /**
     * Returns the connlu format of the sentence with appended prefix string based on the path.
     *
     * @param path Path of the sentence.
     * @return The connlu format of the sentence with appended prefix string based on the path.
     */
    public String getUniversalDependencyFormat(String path) {
        String result = "# sent_id = " + path + getFileName() + "\n" + "# text = " + toWords() + "\n";
        return getUniversalDependencyFormatForSentence(result);
    }

    /**
     * Returns the connlu format of the sentence with appended prefix string.
     *
     * @return The connlu format of the sentence with appended prefix string.
     */
    public String getUniversalDependencyFormat() {
        String result = "# sent_id = " + getFileName() + "\n" + "# text = " + toWords() + "\n";
        return getUniversalDependencyFormatForSentence(result);
    }

    /**
     * Creates a list of literal candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible root forms of the i'th word in the sentence
     * 2. All possible 2-word expressions containing the i'th word in the sentence
     * 3. All possible 3-word expressions containing the i'th word in the sentence
     *
     * @param wordNet   Turkish wordnet
     * @param fsm       Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of literal candidates containing all possible root forms and multiword expressions.
     */
    public ArrayList<Literal> constructLiterals(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex) {
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        MorphologicalParse morphologicalParse = word.getParse();
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        ArrayList<Literal> possibleLiterals = new ArrayList<>(wordNet.constructLiterals(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
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
        possibleLiterals.sort(new LiteralWithSenseComparator());
        return possibleLiterals;
    }

    private String withTabs(int tabCount, String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tabCount; i++) {
            result.append("\t");
        }
        return result + string;
    }

    private String onlyWord(AnnotatedWord word, int i) {
        return (i + 1) + "/" + word.getParse().getWord().getName();
    }

    private boolean containsArg0(String semantic){
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getArgumentList() != null && word.getArgumentList().containsArgument("ARG0", semantic)) {
                return true;
            }
        }
        return false;
    }

    private void extraArgs(ArrayList<String> output, AnnotatedWord word, int tabCount) {
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A1SG) && !toStems().contains("ben ")) {
            output.add(withTabs(tabCount + 1, "ben:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P1SG)) {
            output.add(withTabs(tabCount + 1, "ben:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A1PL) && !toStems().contains("biz ")) {
            output.add(withTabs(tabCount + 1, "biz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P1PL)) {
            output.add(withTabs(tabCount + 1, "biz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A2SG) && !toStems().contains("sen ")) {
            output.add(withTabs(tabCount + 1, "sen:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P2SG)) {
            output.add(withTabs(tabCount + 1, "sen:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A2PL) && !toStems().contains("siz ")) {
            output.add(withTabs(tabCount + 1, "siz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().getPos().equals("NOUN") && word.getParse().containsTag(MorphologicalTag.P2PL)) {
            output.add(withTabs(tabCount + 1, "siz:ARG0"));
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A3SG) && !toStems().contains("o ")) {
            if (!containsArg0(word.getSemantic())){
                if (!(word.getParse().getPos().equals("NOUN") && (word.getParse().containsTag(MorphologicalTag.P1SG) || word.getParse().containsTag(MorphologicalTag.P1PL) || word.getParse().containsTag(MorphologicalTag.P2SG) || word.getParse().containsTag(MorphologicalTag.P2PL)))) {
                    output.add(withTabs(tabCount + 1, "o:ARG0"));
                }
            }
        }
        if (word.getParse().getRootPos().equals("VERB") && word.getParse().containsTag(MorphologicalTag.A3PL) && !toStems().contains("onlar ")) {
            if (!containsArg0(word.getSemantic())){
                if (!(word.getParse().getPos().equals("NOUN") && (word.getParse().containsTag(MorphologicalTag.P1SG) || word.getParse().containsTag(MorphologicalTag.P1PL) || word.getParse().containsTag(MorphologicalTag.P2SG) || word.getParse().containsTag(MorphologicalTag.P2PL)))) {
                    output.add(withTabs(tabCount + 1, "onlar:ARG0"));
                }
            }
        }
    }

    private boolean containsMod(int index){
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                if (word.getUniversalDependency().toString().equals("AMOD") || word.getUniversalDependency().toString().equals("NMOD")){
                    return true;
                }
            }
        }
        return false;
    }

    private void extraPossessive(ArrayList<String> output, AnnotatedWord word, int wordIndex, int tabCount) {
        if (word.getParse().containsTag(MorphologicalTag.P1SG)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")){
                output.add(withTabs(tabCount + 1, "ben:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P1PL)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")){
                output.add(withTabs(tabCount + 1, "biz:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P2SG)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")) {
                output.add(withTabs(tabCount + 1, "sen:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P2PL)) {
            if (!word.getParse().getRootPos().equals("VERB") || !word.getParse().getPos().equals("NOUN")) {
                output.add(withTabs(tabCount + 1, "siz:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P3SG)) {
            if (!containsMod(wordIndex)){
                output.add(withTabs(tabCount + 1, "o:poss"));
            }
        }
        if (word.getParse().containsTag(MorphologicalTag.P3PL)) {
            if (!containsMod(wordIndex)){
                output.add(withTabs(tabCount + 1, "onlar:poss"));
            }
        }
    }

    private boolean isMonth(String next) {
        return new ArrayList<>(Arrays.asList("ocak", "şubat", "mart", "nisan", "mayıs", "haziran", "temmuz", "ağustos",
                "eylül", "ekim", "kasım", "aralık")).contains(next);
    }

    private boolean isWeekDay(String next) {
        return new ArrayList<>(Arrays.asList("pazartesi", "salı", "çarşamba", "perşembe", "cuma", "cumartesi", "pazar")).contains(next);
    }

    private int isOrdinal(String next) {
        switch (next) {
            case "birinci":
                return 1;
            case "ikinci":
                return 2;
            case "üçüncü":
                return 3;
            case "dördüncü":
                return 4;
            case "beşinci":
                return 5;
            case "altıncı":
                return 6;
            case "yedinci":
                return 7;
            case "sekizinci":
                return 8;
            case "dokuzuncu":
                return 9;
        }
        return 0;
    }

    private boolean addArgumentList(ArrayList<String> output, AnnotatedWord current, String semantic, String currentText){
        if (current.getArgumentList() != null) {
            ArgumentList argumentList = current.getArgumentList();
            if (argumentList.containsArgument("ARG0", semantic)) {
                output.add(currentText + ":ARG0");
                return true;
            } else {
                if (argumentList.containsArgument("ARG1", semantic)) {
                    output.add(currentText + ":ARG1");
                    return true;
                } else {
                    if (argumentList.containsArgument("ARG2", semantic)) {
                        output.add(currentText + ":ARG2");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addDetails(int tabCount, ArrayList<String> output, AnnotatedWord current, int wordIndex) {
        if (current.getParse().containsTag(MorphologicalTag.NEGATIVE)) {
            output.add(withTabs(tabCount + 1, "-:polarity"));
        }
        extraArgs(output, current, tabCount);
        extraPossessive(output, current, wordIndex, tabCount);
        if (current.getParse().containsTag(MorphologicalTag.IMPERATIVE)) {
            output.add(withTabs(tabCount + 1, "imperative:mode"));
        }
    }

    private void printAmrRecursively(boolean[] done, int index, int tabCount, ArrayList<String> output, String relation, String semantic, WordNet wordNet, String extraAdded) {
        int currentWordIndex = index;
        if (done[index]){
            return;
        }
        done[index] = true;
        AnnotatedWord current = (AnnotatedWord) getWord(index);
        if (relation.equals("DET") && current.getParse().getWord().getName().equals("bir")){
            return;
        }
        if (new ArrayList<>(Arrays.asList("ve", "veya", "hem", "ama")).contains(current.getParse().getWord().getName())){
            return;
        }
        if (current.getParse().getWord().getName().equals("değil")) {
            output.add(withTabs(tabCount, "-:polarity"));
            return;
        }
        if (current.isPunctuation()){
            return;
        }
        String added = "";
        int addedIndex = -1;
        if (current.getParse().containsTag(MorphologicalTag.CONDITIONAL)){
            added = ":cond";
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                switch (word.getParse().getWord().getName()){
                    case "kadar":
                        added = ":extent";
                        addedIndex = i;
                        break;
                    case "rağmen":
                    case "karşın":
                    case "karşılık":
                        added = ":concession";
                        addedIndex = i;
                        break;
                    case "için":
                    case "sayesinde":
                    case "dolayı":
                        added = ":cause";
                        addedIndex = i;
                        break;
                }
            }
        }
        if (current.getParse().isCardinal() && index + 1 < wordCount()) {
            String next = ((AnnotatedWord) getWord(index + 1)).getParse().getWord().getName();
            if (isMonth(next)) {
                output.add(withTabs(tabCount, "date-entity:date"));
                output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":day"));
                output.add(withTabs(tabCount + 1, onlyWord(((AnnotatedWord) getWord(index + 1)), index + 1) + ":month"));
                done[index + 1] = true;
            } else {
                if (!extraAdded.isEmpty()){
                    output.add(withTabs(tabCount, onlyWord(current, index) + extraAdded));
                } else {
                    output.add(withTabs(tabCount, onlyWord(current, index) + added));
                    if (addedIndex != -1){
                        done[addedIndex] = true;
                    }
                }
            }
        } else {
            if (current.getParse().isProperNoun()) {
                String wikiType = "person";
                ArrayList<SynSet> synSets = wordNet.getSynSetsWithLiteral(current.getParse().getWord().getName());
                for (SynSet synSet : synSets) {
                    if (synSet.containsRelation(new SemanticRelation("TUR10-0820020", "INSTANCE_HYPERNYM"))){
                        wikiType = "city";
                    }
                }
                boolean argumentAdded = addArgumentList(output, current, semantic, withTabs(tabCount, wikiType));
                if (!argumentAdded) {
                    if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.INSTRUMENTAL)) {
                        output.add(withTabs(tabCount, wikiType) + ":instrument");
                    } else {
                        if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.LOCATIVE)) {
                            output.add(withTabs(tabCount, wikiType) + ":location");
                        } else {
                            if (!extraAdded.isEmpty()){
                                output.add(withTabs(tabCount, wikiType) + extraAdded);
                            } else {
                                output.add(withTabs(tabCount, wikiType) + added);
                                if (addedIndex != -1){
                                    done[addedIndex] = true;
                                }
                            }
                        }
                    }
                }
                output.add(withTabs(tabCount + 1, "name:name"));
                output.add(withTabs(tabCount + 2, onlyWord(current, index) + ":op1"));
                for (int i = 1; i <= 3; i++){
                    if (index + i < wordCount() && ((AnnotatedWord) getWord(index + i)).getSemantic() != null && ((AnnotatedWord) getWord(index + i)).getSemantic().equals(current.getSemantic())) {
                        output.add(withTabs(tabCount + 2, onlyWord((AnnotatedWord) getWord(index + i), index + 1) + ":op" + (1 + i)));
                        done[index + i] = true;
                    } else {
                        break;
                    }
                }
                if (wikiType.equals("person")) {
                    output.add(withTabs(tabCount + 1, "-:wiki"));
                } else {
                    output.add(withTabs(tabCount + 1, current.getParse().getWord().getName() + ":wiki"));
                }
            } else {
                if (isMonth(current.getParse().getWord().getName())) {
                    output.add(withTabs(tabCount, "date-entity:date"));
                    output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":month"));
                } else {
                    if (isWeekDay(current.getParse().getWord().getName())) {
                        output.add(withTabs(tabCount, "date-entity:date"));
                        output.add(withTabs(tabCount + 1, onlyWord(current, index) + ":weekday"));
                    } else {
                        String currentWord = onlyWord(current, index);
                        for (int i = 1; i <= 3; i++) {
                            if (index > i - 1 && !done[index - i] && index - i < wordCount() && ((AnnotatedWord) getWord(index - i)).getSemantic() != null && ((AnnotatedWord) getWord(index - i)).getSemantic().equals(current.getSemantic())) {
                                currentWord = onlyWord((AnnotatedWord) getWord(index - i), index - i) + " " + currentWord;
                                done[index - i] = true;
                            } else {
                                break;
                            }
                        }
                        for (int i = 1; i <= 3; i++){
                            if (index + i < wordCount() && ((AnnotatedWord) getWord(index + i)).getSemantic() != null && ((AnnotatedWord) getWord(index + i)).getSemantic().equals(current.getSemantic())) {
                                currentWord += " " + onlyWord((AnnotatedWord) getWord(index + i), index + i);
                                done[index + i] = true;
                                current = (AnnotatedWord) getWord(index + i);
                                currentWordIndex = index + i;
                            } else {
                                break;
                            }
                        }
                        if (new ArrayList<>(Arrays.asList("çok", "gayet", "tam", "bayağı", "fazla", "hiç")).contains(current.getParse().getWord().getName())) {
                            output.add(withTabs(tabCount, currentWord) + ":degree");
                        } else {
                            if (current.getParse().getWord().getName().equals("hep") || current.getParse().getWord().getName().equals("sürekli")){
                                output.add(withTabs(tabCount, currentWord) + ":frequency");
                            } else {
                                boolean argumentAdded = addArgumentList(output, current, semantic, withTabs(tabCount, currentWord));
                                if (argumentAdded) {
                                    addDetails(tabCount, output, current, currentWordIndex);
                                } else {
                                    if (current.getParse().containsTag(MorphologicalTag.ORDINAL) || isOrdinal(current.getParse().getWord().getName()) > 0) {
                                        output.add(withTabs(tabCount, "ordinal-entity:ord"));
                                        int value = isOrdinal(current.getParse().getWord().getName());
                                        if (value > 0) {
                                            output.add(withTabs(tabCount + 1, value + ":value"));
                                        } else {
                                            output.add(withTabs(tabCount + 1, current.getParse().getWord().getName() + ":value"));
                                        }
                                    } else {
                                        if (new ArrayList<>(Arrays.asList("AMOD", "NMOD")).contains(relation)) {
                                            output.add(withTabs(tabCount, currentWord) + ":mod");
                                        } else {
                                            if (relation.equals("NUMMOD")) {
                                                output.add(withTabs(tabCount, currentWord) + ":quant");
                                            } else {
                                                if (relation.equals("ADVMOD")) {
                                                    output.add(withTabs(tabCount, currentWord) + ":manner");
                                                } else {
                                                    if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.INSTRUMENTAL)) {
                                                        output.add(withTabs(tabCount, currentWord) + ":instrument");
                                                    } else {
                                                        if (!current.getParse().getRootPos().equals("VERB") && current.getParse().containsTag(MorphologicalTag.LOCATIVE)) {
                                                            output.add(withTabs(tabCount, currentWord) + ":location");
                                                        } else {
                                                            if (!extraAdded.isEmpty()){
                                                                output.add(withTabs(tabCount, currentWord) + extraAdded);
                                                            } else {
                                                                output.add(withTabs(tabCount, currentWord) + added);
                                                                if (addedIndex != -1){
                                                                    done[addedIndex] = true;
                                                                }
                                                            }
                                                            addDetails(tabCount, output, current, currentWordIndex);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getParse().isCardinal() && i + 1 < wordCount()) {
                String next = ((AnnotatedWord) getWord(i + 1)).getParse().getWord().getName();
                if (isMonth(next)) {
                    if (((AnnotatedWord) getWord(i + 1)).getUniversalDependency().to() == index + 1) {
                        metaVerbTags(word, done, i, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
                    }
                    i++;
                    continue;
                }
            }
            int j = i;
            while (i < wordCount() - 1 && ((AnnotatedWord) getWord(i + 1)).getSemantic() != null && ((AnnotatedWord) getWord(i + 1)).getSemantic().equals(word.getSemantic())) {
                i++;
            }
            if (word.getUniversalDependency() != null && word.getUniversalDependency().to() == index + 1) {
                metaVerbTags(word, done, j, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
            } else {
                if (j != i && ((AnnotatedWord) getWord(i)).getUniversalDependency() != null && ((AnnotatedWord) getWord(i)).getUniversalDependency().to() == index + 1) {
                    metaVerbTags(word, done, j, tabCount + 1, output, word.getUniversalDependency().toString(), current.getSemantic(), wordNet);
                }
            }
        }
    }

    private void metaVerbTags(AnnotatedWord word, boolean[] done, int index, int tabCount, ArrayList<String> output, String relation, String semantic, WordNet wordNet) {
        boolean parataxisOrConj = false;
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord connectedWord = (AnnotatedWord) getWord(i);
            if (connectedWord.getUniversalDependency() != null && (connectedWord.getUniversalDependency().toString().equals("PARATAXIS") || connectedWord.getUniversalDependency().toString().equals("CONJ")) && connectedWord.getUniversalDependency().to() == index + 1) {
                parataxisOrConj = true;
                break;
            }
        }
        if (parataxisOrConj) {
            output.add(withTabs(tabCount, "and"));
            int count = 1;
            for (int i = 0; i < wordCount(); i++) {
                AnnotatedWord connectedWord = (AnnotatedWord) getWord(i);
                if (connectedWord.getUniversalDependency() != null && (connectedWord.getUniversalDependency().toString().equals("PARATAXIS") || connectedWord.getUniversalDependency().toString().equals("CONJ")) && connectedWord.getUniversalDependency().to() == index + 1) {
                    printAmrRecursively(done, i, tabCount + 1, output, relation, semantic, wordNet, ":op" + count);
                    count++;
                }
            }
            printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, ":op" + count);
        } else {
            if (word.getParse().containsTag(MorphologicalTag.ABLE)) {
                output.add(withTabs(tabCount, "mümkün"));
                printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
            } else {
                if (word.getParse().containsTag(MorphologicalTag.CAUSATIVE)){
                    output.add(withTabs(tabCount, "yap"));
                    printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
                } else {
                    if (word.getParse().containsTag(MorphologicalTag.NECESSITY)){
                        output.add(withTabs(tabCount, "öner"));
                        printAmrRecursively(done, index, tabCount + 1, output, relation, semantic, wordNet, "");
                    } else {
                        printAmrRecursively(done, index, tabCount, output, relation, semantic, wordNet, "");
                    }
                }
            }
        }
    }

    public ArrayList<String> constructAmr(WordNet wordNet) {
        ArrayList<String> output = new ArrayList<>();
        boolean[] done = new boolean[wordCount()];
        output.add(getFileName() + "\t" + toWords());
        for (int i = 0; i < wordCount(); i++) {
            AnnotatedWord word = (AnnotatedWord) getWord(i);
            if (word.getUniversalDependency() != null && word.getUniversalDependency().toString().equals("ROOT")) {
                metaVerbTags(word, done, i, 0, output, "ROOT", word.getSemantic(), wordNet);
            }
        }
        return output;
    }

    /**
     * Creates a list of synset candidates for the i'th word in the sentence. It combines the results of
     * 1. All possible synsets containing the i'th word in the sentence
     * 2. All possible synsets containing 2-word expressions, which contains the i'th word in the sentence
     * 3. All possible synsets containing 3-word expressions, which contains the i'th word in the sentence
     *
     * @param wordNet   Turkish wordnet
     * @param fsm       Turkish morphological analyzer
     * @param wordIndex Word index
     * @return List of synset candidates containing all possible root forms and multiword expressions.
     * @throws ParseRequiredException When parse does not exist
     */
    public ArrayList<SynSet> constructSynSets(WordNet wordNet, FsmMorphologicalAnalyzer fsm, int wordIndex) throws ParseRequiredException {
        AnnotatedWord word = (AnnotatedWord) getWord(wordIndex);
        MorphologicalParse morphologicalParse = word.getParse();
        if (morphologicalParse == null) {
            throw new ParseRequiredException(word.getName());
        }
        MetamorphicParse metamorphicParse = word.getMetamorphicParse();
        ArrayList<SynSet> possibleSynSets = new ArrayList<>(wordNet.constructSynSets(morphologicalParse.getWord().getName(), morphologicalParse, metamorphicParse, fsm));
        AnnotatedWord firstPrecedingWord = null;
        AnnotatedWord secondPrecedingWord = null;
        AnnotatedWord firstSucceedingWord = null;
        AnnotatedWord secondSucceedingWord = null;
        if (wordIndex > 0) {
            firstPrecedingWord = (AnnotatedWord) getWord(wordIndex - 1);
            if (firstPrecedingWord.getParse() == null) {
                throw new ParseRequiredException(word.getName());
            }
            if (wordIndex > 1) {
                secondPrecedingWord = (AnnotatedWord) getWord(wordIndex - 2);
                if (secondPrecedingWord.getParse() == null) {
                    throw new ParseRequiredException(word.getName());
                }
            }
        }
        if (wordCount() > wordIndex + 1) {
            firstSucceedingWord = (AnnotatedWord) getWord(wordIndex + 1);
            if (firstSucceedingWord.getParse() == null) {
                throw new ParseRequiredException(word.getName());
            }
            if (wordCount() > wordIndex + 2) {
                secondSucceedingWord = (AnnotatedWord) getWord(wordIndex + 2);
                if (secondSucceedingWord.getParse() == null) {
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
        possibleSynSets.sort(new SynSetComparator());
        return possibleSynSets;
    }


}
