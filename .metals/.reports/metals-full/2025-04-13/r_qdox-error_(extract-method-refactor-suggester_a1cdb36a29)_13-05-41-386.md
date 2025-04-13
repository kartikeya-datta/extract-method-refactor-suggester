error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9242.java
text:
```scala
n@@ew Class<?>[] { this.translatorClass }, adapter);

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
package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Reflective wrapper around a JBoss 5 and 6 class loader methods (discovered and called
 * through reflection) for load time weaving.
 *
 * @author Costin Leau
 * @since 3.1
 */
class JBossMCAdapter implements JBossClassLoaderAdapter {

	private static final String TRANSLATOR_NAME = "org.jboss.util.loading.Translator";
	private static final String POLICY_NAME = "org.jboss.classloader.spi.base.BaseClassLoaderPolicy";
	private static final String DOMAIN_NAME = "org.jboss.classloader.spi.base.BaseClassLoaderDomain";
	private static final String DEDICATED_SYSTEM = "org.jboss.classloader.spi.ClassLoaderSystem";
	private static final String LOADER_NAME = "org.jboss.classloader.spi.base.BaseClassLoader";
	private static final String GET_POLICY = "getPolicy";
	private static final String GET_DOMAIN = "getClassLoaderDomain";
	private static final String GET_SYSTEM = "getClassLoaderSystem";

	// available since JBoss AS 5.1.0 / MC 2.0.6 (allows multiple transformers to be added)
	private static final String ADD_TRANSLATOR_NAME = "addTranslator";
	// available since JBoss AS 5.0.0 / MC 2.0.1 (allows only one transformer to be added)
	private static final String SET_TRANSLATOR_NAME = "setTranslator";

	private final ClassLoader classLoader;
	private final Class<?> translatorClass;

	private final Method addTranslator;
	private final Object target;

	JBossMCAdapter(ClassLoader classLoader) {
		Class<?> clazzLoaderType = null;
		try {
			// resolve BaseClassLoader.class
			clazzLoaderType = classLoader.loadClass(LOADER_NAME);

			ClassLoader clazzLoader = null;
			// walk the hierarchy to detect the instrumentation aware classloader
			for (ClassLoader cl = classLoader; cl != null && clazzLoader == null; cl = cl.getParent()) {
				if (clazzLoaderType.isInstance(cl)) {
					clazzLoader = cl;
				}
			}

			if (clazzLoader == null) {
				throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: "
						+ "A [" + LOADER_NAME + "] implementation is required.");
			}

			this.classLoader = clazzLoader;
			// use the classloader that loaded the classloader to load
			// the types for reflection purposes
			classLoader = clazzLoader.getClass().getClassLoader();

			// BaseClassLoader#getPolicy
			Method method = clazzLoaderType.getDeclaredMethod(GET_POLICY);
			ReflectionUtils.makeAccessible(method);
			Object policy = method.invoke(this.classLoader);

			Object addTarget = null;
			Method addMethod = null;

			// try the 5.1.x hooks
			// check existence of BaseClassLoaderPolicy#addTranslator(Translator)
			this.translatorClass = classLoader.loadClass(TRANSLATOR_NAME);
			Class<?> clazz = classLoader.loadClass(POLICY_NAME);
			try {
				addMethod = clazz.getDeclaredMethod(ADD_TRANSLATOR_NAME, translatorClass);
				addTarget = policy;
			} catch (NoSuchMethodException ex) {
			}

			// fall back to 5.0.x method
			if (addMethod == null) {

				// BaseClassLoaderPolicy#getClassLoaderDomain
				method = clazz.getDeclaredMethod(GET_DOMAIN);
				ReflectionUtils.makeAccessible(method);
				Object domain = method.invoke(policy);

				// BaseClassLoaderDomain#getClassLoaderSystem
				clazz = classLoader.loadClass(DOMAIN_NAME);
				method = clazz.getDeclaredMethod(GET_SYSTEM);
				ReflectionUtils.makeAccessible(method);
				Object system = method.invoke(domain);

				// resolve ClassLoaderSystem
				clazz = classLoader.loadClass(DEDICATED_SYSTEM);
				Assert.isInstanceOf(clazz, system, "JBoss LoadTimeWeaver requires JBoss loader system of type "
						+ clazz.getName() + " on JBoss 5.0.x");

				// ClassLoaderSystem#setTranslator
				addMethod = clazz.getDeclaredMethod(SET_TRANSLATOR_NAME, translatorClass);
				addTarget = system;
			}

			this.addTranslator = addMethod;
			this.target = addTarget;

		} catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize JBoss LoadTimeWeaver because the JBoss 5 API classes are not available", ex);
		}
	}

	@Override
	public void addTransformer(ClassFileTransformer transformer) {
		InvocationHandler adapter = new JBossMCTranslatorAdapter(transformer);
		Object adapterInstance = Proxy.newProxyInstance(this.translatorClass.getClassLoader(),
				new Class[] { this.translatorClass }, adapter);

		try {
			addTranslator.invoke(target, adapterInstance);
		} catch (Exception ex) {
			throw new IllegalStateException("Could not add transformer on JBoss 5/6 classloader " + classLoader, ex);
		}
	}

	@Override
	public ClassLoader getInstrumentableClassLoader() {
		return classLoader;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9242.java