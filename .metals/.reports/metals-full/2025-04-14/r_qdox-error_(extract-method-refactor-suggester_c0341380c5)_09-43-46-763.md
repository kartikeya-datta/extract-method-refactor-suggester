error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3663.java
text:
```scala
private v@@oid partialClear() {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
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
package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.z.AbstractLZWInputStream;

/**
 * Input stream that decompresses ZIP method 1 (unshrinking). A variation of the LZW algorithm, with some twists.
 * @NotThreadSafe
 * @since 1.7
 */
class UnshrinkingInputStream extends AbstractLZWInputStream {
    private static final int MAX_CODE_SIZE = 13;
    private static final int MAX_TABLE_SIZE = 1 << MAX_CODE_SIZE;
    private final boolean[] isUsed;
    
    public UnshrinkingInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        setClearCode(codeSize);
        initializeTables(MAX_CODE_SIZE);
        isUsed = new boolean[prefixes.length];
        for (int i = 0; i < (1 << 8); i++) {
            isUsed[i] = true;
        }
        tableSize = clearCode + 1;
    }

    @Override
    protected int addEntry(int previousCode, byte character) throws IOException {
        while ((tableSize < MAX_TABLE_SIZE) && isUsed[tableSize]) {
            tableSize++;
        }
        int idx = addEntry(previousCode, character, MAX_TABLE_SIZE);
        if (idx >= 0) {
            isUsed[idx] = true;
        }
        return idx;
    }
    
    private void partialClear() throws IOException {
        final boolean[] isParent = new boolean[MAX_TABLE_SIZE];
        for (int i = 0; i < isUsed.length; i++) {
            if (isUsed[i] && prefixes[i] != -1) {
                isParent[prefixes[i]] = true;
            }
        }
        for (int i = clearCode + 1; i < isParent.length; i++) {
            if (!isParent[i]) {
                isUsed[i] = false;
                prefixes[i] = -1;
            }
        }
    }

    @Override
    protected int decompressNextSymbol() throws IOException {
        //
        //                   table entry    table entry
        //                  _____________   _____
        //    table entry  /             \ /     \
        //    ____________/               \       \
        //   /           / \             / \       \
        //  +---+---+---+---+---+---+---+---+---+---+
        //  | . | . | . | . | . | . | . | . | . | . |
        //  +---+---+---+---+---+---+---+---+---+---+
        //  |<--------->|<------------->|<----->|<->|
        //     symbol        symbol      symbol  symbol
        //
        final int code = readNextCode();
        if (code < 0) {
            return -1;
        } else if (code == clearCode) {
            final int subCode = readNextCode();
            if (subCode < 0) {
                throw new IOException("Unexpected EOF;");
            } else if (subCode == 1) {
                if (codeSize < MAX_CODE_SIZE) {
                    codeSize++;
                } else {
                    throw new IOException("Attempt to increase code size beyond maximum");
                }
            } else if (subCode == 2) {
                partialClear();
                tableSize = clearCode + 1;
            } else {
                throw new IOException("Invalid clear code subcode " + subCode);
            }
            return 0;
        } else {
            boolean addedUnfinishedEntry = false;
            int effectiveCode = code;
            if (!isUsed[code]) {
                effectiveCode = addRepeatOfPreviousCode();
                addedUnfinishedEntry = true;
            }
            return expandCodeToOutputStack(effectiveCode, addedUnfinishedEntry);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3663.java