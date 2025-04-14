error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9146.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9146.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,65]

error in qdox parser
file content:
```java
offset: 65
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9146.java
text:
```scala
"src/main/java/org/apache/wicket/ajax/wicket-ajax-debug-drag.js",@@

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
package org.apache.wicket.util.license;


/**
 * Test that the license headers are in place in this project. The tests are run
 * from {@link ApacheLicenseHeaderTestCase}, but you can add project specific
 * tests here if needed.
 * 
 * @author Frank Bille Jensen (frankbille)
 */
public class ApacheLicenceHeaderTest extends ApacheLicenseHeaderTestCase
{
	/**
	 * Construct.
	 */
	public ApacheLicenceHeaderTest()
	{
//		addHeaders = true;
		
		htmlIgnore = new String[] {
				/*
				 * .html in test is very test specific and a license header 
				 * would confuse and make it unclear what the test is about. 
				 */
				"src/test/java",
				/*
				 * See NOTICE.txt
				 */
				"src/main/java/org/apache/wicket/util/diff",				
		};
		
		cssIgnore = new String[] {
				/*
				 * .css in test is very test specific and a license header 
				 * would confuse and make it unclear what the test is about. 
				 */
				"src/test/java" 
		};
		
		xmlIgnore = new String[] { 
				"EclipseCodeFormat.xml",
				"src/assembly/bin.xml"
		};
		
		javaIgnore = new String[] {
				/*
				 * ASL1.1. Taken from Maven JRCS. See NOTICE.txt
				 */
				"src/main/java/org/apache/wicket/util/diff/AddDelta.java",
				"src/main/java/org/apache/wicket/util/diff/ChangeDelta.java",
				"src/main/java/org/apache/wicket/util/diff/Chunk.java",
				"src/main/java/org/apache/wicket/util/diff/DeleteDelta.java",
				"src/main/java/org/apache/wicket/util/diff/Delta.java",
				"src/main/java/org/apache/wicket/util/diff/Diff.java",
				"src/main/java/org/apache/wicket/util/diff/DiffAlgorithm.java",
				"src/main/java/org/apache/wicket/util/diff/DifferentiationFailedException.java",
				"src/main/java/org/apache/wicket/util/diff/DiffException.java",
				"src/main/java/org/apache/wicket/util/diff/PatchFailedException.java",
				"src/main/java/org/apache/wicket/util/diff/Revision.java",
				"src/main/java/org/apache/wicket/util/diff/RevisionVisitor.java",
				"src/main/java/org/apache/wicket/util/diff/ToString.java",
				"src/main/java/org/apache/wicket/util/diff/myers/DiffNode.java",
				"src/main/java/org/apache/wicket/util/diff/myers/MyersDiff.java",
				"src/main/java/org/apache/wicket/util/diff/myers/PathNode.java",
				"src/main/java/org/apache/wicket/util/diff/myers/Snake.java",
				/*
				 * Needs to be resolved (rewritten or NOTICE)
				 */
				"src/main/java/org/apache/wicket/util/concurrent/ConcurrentReaderHashMap.java",
				"src/main/java/org/apache/wicket/util/concurrent/ConcurrentHashMap.java",
				"src/main/java/org/apache/wicket/util/concurrent/CopyOnWriteArrayList.java"
		};
		
		javaScriptIgnore = new String[] { 
				"src/site/xdoc/onestat.js",
				/*
				 * .js in test is very test specific and a license 
				 * header would confuse and make it unclear what the test 
				 * is about. 
				 */
				"src/test/java",
				/*
				 * See NOTICE.txt
				 */
				"src/main/java/org/apache/wicket/ajax/org.apache.wicket-ajax-debug-drag.js",
				"src/main/java/org/apache/wicket/markup/html/form/upload/MultiFileUploadField.js"
		};
		
		propertiesIgnore = new String[] {
				/*
				 * .properties in test is very test specific and a license 
				 * header would confuse and make it unclear what the test 
				 * is about. 
				 */
				"src/test/java", 
		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9146.java