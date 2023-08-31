/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ctakes.web.client.servlet;

import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.ctakes.contexttokenizer.ae.ContextDependentTokenizerAnnotator;
import org.apache.ctakes.core.ae.SentenceDetector;
import org.apache.ctakes.core.ae.SimpleSegmentAnnotator;
import org.apache.ctakes.core.ae.TokenizerAnnotatorPTB;
import org.apache.ctakes.dictionary.lookup2.ae.AbstractJCasTermAnnotator;
import org.apache.ctakes.dictionary.lookup2.ae.DefaultJCasTermAnnotator;
import org.apache.ctakes.lvg.ae.LvgAnnotator;
import org.apache.ctakes.lvg.resource.LvgCmdApiResourceImpl;
import org.apache.ctakes.postagger.POSTagger;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;


public class Pipeline {	
	private static URL getTempLVGFilePath() throws IOException {
		final String lvgProperties = "org/apache/ctakes/lvg/data/config/lvg.properties";
		InputStream in = Pipeline.class.getClassLoader().getResourceAsStream(lvgProperties);
		File tempFile = File.createTempFile("lvg", ".properties");
		tempFile.deleteOnExit();
		FileUtils.copyInputStreamToFile(in, tempFile);

		return tempFile.toURL();
	}
	

	public static AggregateBuilder getAggregateBuilder() throws Exception {

		// setup local files for handling URL bug in fat jar
		URL lvgPath = getTempLVGFilePath();
		System.out.println("lvgPath URL => " + lvgPath); // debug

		AggregateBuilder builder = new AggregateBuilder();
		  builder.add(SimpleSegmentAnnotator.createAnnotatorDescription());
	      builder.add(SentenceDetector.createAnnotatorDescription());
		  builder.add(TokenizerAnnotatorPTB.createAnnotatorDescription());
		  builder.add(ContextDependentTokenizerAnnotator.createAnnotatorDescription());
		  builder.add(POSTagger.createAnnotatorDescription());
		  builder.add(AnalysisEngineFactory.createEngineDescription(
				DefaultJCasTermAnnotator.class,
				AbstractJCasTermAnnotator.PARAM_WINDOW_ANNOT_KEY,
				"org.apache.ctakes.typesystem.type.textspan.Sentence",
				"DictionaryDescriptor",
				"org/apache/ctakes/dictionary/lookup/fast/sno_rx_16ab.xml"
				));
		  // builder.add(LvgAnnotator.createAnnotatorDescription()); // breaking
		  // create a custom engine description for LVG:
		  builder.add(AnalysisEngineFactory.createEngineDescription(
				LvgAnnotator.class,
				LvgAnnotator.PARAM_USE_CMD_CACHE,
				false,
				LvgAnnotator.PARAM_USE_LEMMA_CACHE,
				false,
				LvgAnnotator.PARAM_USE_SEGMENTS,
				false,
				LvgAnnotator.PARAM_LEMMA_CACHE_FREQUENCY_CUTOFF,
				20,
				LvgAnnotator.PARAM_LEMMA_FREQ_CUTOFF,
				20,
				LvgAnnotator.PARAM_POST_LEMMAS,
				false,
				LvgAnnotator.PARAM_LVGCMDAPI_RESRC_KEY,
				ExternalResourceFactory.createExternalResourceDescription(
					LvgCmdApiResourceImpl.class,
					 lvgPath
					)));

		return builder;
	}
}
