package AnnotatedSentence;

import Corpus.Corpus;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UdToSvgConvertTest {

    public void convertToSvg(Corpus corpus) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        Point2D.Double pointCtrl1, pointCtrl2, pointStart, pointEnd;
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            int maxSize = 50;
            int wordSpace = 80;
            int currentLeft = wordSpace;
            int lineSpace = 120;
            output.println("<svg width=\"" + (80 + 130 * sentence.wordCount()) + "\" height=\"400\">");
            ArrayList<Integer> wordSize = new ArrayList<>();
            ArrayList<Integer> wordTotal = new ArrayList<>();
            for (int j = 0; j < sentence.wordCount(); j++) {
                wordTotal.add(currentLeft);
                wordSize.add(maxSize);
                currentLeft += maxSize + wordSpace;
            }
            currentLeft = wordSpace;
            for (int j = 0; j < sentence.wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                output.println("<text x=\"" + currentLeft + "\" y=\"" + lineSpace + "\">" + word.getName() + "</text>");
                if (word.getUniversalDependencyPos() != null){
                    output.println("<text fill=\"red\"  x=\"" + currentLeft + "\" y=\"" + (lineSpace + 30) + "\">" + word.getUniversalDependencyPos() + "</text>");
                    if (word.getParse() != null){
                        ArrayList<String> features = word.getParse().getUniversalDependencyFeatures(word.getUniversalDependencyPos());
                        for (int k = 0; k < features.size(); k++){
                            output.println("<text fill=\"blue\" x=\"" + currentLeft + "\" y=\"" + (lineSpace + 30 * (k + 2)) + "\">" + features.get(k) + "</text>");
                        }
                    }
                }
                if (word.getUniversalDependency() != null){
                    String correct = word.getUniversalDependency().toString().toLowerCase();
                    if (word.getUniversalDependency().to() != 0){
                        String color = "black";
                        switch (correct){
                            case "acl":
                                color = "#ffd600";
                                break;
                            case "advcl":
                                color = "#00796b";
                                break;
                            case "aux":
                                color = "#000080";
                                break;
                            case "advmod":
                                color = "#1e88d5";
                                break;
                            case "amod":
                                color = "#b71c1c";
                                break;
                            case "det":
                                color = "#ff80ab";
                                break;
                            case "flat":
                                color = "#6a1b9a";
                                break;
                            case "obj":
                                color = "#43a047";
                                break;
                            case "conj":
                                color = "#afb42b";
                                break;
                            case "mark":
                                color = "#ff6f00";
                                break;
                            case "nmod":
                                color = "#ff8a65";
                                break;
                            case "nsubj":
                                color = "#b39ddb";
                                break;
                            case "obl":
                                color = "#87cefa";
                                break;
                            case "compound":
                                color = "#546e7a";
                                break;
                            case "cc":
                                color = "#795548";
                                break;
                            case "ccomp":
                                color = "#cd5c5c";
                                break;
                            case "case":
                                color = "#bc8f8f";
                                break;
                            case "nummod":
                                color = "#8fbc8f";
                                break;
                            case "xcomp":
                                color = "#d2691d";
                                break;
                            case "parataxis":
                                color = "#5c6bc0";
                                break;
                        }
                        int startX = currentLeft + maxSize / 2;
                        int startY = lineSpace;
                        double height = Math.pow(Math.abs(word.getUniversalDependency().to() - 1 - j), 0.7);
                        int toX = wordTotal.get(word.getUniversalDependency().to() - 1) + wordSize.get(word.getUniversalDependency().to() - 1) / 2;
                        pointEnd = new Point2D.Double(startX + 5 * Math.signum(word.getUniversalDependency().to() - 1 - j) + 30, startY);
                        pointStart = new Point2D.Double(toX - 30, startY);
                        int controlY = (int) (pointStart.y - 20 - 20 * height);
                        output.println("<text fill=\"" + color + "\" x=\"" + (((int) (pointStart.x + pointEnd.x) / 2) - 25) + "\" y=\"" + (int) (controlY + 4 * height) + "\">" + correct + "</text>");
                        pointCtrl1 = new Point2D.Double(pointStart.x, controlY);
                        pointCtrl2 = new Point2D.Double(pointEnd.x, controlY);
                        output.println("<path fill=\"none\" style=\"stroke:" + color + ";stroke-width:2\" d=\"M" + pointStart.x + "," + pointStart.y + " C" + pointCtrl1.x + "," + pointCtrl1.y + " " + pointCtrl2.x + "," + pointCtrl2.y + " " + pointEnd.x + "," + pointEnd.y + "\" />");
                        output.println("<line x1=\"" + (int) pointEnd.x + "\" y1=\"" + (int) pointEnd.y + "\" x2=\"" + ((int) pointEnd.x - 5) + "\" y2=\"" + ((int) pointEnd.y - 5) + "\" style=\"stroke:black;stroke-width:2\"/>");
                        output.println("<line x1=\"" + (int) pointEnd.x + "\" y1=\"" + (int) pointEnd.y + "\" x2=\"" + ((int) pointEnd.x + 5) + "\" y2=\"" + ((int) pointEnd.y - 5) + "\" style=\"stroke:black;stroke-width:2\"/>");
                    } else {
                        output.println("<text fill=\"" + "black" + "\" x=\"" + (currentLeft + maxSize / 2 - 10) + "\" y=\"" + (lineSpace - 50) + "\">" + "root" + "</text>");
                        output.println("<line x1=\"" + (currentLeft + maxSize / 2) + "\" y1=\"" + (lineSpace - 40) + "\" x2=\"" + (currentLeft + maxSize / 2) + "\" y2=\"" + (lineSpace - 10) + "\" style=\"stroke:black;stroke-width:2\"/>");
                    }
                }
                currentLeft += maxSize + wordSpace;
            }
            output.println("</svg>");
        }
        output.close();
    }

    public void testConvertAtisTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Atis/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertGbTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Gb/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertPudTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Pud/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertImstTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Imst/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertTourismTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Etstur/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertFramenetTurkish() throws FileNotFoundException {
        File[] listOfFiles = new File("../../FrameNet-Examples/Turkish-Phrase/").listFiles();
        Corpus temp = null;
        for (File file:listOfFiles) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                String fileName = file.getName();
                AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../FrameNet-Examples/Turkish-Phrase/" + fileName));
                if (temp == null){
                    temp = corpus;
                } else {
                    temp.combine(corpus);
                }
            }
        }
        convertToSvg(temp);
    }

    public void testConvertKenetTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Kenet-Examples/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

    public void testConvertPennTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Penn-Treebank/Turkish-Phrase/"));
        AnnotatedCorpus corpus2 = new AnnotatedCorpus(new File("../../Penn-Treebank-20/Turkish-Phrase/"));
        corpus.combine(corpus2);
        convertToSvg(corpus);
    }

    public void testConvertBounTurkish() throws FileNotFoundException {
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../../Boun/Turkish-Phrase/"));
        convertToSvg(corpus);
    }

}
