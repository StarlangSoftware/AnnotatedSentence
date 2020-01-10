package AnnotatedSentence;

import Corpus.WordFormat;
import DependencyParser.UniversalDependencyRelation;
import Dictionary.Word;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.MetamorphicParse;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MorphologicalTag;
import NamedEntityRecognition.Gazetteer;
import NamedEntityRecognition.NamedEntityType;
import PropBank.Argument;

import java.awt.*;
import java.io.Serializable;
import java.util.Locale;

public class AnnotatedWord extends Word implements Serializable{
    private MorphologicalParse parse;
    private MetamorphicParse metamorphicParse;
    private String semantic;
    private NamedEntityType namedEntityType;
    private Argument argument;
    private UniversalDependencyRelation universalDependency;
    private String shallowParse;
    private Rectangle area;
    private boolean selected = false;

    /**
     * Constructor for the {@link AnnotatedWord} class. Gets the word with its annotation layers as input and sets the
     * corresponding layers.
     * @param word Input word with annotation layers
     */
    public AnnotatedWord(String word){
        String[] splitLayers = word.split("[\\{\\}]");
        for (String layer:splitLayers){
            if (layer.isEmpty())
                continue;
            if (!layer.contains("=")){
                name = layer;
                continue;
            }
            String layerType = layer.substring(0, layer.indexOf("="));
            String layerValue = layer.substring(layer.indexOf("=") + 1);
            if (layerType.equalsIgnoreCase("turkish")){
                name = layerValue;
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
                                    argument = new Argument(layerValue);
                                } else {
                                    if (layerType.equalsIgnoreCase("shallowParse")){
                                        shallowParse = layerValue;
                                    } else {
                                        if (layerType.equalsIgnoreCase("universalDependency")){
                                            String[] values = layerValue.split("\\$");
                                            universalDependency = new UniversalDependencyRelation(Integer.parseInt(values[0]), values[1]);
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

    /**
     * Converts an {@link AnnotatedWord} to string. For each annotation layer, the method puts a left brace, layer name,
     * equal sign and layer value finishing with right brace.
     * @return String form of the {@link AnnotatedWord}.
     */
    public String toString(){
        String result = "{turkish=" + name + "}";
        if (parse != null){
            result = result + "{morphologicalAnalysis=" + parse.toString() + "}";
        }
        if (metamorphicParse != null){
            result = result + "{metaMorphemes=" + metamorphicParse.toString() + "}";
        }
        if (semantic != null){
            result = result + "{semantics=" + semantic + "}";
        }
        if (namedEntityType != null){
            result = result + "{namedEntity=" + namedEntityType.toString() + "}";
        }
        if (argument != null){
            result = result + "{propbank=" + argument.toString() + "}";
        }
        if (shallowParse != null){
            result = result + "{shallowParse=" + shallowParse + "}";
        }
        if (universalDependency != null){
            result = result + "{universalDependency=" + universalDependency.to() + "$" + universalDependency.toString() + "}";
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
        argument = new Argument("NONE", null);
        shallowParse = null;
        universalDependency = null;
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
        argument = new Argument("NONE", null);
        metamorphicParse = null;
        semantic = null;
        shallowParse = null;
        universalDependency = null;
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
        argument = new Argument("NONE", null);
        setMetamorphicParse(parse.withList());
        semantic = null;
        shallowParse = null;
        universalDependency = null;
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
                if (argument != null){
                    return argument.toString();
                }
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
    public Argument getArgument() {
        return argument;
    }

    /**
     * Sets the semantic role layer of the word.
     * @param argument New semantic role tag of the word.
     */
    public void setArgument(String argument){
        if (argument != null){
            this.argument = new Argument(argument);
        } else {
            this.argument = null;
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
        universalDependency = new UniversalDependencyRelation(to, dependencyType);
    }

    public String getFormattedString(WordFormat format) throws LayerNotExistsException {
        switch (format){
            case SURFACE:
                return name;
            default:
                return name;
        }
    }

    /**
     * Accessor method for the area attribute.
     * @return Area attribute.
     */
    public Rectangle getArea(){
        return area;
    }

    /**
     * Mutator method for the area attribute.
     * @param area New area attribute.
     */
    public void setArea(Rectangle area){
        this.area = area;
    }

    /**
     * Accessor method for the selected attribute.
     * @return Selected attribute value.
     */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Mutator method for the selected attribute.
     * @param selected New value for the selected attribute.
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void checkGazetteer(Gazetteer gazetteer){
        String wordLowercase = name.toLowerCase(new Locale("tr"));
        if (gazetteer.contains(wordLowercase) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(gazetteer.getName());
        }
        if (wordLowercase.contains("'") && gazetteer.contains(wordLowercase.substring(0, wordLowercase.indexOf("'"))) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(gazetteer.getName());
        }
    }

}
