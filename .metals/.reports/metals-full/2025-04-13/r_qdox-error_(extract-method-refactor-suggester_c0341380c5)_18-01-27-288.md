error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12535.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12535.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[192,2]

error in qdox parser
file content:
```java
offset: 6696
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12535.java
text:
```scala
import org.apache.wicket.request.resource.ResourceReference;

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
package org.apache.wicket.behavior;

import org.apache.wicket.ng.resource.ResourceReference;

/**
 * A bunch of static helper methods to add CSS and Javascript to the markup headers
 * 
 * @author Eelco Hillenius
 * @author Matej Knopp
 */
public class HeaderContributor
{
	private static final long serialVersionUID = 1L;

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a CSS file that lives in a package.
	 * 
	 * @param scope
	 *            The scope of the package resource (typically the class of the caller, or a class
	 *            that lives in the package where the resource lives).
	 * @param path
	 *            The path
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final Class<?> scope, final String path)
	{
		return new CssHeaderContributor(scope, path);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a CSS file that lives in a package.
	 * 
	 * @param scope
	 *            The scope of the package resource (typically the class of the caller, or a class
	 *            that lives in the package where the resource lives).
	 * @param path
	 *            The path
	 * @param media
	 *            The media type for this CSS ("print", "screen", etc.)
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final Class<?> scope, final String path,
		final String media)
	{
		return new CssHeaderContributor(scope, path, media);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a CSS file that lives in a package.
	 * 
	 * @param reference
	 * 
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final ResourceReference reference)
	{
		return new CssReferenceHeaderContributor(reference);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a CSS file that lives in a package.
	 * 
	 * @param reference
	 * @param media
	 *            The media type for this CSS ("print", "screen", etc.)
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final ResourceReference reference,
		final String media)
	{
		return new CssReferenceHeaderContributor(reference, media);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor referencing a
	 * CSS file using one of the following schemes:
	 * <ul>
	 * <li>Starts with http:// or https:// for an external reference.</li>
	 * <li>Starts with "/" for an absolute reference that Wicket will not rewrite.</li>
	 * <li>Starts with anything else, which Wicket will automatically prepend to make relative to
	 * the context root of your web-app.</li>
	 * </ul>
	 * 
	 * @param location
	 *            The location of the css file.
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final String location)
	{
		return new CssLocationHeaderContributor(location);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor referencing a
	 * CSS file using one of the following schemes:
	 * <ul>
	 * <li>Starts with http:// or https:// for an external reference.</li>
	 * <li>Starts with "/" for an absolute reference that Wicket will not rewrite.</li>
	 * <li>Starts with anything else, which Wicket will automatically prepend to make relative to
	 * the context root of your web-app.</li>
	 * </ul>
	 * 
	 * @param location
	 *            The location of the css.
	 * @param media
	 *            The media type for this CSS ("print", "screen", etc.)
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forCss(final String location, final String media)
	{
		return new CssLocationHeaderContributor(location, media);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a java script file that lives in a package.
	 * 
	 * @param scope
	 *            The scope of the package resource (typically the class of the caller, or a class
	 *            that lives in the package where the resource lives).
	 * @param path
	 *            The path
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forJavaScript(final Class<?> scope,
		final String path)
	{
		return new JavascriptHeaderContributor(scope, path);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a java script file that lives in a package.
	 * 
	 * @param reference
	 * 
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forJavaScript(final ResourceReference reference)
	{
		return new JavascriptReferenceHeaderContributor(reference);
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor referencing a
	 * java script file using one of the following schemes:
	 * <ul>
	 * <li>Starts with http:// or https:// for an external reference.</li>
	 * <li>Starts with "/" for an absolute reference that Wicket will not rewrite.</li>
	 * <li>Starts with anything else, which Wicket will automatically prepend to make relative to
	 * the context root of your web-app.</li>
	 * </ul>
	 * 
	 * @param location
	 *            The location of the java script file.
	 * @return the new header contributor instance
	 */
	public static final AbstractHeaderContributor forJavaScript(final String location)
	{
		return new JavascriptLocationHeaderContributor(location);
	}

	/**
	 * No need to instantiate
	 */
	private HeaderContributor()
	{
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12535.java