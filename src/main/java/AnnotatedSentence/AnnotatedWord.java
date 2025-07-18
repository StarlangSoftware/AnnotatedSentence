package AnnotatedSentence;

import AnnotatedSentence.GrammaticaError.GrammaticalError;
import Corpus.WordFormat;
import DependencyParser.Universal.UniversalDependencyRelation;
import Dictionary.Word;
import FrameNet.FrameElementList;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.MetamorphicParse;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MorphologicalTag;
import NamedEntityRecognition.Gazetteer;
import NamedEntityRecognition.NamedEntityType;
import NamedEntityRecognition.Slot;
import PropBank.ArgumentList;
import SentiNet.PolarityType;
import Util.RectAngle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class AnnotatedWord extends Word implements Serializable{
    /**
     * In order to add another layer, do the following:
     * 1. Select a name for the layer.
     * 2. Add a new constant to ViewLayerType.
     * 3. Add private attribute.
     * 4. Add an if-else to the constructor, where you set the private attribute with the layer name.
     * 5. Update toString method.
     * 6. Add initial value to the private attribute in other constructors.
     * 7. Update getLayerInfo.
     * 8. Add getter and setter methods.
     */
    private MorphologicalParse parse;
    private MetamorphicParse metamorphicParse;
    private String semantic;
    private NamedEntityType namedEntityType;
    private ArgumentList argumentList;
    private FrameElementList frameElementList;
    private UniversalDependencyRelation universalDependency;
    private String shallowParse;
    private PolarityType polarity;
    private Slot slot;
    private String ccg;
    private GrammaticalError grammaticalError;
    private String posTag;
    private RectAngle area;
    private Language language = Language.TURKISH;
    private boolean selected = false;
    private boolean forceSelected = false;

    /**
     * Constructor for the {@link AnnotatedWord} class. Gets the word with its annotation layers as input and sets the
     * corresponding layers.
     * @param word Input word with annotation layers
     */
    public AnnotatedWord(String word){
        String[] splitLayers = word.split("[{}]");
        for (String layer:splitLayers){
            if (layer.isEmpty())
                continue;
            if (!layer.contains("=")){
                name = layer;
                continue;
            }
            String layerType = layer.substring(0, layer.indexOf("="));
            String layerValue = layer.substring(layer.indexOf("=") + 1);
            if (layerType.equalsIgnoreCase("turkish") || layerType.equalsIgnoreCase("english")
                    || layerType.equalsIgnoreCase("persian")){
                name = layerValue;
                language = getLanguageFromString(layerType);
            } else {
                if (layerType.equalsIgnoreCase("morphologicalAnalysis")){
                    parse = new MorphologicalParse(layerValue);
                } else {
                    if (layerType.equalsIgnoreCase("metaMorphemes")){
                        metamorphicParse = new MetamorphicParse(layerValue);
                    } else {
                        if (layerType.equalsIgnoreCase("semantics")){
                            semantic = layerValue;
                        } else {
                            if (layerType.equalsIgnoreCase("namedEntity")){
                                namedEntityType = NamedEntityType.getNamedEntityType(layerValue);
                            } else {
                                if (layerType.equalsIgnoreCase("propbank")){
                                    argumentList = new ArgumentList(layerValue);
                                } else {
                                    if (layerType.equalsIgnoreCase("shallowParse")){
                                        shallowParse = layerValue;
                                    } else {
                                        if (layerType.equalsIgnoreCase("universalDependency")){
                                            String[] values = layerValue.split("\\$");
                                            universalDependency = new UniversalDependencyRelation(Integer.parseInt(values[0]), values[1]);
                                        } else {
                                            if (layerType.equalsIgnoreCase("framenet")){
                                                frameElementList = new FrameElementList(layerValue);
                                            } else {
                                                if (layerType.equalsIgnoreCase("slot")){
                                                    slot = new Slot(layerValue);
                                                } else {
                                                    if (layerType.equalsIgnoreCase("polarity")){
                                                        setPolarity(layerValue);
                                                    } else {
                                                        if (layerType.equalsIgnoreCase("ccg")) {
                                                            ccg = layerValue;
                                                        } else {
                                                            if (layerType.equalsIgnoreCase("posTag")){
                                                                posTag = layerValue;
                                                            }  else {
                                                                if (layerType.equalsIgnoreCase("grammaticalError")){
                                                                    grammaticalError = new GrammaticalError(layerValue);
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
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Converts an {@link AnnotatedWord} to string. For each annotation layer, the method puts a left brace, layer name,
     * equal sign and layer value finishing with right brace.
     * @return String form of the {@link AnnotatedWord}.
     */
    public String toString(){
        String result;
        switch (language){
            case TURKISH:
            default:
                result = "{turkish=" + name + "}";
                break;
            case ENGLISH:
                result = "{english=" + name + "}";
                break;
            case PERSIAN:
                result = "{persian=" + name + "}";
                break;
        }
        if (parse != null){
            result = result + "{morphologicalAnalysis=" + parse + "}";
        }
        if (metamorphicParse != null){
            result = result + "{metaMorphemes=" + metamorphicParse + "}";
        }
        if (semantic != null){
            result = result + "{semantics=" + semantic + "}";
        }
        if (namedEntityType != null){
            result = result + "{namedEntity=" + namedEntityType + "}";
        }
        if (argumentList != null){
            result = result + "{propbank=" + argumentList + "}";
        }
        if (frameElementList != null){
            result = result + "{framenet=" + frameElementList + "}";
        }
        if (shallowParse != null){
            result = result + "{shallowParse=" + shallowParse + "}";
        }
        if (universalDependency != null){
            result = result + "{universalDependency=" + universalDependency.to() + "$" + universalDependency.toString() + "}";
        }
        if (slot != null){
            result = result + "{slot=" + slot + "}";
        }
        if (polarity != null){
            result = result + "{polarity=" + getPolarityString() + "}";
        }
        if (ccg != null) {
            result = result + "{ccg=" + ccg + "}";
        }
        if (posTag != null) {
            result = result + "{posTag=" + posTag + "}";
        }
        if (grammaticalError != null){
            result = result + "{grammaticalError=" + grammaticalError + "}";
        }
        return result;
    }

    /**
     * Another constructor for {@link AnnotatedWord}. Gets the word and a namedEntityType and sets two layers.
     * @param name Lemma of the word.
     * @param namedEntityType Named entity of the word.
     */
    public AnnotatedWord(String name, NamedEntityType namedEntityType){
        super(name);
        this.namedEntityType = namedEntityType;
        parse = null;
        metamorphicParse = null;
        semantic = null;
        argumentList = null;
        shallowParse = null;
        universalDependency = null;
        frameElementList = null;
        slot = null;
        polarity = null;
        ccg = null;
        posTag = null;
        grammaticalError = null;
    }

    /**
     * Another constructor for {@link AnnotatedWord}. Gets the word and morphological parse and sets two layers.
     * @param name Lemma of the word.
     * @param parse Morphological parse of the word.
     */
    public AnnotatedWord(String name, MorphologicalParse parse){
        super(name);
        this.parse = parse;
        this.namedEntityType = NamedEntityType.NONE;
        argumentList = null;
        metamorphicParse = null;
        semantic = null;
        shallowParse = null;
        universalDependency = null;
        frameElementList = null;
        slot = null;
        polarity = null;
        ccg = null;
        posTag = null;
        grammaticalError = null;
    }

    /**
     * Another constructor for {@link AnnotatedWord}. Gets the word and morphological parse and sets two layers.
     * @param name Lemma of the word.
     * @param parse Morphological parse of the word.
     */
    public AnnotatedWord(String name, FsmParse parse){
        super(name);
        this.parse = parse;
        this.namedEntityType = NamedEntityType.NONE;
        argumentList = null;
        setMetamorphicParse(parse.withList());
        semantic = null;
        shallowParse = null;
        universalDependency = null;
        frameElementList = null;
        slot = null;
        polarity = null;
        ccg = null;
        posTag = null;
        grammaticalError = null;
    }

    /**
     * Returns the value of a given layer.
     * @param viewLayerType Layer for which the value questioned.
     * @return The value of the given layer.
     */
    public String getLayerInfo(ViewLayerType viewLayerType){
        switch (viewLayerType){
            case INFLECTIONAL_GROUP:
                if (parse != null){
                    return parse.toString();
                }
                break;
            case META_MORPHEME:
                if (metamorphicParse != null){
                    return metamorphicParse.toString();
                }
                break;
            case SEMANTICS:
                return semantic;
            case NER:
                if (namedEntityType != null){
                    return namedEntityType.toString();
                }
                break;
            case SHALLOW_PARSE:
                return shallowParse;
            case TURKISH_WORD:
                return name;
            case PROPBANK:
                if (argumentList != null){
                    return argumentList.toString();
                }
                break;
            case DEPENDENCY:
                if (universalDependency != null){
                    return universalDependency.to() + "$" + universalDependency.toString();
                }
                break;
            case DEPENDENCY_TO:
                if (universalDependency != null){
                    return "" + universalDependency.to();
                }
                break;
            case DEPENDENCY_TYPE:
                if (universalDependency != null){
                    return universalDependency.toString();
                }
                break;
            case FRAMENET:
                if (frameElementList != null){
                    return frameElementList.toString();
                }
                break;
            case SLOT:
                if (slot != null){
                    return slot.toString();
                }
                break;
            case POLARITY:
                if (polarity != null){
                    return getPolarityString();
                }
                break;
            case CCG:
                if (ccg != null) {
                    return ccg;
                }
                break;
            case POS_TAG:
                if (posTag != null) {
                    return posTag;
                }
                break;
            case GRAMMATICAL_ERROR:
                if (grammaticalError != null){
                    return grammaticalError.toString();
                }
                break;
        }
        return null;
    }

    /**
     * Returns the morphological parse layer of the word.
     * @return The morphological parse of the word.
     */
    public MorphologicalParse getParse() {
        return parse;
    }

    /**
     * Sets the morphological parse layer of the word.
     * @param parseString The new morphological parse of the word in string form.
     */
    public void setParse(String parseString){
        if (parseString != null){
            parse = new MorphologicalParse(parseString);
        } else {
            parse = null;
        }
    }

    /**
     * Returns the metamorphic parse layer of the word.
     * @return The metamorphic parse of the word.
     */
    public MetamorphicParse getMetamorphicParse() {
        return metamorphicParse;
    }

    /**
     * Sets the metamorphic parse layer of the word.
     * @param parseString The new metamorphic parse of the word in string form.
     */
    public void setMetamorphicParse(String parseString){
        metamorphicParse = new MetamorphicParse(parseString);
    }

    /**
     * Returns the semantic layer of the word.
     * @return Sense id of the word.
     */
    public String getSemantic() {
        return semantic;
    }

    /**
     * Sets the semantic layer of the word.
     * @param semantic New sense id of the word.
     */
    public void setSemantic(String semantic){
        this.semantic = semantic;
    }

    /**
     * Returns the named entity layer of the word.
     * @return Named entity tag of the word.
     */
    public NamedEntityType getNamedEntityType() {
        return namedEntityType;
    }

    /**
     * Sets the named entity layer of the word.
     * @param namedEntity New named entity tag of the word.
     */
    public void setNamedEntityType(String namedEntity){
        if (namedEntity != null){
            namedEntityType = NamedEntityType.getNamedEntityType(namedEntity);
        } else {
            namedEntityType = null;
        }
    }

    /**
     * Returns the semantic role layer of the word.
     * @return Semantic role tag of the word.
     */
    public ArgumentList getArgumentList() {
        return argumentList;
    }

    /**
     * Sets the semantic role layer of the word.
     * @param argument New semantic role tag of the word.
     */
    public void setArgumentList(String argument){
        if (argument != null){
            this.argumentList = new ArgumentList(argument);
        } else {
            this.argumentList = null;
        }
    }

    /**
     * Returns the frameNet layer of the word.
     * @return FrameNet tag of the word.
     */
    public FrameElementList getFrameElementList(){
        return frameElementList;
    }

    /**
     * Sets the framenet layer of the word.
     * @param frameElement New frame element tag of the word.
     */
    public void setFrameElementList(String frameElement){
        if (frameElement != null){
            this.frameElementList = new FrameElementList(frameElement);
        } else {
            this.frameElementList = null;
        }
    }

    /**
     * Returns the slot filling layer of the word.
     * @return Slot tag of the word.
     */
    public Slot getSlot(){
        return slot;
    }

    /**
     * Sets the slot filling layer of the word.
     * @param slot New slot tag of the word.
     */
    public void setSlot(String slot){
        if (slot != null){
            this.slot = new Slot(slot);
        } else {
            this.slot = null;
        }
    }

    /**
     * Returns the polarity layer of the word.
     * @return Polarity tag of the word.
     */
    public PolarityType getPolarity(){
        return polarity;
    }

    /**
     * Returns the polarity layer of the word.
     * @return Polarity string of the word.
     */
    public String getPolarityString(){
        switch (polarity){
            case POSITIVE:
                return "positive";
            case NEGATIVE:
                return "negative";
            default:
                return "neutral";
        }
    }

    /**
     * Sets the polarity layer of the word.
     * @param polarity New polarity tag of the word.
     */
    public void setPolarity(String polarity){
        if (polarity != null){
            switch (polarity.toLowerCase()){
                case "positive":
                case "pos":
                    this.polarity = PolarityType.POSITIVE;
                    break;
                case "negative":
                case "neg":
                    this.polarity = PolarityType.NEGATIVE;
                    break;
                default:
                    this.polarity = PolarityType.NEUTRAL;
            }
        } else {
            this.polarity = null;
        }
    }

    /**
     * Returns the shallow parse layer of the word.
     * @return Shallow parse tag of the word.
     */
    public String getShallowParse(){
        return shallowParse;
    }

    /**
     * Sets the shallow parse layer of the word.
     * @param parse New shallow parse tag of the word.
     */
    public void setShallowParse(String parse){
        shallowParse = parse;
    }

    /**
     * Returns the universal dependency layer of the word.
     * @return Universal dependency relation of the word.
     */
    public UniversalDependencyRelation getUniversalDependency(){
        return universalDependency;
    }

    /**
     * Sets the universal dependency layer of the word.
     * @param to Word related to.
     * @param dependencyType type of dependency the word is related to.
     */
    public void setUniversalDependency(int to, String dependencyType){
        if (to < 0){
            universalDependency = null;
        } else {
            universalDependency = new UniversalDependencyRelation(to, dependencyType);
        }
    }

    /**
     * Returns the universal pos of the word.
     * @return If the language is Turkish, it directly calls getUniversalDependencyPos of the parse. If the language
     * is English, it returns pos according to the Penn tag of the current word.
     */
    public String getUniversalDependencyPos(){
        if (language == Language.TURKISH && parse != null){
            return parse.getUniversalDependencyPos();
        } else {
            if (language == Language.ENGLISH && posTag != null){
                switch (posTag){
                    case "#":
                    case "$":
                    case "SYM":
                        return "SYM";
                    case "\"":
                    case ",":
                    case "-LRB-":
                    case "-RRB-":
                    case ".":
                    case ":":
                    case "``":
                    case "HYPH":
                        return "PUNCT";
                    case "AFX":
                    case "JJ":
                    case "JJR":
                    case "JJS":
                        return "ADJ";
                    case "CC":
                        return "CCONJ";
                    case "CD":
                        return "NUM";
                    case "DT":
                    case "PDT":
                    case "PRP$":
                    case "WDT":
                    case "WP$":
                        return "DET";
                    case "IN":
                    case "RP":
                        return "ADP";
                    case "FW":
                    case "LS":
                    case "NIL":
                        return "X";
                    case "VB":
                    case "VBD":
                    case "VBG":
                    case "VBN":
                    case "VBP":
                    case "VBZ":
                        return "VERB";
                    case "MD":
                    case "AUX:VB":
                    case "AUX:VBD":
                    case "AUX:VBG":
                    case "AUX:VBN":
                    case "AUX:VBP":
                    case "AUX:VBZ":
                        return "AUX";
                    case "NN":
                    case "NNS":
                        return "NOUN";
                    case "NNP":
                    case "NNPS":
                        return "PROPN";
                    case "POS":
                    case "TO":
                        return "PART";
                    case "EX":
                    case "PRP":
                    case "WP":
                        return "PRON";
                    case "RB":
                    case "RBR":
                    case "RBS":
                    case "WRB":
                        return "ADV";
                    case "UH":
                        return "INTJ";
                }
            }
        }
        return null;
    }

    /**
     * Returns the features of the universal dependency relation of the current word.
     * @return If the language is Turkish, it calls getUniversalDependencyFeatures of the parse. If the language is
     * English, it returns dependency features according to the Penn tag of the current word.
     */
    public ArrayList<String> getUniversalDependencyFeatures(){
        ArrayList<String> featureList = new ArrayList<>();
        if (language == Language.TURKISH && parse != null){
            return parse.getUniversalDependencyFeatures(parse.getUniversalDependencyPos());
        } else {
            if (language == Language.ENGLISH && posTag != null) {
                switch (posTag){
                    case "\"":
                        featureList.add("PunctSide=Fin");
                        featureList.add("PunctType=Quot");
                        break;
                    case ",":
                        featureList.add("PunctType=Comm");
                        break;
                    case "-LRB-":
                        featureList.add("PunctSide=Ini");
                        featureList.add("PunctType=Brck");
                        break;
                    case "-RRB-":
                        featureList.add("PunctSide=Fin");
                        featureList.add("PunctType=Brck");
                        break;
                    case ".":
                        featureList.add("PunctType=Peri");
                        break;
                    case "``":
                        featureList.add("PunctSide=Ini");
                        featureList.add("PunctType=Quot");
                        break;
                    case "HYPH":
                        featureList.add("PunctType=Dash");
                        break;
                    case "AFX":
                        featureList.add("Hyph=Yes");
                        break;
                    case "JJ":
                    case "RB":
                        featureList.add("Degree=Pos");
                        if (name.equalsIgnoreCase("first") || name.equalsIgnoreCase("second") || name.equalsIgnoreCase("third")
                        || name.equalsIgnoreCase("fourth") || name.equalsIgnoreCase("fifth") || name.equalsIgnoreCase("sixth")
                        || name.equalsIgnoreCase("seventh") || name.equalsIgnoreCase("eighth") || name.equalsIgnoreCase("ninth")
                        || name.equalsIgnoreCase("tenth")){
                            featureList.add("NumType=Ord");
                        }
                        break;
                    case "JJR":
                    case "RBR":
                        featureList.add("Degree=Cmp");
                        break;
                    case "JJS":
                    case "RBS":
                        featureList.add("Degree=Sup");
                        break;
                    case "CD":
                        featureList.add("NumType=Card");
                        break;
                    case "DT":
                        featureList.add("PronType=Art");
                        break;
                    case "PDT":
                        featureList.add("AdjType=Pdt");
                        break;
                    case "PRP$":
                        switch (name.toLowerCase()) {
                            case "her":
                                featureList.add("Gender=Fem");
                                break;
                            case "his":
                                featureList.add("Gender=Masc");
                                break;
                            case "its":
                                featureList.add("Gender=Neut");
                                break;
                        }
                        switch (name.toLowerCase()){
                            case "my":
                            case "your":
                            case "his":
                            case "her":
                            case "its":
                                featureList.add("Number=Sing");
                                break;
                            case "our":
                            case "their":
                                featureList.add("Number=Plur");
                                break;
                        }
                        featureList.add("Poss=Yes");
                        featureList.add("PronType=Prs");
                        break;
                    case "WDT":
                    case "WP":
                    case "WRB":
                        featureList.add("PronType=Int,Rel");
                        break;
                    case "WP$":
                        featureList.add("Poss=Yes");
                        featureList.add("PronType=Int,Rel");
                        break;
                    case "RP":
                    case "MD":
                    case "POS":
                    case "TO":
                        break;
                    case "FW":
                        featureList.add("Foreign=Yes");
                        break;
                    case "LS":
                        featureList.add("NumType=Ord");
                        break;
                    case "VB":
                    case "AUX:VB":
                        featureList.add("VerbForm=Inf");
                        break;
                    case "VBD":
                    case "AUX:VBD":
                        featureList.add("Mood=Ind");
                        featureList.add("Tense=Past");
                        featureList.add("VerbForm=Fin");
                        break;
                    case "VBG":
                    case "AUX:VBG":
                        featureList.add("Tense=Pres");
                        featureList.add("VerbForm=Part");
                        break;
                    case "VBN":
                    case "AUX:VBN":
                        featureList.add("Tense=Past");
                        featureList.add("VerbForm=Part");
                        break;
                    case "VBP":
                    case "AUX:VBP":
                        featureList.add("Mood=Ind");
                        featureList.add("Tense=Pres");
                        featureList.add("VerbForm=Fin");
                        break;
                    case "VBZ":
                    case "AUX:VBZ":
                        featureList.add("Mood=Ind");
                        featureList.add("Number=Sing");
                        featureList.add("Person=3");
                        featureList.add("Tense=Pres");
                        featureList.add("VerbForm=Fin");
                        break;
                    case "NN":
                    case "NNP":
                        featureList.add("Number=Sing");
                        break;
                    case "NNS":
                    case "NNPS":
                        featureList.add("Number=Plur");
                        break;
                    case "EX":
                        featureList.add("PronType=Dem");
                        break;
                    case "PRP":
                        switch (name.toLowerCase()) {
                            case "i":
                            case "myself":
                            case "he":
                            case "she":
                            case "it":
                            case "you":
                            case "we":
                                featureList.add("Case=Nom");
                                break;
                            case "me":
                            case "yourself":
                            case "him":
                            case "us":
                            case "them":
                            case "theirs":
                                featureList.add("Case=Acc");
                                break;
                        }
                        switch (name.toLowerCase()) {
                            case "she":
                                featureList.add("Gender=Fem");
                                break;
                            case "he":
                            case "him":
                                featureList.add("Gender=Masc");
                                break;
                            case "it":
                                featureList.add("Gender=Neut");
                                break;
                        }
                        switch (name.toLowerCase()){
                            case "i":
                            case "me":
                            case "myself":
                                featureList.add("Number=Sing");
                                featureList.add("Person=1");
                                break;
                            case "you":
                            case "yourself":
                                featureList.add("Number=Sing");
                                featureList.add("Person=2");
                                break;
                            case "he":
                            case "she":
                            case "it":
                            case "him":
                            case "himself":
                            case "herself":
                            case "itself":
                            case "oneself":
                                featureList.add("Number=Sing");
                                featureList.add("Person=3");
                                break;
                            case "ours":
                            case "ourselves":
                            case "we":
                            case "us":
                                featureList.add("Number=Plur");
                                featureList.add("Person=1");
                                break;
                            case "they":
                            case "them":
                            case "themselves":
                            case "theirs":
                                featureList.add("Number=Plur");
                                featureList.add("Person=3");
                                break;
                        }
                        featureList.add("PronType=Prs");
                        switch (name.toLowerCase()){
                            case "myself":
                            case "yourself":
                            case "himself":
                            case "herself":
                            case "itself":
                            case "oneself":
                            case "ourselves":
                            case "themselves":
                                featureList.add("Reflex=Yes");
                                break;
                        }
                        break;
                }
            }
        }
        return featureList;
    }

    /**
     * Returns true if the dependency relation is GOESWITH, false otherwise.
     * @return True if the dependency relation is GOESWITH, false otherwise.
     */
    public boolean goesWithCase(){
        if (getUniversalDependency() != null){
            return getUniversalDependency().toString().equals("GOESWITH");
        }
        return false;
    }

    /**
     * Returns the connlu format string for this word. Adds surface form, root, universal pos tag, features, and
     * universal dependency information.
     * @param sentenceLength Number of words in the sentence.
     * @param goesWithHead If true, the word is a typo and typo feature is added.
     * @return The connlu format string for this word.
     */
    public String getUniversalDependencyFormat(int sentenceLength, boolean goesWithHead){
        String result;
        String uPos;
        if (goesWithCase()){
            uPos = "X";
        } else {
            uPos = getUniversalDependencyPos();
        }
        if (uPos != null){
            if (goesWithCase()){
                result = name + "\t_\t" + uPos + "\t_\t";
            } else {
                switch (language){
                    case TURKISH:
                    default:
                        result = name + "\t" + parse.getWord().getName() + "\t" + uPos + "\t_\t";
                        break;
                    case ENGLISH:
                        if (metamorphicParse != null){
                            result = name + "\t" + metamorphicParse.getWord().getName() + "\t" + uPos + "\t_\t";
                        } else {
                            result = name + "\t" + name + "\t" + uPos + "\t_\t";
                        }
                        break;
                }
            }
            ArrayList<String> features = getUniversalDependencyFeatures();
            if (goesWithHead){
                features.add("Typo=Yes");
            }
            if (features.isEmpty() || goesWithCase()){
                result = result + "_";
            } else {
                boolean first = true;
                StringBuilder resultBuilder = new StringBuilder(result);
                for (String feature : features){
                    if (first){
                        first = false;
                    } else {
                        resultBuilder.append("|");
                    }
                    resultBuilder.append(feature);
                }
                result = resultBuilder.toString();
            }
            result += "\t";
            if (universalDependency != null && universalDependency.to() <= sentenceLength){
                result += universalDependency.to() + "\t" + universalDependency.toString().toLowerCase() + "\t";
            } else {
                result += "_\t_\t";
            }
            result += "_\t_";
            return result;
        } else {
            return name + "\t" + name + "\t_\t_\t_\t_\t_\t_\t_";
        }
    }

    public String getFormattedString(WordFormat format) throws LayerNotExistsException {
        switch (format){
            case SURFACE:
            default:
                return name;
        }
    }

    /**
     * Returns the GRAMMATICAL_ERROR layer of the word.
     * @return GRAMMATICAL_ERROR string of the word.
     */
    public GrammaticalError getGrammaticalError() {
        return grammaticalError;
    }

    /**
     * Sets the GRAMMATICAL_ERROR layer of the word.
     * @param newValue New value of the grammatical error
     */
    public void setGrammaticalError(String newValue) {
        this.grammaticalError = new GrammaticalError(newValue);
    }

    /**
     * Returns the CCG layer of the word.
     * @return CCG string of the word.
     */
    public String getCcg() {
        return ccg;
    }

    /**
     * Sets the CCG layer of the word.
     * @param ccg New CCG of the word.
     */
    public void setCcg(String ccg) {
        this.ccg = ccg;
    }

    /**
     * Returns the posTag layer of the word.
     * @return posTag string of the word.
     */
    public String getPosTag() {
        return posTag;
    }

    /**
     * Sets the posTag layer of the word.
     * @param posTag New posTag of the word.
     */
    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    /**
     * Accessor method for the area attribute.
     * @return Area attribute.
     */
    public RectAngle getArea(){
        return area;
    }

    /**
     * Mutator method for the area attribute.
     * @param area New area attribute.
     */
    public void setArea(RectAngle area){
        this.area = area;
    }

    /**
     * Returns true if the area contains the point (x, y).
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @return True if the area contains the point (x, y), false otherwise.
     */
    public boolean contains(int x, int y){
        return this.area.contains(x, y);
    }

    /**
     * Mutator method for the area attribute.
     * @param x x coordinate of the upper-left point.
     * @param y y coordinate of the upper-left point.
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     */
    public void setArea(int x, int y, int width, int height){
        this.area = new RectAngle(x, y, width, height);
    }

    /**
     * Accessor method for the selected attribute.
     * @return Selected attribute value.
     */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Accessor method for the forceSelected attribute.
     * @return ForceSelected attribute value.
     */
    public boolean isForceSelected(){
        return forceSelected;
    }

    /**
     * Mutator method for the selected attribute.
     * @param selected New value for the selected attribute.
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }

    /**
     * Mutator method for the forceSelected attribute.
     * @param forceSelected New value for the forceSelected attribute.
     */
    public void setForceSelected(boolean forceSelected){
        this.forceSelected = forceSelected;
    }

    /**
     * Checks the gazetteer and sets the named entity tag accordingly.
     * @param gazetteer Gazetteer used to set named entity tag.
     */
    public void checkGazetteer(Gazetteer gazetteer){
        String wordLowercase = name.toLowerCase(new Locale("tr"));
        if (gazetteer.contains(wordLowercase) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(gazetteer.getName());
        }
        if (wordLowercase.contains("'") && gazetteer.contains(wordLowercase.substring(0, wordLowercase.indexOf("'"))) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(gazetteer.getName());
        }
    }

    /**
     * Converts a language string to language.
     * @param languageString String defining the language name.
     * @return Language corresponding to the languageString.
     */
    private Language getLanguageFromString(String languageString){
        switch (languageString){
            case "turkish":
            case "Turkish":
                return Language.TURKISH;
            case "english":
            case "English":
                return Language.ENGLISH;
            case "persian":
            case "Persian":
                return Language.PERSIAN;
        }
        return Language.TURKISH;
    }

    /**
     * Returns the language of the word.
     * @return The language of the word.
     */
    public Language getLanguage(){
        return language;
    }

}
