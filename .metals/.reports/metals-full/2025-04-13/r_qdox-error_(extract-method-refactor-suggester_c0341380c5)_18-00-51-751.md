error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7585.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7585.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7585.java
text:
```scala
s@@uper(name, UidFieldMapper.Defaults.FIELD_TYPE);

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

package org.elasticsearch.common.lucene.uid;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.Numbers;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.index.mapper.internal.UidFieldMapper;

import java.io.IOException;
import java.io.Reader;

/**
 *
 */
// TODO: LUCENE 4 UPGRADE: Store version as doc values instead of as a payload.
public class UidField extends Field {

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
    // LUCENE 4 UPGRADE: We can get rid of the do while loop, since there is only one _uid value (live docs are taken into account)
    public static DocIdAndVersion loadDocIdAndVersion(AtomicReaderContext context, Term term) {
        int docId = Lucene.NO_DOC;
        try {
            DocsAndPositionsEnum uid = context.reader().termPositionsEnum(term);
            if (uid == null || uid.nextDoc() == DocIdSetIterator.NO_MORE_DOCS) {
                return null; // no doc
            }
            // Note, only master docs uid have version payload, so we can use that info to not
            // take them into account
            do {
                docId = uid.docID();
                uid.nextPosition();
                if (uid.getPayload() == null) {
                    continue;
                }
                if (uid.getPayload().length < 8) {
                    continue;
                }
                byte[] payload = new byte[uid.getPayload().length];
                System.arraycopy(uid.getPayload().bytes, uid.getPayload().offset, payload, 0, uid.getPayload().length);
                return new DocIdAndVersion(docId, Numbers.bytesToLong(payload), context);
            } while (uid.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
            return new DocIdAndVersion(docId, -2, context);
        } catch (Exception e) {
            return new DocIdAndVersion(docId, -2, context);
        }
    }

    /**
     * Load the version for the uid from the reader, returning -1 if no doc exists, or -2 if
     * no version is available (for backward comp.)
     */
    // LUCENE 4 UPGRADE: We can get rid of the do while loop, since there is only one _uid value (live docs are taken into account)
    public static long loadVersion(AtomicReaderContext context, Term term) {
        try {
            DocsAndPositionsEnum uid = context.reader().termPositionsEnum(term);
            if (uid == null || uid.nextDoc() == DocIdSetIterator.NO_MORE_DOCS) {
                return -1;
            }
            // Note, only master docs uid have version payload, so we can use that info to not
            // take them into account
            do {
                uid.nextPosition();
                if (uid.getPayload() == null) {
                    continue;
                }
                if (uid.getPayload().length < 8) {
                    continue;
                }
                byte[] payload = new byte[uid.getPayload().length];
                System.arraycopy(uid.getPayload().bytes, uid.getPayload().offset, payload, 0, uid.getPayload().length);
                return Numbers.bytesToLong(payload);
            } while (uid.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
            return -2;
        } catch (Exception e) {
            return -2;
        }
    }

    private String uid;

    private long version;

    public UidField(String uid) {
        this(UidFieldMapper.NAME, uid, 0);
    }

    public UidField(String name, String uid, long version) {
        super(name, UidFieldMapper.Defaults.UID_FIELD_TYPE);
        this.uid = uid;
        this.version = version;
        this.tokenStream = new UidPayloadTokenStream(this);
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
    public TokenStream tokenStream(Analyzer analyzer) throws IOException {
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
            payloadAttribute.setPayload(new BytesRef(Numbers.longToBytes(field.version())));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7585.java