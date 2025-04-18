error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9256.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9256.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9256.java
text:
```scala
t@@his.scriptClass = (Class<?>) result;

/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.scripting.bsh;

import java.io.IOException;

import bsh.EvalError;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.scripting.ScriptCompilationException;
import org.springframework.scripting.ScriptFactory;
import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link org.springframework.scripting.ScriptFactory} implementation
 * for a BeanShell script.
 *
 * <p>Typically used in combination with a
 * {@link org.springframework.scripting.support.ScriptFactoryPostProcessor};
 * see the latter's javadoc for a configuration example.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 2.0
 * @see BshScriptUtils
 * @see org.springframework.scripting.support.ScriptFactoryPostProcessor
 */
public class BshScriptFactory implements ScriptFactory, BeanClassLoaderAware {

	private final String scriptSourceLocator;

	private final Class<?>[] scriptInterfaces;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	private Class<?> scriptClass;

	private final Object scriptClassMonitor = new Object();

	private boolean wasModifiedForTypeCheck = false;


	/**
	 * Create a new BshScriptFactory for the given script source.
	 * <p>With this {@code BshScriptFactory} variant, the script needs to
	 * declare a full class or return an actual instance of the scripted object.
	 * @param scriptSourceLocator a locator that points to the source of the script.
	 * Interpreted by the post-processor that actually creates the script.
	 */
	public BshScriptFactory(String scriptSourceLocator) {
		Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
		this.scriptSourceLocator = scriptSourceLocator;
		this.scriptInterfaces = null;
	}

	/**
	 * Create a new BshScriptFactory for the given script source.
	 * <p>The script may either be a simple script that needs a corresponding proxy
	 * generated (implementing the specified interfaces), or declare a full class
	 * or return an actual instance of the scripted object (in which case the
	 * specified interfaces, if any, need to be implemented by that class/instance).
	 * @param scriptSourceLocator a locator that points to the source of the script.
	 * Interpreted by the post-processor that actually creates the script.
	 * @param scriptInterfaces the Java interfaces that the scripted object
	 * is supposed to implement (may be {@code null})
	 */
	public BshScriptFactory(String scriptSourceLocator, Class<?>... scriptInterfaces) {
		Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
		this.scriptSourceLocator = scriptSourceLocator;
		this.scriptInterfaces = scriptInterfaces;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}


	@Override
	public String getScriptSourceLocator() {
		return this.scriptSourceLocator;
	}

	@Override
	public Class<?>[] getScriptInterfaces() {
		return this.scriptInterfaces;
	}

	/**
	 * BeanShell scripts do require a config interface.
	 */
	@Override
	public boolean requiresConfigInterface() {
		return true;
	}

	/**
	 * Load and parse the BeanShell script via {@link BshScriptUtils}.
	 * @see BshScriptUtils#createBshObject(String, Class[], ClassLoader)
	 */
	@Override
	public Object getScriptedObject(ScriptSource scriptSource, Class<?>... actualInterfaces)
			throws IOException, ScriptCompilationException {

		try {
			Class<?> clazz;

			synchronized (this.scriptClassMonitor) {
				boolean requiresScriptEvaluation = (this.wasModifiedForTypeCheck && this.scriptClass == null);
				this.wasModifiedForTypeCheck = false;

				if (scriptSource.isModified() || requiresScriptEvaluation) {
					// New script content: Let's check whether it evaluates to a Class.
					Object result = BshScriptUtils.evaluateBshScript(
							scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
					if (result instanceof Class) {
						// A Class: We'll cache the Class here and create an instance
						// outside of the synchronized block.
						this.scriptClass = (Class) result;
					}
					else {
						// Not a Class: OK, we'll simply create BeanShell objects
						// through evaluating the script for every call later on.
						// For this first-time check, let's simply return the
						// already evaluated object.
						return result;
					}
				}
				clazz = this.scriptClass;
			}

			if (clazz != null) {
				// A Class: We need to create an instance for every call.
				try {
					return clazz.newInstance();
				}
				catch (Throwable ex) {
					throw new ScriptCompilationException(
							scriptSource, "Could not instantiate script class: " + clazz.getName(), ex);
				}
			}
			else {
				// Not a Class: We need to evaluate the script for every call.
				return BshScriptUtils.createBshObject(
						scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
			}
		}
		catch (EvalError ex) {
			throw new ScriptCompilationException(scriptSource, ex);
		}
	}

	@Override
	public Class<?> getScriptedObjectType(ScriptSource scriptSource)
			throws IOException, ScriptCompilationException {

		try {
			synchronized (this.scriptClassMonitor) {
				if (scriptSource.isModified()) {
					// New script content: Let's check whether it evaluates to a Class.
					this.wasModifiedForTypeCheck = true;
					this.scriptClass = BshScriptUtils.determineBshObjectType(
							scriptSource.getScriptAsString(), this.beanClassLoader);
				}
				return this.scriptClass;
			}
		}
		catch (EvalError ex) {
			throw new ScriptCompilationException(scriptSource, ex);
		}
	}

	@Override
	public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource) {
		synchronized (this.scriptClassMonitor) {
			return (scriptSource.isModified() || this.wasModifiedForTypeCheck);
		}
	}


	@Override
	public String toString() {
		return "BshScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9256.java