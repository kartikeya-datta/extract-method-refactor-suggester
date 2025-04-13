error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2711.java
text:
```scala
U@@nicode.UTF16Result result = Unicode.unsafeFromBytesAsUtf16(json);

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

package org.elasticsearch.util.json;

import org.apache.lucene.util.UnicodeUtil;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.util.Unicode;
import org.elasticsearch.util.concurrent.NotThreadSafe;
import org.elasticsearch.util.io.FastCharArrayWriter;

import java.io.IOException;

/**
 * @author kimchy (Shay Banon)
 */
@NotThreadSafe
public class StringJsonBuilder extends JsonBuilder<StringJsonBuilder> {

    /**
     * A thread local based cache of {@link StringJsonBuilder}.
     */
    public static class Cached {

        private StringJsonBuilder builder;

        public Cached(StringJsonBuilder builder) {
            this.builder = builder;
        }

        private static final ThreadLocal<Cached> cache = new ThreadLocal<Cached>() {
            @Override protected Cached initialValue() {
                try {
                    return new Cached(new StringJsonBuilder());
                } catch (IOException e) {
                    throw new ElasticSearchException("Failed to create json generator", e);
                }
            }
        };

        /**
         * Returns the cached thread local generator, with its internal {@link StringBuilder} cleared.
         */
        static StringJsonBuilder cached() throws IOException {
            Cached cached = cache.get();
            cached.builder.reset();
            return cached.builder;
        }
    }

    private final FastCharArrayWriter writer;

    private final JsonFactory factory;

    final UnicodeUtil.UTF8Result utf8Result = new UnicodeUtil.UTF8Result();

    public StringJsonBuilder() throws IOException {
        this(Jackson.defaultJsonFactory());
    }

    public StringJsonBuilder(JsonFactory factory) throws IOException {
        this.writer = new FastCharArrayWriter();
        this.factory = factory;
        this.generator = factory.createJsonGenerator(writer);
        this.builder = this;
    }

    public StringJsonBuilder(JsonGenerator generator) throws IOException {
        this.writer = new FastCharArrayWriter();
        this.generator = generator;
        this.factory = null;
        this.builder = this;
    }

    @Override public StringJsonBuilder raw(byte[] json) throws IOException {
        flush();
        UnicodeUtil.UTF16Result result = Unicode.unsafeFromBytesAsUtf16(json);
        writer.write(result.result, 0, result.length);
        return this;
    }

    public StringJsonBuilder reset() throws IOException {
        writer.reset();
        generator = factory.createJsonGenerator(writer);
        return this;
    }

    public String string() throws IOException {
        flush();
        return writer.toStringTrim();
    }

    public FastCharArrayWriter unsafeChars() throws IOException {
        flush();
        return writer;
    }

    @Override public byte[] unsafeBytes() throws IOException {
        return utf8().result;
    }

    /**
     * Call this AFTER {@link #unsafeBytes()}.
     */
    @Override public int unsafeBytesLength() {
        return utf8Result.length;
    }

    @Override public byte[] copiedBytes() throws IOException {
        flush();
        byte[] ret = new byte[utf8Result.length];
        System.arraycopy(utf8Result.result, 0, ret, 0, ret.length);
        return ret;
    }

    /**
     * Returns the byte[] that represents the utf8 of the json written up until now.
     * Note, the result is shared within this instance, so copy the byte array if needed
     * or use {@link #utf8copied()}.
     */
    public UnicodeUtil.UTF8Result utf8() throws IOException {
        flush();

        // ignore whitepsaces
        int st = 0;
        int len = writer.size();
        char[] val = writer.unsafeCharArray();

        while ((st < len) && (val[st] <= ' ')) {
            st++;
            len--;
        }
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }

        UnicodeUtil.UTF16toUTF8(val, st, len, utf8Result);

        return utf8Result;
    }

    /**
     * Returns a copied byte[] that represnts the utf8 o fthe json written up until now.
     */
    public byte[] utf8copied() throws IOException {
        utf8();
        byte[] result = new byte[utf8Result.length];
        System.arraycopy(utf8Result.result, 0, result, 0, utf8Result.length);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2711.java