error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9299.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9299.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9299.java
text:
```scala
T@@ermVectorResponse response = request.get();

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.termvectors;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.termvector.TermVectorRequest;
import org.elasticsearch.action.termvector.TermVectorRequestBuilder;
import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.mapper.core.AbstractFieldMapper;
import org.elasticsearch.test.hamcrest.ElasticsearchAssertions;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertThrows;
import static org.hamcrest.Matchers.equalTo;

public class GetTermVectorTests extends AbstractTermVectorTests {



    @Test
    public void testNoSuchDoc() throws Exception {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties")
                        .startObject("field")
                            .field("type", "string")
                            .field("term_vector", "with_positions_offsets_payloads")
                        .endObject()
                .endObject()
                .endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping));

        ensureYellow();

        client().prepareIndex("test", "type1", "666").setSource("field", "foo bar").execute().actionGet();
        refresh();
        for (int i = 0; i < 20; i++) {
            ActionFuture<TermVectorResponse> termVector = client().termVector(new TermVectorRequest("test", "type1", "" + i));
            TermVectorResponse actionGet = termVector.actionGet();
            assertThat(actionGet, Matchers.notNullValue());
            assertThat(actionGet.isExists(), Matchers.equalTo(false));

        }

    }
    
    @Test
    public void testExistingFieldWithNoTermVectorsNoNPE() throws Exception {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties")
                        .startObject("existingfield")
                            .field("type", "string")
                            .field("term_vector", "with_positions_offsets_payloads")
                        .endObject()
                .endObject()
                .endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping));

        ensureYellow();
        // when indexing a field that simply has a question mark, the term
        // vectors will be null
        client().prepareIndex("test", "type1", "0").setSource("existingfield", "?").execute().actionGet();
        refresh();
        String[] selectedFields = { "existingfield" };
        ActionFuture<TermVectorResponse> termVector = client().termVector(
                new TermVectorRequest("test", "type1", "0").selectedFields(selectedFields));
        // lets see if the null term vectors are caught...
        termVector.actionGet();
        TermVectorResponse actionGet = termVector.actionGet();
        assertThat(actionGet.isExists(), Matchers.equalTo(true));

    }
    
    @Test
    public void testExistingFieldButNotInDocNPE() throws Exception {

        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties")
                        .startObject("existingfield")
                            .field("type", "string")
                            .field("term_vector", "with_positions_offsets_payloads")
                        .endObject()
                .endObject()
                .endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping));
        ensureYellow();
        // when indexing a field that simply has a question mark, the term
        // vectors will be null
        client().prepareIndex("test", "type1", "0").setSource("anotherexistingfield", 1).execute().actionGet();
        refresh();
        String[] selectedFields = { "existingfield" };
        ActionFuture<TermVectorResponse> termVector = client().termVector(
                new TermVectorRequest("test", "type1", "0").selectedFields(selectedFields));
        // lets see if the null term vectors are caught...
        TermVectorResponse actionGet = termVector.actionGet();
        assertThat(actionGet.isExists(), Matchers.equalTo(true));

    }
    


    @Test
    public void testSimpleTermVectors() throws ElasticSearchException, IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties")
                        .startObject("field")
                            .field("type", "string")
                            .field("term_vector", "with_positions_offsets_payloads")
                            .field("analyzer", "tv_test")
                        .endObject()
                .endObject()
                .endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping)
                .setSettings(ImmutableSettings.settingsBuilder()
                        .put("index.analysis.analyzer.tv_test.tokenizer", "whitespace")
                        .putArray("index.analysis.analyzer.tv_test.filter", "type_as_payload", "lowercase")));
        ensureYellow();
        for (int i = 0; i < 10; i++) {
            client().prepareIndex("test", "type1", Integer.toString(i))
                    .setSource(XContentFactory.jsonBuilder().startObject().field("field", "the quick brown fox jumps over the lazy dog")
                            // 0the3 4quick9 10brown15 16fox19 20jumps25 26over30
                            // 31the34 35lazy39 40dog43
                            .endObject()).execute().actionGet();
            refresh();
        }
        String[] values = {"brown", "dog", "fox", "jumps", "lazy", "over", "quick", "the"};
        int[] freq = {1, 1, 1, 1, 1, 1, 1, 2};
        int[][] pos = {{2}, {8}, {3}, {4}, {7}, {5}, {1}, {0, 6}};
        int[][] startOffset = {{10}, {40}, {16}, {20}, {35}, {26}, {4}, {0, 31}};
        int[][] endOffset = {{15}, {43}, {19}, {25}, {39}, {30}, {9}, {3, 34}};
        for (int i = 0; i < 10; i++) {
            TermVectorRequestBuilder resp = client().prepareTermVector("test", "type1", Integer.toString(i)).setPayloads(true)
                    .setOffsets(true).setPositions(true).setSelectedFields();
            TermVectorResponse response = resp.execute().actionGet();
            assertThat("doc id: " + i + " doesn't exists but should", response.isExists(), equalTo(true));
            Fields fields = response.getFields();
            assertThat(fields.size(), equalTo(1));
            Terms terms = fields.terms("field");
            assertThat(terms.size(), equalTo(8l));
            TermsEnum iterator = terms.iterator(null);
            for (int j = 0; j < values.length; j++) {
                String string = values[j];
                BytesRef next = iterator.next();
                assertThat(next, Matchers.notNullValue());
                assertThat("expected " + string, string, equalTo(next.utf8ToString()));
                assertThat(next, Matchers.notNullValue());
                // do not test ttf or doc frequency, because here we have many
                // shards and do not know how documents are distributed
                DocsAndPositionsEnum docsAndPositions = iterator.docsAndPositions(null, null);
                assertThat(docsAndPositions.nextDoc(), equalTo(0));
                assertThat(freq[j], equalTo(docsAndPositions.freq()));
                int[] termPos = pos[j];
                int[] termStartOffset = startOffset[j];
                int[] termEndOffset = endOffset[j];
                assertThat(termPos.length, equalTo(freq[j]));
                assertThat(termStartOffset.length, equalTo(freq[j]));
                assertThat(termEndOffset.length, equalTo(freq[j]));
                for (int k = 0; k < freq[j]; k++) {
                    int nextPosition = docsAndPositions.nextPosition();
                    assertThat("term: " + string, nextPosition, equalTo(termPos[k]));
                    assertThat("term: " + string, docsAndPositions.startOffset(), equalTo(termStartOffset[k]));
                    assertThat("term: " + string, docsAndPositions.endOffset(), equalTo(termEndOffset[k]));
                    assertThat("term: " + string, docsAndPositions.getPayload(), equalTo(new BytesRef("word")));
                }
            }
            assertThat(iterator.next(), Matchers.nullValue());
        }
    }

    @Test
    public void testRandomSingleTermVectors() throws ElasticSearchException, IOException {
        FieldType ft = new FieldType();
        int config = randomInt(6);
        boolean storePositions = false;
        boolean storeOffsets = false;
        boolean storePayloads = false;
        boolean storeTermVectors = false;
        switch (config) {
            case 0: {
                // do nothing
            }
            case 1: {
                storeTermVectors = true;
            }
            case 2: {
                storeTermVectors = true;
                storePositions = true;
            }
            case 3: {
                storeTermVectors = true;
                storeOffsets = true;
            }
            case 4: {
                storeTermVectors = true;
                storePositions = true;
                storeOffsets = true;
            }
            case 5: {
                storeTermVectors = true;
                storePositions = true;
                storePayloads = true;
            }
            case 6: {
                storeTermVectors = true;
                storePositions = true;
                storeOffsets = true;
                storePayloads = true;
            }
        }
        ft.setStoreTermVectors(storeTermVectors);
        ft.setStoreTermVectorOffsets(storeOffsets);
        ft.setStoreTermVectorPayloads(storePayloads);
        ft.setStoreTermVectorPositions(storePositions);

        String optionString = AbstractFieldMapper.termVectorOptionsToString(ft);
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties")
                        .startObject("field")
                            .field("type", "string")
                            .field("term_vector", optionString)
                            .field("analyzer", "tv_test")
                        .endObject()
                .endObject()
                .endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping)
                .setSettings(ImmutableSettings.settingsBuilder()
                        .put("index.analysis.analyzer.tv_test.tokenizer", "whitespace")
                        .putArray("index.analysis.analyzer.tv_test.filter", "type_as_payload", "lowercase")));
        ensureYellow();
        for (int i = 0; i < 10; i++) {
            client().prepareIndex("test", "type1", Integer.toString(i))
                    .setSource(XContentFactory.jsonBuilder().startObject().field("field", "the quick brown fox jumps over the lazy dog")
                            // 0the3 4quick9 10brown15 16fox19 20jumps25 26over30
                            // 31the34 35lazy39 40dog43
                            .endObject()).execute().actionGet();
            refresh();
        }
        String[] values = {"brown", "dog", "fox", "jumps", "lazy", "over", "quick", "the"};
        int[] freq = {1, 1, 1, 1, 1, 1, 1, 2};
        int[][] pos = {{2}, {8}, {3}, {4}, {7}, {5}, {1}, {0, 6}};
        int[][] startOffset = {{10}, {40}, {16}, {20}, {35}, {26}, {4}, {0, 31}};
        int[][] endOffset = {{15}, {43}, {19}, {25}, {39}, {30}, {9}, {3, 34}};

        boolean isPayloadRequested = randomBoolean();
        boolean isOffsetRequested = randomBoolean();
        boolean isPositionsRequested = randomBoolean();
        String infoString = createInfoString(isPositionsRequested, isOffsetRequested, isPayloadRequested, optionString);
        for (int i = 0; i < 10; i++) {
            TermVectorRequestBuilder resp = client().prepareTermVector("test", "type1", Integer.toString(i))
                    .setPayloads(isPayloadRequested).setOffsets(isOffsetRequested).setPositions(isPositionsRequested).setSelectedFields();
            TermVectorResponse response = resp.execute().actionGet();
            assertThat(infoString + "doc id: " + i + " doesn't exists but should", response.isExists(), equalTo(true));
            Fields fields = response.getFields();
            assertThat(fields.size(), equalTo(ft.storeTermVectors() ? 1 : 0));
            if (ft.storeTermVectors()) {
                Terms terms = fields.terms("field");
                assertThat(terms.size(), equalTo(8l));
                TermsEnum iterator = terms.iterator(null);
                for (int j = 0; j < values.length; j++) {
                    String string = values[j];
                    BytesRef next = iterator.next();
                    assertThat(infoString, next, Matchers.notNullValue());
                    assertThat(infoString + "expected " + string, string, equalTo(next.utf8ToString()));
                    assertThat(infoString, next, Matchers.notNullValue());
                    // do not test ttf or doc frequency, because here we have
                    // many shards and do not know how documents are distributed
                    DocsAndPositionsEnum docsAndPositions = iterator.docsAndPositions(null, null);
                    // docs and pos only returns something if positions or
                    // payloads or offsets are stored / requestd Otherwise use
                    // DocsEnum?
                    assertThat(infoString, docsAndPositions.nextDoc(), equalTo(0));
                    assertThat(infoString, freq[j], equalTo(docsAndPositions.freq()));
                    int[] termPos = pos[j];
                    int[] termStartOffset = startOffset[j];
                    int[] termEndOffset = endOffset[j];
                    if (isPositionsRequested && storePositions) {
                        assertThat(infoString, termPos.length, equalTo(freq[j]));
                    }
                    if (isOffsetRequested && storeOffsets) {
                        assertThat(termStartOffset.length, equalTo(freq[j]));
                        assertThat(termEndOffset.length, equalTo(freq[j]));
                    }
                    for (int k = 0; k < freq[j]; k++) {
                        int nextPosition = docsAndPositions.nextPosition();
                        // only return something useful if requested and stored
                        if (isPositionsRequested && storePositions) {
                            assertThat(infoString + "positions for term: " + string, nextPosition, equalTo(termPos[k]));
                        } else {
                            assertThat(infoString + "positions for term: ", nextPosition, equalTo(-1));
                        }

                        // only return something useful if requested and stored
                        if (isPayloadRequested && storePayloads) {
                            assertThat(infoString + "payloads for term: " + string, docsAndPositions.getPayload(), equalTo(new BytesRef(
                                    "word")));
                        } else {
                            assertThat(infoString + "payloads for term: " + string, docsAndPositions.getPayload(), equalTo(null));
                        }
                        // only return something useful if requested and stored
                        if (isOffsetRequested && storeOffsets) {

                            assertThat(infoString + "startOffsets term: " + string, docsAndPositions.startOffset(),
                                    equalTo(termStartOffset[k]));
                            assertThat(infoString + "endOffsets term: " + string, docsAndPositions.endOffset(), equalTo(termEndOffset[k]));
                        } else {
                            assertThat(infoString + "startOffsets term: " + string, docsAndPositions.startOffset(), equalTo(-1));
                            assertThat(infoString + "endOffsets term: " + string, docsAndPositions.endOffset(), equalTo(-1));
                        }

                    }
                }
                assertThat(iterator.next(), Matchers.nullValue());
            }

        }
    }

    private String createInfoString(boolean isPositionsRequested, boolean isOffsetRequested, boolean isPayloadRequested,
                                    String optionString) {
        String ret = "Store config: " + optionString + "\n" + "Requested: pos-"
                + (isPositionsRequested ? "yes" : "no") + ", offsets-" + (isOffsetRequested ? "yes" : "no") + ", payload- "
                + (isPayloadRequested ? "yes" : "no") + "\n";
        return ret;
    }

    @Test
    public void testDuelESLucene() throws Exception {
        TestFieldSetting[] testFieldSettings = getFieldSettings();
        createIndexBasedOnFieldSettings(testFieldSettings, -1);
        TestDoc[] testDocs = generateTestDocs(5, testFieldSettings);

        DirectoryReader directoryReader = indexDocsWithLucene(testDocs);
        TestConfig[] testConfigs = generateTestConfigs(20, testDocs, testFieldSettings);

        for (TestConfig test : testConfigs) {
            try {
                TermVectorRequestBuilder request = getRequestForConfig(test);
                if (test.expectedException != null) {
                    assertThrows(request, test.expectedException);
                    continue;
                }

                TermVectorResponse response = run(request);
                Fields luceneTermVectors = getTermVectorsFromLucene(directoryReader, test.doc);
                validateResponse(response, luceneTermVectors, test);
            } catch (Throwable t) {
                throw new Exception("Test exception while running " + test.toString(), t);
            }
        }
    }

    @Test
    public void testRandomPayloadWithDelimitedPayloadTokenFilter() throws ElasticSearchException, IOException {
        
        //create the test document
        int encoding = randomIntBetween(0, 2);
        String encodingString = "";
        if (encoding == 0) {
            encodingString = "float";
        }
        if (encoding == 1) {
            encodingString = "int";
        }
        if (encoding == 2) {
            encodingString = "identity";
        }
        String[] tokens = crateRandomTokens();
        Map<String, List<BytesRef>> payloads = createPayloads(tokens, encoding);
        String delimiter = createRandomDelimiter(tokens);
        String queryString = createString(tokens, payloads, encoding, delimiter.charAt(0));
        //create the mapping
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                .startObject("field").field("type", "string").field("term_vector", "with_positions_offsets_payloads")
                .field("analyzer", "payload_test").endObject().endObject().endObject().endObject();
        ElasticsearchAssertions.assertAcked(prepareCreate("test").addMapping("type1", mapping).setSettings(
                ImmutableSettings.settingsBuilder().put("index.analysis.analyzer.payload_test.tokenizer", "whitespace")
                        .putArray("index.analysis.analyzer.payload_test.filter", "my_delimited_payload_filter")
                        .put("index.analysis.filter.my_delimited_payload_filter.delimiter", delimiter)
                        .put("index.analysis.filter.my_delimited_payload_filter.encoding", encodingString)
                        .put("index.analysis.filter.my_delimited_payload_filter.type", "delimited_payload_filter")));
        ensureYellow();

        client().prepareIndex("test", "type1", Integer.toString(1))
                .setSource(XContentFactory.jsonBuilder().startObject().field("field", queryString).endObject()).execute().actionGet();
        refresh();
        TermVectorRequestBuilder resp = client().prepareTermVector("test", "type1", Integer.toString(1)).setPayloads(true).setOffsets(true)
                .setPositions(true).setSelectedFields();
        TermVectorResponse response = resp.execute().actionGet();
        assertThat("doc id 1 doesn't exists but should", response.isExists(), equalTo(true));
        Fields fields = response.getFields();
        assertThat(fields.size(), equalTo(1));
        Terms terms = fields.terms("field");
        TermsEnum iterator = terms.iterator(null);
        while (iterator.next() != null) {
            String term = iterator.term().utf8ToString();
            DocsAndPositionsEnum docsAndPositions = iterator.docsAndPositions(null, null);
            assertThat(docsAndPositions.nextDoc(), equalTo(0));
            List<BytesRef> curPayloads = payloads.get(term);
            assertThat(term, curPayloads, Matchers.notNullValue());
            assert docsAndPositions != null;
            for (int k = 0; k < docsAndPositions.freq(); k++) {
                docsAndPositions.nextPosition();
                if (docsAndPositions.getPayload()!=null){
                    String infoString = "\nterm: " + term + " has payload \n"+ docsAndPositions.getPayload().toString() + "\n but should have payload \n"+curPayloads.get(k).toString();
                    assertThat(infoString, docsAndPositions.getPayload(), equalTo(curPayloads.get(k)));
                } else {
                    String infoString = "\nterm: " + term + " has no payload but should have payload \n"+curPayloads.get(k).toString();
                    assertThat(infoString, curPayloads.get(k).length, equalTo(0));
                }
            }
        }
        assertThat(iterator.next(), Matchers.nullValue());
    }
    private String createRandomDelimiter(String[] tokens) {
        String delimiter = "";
        boolean isTokenOrWhitespace = true;
        while(isTokenOrWhitespace) {
            isTokenOrWhitespace = false;
            delimiter = randomUnicodeOfLength(1);
            for(String token:tokens) {
                if(token.contains(delimiter)) {
                    isTokenOrWhitespace = true;
                }
            }
            if(Character.isWhitespace(delimiter.charAt(0))) {
                isTokenOrWhitespace = true;
            }
        }
        return delimiter;
    }
    private String createString(String[] tokens, Map<String, List<BytesRef>> payloads, int encoding, char delimiter) {
        String resultString = "";
        ObjectIntOpenHashMap<String> payloadCounter = new ObjectIntOpenHashMap<String>();
        for (String token : tokens) {
            if (!payloadCounter.containsKey(token)) {
                payloadCounter.putIfAbsent(token, 0);
            } else {
                payloadCounter.put(token, payloadCounter.get(token) + 1);
            }
            resultString = resultString + token;
            BytesRef payload = payloads.get(token).get(payloadCounter.get(token));
            if (payload.length > 0) {
                resultString = resultString + delimiter;
                switch (encoding) {
                case 0: {
                    resultString = resultString + Float.toString(PayloadHelper.decodeFloat(payload.bytes, payload.offset));
                    break;
                }
                case 1: {
                    resultString = resultString + Integer.toString(PayloadHelper.decodeInt(payload.bytes, payload.offset));
                    break;
                }
                case 2: {
                    resultString = resultString + payload.utf8ToString();
                    break;
                }
                default: {
                    throw new ElasticSearchException("unsupported encoding type");
                }
                }
            }
            resultString = resultString + " ";
        }
        return resultString;
    }

    private Map<String, List<BytesRef>> createPayloads(String[] tokens, int encoding) {
        Map<String, List<BytesRef>> payloads = new HashMap<String, List<BytesRef>>();
        for (String token : tokens) {
            if (payloads.get(token) == null) {
                payloads.put(token, new ArrayList<BytesRef>());
            }
            boolean createPayload = randomBoolean();
            if (createPayload) {
                switch (encoding) {
                case 0: {
                    float theFloat = randomFloat();
                    payloads.get(token).add(new BytesRef(PayloadHelper.encodeFloat(theFloat)));
                    break;
                }
                case 1: {
                    payloads.get(token).add(new BytesRef(PayloadHelper.encodeInt(randomInt())));
                    break;
                }
                case 2: {
                    String payload = randomUnicodeOfLengthBetween(50, 100);
                    for (int c = 0; c < payload.length(); c++) {
                        if (Character.isWhitespace(payload.charAt(c))) {
                            payload = payload.replace(payload.charAt(c), 'w');
                        }
                    }
                    payloads.get(token).add(new BytesRef(payload));
                    break;
                }
                default: {
                    throw new ElasticSearchException("unsupported encoding type");
                }
                }
            } else {
                payloads.get(token).add(new BytesRef());
            }
        }
        return payloads;
    }

    private String[] crateRandomTokens() {
        String[] tokens = { "the", "quick", "brown", "fox" };
        int numTokensWithDuplicates = randomIntBetween(3, 15);
        String[] finalTokens = new String[numTokensWithDuplicates];
        for (int i = 0; i < numTokensWithDuplicates; i++) {
            finalTokens[i] = tokens[randomIntBetween(0, tokens.length - 1)];
        }
        return finalTokens;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9299.java