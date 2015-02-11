# annotate_ruta_example

An example how to annotate text with Apache Ruta. The main parts are:

a list of names in `src/main/resources/ruta/resources/names.txt` (a plain text file)

    Mark
    John
    Rabbit
    Owl
    Curry
    ATH-MX50
    CC234

a Ruta script in `src/main/resources/ruta/scripts/Example.ruta`

    PACKAGE example.annotate;               // optional package def

    WORDLIST MyNames = 'names.txt';         // declare dictionary location
    DECLARE Name;                           // declare an annotation
    Document{-> MARKFAST(Name, MyNames)};   // annotate document

and some java boilerplate code to launch the annotator:

    JCas jCas = CasCreationUtils.createCas(
        TypeSystemDescriptionFactory.createTypeSystemDescription(),
        null, null).getJCas();

    // the sample text to annotate
    jCas.setDocumentText("Mark wants to buy CC234.");

    // configure the engine with scripts and resources
    AnalysisEngine rutaEngine = AnalysisEngineFactory.createEngine(
        RutaEngine.class, //
        RutaEngine.PARAM_RESOURCE_PATHS,
        "src/main/resources/ruta/resources",//
        RutaEngine.PARAM_SCRIPT_PATHS,
        "src/main/resources/ruta/scripts",
        RutaEngine.PARAM_MAIN_SCRIPT, "Example");

    // run the script. instead of a jCas, you could also provide a UIMA collection reader to process many documents
    SimplePipeline.runPipeline(jCas, rutaEngine);

    // a simple select to print the matched Names
    for (Name name : JCasUtil.select(jCas, Name.class)) {
        System.out.println(name.getCoveredText());
    }

there is also some UIMA type (annotation) definitions, check `src/main/resources/desc/type/ExampleTypes.xml`, `src/main/resources/META-INF/org.apache.uima.fit/types.txt` and `src/main/java/example/annotate`.




### how to test it

    mvn clean install
    mvn exec:java -Dexec.mainClass="example.Annotate"
