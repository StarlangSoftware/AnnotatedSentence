# AnnotatedSentence

This resource allows for matching of Turkish words or expressions with their corresponding entries within the Turkish dictionary and the Turkish PropBank, morphological analysis, named entity recognition and shallow parsing.

## Data Format

The structure of a sample annotated word is as follows:

	{turkish=yatırımcılar}
	{analysis=yatırımcı+NOUN+A3PL+PNON+NOM}
	{semantics=0841060}
	{namedEntity=NONE}
	{shallowParse=ÖZNE}
	{propbank=ARG0:0006410}

As is self-explanatory, 'turkish' tag shows the original Turkish word; 'analysis' tag shows the correct morphological parse of that word; 'semantics' tag shows the ID of the correct sense of that word; 'namedEntity' tag shows the named entity tag of that word; 'shallowParse' tag shows the semantic role of that word; 'propbank' tag shows the semantic role of that word for the verb synset id (frame id in the frame file) which is also given in that tag.

For Developers
============
You can also see either [Python](https://github.com/olcaytaner/AnnotatedSentence-Py) 
or [C++](https://github.com/olcaytaner/AnnotatedSentence-CPP) repository.

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called AnnotatedSentence will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/AnnotatedSentence.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `AnnotatedSentence/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run AnnotatedSentence.

**From Console**

Go to `AnnotatedSentence` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `AnnotatedSentence` root module.

**From Console**

Use below line to generate jar file:

     mvn install

## Maven Usage

	<dependency>
		<groupId>NlpToolkit</groupId>
		<artifactId>AnnotatedSentence</artifactId>
		<version>1.0.8</version>
	</dependency>

------------------------------------------------

Detailed Description
============
+ [AnnotatedCorpus](#annotatedcorpus)
+ [AnnotatedSentence](#annotatedsentence)
+ [AnnotatedWord](#annotatedword)
+ [Automatic Annotation](#automatic-annotation)


## AnnotatedCorpus

İşaretlenmiş corpusu yüklemek için

	AnnotatedCorpus(File folder, String pattern)
	a = AnnotatedCorpus(new File("/Turkish-Phrase"), ".train")

	AnnotatedCorpus(File folder)
	a = AnnotatedCorpus(new File("/Turkish-Phrase"))

Bir AnnotatedCorpus'daki tüm cümlelere erişmek için

	for (int i = 0; i < a.sentenceCount(); i++){
		AnnotatedSentence annotatedSentence = (AnnotatedSentence) a.getSentence(i);
		....
	}

## AnnotatedSentence

Bir AnnotatedSentence'daki tüm kelimelere ulaşmak için de

	for (int j = 0; j < annotatedSentence.wordCount(); j++){
		AnnotatedWord annotatedWord = (AnnotatedWord) annotatedSentence.getWord(j);
		...
	}

## AnnotatedWord

İşaretlenmiş bir kelime AnnotatedWord sınıfında tutulur. İşaretlenmiş kelimenin morfolojik
analizi

	MorphologicalParse getParse()

İşaretlenmiş kelimenin anlamı

	String getSemantic()

İşaretlenmiş kelimenin NER anotasyonu

	NamedEntityType getNamedEntityType()

İşaretlenmiş kelimenin özne, dolaylı tümleç, vs. shallow parse tagı

	String getShallowParse()

İşaretlenmiş kelimenin dependency anotasyonu

	UniversalDependencyRelation getUniversalDependency()
	
## Automatic Annotation

Bir cümlenin Predicatelarını otomatik olarak belirlemek için

	TurkishSentenceAutoPredicate(FramesetList framesetList)

sınıfı kullanılır. Örneğin,

	a = TurkishSentenceAutoPredicate(new FramesetList());
	a.autoPredicate(sentence);

ile sentence cümlesinin predicateları otomatik olarak işaretlenir.

Bir cümlenin argümanlarını otomatik olarak belirlemek için

	TurkishSentenceAutoArgument()

sınıfı kullanılır. Örneğin,

	a = TurkishSentenceAutoArgument();
	a.autoArgument(sentence);

ile sentence cümlesinin argümanları otomatik olarak işaretlenir.

Bir cümlede otomatik olarak morfolojik belirsizlik gidermek için

	TurkishSentenceAutoDisambiguator(RootWordStatistics rootWordStatistics)
	TurkishSentenceAutoDisambiguator(FsmMorphologicalAnalyzer fsm, RootWordStatistics rootWordStatistics)
								  
sınıfı kullanılır. Örneğin,

	a = TurkishSentenceAutoDisambiguator(new RootWordStatistics());
	a.autoDisambiguate(sentence);

ile sentence cümlesinin morfolojik belirsizlik gidermesi otomatik olarak yapılır.

Bir cümlede adlandırılmış varlık tanıma yapmak için

	TurkishSentenceAutoNER()

sınıfı kullanılır. Örneğin,

	a = TurkishSentenceAutoNER();
	a.autoNER(sentence);

ile sentence cümlesinde varlık tanıma otomatik olarak yapılır.

Bir cümlede anlamsal işaretleme için

	TurkishSentenceAutoSemantic()

sınıfı kullanılır. Örneğin,

	a = TurkishSentenceAutoSemantic();
	a.autoSemantic(sentence);

ile sentence cümlesinde anlamsal işaretleme otomatik olarak yapılır.
