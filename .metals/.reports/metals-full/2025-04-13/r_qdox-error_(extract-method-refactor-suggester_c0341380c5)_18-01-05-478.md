error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/984.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/984.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/984.java
text:
```scala
v@@oid onValue(int docId, String value);

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

package org.elasticsearch.index.field;

import org.apache.lucene.index.IndexReader;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.index.field.doubles.DoubleFieldData;
import org.elasticsearch.index.field.floats.FloatFieldData;
import org.elasticsearch.index.field.ints.IntFieldData;
import org.elasticsearch.index.field.longs.LongFieldData;
import org.elasticsearch.index.field.shorts.ShortFieldData;
import org.elasticsearch.index.field.strings.StringFieldData;

import java.io.IOException;

/**
 * @author kimchy (Shay Banon)
 */
// General TODOs on FieldData
// TODO Make storing of freqs optional
// TODO Optimize the order (both int[] and int[][] when they are sparse, create an Order abstraction)
public abstract class FieldData {

    public static enum Type {
        STRING(StringFieldData.class, false),
        SHORT(ShortFieldData.class, true),
        INT(IntFieldData.class, true),
        LONG(LongFieldData.class, true),
        FLOAT(FloatFieldData.class, true),
        DOUBLE(DoubleFieldData.class, true);

        public final Class<? extends FieldData> fieldDataClass;

        private final boolean isNumeric;

        Type(Class<? extends FieldData> clazz, boolean numeric) {
            this.fieldDataClass = clazz;
            this.isNumeric = numeric;
        }

        public boolean isNumeric() {
            return isNumeric;
        }
    }

    private final String fieldName;

    private final FieldDataOptions options;

    protected FieldData(String fieldName, FieldDataOptions options) {
        this.fieldName = fieldName;
        this.options = options;
    }

    /**
     * The field name of this field data.
     */
    public final String fieldName() {
        return fieldName;
    }

    /**
     * Is the field data a multi valued one (has multiple values / terms per document id) or not.
     */
    public abstract boolean multiValued();

    /**
     * Is there a value associated with this document id.
     */
    public abstract boolean hasValue(int docId);

    public abstract String stringValue(int docId);

    public abstract void forEachValue(StringValueProc proc);

    public static interface StringValueProc {
        void onValue(String value, int freq);
    }

    public abstract void forEachValueInDoc(int docId, StringValueInDocProc proc);

    public static interface StringValueInDocProc {
        void onValue(String value, int docId);
    }

    /**
     * The type of this field data.
     */
    public abstract Type type();

    public FieldDataOptions options() {
        return this.options;
    }

    public static FieldData load(Type type, IndexReader reader, String fieldName, FieldDataOptions options) throws IOException {
        return load(type.fieldDataClass, reader, fieldName, options);
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends FieldData> T load(Class<T> type, IndexReader reader, String fieldName, FieldDataOptions options) throws IOException {
        if (type == StringFieldData.class) {
            return (T) StringFieldData.load(reader, fieldName, options);
        } else if (type == IntFieldData.class) {
            return (T) IntFieldData.load(reader, fieldName, options);
        } else if (type == LongFieldData.class) {
            return (T) LongFieldData.load(reader, fieldName, options);
        } else if (type == FloatFieldData.class) {
            return (T) FloatFieldData.load(reader, fieldName, options);
        } else if (type == DoubleFieldData.class) {
            return (T) DoubleFieldData.load(reader, fieldName, options);
        } else if (type == ShortFieldData.class) {
            return (T) ShortFieldData.load(reader, fieldName, options);
        }
        throw new ElasticSearchIllegalArgumentException("No support for type [" + type + "] to load field data");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/984.java