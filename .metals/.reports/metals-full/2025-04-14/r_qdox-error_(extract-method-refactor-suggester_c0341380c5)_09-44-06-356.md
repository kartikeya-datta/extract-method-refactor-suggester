error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3616.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3616.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3616.java
text:
```scala
r@@eturn c2Type.isAssignableFrom(c1Type);

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

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gwtref.client.ReflectionCache;
import com.badlogic.gwtref.client.Type;

/** Utilities for Class reflection.
 * @author nexsoftware */
public final class ClassReflection {

	/** Returns the Class object associated with the class or interface with the supplied string name. */
	static public Class forName (String name) throws ReflectionException {
		try {
			return ReflectionCache.forName(name).getClassOfType();
		} catch (ClassNotFoundException e) {
			throw new ReflectionException("Class not found: " + name);
		}
	}

	/** Returns the simple name of the underlying class as supplied in the source code. */
	static public String getSimpleName(Class c) {
		return c.getName();
	}
	
	/** Determines if the supplied Object is assignment-compatible with the object represented by supplied Class. */
	static public boolean isInstance (Class c, Object obj) {
		return isAssignableFrom(c, obj.getClass());
	}

	/** Determines if the class or interface represented by first Class parameter is either the same as, or is a superclass or
	 * superinterface of, the class or interface represented by the second Class parameter. */	
	static public boolean isAssignableFrom (Class c1, Class c2) {
		Type c1Type = ReflectionCache.getType(c1);
		Type c2Type = ReflectionCache.getType(c2);
		return c1Type.isAssignableFrom(c2Type);
	}

	/** Returns true if the class or interface represented by the supplied Class is a member class. */
	static public boolean isMemberClass (Class c) {
		return ReflectionCache.getType(c).isMemberClass();
	}

	/** Returns true if the class or interface represented by the supplied Class is a static class. */
	static public boolean isStaticClass (Class c) {
		return ReflectionCache.getType(c).isStatic();
	}

	/** Creates a new instance of the class represented by the supplied Class. */
	static public <T> T newInstance (Class<T> c) throws ReflectionException {
		return (T)ReflectionCache.getType(c).newInstance();
	}

	/** Returns an array of {@link Constructor} containing the public constructors of the class represented by the supplied Class. */
	static public Constructor[] getConstructors (Class c) {
		throw new GdxRuntimeException("Not implemented.");
	}

	/** Returns a {@link Constructor} that represents the public constructor for the supplied class which takes the supplied parameter types. */
	static public Constructor getConstructor (Class c, Class... parameterTypes) throws ReflectionException {
		throw new GdxRuntimeException("Not implemented.");
	}

	/** Returns a {@link Constructor} that represents the constructor for the supplied class which takes the supplied parameter types. */
	static public Constructor getDeclaredConstructor (Class c, Class... parameterTypes) throws ReflectionException {
		throw new GdxRuntimeException("Not implemented.");
	}

	/** Returns an array of {@link Method} containing the public member methods of the class represented by the supplied Class. */
	static public Method[] getMethods (Class c) {
		com.badlogic.gwtref.client.Method[] methods = ReflectionCache.getType(c).getMethods();
		Method[] result = new Method[methods.length];
		for (int i = 0, j = methods.length; i < j; i++) {
			result[i] = new Method(methods[i]);
		}
		return result;
	}

	/** Returns a {@link Method} that represents the public member method for the supplied class which takes the supplied parameter types. */
	static public Method getMethod (Class c, String name, Class... parameterTypes) throws ReflectionException {
		try {
			return new Method(ReflectionCache.getType(c).getMethod(name, parameterTypes));
		} catch (SecurityException e) {
			throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
		}
	}

	/** Returns an array of {@link Method} containing the methods declared by the class represented by the supplied Class. */
	static public Method[] getDeclaredMethods (Class c) {
		com.badlogic.gwtref.client.Method[] methods = ReflectionCache.getType(c).getDeclaredMethods();
		Method[] result = new Method[methods.length];
		for (int i = 0, j = methods.length; i < j; i++) {
			result[i] = new Method(methods[i]);
		}
		return result;
	}

	/** Returns a {@link Method} that represents the method declared by the supplied class which takes the supplied parameter types. */
	static public Method getDeclaredMethod (Class c, String name, Class... parameterTypes) throws ReflectionException {
		try {
			return new Method(ReflectionCache.getType(c).getMethod(name, parameterTypes));
		} catch (SecurityException e) {
			throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
		}
	}

	/** Returns an array of {@link Field} containing the public fields of the class represented by the supplied Class. */
	static public Field[] getFields (Class c) {
		com.badlogic.gwtref.client.Field[] fields = ReflectionCache.getType(c).getFields();
		Field[] result = new Field[fields.length];
		for (int i = 0, j = fields.length; i < j; i++) {
			result[i] = new Field(fields[i]);
		}
		return result;
	}

	/** Returns a {@link Field} that represents the specified public member field for the supplied class. */
	static public Field getField (Class c, String name) throws ReflectionException {
		try {
			return new Field(ReflectionCache.getType(c).getField(name));
		} catch (SecurityException e) {
			throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
		}
	}

	/** Returns an array of {@link Field} objects reflecting all the fields declared by the supplied class. */
	static public Field[] getDeclaredFields (Class c) {
		com.badlogic.gwtref.client.Field[] fields = ReflectionCache.getType(c).getDeclaredFields();
		Field[] result = new Field[fields.length];
		for (int i = 0, j = fields.length; i < j; i++) {
			result[i] = new Field(fields[i]);
		}
		return result;
	}

	/** Returns a {@link Field} that represents the specified declared field for the supplied class. */
	static public Field getDeclaredField (Class c, String name) throws ReflectionException {
		try {
			return new Field(ReflectionCache.getType(c).getField(name));
		} catch (SecurityException e) {
			throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3616.java