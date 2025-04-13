error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11349.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11349.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11349.java
text:
```scala
S@@tring loadStringResource(Component component, String key);

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
package org.apache.wicket.resource.loader;

import java.util.Locale;

import org.apache.wicket.Component;


/**
 * The string resource loader interface allows a strategy pattern to be applied to the loading of
 * resource strings for an application. The loader (or chain of loaders) that are used is configured
 * via the application settings.
 * <p>
 * Each particular implementation of this interface may define its own mechanism for searching for
 * resources. Please see the documents for each particular implementation to determine its behavior
 * and to see how it can be configured.
 * <p>
 * It is important to note that if a resource is not found by a particular loader than the loader
 * should return <code>null</code> rather than throw an exception. The reason for this is that
 * loaders can be arranged in a chain and it would be very inefficient for loaders earlier in the
 * chain to throw exceptions that must be caught and handled each time until the correct loader in
 * the chain is reached.
 * 
 * @see org.apache.wicket.settings.IResourceSettings
 * 
 * @author Chris Turner
 * @author Juergen Donnerstag
 */
public interface IStringResourceLoader
{
	/**
	 * Get the string resource for the given combination of component class, resource key, locale
	 * and style. The component class provided is used to allow implementation of component specific
	 * resource loading (e.g. per page or per reusable component). The key should be a String
	 * containing a lookup key into a resource bundle. The locale should contain the locale of the
	 * current operation so that the appropriate set of resources can be selected. The style allows
	 * the set of resources to select to be varied by skin/brand.
	 * 
	 * @param clazz
	 *            The class to get the string resource for
	 * @param key
	 *            The key should be a String containing a lookup key into a resource bundle
	 * @param locale
	 *            The locale should contain the locale of the current operation so that the
	 *            appropriate set of resources can be selected
	 * @param style
	 *            The style identifying the resource set to select the strings from (see
	 *            {@link org.apache.wicket.Session})
	 * @return The string resource value or null if the resource could not be loaded by this loader
	 */
	String loadStringResource(Class< ? > clazz, String key, Locale locale, String style);

	/**
	 * Get the string resource for the given combination of component and resource key. The
	 * component provided is used to allow implementation of component specific resource loading
	 * (e.g. per page or per reusable component). The key should be a String containing a lookup key
	 * into a resource bundle. The Locale and the style will be taken from the Component provided.
	 * 
	 * @param component
	 *            The component to get the string resource for
	 * @param key
	 *            The key should be a String containing a lookup key into a resource bundle
	 * @return The string resource value or null if the resource could not be loaded by this loader
	 */
	String loadStringResource(Component< ? > component, String key);
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11349.java