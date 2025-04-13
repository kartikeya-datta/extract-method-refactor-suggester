error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4061.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4061.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4061.java
text:
```scala
i@@f (!name.equals(name)) return false;

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

package com.badlogic.gwtref.client;

import java.util.Arrays;

/** Describes a method of a {@link Type}.
 * @author mzechner */
public class Method {
	final String name;
	final Class enclosingType;
	final Class returnType;
	final boolean isAbstract;
	final boolean isFinal;
	final boolean isStatic;
	final boolean isNative;
	final boolean isDefaultAccess;
	final boolean isPrivate;
	final boolean isProtected;
	final boolean isPublic;
	final boolean isVarArgs;
	final boolean isMethod;
	final boolean isConstructor;
	final Parameter[] parameters;
	final String methodId;
	boolean accessible;

	public Method (String name, Class enclosingType, Class returnType, Parameter[] parameters, boolean isAbstract,
		boolean isFinal, boolean isStatic, boolean isDefaultAccess, boolean isPrivate, boolean isProtected, boolean isPublic,
		boolean isNative, boolean isVarArgs, boolean isMethod, boolean isConstructor, String methodId) {
		this.name = name;
		this.enclosingType = enclosingType;
		this.parameters = parameters;
		this.returnType = returnType;
		this.isAbstract = isAbstract;
		this.isFinal = isFinal;
		this.isStatic = isStatic;
		this.isNative = isNative;
		this.isDefaultAccess = isDefaultAccess;
		this.isPrivate = isPrivate;
		this.isProtected = isProtected;
		this.isPublic = isPublic;
		this.isVarArgs = isVarArgs;
		this.isMethod = isMethod;
		this.isConstructor = isConstructor;
		this.methodId = methodId;
	}

	public boolean isAccessible () {
		return accessible;
	}

	public void setAccessible (boolean accessible) {
		this.accessible = accessible;
	}

	/** @return the {@link Class} of the enclosing type. */
	public Class getEnclosingType () {
		return enclosingType;
	}

	/** @return the {@link Class} of the return type or null. */
	public Class getReturnType () {
		return returnType;
	}

	/** @return the list of parameters, can be a zero size array. */
	public Parameter[] getParameters () {
		return parameters;
	}

	/** @return the name of the method. */
	public String getName () {
		return name;
	}

	public boolean isAbstract () {
		return isAbstract;
	}

	public boolean isFinal () {
		return isFinal;
	}

	public boolean isDefaultAccess () {
		return isDefaultAccess;
	}

	public boolean isPrivate () {
		return isPrivate;
	}

	public boolean isProtected () {
		return isProtected;
	}

	public boolean isPublic () {
		return isPublic;
	}

	public boolean isNative () {
		return isNative;
	}

	public boolean isVarArgs () {
		return isVarArgs;
	}

	public boolean isStatic () {
		return isStatic;
	}

	public boolean isMethod () {
		return isMethod;
	}

	public boolean isConstructor () {
		return isConstructor;
	}

	/** Invokes the method on the given object. Ignores the object if this is a static method. Throws an IllegalArgumentException if
	 * the parameters do not match.
	 * @param obj the object to invoke the method on or null.
	 * @param params the parameters to pass to the method or null.
	 * @return the return value or null if the method does not return anything. */
	public Object invoke (Object obj, Object... params) {
		if (parameters != null && (params == null || params.length != parameters.length))
			throw new IllegalArgumentException("Parameter mismatch");
		if (parameters == null && params != null && params.length > 0) {
			throw new IllegalArgumentException("Parameter mismatch");
		}
		return ReflectionCache.instance.invoke(this, obj, params);
	}

	boolean match (String name, Class... types) {
		if (!this.name.equals(name)) return false;
		if (types.length != parameters.length) return false;
		for (int i = 0; i < types.length; i++) {
			Type t1 = ReflectionCache.instance.forName(parameters[i].getType().getName());
			Type t2 = ReflectionCache.instance.forName(types[i].getName());
			if (t1 != t2 && !t1.isAssignableFrom(t2)) return false;
		}
		return true;
	}

	@Override
	public String toString () {
		return "Method [name=" + name + ", enclosingType=" + enclosingType + ", returnType=" + returnType + ", isAbstract="
			+ isAbstract + ", isFinal=" + isFinal + ", isStatic=" + isStatic + ", isNative=" + isNative + ", isDefaultAccess="
			+ isDefaultAccess + ", isPrivate=" + isPrivate + ", isProtected=" + isProtected + ", isPublic=" + isPublic
			+ ", isVarArgs=" + isVarArgs + ", isMethod=" + isMethod + ", isConstructor=" + isConstructor + ", parameters="
			+ Arrays.toString(parameters) + ", accessible=" + accessible + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4061.java