package life.inova.nlp.ctakes;

import java.io.ByteArrayOutputStream;
import org.apache.uima.jcas.JCas;
import org.apache.uima.json.JsonCasSerializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.analysis_engine.AnalysisEngine;


public class TaggerPipeline {
    AggregateBuilder builder; 
    AnalysisEngine pipeline;
    
    public void init() throws Exception {
       builder = CustomAnalyzerEngine.getAggregateBuilder();
       pipeline = builder.createAggregate();
    }
    
    public String processToXML(String textInput) throws Exception {
        // Prepare the buffer for results
        StringBuffer sb = new StringBuffer();
        
        // Process inputs
        JCas jcas = pipeline.newJCas();
        jcas.setDocumentText(textInput);
        pipeline.process(jcas);

        // Serialize and cleanup for next round before return results
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmiCasSerializer.serialize(jcas.getCas(), output);
        sb.append(output.toString());
        output.close();

        jcas.reset();
        return sb.toString();
    }

    public String processToJSON(String textInput) throws Exception {
        // Prepare the buffer for results
        StringBuffer sb = new StringBuffer();
        
        // Process inputs
        JCas jcas = pipeline.newJCas();
        jcas.setDocumentText(textInput);
        pipeline.process(jcas);

        // Serialize and cleanup for next round before return results
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonCasSerializer.jsonSerialize(jcas.getCas(), output);
        sb.append(output.toString());
        output.close();

        jcas.reset();
        return sb.toString();
    }
    
}
