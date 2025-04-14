error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4603.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4603.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4603.java
text:
```scala
F@@astCharArrayWriter writer = new FastCharArrayWriter();

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

package org.elasticsearch.common.lucene.all;

import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.io.FastCharArrayWriter;
import org.elasticsearch.common.io.FastStringReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.common.collect.Sets.*;

/**
 * @author kimchy (shay.banon)
 */
public class AllEntries extends Reader {

    public static class Entry {
        private final String name;
        private final FastStringReader reader;
        private final float boost;

        public Entry(String name, FastStringReader reader, float boost) {
            this.name = name;
            this.reader = reader;
            this.boost = boost;
        }

        public String name() {
            return this.name;
        }

        public float boost() {
            return this.boost;
        }

        public FastStringReader reader() {
            return this.reader;
        }
    }

    private final List<Entry> entries = Lists.newArrayList();

    private Entry current;

    private Iterator<Entry> it;

    private boolean itsSeparatorTime = false;

    private boolean customBoost = false;

    public void addText(String name, String text, float boost) {
        if (boost != 1.0f) {
            customBoost = true;
        }
        Entry entry = new Entry(name, new FastStringReader(text), boost);
        entries.add(entry);
    }

    public void clear() {
        this.entries.clear();
        this.current = null;
        this.it = null;
        itsSeparatorTime = false;
    }

    public void reset() {
        try {
            for (Entry entry : entries) {
                entry.reader().reset();
            }
        } catch (IOException e) {
            throw new ElasticSearchIllegalStateException("should not happen");
        }
        it = entries.iterator();
        if (it.hasNext()) {
            current = it.next();
            itsSeparatorTime = true;
        }
    }


    public String buildText() {
        reset();
        FastCharArrayWriter writer = FastCharArrayWriter.Cached.cached();
        for (Entry entry : entries) {
            writer.append(entry.reader());
            writer.append(' ');
        }
        reset();
        return writer.toString();
    }

    public List<Entry> entries() {
        return this.entries;
    }

    public Set<String> fields() {
        Set<String> fields = newHashSet();
        for (Entry entry : entries) {
            fields.add(entry.name());
        }
        return fields;
    }

    public Entry current() {
        return this.current;
    }

    @Override public int read(char[] cbuf, int off, int len) throws IOException {
        if (current == null) {
            return -1;
        }
        if (customBoost) {
            int result = current.reader().read(cbuf, off, len);
            if (result == -1) {
                if (itsSeparatorTime) {
                    itsSeparatorTime = false;
                    cbuf[off] = ' ';
                    return 1;
                }
                itsSeparatorTime = true;
                // close(); No need to close, we work on in mem readers
                if (it.hasNext()) {
                    current = it.next();
                } else {
                    current = null;
                }
                return read(cbuf, off, len);
            }
            return result;
        } else {
            int read = 0;
            while (len > 0) {
                int result = current.reader().read(cbuf, off, len);
                if (result == -1) {
                    if (it.hasNext()) {
                        current = it.next();
                    } else {
                        current = null;
                        if (read == 0) {
                            return -1;
                        }
                        return read;
                    }
                    cbuf[off++] = ' ';
                    read++;
                    len--;
                } else {
                    read += result;
                    off += result;
                    len -= result;
                }
            }
            return read;
        }
    }

    @Override public void close() {
        if (current != null) {
            current.reader().close();
            current = null;
        }
    }


    @Override public boolean ready() throws IOException {
        return (current != null) && current.reader().ready();
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : entries) {
            sb.append(entry.name()).append(',');
        }
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4603.java