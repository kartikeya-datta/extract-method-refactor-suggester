error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10610.java
text:
```scala
r@@eturn FunctorUtils.<T, T>copy(iTransformers);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections.Transformer;

/**
 * Transformer implementation that chains the specified transformers together.
 * <p>
 * The input object is passed to the first transformer. The transformed result
 * is passed to the second transformer and so on.
 *
 * @since 3.0
 * @version $Id$
 */
public class ChainedTransformer<T> implements Transformer<T, T>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 3514945074733160196L;

    /** The transformers to call in turn */
    private final Transformer<? super T, ? extends T>[] iTransformers;

    /**
     * Factory method that performs validation and copies the parameter array.
     *
     * @param <T>  the object type
     * @param transformers  the transformers to chain, copied, no nulls
     * @return the <code>chained</code> transformer
     * @throws IllegalArgumentException if the transformers array is null
     * @throws IllegalArgumentException if any transformer in the array is null
     */
    public static <T> Transformer<T, T> chainedTransformer(final Transformer<? super T, ? extends T>... transformers) {
        FunctorUtils.validate(transformers);
        if (transformers.length == 0) {
            return NOPTransformer.<T>nopTransformer();
        }
        return new ChainedTransformer<T>(FunctorUtils.copy(transformers));
    }
    
    /**
     * Create a new Transformer that calls each transformer in turn, passing the 
     * result into the next transformer. The ordering is that of the iterator()
     * method on the collection.
     *
     * @param <T>  the object type
     * @param transformers  a collection of transformers to chain
     * @return the <code>chained</code> transformer
     * @throws IllegalArgumentException if the transformers collection is null
     * @throws IllegalArgumentException if any transformer in the collection is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Transformer<T, T> chainedTransformer(final Collection<? extends Transformer<T, T>> transformers) {
        if (transformers == null) {
            throw new IllegalArgumentException("Transformer collection must not be null");
        }
        if (transformers.size() == 0) {
            return NOPTransformer.<T>nopTransformer();
        }
        // convert to array like this to guarantee iterator() ordering
        final Transformer<T, T>[] cmds = transformers.toArray(new Transformer[transformers.size()]);
        FunctorUtils.validate(cmds);
        return new ChainedTransformer<T>(cmds);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     * 
     * @param transformers  the transformers to chain, not copied, no nulls
     */
    public ChainedTransformer(final Transformer<? super T, ? extends T>[] transformers) {
        super();
        iTransformers = transformers;
    }

    /**
     * Transforms the input to result via each decorated transformer
     * 
     * @param object  the input object passed to the first transformer
     * @return the transformed result
     */
    public T transform(T object) {
        for (final Transformer<? super T, ? extends T> iTransformer : iTransformers) {
            object = iTransformer.transform(object);
        }
        return object;
    }

    /**
     * Gets the transformers.
     *
     * @return a copy of the transformers
     * @since 3.1
     */
    public Transformer<? super T, ? extends T>[] getTransformers() {
        return FunctorUtils.copy(iTransformers);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10610.java