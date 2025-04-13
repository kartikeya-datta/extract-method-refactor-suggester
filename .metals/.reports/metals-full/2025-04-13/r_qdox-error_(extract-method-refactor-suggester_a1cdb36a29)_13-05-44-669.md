error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/387.java
text:
```scala
private static final l@@ong serialVersionUID = 3518477308466486130L;

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

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

/**
 * Closure implementation calls the closure whose predicate returns true,
 * like a switch statement.
 * 
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class SwitchClosure implements Closure, Serializable {

    /** Serial version UID */
    static final long serialVersionUID = 3518477308466486130L;

    /** The tests to consider */
    private final Predicate[] iPredicates;
    /** The matching closures to call */
    private final Closure[] iClosures;
    /** The default closure to call if no tests match */
    private final Closure iDefault;

    /**
     * Factory method that performs validation and copies the parameter arrays.
     * 
     * @param predicates  array of predicates, cloned, no nulls
     * @param closures  matching array of closures, cloned, no nulls
     * @param defaultClosure  the closure to use if no match, null means nop
     * @return the <code>chained</code> closure
     * @throws IllegalArgumentException if array is null
     * @throws IllegalArgumentException if any element in the array is null
     */
    public static Closure getInstance(Predicate[] predicates, Closure[] closures, Closure defaultClosure) {
        FunctorUtils.validate(predicates);
        FunctorUtils.validate(closures);
        if (predicates.length != closures.length) {
            throw new IllegalArgumentException("The predicate and closure arrays must be the same size");
        }
        if (predicates.length == 0) {
            return (defaultClosure == null ? NOPClosure.INSTANCE : defaultClosure);
        }
        predicates = FunctorUtils.copy(predicates);
        closures = FunctorUtils.copy(closures);
        return new SwitchClosure(predicates, closures, defaultClosure);
    }

    /**
     * Create a new Closure that calls one of the closures depending 
     * on the predicates. 
     * <p>
     * The Map consists of Predicate keys and Closure values. A closure 
     * is called if its matching predicate returns true. Each predicate is evaluated
     * until one returns true. If no predicates evaluate to true, the default
     * closure is called. The default closure is set in the map with a 
     * null key. The ordering is that of the iterator() method on the entryset 
     * collection of the map.
     * 
     * @param predicatesAndClosures  a map of predicates to closures
     * @return the <code>switch</code> closure
     * @throws IllegalArgumentException if the map is null
     * @throws IllegalArgumentException if any closure in the map is null
     * @throws ClassCastException  if the map elements are of the wrong type
     */
    public static Closure getInstance(Map predicatesAndClosures) {
        Closure[] closures = null;
        Predicate[] preds = null;
        if (predicatesAndClosures == null) {
            throw new IllegalArgumentException("The predicate and closure map must not be null");
        }
        if (predicatesAndClosures.size() == 0) {
            return NOPClosure.INSTANCE;
        }
        // convert to array like this to guarantee iterator() ordering
        Closure defaultClosure = (Closure) predicatesAndClosures.remove(null);
        int size = predicatesAndClosures.size();
        if (size == 0) {
            return (defaultClosure == null ? NOPClosure.INSTANCE : defaultClosure);
        }
        closures = new Closure[size];
        preds = new Predicate[size];
        int i = 0;
        for (Iterator it = predicatesAndClosures.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            preds[i] = (Predicate) entry.getKey();
            closures[i] = (Closure) entry.getValue();
            i++;
        }
        return new SwitchClosure(preds, closures, defaultClosure);
    }
    
    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     * 
     * @param predicates  array of predicates, not cloned, no nulls
     * @param closures  matching array of closures, not cloned, no nulls
     * @param defaultClosure  the closure to use if no match, null means nop
     */
    public SwitchClosure(Predicate[] predicates, Closure[] closures, Closure defaultClosure) {
        super();
        iPredicates = predicates;
        iClosures = closures;
        iDefault = (defaultClosure == null ? NOPClosure.INSTANCE : defaultClosure);
    }

    /**
     * Executes the closure whose matching predicate returns true
     * 
     * @param input  the input object
     */
    public void execute(Object input) {
        for (int i = 0; i < iPredicates.length; i++) {
            if (iPredicates[i].evaluate(input) == true) {
                iClosures[i].execute(input);
                return;
            }
        }
        iDefault.execute(input);
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
     * Gets the closures, do not modify the array.
     * 
     * @return the closures
     * @since Commons Collections 3.1
     */
    public Closure[] getClosures() {
        return iClosures;
    }

    /**
     * Gets the default closure.
     * 
     * @return the default closure
     * @since Commons Collections 3.1
     */
    public Closure getDefaultClosure() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/387.java