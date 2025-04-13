error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7969.java
text:
```scala
C@@lassLoader loader = cloneConstructor.newInstance(getClassLoader());

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

package org.springframework.instrument.classloading.websphere;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.util.Assert;

/**
 *
 * Reflective wrapper around a WebSphere 7 class loader. Used to
 * encapsulate the classloader-specific methods (discovered and
 * called through reflection) from the load-time weaver.
 *
 * @author Costin Leau
 * @since 3.1
 */
class WebSphereClassLoaderAdapter {

	private static final String COMPOUND_CLASS_LOADER_NAME = "com.ibm.ws.classloader.CompoundClassLoader";
	private static final String CLASS_PRE_PROCESSOR_NAME = "com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin";
	private static final String PLUGINS_FIELD = "preDefinePlugins";

	private ClassLoader classLoader;
	private Class<?> wsPreProcessorClass;
	private Method addPreDefinePlugin;
	private Constructor<? extends ClassLoader> cloneConstructor;
	private Field transformerList;

	public WebSphereClassLoaderAdapter(ClassLoader classLoader) {
		Class<?> wsCompoundClassLoaderClass = null;
		try {
			wsCompoundClassLoaderClass = classLoader.loadClass(COMPOUND_CLASS_LOADER_NAME);
			cloneConstructor = classLoader.getClass().getDeclaredConstructor(wsCompoundClassLoaderClass);
			cloneConstructor.setAccessible(true);

			wsPreProcessorClass = classLoader.loadClass(CLASS_PRE_PROCESSOR_NAME);
			addPreDefinePlugin = classLoader.getClass().getMethod("addPreDefinePlugin", wsPreProcessorClass);
			transformerList = wsCompoundClassLoaderClass.getDeclaredField(PLUGINS_FIELD);
			transformerList.setAccessible(true);
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize WebSphere LoadTimeWeaver because WebSphere 7 API classes are not available",
					ex);
		}
		Assert.isInstanceOf(wsCompoundClassLoaderClass, classLoader,
				"ClassLoader must be instance of [" + COMPOUND_CLASS_LOADER_NAME + "]");
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public void addTransformer(ClassFileTransformer transformer) {
		Assert.notNull(transformer, "ClassFileTransformer must not be null");
		try {
			InvocationHandler adapter = new WebSphereClassPreDefinePlugin(transformer);
			Object adapterInstance = Proxy.newProxyInstance(this.wsPreProcessorClass.getClassLoader(),
					new Class[] { this.wsPreProcessorClass }, adapter);
			this.addPreDefinePlugin.invoke(this.classLoader, adapterInstance);

		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("WebSphere addPreDefinePlugin method threw exception", ex.getCause());
		}
		catch (Exception ex) {
			throw new IllegalStateException("Could not invoke WebSphere addPreDefinePlugin method", ex);
		}
	}

	public ClassLoader getThrowawayClassLoader() {
		try {
			ClassLoader loader = (ClassLoader) cloneConstructor.newInstance(getClassLoader());
			// clear out the transformers (copied as well)
			List<?> list = (List<?>) transformerList.get(loader);
			list.clear();
			return loader;
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("WebSphere CompoundClassLoader constructor failed", ex.getCause());
		}
		catch (Exception ex) {
			throw new IllegalStateException("Could not construct WebSphere CompoundClassLoader", ex);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7969.java