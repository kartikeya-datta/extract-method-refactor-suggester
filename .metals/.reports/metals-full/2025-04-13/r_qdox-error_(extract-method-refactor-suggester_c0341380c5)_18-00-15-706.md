error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/858.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/858.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/858.java
text:
```scala
p@@arameterTypes[i] = parameters[i].getType();

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.utils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.badlogic.gwtref.client.Parameter;

/** Provides information about, and access to, a single method on a class or interface.
 * @author nexsoftware */
public final class Method {

	private final com.badlogic.gwtref.client.Method method;

	Method (com.badlogic.gwtref.client.Method method) {
		this.method = method;
	}

	/** Returns the name of the method. */
	public String getName () {
		return method.getName();
	}

	/** Returns a Class object that represents the formal return type of the method. */
	public Class getReturnType () {
		return method.getReturnType();
	}

	/** Returns an array of Class objects that represent the formal parameter types, in declaration order, of the method. */
	public Class[] getParameterTypes () {
		Parameter[] parameters = method.getParameters();
		Class[] parameterTypes = new Class[parameters.length];
		for (int i = 0, j = parameters.length; i < j; i++) {
			parameterTypes[i] = parameters[i].getClazz();
		}
		return parameterTypes;
	}

	/** Returns the Class object representing the class or interface that declares the method. */
	public Class getDeclaringClass () {
		return method.getEnclosingType();
	}

	public boolean isAccessible () {
		return method.isPublic();
	}

	public void setAccessible (boolean accessible) {
		// NOOP in GWT
	}

	/** Return true if the method includes the {@code abstract} modifier. */
	public boolean isAbstract () {
		return method.isAbstract();
	}

	/** Return true if the method does not include any of the {@code private}, {@code protected}, or {@code public} modifiers. */
	public boolean isDefaultAccess () {
		return !isPrivate() && !isProtected() && !isPublic();
	}

	/** Return true if the method includes the {@code final} modifier. */
	public boolean isFinal () {
		return method.isFinal();
	}

	/** Return true if the method includes the {@code private} modifier. */
	public boolean isPrivate () {
		return method.isPrivate();
	}

	/** Return true if the method includes the {@code protected} modifier. */
	public boolean isProtected () {
		return method.isProtected();
	}

	/** Return true if the method includes the {@code public} modifier. */
	public boolean isPublic () {
		return method.isPublic();
	}

	/** Return true if the method includes the {@code native} modifier. */
	public boolean isNative () {
		return method.isNative();
	}

	/** Return true if the method includes the {@code static} modifier. */
	public boolean isStatic () {
		return method.isStatic();
	}

	/** Return true if the method takes a variable number of arguments. */
	public boolean isVarArgs () {
		return method.isVarArgs();
	}

	/** Invokes the underlying method on the supplied object with the supplied parameters. */
	public Object invoke (Object obj, Object... args) throws ReflectionException {
		try {
			return method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Illegal argument(s) supplied to method: " + getName(), e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/858.java