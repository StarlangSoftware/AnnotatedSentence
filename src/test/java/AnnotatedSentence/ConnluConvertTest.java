package AnnotatedSentence;

import DependencyParser.Universal.UniversalDependencyTreeBankCorpus;

public class ConnluConvertTest {

    public void testConvertImst(){
        AnnotatedCorpus corpus = new AnnotatedCorpus(new UniversalDependencyTreeBankCorpus("tr_imst2-ud-train.conllu"), "train");
        corpus.exportToPath("../../Imst2/Turkish-Phrase/");
        corpus = new AnnotatedCorpus(new UniversalDependencyTreeBankCorpus("tr_imst2-ud-test.conllu"), "test");
        corpus.exportToPath("../../Imst2/Turkish-Phrase/");
        corpus = new AnnotatedCorpus(new UniversalDependencyTreeBankCorpus("tr_imst2-ud-dev.conllu"), "dev");
        corpus.exportToPath("../../Imst2/Turkish-Phrase/");
    }
}
