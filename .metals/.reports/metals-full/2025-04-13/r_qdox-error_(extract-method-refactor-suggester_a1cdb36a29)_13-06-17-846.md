error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9590.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9590.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9590.java
text:
```scala
x@@cb.field("_language", forcedLanguage[0]);

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

package org.elasticsearch.index.mapper.xcontent;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.DocumentMapperParser;
import org.elasticsearch.index.mapper.ParseContext;
import org.elasticsearch.index.mapper.attachment.AttachmentMapper;
import org.elasticsearch.index.mapper.core.StringFieldMapper;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.common.io.Streams.copyToBytesFromClasspath;
import static org.elasticsearch.common.io.Streams.copyToStringFromClasspath;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

/**
 *
 */
public class LanguageDetectionAttachmentMapperTests extends ElasticsearchTestCase {

    private DocumentMapper docMapper;

    @Before
    public void setupMapperParser() throws IOException {
        setupMapperParser(true);
    }

    public void setupMapperParser(boolean langDetect) throws IOException {
        DocumentMapperParser mapperParser = new DocumentMapperParser(new Index("test"),
                ImmutableSettings.settingsBuilder().put("index.mapping.attachment.detect_language", langDetect).build(),
                new AnalysisService(new Index("test")), null, null, null);
        mapperParser.putTypeParser(AttachmentMapper.CONTENT_TYPE, new AttachmentMapper.TypeParser());
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/language/language-mapping.json");
        docMapper = mapperParser.parse(mapping);

        assertThat(docMapper.mappers().fullName("file.language").mapper(), instanceOf(StringFieldMapper.class));
    }

    private void testLanguage(String filename, String expected, String... forcedLanguage) throws IOException {
        byte[] html = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/xcontent/" + filename);

        XContentBuilder xcb = jsonBuilder()
                .startObject()
                    .field("_id", 1)
                    .startObject("file")
                        .field("_name", filename)
                        .field("content", html);

        if (forcedLanguage.length > 0) {
            xcb.field("language", forcedLanguage[0]);
        }

        xcb.endObject().endObject();

        ParseContext.Document doc =  docMapper.parse(xcb.bytes()).rootDoc();

        // Our mapping should be kept as a String
        assertThat(doc.get(docMapper.mappers().smartName("file.language").mapper().names().indexName()), equalTo(expected));
    }

    @Test
    public void testFrDetection() throws Exception {
        testLanguage("text-in-french.txt", "fr");
    }

    @Test
    public void testEnDetection() throws Exception {
        testLanguage("text-in-english.txt", "en");
    }

    @Test
    public void testFrForced() throws Exception {
        testLanguage("text-in-english.txt", "fr", "fr");
    }

    /**
     * This test gives strange results! detection of ":-)" gives "lt" as a result
     * @throws Exception
     */
    @Test
    public void testNoLanguage() throws Exception {
        testLanguage("text-in-nolang.txt", "lt");
    }

    @Test
    public void testLangDetectDisabled() throws Exception {
        // We replace the mapper with another one which have index.mapping.attachment.detect_language = false
        setupMapperParser(false);
        testLanguage("text-in-english.txt", null);
    }

    @Test
    public void testLangDetectDocumentEnabled() throws Exception {
        // We replace the mapper with another one which have index.mapping.attachment.detect_language = false
        setupMapperParser(false);

        byte[] html = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/xcontent/text-in-english.txt");

        XContentBuilder xcb = jsonBuilder()
                .startObject()
                .field("_id", 1)
                .startObject("file")
                    .field("_name", "text-in-english.txt")
                    .field("content", html)
                    .field("_detect_language", true)
                .endObject().endObject();

        ParseContext.Document doc =  docMapper.parse(xcb.bytes()).rootDoc();

        // Our mapping should be kept as a String
        assertThat(doc.get(docMapper.mappers().smartName("file.language").mapper().names().indexName()), equalTo("en"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9590.java