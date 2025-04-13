error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/388.java
text:
```scala
private static final l@@ong serialVersionUID = -6404460890903469332L;

/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * Transformer implementation calls the transformer whose predicate returns true,
 * like a switch statement.
 * 
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class SwitchTransformer implements Transformer, Serializable {

    /** Serial version UID */
    static final long serialVersionUID = -6404460890903469332L;

    /** The tests to consider */
    private final Predicate[] iPredicates;
    /** The matching transformers to call */
    private final Transformer[] iTransformers;
    /** The default transformer to call if no tests match */
    private final Transformer iDefault;

    /**
     * Factory method that performs validation and copies the parameter arrays.
     * 
     * @param predicates  array of predicates, cloned, no nulls
     * @param transformers  matching array of transformers, cloned, no nulls
     * @param defaultTransformer  the transformer to use if no match, null means nop
     * @return the <code>chained</code> transformer
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if any element in the array is null
     */
    public static Transformer getInstance(Predicate[] predicates, Transformer[] transformers, Transformer defaultTransformer) {
        FunctorUtils.validate(predicates);
        FunctorUtils.validate(transformers);
        if (predicates.length != transformers.length) {
            throw new IllegalArgumentException("The predicate and transformer arrays must be the same size");
        }
        if (predicates.length == 0) {
            return (defaultTransformer == null ? ConstantTransformer.NULL_INSTANCE : defaultTransformer);
        }
        predicates = FunctorUtils.copy(predicates);
        transformers = FunctorUtils.copy(transformers);
        return new SwitchTransformer(predicates, transformers, defaultTransformer);
    }

    /**
     * Create a new Transformer that calls one of the transformers depending 
     * on the predicates. 
     * <p>
     * The Map consists of Predicate keys and Transformer values. A transformer 
     * is called if its matching predicate returns true. Each predicate is evaluated
     * until one returns true. If no predicates evaluate to true, the default
     * transformer is called. The default transformer is set in the map with a 
     * null key. The ordering is that of the iterator() method on the entryset 
     * collection of the map.
     * 
     * @param predicatesAndTransformers  a map of predicates to transformers
     * @return the <code>switch</code> transformer
     * @throws IllegalArgumentException if the map is null
     * @throws IllegalArgumentException if any transformer in the map is null
     * @throws ClassCastException  if the map elements are of the wrong type
     */
    public static Transformer getInstance(Map predicatesAndTransformers) {
        Transformer[] transformers = null;
        Predicate[] preds = null;
        if (predicatesAndTransformers == null) {
            throw new IllegalArgumentException("The predicate and transformer map must not be null");
        }
        if (predicatesAndTransformers.size() == 0) {
            return ConstantTransformer.NULL_INSTANCE;
        }
        // convert to array like this to guarantee iterator() ordering
        Transformer defaultTransformer = (Transformer) predicatesAndTransformers.remove(null);
        int size = predicatesAndTransformers.size();
        if (size == 0) {
            return (defaultTransformer == null ? ConstantTransformer.NULL_INSTANCE : defaultTransformer);
        }
        transformers = new Transformer[size];
        preds = new Predicate[size];
        int i = 0;
        for (Iterator it = predicatesAndTransformers.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            preds[i] = (Predicate) entry.getKey();
            transformers[i] = (Transformer) entry.getValue();
            i++;
        }
        return new SwitchTransformer(preds, transformers, defaultTransformer);
    }
    
    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     * 
     * @param predicates  array of predicates, not cloned, no nulls
     * @param transformers  matching array of transformers, not cloned, no nulls
     * @param defaultTransformer  the transformer to use if no match, null means nop
     */
    public SwitchTransformer(Predicate[] predicates, Transformer[] transformers, Transformer defaultTransformer) {
        super();
        iPredicates = predicates;
        iTransformers = transformers;
        iDefault = (defaultTransformer == null ? ConstantTransformer.NULL_INSTANCE : defaultTransformer);
    }

    /**
     * Transforms the input to result by calling the transformer whose matching
     * predicate returns true.
     * 
     * @param input  the input object to transform
     * @return the transformed result
     */
    public Object transform(Object input) {
        for (int i = 0; i < iPredicates.length; i++) {
            if (iPredicates[i].evaluate(input) == true) {
                return iTransformers[i].transform(input);
            }
        }
        return iDefault.transform(input);
    }

    /**
     * Gets the predicates, do not modify the array.
     * 
     * @return the predicates
     * @since Commons Collections 3.1
     */
    public Predicate[] getPredicates() {
        return iPredicates;
    }

    /**
     * Gets the transformers, do not modify the array.
     * 
     * @return the transformers
     * @since Commons Collections 3.1
     */
    public Transformer[] getTransformers() {
        return iTransformers;
    }

    /**
     * Gets the default transformer.
     * 
     * @return the default transformer
     * @since Commons Collections 3.1
     */
    public Transformer getDefaultTransformer() {
        return iDefault;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/388.java