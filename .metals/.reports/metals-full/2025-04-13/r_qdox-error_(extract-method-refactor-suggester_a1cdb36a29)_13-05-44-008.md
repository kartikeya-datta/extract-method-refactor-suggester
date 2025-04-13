error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4033.java
text:
```scala
t@@his.listener = listener == null ? DocumentMapper.ParseListener.EMPTY : listener;

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.elasticsearch.common.lucene.all.AllEntries;
import org.elasticsearch.common.util.concurrent.NotThreadSafe;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.mapper.DocumentMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kimchy (Shay Banon)
 */
@NotThreadSafe
public class ParseContext {

    private final XContentDocumentMapper docMapper;

    private final XContentDocumentMapperParser docMapperParser;

    private final ContentPath path;

    private XContentParser parser;

    private Document document;

    private Analyzer analyzer;

    private String index;

    private String type;

    private byte[] source;

    private String id;

    private DocumentMapper.ParseListener listener;

    private String uid;

    private StringBuilder stringBuilder = new StringBuilder();

    private Map<String, String> ignoredValues = new HashMap<String, String>();

    private ParsedIdState parsedIdState;

    private boolean mappersAdded = false;

    private boolean externalValueSet;

    private Object externalValue;

    private AllEntries allEntries = new AllEntries();

    public ParseContext(String index, XContentDocumentMapperParser docMapperParser, XContentDocumentMapper docMapper, ContentPath path) {
        this.index = index;
        this.docMapper = docMapper;
        this.docMapperParser = docMapperParser;
        this.path = path;
    }

    public void reset(XContentParser parser, Document document, String type, byte[] source, DocumentMapper.ParseListener listener) {
        this.parser = parser;
        this.document = document;
        this.analyzer = null;
        this.type = type;
        this.source = source;
        this.path.reset();
        this.parsedIdState = ParsedIdState.NO;
        this.mappersAdded = false;
        this.listener = listener;
        this.allEntries = new AllEntries();
        this.ignoredValues.clear();
    }

    public XContentDocumentMapperParser docMapperParser() {
        return this.docMapperParser;
    }

    public boolean mappersAdded() {
        return this.mappersAdded;
    }

    public void addedMapper() {
        this.mappersAdded = true;
    }

    public String index() {
        return this.index;
    }

    public String type() {
        return this.type;
    }

    public byte[] source() {
        return this.source;
    }

    public ContentPath path() {
        return this.path;
    }

    public XContentParser parser() {
        return this.parser;
    }

    public DocumentMapper.ParseListener listener() {
        return this.listener;
    }

    public Document doc() {
        return this.document;
    }

    public RootObjectMapper root() {
        return docMapper.root();
    }

    public XContentDocumentMapper docMapper() {
        return this.docMapper;
    }

    public AnalysisService analysisService() {
        return docMapperParser.analysisService;
    }

    public String id() {
        return id;
    }

    public void parsedId(ParsedIdState parsedIdState) {
        this.parsedIdState = parsedIdState;
    }

    public ParsedIdState parsedIdState() {
        return this.parsedIdState;
    }

    public void ignoredValue(String indexName, String value) {
        ignoredValues.put(indexName, value);
    }

    public String ignoredValue(String indexName) {
        return ignoredValues.get(indexName);
    }

    /**
     * Really, just the id mapper should set this.
     */
    public void id(String id) {
        this.id = id;
    }

    public String uid() {
        return this.uid;
    }

    /**
     * Really, just the uid mapper should set this.
     */
    public void uid(String uid) {
        this.uid = uid;
    }

    public AllEntries allEntries() {
        return this.allEntries;
    }

    public Analyzer analyzer() {
        return this.analyzer;
    }

    public void analyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void externalValue(Object externalValue) {
        this.externalValueSet = true;
        this.externalValue = externalValue;
    }

    public boolean externalValueSet() {
        return this.externalValueSet;
    }

    public Object externalValue() {
        externalValueSet = false;
        return externalValue;
    }

    /**
     * A string builder that can be used to construct complex names for example.
     * Its better to reuse the.
     */
    public StringBuilder stringBuilder() {
        stringBuilder.setLength(0);
        return this.stringBuilder;
    }

    public static enum ParsedIdState {
        NO,
        PARSED,
        EXTERNAL
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4033.java