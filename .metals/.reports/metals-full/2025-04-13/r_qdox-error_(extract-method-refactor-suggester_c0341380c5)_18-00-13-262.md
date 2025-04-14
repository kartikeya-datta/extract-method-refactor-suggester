error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11618.java
text:
```scala
private final T@@ object;

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
package org.apache.commons.collections4.sequence;

/**
 * Abstract base class for all commands used to transform an objects sequence
 * into another one.
 * <p>
 * When two objects sequences are compared through the
 * {@link SequencesComparator#getScript SequencesComparator.getScript} method,
 * the result is provided has a {@link EditScript script} containing the commands
 * that progressively transform the first sequence into the second one.
 * <p>
 * There are only three types of commands, all of which are subclasses of this
 * abstract class. Each command is associated with one object belonging to at
 * least one of the sequences. These commands are {@link InsertCommand
 * InsertCommand} which correspond to an object of the second sequence being
 * inserted into the first sequence, {@link DeleteCommand DeleteCommand} which
 * correspond to an object of the first sequence being removed and
 * {@link KeepCommand KeepCommand} which correspond to an object of the first
 * sequence which <code>equals</code> an object in the second sequence. It is
 * guaranteed that comparison is always performed this way (i.e. the
 * <code>equals</code> method of the object from the first sequence is used and
 * the object passed as an argument comes from the second sequence) ; this can
 * be important if subclassing is used for some elements in the first sequence
 * and the <code>equals</code> method is specialized.
 * 
 * @see SequencesComparator
 * @see EditScript
 * 
 * @since 4.0
 * @version $Id$
 */
public abstract class EditCommand<T> {

    /** Object on which the command should be applied. */
    private T object;

    /**
     * Simple constructor. Creates a new instance of EditCommand
     * 
     * @param object  reference to the object associated with this command, this
     *   refers to an element of one of the sequences being compared
     */
    protected EditCommand(final T object) {
        this.object = object;
    }

    /**
     * Returns the object associated with this command.
     *
     * @return the object on which the command is applied
     */
    protected T getObject() {
        return object;
    }

    /**
     * Accept a visitor.
     * <p>
     * This method is invoked for each commands belonging to
     * an {@link EditScript EditScript}, in order to implement the visitor design pattern
     * 
     * @param visitor  the visitor to be accepted
     */
    public abstract void accept(CommandVisitor<T> visitor);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11618.java