package example;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.ruta.engine.RutaEngine;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;

import example.annotate.AnotherName;
import example.annotate.Name;

public class Annotate {

    public static void main(String[] args)
            throws AnalysisEngineProcessException, InvalidXMLException,
            ResourceInitializationException, ResourceConfigurationException,
            IOException, URISyntaxException, CASException {

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

        // run the script. instead of a jCas, you could also provide a UIMA
        // collection reader to process many documents
        SimplePipeline.runPipeline(jCas, rutaEngine);

        // a simple select to print the matched Names
        for (Name name : JCasUtil.select(jCas, Name.class)) {
            System.out.println(name.getCoveredText());
        }
    }
}
