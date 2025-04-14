error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13838.java
text:
```scala
E@@numeration<FileSelector> selectorElements();

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

package org.apache.tools.ant.types.selectors;

import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

/**
 * This is the interface for selectors that can contain other selectors.
 *
 * @since 1.5
 */
public interface SelectorContainer {

    /**
     * Indicates whether there are any selectors here.
     *
     * @return whether any selectors are in this container
     */
    boolean hasSelectors();

    /**
     * Gives the count of the number of selectors in this container
     *
     * @return the number of selectors in this container
     */
    int selectorCount();

    /**
     * Returns the set of selectors as an array.
     * @param p the current project
     * @return an array of selectors in this container
     */
    FileSelector[] getSelectors(Project p);

    /**
     * Returns an enumerator for accessing the set of selectors.
     *
     * @return an enumerator that goes through each of the selectors
     */
    Enumeration selectorElements();

    /**
     * Add a new selector into this container.
     *
     * @param selector the new selector to add
     */
    void appendSelector(FileSelector selector);

    /* Methods below all add specific selectors */

    /**
     * add a "Select" selector entry on the selector list
     * @param selector the selector to add
     */
    void addSelector(SelectSelector selector);

    /**
     * add an "And" selector entry on the selector list
     * @param selector the selector to add
     */
    void addAnd(AndSelector selector);

    /**
     * add an "Or" selector entry on the selector list
     * @param selector the selector to add
     */
    void addOr(OrSelector selector);

    /**
     * add a "Not" selector entry on the selector list
     * @param selector the selector to add
     */
    void addNot(NotSelector selector);

    /**
     * add a "None" selector entry on the selector list
     * @param selector the selector to add
     */
    void addNone(NoneSelector selector);

    /**
     * add a majority selector entry on the selector list
     * @param selector the selector to add
     */
    void addMajority(MajoritySelector selector);

    /**
     * add a selector date entry on the selector list
     * @param selector the selector to add
     */
    void addDate(DateSelector selector);

    /**
     * add a selector size entry on the selector list
     * @param selector the selector to add
     */
    void addSize(SizeSelector selector);

    /**
     * add a selector filename entry on the selector list
     * @param selector the selector to add
     */
    void addFilename(FilenameSelector selector);

    /**
     * add an extended selector entry on the selector list
     * @param selector the selector to add
     */
    void addCustom(ExtendSelector selector);

    /**
     * add a contains selector entry on the selector list
     * @param selector the selector to add
     */
    void addContains(ContainsSelector selector);

    /**
     * add a present selector entry on the selector list
     * @param selector the selector to add
     */
    void addPresent(PresentSelector selector);

    /**
     * add a depth selector entry on the selector list
     * @param selector the selector to add
     */
    void addDepth(DepthSelector selector);

    /**
     * add a depends selector entry on the selector list
     * @param selector the selector to add
     */
    void addDepend(DependSelector selector);

    /**
     * add a regular expression selector entry on the selector list
     * @param selector the selector to add
     */
    void addContainsRegexp(ContainsRegexpSelector selector);

    /**
     * add the type selector
     * @param selector the selector to add
     * @since ant 1.6
     */
    void addType(TypeSelector selector);

    /**
     * add the different selector
     * @param selector the selector to add
     * @since ant 1.6
     */
    void addDifferent(DifferentSelector selector);

    /**
     * add the modified selector
     * @param selector the selector to add
     * @since ant 1.6
     */
    void addModified(ModifiedSelector selector);

    /**
     * add an arbitrary selector
     * @param selector the selector to add
     * @since Ant 1.6
     */
    void add(FileSelector selector);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13838.java