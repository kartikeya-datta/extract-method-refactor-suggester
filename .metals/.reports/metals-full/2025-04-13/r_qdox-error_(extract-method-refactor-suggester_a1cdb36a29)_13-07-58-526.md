error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10185.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10185.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 846
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10185.java
text:
```scala
public class UidField extends Field {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

p@@ackage org.elasticsearch.common.lucene.uid;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.AbstractField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.Term;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.lucene.Lucene;

import java.io.IOException;
import java.io.Reader;

/**
 *
 */
public class UidField extends AbstractField {

    public static class DocIdAndVersion {
        public final int docId;
        public final long version;
        public final AtomicReaderContext reader;

        public DocIdAndVersion(int docId, long version, AtomicReaderContext reader) {
            this.docId = docId;
            this.version = version;
            this.reader = reader;
        }
    }

    // this works fine for nested docs since they don't have the payload which has the version
    // so we iterate till we find the one with the payload
    public static DocIdAndVersion loadDocIdAndVersion(AtomicReaderContext reader, Term term) {
        int docId = Lucene.NO_DOC;
        TermPositions uid = null;
        try {
            uid = reader.termPositions(term);
            if (!uid.next()) {
                return null; // no doc
            }
            // Note, only master docs uid have version payload, so we can use that info to not
            // take them into account
            do {
                docId = uid.doc();
                uid.nextPosition();
                if (!uid.isPayloadAvailable()) {
                    continue;
                }
                if (uid.getPayloadLength() < 8) {
                    continue;
                }
                byte[] payload = uid.getPayload(new byte[8], 0);
                return new DocIdAndVersion(docId, Numbers.bytesToLong(payload), reader);
            } while (uid.next());
            return new DocIdAndVersion(docId, -2, reader);
        } catch (Exception e) {
            return new DocIdAndVersion(docId, -2, reader);
        } finally {
            if (uid != null) {
                try {
                    uid.close();
                } catch (IOException e) {
                    // nothing to do here...
                }
            }
        }
    }

    /**
     * Load the version for the uid from the reader, returning -1 if no doc exists, or -2 if
     * no version is available (for backward comp.)
     */
    public static long loadVersion(AtomicReaderContext reader, Term term) {
        TermPositions uid = null;
        try {
            uid = reader.termPositions(term);
            if (!uid.next()) {
                return -1;
            }
            // Note, only master docs uid have version payload, so we can use that info to not
            // take them into account
            do {
                uid.nextPosition();
                if (!uid.isPayloadAvailable()) {
                    continue;
                }
                if (uid.getPayloadLength() < 8) {
                    continue;
                }
                byte[] payload = uid.getPayload(new byte[8], 0);
                return Numbers.bytesToLong(payload);
            } while (uid.next());
            return -2;
        } catch (Exception e) {
            return -2;
        } finally {
            if (uid != null) {
                try {
                    uid.close();
                } catch (IOException e) {
                    // nothing to do here...
                }
            }
        }
    }

    private String uid;

    private long version;

    private final UidPayloadTokenStream tokenStream;

    public UidField(String name, String uid, long version) {
        super(name, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.NO);
        this.uid = uid;
        this.version = version;
        this.indexOptions = FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS;
        this.tokenStream = new UidPayloadTokenStream(this);
    }

    @Override
    public void setIndexOptions(FieldInfo.IndexOptions indexOptions) {
        // never allow to set this, since we want payload!
    }

    @Override
    public void setOmitTermFreqAndPositions(boolean omitTermFreqAndPositions) {
        // never allow to set this, since we want payload!
    }

    public String uid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String stringValue() {
        return uid;
    }

    @Override
    public Reader readerValue() {
        return null;
    }

    public long version() {
        return this.version;
    }

    public void version(long version) {
        this.version = version;
    }

    @Override
    public TokenStream tokenStreamValue() {
        return tokenStream;
    }

    public static final class UidPayloadTokenStream extends TokenStream {

        private final PayloadAttribute payloadAttribute = addAttribute(PayloadAttribute.class);
        private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

        private final UidField field;

        private boolean added = false;

        public UidPayloadTokenStream(UidField field) {
            this.field = field;
        }

        @Override
        public void reset() throws IOException {
            added = false;
        }

        @Override
        public final boolean incrementToken() throws IOException {
            if (added) {
                return false;
            }
            termAtt.setLength(0);
            termAtt.append(field.uid);
            payloadAttribute.setPayload(new Payload(Numbers.longToBytes(field.version())));
            added = true;
            return true;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10185.java