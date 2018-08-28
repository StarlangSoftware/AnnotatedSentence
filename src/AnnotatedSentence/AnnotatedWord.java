package AnnotatedSentence;

import Corpus.WordFormat;
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
    private String shallowParse;
    private Rectangle area;
    private boolean selected = false;

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
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

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
        return result;
    }

    public AnnotatedWord(String name, NamedEntityType namedEntityType){
        super(name);
        this.namedEntityType = namedEntityType;
        parse = null;
        metamorphicParse = null;
        semantic = null;
        argument = new Argument("NONE", null);
        shallowParse = null;
    }

    public AnnotatedWord(String name, MorphologicalParse parse){
        super(name);
        this.parse = parse;
        this.namedEntityType = NamedEntityType.NONE;
        argument = new Argument("NONE", null);
        metamorphicParse = null;
        semantic = null;
        shallowParse = null;
    }

    public AnnotatedWord(String name, FsmParse parse){
        super(name);
        this.parse = parse;
        this.namedEntityType = NamedEntityType.NONE;
        argument = new Argument("NONE", null);
        setMetamorphicParse(parse.withList());
        semantic = null;
        shallowParse = null;
    }

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

    public MorphologicalParse getParse() {
        return parse;
    }

    public void setParse(String parseString){
        parse = new MorphologicalParse(parseString);
    }

    public MetamorphicParse getMetamorphicParse() {
        return metamorphicParse;
    }

    public void setMetamorphicParse(String parseString){
        metamorphicParse = new MetamorphicParse(parseString);
    }

    public String getSemantic() {
        return semantic;
    }

    public void setSemantic(String semantic){
        this.semantic = semantic;
    }

    public NamedEntityType getNamedEntityType() {
        return namedEntityType;
    }

    public void setNamedEntityType(String namedEntity){
        namedEntityType = NamedEntityType.getNamedEntityType(namedEntity);
    }

    public Argument getArgument() {
        return argument;
    }

    public void setArgument(String argument){
        this.argument = new Argument(argument);
    }

    public String getShallowParse(){
        return shallowParse;
    }

    public void setShallowParse(String parse){
        shallowParse = parse;
    }

    public String getFormattedString(WordFormat format) throws LayerNotExistsException {
        switch (format){
            case SURFACE:
                return name;
            default:
                return name;
        }
    }

    public Rectangle getArea(){
        return area;
    }

    public void setArea(Rectangle area){
        this.area = area;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void checkGazetteer(Gazetteer gazetteer){
        String wordLowercase = name.toLowerCase(new Locale("tr"));
        if (gazetteer.contains(wordLowercase) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(name);
        }
        if (wordLowercase.contains("'") && gazetteer.contains(wordLowercase.substring(0, wordLowercase.indexOf("'"))) && parse.containsTag(MorphologicalTag.PROPERNOUN)){
            setNamedEntityType(name);
        }
    }

}
