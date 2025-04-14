error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4588.java
text:
```scala
f@@or (final Field field : fClass.getJavaClass().getFields()) {

/**
 * 
 */
package org.junit.experimental.theories.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class AllMembersSupplier extends ParameterSupplier {
	static class MethodParameterValue extends PotentialAssignment {
		private final Method fMethod;

		private MethodParameterValue(Method method) {
			fMethod= method;
		}

		@Override
		public Object getValue() throws CouldNotGenerateValueException {
			try {
				return fMethod.invoke(null);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(
						"unexpected: argument length is checked");
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"unexpected: getMethods returned an inaccessible method");
			} catch (InvocationTargetException e) {
				throw new CouldNotGenerateValueException();
				// do nothing, just look for more values
			}
		}

		@Override
		public String getDescription() throws CouldNotGenerateValueException {
			return fMethod.getName();
		}
	}

	private final TestClass fClass;

	public AllMembersSupplier(TestClass type) {
		fClass= type;
	}

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
		List<PotentialAssignment> list= new ArrayList<PotentialAssignment>();

		addFields(sig, list);
		addSinglePointMethods(sig, list);
		addMultiPointMethods(list);

		return list;
	}

	private void addMultiPointMethods(List<PotentialAssignment> list) {
		for (FrameworkMethod dataPointsMethod : fClass
				.getAnnotatedMethods(DataPoints.class))
			try {
				addArrayValues(dataPointsMethod.getName(), list, dataPointsMethod.invokeExplosively(null));
			} catch (Throwable e) {
				// ignore and move on
			}
	}

	private void addSinglePointMethods(ParameterSignature sig,
			List<PotentialAssignment> list) {
		for (FrameworkMethod dataPointMethod : fClass
				.getAnnotatedMethods(DataPoint.class)) {
			Class<?> type= sig.getType();
			if ((dataPointMethod.producesType(type)))
				list.add(new MethodParameterValue(dataPointMethod.getMethod()));
		}
	}

	private void addFields(ParameterSignature sig,
			List<PotentialAssignment> list) {
		for (final Field field : fClass.getFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				Class<?> type= field.getType();
				if (sig.canAcceptArrayType(type)
						&& field.getAnnotation(DataPoints.class) != null) {
					addArrayValues(field.getName(), list, getStaticFieldValue(field));
				} else if (sig.canAcceptType(type)) {
					list.add(PotentialAssignment
							.forValue(field.getName(), getStaticFieldValue(field)));
				}
			}
		}
	}

	private void addArrayValues(String name, List<PotentialAssignment> list, Object array) {
		for (int i= 0; i < Array.getLength(array); i++)
			list.add(PotentialAssignment.forValue(name + "[" + i + "]", Array.get(array, i)));
	}

	private Object getStaticFieldValue(final Field field) {
		try {
			return field.get(null);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"unexpected: field from getClass doesn't exist on object");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"unexpected: getFields returned an inaccessible field");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4588.java