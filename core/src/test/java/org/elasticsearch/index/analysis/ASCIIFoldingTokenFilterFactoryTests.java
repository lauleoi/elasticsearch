/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.test.ESTokenStreamTestCase;

import java.io.IOException;
import java.io.StringReader;

public class ASCIIFoldingTokenFilterFactoryTests extends ESTokenStreamTestCase {
    public void testDefault() throws IOException {
        AnalysisService analysisService = AnalysisTestsHelper.createAnalysisServiceFromSettings(Settings.builder()
                .put(Environment.PATH_HOME_SETTING.getKey(), createTempDir().toString())
                .put("index.analysis.filter.my_ascii_folding.type", "asciifolding")
                .build());
        TokenFilterFactory tokenFilter = analysisService.tokenFilter("my_ascii_folding");
        String source = "Ansprüche";
        String[] expected = new String[]{"Anspruche"};
        Tokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader(source));
        assertTokenStreamContents(tokenFilter.create(tokenizer), expected);
    }

    public void testPreserveOriginal() throws IOException {
        AnalysisService analysisService = AnalysisTestsHelper.createAnalysisServiceFromSettings(Settings.builder()
                .put(Environment.PATH_HOME_SETTING.getKey(), createTempDir().toString())
                .put("index.analysis.filter.my_ascii_folding.type", "asciifolding")
                .put("index.analysis.filter.my_ascii_folding.preserve_original", true)
                .build());
        TokenFilterFactory tokenFilter = analysisService.tokenFilter("my_ascii_folding");
        String source = "Ansprüche";
        String[] expected = new String[]{"Anspruche", "Ansprüche"};
        Tokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader(source));
        assertTokenStreamContents(tokenFilter.create(tokenizer), expected);
    }
}
