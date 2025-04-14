error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5532.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5532.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5532.java
text:
```scala
public L@@ist<AttributeDefinition> getAttributeDefinitions(String element);

/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.core.internal.provisional.dom;

import java.io.Serializable;
import java.util.Set;
import java.util.List;

import org.eclipse.wst.xml.vex.core.internal.validator.AttributeDefinition;

/**
 * Represents an object that can validate the structure of a document.
 * Validators must be serializable.
 * @model
 */
public interface Validator extends Serializable {

	/**
	 * String indicating that character data is allowed at the given point in
	 * the document.
	 * @model
	 */
	public static final String PCDATA = "#PCDATA";

	/**
	 * Returns the AttributeDefinition for a particular attribute.
	 * 
	 * @param element
	 *            Name of the element.
	 * @param attribute
	 *            Name of the attribute.
	 * @model
	 */
	public AttributeDefinition getAttributeDefinition(String element,
			String attribute);

	/**
	 * Returns the attribute definitions that apply to the given element.
	 * 
	 * @param element
	 *            Name of the element to check.
	 * @model
	 */
	public AttributeDefinition[] getAttributeDefinitions(String element);

	/**
	 * Returns a set of Strings representing valid root elements for the given
	 * document type.
	 * @model 
	 */
	public Set getValidRootElements();

	/**
	 * Returns a set of Strings representing items that are valid at point in
	 * the child nodes of a given element. Each string is either an element name
	 * or Validator.PCDATA.
	 * 
	 * @param element
	 *            Name of the parent element.
	 * @model 
	 */
	public Set<String> getValidItems(String element);

	/**
	 * Returns true if the given sequence is valid for the given element.
	 * Accepts three sequences, which will be concatenated before doing the
	 * check.
	 * 
	 * @param element
	 *            Name of the element being tested.
	 * @param nodes
	 *            Array of element names and Validator.PCDATA.
	 * @param partial
	 *            If true, an valid but incomplete sequence is acceptable.
	 * @model
	 */
	public boolean isValidSequence(String element, List<String> nodes,
			boolean partial);

	/**
	 * Returns true if the given sequence is valid for the given element.
	 * Accepts three sequences, which will be concatenated before doing the
	 * check.
	 * 
	 * @param element
	 *            Name of the element being tested.
	 * @param seq1
	 *            List of element names and Validator.PCDATA.
	 * @param seq2
	 *            List of element names and Validator.PCDATA. May be null or
	 *            empty.
	 * @param seq3
	 *            List of element names and Validator.PCDATA. May be null or
	 *            empty.
	 * @param partial
	 *            If true, an valid but incomplete sequence is acceptable.
	 * @model
	 */
	public boolean isValidSequence(String element, List<String> seq1,
			List<String> seq2, List<String> seq3, boolean partial);

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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5532.java