error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12135.java
text:
```scala
r@@egister(new QualifiedName(mtd.getName()), new JavaOperation(mtd, paramTypes, ts.findType (mtd.getReturnType()), null));

/*
Copyright (c) 2008 Arno Haase, André Arnold.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
    André Arnold
 */
package org.eclipse.xtend.backend.types.java.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xtend.backend.common.BackendType;
import org.eclipse.xtend.backend.common.BackendTypesystem;
import org.eclipse.xtend.backend.common.QualifiedName;
import org.eclipse.xtend.backend.functions.java.internal.JavaBuiltinConverterFactory;
import org.eclipse.xtend.backend.types.AbstractType;
import org.eclipse.xtend.backend.util.ErrorHandler;

/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public final class JavaBeansType extends AbstractType {
	private final Class<?> _javaClass;

	public JavaBeansType(Class<?> javaCls, BackendTypesystem ts) {
		super(javaCls.getName().replace(".", "::"), AbstractJavaBeansTypesystem.UNIQUE_REPRESENTATION_PREFIX
				+ javaCls.getName(), superTypes(javaCls, ts));

		_javaClass = javaCls;
	}

	private static BackendType[] superTypes(Class<?> javaCls, BackendTypesystem ts) {
		final List<Class<?>> resultRaw = new ArrayList<Class<?>>(Arrays.<Class<?>> asList(javaCls.getInterfaces()));
		Class<?> superClass = javaCls.getSuperclass();
		if (superClass != null)
			resultRaw.add(superClass);

		final List<BackendType> result = new ArrayList<BackendType>();
		for (Class<?> cls : resultRaw)
			result.add(ts.getRootTypesystem().findType(cls));

		return result.toArray(new BackendType[result.size()]);
	}

	/**
	 * the actual initialization is separated to deal with circular dependencies
	 * of operations and/or properties referring to this very same type.
	 */
	void init(BackendTypesystem ts) {
		for (Method mtd : _javaClass.getMethods()) {
			if (mtd.getDeclaringClass() == Object.class) // toString is added as
				// a syslib function
				continue;

			final List<BackendType> paramTypes = new ArrayList<BackendType>();

			paramTypes.add(this); // first parameter is the object on which the
			// method is called
			for (Class<?> cls : mtd.getParameterTypes()) {
				paramTypes.add(ts.getRootTypesystem().findType(cls));
			}

			register(new QualifiedName(mtd.getName()), new JavaOperation(mtd, paramTypes, null));
		}

		// static properties
		for (Field field : _javaClass.getFields()) {
			final int mod = field.getModifiers();
			if (Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
				try {
					register(new JavaBeansStaticProperty(field, this, ts.getRootTypesystem().findType(field.getType()),
							JavaBuiltinConverterFactory.getConverter(field.getType())));
				}
				catch (Exception e) {
					ErrorHandler.handle(e);
				}
			}
		}

		// Java 5 enums
		final Object[] enumValues = _javaClass.getEnumConstants();
		if (enumValues != null) {
			for (Object o : enumValues) {
				final Enum<?> curEnum = (Enum<?>) o;
				register(new JavaBeansStaticProperty(this, ts.getRootTypesystem().findType(curEnum), curEnum.name(),
						curEnum));
			}
		}
	}

	@Override
	public Object create() {
		try {
			return _javaClass.newInstance();
		}
		catch (Exception e) {
			ErrorHandler.handle(e);
			return null; // to make the compiler happy - this is never executed
		}
	}

	@Override
	public String toString() {
		return "JavaBeansType[" + _javaClass.getName() + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof JavaBeansType))
			return false;

		return ((JavaBeansType) other)._javaClass.equals(_javaClass);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12135.java