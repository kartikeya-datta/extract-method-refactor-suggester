error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 861
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6366.java
text:
```scala
private abstract static class Predicate {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
p@@ackage org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;

/**
 * EnumeratedAttribute for quantifier comparisons. Evaluates a
 * <code>boolean[]</code> or raw <code>true</code> and <code>false</code>
 * counts. Accepts the following values:<ul>
 * <li>"all"</li> - none <code>false</code>
 * <li>"each"</li> - none <code>false</code>
 * <li>"every"</li> - none <code>false</code>
 * <li>"any"</li> - at least one <code>true</code>
 * <li>"some"</li> - at least one <code>true</code>
 * <li>"one"</li> - exactly one <code>true</code>
 * <li>"majority"</li> - more <code>true</code> than <code>false</code>
 * <li>"most"</li> - more <code>true</code> than <code>false</code>
 * <li>"none"</li> - none <code>true</code>
 * </ul>
 * @since Ant 1.7
 */
public class Quantifier extends EnumeratedAttribute {
    private static final String[] VALUES
        = new String[] {"all", "each", "every", "any", "some", "one",
                        "majority", "most", "none"};

    /** ALL instance */
    public static final Quantifier ALL = new Quantifier("all");
    /** ANY instance */
    public static final Quantifier ANY = new Quantifier("any");
    /** ONE instance */
    public static final Quantifier ONE = new Quantifier("one");
    /** MAJORITY instance */
    public static final Quantifier MAJORITY = new Quantifier("majority");
    /** NONE instance */
    public static final Quantifier NONE = new Quantifier("none");

    private static abstract class Predicate {
        abstract boolean eval(int t, int f);
    }

    private static final Predicate ALL_PRED = new Predicate() {
        boolean eval(int t, int f) { return f == 0; }
    };

    private static final Predicate ANY_PRED = new Predicate() {
        boolean eval(int t, int f) { return t > 0; }
    };

    private static final Predicate ONE_PRED = new Predicate() {
        boolean eval(int t, int f) { return t == 1; }
    };

    private static final Predicate MAJORITY_PRED = new Predicate() {
        boolean eval(int t, int f) { return t > f; }
    };

    private static final Predicate NONE_PRED = new Predicate() {
        boolean eval(int t, int f) { return t == 0; }
    };

    private static final Predicate[] PREDS = new Predicate[VALUES.length];

    static {
        PREDS[0] = ALL_PRED;
        PREDS[1] = ALL_PRED;
        PREDS[2] = ALL_PRED;
        PREDS[3] = ANY_PRED;
        PREDS[4] = ANY_PRED;
        PREDS[5] = ONE_PRED;
        PREDS[6] = MAJORITY_PRED;
        PREDS[7] = MAJORITY_PRED;
        PREDS[8] = NONE_PRED;
    }

    /**
     * Default constructor.
     */
    public Quantifier() {
    }

    /**
     * Construct a new Quantifier with the specified value.
     * @param value the EnumeratedAttribute value.
     */
    public Quantifier(String value) {
        setValue(value);
    }

    /**
     * Return the possible values.
     * @return String[] of EnumeratedAttribute values.
     */
    public String[] getValues() {
        return VALUES;
    }

    /**
     * Evaluate a <code>boolean<code> array.
     * @param b the <code>boolean[]</code> to evaluate.
     * @return true if the argument fell within the parameters of this Quantifier.
     */
    public boolean evaluate(boolean[] b) {
        int t = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i]) {
                t++;
            }
        }
        return evaluate(t, b.length - t);
    }

    /**
     * Evaluate integer <code>true</code> vs. <code>false</code> counts.
     * @param t the number of <code>true</code> values.
     * @param f the number of <code>false</code> values.
     * @return true if the arguments fell within the parameters of this Quantifier.
     */
    public boolean evaluate(int t, int f) {
        int index = getIndex();
        if (index == -1) {
            throw new BuildException("Quantifier value not set.");
        }
        return PREDS[index].eval(t, f);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6366.java