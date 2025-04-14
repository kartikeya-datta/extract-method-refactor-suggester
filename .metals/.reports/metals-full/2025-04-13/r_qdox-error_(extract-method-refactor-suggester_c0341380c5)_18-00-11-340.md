error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9254.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9254.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9254.java
text:
```scala
protected C@@lass<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex)

/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.remoting.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.server.RMIClassLoader;

import org.springframework.core.ConfigurableObjectInputStream;

/**
 * Special ObjectInputStream subclass that falls back to a specified codebase
 * to load classes from if not found locally. In contrast to standard RMI
 * conventions for dynamic class download, it is the client that determines
 * the codebase URL here, rather than the "java.rmi.server.codebase" system
 * property on the server.
 *
 * <p>Uses the JDK's RMIClassLoader to load classes from the specified codebase.
 * The codebase can consist of multiple URLs, separated by spaces.
 * Note that RMIClassLoader requires a SecurityManager to be set, like when
 * using dynamic class download with standard RMI! (See the RMI documentation
 * for details.)
 *
 * <p>Despite residing in the RMI package, this class is <i>not</i> used for
 * RmiClientInterceptor, which uses the standard RMI infrastructure instead
 * and thus is only able to rely on RMI's standard dynamic class download via
 * "java.rmi.server.codebase". CodebaseAwareObjectInputStream is used by
 * HttpInvokerClientInterceptor (see the "codebaseUrl" property there).
 *
 * <p>Thanks to Lionel Mestre for suggesting the option and providing
 * a prototype!
 *
 * @author Juergen Hoeller
 * @since 1.1.3
 * @see java.rmi.server.RMIClassLoader
 * @see RemoteInvocationSerializingExporter#createObjectInputStream
 * @see org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor#setCodebaseUrl
 */
public class CodebaseAwareObjectInputStream extends ConfigurableObjectInputStream {

	private final String codebaseUrl;


	/**
	 * Create a new CodebaseAwareObjectInputStream for the given InputStream and codebase.
	 * @param in the InputStream to read from
	 * @param codebaseUrl the codebase URL to load classes from if not found locally
	 * (can consist of multiple URLs, separated by spaces)
	 * @see java.io.ObjectInputStream#ObjectInputStream(java.io.InputStream)
	 */
	public CodebaseAwareObjectInputStream(InputStream in, String codebaseUrl) throws IOException {
		this(in, null, codebaseUrl);
	}

	/**
	 * Create a new CodebaseAwareObjectInputStream for the given InputStream and codebase.
	 * @param in the InputStream to read from
	 * @param classLoader the ClassLoader to use for loading local classes
	 * (may be {@code null} to indicate RMI's default ClassLoader)
	 * @param codebaseUrl the codebase URL to load classes from if not found locally
	 * (can consist of multiple URLs, separated by spaces)
	 * @see java.io.ObjectInputStream#ObjectInputStream(java.io.InputStream)
	 */
	public CodebaseAwareObjectInputStream(
			InputStream in, ClassLoader classLoader, String codebaseUrl) throws IOException {

		super(in, classLoader);
		this.codebaseUrl = codebaseUrl;
	}

	/**
	 * Create a new CodebaseAwareObjectInputStream for the given InputStream and codebase.
	 * @param in the InputStream to read from
	 * @param classLoader the ClassLoader to use for loading local classes
	 * (may be {@code null} to indicate RMI's default ClassLoader)
	 * @param acceptProxyClasses whether to accept deserialization of proxy classes
	 * (may be deactivated as a security measure)
	 * @see java.io.ObjectInputStream#ObjectInputStream(java.io.InputStream)
	 */
	public CodebaseAwareObjectInputStream(
			InputStream in, ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {

		super(in, classLoader, acceptProxyClasses);
		this.codebaseUrl = null;
	}


	@Override
	protected Class resolveFallbackIfPossible(String className, ClassNotFoundException ex)
			throws IOException, ClassNotFoundException {

		// If codebaseUrl is set, try to load the class with the RMIClassLoader.
		// Else, propagate the ClassNotFoundException.
		if (this.codebaseUrl == null) {
			throw ex;
		}
		return RMIClassLoader.loadClass(this.codebaseUrl, className);
	}

	@Override
	protected ClassLoader getFallbackClassLoader() throws IOException {
		return RMIClassLoader.getClassLoader(this.codebaseUrl);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9254.java