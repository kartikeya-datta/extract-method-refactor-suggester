error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7562.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7562.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7562.java
text:
```scala
a@@ssertThat(corrections[3].join(new BytesRef(" ")).utf8ToString(), equalTo("varr the god jewel"));

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
package org.elasticsearch.search.suggest.phrase;

import com.google.common.base.Charsets;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.search.spell.DirectSpellChecker;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.elasticsearch.search.suggest.phrase.NoisyChannelSpellChecker.Result;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class NoisyChannelSpellCheckerTests extends ElasticsearchTestCase{
    private final BytesRef space = new BytesRef(" ");
    private final BytesRef preTag = new BytesRef("<em>");
    private final BytesRef postTag = new BytesRef("</em>");

    @Test
    public void testMarvelHeros() throws IOException {
        RAMDirectory dir = new RAMDirectory();
        Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
        mapping.put("body_ngram", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                ShingleFilter tf = new ShingleFilter(t, 2, 3);
                tf.setOutputUnigrams(false);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, tf));
            }

        });

        mapping.put("body", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, t));
            }

        });
        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(Version.LUCENE_41), mapping);

        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_41, wrapper);
        IndexWriter writer = new IndexWriter(dir, conf);
        BufferedReader reader = new BufferedReader(new InputStreamReader(NoisyChannelSpellCheckerTests.class.getResourceAsStream("/config/names.txt"), Charsets.UTF_8));
        String line = null;
        while ((line = reader.readLine()) != null) {
            Document doc = new Document();
            doc.add(new Field("body", line, TextField.TYPE_NOT_STORED));
            doc.add(new Field("body_ngram", line, TextField.TYPE_NOT_STORED));
            writer.addDocument(doc);
        }

        DirectoryReader ir = DirectoryReader.open(writer, false);
        WordScorer wordScorer = new LaplaceScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.95d, new BytesRef(" "), 0.5f);
        
        NoisyChannelSpellChecker suggester = new NoisyChannelSpellChecker();
        DirectSpellChecker spellchecker = new DirectSpellChecker();
        spellchecker.setMinQueryLength(1);
        DirectCandidateGenerator generator = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_MORE_POPULAR, ir, 0.95, 5);
        Result result = suggester.getCorrections(wrapper, new BytesRef("american ame"), generator, 1, 1, ir, "body", wordScorer, 1, 2);
        Correction[] corrections = result.corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("american ace"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("american <em>ace</em>"));
        assertThat(result.cutoffScore, greaterThan(0d));
        
        result = suggester.getCorrections(wrapper, new BytesRef("american ame"), generator, 1, 1, ir, "body", wordScorer, 0, 1);
        corrections = result.corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("american ame"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("american ame"));
        assertThat(result.cutoffScore, equalTo(Double.MIN_VALUE));

        suggester = new NoisyChannelSpellChecker(0.85);
        wordScorer = new LaplaceScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.5f);
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 4, ir, "body", wordScorer, 0, 2).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(space).utf8ToString(), equalTo("xor the god jewel"));
        assertThat(corrections[2].join(space).utf8ToString(), equalTo("xorn the god jewel"));
        assertThat(corrections[3].join(space).utf8ToString(), equalTo("xorr the got jewel"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("<em>xorr</em> the <em>god</em> jewel"));
        assertThat(corrections[1].join(space, preTag, postTag).utf8ToString(), equalTo("xor the <em>god</em> jewel"));
        assertThat(corrections[2].join(space, preTag, postTag).utf8ToString(), equalTo("<em>xorn</em> the <em>god</em> jewel"));
        assertThat(corrections[3].join(space, preTag, postTag).utf8ToString(), equalTo("<em>xorr</em> the got jewel"));
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 4, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(space).utf8ToString(), equalTo("xor the god jewel"));
        assertThat(corrections[2].join(space).utf8ToString(), equalTo("xorn the god jewel"));
        assertThat(corrections[3].join(space).utf8ToString(), equalTo("xorr the got jewel"));
        
        // Test some of the highlighting corner cases
        suggester = new NoisyChannelSpellChecker(0.85);
        wordScorer = new LaplaceScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.5f);
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor teh Got-Jewel"), generator, 4f, 4, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(space).utf8ToString(), equalTo("xor the god jewel"));
        assertThat(corrections[2].join(space).utf8ToString(), equalTo("xorn the god jewel"));
        assertThat(corrections[3].join(space).utf8ToString(), equalTo("xor teh god jewel"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("<em>xorr the god</em> jewel"));
        assertThat(corrections[1].join(space, preTag, postTag).utf8ToString(), equalTo("xor <em>the god</em> jewel"));
        assertThat(corrections[2].join(space, preTag, postTag).utf8ToString(), equalTo("<em>xorn the god</em> jewel"));
        assertThat(corrections[3].join(space, preTag, postTag).utf8ToString(), equalTo("xor teh <em>god</em> jewel"));

        // test synonyms
        
        Analyzer analyzer = new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                TokenFilter filter = new LowerCaseFilter(Version.LUCENE_41, t);
                try {
                    SolrSynonymParser parser = new SolrSynonymParser(true, false, new WhitespaceAnalyzer(Version.LUCENE_41));
                    ((SolrSynonymParser) parser).parse(new StringReader("usa => usa, america, american\nursa => usa, america, american"));
                    filter = new SynonymFilter(filter, parser.build(), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return new TokenStreamComponents(t, filter);
            }
        };
        
        spellchecker.setAccuracy(0.0f);
        spellchecker.setMinPrefix(1);
        spellchecker.setMinQueryLength(1);
        suggester = new NoisyChannelSpellChecker(0.85);
        wordScorer = new LaplaceScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.5f);
        corrections = suggester.getCorrections(analyzer, new BytesRef("captian usa"), generator, 2, 4, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections[0].join(space).utf8ToString(), equalTo("captain america"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("<em>captain america</em>"));
        
        generator = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_MORE_POPULAR, ir, 0.85, 10, null, analyzer, MultiFields.getTerms(ir, "body"));
        corrections = suggester.getCorrections(analyzer, new BytesRef("captian usw"), generator, 2, 4, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("captain america"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("<em>captain america</em>"));

        // Make sure that user supplied text is not marked as highlighted in the presence of a synonym filter
        generator = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_MORE_POPULAR, ir, 0.85, 10, null, analyzer, MultiFields.getTerms(ir, "body"));
        corrections = suggester.getCorrections(analyzer, new BytesRef("captain usw"), generator, 2, 4, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("captain america"));
        assertThat(corrections[0].join(space, preTag, postTag).utf8ToString(), equalTo("captain <em>america</em>"));
    }
    
    @Test
    public void testMarvelHerosMultiGenerator() throws IOException {
        RAMDirectory dir = new RAMDirectory();
        Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
        mapping.put("body_ngram", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                ShingleFilter tf = new ShingleFilter(t, 2, 3);
                tf.setOutputUnigrams(false);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, tf));
            }

        });

        mapping.put("body", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, t));
            }

        });
        mapping.put("body_reverse", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                return new TokenStreamComponents(t, new ReverseStringFilter(Version.LUCENE_41, new LowerCaseFilter(Version.LUCENE_41, t)));
            }

        });
        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(Version.LUCENE_41), mapping);

        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_41, wrapper);
        IndexWriter writer = new IndexWriter(dir, conf);
        BufferedReader reader = new BufferedReader(new InputStreamReader(NoisyChannelSpellCheckerTests.class.getResourceAsStream("/config/names.txt"), Charsets.UTF_8));
        String line = null;
        while ((line = reader.readLine()) != null) {
            Document doc = new Document();
            doc.add(new Field("body", line, TextField.TYPE_NOT_STORED));
            doc.add(new Field("body_reverse", line, TextField.TYPE_NOT_STORED));
            doc.add(new Field("body_ngram", line, TextField.TYPE_NOT_STORED));
            writer.addDocument(doc);
        }

        DirectoryReader ir = DirectoryReader.open(writer, false);
        LaplaceScorer wordScorer = new LaplaceScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.95d, new BytesRef(" "), 0.5f);
        NoisyChannelSpellChecker suggester = new NoisyChannelSpellChecker();
        DirectSpellChecker spellchecker = new DirectSpellChecker();
        spellchecker.setMinQueryLength(1);
        DirectCandidateGenerator forward = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_ALWAYS, ir, 0.95, 10);
        DirectCandidateGenerator reverse = new DirectCandidateGenerator(spellchecker, "body_reverse", SuggestMode.SUGGEST_ALWAYS, ir, 0.95, 10, wrapper, wrapper,  MultiFields.getTerms(ir, "body_reverse"));
        CandidateGenerator generator = new MultiCandidateGeneratorWrapper(10, forward, reverse);
        
        Correction[] corrections = suggester.getCorrections(wrapper, new BytesRef("american cae"), generator, 1, 1, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("american ace"));
        
        generator = new MultiCandidateGeneratorWrapper(5, forward, reverse);
        corrections = suggester.getCorrections(wrapper, new BytesRef("american ame"), generator, 1, 1, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("american ace"));
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("american cae"), forward, 1, 1, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(0)); // only use forward with constant prefix
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("america cae"), generator, 2, 1, ir, "body", wordScorer, 1, 2).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("american ace"));
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("Zorr the Got-Jewel"), generator, 0.5f, 4, ir, "body", wordScorer, 0, 2).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(new BytesRef(" ")).utf8ToString(), equalTo("zorr the god jewel"));
        assertThat(corrections[2].join(new BytesRef(" ")).utf8ToString(), equalTo("gorr the god jewel"));
        assertThat(corrections[3].join(new BytesRef(" ")).utf8ToString(), equalTo("tarr the god jewel"));
        
        

        corrections = suggester.getCorrections(wrapper, new BytesRef("Zorr the Got-Jewel"), generator, 0.5f, 1, ir, "body", wordScorer, 1.5f, 2).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 1, ir, "body", wordScorer, 1.5f, 2).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));

    }
    
    @Test
    public void testMarvelHerosTrigram() throws IOException {
        
      
        RAMDirectory dir = new RAMDirectory();
        Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
        mapping.put("body_ngram", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                ShingleFilter tf = new ShingleFilter(t, 2, 3);
                tf.setOutputUnigrams(false);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, tf));
            }

        });

        mapping.put("body", new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_41, t));
            }

        });
        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(Version.LUCENE_41), mapping);

        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_41, wrapper);
        IndexWriter writer = new IndexWriter(dir, conf);
        BufferedReader reader = new BufferedReader(new InputStreamReader(NoisyChannelSpellCheckerTests.class.getResourceAsStream("/config/names.txt"), Charsets.UTF_8));
        String line = null;
        while ((line = reader.readLine()) != null) {
            Document doc = new Document();
            doc.add(new Field("body", line, TextField.TYPE_NOT_STORED));
            doc.add(new Field("body_ngram", line, TextField.TYPE_NOT_STORED));
            writer.addDocument(doc);
        }

        DirectoryReader ir = DirectoryReader.open(writer, false);
        WordScorer wordScorer = new LinearInterpoatingScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.5, 0.4, 0.1);

        NoisyChannelSpellChecker suggester = new NoisyChannelSpellChecker();
        DirectSpellChecker spellchecker = new DirectSpellChecker();
        spellchecker.setMinQueryLength(1);
        DirectCandidateGenerator generator = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_MORE_POPULAR, ir, 0.95, 5);
        Correction[] corrections = suggester.getCorrections(wrapper, new BytesRef("american ame"), generator, 1, 1, ir, "body", wordScorer, 1, 3).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("american ace"));
        
        corrections = suggester.getCorrections(wrapper, new BytesRef("american ame"), generator, 1, 1, ir, "body", wordScorer, 1, 1).corrections;
        assertThat(corrections.length, equalTo(0));
//        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("american ape"));
        
        wordScorer = new LinearInterpoatingScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.5, 0.4, 0.1);
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 4, ir, "body", wordScorer, 0, 3).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(new BytesRef(" ")).utf8ToString(), equalTo("xor the god jewel"));
        assertThat(corrections[2].join(new BytesRef(" ")).utf8ToString(), equalTo("xorn the god jewel"));
        assertThat(corrections[3].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the got jewel"));
        
      

        
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 4, ir, "body", wordScorer, 1, 3).corrections;
        assertThat(corrections.length, equalTo(4));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(new BytesRef(" ")).utf8ToString(), equalTo("xor the god jewel"));
        assertThat(corrections[2].join(new BytesRef(" ")).utf8ToString(), equalTo("xorn the god jewel"));
        assertThat(corrections[3].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the got jewel"));
        

        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 1, ir, "body", wordScorer, 100, 3).corrections;
        assertThat(corrections.length, equalTo(1));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        

        // test synonyms
        
        Analyzer analyzer = new Analyzer() {

            @Override
            protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                Tokenizer t = new StandardTokenizer(Version.LUCENE_41, reader);
                TokenFilter filter = new LowerCaseFilter(Version.LUCENE_41, t);
                try {
                    SolrSynonymParser parser = new SolrSynonymParser(true, false, new WhitespaceAnalyzer(Version.LUCENE_41));
                    ((SolrSynonymParser) parser).parse(new StringReader("usa => usa, america, american\nursa => usa, america, american"));
                    filter = new SynonymFilter(filter, parser.build(), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return new TokenStreamComponents(t, filter);
            }
        };
        
        spellchecker.setAccuracy(0.0f);
        spellchecker.setMinPrefix(1);
        spellchecker.setMinQueryLength(1);
        suggester = new NoisyChannelSpellChecker(0.95);
        wordScorer = new LinearInterpoatingScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.95d, new BytesRef(" "),  0.5, 0.4, 0.1);
        corrections = suggester.getCorrections(analyzer, new BytesRef("captian usa"), generator, 2, 4, ir, "body", wordScorer, 1, 3).corrections;
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("captain america"));
        
        generator = new DirectCandidateGenerator(spellchecker, "body", SuggestMode.SUGGEST_MORE_POPULAR, ir, 0.95, 10, null, analyzer, MultiFields.getTerms(ir, "body"));
        corrections = suggester.getCorrections(analyzer, new BytesRef("captian usw"), generator, 2, 4, ir, "body", wordScorer, 1, 3).corrections;
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("captain america"));
        
        
        wordScorer = new StupidBackoffScorer(ir, MultiFields.getTerms(ir, "body_ngram"), "body_ngram", 0.85d, new BytesRef(" "), 0.4);
        corrections = suggester.getCorrections(wrapper, new BytesRef("Xor the Got-Jewel"), generator, 0.5f, 2, ir, "body", wordScorer, 0, 3).corrections;
        assertThat(corrections.length, equalTo(2));
        assertThat(corrections[0].join(new BytesRef(" ")).utf8ToString(), equalTo("xorr the god jewel"));
        assertThat(corrections[1].join(new BytesRef(" ")).utf8ToString(), equalTo("xor the god jewel"));
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7562.java