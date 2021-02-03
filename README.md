For Developers
============

You can also see [Python](https://github.com/starlangsoftware/AnnotatedSentence-Py), [C++](https://github.com/starlangsoftware/AnnotatedSentence-CPP), or [C#](https://github.com/starlangsoftware/AnnotatedSentence-CS) repository.

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
            <groupId>io.github.starlangsoftware</groupId>
            <artifactId>AnnotatedSentence</artifactId>
            <version>1.0.9</version>
        </dependency>

Detailed Description
============

+ [AnnotatedCorpus](#annotatedcorpus)
+ [AnnotatedSentence](#annotatedsentence)
+ [AnnotatedWord](#annotatedword)
+ [Automatic Annotation](#automatic-annotation)


## AnnotatedCorpus

To load the annotated corpus:

	AnnotatedCorpus(File folder, String pattern)
	a = AnnotatedCorpus(new File("/Turkish-Phrase"), ".train")

	AnnotatedCorpus(File folder)
	a = AnnotatedCorpus(new File("/Turkish-Phrase"))

To access all the sentences in a AnnotatedCorpus:

	for (int i = 0; i < a.sentenceCount(); i++){
		AnnotatedSentence annotatedSentence = (AnnotatedSentence) a.getSentence(i);
		....
	}

## AnnotatedSentence

To access all the words in a AnnotatedSentence:

	for (int j = 0; j < annotatedSentence.wordCount(); j++){
		AnnotatedWord annotatedWord = (AnnotatedWord) annotatedSentence.getWord(j);
		...
	}

## AnnotatedWord

An annotated word is kept in AnnotatedWord class. To access the morphological analysis of 
the annotated word:

	MorphologicalParse getParse()

Meaning of the annotated word:

	String getSemantic()

NER annotation of the annotated word:

	NamedEntityType getNamedEntityType()

Shallow parse tag of the annotated word (e.g., subject, indirect object):

	String getShallowParse()

Dependency annotation of the annotated word:

	UniversalDependencyRelation getUniversalDependency()
	
## Automatic Annotation

To detect predicates of a sentence automatically

	TurkishSentenceAutoPredicate(FramesetList framesetList)

this class is used. For example, with

	a = TurkishSentenceAutoPredicate(new FramesetList());
	a.autoPredicate(sentence);

the predicates of the sentence "sentence" are annotated automatically.

To detect arguments of a sentence automatically

	TurkishSentenceAutoArgument()

this class is used. For example, with

	a = TurkishSentenceAutoArgument();
	a.autoArgument(sentence);

arguments of the sentence "sentence" are annotated automatically.

To disambiguate the morphological ambiguity in a sentence automatically

	TurkishSentenceAutoDisambiguator(RootWordStatistics rootWordStatistics)
	TurkishSentenceAutoDisambiguator(FsmMorphologicalAnalyzer fsm, RootWordStatistics rootWordStatistics)
								  
this class is used. For example, with 

	a = TurkishSentenceAutoDisambiguator(new RootWordStatistics());
	a.autoDisambiguate(sentence);

morphological disambugiation of the sentence "sentence" is done automatically.

To make a named entity recognition in a sentence

	TurkishSentenceAutoNER()

this class is used. For example, with

	a = TurkishSentenceAutoNER();
	a.autoNER(sentence);

named entity recognition in the sentence "sentence" is done automatically.

To make a semantic annotation in a sentence

	TurkishSentenceAutoSemantic()

this class is used. For example, with

	a = TurkishSentenceAutoSemantic();
	a.autoSemantic(sentence);

semantic annotation of the sentence "sentence" is done automatically.
