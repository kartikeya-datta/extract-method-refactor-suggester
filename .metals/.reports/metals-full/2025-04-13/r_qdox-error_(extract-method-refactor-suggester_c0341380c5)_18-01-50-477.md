error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13323.java
text:
```scala
private static final l@@ong serialVersionUID = -317289374378977972L;

/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math;

/**
 * Exeption thrown when an error occurs evaluating a function.
 * <p>
 * Maintains an <code>argument</code> property holding the input value that
 * caused the function evaluation to fail.
 * 
 * @version $Revision$ $Date$
 */
public class FunctionEvaluationException extends MathException  {
    
    /** Serializable version identifier */
    static final long serialVersionUID = -317289374378977972L;
    
    /** Argument causing function evaluation failure */
    private double argument = Double.NaN;
    
    /**
     * Construct an exception indicating the argument value
     * that caused the function evaluation to fail.  Generates an exception
     * message of the form "Evaluation failed for argument = " + argument.
     * 
     * @param argument  the failing function argument 
     */
    public FunctionEvaluationException(double argument) {
        this(argument, "Evaluation failed for argument = " + argument);
    }
    
    /**
     * Construct an exception using the given argument and message
     * text.  The message text of the exception will start with 
     * <code>message</code> and be followed by 
     * " Evaluation failed for argument = " + argument.
     * 
     * @param argument  the failing function argument 
     * @param message  the exception message text
     */
    public FunctionEvaluationException(double argument, String message) {
        this(argument, message, null);
    }

    /**
     * Construct an exception with the given argument, message and root cause.
     * The message text of the exception will start with  <code>message</code>
     * and be followed by " Evaluation failed for argument = " + argument.
     * 
     * @param argument  the failing function argument 
     * @param message descriptive error message
     * @param cause root cause.
     */
    public FunctionEvaluationException(double argument, String message, 
            Throwable cause) {
        super(message + " Evaluation failed for argument=" + argument, cause);
        this.argument = argument;
    }
    
    /**
     * Returns the function argument that caused this exception.
     * 
     * @return  argument that caused function evaluation to fail
     */
    public double getArgument() {
        return this.argument;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13323.java