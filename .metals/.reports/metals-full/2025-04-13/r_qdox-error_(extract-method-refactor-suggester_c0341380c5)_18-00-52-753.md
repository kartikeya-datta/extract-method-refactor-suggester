error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4327.java
text:
```scala
X@@ContentDocumentMapper builderDocMapper = doc("test", object("person").add(

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

package org.elasticsearch.index.mapper.xcontent.multifield;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.elasticsearch.index.mapper.xcontent.XContentDocumentMapper;
import org.elasticsearch.index.mapper.xcontent.XContentMapperTests;
import org.testng.annotations.Test;

import static org.elasticsearch.common.io.Streams.*;
import static org.elasticsearch.index.mapper.xcontent.XContentMapperBuilders.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
@Test
public class XContentMultiFieldTests {

    @Test public void testMultiField() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/xcontent/multifield/test-mapping.json");
        XContentDocumentMapper docMapper = XContentMapperTests.newParser().parse(mapping);
        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/xcontent/multifield/test-data.json");
        Document doc = docMapper.parse(json).doc();

        Field f = doc.getField("name");
        assertThat(f.name(), equalTo("name"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(true));
        assertThat(f.isIndexed(), equalTo(true));

        f = doc.getField("name.indexed");
        assertThat(f.name(), equalTo("name.indexed"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(false));
        assertThat(f.isIndexed(), equalTo(true));

        f = doc.getField("name.not_indexed");
        assertThat(f.name(), equalTo("name.not_indexed"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(true));
        assertThat(f.isIndexed(), equalTo(false));

        f = doc.getField("object1.multi1");
        assertThat(f.name(), equalTo("object1.multi1"));

        f = doc.getField("object1.multi1.string");
        assertThat(f.name(), equalTo("object1.multi1.string"));
        assertThat(f.stringValue(), equalTo("2010-01-01"));
    }

    @Test public void testBuildThenParse() throws Exception {
        XContentDocumentMapper builderDocMapper = doc(object("person").add(
                multiField("name")
                        .add(stringField("name").store(Field.Store.YES))
                        .add(stringField("indexed").index(Field.Index.ANALYZED))
                        .add(stringField("not_indexed").index(Field.Index.NO).store(Field.Store.YES))
        )).build();

        String builtMapping = builderDocMapper.buildSource();
//        System.out.println(builtMapping);
        // reparse it
        XContentDocumentMapper docMapper = XContentMapperTests.newParser().parse(builtMapping);


        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/xcontent/multifield/test-data.json");
        Document doc = docMapper.parse(json).doc();

        Field f = doc.getField("name");
        assertThat(f.name(), equalTo("name"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(true));
        assertThat(f.isIndexed(), equalTo(true));

        f = doc.getField("name.indexed");
        assertThat(f.name(), equalTo("name.indexed"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(false));
        assertThat(f.isIndexed(), equalTo(true));

        f = doc.getField("name.not_indexed");
        assertThat(f.name(), equalTo("name.not_indexed"));
        assertThat(f.stringValue(), equalTo("some name"));
        assertThat(f.isStored(), equalTo(true));
        assertThat(f.isIndexed(), equalTo(false));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4327.java